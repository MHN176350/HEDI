package com.group.thr.hedi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.group.thr.hedi.DTO.Authetication.Request.LoginRequest;
import com.group.thr.hedi.DTO.Authetication.Request.RegisterRequest;
import com.group.thr.hedi.DTO.Authetication.Request.OAuthUserInfo;
import com.group.thr.hedi.DTO.Authetication.Request.OAuthCallbackRequest;
import com.group.thr.hedi.DTO.Authetication.Request.RefreshTokenRequest;
import com.group.thr.hedi.DTO.Authetication.Response.LoginResponse;
import com.group.thr.hedi.DTO.Authetication.Response.RegisterResponse;
import com.group.thr.hedi.DTO.Authetication.Response.RefreshTokenResponse;
import com.group.thr.hedi.DTO.Common.Response.ResponseFormat;
import com.group.thr.hedi.Enum.ResponseCode;
import com.group.thr.hedi.Service.Interface.IAuthenticationService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {
    
    @Autowired
    private IAuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseFormat login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            return new ResponseFormat(ResponseCode.SUCCESS, response);
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseFormat register(@RequestBody RegisterRequest registerRequest) {
        try {
            RegisterResponse response = authenticationService.register(registerRequest);
            return new ResponseFormat(ResponseCode.SUCCESS, response);
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/oauth/callback")
    public ResponseFormat oauthCallback(@RequestBody OAuthCallbackRequest request) {
        try {
            if (request.getCode() == null || request.getCode().trim().isEmpty()) {
                return new ResponseFormat(ResponseCode.BAD_REQUEST, 
                    "Code is null or empty. Ensure the authorization code from Google is being passed correctly from the frontend.");
            }
            
            LoginResponse response = authenticationService.authenticateWithGoogleCode(request.getCode(), request.getProvider());
            return new ResponseFormat(ResponseCode.SUCCESS, response);
        } catch (Exception e) {
            System.out.println(" OAuth callback failed: " + e.getMessage());
            e.printStackTrace();
            return new ResponseFormat(ResponseCode.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseFormat refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            RefreshTokenResponse response = authenticationService.refreshToken(request.getRefreshToken());
            return new ResponseFormat(ResponseCode.SUCCESS, response);
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/logout/{userId}")
    public ResponseFormat logout(@PathVariable Long userId) {
        try {
            authenticationService.logout(userId);
            return new ResponseFormat(ResponseCode.SUCCESS, "Logged out successfully");
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}