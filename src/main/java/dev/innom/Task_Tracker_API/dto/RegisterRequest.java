package dev.innom.Task_Tracker_API.dto;


import dev.innom.Task_Tracker_API.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RegisterRequest {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
