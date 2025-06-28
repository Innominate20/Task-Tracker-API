package dev.innom.Task_Tracker_API.controller;

import dev.innom.Task_Tracker_API.dto.LoginRequest;
import dev.innom.Task_Tracker_API.dto.PasswordResetRequest;
import dev.innom.Task_Tracker_API.dto.RegisterRequest;
import dev.innom.Task_Tracker_API.enums.Role;
import dev.innom.Task_Tracker_API.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/manager/register")
    public ResponseEntity<String> registerManager(@Valid @RequestBody RegisterRequest registerRequest){

        return authService.registerUserByRole(registerRequest, Role.MANAGER);
    }

    @PostMapping("/password-reset")
    public ResponseEntity<String> passwordReset(@Valid @RequestBody PasswordResetRequest passwordResetRequest){

        return authService.passwordReset(passwordResetRequest);
    }


    @PostMapping("/manager/login")
    public ResponseEntity<String> loginManager(@Valid @RequestBody LoginRequest loginRequest){

        return authService.loginUser(loginRequest);
    }

    @PostMapping("/user/register")
    public ResponseEntity<String> registerRegularUser(@Valid @RequestBody RegisterRequest registerRequest){

        return authService.registerUserByRole(registerRequest, Role.USER);
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> loginRegularUser(@Valid @RequestBody LoginRequest loginRequest){

        return authService.loginUser(loginRequest);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody RegisterRequest registerRequest){

        return authService.registerUserByRole(registerRequest, Role.ADMIN);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<String> loginAdmin(@Valid @RequestBody LoginRequest loginRequest){

        return authService.loginUser(loginRequest);
    }
}
