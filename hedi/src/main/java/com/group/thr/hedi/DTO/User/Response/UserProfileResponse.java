package com.group.thr.hedi.DTO.User.Response;

import lombok.Data;

@Data
public class UserProfileResponse {
private String firstName;
private String lastName;
private String email;
private Integer age;
private String gender;
private Double height;
private Double weight;
}
