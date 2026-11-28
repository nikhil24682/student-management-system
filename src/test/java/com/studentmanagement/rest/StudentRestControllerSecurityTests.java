package com.studentmanagement.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import com.studentmanagement.config.SecurityConfig;
import com.studentmanagement.controller.StudentController;
import com.studentmanagement.entity.Student;
import com.studentmanagement.security.CustomUserDetailsService;
import com.studentmanagement.service.StudentService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.studentmanagement.exception.StudentNotFoundException;

@WebMvcTest(controllers = {StudentController.class, StudentRestController.class})
@Import(SecurityConfig.class)
class StudentRestControllerSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void anonymousApiRequestIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void authenticatedUserCanReadApiButCannotWrite() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of());

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/students/1").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void apiWritesRequireCsrfAndReturnResponseDto() throws Exception {
        when(studentService.saveStudent(any(Student.class))).thenAnswer(invocation -> {
            Student student = invocation.getArgument(0);
            student.setId(12);
            return student;
        });

        mockMvc.perform(post("/api/students")
                        .contentType("application/json")
                        .content(validStudentJson()))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/students")
                        .with(csrf())
                        .contentType("application/json")
                        .content(validStudentJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(12))
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void regularUserCannotOpenStudentWriteFormOrDeleteByGet() throws Exception {
        mockMvc.perform(get("/students/add"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/students/delete/1"))
                .andExpect(status().isMethodNotAllowed());
        mockMvc.perform(post("/students/delete/1").with(csrf()))
                .andExpect(status().isForbidden());
        verify(studentService, org.mockito.Mockito.never()).deleteStudent(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void browserDeleteRequiresCsrfAndAdminRole() throws Exception {
        mockMvc.perform(post("/students/delete/1"))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/students/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentService).deleteStudent(1);
    }

    @Test
    @WithMockUser(roles = "USER")
    void missingStudentHasSafeApiNotFoundResponse() throws Exception {
        when(studentService.getStudentById(404)).thenThrow(new StudentNotFoundException(404));

        mockMvc.perform(get("/api/students/404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Student not found."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void invalidStudentRequestReturnsSafeValidationDetails() throws Exception {
        mockMvc.perform(post("/api/students")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Request validation failed."))
                .andExpect(jsonPath("$.errors[0].field").exists());
    }

    private String validStudentJson() {
        return """
                {
                  "id": 999,
                  "name": "Jane Doe",
                  "email": "jane.doe@example.com",
                  "phone": "9876543210",
                  "gender": "Female",
                  "course": "Computer Science",
                  "address": "123 Main Street",
                  "dateOfBirth": "%s",
                  "admissionDate": "%s",
                  "status": "Active"
                }
                """.formatted(LocalDate.of(2004, 5, 15), LocalDate.of(2024, 7, 1));
    }
}
