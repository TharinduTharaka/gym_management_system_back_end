package com.kiu.gym_management_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "login")
public class LoginEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;
    @Column(name = "email", unique = true, length = 40)
    private String email;
    @Column(name = "password")
    private String password;

    @Column(name = "username")
    private String username;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "role")
    private String role;

//    @Column(name = "ability")
//    private String ability;

//    @Enumerated(EnumType.STRING)
//    private Role role;

}
