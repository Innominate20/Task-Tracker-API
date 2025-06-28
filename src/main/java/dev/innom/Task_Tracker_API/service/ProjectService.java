package dev.innom.Task_Tracker_API.service;

import dev.innom.Task_Tracker_API.dto.ProjectCreateRequest;
import dev.innom.Task_Tracker_API.dto.ProjectResponse;
import dev.innom.Task_Tracker_API.dto.ProjectUpdateRequest;
import dev.innom.Task_Tracker_API.entity.Admin;
import dev.innom.Task_Tracker_API.entity.Manager;
import dev.innom.Task_Tracker_API.entity.Project;
import dev.innom.Task_Tracker_API.entity.User;
import dev.innom.Task_Tracker_API.exception.AccessNotAllowedException;
import dev.innom.Task_Tracker_API.exception.ResourceUpdateDataException;
import dev.innom.Task_Tracker_API.exception.ResourceNotFoundException;
import dev.innom.Task_Tracker_API.exception.UserNotFoundException;
import dev.innom.Task_Tracker_API.mapper.ProjectDtoMapper;
import dev.innom.Task_Tracker_API.repository.ProjectRepository;
import dev.innom.Task_Tracker_API.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectDtoMapper projectDtoMapper;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, ProjectDtoMapper projectDtoMapper) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectDtoMapper = projectDtoMapper;
    }

    @Transactional
    public ResponseEntity<String> createProject(ProjectCreateRequest projectCreateRequest){

        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Since authentication did not fail then the user does exist, but something might happen during the interaction
        var user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException("Authenticated user cannot be found !"));

        var projectToSave = projectDtoMapper.toProject(projectCreateRequest);

        projectToSave.setUser(user);

        projectRepository.save(projectToSave);

        return ResponseEntity.status(HttpStatus.CREATED).body("Project is successfully created !");
    }

    public ResponseEntity<List<ProjectResponse>> reviewProjects(){

        var user = getUser();
        var userEmail = user.getEmail();

        List<Project> projects;
        if (user instanceof Admin){
            Admin admin = (Admin) user;
            projects = admin.getProjects();
        }
        else {
            Manager manager = (Manager) user;
            projects = manager.getProjects();
        }

        if (projects.isEmpty()){
            throw new ResourceNotFoundException("No projects found for the user !");
        }

        var projectResponse = projects.stream()
                .map(projectDtoMapper::toProjectResponse)
                .peek(elem -> elem.setUserEmail(userEmail))
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(projectResponse);
    }

    public ResponseEntity<ProjectResponse> findProjectById(Long id){

        var user = getUser();
        var userEmail = user.getEmail();

        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project with the id "+ id+" is not found !"));

        // Manager is allowed to access only to the project owned by him
        if (user instanceof Manager){
            if ( ! project.getUser().getEmail().equals(userEmail)){

                throw new AccessNotAllowedException("User is not the owner of the project !");
            }
        }

        var projectResponse = projectDtoMapper.toProjectResponse(project);
        projectResponse.setUserEmail(userEmail);

        return ResponseEntity.status(HttpStatus.OK).body(projectResponse);
    }

    @Transactional
    public ResponseEntity<String> updateProject(long id, ProjectUpdateRequest projectUpdateRequest){

        // If there is no data to update from then throw exception
        if (projectUpdateRequest.getName().isEmpty() && projectUpdateRequest.getDescription().isEmpty()){
            throw new ResourceUpdateDataException("Provided at leas one field !");
        }

        var user = getUser();
        var userEmail = user.getEmail();

        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project with the id "+ id+" is not found !"));

        // Manager is allowed to access only to the project owned by him
        if (user instanceof Manager){
            if ( ! project.getUser().getEmail().equals(userEmail)){

                throw new AccessNotAllowedException("Manager is not the owner of the project !");
            }
        }

        projectDtoMapper.UpdateProjectDto(projectUpdateRequest, project);
        project.setUpdateDate(LocalDateTime.now());

        projectRepository.save(project);

        return ResponseEntity.status(HttpStatus.OK).body("Project updated successfully !");
    }

    @Transactional
    public ResponseEntity<String> deleteProject(long id){

        var user = getUser();
        var userEmail = user.getEmail();

        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project with the id "+ id+" is not found !"));

        // Manager is allowed to access only to the project owned by him
        if (user instanceof Manager){
            if ( ! project.getUser().getEmail().equals(userEmail)){

                throw new AccessNotAllowedException("Manager is not the owner of the project !");
            }
        }

        projectRepository.delete(project);

        return ResponseEntity.ok("Project deleted successfully !");
    }

    // Helper method for retrieving current user
    private User getUser(){

        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Since authentication did not fail then the user does exist, but something might happen during the interaction
        var user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException("Authenticated user cannot be found !"));

        return user;
    }
}
