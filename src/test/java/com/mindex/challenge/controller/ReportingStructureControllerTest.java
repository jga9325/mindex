package com.mindex.challenge.controller;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureControllerTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportingStructureUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{employeeId}";
    }

    @Test
    public void testGetReportingStructure() {
        // Create new employees
        Employee employeeBob = createEmployee("Bob", "Smith", "Engineering", "Manager");
        Employee employeeJack = createEmployee("Jack", "Sparrow", "Engineering", "Senior Developer");
        Employee employeeDana = createEmployee("Dana", "White", "Engineering", "UX Designer");
        Employee employeeSarah = createEmployee("Sarah", "Lee", "Sales", "Salesperson");

        // Set direct reports
        List<Employee> jackReports = new ArrayList<>();
        jackReports.add(employeeDana);
        jackReports.add(employeeSarah);
        employeeJack.setDirectReports(jackReports);

        List<Employee> bobReports = new ArrayList<>();
        bobReports.add(employeeJack);
        employeeBob.setDirectReports(bobReports);

        // Update employees
        Employee updatedEmployeeJack = updateEmployee(employeeJack);
        Employee updatedEmployeeBob = updateEmployee(employeeBob);

        // Check reporting structure for employee with no reports
        assertCorrectReportingStructure(employeeDana, 0);
        // Check reporting structure for employee with direct reports
        assertCorrectReportingStructure(updatedEmployeeJack, 2);
        // Check reporting structure for employee with multiple levels of direct reports
        assertCorrectReportingStructure(updatedEmployeeBob, 3);
    }

    private Employee createEmployee(String firstName, String lastName, String department, String position) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setDepartment(department);
        employee.setPosition(position);
        return restTemplate.postForEntity(employeeUrl, employee, Employee.class).getBody();
    }

    private Employee updateEmployee(Employee employee) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return restTemplate.exchange(employeeIdUrl,
                    HttpMethod.PUT,
                    new HttpEntity<Employee>(employee, headers),
                    Employee.class,
                    employee.getEmployeeId()).getBody();
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    private void assertCorrectReportingStructure(Employee employee, int expectedNumberOfReports) {
        ReportingStructure structure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, employee.getEmployeeId()).getBody();
        assertEmployeeEquivalence(employee, structure.getEmployee());
        assertEquals(expectedNumberOfReports, structure.getNumberOfReports());
    }
}
