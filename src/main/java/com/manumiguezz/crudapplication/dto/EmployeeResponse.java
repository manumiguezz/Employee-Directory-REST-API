package com.manumiguezz.crudapplication.dto;

import com.manumiguezz.crudapplication.entity.Employee;

public record EmployeeResponse(
        Integer id,
        String firstName,
        String lastName,
        String email
) {

    public static EmployeeResponse fromEntity(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail()
        );
    }
}
