package mx.egd.fmre.register.controller;

import java.io.IOException;

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
import mx.egd.fmre.register.exception.StorageException;
import mx.egd.fmre.register.exception.StorageFileNotFoundException;
import mx.egd.fmre.register.record.UploadResult;
import mx.egd.fmre.register.service.StorageService;

@RestController
@RequestMapping("file")
@AllArgsConstructor
@Slf4j
public class FileUploadController {

    private final StorageService storageService;

    /*
    @GetMapping("/")
    public String listUploadedFiles(Model model) {

        model.addAttribute("files",
                storageService.loadAll()
                        .map(path -> MvcUriComponentsBuilder
                                .fromMethodName(FileUploadController.class, "serveFile", path.getFileName().toString())
                                .build().toUri().toString())
                        .collect(Collectors.toList()));

        return "uploadForm";
    }
    */

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = null;
        try {
            file = storageService.loadAsResource(filename);
        } catch (StorageFileNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (StorageException e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        String detectedType = null;
        try {
            detectedType = storageService.getMimeType(file.getInputStream());
        } catch (StorageException | IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, detectedType).body(file);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<UploadResult> handleFileUpload(@RequestParam MultipartFile file) {

        String filename = null;
        try {
            filename = storageService.store(file);
        } catch (StorageException e) {
            UploadResult uploadResult =  new UploadResult(null, true, e.getMessage());
            return new ResponseEntity<>(uploadResult, HttpStatus.OK);
        }

        UploadResult uploadResult =  new UploadResult(filename, false, null);

        return new ResponseEntity<>(uploadResult, HttpStatus.OK);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}