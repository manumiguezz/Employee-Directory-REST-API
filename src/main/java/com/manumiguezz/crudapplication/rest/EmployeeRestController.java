package com.manumiguezz.crudapplication.rest;

import com.manumiguezz.crudapplication.dto.EmployeeRequest;
import com.manumiguezz.crudapplication.dto.EmployeeResponse;
import com.manumiguezz.crudapplication.entity.Employee;
import com.manumiguezz.crudapplication.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private final EmployeeService employeeService;

    public EmployeeRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeResponse> findAll() {
        return employeeService.findAll()
                .stream()
                .map(EmployeeResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{employeeId}")
    public EmployeeResponse getEmployee(@PathVariable int employeeId) {
        return EmployeeResponse.fromEntity(employeeService.findById(employeeId));
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> addEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        Employee createdEmployee = employeeService.create(toEntity(employeeRequest));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{employeeId}")
                .buildAndExpand(createdEmployee.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(EmployeeResponse.fromEntity(createdEmployee));
    }

    @PutMapping("/{employeeId}")
    public EmployeeResponse updateEmployee(
            @PathVariable int employeeId,
            @Valid @RequestBody EmployeeRequest employeeRequest
    ) {
        Employee updatedEmployee = employeeService.update(employeeId, toEntity(employeeRequest));

        return EmployeeResponse.fromEntity(updatedEmployee);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int employeeId) {
        employeeService.deleteById(employeeId);

        return ResponseEntity.noContent().build();
    }

    private Employee toEntity(EmployeeRequest employeeRequest) {
        return new Employee(
                employeeRequest.firstName(),
                employeeRequest.lastName(),
                employeeRequest.email()
        );
    }
}
