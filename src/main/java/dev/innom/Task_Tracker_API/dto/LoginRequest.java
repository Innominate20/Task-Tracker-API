package dev.innom.Task_Tracker_API.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
