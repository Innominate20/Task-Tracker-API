package dev.innom.Task_Tracker_API.service;

import dev.innom.Task_Tracker_API.dto.*;
import dev.innom.Task_Tracker_API.entity.*;
import dev.innom.Task_Tracker_API.enums.Priority;
import dev.innom.Task_Tracker_API.enums.Status;
import dev.innom.Task_Tracker_API.exception.*;
import dev.innom.Task_Tracker_API.mapper.TaskDtoMapper;
import dev.innom.Task_Tracker_API.repository.ProjectRepository;
import dev.innom.Task_Tracker_API.repository.TaskRepository;
import dev.innom.Task_Tracker_API.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.query.sqm.mutation.internal.TableKeyExpressionCollector;
import org.mapstruct.control.MappingControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.geom.RectangularShape;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final UserRepository userRepository;
    private final TaskDtoMapper taskDtoMapper;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskService(UserRepository userRepository, TaskDtoMapper taskDtoMapper, TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.taskDtoMapper = taskDtoMapper;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public ResponseEntity<String> createTask(TaskCreateRequest taskCreateRequest){

        var user = getUser();

        var project = projectRepository.findById(taskCreateRequest.getProjectId()).orElseThrow(() -> new ResourceNotFoundException("No projects found with the id "+taskCreateRequest.getProjectId()+ " !"));

        Task taskToSave = taskDtoMapper.toTask(taskCreateRequest);

        taskToSave.setProject(project);
        taskToSave.setCreator(user);
        taskToSave.setAssignedUser(null);

        taskRepository.save(taskToSave);

        return ResponseEntity.status(HttpStatus.CREATED).body("Task created successfully !");
    }

    public ResponseEntity<Page<TaskResponse>> reviewTasks(Pageable pageable){

        var user = getUser();

        List<Task> tasks;

        // It is a must to identify exact type of the user since task is not the field of the base class

        if (user instanceof Admin){
            Admin admin = (Admin) user;
            tasks = admin.getTasks();
        }
        else if(user instanceof Manager){
            Manager manager = (Manager) user;
            tasks = manager.getTasks();
        }
        else {
            RegularUser regularUser = (RegularUser) user;
            tasks = regularUser.getTasks();
        }

        if (tasks.isEmpty()){
            throw new ResourceNotFoundException("No Tasks found for the user !");
        }

        Page<Task> pagedTask = taskRepository.findByProjectIn(tasks, pageable);

        var responsePage = pagedTask.map(elem -> {
            TaskResponse taskResponse = taskDtoMapper.toTaskResponse(elem);
            taskResponse.setCreator(user.getEmail());
            taskResponse.setProject(elem.getProject().getName());
            taskResponse.setAssignedUser(elem.getAssignedUser().getEmail());
            return taskResponse;
        });

        return ResponseEntity.status(HttpStatus.OK).body(responsePage);
    }

    @Transactional
    public ResponseEntity<String> deleteTask(long id){

        var user = getUser();

        Task task =  taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task with the id "+ id+ " is not found !"));

        if (user instanceof Manager){
            if ( !task.getCreator().getEmail().equals(user.getEmail())){

                throw new AccessNotAllowedException("Manager is not the owner of the task !");
            }
        }

        taskRepository.delete(task);

        return ResponseEntity.ok("Task deleted successfully !");
    }

    @Transactional
    public ResponseEntity<String> updateTask(long id, TaskUpdateRequest taskUpdateRequest){

        // throw exception if there is no data in updatee dto
        if ((taskUpdateRequest.getTitle() == null || taskUpdateRequest.getTitle().isEmpty()) &&
                (taskUpdateRequest.getDescription() == null || taskUpdateRequest.getDescription().isEmpty()) &&
                taskUpdateRequest.getDueDate() == null &&
                taskUpdateRequest.getPriority() == null &&
                taskUpdateRequest.getStatus() == null) {

            throw new ResourceUpdateDataException("Provided at leas one field !");
        }
        var user = getUser();

        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task with the id "+ id+ " is not found !"));

        // Managers and regular users ( USER)  can update their tasks only
        if (user instanceof Manager ){
            if ( !task.getCreator().getEmail().equals(user.getEmail())){

                throw new AccessNotAllowedException("Manager is not the owner of the task !");
            }
        }
        else if(user instanceof RegularUser){
            if ( !task.getAssignedUser().getEmail().equals(user.getEmail())){

                throw new AccessNotAllowedException("Regular user is not assigned to the task !");
            }
        }

        taskDtoMapper.UpdateTaskDto(taskUpdateRequest, task);
        task.setUpdateDate(LocalDateTime.now());

        return ResponseEntity.ok("Task updated successfully !");
    }

    @Transactional
    public ResponseEntity<String> assignTask(long taskId, long userId){

        var user = getUser();

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task with the id "+ taskId+ " is not found !"));

        if (user instanceof Manager ){
            if ( !task.getCreator().getEmail().equals(user.getEmail())){

                throw new AccessNotAllowedException("Manager is not the owner of the task !");
            }
        }

        User regularUser =  userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with the id " + userId+ " !"));

        if (! (regularUser instanceof RegularUser)){
            throw new UserTypeMismatchException("User with the id " + userId + " is not regular user. Can't assign task !");
        }

        task.setAssignedUser((RegularUser) regularUser);

        return ResponseEntity.ok("Task assigned successfully !");
    }

    @Transactional
    public ResponseEntity<String> changeTaskStatus(ChangeStatusDto changeStatusDto, long id){

        var user = getUser();

        if (!(user instanceof RegularUser)){
            throw new AccessNotAllowedException("Only assigned user can change the status !");
        }

        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task with the id "+ id+ " is not found !"));

        if (!task.getAssignedUser().getEmail().equals(user.getEmail())){
            throw new AccessNotAllowedException("User is not assigned to the task with the id "+ id+ " !");
        }

        task.setStatus(changeStatusDto.getStatus());

        taskRepository.save(task);

        return ResponseEntity.ok("Task status updated successfully !");
    }

    public ResponseEntity<TaskResponse> getTaskById(long id){

        var user = getUser();

        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceUpdateDataException("Task with the id " + id + " is not found !" ));

        if (user instanceof Manager ){
            if ( !task.getCreator().getEmail().equals(user.getEmail())){

                throw new AccessNotAllowedException("Manager is not the owner of the task !");
            }
        }else if(user instanceof RegularUser){
            if ( !task.getAssignedUser().getEmail().equals(user.getEmail())){

                throw new AccessNotAllowedException("Regular user is not assigned to the task !");
            }
        }

        var taskResponse = taskDtoMapper.toTaskResponse(task);
        taskResponse.setAssignedUser(task.getAssignedUser().getEmail());
        taskResponse.setProject(task.getProject().getName());
        taskResponse.setCreator(task.getCreator().getEmail());

        return ResponseEntity.ok(taskResponse);
    }

    public ResponseEntity<Page<TaskResponse>> filterTasks(Status status, Priority priority, int size, int page){

        var user = getUser();



        Pageable pageable = PageRequest.of(page,size);

        Page<Task> tasks;

        if (user instanceof Manager){
            if (status != null && priority != null) {
                tasks = taskRepository.findByStatusAndPriorityAndCreator(user,status, priority, pageable);
            } else if (status != null) {
                tasks = taskRepository.findByStatusAndCreator(user,status, pageable);
            } else if (priority != null) {
                tasks = taskRepository.findByPriorityAndCreator(user, priority, pageable);
            } else {
                tasks = taskRepository.findAllByCreator(user, pageable);
            }
        }

        else if (user instanceof RegularUser){
            if (status != null && priority != null) {
                tasks = taskRepository.findByStatusAndPriorityAndAssignedUser((RegularUser) user,status, priority, pageable);
            } else if (status != null) {
                tasks = taskRepository.findByStatusAndAssignedUser((RegularUser) user,status, pageable);
            } else if (priority != null) {
                tasks = taskRepository.findByPriorityAndAssignedUser((RegularUser) user, priority, pageable);
            } else {
                tasks = taskRepository.findAllByAssignedUser((RegularUser) user, pageable);
            }

        }
        else {
            if (status != null && priority != null) {
                tasks = taskRepository.findByStatusAndPriority(status, priority, pageable);
            } else if (status != null) {
                tasks = taskRepository.findByStatus(status, pageable);
            } else if (priority != null) {
                tasks = taskRepository.findByPriority(priority, pageable);
            } else {
                tasks = taskRepository.findAll(pageable);
            }
        }
        var taskResponse = tasks.map(elem -> {
            TaskResponse taskRsp = taskDtoMapper.toTaskResponse(elem);
            taskRsp.setCreator(elem.getCreator().getEmail());
            taskRsp.setProject(elem.getProject().getName());
            taskRsp.setAssignedUser(elem.getAssignedUser().getEmail());

            return taskRsp;
        });

        return ResponseEntity.ok(taskResponse);
    }

    // Helper method for retrieving current user
    private User getUser(){

        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Since authentication did not fail then the user does exist, but something might happen during the interaction
        var user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException("Authenticated user cannot be found !"));

        return user;
    }
}
