package app.DbDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import app.DAO.CouponDAO;
import app.core.beans.Coupon;
import app.core.connections.ConnectionPool;
import app.exceptions.CouponSystemException;

public class CouponDBDAO implements CouponDAO {
	/*
	 * implements SQL methods for coupons using the DAO 
	 */
	ConnectionPool pool;

	//constructor initiates class with connection pool instance
	public CouponDBDAO() throws CouponSystemException {
		try {
			this.pool = ConnectionPool.getInstance();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addCoupon(Coupon coupon) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt = con
					.prepareStatement("insert into `COUPONS` values(0, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

			pstmt.setInt(1, coupon.getCompanyId());
			pstmt.setInt(2, coupon.getCategoryId());
			pstmt.setString(3, coupon.getTitle());
			pstmt.setString(4, coupon.getDescription());
			pstmt.setDate(5, new java.sql.Date(coupon.getStartDate().getTime()));
			pstmt.setDate(6, new java.sql.Date(coupon.getEndDate().getTime()));
			pstmt.setInt(7, coupon.getAmount());
			pstmt.setDouble(8, coupon.getPrice());
			pstmt.setString(9, coupon.getImage());

			pstmt.executeUpdate();

		} catch (Exception e) {
			throw new CouponSystemException("addCoupon Failed", e);
		} finally {
			pool.restoreConnection(con);
		}
	}

	@Override
	public void updateCoupon(Coupon coupon) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					" update coupons set "
					+ " coupons.CATEGORY_ID = ?, coupons.title = ?, coupons.DESCRIPTION = ?,"
					+ " coupons.START_DATE = ?, coupons.END_DATE = ?,"
					+ " coupons.AMOUNT = ?, coupons.PRICE = ?, coupons.IMAGE = ? "
					+ " where coupons.ID = ? and coupons.COMPANY_ID = ?;");
			pstmt.setInt(1, coupon.getCategoryId());
			pstmt.setString(2, coupon.getTitle());
			pstmt.setString(3, coupon.getDescription());
			pstmt.setDate(4, new java.sql.Date(coupon.getStartDate().getTime()));
			pstmt.setDate(5, new java.sql.Date(coupon.getEndDate().getTime()));
			pstmt.setInt(6, coupon.getAmount());
			pstmt.setDouble(7, coupon.getPrice());
			pstmt.setString(8, coupon.getImage());
			pstmt.setInt(9, coupon.getId());
			pstmt.setInt(10, coupon.getCompanyId());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new CouponSystemException("updateCoupon failed", e);
		} finally {
			pool.restoreConnection(con);
		}
	}

	@Override
	public void deleteCoupon(int couponId) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt1 = con.prepareStatement(
					"delete from `CUSTOMERS_VS_COUPONS` "
							+ "where customers_vs_coupons.COUPON_ID = ?;");
			pstmt1.setInt(1, couponId);
			pstmt1.executeUpdate();
			PreparedStatement pstmt2 = con.prepareStatement(
					"delete from `COUPONS` where COUPONS.ID = ?;");
			pstmt2.setInt(1, couponId);
			pstmt2.executeUpdate();
		} catch (Exception e) {
			throw new CouponSystemException("deleteCoupon failed ", e);
		} finally {
			pool.restoreConnection(con);
		}

	}

	@Override
	public ArrayList<Coupon> getAllCoupons() throws CouponSystemException {
		ArrayList<Coupon> coupons = new ArrayList<>();
		Connection con = null;
		try {
			con = pool.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `COUPONS`; ");
			while (rs.next()) {
				Coupon coupon = tableSQLToCoupon(rs);
				coupons.add(coupon);
			}
		} catch (Exception e) {
			throw new CouponSystemException("getAllCoupons failed ", e);
		} finally {
			pool.restoreConnection(con);
		}
		return coupons;
	}

	@Override
	public Coupon getCoupon(int couponId) throws CouponSystemException {
		Coupon coupon = null;
		if (isCouponExists(couponId)) {
			Connection con = null;
			try {
				con = pool.getConnection();
				PreparedStatement pstmt = con
						.prepareStatement(
								"select * from `COUPONS` where COUPONS.ID = ?;");
				pstmt.setInt(1, couponId);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					coupon = tableSQLToCoupon(rs);
				}
			} catch (Exception e) {
				throw new CouponSystemException("getCoupons failed ", e);
			} finally {
				pool.restoreConnection(con);
			}
		} else {
			throw new CouponSystemException("Coupon doesn't exists");
		}
		return coupon;
	}

	@Override
	public void addCouponPurchase(int couponId, int customerId) throws CouponSystemException{
		Connection con = null;
		try {
			con = pool.getConnection();
			//update coupon amount
			PreparedStatement pstmt1 = con.prepareStatement(
					"update coupons set "
					+ "coupons.AMOUNT = coupons.AMOUNT - 1 "
					+ "where coupons.ID = ?;");
			pstmt1.setInt(1, couponId);
			pstmt1.executeUpdate();
			//update connecter table
			PreparedStatement pstmt2 = con
					.prepareStatement("insert into `CUSTOMERS_VS_COUPONS` values(?,?);");
			pstmt2.setInt(1, customerId);
			pstmt2.setInt(2, couponId);
			pstmt2.executeUpdate();
		}catch (Exception e) {
			throw new CouponSystemException("addCouponPurchase failed ", e);
		}finally {
			pool.restoreConnection(con);
		}
	}

	@Override
	public void deleteCouponPurchase(int couponId, int customerId) throws CouponSystemException {
		Connection con = null;
		try {
			con = pool.getConnection();
			//update coupon amount
			PreparedStatement pstmt1 = con.prepareStatement(
					"update coupons set "
					+ "coupons.AMOUNT = coupons.AMOUNT + 1 "
					+ "where coupons.ID = ?;");
			pstmt1.setInt(1, couponId);
			pstmt1.executeUpdate();
			//delete from connecter table
			PreparedStatement pstmt2 = con.prepareStatement(
					"delete from `CUSTOMERS_VS_COUPONS` "
					+ "where customers_vs_coupons.CUSTOMER_ID = ? "
					+ "and customers_vs_coupons.COUPON_ID = ?;");
			pstmt2.setInt(1, customerId);
			pstmt2.setInt(2, couponId);
			pstmt2.executeUpdate();
		}catch (Exception e) {
			throw new CouponSystemException("deleteCouponPurchase failed ", e);
		}finally {
			pool.restoreConnection(con);
		}
	}

	public boolean isCouponExists(int couponId) throws CouponSystemException {
		Connection con = null;
		Coupon coupon = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt = con.prepareStatement(
					"select * from `COUPONS` where COUPONS.ID = ?;");
			pstmt.setInt(1, couponId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				coupon = tableSQLToCoupon(rs);
			}
		} catch (Exception e) {
			throw new CouponSystemException("couponExists failed " + e.getMessage());
		} finally {
			pool.restoreConnection(con);
		}
		return coupon != null;
	}

	/*
	 * this method is used to make the other methods easier
	 * turns the resultSet to Java coupon bean
	 */
	public static Coupon tableSQLToCoupon(ResultSet rs) throws SQLException {

		Coupon Coupon = new Coupon();
		Coupon.setId(rs.getInt("ID"));
		Coupon.setCompanyId(rs.getInt("COMPANY_ID"));
		Coupon.setCategoryId(rs.getInt("CATEGORY_ID"));
		Coupon.setTitle(rs.getString("TITLE"));
		Coupon.setDescription(rs.getString("DESCRIPTION"));
		Coupon.setStartDate(rs.getDate("START_DATE"));
		Coupon.setEndDate(rs.getDate("END_DATE"));
		Coupon.setAmount(rs.getInt("AMOUNT"));
		Coupon.setPrice(rs.getDouble("PRICE"));
		Coupon.setImage(rs.getString("IMAGE"));

		return Coupon;
	}
	
	
	public Coupon getCoupon(ResultSet couponIdRS) throws CouponSystemException {
		Coupon coupon = null;
		Connection con = null;
		try {
			con = pool.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
			"select * from `COUPONS` where COUPONS.ID = " + couponIdRS + ";");
			if (rs.next()) {
				coupon = tableSQLToCoupon(rs);
			}
		} catch (Exception e) {
			throw new CouponSystemException("getCoupons failed ", e);
		} finally {
			pool.restoreConnection(con);
		}
		return coupon;
	}
	
	// used in company facade
	public void deleteCompanyCoupons(int companyId) throws CouponSystemException{
		Connection con = null;
		try {
			con = pool.getConnection();
			for(Coupon c: getAllCompanyCoupons(companyId)) {
				deleteCoupon(c.getId());
			}
		}catch (Exception e) {
			throw new CouponSystemException("deleteCompanyCoupons failed", e);
		}finally {
			pool.restoreConnection(con);
		}
	}

	// used in company facade
	public ArrayList<Coupon> getAllCompanyCoupons(int companyId) throws CouponSystemException {
		ArrayList<Coupon> coupons = new ArrayList<>();
		Connection con = null;
		try {
			con = pool.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `COUPONS` "
					+ "where coupons.COMPANY_ID = " + companyId + ";");
			while (rs.next()) {
				Coupon coupon = tableSQLToCoupon(rs);
				coupons.add(coupon);
			}
		} catch (Exception e) {
			throw new CouponSystemException("getAllCompanyCoupons failed ", e);
		} finally {
			pool.restoreConnection(con);
		}
		return coupons;
	}
	
	// used in customer facade
	public ArrayList<Coupon> getAllCustomerCoupons(int customerId) throws CouponSystemException {
		Set<Coupon> couponSet = new HashSet<>();
		Connection con = null;
		try {
			con = pool.getConnection();
			PreparedStatement pstmt1 = con.prepareStatement( 
					"SELECT * FROM coupons "
					+ "JOIN customers_vs_coupons "
					+ "ON customers_vs_coupons.COUPON_ID = coupons.ID"
					+ " where customers_vs_coupons.CUSTOMER_ID = ?;");
			pstmt1.setInt(1, customerId);
			ResultSet rs = pstmt1.executeQuery();
			while (rs.next()) {
				Coupon coupon = tableSQLToCoupon(rs);
				couponSet.add(coupon);
			}
			ArrayList<Coupon> coupons = new ArrayList<>(couponSet);
			return coupons;
		} catch (Exception e) {
			throw new CouponSystemException("getAllCustomerCoupons failed ", e);
		} finally {
			pool.restoreConnection(con);
		}	
	}
	
	
	
