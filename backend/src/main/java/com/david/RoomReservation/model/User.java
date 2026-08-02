package com.david.RoomReservation.model;

import com.david.RoomReservation.model.constans.Department;
import com.david.RoomReservation.model.constans.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Department department;
    private boolean enabled;

    public User() {
    }

    public User(String email, String firstName, String lastName, String password, Role role, Department department, boolean enabled) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
        this.department = department;
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
