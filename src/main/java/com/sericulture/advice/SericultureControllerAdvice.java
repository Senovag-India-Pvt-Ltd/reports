package com.sericulture.advice;

import com.sericulture.controller.Response;
import com.sericulture.exception.EmptyInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SericultureControllerAdvice {

    @ExceptionHandler(EmptyInputException.class)
    public ResponseEntity<Response> handleEmptyInput(EmptyInputException emptyInputException){
        Response response = new Response<String>();
        response.setErrorCode(emptyInputException.getErrorCode());
        response.setErrorMessage(emptyInputException.getErrorMessage());
        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
    }
}
