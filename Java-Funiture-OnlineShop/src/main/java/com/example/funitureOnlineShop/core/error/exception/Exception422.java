package com.example.funitureOnlineShop.core.error.exception;

import com.example.funitureOnlineShop.core.utils.ApiUtils;
import org.springframework.http.HttpStatus;

public class Exception422 extends RuntimeException {
    public Exception422(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public HttpStatus status(){
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}

