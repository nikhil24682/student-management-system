package com.studentmanagement.exception;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(Integer id) {
        super("Student " + id + " was not found");
    }
}
