package com.group.thr.hedi.DTO.Authetication.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String message;
    private String token;
}
