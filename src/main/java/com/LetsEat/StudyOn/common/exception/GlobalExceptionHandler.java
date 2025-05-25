package com.LetsEat.StudyOn.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 예외처리 핸들러
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleScheduleException(CustomException e) {
        e.printStackTrace();
        return ResponseEntity.status(e.getErrorType().getHttpStatus())
                .body(new ExceptionDto(e.getErrorType()));
    }

    // validation 예외처리 핸들러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException e){
        e.printStackTrace();
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder builder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getField()).append(" : ")
                    .append(fieldError.getDefaultMessage()).append("\n");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDto(builder.toString()));
    }
}