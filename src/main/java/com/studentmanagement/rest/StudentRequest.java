package com.studentmanagement.rest;

import java.time.LocalDate;

import com.studentmanagement.entity.Student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentRequest(
        @NotBlank(message = "Student name is required") String name,
        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email") String email,
        @NotBlank(message = "Phone number is required") String phone,
        @NotBlank(message = "Gender is required") String gender,
        @NotBlank(message = "Course is required") String course,
        @NotBlank(message = "Address is required") String address,
        @NotNull(message = "Date of Birth is required") LocalDate dateOfBirth,
        @NotNull(message = "Admission Date is required") LocalDate admissionDate,
        @NotBlank(message = "Status is required") String status) {

    public Student toStudent(Integer id) {
        return new Student(id, name, email, phone, gender, course, address,
                dateOfBirth, admissionDate, status);
    }
}
