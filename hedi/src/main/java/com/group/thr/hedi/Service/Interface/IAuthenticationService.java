package com.group.thr.hedi.Service.Interface;

import com.group.thr.hedi.DTO.Authetication.Request.RegisterRequest;
import com.group.thr.hedi.DTO.Authetication.Request.OAuthUserInfo;
import com.group.thr.hedi.DTO.Authetication.Response.LoginResponse;
import com.group.thr.hedi.DTO.Authetication.Response.RegisterResponse;
import com.group.thr.hedi.DTO.User.Request.UserProfileRequest;
import com.group.thr.hedi.DTO.User.Response.UserProfileResponse;
import com.group.thr.hedi.DTO.Authetication.Response.RefreshTokenResponse;

public interface IAuthenticationService {
    LoginResponse authenticate(String email, String password);
    RegisterResponse register(RegisterRequest registerRequest);
    LoginResponse authenticateWithOAuth(OAuthUserInfo oAuthUserInfo);
    LoginResponse authenticateWithGoogleCode(String code, String provider);
    RefreshTokenResponse refreshToken(String refreshToken);
    void logout(Long userId);
    String updateUserProfile(Long userId, UserProfileRequest user);
    UserProfileResponse getUserProfile(Long userId);
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
    void intializeUserData();
}
