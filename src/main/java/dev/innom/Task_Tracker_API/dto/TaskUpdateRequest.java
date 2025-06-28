package dev.innom.Task_Tracker_API.dto;

import dev.innom.Task_Tracker_API.enums.Priority;
import dev.innom.Task_Tracker_API.enums.Status;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TaskUpdateRequest {

    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private Status status;
}
