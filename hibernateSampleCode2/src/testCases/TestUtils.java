package testCases;

import basicJPA.entity.Department;
import basicJPA.entity.Employee;

public class TestUtils
{
    static public Department buildDepartment()
    {
        Department dept = new Department();
        Long ts = System.currentTimeMillis();
        dept.setDeptName("DeptName " + ts);
        dept.setDescription("DeptDesc " + ts);
        return dept;
    }

    static public Employee buildEmployee()
    {
        Employee emp = new Employee();
        Long ts = System.currentTimeMillis();
        emp.setEmpName("Employee " + ts);
        emp.setSalary(ts);
        return emp;
    }
}
