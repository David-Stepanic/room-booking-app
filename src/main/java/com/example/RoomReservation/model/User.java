package com.example.RoomReservation.model;

import com.example.RoomReservation.model.constans.Department;
import com.example.RoomReservation.model.constans.Role;
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
    private String indexNumber;
    @Enumerated(EnumType.STRING)
    private Department department;

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
