package dev.innom.Task_Tracker_API.dto;

import dev.innom.Task_Tracker_API.entity.Project;
import dev.innom.Task_Tracker_API.entity.RegularUser;
import dev.innom.Task_Tracker_API.entity.User;
import dev.innom.Task_Tracker_API.enums.Priority;
import dev.innom.Task_Tracker_API.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class TaskCreateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private LocalDate dueDate;
    @NotNull
    private Priority priority;
    @NotNull
    private long projectId;
}