////	==============================test================================
//	
//	public static int couponNum = 1;
//	public static void main(String[] args) {
//		try {
//			CouponDBDAO dao = new CouponDBDAO();
//			dao.addCoupon(couponMaker());
//			dao.addCoupon(couponMaker());
//			Coupon coupon = couponMaker();
//			coupon.setCompanyId(2);
//			dao.addCoupon(coupon);
//			for (Coupon c: dao.getAllCoupons()) {
//				System.out.println(c);
//			}
//			System.out.println("===============================");
//			for (Coupon c: dao.getAllCompanyCoupons(1)) {
//				System.out.println(c);
//			}
//			Coupon coupon2 = couponMaker();
//			coupon2.setId(2);
//			coupon2.setCompanyId(1);
//			dao.updateCoupon(coupon2);
//			System.out.println("===============================");
//			System.out.println(coupon2);
//			System.out.println("===============================");
//			for (Coupon c: dao.getAllCoupons()) {
//				System.out.println(c);
//			}
//			dao.deleteCoupon(4);
//			System.out.println("===============================");
//			for (Coupon c: dao.getAllCoupons()) {
//				System.out.println(c);
//			}
//			dao.deleteCompanyCoupons(1);
//			System.out.println("===============================");
//			for (Coupon c: dao.getAllCoupons()) {
//				System.out.println(c);
//			}
//			System.out.println("===============================");
//			dao.deleteCompanyCoupons(2);
//			System.out.println("===============================");
//			for (Coupon c: dao.getAllCoupons()) {
//				System.out.println(c);
//			}
//			System.out.println("===============================");
//			dao.addCouponPurchase(7, 1);
//			System.out.println(dao.getCoupon(7));
//			System.out.println("===============================");
//			dao.deleteCouponPurchase(7, 1);
//			System.out.println(dao.getCoupon(7));
//		} catch (CouponSystemException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public static Coupon couponMaker() {
//		Date startD = new Date(System.currentTimeMillis());
//		Date endD = addDays(startD, 1);
//		return new Coupon(couponNum, 1, couponNum, "coupon" + couponNum, "coupon" + couponNum, startD, endD, 4, 19.9, "couponImage" + couponNum++);
//	}
//	
//	public static Date addDays(Date date, int days) {
//        Calendar c = Calendar.getInstance();
//        c.setTime(date);
//        c.add(Calendar.DATE, days);
//        return new Date(c.getTimeInMillis());
//    }
	
}
