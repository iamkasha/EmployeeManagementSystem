package com.employee.ems.dto;

public class EmployeeResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String emailId;

    public EmployeeResponse(Long id, String firstName, String lastName, String emailId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailId() {
        return emailId;
    }
}
