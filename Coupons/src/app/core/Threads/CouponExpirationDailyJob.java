package app.core.Threads;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import app.DbDAO.CouponDBDAO;
import app.core.beans.Coupon;
import app.exceptions.CouponSystemException;

public class CouponExpirationDailyJob implements Runnable {
	/*
	 * runnable job to create a thread that checks once a day if there are any
	 * expired coupons and deletes them should start running when system starts
	 */
	private static CouponDBDAO dao;
	private static boolean quit = false;
	private static Thread thread;

	public CouponExpirationDailyJob() throws CouponSystemException {
		dao = new CouponDBDAO();
		thread = new Thread();
	}

	@Override
	public void run() {
		System.out.println("coupon expiration cheack daily job started\n");
		while (!quit & !thread.isInterrupted()) {
			try {
				ExpirationDateDeleter();
				Thread.sleep(timeToSleep());

			} catch (CouponSystemException e) {
				System.out.println("daily job failed" + e.getMessage());
			} catch (InterruptedException e) {
				System.out.println("daily job interrupted");// interrupting the thread when closing the system
			} finally {
				System.out.println("daily job closed");
			}
		}
	}
	
//	used to stop the runnable
	public void stop() {
		quit = true;
		System.out.println("daily job closing");
		thread.interrupt();
	}
	
//	checking coupon end dates and deleting if needed  
	private static void ExpirationDateDeleter() throws CouponSystemException {
		try {
			Date today = new Date();
			for (Coupon coupon : dao.getAllCoupons()) {
				if (coupon.getEndDate().before(today)) {
					dao.deleteCoupon(coupon.getId());
				}
			}
		} catch (CouponSystemException e) {
			// TODO Auto-generated catch block
			throw new CouponSystemException("ExpirationDateChecker failed ", e);
		}
	}

	//calculating time to midnight so the thread sleeps till then
	private static long timeToSleep() {
		LocalDate today = LocalDate.now();
		LocalTime midnight = LocalTime.of(00, 00, 00);
		LocalDateTime todayAtMidnight = LocalDateTime.of(today, midnight);
		ZonedDateTime zdt = ZonedDateTime.of(todayAtMidnight, ZoneId.systemDefault());
		long timeMilis = System.currentTimeMillis() - zdt.toInstant().toEpochMilli();
		return timeMilis;

	}

}
