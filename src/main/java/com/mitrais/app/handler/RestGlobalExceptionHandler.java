package com.mitrais.app.handler;

import com.mitrais.app.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private static Map<String, Map<String, String>> constraintCodeMap = new HashMap<String, Map<String, String>>() {
        {
            put("uk_mobile_no", new HashMap<String, String>() {
                {
                    put("exception.user.duplicate.mobileNo", "mobileNo");
                }
            });
            put("uk_email", new HashMap<String, String>() {
                {
                    put("exception.user.duplicate.email", "email");
                }
            });
        }
    };

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(HttpServletRequest req, DataIntegrityViolationException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.CONFLICT.value());

        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg != null) {
            Map<String, String> fieldErrors = new HashMap<>();
            Optional<Map.Entry<String, Map<String, String>>> entry = constraintCodeMap.entrySet().stream()
                    .filter((it) -> rootMsg.toLowerCase().contains(it.getKey()))
                    .findAny();
            if (entry.isPresent()) {
                Iterator<String> iterator = entry.get().getValue().keySet().iterator();
                if (iterator.hasNext()) {
                    String key = iterator.next();
                    fieldErrors.put(entry.get().getValue().get(key), messageSource.getMessage(key, null, LocaleContextHolder.getLocale()));
                    body.put("errors", fieldErrors);
                }
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(body, headers, HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream().collect(
                Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        body.put("errors", fieldErrors);

        return new ResponseEntity<>(body, headers, status);
    }

}
