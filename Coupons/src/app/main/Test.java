package app.main;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

import app.Clients.ClientType;
import app.Clients.LoginManager;
import app.core.Threads.CouponExpirationDailyJob;
import app.core.beans.Category;
import app.core.beans.Company;
import app.core.beans.Coupon;
import app.core.beans.Customer;
import app.core.connections.ConnectionPool;
import app.exceptions.CouponSystemException;
import app.facades.AdminFacade;
import app.facades.CompanyFacade;
import app.facades.CustomerFacade;

public class Test {
	public static void testAll() {
		
		//used for auto testing of the 3 facade classes
		System.out.println("********************************daily job Test********************************");
		try {
			CouponExpirationDailyJob job = new CouponExpirationDailyJob();
			Thread dailyJob = new Thread(job);
			dailyJob.start();
			System.out.println("\n");
			LoginManager clientLogin = LoginManager.getInstance();
			System.out.println("********************************Admin facade Test********************************");
			AdminFacade adminF = (AdminFacade) clientLogin.Login("admin@admin.com", "admin", ClientType.Administrator);

			System.out.println();
			System.out.println("=========================adding app clients=======================");
			System.out.println();
			adminF.addCompany(companyMaker());
			adminF.addCompany(companyMaker());
			adminF.addCompany(companyMaker());
			adminF.addCompany(companyMaker());
			System.out.println("4 companies created");

			adminF.addCustomer(customerMaker());
			adminF.addCustomer(customerMaker());
			adminF.addCustomer(customerMaker());
			adminF.addCustomer(customerMaker());
			System.out.println("4 customers created");
			System.out.println();

			System.out.println("=========================testing companies=======================");
			System.out.println();
			System.out.println("get companies list:");
			for (Company company : adminF.getAllCompanies()) {
				System.out.println(company);
			}
			System.out.println("___________________________________________");
			System.out.println();

			Company updateTestCompany = companyMaker();
			updateTestCompany.setId(2);
			updateTestCompany.setName("Company1");
			updateTestCompany.setEmail("updateCompanyEmail@email");
			System.out.println("company: ");
			System.out.println(adminF.getCompany(2));
			adminF.updateCompany(updateTestCompany);
			System.out.println("updated to:");
			System.out.println(updateTestCompany);
			System.out.println("new companies list:");
			for (Company company : adminF.getAllCompanies()) {
				System.out.println(company);
			}
			System.out.println("___________________________________________");
			System.out.println();

			System.out.println("delete company: " + adminF.getCompany(3));
			adminF.deleteCompany(3);
			for (Company company : adminF.getAllCompanies()) {
				System.out.println(company);
			}
			System.out.println();

			System.out.println("=========================testing customers=======================");
			System.out.println();
			System.out.println("get client list:");
			for (Customer customer : adminF.getAllCustomers()) {
				System.out.println(customer);
			}
			System.out.println("___________________________________________");
			System.out.println();

			Customer updateTestCustomer = customerMaker();
			updateTestCustomer.setId(2);
			System.out.println("customer: " + adminF.getCustomer(2));
			adminF.updateCustomer(updateTestCustomer);
			System.out.println("updated to:");
			System.out.println(updateTestCustomer);
			System.out.println("new customers list: ");
			for (Customer customer : adminF.getAllCustomers()) {
				System.out.println(customer);
			}

			System.out.println("___________________________________________");
			System.out.println();
			System.out.println("delete customer: " + adminF.getCustomer(3));
			adminF.deleteCustomer(3);
			System.out.println("new customers list: ");
			for (Customer customer : adminF.getAllCustomers()) {
				System.out.println(customer);
			}
			System.out.println("___________________________________________");

			System.out.println("\n\n");
			System.out.println("********************************company facade Test********************************");
			System.out.println();

			CompanyFacade companyF = (CompanyFacade) clientLogin.Login("Company0@mail.com", "1111", ClientType.Company);
			System.out.println("testing for company with id = " + companyF.getId());

			System.out.println("adding coupons");
			for (int i = 0; i < 20; i++) {
				Coupon coupon = couponMaker();
				coupon.setCompanyId(companyF.getId());
				companyF.addCoupon(coupon);
			}
			Coupon failSafeCoupon = couponMaker();// for auto test for update and delete methods
			failSafeCoupon.setPrice(10.0);
			companyF.addCoupon(failSafeCoupon);
			System.out.println("coupons added");
			System.out.println("___________________________________________");
			System.out.println();

			System.out.println("company coupons list");
			for (Coupon coupon : companyF.getAllCompanyCoupons()) {
				System.out.println(coupon);
			}
			System.out.println("___________________________________________");
			System.out.println();

			System.out.println("get coupons by category: ");
			System.out.println("\n" + Category.getCategory(1) + " category: ");
			for (Coupon coupon : companyF.getCompanyCouponsByCategory(1)) {
				System.out.println(coupon);
			}

			System.out.println("\n" + Category.getCategory(2) + " category: ");
			for (Coupon coupon : companyF.getCompanyCouponsByCategory(2)) {
				System.out.println(coupon);
			}

			System.out.println("\n" + Category.getCategory(3) + " category: ");
			for (Coupon coupon : companyF.getCompanyCouponsByCategory(3)) {
				System.out.println(coupon);
			}

			System.out.println("\n" + Category.getCategory(4) + " category: ");
			for (Coupon coupon : companyF.getCompanyCouponsByCategory(4)) {
				System.out.println(coupon);
			}
			System.out.println("___________________________________________");
			System.out.println();

			System.out.println("get coupons under 70 monies price: "); // monies noun - plural form of money, as used in
																		// financial contexts.
			Coupon couponUpdate = new Coupon();
			for (Coupon coupon : companyF.getCompanyCouponsUnderPrice(70)) {
				System.out.println(coupon);
				couponUpdate = coupon; // getting the last one under the price to be updated
			}
			System.out.println("___________________________________________");
			System.out.println();

			System.out.println("update coupon with id: " + couponUpdate.getId());
			couponUpdate.setTitle("I was updated");
			couponUpdate.setDescription("I was updated");
			companyF.updateCoupon(couponUpdate);
			for (Coupon coupon : companyF.getCompanyCouponsUnderPrice(70)) {
				if (coupon.getId() == couponUpdate.getId()) {
					System.out.println("coupon was updated ");
					System.out.println(coupon);
				}
			}
			System.out.println("___________________________________________");
			System.out.println();

			System.out.println("delete coupons: (deleting all coupon with id > 5)");
			for (Coupon coupon : companyF.getAllCompanyCoupons()) {
				if (coupon.getId() > 5) {
					companyF.deleteCoupon(coupon.getId());
				}
			}
			System.out.println("___________________________________________");
			System.out.println();

			System.out.println("\n\n");
			System.out.println("********************************customer facade Test********************************");
			System.out.println();

			CustomerFacade customerF = (CustomerFacade) clientLogin.Login("first0.last0@mail.com", "1111",
					ClientType.Customer);
			System.out.println("testing done for customer with id number: " + customerF.getId());
			System.out.println("___________________________________________");
			System.out.println();
			System.out.println("get customer details: ");
			System.out.println(customerF.getCustomerDetails());
			System.out.println("___________________________________________");
			System.out.println();

			System.out.println("buying coupons ");
			Coupon buyMeCoupon1 = new Coupon(1);
			Coupon buyMeCoupon2 = new Coupon(2);
			customerF.purchaseCoupon(buyMeCoupon1);
			customerF.purchaseCoupon(buyMeCoupon2);
			System.out.println("coupons bought");
			for (Coupon coupon : customerF.getAllCoupons()) {
				System.out.println(coupon);
			}
			System.out.println("___________________________________________");
			System.out.println();
			System.out.println("customer coupons by category: ");
			System.out.println(Category.getCategory(1) + " category: ");
			for (Coupon coupon : customerF.getAllCategoryCoupons(1)) {
				System.out.println(coupon);
			}
			System.out.println("\n" + Category.getCategory(2) + " category: ");
			for (Coupon coupon : customerF.getAllCategoryCoupons(2)) {
				System.out.println(coupon);
			}
			System.out.println("\n" + Category.getCategory(3) + " category: ");
			for (Coupon coupon : customerF.getAllCategoryCoupons(3)) {
				System.out.println(coupon);
			}
			System.out.println("\n" + Category.getCategory(4) + " category: ");
			for (Coupon coupon : customerF.getAllCategoryCoupons(4)) {
				System.out.println(coupon);
			}
			System.out.println();
			dailyJob.interrupt();
			job.stop();

		} catch (CouponSystemException e) {
			System.err.println("Error: " + e.getMessage());
			// e.printStackTrace();
		} finally {
			try {
				ConnectionPool.getInstance().closeAllConnections();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static int companyNum = 0;
	private static int customerNum = 0;
	private static int couponNum = 0;

	public static Company companyMaker() {
		String name = ("Company" + companyNum++);
		String email = (name + "@mail.com");
		String pass = "1111";
		Company company = new Company(name, email, pass);
		return company;
	}

	public static Customer customerMaker() {
		String fName = ("first" + customerNum);
		String lName = ("last" + customerNum++);
		String email = (fName + "." + lName + "@mail.com");
		String pass = "1111";
		Customer customer = new Customer(fName, lName, email, pass);
		return customer;
	}

	public static Coupon couponMaker() {
		Date startD = new Date(System.currentTimeMillis());
		Date endD = addDays(startD, 1);
		return new Coupon(0, 0, 1 + (int) (Math.random() * 4), "coupon" + couponNum, "coupon" + couponNum, startD, endD,
				(int) (Math.random() * 101), Math.random() * 101, "couponImage" + couponNum++);
	}

	public static Date addDays(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return new Date(c.getTimeInMillis());
	}

}
