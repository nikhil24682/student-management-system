package com.studentmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.studentmanagement.service.StudentService;

@Controller
public class LoginController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("totalStudents",
                studentService.getTotalStudents());

        model.addAttribute("activeStudents",
                studentService.getActiveStudents());

        model.addAttribute("inactiveStudents",
                studentService.getInactiveStudents());

        return "dashboard";
    }
}