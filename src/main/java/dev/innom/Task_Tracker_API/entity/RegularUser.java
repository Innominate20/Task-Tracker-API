package dev.innom.Task_Tracker_API.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@DiscriminatorValue("USER")
@Getter
@Setter
public class RegularUser extends User{

    @OneToMany(mappedBy = "assignedUser")
    private List<Task> tasks;
}
