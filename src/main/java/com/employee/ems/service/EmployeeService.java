package com.employee.ems.service;

import com.employee.ems.dto.EmployeeRequest;
import com.employee.ems.dto.EmployeeResponse;
import com.employee.ems.exception.DuplicateEmployeeException;
import com.employee.ems.exception.ResourceNotFoundException;
import com.employee.ems.model.Employee;
import com.employee.ems.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getEmployees(String search, Pageable pageable) {
        Page<Employee> employees;
        if (search == null || search.trim().isEmpty()) {
            employees = employeeRepository.findAll(pageable);
        } else {
            String term = search.trim();
            employees = employeeRepository
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailIdContainingIgnoreCase(
                            term,
                            term,
                            term,
                            pageable
                    );
        }

        return employees.map(EmployeeMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {
        return EmployeeMapper.toResponse(findEmployee(id));
    }

    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (employeeRepository.existsByEmailIdIgnoreCase(request.getEmailId())) {
            throw new DuplicateEmployeeException("Employee already exists with email: " + request.getEmailId());
        }

        Employee employee = new Employee(request.getFirstName(), request.getLastName(), request.getEmailId());
        return EmployeeMapper.toResponse(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = findEmployee(id);
        employee.update(request.getFirstName(), request.getLastName(), request.getEmailId());
        return EmployeeMapper.toResponse(employee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = findEmployee(id);
        employeeRepository.delete(employee);
    }

    private Employee findEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }
}
