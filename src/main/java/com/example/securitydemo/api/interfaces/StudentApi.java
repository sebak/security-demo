package com.example.securitydemo.api.interfaces;

import com.example.securitydemo.model.internal.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface StudentApi {

    static final String API_NAME = "/student" ;

    @GetMapping("/{studentId}")
    Student getStudentById(@PathVariable UUID studentId) throws Exception;
}
