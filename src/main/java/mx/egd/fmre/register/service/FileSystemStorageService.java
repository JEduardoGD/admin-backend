package mx.egd.fmre.register.service;

import java.io.InputStream;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import mx.egd.fmre.register.exception.FileSystemStorageServiceException;

public interface FileSystemStorageService {

	String store(MultipartFile file) throws FileSystemStorageServiceException;

	String getExtension(InputStream is) throws FileSystemStorageServiceException;

	String getMimeType(InputStream is) throws FileSystemStorageServiceException;

	Path load(String filename);

	Resource loadAsResource(String filename) throws FileSystemStorageServiceException;

	Resource loadAsResourceByUuid(String uuid) throws FileSystemStorageServiceException;
}
