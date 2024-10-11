package com.stark.webbanhang.helper.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXISTED(9999,"uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_MESSAGEKEY(1002,"Invalid message key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1001,"User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003,"Username must be at least 3 character",HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004,"Password must be at least 8 character",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005,"User not existed",HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006,"Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007,"you do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008,"Your age must be at least 18",HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1009,"Role not existed",HttpStatus.NOT_FOUND)
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;

}