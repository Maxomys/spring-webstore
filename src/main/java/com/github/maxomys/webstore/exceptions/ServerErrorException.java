package com.github.maxomys.webstore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerErrorException extends RuntimeException {

    public ServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

}
