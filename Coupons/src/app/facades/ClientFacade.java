package app.facades;

import app.DbDAO.CompanyDBDAO;
import app.DbDAO.CouponDBDAO;
import app.DbDAO.CustomerDBDAO;
import app.exceptions.CouponSystemException;

public abstract class ClientFacade {
	/*
	 * the facades are used for all business applications of the system
	 * login for each company or customer 
	 * login for system manager for adding or removing companies and customers 
	 */
	protected CompanyDBDAO companyDBDAO;
	protected CustomerDBDAO customerDBDAO;
	protected CouponDBDAO couponDBDAO;
	
	public boolean Login(String email, String password) throws CouponSystemException {
		boolean b = false;
		return b;
	}

	
}
