package com.nishant.springboot.currencyutilities.restcontrollers.advice;

import com.nishant.springboot.currencyutilities.model.ResponseStatus;
import com.nishant.springboot.currencyutilities.model.errorhandling.CurrencyAPIInputValidationException;
import com.nishant.springboot.currencyutilities.model.errorhandling.CurrencyAPIServerValidationException;
import com.nishant.springboot.currencyutilities.model.errorhandling.CurrencyAPITechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;

@ControllerAdvice
public class GlobalControllerExceptionAdvice {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(CurrencyAPITechnicalException.class)
    public ResponseEntity<ResponseStatus> handleTechnicalExceptions(CurrencyAPITechnicalException exception) {
        String errorStatus = "Failed";
        String errorCode = "Error while accessing Currency API";

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        logger.error(sw.toString());
        String errorDescription = exception.getCause().getMessage();

        Throwable cause = exception;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        if (cause instanceof ConnectException) {
            errorDescription = "Unable to connect to Currency Provider API";
        }

        ResponseStatus status = new ResponseStatus(errorStatus, errorCode, errorDescription);

        return new ResponseEntity<>(status, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CurrencyAPIInputValidationException.class)
    public ResponseEntity<ResponseStatus> handleInputValidationException(CurrencyAPIInputValidationException exception) {
        return new ResponseEntity<>(exception.getResponseStatus(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyAPIServerValidationException.class)
    public ResponseEntity<ResponseStatus> handlerServerValidationException(CurrencyAPIServerValidationException exception) {

        ResponseStatus status = exception.getResponseStatus();
        logger.error(status.toString());
        status.setErrorDescription(status.getErrorCode());
        status.setErrorCode("Validation Error occurred while invoking Currency Provider API");
        return new ResponseEntity<>(exception.getResponseStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
