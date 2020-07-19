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
 
package testCases;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import basicJPA.EmployeeServiceImpl;
import basicJPA.entity.Employee;

public class EmployeeServiceTC
{
	protected static EntityManagerFactory emf;

	@BeforeClass
	public static void createEntityManagerFactory()
	{
		emf = Persistence.createEntityManagerFactory("jpa-employee");
	}

	@AfterClass
	public static void closeEntityManagerFactory()
	{
		if(emf != null) {
			emf.close();
			emf = null;
		}
	}

	@Test
	public void testCreate()
	{
		EntityManager em = emf.createEntityManager();
		EmployeeServiceImpl empService = new EmployeeServiceImpl(em);

		Employee emp = TestUtils.buildEmployee();
		assertNull(emp.getEmpID());
		empService.create(emp);
		assertNotNull(emp.getEmpID());

		Employee emp2 = em.find(Employee.class, emp.getEmpID());
		assertNotNull(emp2);
		assertTrue(emp2 == emp);

		em.close();
	}

	@Test
	public void testFindEmployee()
	{
		EntityManager em = emf.createEntityManager();
		EmployeeServiceImpl empService = new EmployeeServiceImpl(em);

		Employee emp = TestUtils.buildEmployee();
		empService.create(emp);
		Long id = emp.getEmpID();

		Employee emp2 = empService.find(id);
		assertNotNull(emp2);
		assertEquals(emp.getEmpID(), emp2.getEmpID());
		assertEquals(emp.getEmpName(), emp2.getEmpName());
		assertEquals(emp.getSalary(), emp2.getSalary(), .01);

		em.close();
	}

	@Test
	public void testUpdateEmployee()
	{
		EntityManager em = emf.createEntityManager();
		EmployeeServiceImpl empService = new EmployeeServiceImpl(em);

		Employee emp = TestUtils.buildEmployee();
		empService.create(emp);
		Long id = emp.getEmpID();

		Long ts = System.currentTimeMillis();
		emp.setSalary(ts);

		empService.update(emp);
		em.close();

		em = emf.createEntityManager();
		empService = new EmployeeServiceImpl(em);

		Employee emp2 = empService.find(id);
		assertEquals(ts, emp2.getSalary(), .01);

		em.close();
	}

	@Test
	public void testRetrieveEmpByDeptAndSalary()
	{
		EntityManager em = emf.createEntityManager();
		EmployeeServiceImpl empService = new EmployeeServiceImpl(em);
		
		List<Employee> emps = empService.retrieveEmpByDeptAndSalary("OSI", 100000);
		assertTrue(!emps.isEmpty());
	}

}
