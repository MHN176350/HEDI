package com.group.thr.hedi.DTO.Authetication.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthCallbackRequest {
    private String code;
    private String provider;  
    private String redirectUri;
}
