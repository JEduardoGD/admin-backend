package mx.egd.fmre.register.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.CONTENT_TOO_LARGE.value());
        body.put("error", "Payload Too Large");
        body.put("message", "The file you are trying to upload exceeds the maximum allowed limit.");

        return ResponseEntity.status(HttpStatus.CONTENT_TOO_LARGE).body(body);
    }
}
