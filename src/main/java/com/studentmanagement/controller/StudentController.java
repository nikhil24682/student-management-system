package com.studentmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.studentmanagement.service.StudentService;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.studentmanagement.entity.Student;

import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public String viewStudents(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Page<Student> studentPage =
                studentService.getAllStudents(PageRequest.of(page, 5));

        model.addAttribute("studentPage", studentPage);
        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());

        return "students";
    }
    
    @GetMapping("/students/add")
    public String showAddStudentForm(Model model) {

        model.addAttribute("student", new Student());

        return "student-form";
    }

    @PostMapping("/students/save")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult result) {

        if (result.hasErrors()) {
            return "student-form";
        }

        studentService.saveStudent(student);

        return "redirect:/students";
    }
    
    @GetMapping("/students/edit/{id}")
    public String showEditStudentForm(@PathVariable Integer id, Model model) {

        Student student = studentService.getStudentById(id);

        System.out.println("DOB = " + student.getDateOfBirth());
        System.out.println("Admission = " + student.getAdmissionDate());

        model.addAttribute("student", student);

        return "student-form";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Integer id) {

        studentService.deleteStudent(id);

        return "redirect:/students";
    }
    
    @GetMapping("/students/search")
    public String searchStudents(String name, Model model) {

        model.addAttribute("students", studentService.searchStudents(name));

        return "students";
    }
    
    @GetMapping("/test-error")
    public String testError() {

        int x = 10 / 0;

        return "students";
    }
    

}