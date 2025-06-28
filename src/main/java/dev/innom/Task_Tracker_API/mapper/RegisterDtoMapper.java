package dev.innom.Task_Tracker_API.mapper;

import dev.innom.Task_Tracker_API.dto.RegisterRequest;
import dev.innom.Task_Tracker_API.entity.Admin;
import dev.innom.Task_Tracker_API.entity.Manager;
import dev.innom.Task_Tracker_API.entity.RegularUser;
import dev.innom.Task_Tracker_API.enums.Role;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Mapper(componentModel = "spring")
public interface RegisterDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "projects", ignore = true)
    Admin toAdmin(RegisterRequest registerRequest);

    @AfterMapping
    default void enhanceAdmin(@MappingTarget Admin admin){
        admin.setCreateDate(LocalDateTime.now());
        admin.setUpdateDate(LocalDateTime.now());
        admin.setProjects(new ArrayList<>());
        admin.setRole(Role.ADMIN);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    Manager toManager(RegisterRequest registerRequest);

    @AfterMapping
    default void enhanceManager(@MappingTarget Manager manager){
        manager.setCreateDate(LocalDateTime.now());
        manager.setUpdateDate(LocalDateTime.now());
        manager.setProjects(new ArrayList<>());
        manager.setRole(Role.MANAGER);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    RegularUser toRegularUser(RegisterRequest registerRequest);

    @AfterMapping
    default void enhanceRegularUser(@MappingTarget RegularUser regularUser){
        regularUser.setCreateDate(LocalDateTime.now());
        regularUser.setUpdateDate(LocalDateTime.now());
        regularUser.setTasks(new ArrayList<>());
        regularUser.setRole(Role.USER);
    }
}
