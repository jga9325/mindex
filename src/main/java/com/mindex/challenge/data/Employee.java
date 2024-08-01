package com.mindex.challenge.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

/**
 * Represents an employee, including their compensation and direct reports
 */
public class Employee {
    @Id
    private String employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private String department;
    private List<Employee> directReports;
    @ReadOnlyProperty
    @DocumentReference(lookup = "{ 'employeeId' : ?#{#self._id} }")
    private Compensation compensation;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String department, String position) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.position = position;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Employee> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<Employee> directReports) {
        this.directReports = directReports;
    }

    public Compensation getCompensation() {
        return compensation;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("First Name: ");
        s.append(firstName);
        s.append(", Last Name: ");
        s.append(lastName);
        s.append(", Department: ");
        s.append(department);
        s.append(", Position: ");
        s.append(position);
        s.append(", ID: ");
        s.append(employeeId);
        return  s.toString();
    }
}
