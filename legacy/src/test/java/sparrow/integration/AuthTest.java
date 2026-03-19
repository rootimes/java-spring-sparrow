package sparrow.integration;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import sparrow.auth.dto.AuthResponse;
import sparrow.auth.dto.LoginRequest;
import sparrow.user.User;
import sparrow.user.UserRepository;

@Transactional
public class AuthTest extends RestDocsTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void login_withValidPassword_shouldReturnToken() throws Exception {
        String email = "test-" + UUID.randomUUID() + "@example.com";
        String rawPassword = "password123";
        User user = new User();
        user.setName("Test User");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(rawPassword);

        this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andDo(document("auth-login-success"));
    }

    @Test
    public void login_withInvalidPassword_shouldReturnUnauthorized() throws Exception {
        String email = "test-fail@example.com";
        User user = new User();
        user.setName("Fail User");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("correct-password"));
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword("wrong-password");

        this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(document("auth-login-fail"));
    }

    @Test
    public void logout_shouldClearContext() throws Exception {
        this.mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout successful"))
                .andDo(document("auth-logout"));
    }

    @Test
    public void refreshToken_withValidRefreshToken_shouldReturnNewToken() throws Exception {
        String email = "refresh-" + UUID.randomUUID() + "@example.com";
        String rawPassword = "password123";
        User user = new User();
        user.setName("Refresh User");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(rawPassword);

        String loginResponse = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        AuthResponse authResponse = objectMapper.readValue(loginResponse, AuthResponse.class);
        String refreshToken = authResponse.getRefreshToken();

        AuthResponse refreshRequest = new AuthResponse();
        refreshRequest.setRefreshToken(refreshToken);

        this.mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.message").value("Token refreshed and rotated"))
                .andDo(document("auth-refresh-success"));
    }

    @Test
    public void refreshToken_withInvalidRefreshToken_shouldReturnUnauthorized() throws Exception {
        AuthResponse refreshRequest = new AuthResponse();
        refreshRequest.setRefreshToken("invalid-refresh-token");

        this.mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(document("auth-refresh-fail"));
    }
}
