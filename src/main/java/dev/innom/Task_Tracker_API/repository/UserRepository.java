package dev.innom.Task_Tracker_API.repository;

import dev.innom.Task_Tracker_API.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
