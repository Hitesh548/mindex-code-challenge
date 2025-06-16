package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String employeeUrl;
    private String compensationCreateUrl;
    private String compensationReadUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationCreateUrl = "http://localhost:" + port + "/compensations/create";
        compensationReadUrl = "http://localhost:" + port + "/compensations/{id}";
    }

    @Test
    public void testCreateAndReadCompensation() {
        // Step 1: Create a test employee
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("Alice");
        testEmployee.setLastName("Johnson");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Engineer");

        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());

        // Step 2: Create a compensation for the employee
        Compensation testCompensation = new Compensation();
        testCompensation.setEmployeeId(createdEmployee.getEmployeeId());
        testCompensation.setSalary(85000.0);
        testCompensation.setEffectiveDate(LocalDate.of(2025, 6, 15));

        Compensation createdCompensation = restTemplate.postForEntity(compensationCreateUrl, testCompensation, Compensation.class).getBody();

        assertNotNull(createdCompensation);
        assertEquals(testCompensation.getEmployeeId(), createdCompensation.getEmployeeId());
        assertEquals(testCompensation.getSalary(), createdCompensation.getSalary());
        assertEquals(testCompensation.getEffectiveDate(), createdCompensation.getEffectiveDate());

        // Step 3: Read the compensation
        Compensation[] compensationArray = restTemplate.getForObject(compensationReadUrl, Compensation[].class, createdEmployee.getEmployeeId());

        assertNotNull(compensationArray);
        assertTrue(compensationArray.length >= 1); // Could be more if not using clean DB

        boolean matchFound = false;
        for (Compensation comp : compensationArray) {
            if (comp.getSalary().equals(testCompensation.getSalary()) &&
                    comp.getEffectiveDate().equals(testCompensation.getEffectiveDate())) {
                matchFound = true;
                break;
            }
        }

        assertTrue("Created compensation should exist in read result", matchFound);
    }
}
