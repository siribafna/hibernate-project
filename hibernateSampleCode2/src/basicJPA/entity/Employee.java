/* NOTICE: All materials provided by this project, and materials derived 
 * from the project, are the property of the University of Texas. 
 * Project materials, or those derived from the materials, cannot be placed 
 * into publicly accessible locations on the web. Project materials cannot 
 * be shared with other project teams. Making project materials publicly 
 * accessible, or sharing with other project teams will result in the 
 * failure of the team responsible and any team that uses the shared materials. 
 * Sharing project materials or using shared materials will also result 
 * in the reporting of all team members for academic dishonesty. 
 */ 
 
package basicJPA.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empID;
    
	private String empName;
	private double salary;

	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="dept_id", unique=false) // FK column to be generated.
	private Department department;

	public Long getEmpID()
	{
		return empID;
	}

	public void setEmpID(Long empID)
	{
		this.empID = empID;
	}

	public String getEmpName()
	{
		return empName;
	}

	public void setEmpName(String empName)
	{
		this.empName = empName;
	}

	public double getSalary()
	{
		return salary;
	}

	public void setSalary(double salary)
	{
		this.salary = salary;
	}

	public Department getDepartment()
	{
		return department;
	}

	public void setDepartment(Department department)
	{
		this.department = department;
	}

    public boolean equals(Object obj)
    {
        if(obj == null) {
            return false;
        }
        
        if (!(obj instanceof Employee)) {
            return false;
        }
        
        Employee emp = (Employee)obj;
        
        if(emp.empID == null) {
            return false;
        }
        
        return (emp.empID == empID);
    }
    
}
