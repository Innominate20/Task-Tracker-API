package dev.innom.Task_Tracker_API.dto;

import dev.innom.Task_Tracker_API.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ChangeStatusDto {

    @NotNull
    private Status status;
}
