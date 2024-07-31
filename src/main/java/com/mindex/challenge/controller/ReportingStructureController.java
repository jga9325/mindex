package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.Queue;

@RestController
public class ReportingStructureController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureController.class);

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/reportingStructure/{employeeId}")
    public ReportingStructure get(@PathVariable String employeeId) {
        LOG.debug("Received reporting structure get request for employeeId [{}]", employeeId);

        ReportingStructure structure = new ReportingStructure();
        Employee employee = employeeService.read(employeeId);
        structure.setEmployee(employee);

        int numberOfReports = 0;
        Queue<Employee> employees = new LinkedList<>();
        if (employee.getDirectReports() != null) {
            for (Employee emp : employee.getDirectReports()) {
                employees.offer(employeeService.read(emp.getEmployeeId()));
            }
        }
        while (!employees.isEmpty()) {
            Employee report = employees.poll();
            if (report.getDirectReports() != null) {
                for (Employee emp : report.getDirectReports()) {
                    employees.offer(employeeService.read(emp.getEmployeeId()));
                }
            }
            numberOfReports++;
        }
        structure.setNumberOfReports(numberOfReports);
        return structure;
    }
}
