package app.facades;

import java.util.ArrayList;


import app.DbDAO.CompanyDBDAO;
import app.DbDAO.CouponDBDAO;
import app.DbDAO.CustomerDBDAO;
import app.core.beans.Company;
import app.core.beans.Coupon;
import app.exceptions.CouponSystemException;

public class CompanyFacade extends ClientFacade {
	
	/*
	 * company facade is used by company to add update or remove coupons from the app date base
	 * all actions are done for the company thats logged in and don't affect other companies  
	 */
	private static int companyID;

	public CompanyFacade() {
		try {
			this.companyDBDAO = new CompanyDBDAO();
			this.couponDBDAO = new CouponDBDAO();
			this.customerDBDAO = new CustomerDBDAO();
		} catch (CouponSystemException e) {
			// TODO Auto-generated catch block
			System.err.println("Company facade ctor failed: " + e.getMessage());
		}
	}
	public int getId() {
		return companyID;
	}
	
	//checks if company exists and returns the id for the other methods to use
	public boolean Login(String email, String password) throws CouponSystemException {
		if(companyDBDAO.isCompanyExists(email, password)) {
			try {
				companyID = companyDBDAO.getCompanyId(email, password);
			}catch (CouponSystemException e) {
				throw new CouponSystemException("login failed " + e.getMessage());
			}	
		}
		return companyDBDAO.isCompanyExists(email, password);		
	}


	public void addCoupon (Coupon coupon) throws CouponSystemException{
		if(coupon.getCompanyId() == companyID)	{
			try {
				boolean b = true;
				for(Coupon c: this.couponDBDAO.getAllCompanyCoupons(companyID)) {
					if(coupon.getTitle() == c.getTitle()) {
						b = false;
					}
				}
				if(b) {
					this.couponDBDAO.addCoupon(coupon);
				} else {
					throw new CouponSystemException("addCoupon failed - coupon already exsits in company coupons list");
				}
			} catch (CouponSystemException e) {
				throw new CouponSystemException("couponDBDAO.getAllCompanyCoupons failed ", e);
			}
		}
	}
	
	public void updateCoupon(Coupon coupon) throws CouponSystemException {
		if(coupon.getCompanyId() == companyID)	{
			try {
				this.couponDBDAO.updateCoupon(coupon);
			} catch (CouponSystemException e) {
				throw new CouponSystemException("couponDBDAO.updateCoupon failed" , e);
			}
		}
	}
	
	public void deleteCoupon (int couponId) throws CouponSystemException {
		try {
			this.couponDBDAO.deleteCoupon(couponId);
		} catch (CouponSystemException e) {
			throw new CouponSystemException("deleteCoupon failed ",e);
		}
	}
	

	public ArrayList<Coupon> getAllCompanyCoupons() throws CouponSystemException{
		try {
			ArrayList<Coupon> coupons = this.couponDBDAO.getAllCompanyCoupons(companyID);
			return coupons;
		} catch (CouponSystemException e) {
			// TODO Auto-generated catch block
			throw new CouponSystemException("getAllCompanyCoupons failed ",e);
		}		
	}
	
	public ArrayList<Coupon> getCompanyCouponsByCategory(int categoryId) throws CouponSystemException{
		ArrayList<Coupon> coupons = new ArrayList<>();
		try {
			for(Coupon coupon:this.getAllCompanyCoupons()) {
				if(coupon.getCategoryId() == categoryId) {
					coupons.add(coupon);
				}
			}
			return coupons;
		} catch (CouponSystemException e) {
			// TODO Auto-generated catch block
			throw new CouponSystemException("getCompanyCouponsByCategory failed ",e);
		}
	}

	public ArrayList<Coupon> getCompanyCouponsUnderPrice(double maxPrice) throws CouponSystemException{
		ArrayList<Coupon> coupons = new ArrayList<>();
		try {
			for(Coupon coupon:this.getAllCompanyCoupons()) {
				if(coupon.getPrice() <= maxPrice) {
					coupons.add(coupon);
				}
			}
			return coupons;
		} catch (CouponSystemException e) {
			throw new CouponSystemException("getCompanyCouponsUnderPrice failed ",e);
		}
		
		
	}

	//returns the logged company details
	public Company getCompanyDetails() throws CouponSystemException {
		try {
			return this.companyDBDAO.getCompany(companyID);
		} catch (CouponSystemException e) {
			throw new CouponSystemException("getCompanyDetails failed ",e);
		}
	}
}





















