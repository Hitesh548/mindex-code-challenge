package com.mindex.challenge.controller;


import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reportingStructure")
public class ReportingStructureController {

    @Autowired
    private ReportingStructureService reportingStructureService;

    @Autowired
    private EmployeeRepository employeeRepository;


    // Endpoint to get ReportingStructure for a given employeeId
    @GetMapping("/{employeeId}")
    public ReportingStructure getReportingStructure(@PathVariable String employeeId) {
        List<Employee> employeeList = employeeRepository.findAll();
        return reportingStructureService.getReportingStructure(employeeId, employeeList);
    }
}