package app.facades;

import java.util.ArrayList;

import app.DbDAO.CompanyDBDAO;
import app.DbDAO.CouponDBDAO;
import app.DbDAO.CustomerDBDAO;
import app.core.beans.Company;
import app.core.beans.Customer;
import app.exceptions.CouponSystemException;

public class AdminFacade extends ClientFacade {

	/*
	 * admin facade is for the system admin
	 * used to add, remove and update user clients (companies and customers)  
	 */
	private static final String ADMIN_EMAIL = "admin@admin.com";
	private static final String ADMIN_PASS = "admin";

	//constructor initiates class with needed DBDAO
	public AdminFacade() {
		try {
			this.companyDBDAO = new CompanyDBDAO();
			this.couponDBDAO = new CouponDBDAO();
			this.customerDBDAO = new CustomerDBDAO();
		} catch (CouponSystemException e) {
			System.err.println("Admin facade constructor failed: " + e.getMessage());
		}
	}

	@Override
	public boolean Login(String email, String password) {
		return (email == ADMIN_EMAIL & password == ADMIN_PASS);
	}

	public void addCompany(Company company) throws CouponSystemException {
		try {
			this.companyDBDAO.addCompany(company);
		} catch (CouponSystemException e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}

	public void updateCompany(Company company) throws CouponSystemException {
		try {
			// updates only matching id
			this.companyDBDAO.updateCompany(company);
		} catch (CouponSystemException e) {
			throw e;
		}
	}

	public void deleteCompany(int companyId) throws CouponSystemException {
		try {
			this.couponDBDAO.deleteCompanyCoupons(companyId);
			this.companyDBDAO.deleteCompany(companyId);
		} catch (CouponSystemException e) {
			throw e;
		}
	}

	public ArrayList<Company> getAllCompanies() throws CouponSystemException {
		try {
			return this.companyDBDAO.getAllCompanys();
		} catch (CouponSystemException e) {
			throw e;
		}
	}

	public Company getCompany(int companyId) throws CouponSystemException {
		try {
			return this.companyDBDAO.getCompany(companyId);
		} catch (CouponSystemException e) {
			throw e;
		}
	}

	public void addCustomer(Customer customer) throws CouponSystemException {
		try {
			if (this.customerDBDAO.isCustomerEmailExists(customer.getEmail())) {
				throw new CouponSystemException("addCustomer failed - customer Email already in DB");
			} else {
				this.customerDBDAO.addCustomer(customer);
			}
		} catch (CouponSystemException e) {
			throw e;
		}
	}

	public void updateCustomer(Customer customer) throws CouponSystemException {
		try {
			this.customerDBDAO.updateCustomer(customer);
		} catch (CouponSystemException e) {
			throw e;
		}
	}

	public void deleteCustomer(int customerId) throws CouponSystemException {
		try {
			// customerDBDAO.deleteCustomer deletes the coupons in DAO
			this.customerDBDAO.deleteCustomer(customerId);
		} catch (CouponSystemException e) {
			throw e;
		}
	}

	public ArrayList<Customer> getAllCustomers() throws CouponSystemException {
		try {
			return this.customerDBDAO.getAllCustomers();
		} catch (CouponSystemException e) {
			throw e;
		}
	}

	public Customer getCustomer(int customerId) throws CouponSystemException {
		try {
			return this.customerDBDAO.getCustomer(customerId);
		} catch (CouponSystemException e) {
			throw e;
		}
	}

	

}
