package mx.egd.fmre.register.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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

@Service
public class FileSystemStorageService implements StorageService {

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
    public String store(MultipartFile file) throws StorageException {
        String fileName;
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            // Paths.get(file.getOriginalFilename())
            String uuid = UUID.randomUUID().toString();
            Tika tika = new Tika();
            // Detect the MIME type (e.g., "image/jpeg")
            String detectedType = tika.detect(file.getInputStream());
            String extension = MimeTypesUtil.getExtension(detectedType);
            
            fileName = uuid + extension;
            
            Path rootLocationPath = Paths.get(this.rootLocation);

            Path destinationFile = rootLocationPath.resolve(fileName).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(rootLocationPath.toAbsolutePath())) {
                // This is a security check
                throw new StorageException("Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
        return fileName;
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
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
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