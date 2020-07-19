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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import basicJPA.entity.Employee;

public class EmployeeServiceImpl implements EmployeeService
{
	@PersistenceContext 
	private EntityManager em; 

	public EmployeeServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public void create(Employee employee)
	{
		try {
			em.getTransaction().begin();
			em.persist(employee);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Employee find(Long id)
	{
		try {
			em.getTransaction().begin();
			Employee employee = em.find(Employee.class, id);
			em.getTransaction().commit();
			return employee;
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void update(Employee employee)
	{
		try {
			em.getTransaction().begin();
			Employee e2 = em.find(Employee.class, employee.getEmpID());
			e2.setEmpName(employee.getEmpName());
			e2.setSalary(employee.getSalary());
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public List<Employee> retrieveEmpByDeptAndSalary(String deptName, double salary)
	{
		em.getTransaction().begin();
		List<Employee> emps = (List<Employee>)em.createQuery("from Employee as e where e.salary >= :salary and e.department.deptName = :name")
				.setParameter("salary", salary)
				.setParameter("name", deptName)
				.getResultList();
		em.getTransaction().commit();
		return emps;
	}
}
