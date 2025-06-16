package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String employeeUrl;
    private String reportingStructureUrl;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        reportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{id}";
    }


    @Test
    public void testReportingStructureCalculation() {
        // Create a leaf employee (no direct reports)
        Employee leaf = new Employee();
        leaf.setFirstName("Leaf");
        leaf.setLastName("Worker");
        leaf.setDepartment("Engineering");
        leaf.setPosition("Developer");

        Employee createdLeaf = restTemplate.postForEntity(employeeUrl, leaf, Employee.class).getBody();
        assertNotNull(createdLeaf);

        // Create a mid-level manager with one direct report (the leaf)
        Employee manager = new Employee();
        manager.setFirstName("Manager");
        manager.setLastName("Middle");
        manager.setDepartment("Engineering");
        manager.setPosition("Team Lead");

        Employee leafRef = new Employee(); // Reference by ID only
        leafRef.setEmployeeId(createdLeaf.getEmployeeId());
        manager.setDirectReports(Collections.singletonList(leafRef));

        Employee createdManager = restTemplate.postForEntity(employeeUrl, manager, Employee.class).getBody();
        assertNotNull(createdManager);

        // Create a top-level manager with one direct report (the manager)
        Employee director = new Employee();
        director.setFirstName("Director");
        director.setLastName("Top");
        director.setDepartment("Engineering");
        director.setPosition("Director");

        Employee managerRef = new Employee(); // Reference by ID only
        managerRef.setEmployeeId(createdManager.getEmployeeId());
        director.setDirectReports(Collections.singletonList(managerRef));

        Employee createdDirector = restTemplate.postForEntity(employeeUrl, director, Employee.class).getBody();
        assertNotNull(createdDirector);

        // Fetch reporting structure for the top-level manager
        ReportingStructure response = restTemplate.getForEntity(
                reportingStructureUrl,
                ReportingStructure.class,
                createdDirector.getEmployeeId()
        ).getBody();

        assertNotNull(response);
        assertEquals(createdDirector.getEmployeeId(), response.getEmployee().getEmployeeId());
        assertEquals(2, response.getNumberOfReports()); // 1 direct + 1 indirect
    }
}
