package com.zuzannastolc.gradebook.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "grade")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Long id;
    @Column(name = "student_id")
    private Long studentId;
    @Column(name = "teacher_id")
    private Long teacherId;
    @Column(name = "subject_id")
    private Long subjectId;
    @Column(name = "grade")
    private int Grade;

    public Grade() {
    }

    public Grade(Long studentId, Long teacherId, Long subjectId, int grade) {
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.subjectId = subjectId;
        Grade = grade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public int getGrade() {
        return Grade;
    }

    public void setGrade(int grade) {
        Grade = grade;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", teacherId=" + teacherId +
                ", subjectId=" + subjectId +
                ", Grade=" + Grade +
                '}';
    }
}
