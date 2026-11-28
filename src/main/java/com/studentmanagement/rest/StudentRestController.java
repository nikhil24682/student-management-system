package com.studentmanagement.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.studentmanagement.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {

    private final StudentService studentService;

    public StudentRestController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentResponse> getAllStudents() {
        return studentService.getAllStudents().stream()
                .map(StudentResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public StudentResponse getStudentById(@PathVariable Integer id) {
        return StudentResponse.from(studentService.getStudentById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse saveStudent(@Valid @RequestBody StudentRequest request) {
        return StudentResponse.from(studentService.saveStudent(request.toStudent(null)));
    }

    @PutMapping("/{id}")
    public StudentResponse updateStudent(@PathVariable Integer id,
                                         @Valid @RequestBody StudentRequest request) {
        return StudentResponse.from(studentService.updateStudent(request.toStudent(id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/search")
    public List<StudentResponse> searchStudent(@RequestParam String name) {
        return studentService.searchStudents(name).stream()
                .map(StudentResponse::from)
                .collect(Collectors.toList());
    }
}