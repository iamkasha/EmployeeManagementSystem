package com.employee.ems.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.employee.ems.dto.EmployeeRequest;
import com.employee.ems.dto.EmployeeResponse;
import com.employee.ems.exception.DuplicateEmployeeException;
import com.employee.ems.exception.ResourceNotFoundException;
import com.employee.ems.model.Employee;
import com.employee.ems.repository.EmployeeRepository;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void getEmployeesMapsPagedResults() {
        Employee employee = employeeWithId(1L, "Akash", "Kasha", "akash@example.com");
        when(employeeRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<Employee>(Collections.singletonList(employee)));

        Page<EmployeeResponse> result = employeeService.getEmployees("", PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getEmailId()).isEqualTo("akash@example.com");
    }

    @Test
    void createEmployeeRejectsDuplicateEmail() {
        EmployeeRequest request = new EmployeeRequest("Akash", "Kasha", "akash@example.com");
        when(employeeRepository.existsByEmailIdIgnoreCase("akash@example.com")).thenReturn(true);

        assertThatThrownBy(() -> employeeService.createEmployee(request))
                .isInstanceOf(DuplicateEmployeeException.class)
                .hasMessage("Employee already exists with email: akash@example.com");
    }

    @Test
    void createEmployeeSavesValidEmployee() {
        EmployeeRequest request = new EmployeeRequest("Maya", "Patel", "maya@example.com");
        when(employeeRepository.existsByEmailIdIgnoreCase("maya@example.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> employeeWithId(7L, invocation.getArgument(0)));

        EmployeeResponse response = employeeService.createEmployee(request);

        assertThat(response.getId()).isEqualTo(7L);
        assertThat(response.getFirstName()).isEqualTo("Maya");

        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(captor.capture());
        assertThat(captor.getValue().getEmailId()).isEqualTo("maya@example.com");
    }

    @Test
    void getEmployeeByIdThrowsWhenMissing() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 99");
    }

    private Employee employeeWithId(Long id, String firstName, String lastName, String emailId) {
        Employee employee = new Employee(firstName, lastName, emailId);
        ReflectionTestUtils.setField(employee, "id", id);
        return employee;
    }

    private Employee employeeWithId(Long id, Employee employee) {
        ReflectionTestUtils.setField(employee, "id", id);
        return employee;
    }
}
