package mx.egd.fmre.register.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import mx.egd.fmre.register.exception.StorageException;
import mx.egd.fmre.register.exception.StorageFileNotFoundException;
import mx.egd.fmre.register.service.StorageService;
import mx.egd.fmre.register.util.MimeTypesUtil;
import mx.egd.fmre.register.util.exception.MimeTypesUtilException;

@Service
public class FileSystemStorageService implements StorageService {
    
    private static final String FAILED_TO_STORE_EMPTY_FILE = "Failed to store empty file.";
    private static final String CANT_STORE_FILE_OUTSITE_DIRECTORY= "Cannot store file outside current directory.";
    private static final String FAILED_TO_STORE_FILE = "Failed to store file.";
    private static final String CULD_NOT_READ_FILE = "Could not read file: ";

    //private final Path rootLocation = Paths.get("uploaded_files");
    @Value("${spring.files.location}")
    private String filesLocation;
    
    @Value("${spring.files.upload_path}")
    private String uploadPath;
    
    private String rootLocation;
    
    private static final  Tika TIKA = new Tika();
    
    @PostConstruct
    private void init() {
        this.rootLocation = filesLocation +  File.separator + uploadPath;
    }

    @Override
    public String store(MultipartFile file) throws StorageException {
        String fileName;
        try {
            if (file.isEmpty()) {
                throw new StorageException(FAILED_TO_STORE_EMPTY_FILE);
            }
            // Paths.get(file.getOriginalFilename())
            String uuid = UUID.randomUUID().toString();
            // Detect the MIME type (e.g., "image/jpeg")
            String extension = getExtension(file.getInputStream());
            
            fileName = uuid + extension;
            
            Path rootLocationPath = Paths.get(this.rootLocation);

            Path destinationFile = rootLocationPath.resolve(fileName).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(rootLocationPath.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(CANT_STORE_FILE_OUTSITE_DIRECTORY);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException(FAILED_TO_STORE_FILE, e);
        }
        return fileName;
    }
    
    @Override
    public String getExtension(InputStream is) throws StorageException {
        String detectedType;
        String extension;
        try {
            detectedType = getMimeType(is);
            extension = MimeTypesUtil.getExtension(detectedType);
        } catch (MimeTypesUtilException e) {
            throw new StorageException(e);
        }
        return extension;
    }
    
    @Override
    public String getMimeType(InputStream is) throws StorageException {
        try {
            return TIKA.detect(is);
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    /*
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }
    */

    @Override
    public Path load(String filename) {
        return Paths.get(this.rootLocation).resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) throws StorageException {
        try {
            Path path = load(filename);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(CULD_NOT_READ_FILE + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException(CULD_NOT_READ_FILE + filename, e);
        }
    }

    @Override
    public Resource loadAsResourceByUuid(String uuid) throws StorageException {
        String uuidPart = extractUuidPart(uuid);
        Path rootLocationPath = Paths.get(this.rootLocation).toAbsolutePath().normalize();

        Path requested = rootLocationPath.resolve(uuid).normalize().toAbsolutePath();
        if (requested.getParent().equals(rootLocationPath) && isReadableFile(requested)) {
            return toUrlResource(requested, uuid);
        }

        Path withoutExtension = rootLocationPath.resolve(uuidPart).normalize().toAbsolutePath();
        if (withoutExtension.getParent().equals(rootLocationPath) && isReadableFile(withoutExtension)) {
            return toUrlResource(withoutExtension, uuidPart);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(rootLocationPath, uuidPart + ".*")) {
            for (Path path : stream) {
                Path normalized = path.toAbsolutePath().normalize();
                if (normalized.getParent().equals(rootLocationPath) && isReadableFile(normalized)) {
                    return toUrlResource(normalized, path.getFileName().toString());
                }
            }
        } catch (IOException e) {
            throw new StorageException(CULD_NOT_READ_FILE + uuid, e);
        }

        throw new StorageFileNotFoundException(CULD_NOT_READ_FILE + uuid);
    }

    private String extractUuidPart(String uuid) throws StorageFileNotFoundException {
        if (uuid == null || uuid.isBlank()) {
            throw new StorageFileNotFoundException(CULD_NOT_READ_FILE + uuid);
        }
        String part = uuid.trim();
        if (part.contains("/") || part.contains("\\") || part.contains("..")) {
            throw new StorageFileNotFoundException(CULD_NOT_READ_FILE + uuid);
        }
        int dot = part.lastIndexOf('.');
        if (dot > 0) {
            part = part.substring(0, dot);
        }
        try {
            UUID.fromString(part);
        } catch (IllegalArgumentException e) {
            throw new StorageFileNotFoundException(CULD_NOT_READ_FILE + uuid, e);
        }
        return part;
    }

    private boolean isReadableFile(Path path) {
        return Files.isRegularFile(path) && Files.isReadable(path);
    }

    private Resource toUrlResource(Path file, String nameForError) throws StorageException {
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new StorageFileNotFoundException(CULD_NOT_READ_FILE + nameForError);
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException(CULD_NOT_READ_FILE + nameForError, e);
        }
    }

    /*
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
    */

    /*
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
    */
}