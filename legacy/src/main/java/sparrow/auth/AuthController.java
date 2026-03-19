package sparrow.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import sparrow.auth.dto.AuthResponse;
import sparrow.auth.dto.LoginRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        return authService.createAuthResponse(loginRequest.getEmail());
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody AuthResponse refreshRequest) {
        return authService.refreshToken(refreshRequest.getRefreshToken());
    }

    @PostMapping("/logout")
    public AuthResponse logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            authService.revokeUserToken(authentication.getName());
        }

        SecurityContextHolder.clearContext();

        AuthResponse response = new AuthResponse();
        response.setMessage("Logout successful");
        return response;
    }
}
