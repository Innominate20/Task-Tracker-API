package dev.innom.Task_Tracker_API.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ProjectResponse {

    private long id;
    private String name;
    private String description;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String userEmail;
}
