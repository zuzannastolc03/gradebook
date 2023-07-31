package com.zuzannastolc.gradebook.service;

import com.zuzannastolc.gradebook.entity.*;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AppService {
    String getLoggedUsername(Authentication authentication);

    String getLoggedAuthorities(Authentication authentication);

    void addNewUserWithAuthorities(WebUser webUser);

    User findUserByUsername(String username);

    void addNewStudent(Student student, SchoolClass schoolClass);

    void addNewTeacher(Teacher teacher);

    void disableUser(User user);

    void changePassword(User user, String newPassword);

    void updateStudentWithUser(Student newStudent, Student oldStudent);

    void updateTeacherWithUser(Teacher newTeacher, Teacher oldTeacher);
    List<String> findAllClasses();
    SchoolClass findClassByClassName(String className);
    void addNewClass(SchoolClass schoolClass);
    List<?> getStudentsInClass(String className);
    Subject findSubjectBySubjectName(String subjectName);
    void addNewSubject(Subject subject);
    Teacher findTeacherByUsername(String username);
    Student findStudentByUsername(String username);
    void assignTeacherToSubject(Teacher teacher, Subject subject);
    List<Subject> getListOfTeachersSubjects(Teacher teacher);
    List<Teacher> getListOfSubjectsTeachers(Subject subject);
    void assignClassToSubject(SchoolClass schoolClass, Subject subject);
    List<Subject> getListOfClassesSubjects(SchoolClass schoolClass);
    List<SchoolClass> getListOfSubjectsClasses(Subject subject);
    List<?> getStudentsGradesFromSubject(Student student, Subject subject);
    void addGrade(Grade grade, Teacher teacher, Subject subject, Student student);
    void updateGrade(Grade grade);
    Grade findGradeById(int id);
    void deleteGrade(int id);

}
