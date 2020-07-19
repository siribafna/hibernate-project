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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "department")
public class Department
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deptID;
    
	private String deptName;
	private String description;
	
    @OneToMany(fetch=FetchType.LAZY, mappedBy="department")
	private List<Employee> employees;

    public Department()
    {
        employees = new ArrayList<Employee>();
    }
    
	public Long getDeptID()
	{
		return deptID;
	}

	public void setDeptID(Long deptID)
	{
		this.deptID = deptID;
	}

	public String getDeptName()
	{
		return deptName;
	}

	public void setDeptName(String deptName)
	{
		this.deptName = deptName;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public List<Employee> getEmployees()
	{
		return employees;
	}

	public void addEmployee(Employee emp) {
		employees.add(emp);
		emp.setDepartment(this);
	}

	public void removeEmployee(Employee emp) {
		employees.remove(emp);
        emp.setDepartment(this);
	}

}
