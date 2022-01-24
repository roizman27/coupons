package app.DAO;

import java.util.ArrayList;

import app.core.beans.Customer;
import app.exceptions.CouponSystemException;

public interface CustomerDAO {
	/*
	 * interface for customer DAO
	 */
	public Boolean isCustomerExists(String email, String password) throws CouponSystemException;
	//for login 
	
	public void addCustomer(Customer customer) throws CouponSystemException;
	//creating new customer in db 
	
	public void updateCustomer(Customer customer) throws CouponSystemException;
	//updating customer details

	public void deleteCustomer(int customerId) throws CouponSystemException;
	//remove customer from db
	
	public ArrayList<Customer> getAllCustomers() throws CouponSystemException;
	//returns all customers in db
	
	public Customer getCustomer(int customerId) throws CouponSystemException;
	//returns a customers with id

}
