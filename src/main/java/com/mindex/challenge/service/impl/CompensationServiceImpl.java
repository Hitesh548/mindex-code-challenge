package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exception_handler.GlobalNotFoundException;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation for employeeId [{}]", compensation.getEmployeeId());

        //Fetching the employee by id to ensurebit exists
        Optional<Employee> employeeOpt = employeeRepository.findByEmployeeId(compensation.getEmployeeId());

        //If employee is not found, throw exception
        if (employeeOpt.isEmpty()) {
            throw new GlobalNotFoundException("Invalid employee id.");
        }

        return compensationRepository.save(compensation);
    }

    public List<Compensation> read(String employeeId) {
        //Fetching the employee by id to ensurebit exists
        Optional<Employee> employeeOpt = employeeRepository.findByEmployeeId(employeeId);

        //If employee is not found, throw exception
        if (employeeOpt.isEmpty()) {
            throw new GlobalNotFoundException("Invalid employee id.");
        }

        //Retrive all compensations records for the given employee
        List<Compensation> compensations = compensationRepository.findByEmployeeId(employeeId);
        if (compensations.isEmpty()) {
            throw new GlobalNotFoundException("No data found.");
        }
        return compensations;
    }
}
