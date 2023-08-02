package com.zuzannastolc.gradebook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classes")
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private int classId;
    @Column(name = "class_name")
    private String className;
    @OneToMany(mappedBy = "schoolClass",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JsonIgnore
    private List<Student> students;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "classes_subjects",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    @JsonIgnore
    private List<Subject> subjects;

    public SchoolClass() {
    }

    public SchoolClass(String className) {
        this.className = className;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addStudent(Student tempStudent) {
        if(students == null){
            students = new ArrayList<>();
        }
        students.add(tempStudent);
        tempStudent.setSchoolClass(this);
    }

    public void addSubject(Subject subject){
        if(subjects == null){
            subjects = new ArrayList<>();
        }
        subjects.add(subject);
    }

    @Override
    public String toString() {
        return "SchoolClass{" +
                "classId=" + classId +
                ", className='" + className + '\'' +
                '}';
    }
}
