package app.setUpDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DropTables {
	/*
	 * drops all SQL tables, used for testing
	 */
	public static void main(String[] args) {
		String dbUrl = "jdbc:mysql://localhost:3306/coupondb";
		String user = "root";
		String pass = "1234M";
		
		try (Connection con = DriverManager.getConnection(dbUrl, user, pass);) {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("drop table if exists CUSTOMERS_VS_COUPONS;");
			stmt.executeUpdate("drop table if exists COUPONS;");
			stmt.executeUpdate("drop table if exists CATEGORIES;");
			stmt.executeUpdate("drop table if exists CUSTOMERS;");
			stmt.executeUpdate("drop table if exists companies;");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			System.out.println("Tables dropped");
		}
	}

}
