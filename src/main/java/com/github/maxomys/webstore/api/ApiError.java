package com.github.maxomys.webstore.api;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class ApiError {

    private HttpStatus status;
    private Date timestamp;
    private String message;
    private String debugMessage;

    public ApiError() {
        timestamp = new Date();
    }

    public ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiError(Throwable e) {
        if (e.getMessage() != null) {
            this.debugMessage = e.getMessage();
        }
    }

    public ApiError(String message, Throwable e) {
        this.status = status;
        this.message = message;

        if (e.getMessage() != null) {
            this.debugMessage = e.getMessage();
        }
    }

    public ApiError(HttpStatus status, Throwable e) {
        this();
        this.status = status;

        if (e.getMessage() != null) {
            this.debugMessage = e.getMessage();
        }
    }

    public ApiError(HttpStatus status, String message, Throwable e) {
        this.status = status;
        this.message = message;

        if (e.getMessage() != null) {
            this.debugMessage = e.getMessage();
        }
    }

}
