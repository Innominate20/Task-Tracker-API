package dev.innom.Task_Tracker_API.controller;

import dev.innom.Task_Tracker_API.dto.TaskCreateRequest;
import dev.innom.Task_Tracker_API.dto.TaskResponse;
import dev.innom.Task_Tracker_API.dto.TaskUpdateRequest;
import dev.innom.Task_Tracker_API.enums.Priority;
import dev.innom.Task_Tracker_API.enums.Status;
import dev.innom.Task_Tracker_API.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<String> createTask( @Valid @RequestBody TaskCreateRequest request) {

        return taskService.createTask(request);
    }

    @GetMapping("/review")
    public ResponseEntity<Page<TaskResponse>> reviewTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskService.reviewTasks(pageable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable long id) {

        return taskService.deleteTask(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable long id,
                                             @RequestBody @Valid TaskUpdateRequest taskUpdateRequest) {
        return taskService.updateTask(id, taskUpdateRequest);
    }

    @PostMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<String> assignTask(@PathVariable long taskId, @PathVariable long userId) {

        return taskService.assignTask(taskId, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<TaskResponse>> filterTasks(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "0") int page) {

        return taskService.filterTasks(status, priority, size, page);
    }


}
