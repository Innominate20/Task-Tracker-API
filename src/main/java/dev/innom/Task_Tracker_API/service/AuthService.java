package dev.innom.Task_Tracker_API.service;

import dev.innom.Task_Tracker_API.dto.LoginRequest;
import dev.innom.Task_Tracker_API.dto.PasswordResetRequest;
import dev.innom.Task_Tracker_API.dto.RegisterRequest;
import dev.innom.Task_Tracker_API.entity.Admin;
import dev.innom.Task_Tracker_API.entity.Manager;
import dev.innom.Task_Tracker_API.entity.RegularUser;
import dev.innom.Task_Tracker_API.enums.Role;
import dev.innom.Task_Tracker_API.exception.UserAlreadyExistsException;
import dev.innom.Task_Tracker_API.exception.UserNotFoundException;
import dev.innom.Task_Tracker_API.jwt.JwtUtility;
import dev.innom.Task_Tracker_API.mapper.RegisterDtoMapper;
import dev.innom.Task_Tracker_API.repository.UserRepository;
import dev.innom.Task_Tracker_API.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RegisterDtoMapper registerDtoMapper;
    private final JwtUtility jwtUtility;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, RegisterDtoMapper registerDtoMapper, JwtUtility jwtUtility, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.registerDtoMapper = registerDtoMapper;
        this.jwtUtility = jwtUtility;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public ResponseEntity<String> registerUserByRole(RegisterRequest registerRequest, Role role){

        var user = userRepository.findByEmail(registerRequest.getEmail());

        if(user.isPresent()){
            throw  new UserAlreadyExistsException("User with the email \""+registerRequest.getEmail()+"\" already exists !");
        }

        switch (role){

            case ADMIN : {
                Admin adminToRegister = registerDtoMapper.toAdmin(registerRequest);
                adminToRegister.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
                userRepository.save(adminToRegister);
                break;
            }
            case MANAGER : {
                Manager managerToRegister = registerDtoMapper.toManager(registerRequest);
                managerToRegister.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
                userRepository.save(managerToRegister);
                break;
            }
            case USER : {
                RegularUser regularUserToRegister = registerDtoMapper.toRegularUser(registerRequest);
                regularUserToRegister.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
                userRepository.save(regularUserToRegister);
                break;
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(role + " registered successfully !");
    }

    public ResponseEntity<String> loginUser(LoginRequest loginRequest){

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtUtility.generateToke(customUserDetails.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @Transactional
    public ResponseEntity<String> passwordReset(PasswordResetRequest passwordResetRequest){

        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        var user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException("Authenticated user cannot be found !"));

        user.setPassword(passwordEncoder.encode(passwordResetRequest.getPassword()));
        user.setUpdateDate(LocalDateTime.now());

        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully !");
    }

}
