package mx.egd.fmre.register.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.egd.fmre.register.service.IdBadgeService;
import mx.egd.fmre.register.service.exceptions.AficionadoServiceException;
import mx.egd.fmre.register.service.exceptions.CredencialControllerSeviceException;
import mx.egd.fmre.register.service.exceptions.ServiceException;

@RestController
@RequestMapping("credencial")
@RequiredArgsConstructor
@Slf4j
public class CredencialController {

    private final IdBadgeService idBadgeService;
    
    @GetMapping(path = "/{idPersona}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getCredencial(@PathVariable Integer idPersona) {
        byte[] pdfBytes = null;
        try {
            pdfBytes = idBadgeService.createIdBadgeService(idPersona);
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }
        
        // 2. Set the appropriate HTTP headers
        HttpHeaders headers = new HttpHeaders();
        // 'attachment' forces a download prompt, 'inline' tries to render inside the browser
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(pdfBytes.length));

        // Optional: Helps the client know the exact file size
        headers.setContentLength(pdfBytes.length); 

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @ExceptionHandler(CredencialControllerSeviceException.class)
    public ResponseEntity<String> handleUnexpected(AficionadoServiceException ex) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }
}
