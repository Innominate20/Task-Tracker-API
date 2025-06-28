package dev.innom.Task_Tracker_API.repository;

import dev.innom.Task_Tracker_API.entity.Project;
import dev.innom.Task_Tracker_API.entity.RegularUser;
import dev.innom.Task_Tracker_API.entity.Task;
import dev.innom.Task_Tracker_API.entity.User;
import dev.innom.Task_Tracker_API.enums.Priority;
import dev.innom.Task_Tracker_API.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// methods with creator or regular user are used when current user is on of their type
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByStatus(Status status, Pageable pageable);

    Page<Task> findAllByCreator(User creator, Pageable pageable);

    Page<Task> findAllByAssignedUser(RegularUser regularUser, Pageable pageable);

    Page<Task> findByStatusAndCreator(User creator, Status status, Pageable pageable);

    Page<Task> findByStatusAndAssignedUser(RegularUser regularUser, Status status, Pageable pageable);

    Page<Task> findByPriority(Priority priority, Pageable pageable);

    Page<Task> findByPriorityAndCreator(User creator, Priority priority, Pageable pageable);

    Page<Task> findByPriorityAndAssignedUser(RegularUser regularUser, Priority priority, Pageable pageable);

    Page<Task> findByStatusAndPriority(Status status, Priority priority, Pageable pageable);
    Page<Task> findByStatusAndPriorityAndCreator(User creator, Status status, Priority priority, Pageable pageable);

    Page<Task> findByStatusAndPriorityAndAssignedUser(RegularUser regularUser, Status status, Priority priority, Pageable pageable);

    Optional<Task> findById(long id);
    Page<Task> findByProjectIn(List<Task> projects, Pageable pageable);
}
