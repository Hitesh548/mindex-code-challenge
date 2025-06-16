package com.mindex.challenge.service;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;

import java.util.List;

public interface ReportingStructureService {

    Employee findEmployeeById(String employee, List<Employee> employees);
    // Method to calculate the total number of reports (direct and indirect reports)
    int calculateNumberOfReports(Employee employee, List<Employee> employees);

    // Method to get the ReportingStructure for a given employeeId
    ReportingStructure getReportingStructure(String employeeId, List<Employee> employees);
}