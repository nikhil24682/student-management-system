package com.studentmanagement.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import com.studentmanagement.controller.StudentController;

@ControllerAdvice(assignableTypes = StudentController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ModelAndView handleStudentNotFound() {
        ModelAndView view = new ModelAndView("error");
        view.setStatus(HttpStatus.NOT_FOUND);
        view.addObject("errorMessage", "Student not found.");
        return view;
    }
}