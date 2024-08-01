package com.mindex.challenge.data;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Represents the compensation for an employee
 */
public class Compensation {
    @Id
    private String compensationId;
    private String employeeId;
    private String salary;
    private Date effectiveDate;

    public Compensation() {
    }

    public Compensation(String employeeId, String salary, Date effectiveDate) {
        this.employeeId = employeeId;
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }

    public String getCompensationId() {
        return compensationId;
    }

    public void setCompensationId(String compensationId) {
        this.compensationId = compensationId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
