package app.DAO;

import java.util.ArrayList;

import app.core.beans.Coupon;
import app.exceptions.CouponSystemException;

public interface CouponDAO {
	/*
	 * interface for coupon DAO
	 */
	public void addCoupon(Coupon coupon) throws CouponSystemException;
	//creating a new coupon
	
	public void updateCoupon(Coupon coupon) throws CouponSystemException;
	//updating coupon details
	
	public void deleteCoupon(int couponId) throws CouponSystemException;
	//deleting a coupon from db
	
	public ArrayList<Coupon> getAllCoupons() throws CouponSystemException;
	//returns list of all coupons in db	
	
	public Coupon getCoupon(int couponId) throws CouponSystemException;
	//returns coupon with id			
	
	public void addCouponPurchase(int couponId, int customerId) throws CouponSystemException;
	//used when coupon is bought by customer 
	
	public void deleteCouponPurchase(int couponId, int customerId) throws CouponSystemException;
 
}
