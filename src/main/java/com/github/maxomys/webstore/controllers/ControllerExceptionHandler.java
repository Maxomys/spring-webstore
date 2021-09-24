package com.github.maxomys.webstore.controllers;

import com.github.maxomys.webstore.exceptions.ResourceNotFoundException;
import com.github.maxomys.webstore.exceptions.UnableToRemoveUserException;
import com.github.maxomys.webstore.exceptions.UsernameAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public ModelAndView usernameExists() {
        return new ModelAndView("/errors/usernameExists");
    }

    @ExceptionHandler(UnableToRemoveUserException.class)
    public ModelAndView unableToRemoveUser() {
        return new ModelAndView("/errors/unableToRemoveUser");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView resourceNotFound() {
        return new ModelAndView("/errors/404error");
    }

}
