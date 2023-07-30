package com.zuzannastolc.gradebook.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private int gradeId;
    @Column(name = "grade")
    private int grade;
    @Column(name = "description")
    private String description;

    public Grade() {
    }

    public Grade(int grade, String description) {
        this.grade = grade;
        this.description = description;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "gradeId=" + gradeId +
                ", grade=" + grade +
                ", description='" + description + '\'' +
                '}';
    }
}
