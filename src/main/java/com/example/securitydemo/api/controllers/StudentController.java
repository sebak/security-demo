package com.example.securitydemo.api.controllers;

import com.example.securitydemo.api.interfaces.StudentApi;
import com.example.securitydemo.model.internal.Student;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.example.securitydemo.api.RootPaths.BASE_API_V1_PATH;
import static com.example.securitydemo.api.interfaces.StudentApi.API_NAME;

@RestController
@RequestMapping(path = BASE_API_V1_PATH + API_NAME)
public class StudentController implements StudentApi {

    private static List<Student> students = List.of(Student.builder().id(UUID.fromString("2cee4a06-cb45-11eb-9d2c-34e6d72a5caa")).name("Student_1").build(),
            Student.builder().id(UUID.fromString("eb8d7ad4-cd4f-11eb-b5f2-34e6d72a5caa")).name("Student_2").build());
    @Override
    public Student getStudentById(UUID studentId) throws Exception {
        return students.stream()
                .filter(student -> student.getId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new Exception("No student found with id " + studentId));
    }
}
