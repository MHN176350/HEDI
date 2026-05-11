package com.group.thr.hedi.Service.Implement;

import com.group.thr.hedi.DTO.Authetication.Request.RegisterRequest;
import com.group.thr.hedi.DTO.Authetication.Request.OAuthUserInfo;
import com.group.thr.hedi.DTO.Authetication.Request.GoogleUserProfile;
import com.group.thr.hedi.DTO.Authetication.Response.LoginResponse;
import com.group.thr.hedi.DTO.Authetication.Response.RegisterResponse;
import com.group.thr.hedi.DTO.User.Request.UserProfileRequest;
import com.group.thr.hedi.DTO.User.Response.UserProfileResponse;
import com.group.thr.hedi.DTO.Authetication.Response.RefreshTokenResponse;
import com.group.thr.hedi.Entity.User;
import com.group.thr.hedi.Repository.IAuthenticationRepository;
import com.group.thr.hedi.Repository.IRefreshTokenRepository;
import com.group.thr.hedi.Entity.RefreshToken;
import com.group.thr.hedi.Service.Interface.IAuthenticationService;
import com.group.thr.hedi.Utils.JwtUtil;
import com.group.thr.hedi.Utils.GoogleOAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

    @Autowired
    private IAuthenticationRepository authenticationRepository;

    @Autowired
    private IRefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private GoogleOAuthUtil googleOAuthUtil;

    @Override
    public LoginResponse authenticate(String email, String password) {
        Optional<User> user = authenticationRepository.findByEmail(email);
        
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User foundUser = user.get();
        
        if (foundUser.getHashed_Password() == null) {
            throw new RuntimeException("This email is registered with OAuth. Please use OAuth to login.");
        }
        
        if (!passwordEncoder.matches(password, foundUser.getHashed_Password())) {
            throw new RuntimeException("Invalid password");
        }
        
        if (!foundUser.getStatus().equals(User.AccountStatus.ACTIVE)) {
            throw new RuntimeException("Account is not active");
        }
        
        String token = jwtUtil.generateToken(foundUser.getEmail(), Math.toIntExact(foundUser.getId()), foundUser.getRole().toString());
        String refreshToken = jwtUtil.generateRefreshToken(Math.toIntExact(foundUser.getId()));
         saveRefreshToken(foundUser, refreshToken);
        
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(foundUser.getId());
        response.setEmail(foundUser.getEmail());
        response.setFirstName(foundUser.getFirstName());
        response.setLastName(foundUser.getLastName());
       
        return response;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        Optional<User> existingUser = authenticationRepository.findByEmail(registerRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }
        
        User newUser = User.builder()
                .email(registerRequest.getEmail())
                .hashed_Password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .role(User.Role.USER)
                .status(User.AccountStatus.ACTIVE)
                .build();
        
        User savedUser = authenticationRepository.save(newUser);
        String token = jwtUtil.generateToken(savedUser.getEmail(), Math.toIntExact(savedUser.getId()), savedUser.getRole().toString());
        String refreshToken = jwtUtil.generateRefreshToken(Math.toIntExact(savedUser.getId()));
        
        saveRefreshToken(savedUser, refreshToken);
        
        RegisterResponse response = new RegisterResponse();
        response.setUserId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());
        response.setMessage("Registration successful");
        response.setToken(token);
        
        return response;
    }

    @Override
    public LoginResponse authenticateWithOAuth(OAuthUserInfo oAuthUserInfo) {
        Optional<User> existingUser = authenticationRepository.findByEmail(oAuthUserInfo.getEmail());
        
        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            if (user.getOauth_Id() == null) {
                user.setOauth_Id(oAuthUserInfo.getId());
                user.setOauth_Provider(oAuthUserInfo.getProvider());
                authenticationRepository.save(user);
            }
        } else {
            user = User.builder()
                    .email(oAuthUserInfo.getEmail())
                    .firstName(oAuthUserInfo.getFirstName())
                    .lastName(oAuthUserInfo.getLastName())
                    .oauth_Id(oAuthUserInfo.getId())
                    .oauth_Provider(oAuthUserInfo.getProvider())
                    .role(User.Role.USER)
                    .status(User.AccountStatus.ACTIVE)
                    .build();
            user = authenticationRepository.save(user);
        }
        
        if (!user.getStatus().equals(User.AccountStatus.ACTIVE)) {
            throw new RuntimeException("Account is not active");
        }
        
        String token = jwtUtil.generateToken(user.getEmail(), Math.toIntExact(user.getId()), user.getRole().toString());
        
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        return response;
    }
    public LoginResponse authenticateWithGoogleCode(String code, String provider) {
        try {
            System.out.println("[DEBUG] authenticateWithGoogleCode called with code: " + code + ", provider: " + provider);
            
            if (code == null || code.trim().isEmpty()) {
                throw new RuntimeException("Authorization code is null or empty. Make sure the code is being passed from the frontend.");
            }
            
            String accessToken = googleOAuthUtil.getGoogleAccessToken(code);
            
            GoogleUserProfile googleProfile = googleOAuthUtil.getGoogleUserProfile(accessToken);
            
            System.out.println("[DEBUG] Retrieved Google profile: " + googleProfile.getEmail());
            
            OAuthUserInfo oAuthUserInfo = new OAuthUserInfo();
            oAuthUserInfo.setId(googleProfile.getId());
            oAuthUserInfo.setEmail(googleProfile.getEmail());
            oAuthUserInfo.setFirstName(googleProfile.getGivenName() != null ? googleProfile.getGivenName() : "User");
            oAuthUserInfo.setLastName(googleProfile.getFamilyName() != null ? googleProfile.getFamilyName() : "");
            oAuthUserInfo.setProvider(provider);
            
            return authenticateWithOAuth(oAuthUserInfo);
        } catch (Exception e) {
            System.out.println("[ERROR] Google OAuth authentication failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Google OAuth authentication failed: " + e.getMessage());
        }
    }


    @Override
    public RefreshTokenResponse refreshToken(String refreshTokenString) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(refreshTokenString);
        
        if (refreshToken.isEmpty()) {
            throw new RuntimeException("Refresh token not found");
        }
        
        RefreshToken token = refreshToken.get();
        
        if (token.isRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }
        
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token has expired");
        }
        
        if (!jwtUtil.validateToken(refreshTokenString)) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        User user = token.getUser();
        
        String newAccessToken = jwtUtil.generateToken(user.getEmail(), Math.toIntExact(user.getId()), user.getRole().toString());
        
        String newRefreshToken = jwtUtil.generateRefreshToken(Math.toIntExact(user.getId()));
        
        token.setRevoked(true);
        refreshTokenRepository.save(token);
        
        saveRefreshToken(user, newRefreshToken);
        
        RefreshTokenResponse response = new RefreshTokenResponse();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        response.setTokenType("Bearer");
        response.setExpiresIn(jwtUtil.getExpirationTime());
        
        return response;
    }

    @Override
    public void logout(Long userId) {
        Optional<User> user = authenticationRepository.findById(userId);
        if (user.isPresent()) {
            user.get().getRefreshTokens().forEach(token -> {
                token.setRevoked(true);
                refreshTokenRepository.save(token);
            });
        }
    }

    private void saveRefreshToken(User user, String refreshTokenString) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenString)
                .user(user)
                .expiryDate(LocalDateTime.now().plusSeconds(jwtUtil.getRefreshExpirationTime() / 1000))
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public String updateUserProfile(Long userId, UserProfileRequest request) {
    try{
    User user = authenticationRepository.findById(userId).orElseThrow(
        () -> new RuntimeException("User not found")
    );
    user.setAge(request.getAge());
    user.setGender(request.getGender());
    user.setHeight(request.getHeight());
    user.setWeight(request.getWeight());
    authenticationRepository.save(user);}
    catch(Exception e){
        throw new RuntimeException("Failed to update profile: " + e.getMessage());
    }
    return "Profile updated successfully";
    }

    @Override
    public UserProfileResponse getUserProfile(Long userId) {
        try {
            User user = authenticationRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
            );
            UserProfileResponse response = new UserProfileResponse();
            response.setEmail(user.getEmail());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setAge(user.getAge());
            response.setGender(user.getGender());
            response.setHeight(user.getHeight());
            response.setWeight(user.getWeight());
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve profile: " + e.getMessage());
        }
    }
    
   
}
