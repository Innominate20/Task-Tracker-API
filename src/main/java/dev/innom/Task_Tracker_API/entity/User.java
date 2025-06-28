package dev.innom.Task_Tracker_API.entity;


import dev.innom.Task_Tracker_API.enums.Role;


import jakarta.persistence.*;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
@Getter
@Setter
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

}
