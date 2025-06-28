package dev.innom.Task_Tracker_API.controller;

import dev.innom.Task_Tracker_API.dto.ProjectCreateRequest;
import dev.innom.Task_Tracker_API.dto.ProjectResponse;
import dev.innom.Task_Tracker_API.dto.ProjectUpdateRequest;
import dev.innom.Task_Tracker_API.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<String> createProject(@Valid @RequestBody ProjectCreateRequest projectCreateRequest){

        return projectService.createProject(projectCreateRequest);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> reviewProjects(){

        return projectService.reviewProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable("id") long id){

        return projectService.findProjectById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProject(@PathVariable("id") long id, @RequestBody ProjectUpdateRequest projectUpdateRequest){

        return projectService.updateProject(id, projectUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable("id") long id){

        return projectService.deleteProject(id);
    }
}
