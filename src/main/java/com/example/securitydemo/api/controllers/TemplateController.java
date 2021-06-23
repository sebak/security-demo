package com.example.securitydemo.api.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // when wwant to retrieve a view we can use it instead of @RestController
@RequestMapping("/")
public class TemplateController {

    @GetMapping("/login")
    public String getLoginView() {
        return "login"; // it will return a login.html inside of resources/templates
    }

    @GetMapping("/courses")
    public String getCourses() {
        return "courses"; // it will return a courses.html inside of resources/templates
    }
}
