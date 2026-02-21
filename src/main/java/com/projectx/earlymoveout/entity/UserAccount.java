package com.projectx.earlymoveout.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_account")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // login name

    @Column(nullable = false)
    private String password; // bcrypt

    @Column(nullable = false)
    private String role; // FACULTY / HOD / GATE

    // Only for FACULTY users (can keep blank for HOD/GATE)
    private String facultyId;

    public UserAccount() {}

    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFacultyId() { return facultyId; }
    public void setFacultyId(String facultyId) { this.facultyId = facultyId; }
}
