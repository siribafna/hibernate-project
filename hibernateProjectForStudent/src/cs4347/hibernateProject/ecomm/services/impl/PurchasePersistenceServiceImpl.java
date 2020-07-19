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

package cs4347.hibernateProject.ecomm.services.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cs4347.hibernateProject.ecomm.entity.Purchase;
import cs4347.hibernateProject.ecomm.services.PurchasePersistenceService;
import cs4347.hibernateProject.ecomm.services.PurchaseSummary;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class PurchasePersistenceServiceImpl implements PurchasePersistenceService {
	@PersistenceContext
	private EntityManager em;

	public PurchasePersistenceServiceImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public void create(Purchase purchase) throws SQLException, DAOException {
		try {
			em.getTransaction().begin();
			em.persist(purchase);
			em.getTransaction().commit();
		} catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Purchase retrieve(Long id) throws SQLException, DAOException {
		try {
			em.getTransaction().begin();
			Purchase purchase = em.find(Purchase.class, id);
			em.getTransaction().commit();
			return purchase;
		} catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void update(Purchase purchase) throws SQLException, DAOException {
		try {
			em.getTransaction().begin();
			Purchase p2 = em.find(Purchase.class, purchase.getId());
			p2.setPurchaseDate(purchase.getPurchaseDate());
			p2.setPurchaseAmount(purchase.getPurchaseAmount());
			p2.setCustomer(purchase.getCustomer());
			p2.setProduct(purchase.getProduct());
			em.getTransaction().commit();
		} catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void delete(Long id) throws SQLException, DAOException {

		em.getTransaction().begin();
		Purchase purchase = (Purchase) em.createQuery("from Purchase as p where p.id = :id").setParameter("id", id)
				.getSingleResult();

		if (purchase == null) {
			em.getTransaction().rollback();
			throw new DAOException("Purchase ID Not Found " + id);
		}

		em.remove(purchase); // Does this work?
		em.getTransaction().commit();
	}

	@Override
	public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException {
		em.getTransaction().begin();
		List<Purchase> purchases = (List<Purchase>) em.createQuery("from Purchase as p  where p.customer.id = :custId")
				.setParameter("custId", customerID).getResultList();

		em.getTransaction().commit();
		return purchases;
	}

	@Override
	public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException {
		List<Purchase> purchases = new ArrayList<Purchase>();
		purchases = retrieveForCustomerID(customerID);
		PurchaseSummary result = new PurchaseSummary();
		double minimum = 999999999999.99;
		double maximum = 0;
		double sum = 0;
		for (Purchase p : purchases) {
			double temp = p.getPurchaseAmount();
			if (temp < minimum) {
				minimum = temp;
			}
			if (temp > maximum) {
				maximum = temp;
			}
			sum += temp;
		}
		double avg = (sum / purchases.size());

		result.minPurchase = (float) minimum;
		result.maxPurchase = (float) maximum;
		result.avgPurchase = (float) avg;

		return result;
	}

	@Override
	public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException {
		em.getTransaction().begin();
		List<Purchase> purchases = (List<Purchase>) em.createQuery("from Purchase as p  where p.product.id = :prodId")
				.setParameter("prodId", productID).getResultList();

		em.getTransaction().commit();
		return purchases;
	}
}
