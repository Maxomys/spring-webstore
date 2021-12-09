package com.github.maxomys.webstore.api.controllers;

import com.github.maxomys.webstore.api.ApiError;
import com.github.maxomys.webstore.exceptions.NotInStockException;
import com.github.maxomys.webstore.exceptions.ResourceNotFoundException;
import com.github.maxomys.webstore.exceptions.UnableToRemoveUserException;
import com.github.maxomys.webstore.exceptions.UsernameAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ApiError handleResourceNotFound(ResourceNotFoundException e) {
        return new ApiError("Resource not found", e);
    }

    @ExceptionHandler(NotInStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleNotInStock(NotInStockException e) {
        return new ApiError("Not in stock", e);
    }

    @ExceptionHandler(UnableToRemoveUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleUnableToRemoveUser(UnableToRemoveUserException e) {
        return new ApiError("Unable to remove user", e);
    }

    @ExceptionHandler(UsernameAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiError handleUsernameAlreadyExistException(UsernameAlreadyExistException e) {
        return new ApiError("Username already exists", e);
    }

}
