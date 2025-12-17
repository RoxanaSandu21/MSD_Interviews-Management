package com.example.interviewmgmt.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Interviewer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    private String department; // optional (Development, Testing, etc.)

    @OneToMany(mappedBy = "interviewer")
    @JsonManagedReference("interviewer-interviews")
    private List<Interview> interviews = new ArrayList<>();


    public Interviewer() {}

    public Interviewer(String name, String email, String department) {
        this.name = name;
        this.email = email;
        this.department = department;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }

    public void setDepartment(String department) { this.department = department; }
}
