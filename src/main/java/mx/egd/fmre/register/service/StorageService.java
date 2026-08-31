package mx.egd.fmre.register.service;

import java.io.InputStream;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import mx.egd.fmre.register.exception.StorageException;

public interface StorageService {

    String store(MultipartFile file) throws StorageException;

    //Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename) throws StorageException;

    Resource loadAsResourceByUuid(String uuid) throws StorageException;

    String getExtension(InputStream is) throws StorageException;

    String getMimeType(InputStream is) throws StorageException;

    //void deleteAll();

    //void init();

}
