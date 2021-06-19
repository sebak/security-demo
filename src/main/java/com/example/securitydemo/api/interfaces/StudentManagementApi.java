package com.example.securitydemo.api.interfaces;

import com.example.securitydemo.model.internal.Student;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public interface StudentManagementApi {
    static final String MANAGEMENT_NAME = "/management";
    static final String API_NAME = "/student";

    @GetMapping
    List<Student> getAllStudent();

    @PostMapping
    void registerNewStudent(@RequestBody Student student) throws Exception;

    @DeleteMapping(path = "{studentId}")
    void deleteStudent(@PathVariable("studentId") UUID studentId);

    @PutMapping
    void updateStudent(@RequestBody Student updatedStudent) throws Exception;
}
