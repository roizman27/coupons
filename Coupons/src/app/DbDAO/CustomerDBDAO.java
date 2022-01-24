package app.DbDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import app.DAO.CustomerDAO;
import app.core.beans.Customer;
import app.core.connections.ConnectionPool;
import app.exceptions.CouponSystemException;

public class CustomerDBDAO implements CustomerDAO {
	/*
	 * implements SQL methods for companies using the DAO
	 */
	ConnectionPool pool;

	//constructor initiates class with connection pool instance
	public CustomerDBDAO() throws CouponSystemException {
		try {
			this.pool = ConnectionPool.getInstance();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean isCustomerExists(String email, String password) throws CouponSystemException {
		Connection con = null;
		Customer customer = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"select * from `CUSTOMERS` "
					+ "where CUSTOMERS.EMAIL = ? "
					+ "and CUSTOMERS.PASSWORD = ?;");
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				customer = tableSQLToCustomer(rs);
			}
		} catch (Exception e) {
			throw new CouponSystemException("isCustomerExists failed " + e);
		} finally {
			pool.restoreConnection(con);
		}
		return customer != null;
	}

	@Override
	public void addCustomer(Customer customer) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
							"insert into `CUSTOMERS` values (0, ?, ?, ?, ?);");
			pstmt.setString(1, customer.getFirstName());
			pstmt.setString(2, customer.getLastName());
			pstmt.setString(3, customer.getEmail());
			pstmt.setString(4, customer.getPassword());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new CouponSystemException("addCustomer failed - error accessing the database", e);
		} finally {
			pool.restoreConnection(con);
		}
	}

	@Override
	public void updateCustomer(Customer customer) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"update `CUSTOMERS` set FIRST_NAME = ?,"
							+ "LAST_NAME = ?, EMAIL = ?, PASSWORD = ? "
							+ "where ID = ? ;");
			pstmt.setString(1, customer.getFirstName());
			pstmt.setString(2, customer.getLastName());
			pstmt.setString(3, customer.getEmail());
			pstmt.setString(4, customer.getPassword());
			pstmt.setInt(5,customer.getId());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new CouponSystemException("updateCustomer failed - error accessing the database", e);
		} finally {
			pool.restoreConnection(con);
		}
	}

	@Override
	public void deleteCustomer(int customerId) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			// delete from connector table
			PreparedStatement pstmt1 = con.prepareStatement(
					"delete from `CUSTOMERS_VS_COUPONS` "
							+ "where customers_vs_coupons.CUSTOMER_ID = ? ;");
			pstmt1.setInt(1, customerId);
			pstmt1.executeUpdate();
			// delete from customers
			PreparedStatement pstmt2 = con.prepareStatement(
					"delete from CUSTOMERS where CUSTOMERS.ID = ?;");
			pstmt2.setInt(1, customerId);
			pstmt2.executeUpdate();
		} catch (Exception e) {
			throw new CouponSystemException("deleteCustomer failed - error accessing the database", e);
		} finally {
			pool.restoreConnection(con);
		}
	}

	@Override
	public ArrayList<Customer> getAllCustomers() throws CouponSystemException {
		Connection con = null;
		ArrayList<Customer> customers = new ArrayList<>();
		try {
			con = pool.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from `CUSTOMERS`;");
			while (rs.next()) {
				Customer customer = tableSQLToCustomer(rs);
				customers.add(customer);
			}
		} catch (Exception e) {
			throw new CouponSystemException("getAllCustomers failed " + e.getMessage());
		} finally {
			pool.restoreConnection(con);
		}
		return customers;
	}

	@Override
	public Customer getCustomer(int customerId) throws CouponSystemException {
		Customer customer = null;
		Connection con = null;
		try {
			con = pool.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from `CUSTOMERS` where CUSTOMERS.ID = " + customerId + ";");
			if(rs.next()) {
				customer = tableSQLToCustomer(rs);
			}
		} catch (Exception e) {
			throw new CouponSystemException("getCustomer failed " + e.getMessage());
		} finally {
			pool.restoreConnection(con);
		}
		return customer;
	}

	public Boolean isCustomerIdExists(int customerId) throws CouponSystemException {
		Connection con = null;
		Customer customer = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"select * from `CUSTOMERS` where `CUSTOMERS`.`ID` = ?;");
			pstmt.setInt(1, customerId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				customer = tableSQLToCustomer(rs);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			pool.restoreConnection(con);
		}
		return customer != null;
	}

	//customer email needs to be unique
	public Boolean isCustomerEmailExists(String customerEmail) throws CouponSystemException {
		Connection con = null;
		Customer customer = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"select * from CUSTOMERS where CUSTOMERS.EMAIL = ?;");
			pstmt.setString(1, customerEmail);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				customer = tableSQLToCustomer(rs);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			pool.restoreConnection(con);
		}
		return customer != null;
	}
	
	/*
	 * this method is used to make the other methods easier
	 * turns the resultSet to Java customer bean
	 */
	public static Customer tableSQLToCustomer(ResultSet rs) throws SQLException {
		Customer customer = new Customer();
		customer.setId(rs.getInt("ID"));
		customer.setEmail(rs.getString("EMAIL"));
		customer.setPassword(rs.getString("PASSWORD"));
		customer.setFirstName(rs.getString("FIRST_NAME"));
		customer.setLastName(rs.getString("LAST_NAME"));

		return customer;
	}
	
	//for customer facade login and actions
	public int getCustomerId(String email, String password) throws CouponSystemException {
		Connection con = null;
		Customer customer = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"select * from customers "
					+ "where customers.EMAIL = ? "
					+ "and customers.PASSWORD = ?;");
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				customer = tableSQLToCustomer(rs);
			} 
		} catch (Exception e) {
			throw new CouponSystemException("getCustomerId failed " + e);

		} finally {
			pool.restoreConnection(con);
		}
		return customer.getId();
	}
	
//	========================test======================
//	public static int customerNum = 1;
//	
//	public static void main(String[] args) {
//		try {
//			CustomerDBDAO dao = new CustomerDBDAO();
//			Customer c = dao.getCustomer(1);
//			Coupon coupon1 = new Coupon(1);
//			Coupon coupon2 = new Coupon(2);
//			c.addCoupon(coupon1);
//			c.addCoupon(coupon2);
//			System.out.println(c);
//			System.out.println("============================");
//		} catch (CouponSystemException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//		
//	public static Customer customerMaker() {
//		String fName = ("first" + customerNum);
//		String lName = ("last" + customerNum++);
//		String email = (fName + "." + lName + "@mail.com");
//		String pass = "1111";
//		Customer customer = new Customer(fName, lName, email, pass);
//		return customer;
//	}





}
