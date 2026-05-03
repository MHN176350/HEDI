package com.group.thr.hedi.DTO.Common.Response;

import com.group.thr.hedi.Enum.ResponseCode;

public class ResponseFormat {
    private ResponseCode status;
    private String message;
    private Object data;

    public ResponseFormat(ResponseCode status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    public ResponseFormat(ResponseCode status, Object data){
        this.status = status;
        this.data = data;
        if(status.getCode()>=200 && status.getCode()<300){
            this.message = "Success";
        } else if(status.getCode()>=400 && status.getCode()<500){
            this.message = "Client Error";
        } else if(status.getCode()>=500 && status.getCode()<600){
            this.message = "Server Error";
        } else {
            this.message = "Unknown Status";
        }
    }


    public ResponseCode getStatus() {
        return status;
    }

    public void setStatus(ResponseCode status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
