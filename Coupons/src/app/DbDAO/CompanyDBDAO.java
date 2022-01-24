package app.DbDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import app.DAO.CompanyDAO;
import app.core.beans.Company;
import app.core.connections.ConnectionPool;
import app.exceptions.CouponSystemException;

public class CompanyDBDAO implements CompanyDAO {
	/*
	 * implements SQL methods for companies using the DAO interface 
	 */
	ConnectionPool pool;
	
	//constructor initiates class with connection pool instance
	public CompanyDBDAO() throws CouponSystemException {
		try {
			this.pool = ConnectionPool.getInstance();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean isCompanyExists(String email, String password) throws CouponSystemException {
		Connection con = null;
		Company company = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"select * from `COMPANIES`"
					+ " where COMPANIES.EMAIL = ? "
					+ "and COMPANIES.PASSWORD = ?;");
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				company = tableSQLToCompany(rs);
			} 
		} catch (Exception e) {
			throw new CouponSystemException("isCompanyExists failed " + e);

		} finally {
			pool.restoreConnection(con);
		}
		return company != null;
	}

	@Override
	public void addCompany(Company company) throws CouponSystemException {
		if (isCompanyNameExists(company.getName()) | isCompanyEmailExists(company.getEmail())) {
			throw new CouponSystemException("addCompany failed - company already exists in DB");
		} else {
			Connection con = null;
			try {
				con = pool.getConnection();
				PreparedStatement pstmt = con.prepareStatement("insert into `COMPANIES` values (0, ?, ?, ?);");
				pstmt.setString(1, company.getName());
				pstmt.setString(2, company.getEmail());
				pstmt.setString(3, company.getPassword());
				pstmt.executeUpdate();
			} catch (Exception e) {
				throw new CouponSystemException("addCompany failed ", e);
			} finally {
				pool.restoreConnection(con);
			}
		}
	}

	@Override
	public void updateCompany(Company company) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					 "update COMPANIES set EMAIL = ?, PASSWORD = ? Where id= ?;");
			pstmt.setString(1, company.getEmail());
			pstmt.setString(2, company.getPassword());
			pstmt.setInt(3, company.getId());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new CouponSystemException("updateCompany failed ", e);
		} finally {
			pool.restoreConnection(con);
		}

	}

	@Override
	public void deleteCompany(int companyId) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			//delete all of the coupons the company offered
			CouponDBDAO couDBDAO = new CouponDBDAO();
			couDBDAO.deleteCompanyCoupons(companyId);
			//delete the company
			PreparedStatement pstmt = con.prepareStatement(
					"delete from `COMPANIES` where ID = ?;");
			pstmt.setInt(1, companyId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new CouponSystemException("deleteCompany failed ", e);
		} finally {
			pool.restoreConnection(con);
		}
	
	}

	@Override
	public Company getCompany(int companyId) throws CouponSystemException {
		Connection con = null;
		Company company = null;
		if (isCompanyIdExists(companyId)) {
			try {
				con = pool.getConnection();
				PreparedStatement pstmt = con.prepareStatement("select * from `COMPANIES` where ID = ?;");
				pstmt.setInt(1, companyId);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					company = tableSQLToCompany(rs);
				}
			} catch (Exception e) {
				throw new CouponSystemException("getCompany failed - error accessing the database", e);
			} finally {
				pool.restoreConnection(con);
			}
		} else {
			throw new CouponSystemException("getCompany failed ");
		}
		return company;
	}

	@Override
	public ArrayList<Company> getAllCompanys() throws CouponSystemException {
		Connection con = null;
		ArrayList<Company> companies = new ArrayList<>();
		try {
			con = pool.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from `COMPANIES`;");
			while (rs.next()) {
				Company company = tableSQLToCompany(rs);
				companies.add(company);
			}
		} catch (Exception e) {
			throw new CouponSystemException("getAllCompanies failed ", e);
		} finally {
			pool.restoreConnection(con);
		}
		return companies;
	}

	public boolean isCompanyIdExists(int companyID) throws CouponSystemException {
		Connection con = null;
		Company company = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement(
							"select * from `COMPANIES` where `COMPANIES`.`ID` = ?;");
			pstmt.setInt(1, companyID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				company = tableSQLToCompany(rs);
			}
			pool.restoreConnection(con);
		} catch (Exception e) {
			throw new CouponSystemException("isCompanyExists failed ", e);
		}
		return company != null;
	}
	
	//used for checking that the company name is unique 
	public boolean isCompanyNameExists(String companyName) throws CouponSystemException {
		Connection con = null;
		Company company = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement(
							"select * from `COMPANIES` where COMPANIES.NAME = ?;");
			pstmt.setString(1, companyName);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				company = tableSQLToCompany(rs);
			}
			pool.restoreConnection(con);
		} catch (Exception e) {
			throw new CouponSystemException("isCompanyExists failed ", e);
		}
		return company != null;
	}

	//used for checking that the company email is unique 
	public boolean isCompanyEmailExists(String companyEmail) throws CouponSystemException {
		Connection con = null;
		Company company = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement(
							"select * from `COMPANIES` where COMPANIES.Email = ?;");
			pstmt.setString(1, companyEmail);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				company = tableSQLToCompany(rs);
			}
			pool.restoreConnection(con);
		} catch (Exception e) {
			throw new CouponSystemException("isCompanyExists failed ", e);
		}
		return company != null;
	}
	
	
	/*
	 * this method is used to make the other methods easier
	 * turns the resultSet to Java company bean
	 */
	public static Company tableSQLToCompany(ResultSet rs) throws SQLException {

		Company company = new Company();
		company.setId(rs.getInt("ID"));
		company.setName(rs.getString("NAME"));
		company.setPassword(rs.getString("PASSWORD"));
		company.setEmail(rs.getString("EMAIL"));

		return company;
	}

	//used in facade for login and company actions
	public int getCompanyId(String email, String password) throws CouponSystemException {
		Connection con = null;
		Company company = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"select * from Companies "
					+ "where companies.EMAIL = ? and companies.PASSWORD = ?");
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				company = tableSQLToCompany(rs);
			} 
		} catch (Exception e) {
			throw new CouponSystemException("getCompanyId failed " + e);

		} finally {
			pool.restoreConnection(con);
		}
		return company.getId();
	}
	
//	====================test==========================
//	public static int companyNum = 1;
//	public static void main(String[] args) {
//		try {
//			CompanyDBDAO dao = new CompanyDBDAO();
//			dao.addCompany(companyMaker());
//			dao.addCompany(companyMaker());
//			dao.updateCompany(update);
//			System.out.println(dao.getCompany(2));
//			System.out.println("==================================");
//			dao.deleteCompany(3);
//			for(Company c: dao.getAllCompanys()) {
//				System.out.println(c);
//			}
//			System.out.println("==================================");
//			Company getcomp = dao.getCompany(1);
//			System.out.println(dao.isCompanyExists(getcomp.getEmail(), getcomp.getPassword()));
//			System.out.println(dao.getCompanyId(getcomp.getEmail(), getcomp.getPassword()));
//			System.out.println(getcomp);
//			System.out.println("====================================");
//			Company update = new Company(1,"aaa", "aaa@mail", "aaa" );
//			dao.updateCompany(update);
//			getcomp = dao.getCompany(1);
//			System.out.println(getcomp);
//		} catch (CouponSystemException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public static Company companyMaker() {
//		String name = ("Company " + companyNum++);
//		String email = (name + "@mail.com");
//		String pass = "1111";
//		Company company = new Company(name, email, pass);
//		return company;
//	}


}
