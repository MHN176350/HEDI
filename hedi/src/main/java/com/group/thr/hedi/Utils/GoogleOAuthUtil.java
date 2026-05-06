package com.group.thr.hedi.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.thr.hedi.DTO.Authetication.Request.GoogleTokenResponse;
import com.group.thr.hedi.DTO.Authetication.Request.GoogleUserProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleOAuthUtil {

    @Value("${google.oauth.client-id}")
    private String clientId;

    @Value("${google.oauth.client-secret}")
    private String clientSecret;

    @Value("${google.oauth.redirect-uri}")
    private String redirectUri;

    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Exchange authorization code for access token
     */
    public String getGoogleAccessToken(String code) {
        try {
            // Validate inputs
            if (code == null || code.trim().isEmpty()) {
                throw new RuntimeException("Authorization code is null or empty. Code received: " + code);
            }
            if (clientId == null || clientId.startsWith("YOUR_")) {
                throw new RuntimeException("Google OAuth client-id not configured. Please set google.oauth.client-id in application.properties");
            }
            if (clientSecret == null || clientSecret.isEmpty()) {
                throw new RuntimeException("Google OAuth client-secret not configured. Please set google.oauth.client-secret in application.properties");
            }
            if (redirectUri == null || redirectUri.isEmpty()) {
                throw new RuntimeException("Google OAuth redirect-uri not configured. Please set google.oauth.redirect-uri in application.properties");
            }

            System.out.println("[DEBUG] Exchanging Google OAuth code. Code: " + code);
            System.out.println("[DEBUG] Client ID: " + clientId);
            System.out.println("[DEBUG] Redirect URI: " + redirectUri);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("code", code);
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            body.add("redirect_uri", redirectUri);
            body.add("grant_type", "authorization_code");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(GOOGLE_TOKEN_URL, request, String.class);

            GoogleTokenResponse tokenResponse = objectMapper.readValue(response.getBody(), GoogleTokenResponse.class);
            return tokenResponse.getAccessToken();
        } catch (Exception e) {
            throw new RuntimeException("Failed to exchange authorization code: " + e.getMessage());
        }
    }

    /**
     * Get user profile from Google using access token
     */
    public GoogleUserProfile getGoogleUserProfile(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.getForEntity(
                    GOOGLE_USERINFO_URL + "?access_token=" + accessToken,
                    String.class
            );

            return objectMapper.readValue(response.getBody(), GoogleUserProfile.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Google user profile: " + e.getMessage());
        }
    }
}
