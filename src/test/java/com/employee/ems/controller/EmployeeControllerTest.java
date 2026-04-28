package com.employee.ems.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.employee.ems.dto.EmployeeRequest;
import com.employee.ems.dto.EmployeeResponse;
import com.employee.ems.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void getEmployeesReturnsPagedEmployees() throws Exception {
        when(employeeService.getEmployees(eq("akash"), any(Pageable.class)))
                .thenReturn(new PageImpl<EmployeeResponse>(Collections.singletonList(
                        new EmployeeResponse(1L, "Akash", "Kasha", "akash@example.com")
                )));

        mockMvc.perform(get("/api/v1/employees").param("search", "akash"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].emailId").value("akash@example.com"));
    }

    @Test
    void createEmployeeReturnsCreatedEmployee() throws Exception {
        EmployeeRequest request = new EmployeeRequest("Maya", "Patel", "maya@example.com");
        when(employeeService.createEmployee(any(EmployeeRequest.class)))
                .thenReturn(new EmployeeResponse(10L, "Maya", "Patel", "maya@example.com"));

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/employees/10"))
                .andExpect(jsonPath("$.firstName").value("Maya"));
    }

    @Test
    void createEmployeeRejectsInvalidEmail() throws Exception {
        EmployeeRequest request = new EmployeeRequest("Maya", "Patel", "not-an-email");

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.validationErrors.emailId").exists());
    }

    @Test
    void deleteEmployeeReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/employees/{id}", 5L))
                .andExpect(status().isNoContent());

        verify(employeeService).deleteEmployee(5L);
    }
}
