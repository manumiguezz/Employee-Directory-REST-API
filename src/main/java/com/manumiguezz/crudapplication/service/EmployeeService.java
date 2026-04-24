package com.manumiguezz.crudapplication.service;


import com.manumiguezz.crudapplication.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> findAll();

    Employee findById(int employeeId);

    Employee create(Employee employee);

    Employee update(int employeeId, Employee employee);

    void deleteById(int employeeId);

}
