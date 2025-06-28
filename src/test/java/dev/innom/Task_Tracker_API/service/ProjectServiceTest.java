package dev.innom.Task_Tracker_API.service;

import dev.innom.Task_Tracker_API.dto.ProjectCreateRequest;
import dev.innom.Task_Tracker_API.dto.ProjectResponse;
import dev.innom.Task_Tracker_API.entity.Admin;
import dev.innom.Task_Tracker_API.entity.Manager;
import dev.innom.Task_Tracker_API.entity.Project;
import dev.innom.Task_Tracker_API.entity.User;
import dev.innom.Task_Tracker_API.mapper.ProjectDtoMapper;
import dev.innom.Task_Tracker_API.repository.ProjectRepository;
import dev.innom.Task_Tracker_API.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectDtoMapper projectDtoMapper;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setup() {
        // Set the email of authenticated user
        var auth = new UsernamePasswordAuthenticationToken("test@example.com", null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void createProject_returnCreatedResponse() {
        ProjectCreateRequest request = new ProjectCreateRequest();
        request.setName("Name");
        request.setDescription("Description");

        User user = new Manager();
        user.setEmail("test@example.com");

        Project project = new Project();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(projectDtoMapper.toProject(request)).thenReturn(project);

        ResponseEntity<String> response = projectService.createProject(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Project is successfully created !", response.getBody());
    }

}
