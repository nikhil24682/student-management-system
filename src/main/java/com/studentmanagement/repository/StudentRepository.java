package com.studentmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studentmanagement.entity.Student;

import org.springframework.data.domain.Sort;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findByNameContainingIgnoreCase(String name);
    long countByStatus(String status);
    
    

}