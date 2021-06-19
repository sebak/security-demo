package com.example.securitydemo.api.controllers;

import com.example.securitydemo.api.interfaces.StudentManagementApi;
import com.example.securitydemo.model.internal.Student;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.securitydemo.api.RootPaths.BASE_API_V1_PATH;
import static com.example.securitydemo.api.interfaces.StudentManagementApi.API_NAME;
import static com.example.securitydemo.api.interfaces.StudentManagementApi.MANAGEMENT_NAME;

@RestController
@RequestMapping(path = MANAGEMENT_NAME + BASE_API_V1_PATH + API_NAME)
public class StudentManagementController implements StudentManagementApi {

    private static List<Student> students = new ArrayList<>();

    static {
        students.add(Student.builder().id(UUID.fromString("2cee4a06-cb45-11eb-9d2c-34e6d72a5caa")).name("Student_1").build());
        students.add(Student.builder().id(UUID.fromString("eb8d7ad4-cd4f-11eb-b5f2-34e6d72a5caa")).name("Student_2").build());
        students.add(Student.builder().id(UUID.fromString("59eaa45e-cf60-11eb-949c-34e6d72a5caa")).name("Student_3").build());
    }

    @Override
    public List<Student> getAllStudent () {
        return students;
    }

    /**
     if i call http://localhost:7902/management/api/v1/student (post) delete, and put request by any user without adding
     .csrf().disable() on ApplicationSecurityConfig in method configure the request will fail because by default spring security try to protect our api
     to avoid to write on it
     */
    @Override
    public void registerNewStudent(Student student) throws Exception {
        UUID newStudentId = UUID.randomUUID();
        boolean studentExist = studentExist(newStudentId);

        if (!studentExist)  {
            student.setId(newStudentId);
            students.add(student);
        } else {
            throw new Exception(" Student exist");
        }

    }

    @Override
    public void deleteStudent(UUID studentId) {
        students.stream()
                .filter(student -> student.getId().equals(studentId))
                .map(students::remove);
    }

    @Override
    public void updateStudent(Student updatedStudent) throws Exception {
        boolean studentExist = studentExist(updatedStudent.getId());

        if (!studentExist)  {
            throw new Exception("No student found with id : " + updatedStudent.getId());
        }

        students.stream()
                .filter(student1 -> student1.getId().equals(updatedStudent.getId()))
                .map(oldStudent -> {
                    students.remove(oldStudent);
                    students.add(updatedStudent);
                    return students;
                });
    }

    private boolean studentExist(UUID studentId) {
        return students.stream()
                .map(Student::getId)
                .collect(Collectors.toList())
                .stream().anyMatch(studentId::equals);
    }
}
