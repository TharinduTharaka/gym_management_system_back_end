package com.kiu.gym_management_system.response;

import lombok.Data;

@Data
public class Response {
    private int code;
    private String msg;
    private Object data;
}
