package dev.innom.Task_Tracker_API.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@DiscriminatorValue("ADMIN")
@Getter
@Setter
public class Admin extends User{

    @OneToMany(mappedBy = "user")
    private List<Project> projects;
    @OneToMany(mappedBy = "creator")
    private List<Task> tasks;
}
