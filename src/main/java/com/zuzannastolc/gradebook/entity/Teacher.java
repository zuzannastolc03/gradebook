package com.zuzannastolc.gradebook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private int teacherId;
    @Column(name = "first_name")
    @Pattern(regexp = "^[A-Z][a-z]*$", message = "Invalid first name.")
    @NotBlank
    private String firstName;
    @Column(name = "last_name")
    @Pattern(regexp = "^[A-Z][a-z]*$", message = "Invalid last name.")
    @NotBlank
    private String lastName;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "teachers_subjects",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    @JsonIgnore
    private List<Subject> subjects;
    @OneToMany(mappedBy = "teacher",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JsonIgnore
    private List<Grade> grades;

    public Teacher() {
    }

    public Teacher(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public void addSubject(Subject subject) {
        if (subjects == null) {
            subjects = new ArrayList<>();
        }
        subjects.add(subject);
    }

    public void addGrade(Grade grade) {
        if (grades == null) {
            grades = new ArrayList<>();
        }
        grades.add(grade);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId=" + teacherId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName +
                '}';
    }
}
