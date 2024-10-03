package com.example.demo.exception;

import com.example.demo.response.ApiResponse;
import com.example.demo.response.Constants;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(NoStaffFoundException.class)
    public ResponseEntity<ApiResponse> handleNoLoanFoundException(NoStaffFoundException ex){
        return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_SUCCESS_CODE, Constants.OPERATION_SUCCESS_DESCRIPTION, ex.getLocalizedMessage()), HttpStatus.OK);
    }

    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseEntity<ApiResponse> handleInvalidAccountNumberException(InvalidVerificationCodeException ex){
        return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_FAILURE_CODE, Constants.OPERATION_FAILED_DESCRIPTION, ex.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GenericServiceException.class)
    public ResponseEntity<ApiResponse> handleGenericServiceException(GenericServiceException ex) {
        return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_FAILURE_CODE, Constants.OPERATION_FAILED_DESCRIPTION, ex.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        return new ResponseEntity<>(new ApiResponse(Constants.OPERATION_FAILURE_CODE, Constants.OPERATION_FAILED_DESCRIPTION, ex.getLocalizedMessage()), HttpStatus.FORBIDDEN);
    }



}
