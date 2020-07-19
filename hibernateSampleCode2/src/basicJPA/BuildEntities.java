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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import basicJPA.entity.Department;
import basicJPA.entity.Employee;

public class BuildEntities
{

	private void createOSI(EntityManager em)
	{
		em.getTransaction().begin();
		
		Department newDept = new Department();
		newDept.setDeptName("OSI");
		newDept.setDescription("Office of Secret Intelligence");
		
		Employee newEmp = new Employee();
		newEmp.setEmpName("Hunter Gathers");
		newEmp.setSalary(100000);
		newEmp.setDepartment(newDept);
        newDept.addEmployee(newEmp);
        em.persist(newEmp);
		
        newEmp = new Employee();
        newEmp.setEmpName("Brock Sampson");
        newEmp.setSalary(90000);
        newEmp.setDepartment(newDept);
        newDept.addEmployee(newEmp);
        em.persist(newEmp);
        
        newEmp = new Employee();
        newEmp.setEmpName("Dean Venture");
        newEmp.setSalary(40000);
        newEmp.setDepartment(newDept);
        newDept.addEmployee(newEmp);
        em.persist(newEmp);
        
        newEmp = new Employee();
        newEmp.setEmpName("Hank Venture");
        newEmp.setSalary(40000);
        newEmp.setDepartment(newDept);
        newDept.addEmployee(newEmp);
        em.persist(newEmp);
        
        newEmp = new Employee();
        newEmp.setEmpName("Jonas Venture");
        newEmp.setSalary(0);
        newEmp.setDepartment(newDept);
        newDept.addEmployee(newEmp);
        em.persist(newEmp);
        
        em.persist(newDept);

        em.getTransaction().commit();
	}

	private void createGuild(EntityManager em)
	{
		em.getTransaction().begin();
		
		Department newDept = new Department();
		newDept.setDeptName("Guild");
		newDept.setDescription("The Guild of Calamitous Intent");
		em.persist(newDept);

		Employee newEmp = new Employee();
		newEmp.setEmpName("The Monarch");
		newEmp.setSalary(100000);
		em.persist(newEmp);
        newDept.addEmployee(newEmp);

        newEmp = new Employee();
        newEmp.setEmpName("Dr. Girlfriend");
        newEmp.setSalary(90000);
        em.persist(newEmp);
        newDept.addEmployee(newEmp);
        
        newEmp = new Employee();
        newEmp.setEmpName("Henchman 21");
        newEmp.setSalary(40000);
        em.persist(newEmp);
        newDept.addEmployee(newEmp);
        
        newEmp = new Employee();
        newEmp.setEmpName("Dr. Henry Killinger");
        newEmp.setSalary(120000);
        em.persist(newEmp);
        newDept.addEmployee(newEmp);
        
        newEmp = new Employee();
        newEmp.setEmpName("Henchman 24");
        newEmp.setSalary(40000);
        em.persist(newEmp);
        newDept.addEmployee(newEmp);
        
        em.persist(newDept);
        
		em.getTransaction().commit();
	}
	
	public static void main(String args[])
	{
	    EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-employee");
		EntityManager em = emf.createEntityManager();
		
		BuildEntities app = new BuildEntities();
        System.out.println("Building Guild");
		app.createGuild(em);
        System.out.println("Building OSI");
		app.createOSI(em);
        System.out.println("Done Building");
		em.close();
		emf.close();
	}
}
