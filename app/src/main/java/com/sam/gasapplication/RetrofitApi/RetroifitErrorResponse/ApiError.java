package com.sam.gasapplication.RetrofitApi.RetroifitErrorResponse;

public class ApiError {

    private int statusCode;
    private String message;

    public ApiError()
    {

    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}
