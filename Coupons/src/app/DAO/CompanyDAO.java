package app.DAO;

import java.util.ArrayList;

import app.core.beans.Company;
import app.exceptions.CouponSystemException;

public interface CompanyDAO {
	/*
	 * interface for company DAO
	 */
	public boolean isCompanyExists(String email, String password) throws CouponSystemException ;
	//check for login 

	public void addCompany(Company company) throws CouponSystemException;
	//add to DB

	public void updateCompany(Company company) throws CouponSystemException;
	//update existing company if found
	
	public void deleteCompany(int companyId) throws CouponSystemException;
	//delete existing company if found
	
	public Company getCompany(int companyId) throws CouponSystemException;
	//return existing company if found

	public ArrayList<Company> getAllCompanys() throws CouponSystemException;
	//return all existing companies
	
}
