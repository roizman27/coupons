# coupons
Java project for John Bryce course

projet needs a sql server to work

the connections uses (setup and connectionPool):

	String dbUrl = "jdbc:mysql://localhost:3306/coupondb";
	
	String user = "root";
		
	String pass = "1234M";

table creation should be used before running the testALL function

table creation and drop methodes are under app.setUpDB in twe seperate classes (both are main and don't use the connectionPool)

main test program runs from Program class under app.main

project uses lombok, mysql-connector-java-8.0.27 libraries
