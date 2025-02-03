import org.example.controllers.AuthController;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.dto.RegistrationRequest;
import org.example.entities.Users;
import org.example.security.jwt.JwtUtil;
import org.example.services.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_NewUser_Success() {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("newuser");
        request.setPassword("password");
        request.setRole("ROLE_USER");

        when(userDetailsService.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        ResponseEntity<?> response = authController.register(request);

        assertEquals(201, response.getStatusCodeValue());
        Users createdUser = (Users) response.getBody();
        assertNotNull(createdUser);
        assertEquals("newuser", createdUser.getUsername());
        assertEquals("ROLE_USER", createdUser.getRole());
    }

    @Test
    void register_ExistingUser_Failure() {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("existingUser");
        request.setPassword("password");

        when(userDetailsService.existsByUsername("existingUser")).thenReturn(true);

        ResponseEntity<?> response = authController.register(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Username is already taken", response.getBody());
    }

    @Test
    void login_ValidCredentials_Success() {
        AuthRequest request = new AuthRequest();
        request.setUsername("user");
        request.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtil.generateJwtToken(authentication)).thenReturn("token");

        ResponseEntity<?> response = authController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        AuthResponse authResponse = (AuthResponse) response.getBody();
        assertNotNull(authResponse);
        assertEquals("token", authResponse.getToken());
    }

    @Test
    void login_InvalidCredentials_Failure() {
        AuthRequest request = new AuthRequest();
        request.setUsername("user");
        request.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        ResponseEntity<?> response = authController.login(request);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid username or password", response.getBody());
    }
}
