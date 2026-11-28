package com.studentmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.studentmanagement.entity.Student;
import com.studentmanagement.exception.StudentNotFoundException;
import com.studentmanagement.repository.StudentRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student saveStudent(Student student) {
        if (student.getId() != null) {
            return updateStudent(student);
        }
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Student student) {
        Student existing = getStudentById(student.getId());
        existing.setName(student.getName());
        existing.setEmail(student.getEmail());
        existing.setPhone(student.getPhone());
        existing.setGender(student.getGender());
        existing.setCourse(student.getCourse());
        existing.setAddress(student.getAddress());
        existing.setDateOfBirth(student.getDateOfBirth());
        existing.setAdmissionDate(student.getAdmissionDate());
        existing.setStatus(student.getStatus());
        return studentRepository.save(existing);
    }

    @Override
    public void deleteStudent(Integer id) {
        studentRepository.delete(getStudentById(id));
    }

    @Override
    public Student getStudentById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public List<Student> searchStudents(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    public List<Student> getAllStudents(Sort sort) {
        return studentRepository.findAll(sort);
    }
    
    @Override
    public long countByStatus(String status) {
        return studentRepository.countByStatus(status);
    }
    
    @Override
    public long getTotalStudents() {
        return studentRepository.count();
    }
    
    @Override
    public long getActiveStudents() {
        return studentRepository.countByStatus("Active");
    }
    
    @Override
    public long getInactiveStudents() {
        return studentRepository.countByStatus("Inactive");
    }

}