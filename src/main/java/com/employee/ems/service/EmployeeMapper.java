package com.employee.ems.service;

import com.employee.ems.dto.EmployeeResponse;
import com.employee.ems.model.Employee;

final class EmployeeMapper {

    private EmployeeMapper() {
    }

    static EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmailId()
        );
    }
}
