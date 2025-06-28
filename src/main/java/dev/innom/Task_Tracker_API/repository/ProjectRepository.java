package dev.innom.Task_Tracker_API.repository;

import dev.innom.Task_Tracker_API.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findById(long id);
}
