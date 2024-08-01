package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Service that builds a reporting structure for an employee
 */
@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    /**
     * Builds a ReportingStructure object for a specific employee
     * @param id
     * @return structure
     */
    @Override
    public ReportingStructure build(String id) {
        LOG.debug("Building reporting structure for employee with id [{}]", id);

        ReportingStructure structure = new ReportingStructure();
        Employee employee = employeeService.read(id);
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