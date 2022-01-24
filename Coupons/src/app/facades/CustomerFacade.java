package app.facades;


import java.util.ArrayList;
import java.util.Date;

import app.DbDAO.CompanyDBDAO;
import app.DbDAO.CouponDBDAO;
import app.DbDAO.CustomerDBDAO;
import app.core.beans.Coupon;
import app.core.beans.Customer;
import app.exceptions.CouponSystemException;

public class CustomerFacade extends ClientFacade {

	/*
	 * customer facade is used for customer purchasing of coupons and keeping track of the coupons they have
	 * the methods use the id returned from the login 
	 */
	private static int customerID;
	private static Customer CUSTOMER;

	public CustomerFacade() {
		try {
			this.companyDBDAO = new CompanyDBDAO();
			this.couponDBDAO = new CouponDBDAO();
			this.customerDBDAO = new CustomerDBDAO();
		} catch (CouponSystemException e) {
			// TODO Auto-generated catch block
			System.err.println("Customer facade ctor failed: " + e.getMessage());
		}
	}
	

	public int getCustomerID() {
		return customerID;
	}
	public boolean Login(String email, String password) throws CouponSystemException {
		if(customerDBDAO.isCustomerExists(email, password)) {
			try {
				customerID = customerDBDAO.getCustomerId(email, password);
				CUSTOMER = this.customerDBDAO.getCustomer(customerID);
			}catch (CouponSystemException e) {
				throw new CouponSystemException("login failed " + e.getMessage());
			}	
		}
		return customerDBDAO.isCustomerExists(email, password);		
	}
	
	public void purchaseCoupon(Coupon coupon) throws CouponSystemException {
		try {
			Coupon dbCoupon = couponDBDAO.getCoupon(coupon.getId());
			if (dbCoupon.getAmount() == 0) {
				throw new CouponSystemException("coupon sold out. purchase failed");
			} else if (dbCoupon.getEndDate().before(new Date())) {
				throw new CouponSystemException("coupon already expired. purchase failed");
			}
			try {
				this.couponDBDAO.addCouponPurchase(coupon.getId(), customerID);
				Customer customer = CUSTOMER;
				customer.addCoupon(coupon);
			}catch (CouponSystemException e) {
				throw new CouponSystemException("purchaseCoupon failed " + e.getMessage());
			}
		}catch (CouponSystemException e) {
			throw new CouponSystemException("purchaseCoupon failed" + e.getMessage());
		}
	}
	
	
	public ArrayList<Coupon> getAllCoupons() throws CouponSystemException{
		try {
		return this.couponDBDAO.getAllCustomerCoupons(customerID);				
		}catch (CouponSystemException e) {
			throw new CouponSystemException("getAllCoupons failed" , e);
		}
	}

	public ArrayList<Coupon> getAllCategoryCoupons(int categoryId) throws CouponSystemException{
		ArrayList<Coupon> coupons = new ArrayList<>();
		for(Coupon coupon: getAllCoupons()) {
			if(coupon.getCategoryId() == categoryId) {
				coupons.add(coupon);
			}
		}
		return coupons;
	}
	
	public ArrayList<Coupon> getCouponsUnderPrice(double maxPrice)throws CouponSystemException{
		ArrayList<Coupon> coupons = new ArrayList<>();
		for(Coupon coupon: getAllCoupons()) {
			if(coupon.getPrice() <= maxPrice) {
				coupons.add(coupon);
			}
		}
		return coupons;
	}
	
	public Customer getCustomerDetails() {
		return CUSTOMER;
	}

	public int getId() {
		return customerID;
	}
}
























