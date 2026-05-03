package com.group.thr.hedi.DTO.Authetication.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthUserInfo {
    private String id;          
    private String email;
    private String firstName;
    private String lastName;
    private String provider; 
}
