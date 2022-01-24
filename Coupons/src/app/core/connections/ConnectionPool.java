package app.core.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import app.exceptions.CouponSystemException;

public class ConnectionPool {
	/*
	 * connection pool to limit the number of simultaneous actions done on the data base
	 * open connections when system turns on and closes all connections when it closes 
	 */
	private Set<Connection> connections = new HashSet<Connection>();
	public static final int POOL_SIZE = 5;
	private String dbUrl = "jdbc:mysql://localhost:3306/coupondb";
	private String user = "root";
	private String pass = "1234M";
	private boolean open; // indicator for system up or down

	private static ConnectionPool instance;

	private ConnectionPool() throws SQLException {
		// add 5 connections to pool
		for (int i = 0; i < POOL_SIZE; i++) {
			this.connections.add(DriverManager.getConnection(dbUrl, user, pass));
		}
		open = true;
	}

	public static ConnectionPool getInstance() throws SQLException {
		if (instance == null) {
			instance = new ConnectionPool();
		}
		return instance;
	}
	
	/*
	 * getConnection used to allow use of a connection to a DAO method
	 * wait if pool is empty
	 * won't give new connections after pool close
	 */
	public synchronized Connection getConnection() throws CouponSystemException {
		if (!open) {
			throw new CouponSystemException("getConnection failed - pool is closed");
		}
		while (this.connections.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// if we are here - there are available connections
		Iterator<Connection> it = connections.iterator();
		Connection con = it.next();
		it.remove();
		return con;
	}
	
	/*
	 * returns a connection to the pool
	 */
	public synchronized void restoreConnection(Connection con) {
		this.connections.add(con);
		notifyAll();
	}
	
	/*
	 * closing the connection pool when system is closing
	 */
	public synchronized void closeAllConnections() {
		open = false;
		while (this.connections.size() < POOL_SIZE) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Iterator<Connection> it = this.connections.iterator();
		while (it.hasNext()) {
			try {
				it.next().close();
				it.remove();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
////	test
//	public static void main(String[] args) {
//		try {
//			ConnectionPool pool = ConnectionPool.getInstance();
//			Connection con = pool.getConnection();
//			pool.restoreConnection(con);
//			pool.closeAllConnections();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
