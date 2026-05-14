package com.group.thr.hedi.DTO.User.Request;
import lombok.Data;

@Data
public class UserProfileRequest {
    private Integer age;
    private String gender;
    private Double height;
    private Double weight;
}