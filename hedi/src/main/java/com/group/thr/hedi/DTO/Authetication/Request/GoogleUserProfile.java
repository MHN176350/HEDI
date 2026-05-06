package com.group.thr.hedi.DTO.Authetication.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserProfile {
    private String id;
    private String email;
    
    @JsonProperty("given_name")
    private String givenName;
    
    @JsonProperty("family_name")
    private String familyName;
    
    private String name;
    private String picture;
    
    @JsonProperty("email_verified")
    private Boolean emailVerified;
}
