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
 
package basicJPA;

import java.util.List;

import basicJPA.entity.Department;
import basicJPA.entity.Employee;

public interface DepartmentService
{
	void create(Department department);
	Department find(Long id);
	Department findByName(String name) throws DAOException;
	void update(Department departement);
	void addEmpToDept(String deptName, Employee emp) throws DAOException;
	void removeEmpFromDept(String deptName, Employee emp) throws DAOException;
	
	List<Employee> findEmployeeInSalaryRange(String deptName, double lowSalary, double highSalary);
}
