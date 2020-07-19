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

package cs4347.hibernateProject.ecomm.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstName;
	private String lastName;
	private Character gender;
	private Date dob;
	private String email;

	// You will need @OrderColumn to avoid MultipleBagFetchException
	@OrderColumn
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL)
	private List<Address> addressList;

	@OrderColumn
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL)
	private List<CreditCard> creditCardList;

	public Customer() {
		addressList = new ArrayList<Address>();
		creditCardList = new ArrayList<CreditCard>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Address> getAddressList() {
		return addressList;
	}

	public List<CreditCard> getCreditCardList() {
		return creditCardList;
	}

	public void addCreditCard(CreditCard ccard) {
		creditCardList.add(ccard);
		ccard.setCustomer(this);
	}

	public void removeCreditCard(CreditCard ccard) {
		creditCardList.remove(ccard);
		ccard.setCustomer(null);
	}

	public void addAddress(Address addr) {
		addressList.add(addr);
		addr.setCustomer(this);
	}

	public void removeAddress(Address addr) {
		addressList.remove(addr);
		addr.setCustomer(null);
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof Customer)) {
			return false;
		}

		Customer cust = (Customer) obj;

		if (cust.id == null) {
			return false;
		}

		return (cust.id == id);
	}

}
