package com.zuzannastolc.gradebook.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private int id;
    @Column(name = "students_username")
    private String studentsName;
    @Column(name = "teachers_username")
    private String teachersName;
    @Column(name = "subject_id")
    private int subjectId;
    @Column(name = "grade")
    private int Grade;
}
