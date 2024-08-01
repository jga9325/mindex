package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String compensationUrl;
    private String compensationIdUrl;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateRead() {
        // Create new employee
        Employee employee = new Employee("Bob", "Smith", "Developer", "Engineering");
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, employee, Employee.class).getBody();

        // Check that employee doesn't have a compensation
        assertNull(createdEmployee.getCompensation());

        // Create compensation for employee
        Compensation compensation = new Compensation(createdEmployee.getEmployeeId(), "100000", new Date());
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, compensation, Compensation.class).getBody();

        // Retrieve employee and check compensation
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertCompensationEquivalence(createdCompensation, readEmployee.getCompensation());

        // Retrieve compensation using employee data and compare to created compensation
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, readEmployee.getCompensation().getCompensationId()).getBody();
        assertCompensationEquivalence(createdCompensation, readCompensation);
    }

    @Test
    public void testDuplicateCompensations() {
        // Create new employee
        Employee employee = new Employee("Bob", "Smith", "Developer", "Engineering");
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, employee, Employee.class).getBody();

        // Create compensation for employee
        Compensation compensation = new Compensation(createdEmployee.getEmployeeId(), "100000", new Date());
        compensationService.create(compensation);

        // check that duplicate compensations cannot be created for an employee
        Exception exception = assertThrows(RuntimeException.class, () -> compensationService.create(compensation));
        assertEquals("Compensation already exists for employee with id: " + compensation.getEmployeeId(), exception.getMessage());
    }

    @Test
    public void testReadInvalidCompensationId() {
        // Check that an invalid compensationId cannot be used
        String id = "5";
        Exception exception = assertThrows(RuntimeException.class, () -> compensationService.read(id));
        assertEquals("Invalid compensationId: " + id, exception.getMessage());
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(0, expected.getEffectiveDate().compareTo(actual.getEffectiveDate()));
    }
}