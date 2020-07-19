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

import basicJPA.entity.Department;
import basicJPA.entity.Employee;

public class DepartmentServiceImpl implements DepartmentService
{
	@PersistenceContext 
	private EntityManager em; 

	public DepartmentServiceImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public void create(Department department)
	{
		try {
			em.getTransaction().begin();
			em.persist(department);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Department find(Long id)
	{
		try {
			em.getTransaction().begin();
			Department dept = em.find(Department.class, id);
			em.getTransaction().commit();
			return dept;
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Department findByName(String deptName) throws DAOException
	{
		em.getTransaction().begin();
		Department dept = (Department)em.createQuery("from Department as d where d.deptName = :name")
				.setParameter("name", deptName)
				.getSingleResult();
		em.getTransaction().commit();
		
		if(dept == null) {
			throw new DAOException("Department Name Not Found " + deptName);
		}
		
		return dept;
	}

	@Override
	public void update(Department departement)
	{
		try {
			em.getTransaction().begin();
			Department d2 = em.find(Department.class, departement.getDeptID());
			d2.setDeptName(departement.getDeptName());
			d2.setDescription(departement.getDescription());
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void addEmpToDept(String deptName, Employee emp) throws DAOException
	{
		em.getTransaction().begin();
		Department dept = (Department)em.createQuery("from Department as d where d.deptName = :name")
				.setParameter("name", deptName)
				.getSingleResult();

		if(dept == null) {
			em.getTransaction().rollback();
			throw new DAOException("Department Name Not Found " + deptName);
		}
		
		dept.addEmployee(emp);
		em.getTransaction().commit();
	}

	@Override
	public void removeEmpFromDept(String deptName, Employee emp) throws DAOException
	{
		em.getTransaction().begin();
		Department dept = (Department)em.createQuery("from Department as d where d.deptName = :name")
				.setParameter("name", deptName)
				.getSingleResult();
		
		if(dept == null) {
			em.getTransaction().rollback();
			throw new DAOException("Department Name Not Found " + deptName);
		}
		
		dept.removeEmployee(emp);
		em.getTransaction().commit();
	}

    // Note the use of a JPA join needed to query though Department into a property of members of Employee
    @Override
    public List<Employee> findEmployeeInSalaryRange(String deptName, double lowSalary, double highSalary)
    {
        List<Employee> employees = em.createQuery("select e from Department d inner join d.employees e where e.salary between :low and :high and d.deptName = :name")
                .setParameter("low", lowSalary)
                .setParameter("high", highSalary)
                .setParameter("name", deptName)
                .getResultList();
        return employees;
    }

}
