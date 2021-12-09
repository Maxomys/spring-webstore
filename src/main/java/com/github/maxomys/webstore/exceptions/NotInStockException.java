package com.github.maxomys.webstore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotInStockException extends RuntimeException {

    public NotInStockException() {
        super();
    }

    public NotInStockException(String message) {
        super(message);
    }

}
