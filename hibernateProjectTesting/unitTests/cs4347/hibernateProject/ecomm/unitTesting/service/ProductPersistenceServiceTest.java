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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cs4347.hibernateProject.ecomm.entity.Product;
import cs4347.hibernateProject.ecomm.services.impl.ProductPersistenceServiceImpl;

public class ProductPersistenceServiceTest
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
		ProductPersistenceServiceImpl ppService = new ProductPersistenceServiceImpl(em);

		Product product = buildProduct();
		assertNull(product.getId());
		ppService.create(product);
		assertNotNull(product.getId());

		em.close();
	}

	@Test
	public void testRetrieve() throws Exception
	{
		EntityManager em = emf.createEntityManager();
		ProductPersistenceServiceImpl ppService = new ProductPersistenceServiceImpl(em);

		Product product = buildProduct();
		ppService.create(product);
		Long newProdID = product.getId();
		
		Product prod2 = ppService.retrieve(newProdID);
		assertNotNull(prod2);
		assertEquals(product.getProdCategory(), prod2.getProdCategory());
		assertEquals(product.getProdDescription(), prod2.getProdDescription());
		assertEquals(product.getProdName(), prod2.getProdName());
		assertEquals(product.getProdUPC(), prod2.getProdUPC());

		em.close();
	}

	@Test
	public void testUpdate() throws Exception
	{
		EntityManager em = emf.createEntityManager();
		ProductPersistenceServiceImpl ppService = new ProductPersistenceServiceImpl(em);

		Product product = buildProduct();
		ppService.create(product);
		Long id = product.getId();
		
		String newUPC = "3333333333";
		product.setProdUPC(newUPC);
		ppService.update(product);

		Product prod3 = ppService.retrieve(id);
		assertEquals(product.getId(), prod3.getId());
		assertEquals(product.getProdName(), prod3.getProdName());
		assertEquals(newUPC, prod3.getProdUPC());
		em.close();
	}

	@Test
	public void testDelete() throws Exception
	{
		EntityManager em = emf.createEntityManager();
		ProductPersistenceServiceImpl ppService = new ProductPersistenceServiceImpl(em);

		Product product = buildProduct();
		ppService.create(product);

		Long id = product.getId();
		ppService.delete(id);
		
		Product prod3 = ppService.retrieve(id);
		assertNull(prod3);
		em.close();
	}

	@Test
	public void testRetrieveByUPC() throws Exception
	{
		EntityManager em = emf.createEntityManager();
		ProductPersistenceServiceImpl ppService = new ProductPersistenceServiceImpl(em);

		Product product = ppService.retrieveByUPC("576236786900");
		assertNotNull(product);
		em.close();
	}

	@Test
	public void testRetrieveByCategory() throws Exception
	{
		EntityManager em = emf.createEntityManager();
		ProductPersistenceServiceImpl ppService = new ProductPersistenceServiceImpl(em);

		List<Product> products = ppService.retrieveByCategory(2);
		assertTrue(products.size() > 1);
		em.close();
	}

	private Product buildProduct()
    {
		Product product = new Product();
		product.setProdCategory(1);
		product.setProdDescription("Product Description");
		product.setProdName("Product Name");
		product.setProdUPC("1112223333");
	    return product;
    }
}
