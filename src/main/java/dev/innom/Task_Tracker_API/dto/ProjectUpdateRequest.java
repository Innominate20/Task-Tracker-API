package dev.innom.Task_Tracker_API.dto;

import lombok.Getter;

@Getter
public class ProjectUpdateRequest {

    private String name;
    private String description;
}
