package app.setUpDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTables {
	/*
	 * SQL setup for the tables, used for testing
	 */
	public static void main(String[] args) {
		String dbUrl = "jdbc:mysql://localhost:3306/coupondb";
		String user = "root";
		String pass = "1234M";
		try (Connection con = DriverManager.getConnection(dbUrl, user, pass);){

			Statement stmt = con.createStatement();
		
			stmt.executeUpdate(
					"create table `COMPANIES` (`ID` int primary key auto_increment,"
					+ "`NAME` varchar(50),`EMAIL` varchar(50),`PASSWORD`  varchar(50));");
			
			stmt.executeUpdate(
					"create table `CUSTOMERS` (`ID` int primary key auto_increment,"
					+ "`FIRST_NAME` varchar(50),`LAST_NAME` varchar(50),"
					+ "`EMAIL` varchar(50),`PASSWORD` varchar(50));");
			
			stmt.executeUpdate(
					"create table `CATEGORIES` (`ID` int primary key auto_increment,"
					+ "`NAME` varchar(50));");

			stmt.executeUpdate(
					" insert into CATEGORIES values (1, 'Food');");
			stmt.executeUpdate(
					" insert into CATEGORIES values (2, 'Electricity');");
			stmt.executeUpdate(
					" insert into CATEGORIES values (3, 'Restaurant');");
			stmt.executeUpdate(
					" insert into CATEGORIES values (4, 'Vacation');");
		
			stmt.executeUpdate(
					"create table `COUPONS` (`ID` int primary key auto_increment,"
					+ "`COMPANY_ID` int, `CATEGORY_ID` int,`TITLE` varchar(50),"
					+ "`DESCRIPTION` varchar(50),`START_DATE` date,"
					+ "`END_DATE` date,`AMOUNT` integer,`PRICE` double,"
					+ "`IMAGE` varchar(50), foreign key(COMPANY_ID) REFERENCES COMPANIES(ID),"
					+ " FOREIGN KEY(CATEGORY_ID) REFERENCES CATEGORIES(ID));");
			
			stmt.executeUpdate(
					"create table `CUSTOMERS_VS_COUPONS`(`CUSTOMER_ID` int,"
					+ "`COUPON_ID` int,FOREIGN KEY (CUSTOMER_ID) references companies(id),"
					+ "FOREIGN KEY (COUPON_ID) references coupons(id)); ");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			System.out.println("Table setup done");
		}
	}

}
