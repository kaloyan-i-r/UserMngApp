package org.demo.userapp.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResource> handleException(HttpServletRequest req, Exception ex) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        String errorMessage = null;
        if (ex instanceof UserMngAppExceptionMessage) {
            errorMessage = ((UserMngAppExceptionMessage) ex).getDsdMsg();
        } else if (ex instanceof MethodArgumentNotValidException) {
            errorMessage = "Some of the inserted data is invalid!";
        } else {
            errorMessage = "An unknown error occurred!";
            logger.info("Exception : " + ex);
            logger.error("Exception : " + ex.getMessage());
            ex.printStackTrace();
        }
        ErrorResource errorInfo = new ErrorResource();
        errorInfo.setMessage(errorMessage);

        return new ResponseEntity<ErrorResource>(errorInfo, headers, HttpStatus.BAD_REQUEST);

    }

}