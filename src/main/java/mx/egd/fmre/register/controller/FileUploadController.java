package mx.egd.fmre.register.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.exception.FileSystemStorageServiceException;
import mx.egd.fmre.register.record.UploadResult;
import mx.egd.fmre.register.service.FileSystemStorageService;
import mx.egd.fmre.register.service.GetMimeTypeService;
import mx.egd.fmre.register.service.exceptions.GetMimeTypeServiceException;

@RestController
@RequestMapping("file")
@AllArgsConstructor
@Slf4j
public class FileUploadController {

    private final FileSystemStorageService storageService;
    private final GetMimeTypeService getMimeTypeService;

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource resource = null;
		try {
		    resource = storageService.loadAsResource(filename);
		} catch (FileSystemStorageServiceException e) {
			log.error(e.getMessage());
		}

		if (resource == null) {
			return ResponseEntity.notFound().build();
		}

		String detectedType = null;
		try {
			detectedType = getMimeTypeService.getDetectedType(filename);
		} catch (GetMimeTypeServiceException e) {
			log.error(e.getMessage());
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, detectedType).body(resource);
	}

	@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<UploadResult> handleFileUpload(@RequestParam MultipartFile file) {

		String filename = null;
		try {
			filename = storageService.store(file);
		} catch (FileSystemStorageServiceException e) {
			log.error(e.getMessage());
		}

		UploadResult uploadResult = new UploadResult(filename, false, null);

		return new ResponseEntity<>(uploadResult, HttpStatus.OK);
	}

	@ExceptionHandler(FileSystemStorageServiceException.class)
	public ResponseEntity<UploadResult> handleStorageFileNotFound(FileSystemStorageServiceException exc) {
		return ResponseEntity.notFound().build();
	}

}