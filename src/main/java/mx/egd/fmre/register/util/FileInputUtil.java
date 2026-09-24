package mx.egd.fmre.register.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import mx.egd.fmre.register.util.exception.FileInputUtilException;
import mx.egd.fmre.register.util.exception.MimeTypesUtilException;

public abstract class FileInputUtil {
    
    private static final String CULD_NOT_READ_FILE = "Could not read file: ";

    private static final Tika TIKA = new Tika();

    protected String getUuid() {
        return UUID.randomUUID().toString();
    }

    protected String getExtension(InputStream is) throws FileInputUtilException {
        String detectedType;
        String extension;
        try {
            detectedType = getMimeType(is);
            extension = MimeTypesUtil.getExtension(detectedType);
        } catch (MimeTypesUtilException e) {
            throw new FileInputUtilException(e);
        }
        return extension;
    }

    protected String getMimeType(InputStream is) throws FileInputUtilException {
        try {
            return TIKA.detect(is);
        } catch (IOException e) {
            throw new FileInputUtilException(e);
        }
    }

    protected Path load(String rootLocation, String filename) {
        return Paths.get(rootLocation).resolve(filename);
    }

    protected String extractUuidPart(String uuid) throws FileInputUtilException {
        if (uuid == null || uuid.isBlank()) {
            throw new FileInputUtilException(CULD_NOT_READ_FILE + uuid);
        }
        String part = uuid.trim();
        if (part.contains("/") || part.contains("\\") || part.contains("..")) {
            throw new FileInputUtilException(CULD_NOT_READ_FILE + uuid);
        }
        int dot = part.lastIndexOf('.');
        if (dot > 0) {
            part = part.substring(0, dot);
        }
        try {
            UUID.fromString(part);
        } catch (IllegalArgumentException e) {
            throw new FileInputUtilException(CULD_NOT_READ_FILE + uuid, e);
        }
        return part;
    }

    protected boolean isReadableFile(Path path) {
        return Files.isRegularFile(path) && Files.isReadable(path);
    }

    protected Resource toUrlResource(Path file, String nameForError) throws FileInputUtilException {
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new FileInputUtilException(CULD_NOT_READ_FILE + nameForError);
        } catch (MalformedURLException e) {
            throw new FileInputUtilException(CULD_NOT_READ_FILE + nameForError, e);
        }
    }
}
