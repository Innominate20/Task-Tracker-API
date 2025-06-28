package dev.innom.Task_Tracker_API.dto;

import dev.innom.Task_Tracker_API.enums.Priority;
import dev.innom.Task_Tracker_API.enums.Status;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
public class TaskResponse {

    private long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private Status status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String creator;
    private String project;
    private String assignedUser;
}
