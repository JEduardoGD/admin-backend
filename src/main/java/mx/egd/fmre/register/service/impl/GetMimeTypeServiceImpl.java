package mx.egd.fmre.register.service.impl;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.exception.FileSystemStorageServiceException;
import mx.egd.fmre.register.service.FileSystemStorageService;
import mx.egd.fmre.register.service.GetMimeTypeService;
import mx.egd.fmre.register.service.exceptions.GetMimeTypeServiceException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetMimeTypeServiceImpl implements GetMimeTypeService {

	private final FileSystemStorageService storageService;

	@Override
	public String getDetectedType(String filename) throws GetMimeTypeServiceException {

		Resource resouce = null;
		try {
			resouce = storageService.loadAsResource(filename);
		} catch (FileSystemStorageServiceException e) {
			log.error(e.getMessage());
			throw new GetMimeTypeServiceException(e);
		}

		try (InputStream inputStream = resouce.getInputStream()) {
			return storageService.getMimeType(inputStream);
		} catch (FileSystemStorageServiceException e) {
			log.error(e.getMessage());
			throw new GetMimeTypeServiceException(e);
		} catch (IOException e1) {
			log.error(e1.getMessage());
			throw new GetMimeTypeServiceException(e1);
		}
	}
}
