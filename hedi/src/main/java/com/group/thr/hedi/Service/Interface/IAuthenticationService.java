package com.group.thr.hedi.Service.Interface;

import com.group.thr.hedi.DTO.Authetication.Request.RegisterRequest;
import com.group.thr.hedi.DTO.Authetication.Request.OAuthUserInfo;
import com.group.thr.hedi.DTO.Authetication.Response.LoginResponse;
import com.group.thr.hedi.DTO.Authetication.Response.RegisterResponse;
import com.group.thr.hedi.DTO.Authetication.Response.RefreshTokenResponse;

public interface IAuthenticationService {
    LoginResponse authenticate(String email, String password);
    RegisterResponse register(RegisterRequest registerRequest);
    LoginResponse authenticateWithOAuth(OAuthUserInfo oAuthUserInfo);
    RefreshTokenResponse refreshToken(String refreshToken);
    void logout(Long userId);
}
