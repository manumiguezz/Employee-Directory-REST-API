package com.manumiguezz.crudapplication.service;


import com.manumiguezz.crudapplication.dao.EmployeeRepository;
import com.manumiguezz.crudapplication.entity.Employee;
import com.manumiguezz.crudapplication.exception.DuplicateResourceException;
import com.manumiguezz.crudapplication.exception.ResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public Employee findById(int employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee id not found - " + employeeId));
    }

    @Override
    public Employee create(Employee employee) {
        normalize(employee);

        if (employeeRepository.existsByEmailIgnoreCase(employee.getEmail())) {
            throw new DuplicateResourceException("An employee with that email already exists.");
        }

        employee.setId(null);

        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(int employeeId, Employee employee) {
        Employee existingEmployee = findById(employeeId);
        normalize(employee);

        if (employeeRepository.existsByEmailIgnoreCaseAndIdNot(employee.getEmail(), employeeId)) {
            throw new DuplicateResourceException("An employee with that email already exists.");
        }

        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());

        return employeeRepository.save(existingEmployee);
    }

    @Override
    public void deleteById(int employeeId) {
        Employee existingEmployee = findById(employeeId);
        employeeRepository.delete(existingEmployee);
    }

    private void normalize(Employee employee) {
        employee.setFirstName(clean(employee.getFirstName()));
        employee.setLastName(clean(employee.getLastName()));

        String normalizedEmail = clean(employee.getEmail());
        employee.setEmail(normalizedEmail == null ? null : normalizedEmail.toLowerCase(Locale.ROOT));
    }

    private String clean(String value) {
        return value == null ? null : value.trim();
    }
}
