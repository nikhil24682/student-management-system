package com.studentmanagement.service;

import java.util.List;

import com.studentmanagement.entity.Student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;



public interface StudentService {

    Student saveStudent(Student student);

    Student updateStudent(Student student);

    void deleteStudent(Integer id);

    Student getStudentById(Integer id);

    List<Student> getAllStudents();

    Page<Student> getAllStudents(Pageable pageable);

    List<Student> searchStudents(String name);
    
    List<Student> getAllStudents(Sort sort);

    long countByStatus(String status);
    
    long getTotalStudents();
    
    long getActiveStudents();
    
    long getInactiveStudents();

}