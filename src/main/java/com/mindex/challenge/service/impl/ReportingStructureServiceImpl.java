package com.mindex.challenge.service.impl;


import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exception_hendler.GlobalNotFoundException;
import com.mindex.challenge.service.ReportingStructureService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    // This method looks up an employee by their employeeId
    public Employee findEmployeeById(String employeeId, List<Employee> employees) {
        employees.forEach(employee -> System.out.println("Employee ID in list: " + employee.getEmployeeId()));

        // Search for the employee by their employeeId
        Employee employee = employees.stream()
                .filter(emp -> employeeId.equals(emp.getEmployeeId())) // First, check if the employeeId matches
                .findFirst()
                .orElse(null); // If not found, return null

        if (employee != null) {
            return employee;
        }

        for (Employee emp : employees) {
            if (emp.getDirectReports() != null) {
                // Check each direct report
                for (Employee directReport : emp.getDirectReports()) {
                    if (employeeId.equals(directReport.getEmployeeId())) {
                        return directReport;
                    }
                    Employee nestedEmployee = findEmployeeById(employeeId, emp.getDirectReports());
                    if (nestedEmployee != null) {
                        return nestedEmployee;
                    }
                }
            }
        }

        throw new GlobalNotFoundException("Invalid employee id.");
    }

    // Method to calculate the total number of reports (direct and indirect reports)
    @Override
    public int calculateNumberOfReports(Employee employee, List<Employee> employees) {
        if (employee == null || employee.getDirectReports() == null) {
            return 0;
        }

        // The direct reports count
        int count = employee.getDirectReports().size();

        // Recursively count the number of reports for each direct report
        for (Employee directReport : employee.getDirectReports()) {
            // Look up the direct report by ID and recursively calculate the number of reports
            Employee directReportEmployee = findEmployeeById(directReport.getEmployeeId(), employees);
            count += calculateNumberOfReports(directReportEmployee, employees); // Recursively count reports
        }

        return count;
    }

    @Override
    public ReportingStructure getReportingStructure(String employeeId, List<Employee> employees) {
        Employee employee = findEmployeeById(employeeId, employees);

        // Calculate the number of reports for the employee
        int numberOfReports = calculateNumberOfReports(employee, employees);

        return new ReportingStructure(employee, numberOfReports);
    }
}


