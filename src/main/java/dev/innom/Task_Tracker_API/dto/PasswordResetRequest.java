package dev.innom.Task_Tracker_API.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordResetRequest {

    @NotBlank
    private String password;
}
