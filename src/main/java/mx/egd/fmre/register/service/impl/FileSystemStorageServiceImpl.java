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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import mx.egd.fmre.register.exception.FileSystemStorageServiceException;
import mx.egd.fmre.register.service.FileSystemStorageService;
import mx.egd.fmre.register.util.FileInputUtil;
import mx.egd.fmre.register.util.exception.FileInputUtilException;

@Service
public class FileSystemStorageServiceImpl extends FileInputUtil implements FileSystemStorageService {
    
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
    
    @PostConstruct
    private void init() {
        this.rootLocation = filesLocation +  File.separator + uploadPath;
    }

    @Override
    public String store(MultipartFile file) throws FileSystemStorageServiceException {
        String fileName;
        try {
            if (file.isEmpty()) {
                throw new FileSystemStorageServiceException(FAILED_TO_STORE_EMPTY_FILE);
            }
            // Paths.get(file.getOriginalFilename())
            String uuid = UUID.randomUUID().toString();
            // Detect the MIME type (e.g., "image/jpeg")
            String extension;
            try {
                extension = getExtension(file.getInputStream());
            } catch (FileInputUtilException e) {
                throw new FileSystemStorageServiceException(e);
            }
            
            fileName = uuid + extension;
            
            Path rootLocationPath = Paths.get(this.rootLocation);

            Path destinationFile = rootLocationPath.resolve(fileName).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(rootLocationPath.toAbsolutePath())) {
                // This is a security check
                throw new FileSystemStorageServiceException(CANT_STORE_FILE_OUTSITE_DIRECTORY);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new FileSystemStorageServiceException(FAILED_TO_STORE_FILE, e);
        }
        return fileName;
    }
    
    /*
    @Override
    public String getExtension(InputStream is) throws FileSystemStorageServiceException {
        String detectedType;
        String extension;
        try {
            detectedType = getMimeType(is);
            extension = MimeTypesUtil.getExtension(detectedType);
        } catch (MimeTypesUtilException e) {
            throw new FileSystemStorageServiceException(e);
        }
        return extension;
    }
    
    @Override
    public String getMimeType(InputStream is) throws FileSystemStorageServiceException {
        try {
            return TIKA.detect(is);
        } catch (IOException e) {
            throw new FileSystemStorageServiceException(e);
        }
    }

    @Override
    public Path load(String filename) {
        return Paths.get(this.rootLocation).resolve(filename);
    }
    */

    @Override
    public Resource loadAsResource(String filename) throws FileSystemStorageServiceException {
        try {
            Path path = load(this.rootLocation, filename);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileSystemStorageServiceException(CULD_NOT_READ_FILE + filename);

            }
        } catch (MalformedURLException e) {
            throw new FileSystemStorageServiceException(CULD_NOT_READ_FILE + filename, e);
        }
    }

    @Override
    public Resource loadAsResourceByUuid(String uuid) throws FileSystemStorageServiceException {
        String uuidPart;
        try {
            uuidPart = extractUuidPart(uuid);
        } catch (FileInputUtilException e) {
            throw new FileSystemStorageServiceException(e);
        }
        Path rootLocationPath = Paths.get(this.rootLocation).toAbsolutePath().normalize();

        Path requested = rootLocationPath.resolve(uuid).normalize().toAbsolutePath();
        if (requested.getParent().equals(rootLocationPath) && isReadableFile(requested)) {
            try {
                return toUrlResource(requested, uuid);
            } catch (FileInputUtilException e) {
                throw new FileSystemStorageServiceException(e);
            }
        }

        Path withoutExtension = rootLocationPath.resolve(uuidPart).normalize().toAbsolutePath();
        if (withoutExtension.getParent().equals(rootLocationPath) && isReadableFile(withoutExtension)) {
            try {
                return toUrlResource(withoutExtension, uuidPart);
            } catch (FileInputUtilException e) {
                throw new FileSystemStorageServiceException(e);
            }
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(rootLocationPath, uuidPart + ".*")) {
            for (Path path : stream) {
                Path normalized = path.toAbsolutePath().normalize();
                if (normalized.getParent().equals(rootLocationPath) && isReadableFile(normalized)) {
                    return toUrlResource(normalized, path.getFileName().toString());
                }
            }
        } catch (IOException | FileInputUtilException e) {
            throw new FileSystemStorageServiceException(CULD_NOT_READ_FILE + uuid, e);
        }

        throw new FileSystemStorageServiceException(CULD_NOT_READ_FILE + uuid);
    }
}