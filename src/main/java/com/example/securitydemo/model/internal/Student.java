package com.example.securitydemo.model.internal;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Student {
    UUID id;
    String name;

    @Builder
    public Student(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
