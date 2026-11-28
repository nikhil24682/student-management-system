package com.studentmanagement.rest;

import java.time.LocalDate;

import com.studentmanagement.entity.Student;

public record StudentResponse(
        Integer id,
        String name,
        String email,
        String phone,
        String gender,
        String course,
        String address,
        LocalDate dateOfBirth,
        LocalDate admissionDate,
        String status) {

    public static StudentResponse from(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getPhone(),
                student.getGender(),
                student.getCourse(),
                student.getAddress(),
                student.getDateOfBirth(),
                student.getAdmissionDate(),
                student.getStatus());
    }
}
