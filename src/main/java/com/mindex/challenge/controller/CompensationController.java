package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compensations")
public class CompensationController {

    @Autowired
    private CompensationService compensationService;

    // Endpoint to create a Compensation for an Employee
    @PostMapping("/create")
    public Compensation createCompensation(@Valid @RequestBody Compensation compensation) {
        return compensationService.create(compensation);
    }

    // Endpoint to get Compensation details for a given employee ID
    @GetMapping("/{employeeId}")
    public List<Compensation> getCompensation(@PathVariable String employeeId) {
        return compensationService.read(employeeId);
    }
}
