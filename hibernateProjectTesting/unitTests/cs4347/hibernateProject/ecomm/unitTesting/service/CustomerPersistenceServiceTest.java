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
 
package cs4347.hibernateProject.ecomm.unitTesting.service;

import static org.junit.Assert.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cs4347.hibernateProject.ecomm.entity.Address;
import cs4347.hibernateProject.ecomm.entity.CreditCard;
import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.services.impl.CustomerPersistenceServiceImpl;

public class CustomerPersistenceServiceTest
{
	protected static EntityManagerFactory emf;
	
	@BeforeClass
    public static void createEntityManagerFactory() {
        emf = Persistence.createEntityManagerFactory("jpa-simple_company");
    }

    @AfterClass
    public static void closeEntityManagerFactory() {
		if(emf != null) {
			emf.close();
			emf = null;
		}
    }
    
	@Test
	public void testCreate() throws Exception
	{
		EntityManager em = emf.createEntityManager();
		CustomerPersistenceServiceImpl cpService = new CustomerPersistenceServiceImpl(em);
		
		Customer cust = buildCustomer();
		assertNull(cust.getId());
		cpService.create(cust);
		assertNotNull(cust.getId());

		Customer cust3 = em.find(Customer.class, cust.getId());
		assertNotNull(cust3);
		assertTrue(cust3 == cust);
		
		em.close();
	}

	@Test
	public void testRetrieve() throws Exception
	{
		EntityManager em = emf.createEntityManager();
		CustomerPersistenceServiceImpl cpService = new CustomerPersistenceServiceImpl(em);

		Customer cust = buildCustomer();
		cpService.create(cust);
		Long id = cust.getId();

		Customer cust3 = cpService.retrieve(id);
		assertNotNull(cust3);
		assertEquals(cust.getId(), cust3.getId());
		assertEquals(cust.getFirstName(), cust3.getFirstName());
		assertEquals(cust.getLastName(), cust3.getLastName());
		assertTrue(cust.getDob().equals(cust3.getDob()));
		assertEquals(cust.getGender(), cust3.getGender());
		assertEquals(cust.getEmail(), cust3.getEmail());
		
        // This works reliably because only one Addr is added by buildCustomer()
		Address addr1 = cust.getAddressList().toArray(new Address[0])[0];
		Address addr2 = cust3.getAddressList().toArray(new Address[0])[0];
		assertNotNull(addr2);
		assertEquals(addr1.getAddress1(), addr2.getAddress1());
		assertEquals(addr1.getAddress2(), addr2.getAddress2());
		assertEquals(addr1.getCity(), addr2.getCity());
		assertEquals(addr1.getState(), addr2.getState());
		assertEquals(addr1.getZipcode(), addr2.getZipcode());

		// This works reliably because only one CC is added by buildCustomer()
		CreditCard ccard = cust.getCreditCardList().toArray(new CreditCard[0])[0];
		CreditCard ccard2 = cust3.getCreditCardList().toArray(new CreditCard[0])[0];
		assertNotNull(ccard2);
		assertEquals(ccard.getName(), ccard2.getName());
		assertEquals(ccard.getCcNumber(), ccard2.getCcNumber());
		assertEquals(ccard.getExpDate(), ccard2.getExpDate());
		assertEquals(ccard.getSecurityCode(), ccard2.getSecurityCode());
		
		em.close();
}

	@Test
	public void testUpdate() throws Exception
	{
		EntityManager em = emf.createEntityManager();
		CustomerPersistenceServiceImpl cpService = new CustomerPersistenceServiceImpl(em);

		Customer cust = buildCustomer();
		cpService.create(cust);
		Long id = cust.getId();
		
		String newEmail = "fred@gmail" + System.currentTimeMillis();
		cust.setEmail(newEmail);
		for(Address addr : cust.getAddressList()) {
	        addr.setZipcode("00001");		    
		}
		for(CreditCard ccard : cust.getCreditCardList()) {
	        ccard.setExpDate("01/1234");
		}
		
		cpService.update(cust);
		em.close();
		
		em = emf.createEntityManager();
		cpService = new CustomerPersistenceServiceImpl(em);

		Customer cust3 = cpService.retrieve(id);
		assertEquals(newEmail, cust3.getEmail());
		for(Address addr2 : cust3.getAddressList()) {
	        assertEquals("00001", addr2.getZipcode());		    
		}
		for(CreditCard ccard2 : cust3.getCreditCardList()) {
	        assertEquals("01/1234", ccard2.getExpDate());
		}
		
		em.close();
	}

	@Test
	public void testDelete() throws Exception
	{
		EntityManager em = emf.createEntityManager();
		CustomerPersistenceServiceImpl cpService = new CustomerPersistenceServiceImpl(em);

		Customer cust = buildCustomer();
		cpService.create(cust);
		Long custID = cust.getId();
		
		cpService.delete(custID);
		
		assertNull(em.find(Customer.class, custID));

        // This works reliably because only one Addr is added by buildCustomer()
		// This code assumes that Address.customerID FK is set cascade delete
		assertNull(em.find(Address.class, cust.getAddressList().toArray(new Address[0])[0].getId())); // This hurts my brain.

        // This works reliably because only one CC is added by buildCustomer()
		// This code assumes that credit_card.customerID FK is set cascade delete
		assertNull(em.find(CreditCard.class, cust.getCreditCardList().toArray(new CreditCard[0])[0].getId())); // This hurts my brain.

		em.close();
	}

	@Test
	public void testRetrieveByZipCode() throws Exception
	{
		EntityManager em = emf.createEntityManager();
		CustomerPersistenceServiceImpl cpService = new CustomerPersistenceServiceImpl(em);

		List<Customer> custs = cpService.retrieveByZipCode("76953-7323"); // Need a customer with this address
		assertTrue(custs.size() > 0);

		for(Customer customer: custs) {
			assertTrue(customer.getAddressList() != null);
			assertTrue(customer.getCreditCardList() != null);
		}
		
		em.close();
	}

	@Test
	public void testRetrieveByDOB() throws Exception
	{
		EntityManager em = emf.createEntityManager();
		CustomerPersistenceServiceImpl cpService = new CustomerPersistenceServiceImpl(em);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date start = new Date(sdf.parse("1970-01-01").getTime());
		Date end = new Date(sdf.parse("1990-01-01").getTime());
		
		List<Customer> custs = cpService.retrieveByDOB(start, end); // Need a customer with this address
		assertTrue(custs.size() > 0);
		
		for(Customer customer: custs) {
			assertTrue(customer.getAddressList() != null);
			assertTrue(customer.getCreditCardList() != null);
		}
		
		em.close();
	}

	private Customer buildCustomer()
	{
		Customer cust = new Customer();
		cust.setFirstName("fred");
		cust.setLastName("flintstone");
		cust.setDob(new java.sql.Date(System.currentTimeMillis()));
		cust.setEmail("fred@gmail" + System.currentTimeMillis());
		cust.setGender('M');

		Address addr = new Address();
		addr.setAddress1("123 First St.");
		addr.setAddress2("Appmt 1b");
		addr.setCity("Dallas");
		addr.setState("TX");
		addr.setZipcode("70765");
		cust.addAddress(addr);

		CreditCard ccard = new CreditCard();
		ccard.setName("Fred Flintstone");
		ccard.setCcNumber("1111 1111 1111 1111");
		ccard.setExpDate("12/2018");
		ccard.setSecurityCode("123");
        cust.addCreditCard(ccard);

		return cust;
	}
}
