package dev.innom.Task_Tracker_API.mapper;

import dev.innom.Task_Tracker_API.dto.ProjectCreateRequest;
import dev.innom.Task_Tracker_API.dto.ProjectResponse;
import dev.innom.Task_Tracker_API.dto.ProjectUpdateRequest;
import dev.innom.Task_Tracker_API.dto.RegisterRequest;
import dev.innom.Task_Tracker_API.entity.Admin;
import dev.innom.Task_Tracker_API.entity.Project;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Project toProject(ProjectCreateRequest projectCreateRequest);

    @AfterMapping
    default void enhanceProject(@MappingTarget Project project){

        project.setCreateDate(LocalDateTime.now());
        project.setUpdateDate(LocalDateTime.now());
        project.setTasks(new ArrayList<>());
    }

    @Mapping(target = "userEmail", ignore = true)
    ProjectResponse toProjectResponse(Project project);

    void UpdateProjectDto(ProjectUpdateRequest projectUpdateRequest, @MappingTarget Project project);
}
