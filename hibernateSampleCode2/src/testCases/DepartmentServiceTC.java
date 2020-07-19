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

import basicJPA.DepartmentServiceImpl;
import basicJPA.entity.Department;
import basicJPA.entity.Employee;

public class DepartmentServiceTC
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
		DepartmentServiceImpl deptService = new DepartmentServiceImpl(em);

		Department dept = TestUtils.buildDepartment();
		assertNull(dept.getDeptID());
		deptService.create(dept);
		assertNotNull(dept.getDeptID());

		Department dept2 = em.find(Department.class, dept.getDeptID());
		assertNotNull(dept2);
		assertTrue(dept == dept2);

		em.close();
	}

	@Test
	public void testFindDepartment()
	{
		EntityManager em = emf.createEntityManager();
		DepartmentServiceImpl deptService = new DepartmentServiceImpl(em);

		Department dept = TestUtils.buildDepartment();
		deptService.create(dept);
		assertNotNull(dept.getDeptID());

		Department dept2 = deptService.find(dept.getDeptID());
		assertNotNull(dept2);
		assertEquals(dept.getDeptID(), dept2.getDeptID());
		assertEquals(dept.getDeptName(), dept2.getDeptName());
		assertEquals(dept.getDescription(), dept2.getDescription());

		em.close();
	}

	@Test
	public void testUpdateDepartment()
	{
		EntityManager em = emf.createEntityManager();
		DepartmentServiceImpl deptService = new DepartmentServiceImpl(em);

		Department dept = TestUtils.buildDepartment();
		deptService.create(dept);
		assertNotNull(dept.getDeptID());

		Long ts = System.currentTimeMillis();
		dept.setDescription("UpdatedDescription " + ts);

		deptService.update(dept);
		em.close();

		em = emf.createEntityManager();
		deptService = new DepartmentServiceImpl(em);

		Department dept2 = deptService.find(dept.getDeptID());
		assertEquals(dept.getDescription(), dept2.getDescription());

		em.close();
	}

	@Test
	public void testAddEmpToDept() throws Exception
	{
		EntityManager em = emf.createEntityManager();
		DepartmentServiceImpl deptService = new DepartmentServiceImpl(em);

		Department dept = deptService.findByName("OSI");
		int beforeSize = dept.getEmployees().size();

		Employee emp = TestUtils.buildEmployee();
		deptService.addEmpToDept("OSI", emp);
		
		Department dept2 = deptService.findByName("OSI");
		assertTrue(beforeSize < dept2.getEmployees().size());
	}

	@Test
	public void testRemoveEmpFromDept() throws Exception
	{
		EntityManager em = emf.createEntityManager();
		DepartmentServiceImpl deptService = new DepartmentServiceImpl(em);

		Department dept = deptService.findByName("OSI");
		int beforeSize = dept.getEmployees().size();

		Employee emp = (Employee)dept.getEmployees().get(0);
		deptService.removeEmpFromDept("OSI", emp);
		
		Department dept2 = deptService.findByName("OSI");
		assertTrue(beforeSize > dept2.getEmployees().size());
	}

    @Test
    public void testDepartmentEmployeeSalaryRange() throws Exception
    {
        EntityManager em = emf.createEntityManager();
        DepartmentServiceImpl deptService = new DepartmentServiceImpl(em);
        
        List<Employee> emps = deptService.findEmployeeInSalaryRange("OSI", 20000, 80000);
        assertEquals(2,emps.size());
    }
}
