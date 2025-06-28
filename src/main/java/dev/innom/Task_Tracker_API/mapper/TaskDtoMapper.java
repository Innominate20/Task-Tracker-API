package dev.innom.Task_Tracker_API.mapper;

import dev.innom.Task_Tracker_API.dto.ProjectUpdateRequest;
import dev.innom.Task_Tracker_API.dto.TaskCreateRequest;
import dev.innom.Task_Tracker_API.dto.TaskResponse;
import dev.innom.Task_Tracker_API.dto.TaskUpdateRequest;
import dev.innom.Task_Tracker_API.entity.Project;
import dev.innom.Task_Tracker_API.entity.Task;
import dev.innom.Task_Tracker_API.enums.Status;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "assignedUser", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    Task toTask(TaskCreateRequest taskCreateRequest);

    @AfterMapping
    default void enhanceTask(@MappingTarget Task task){

        task.setCreateDate(LocalDateTime.now());
        task.setUpdateDate(LocalDateTime.now());
        task.setStatus(Status.TODO);
    }

    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "assignedUser", ignore = true)
    TaskResponse toTaskResponse(Task task);


    void UpdateTaskDto(TaskUpdateRequest taskUpdateRequest, @MappingTarget Task task);

}
