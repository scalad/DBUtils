package com.silence.utils;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionFactory {

	private static String driver;
	private static String dburl;
	private static String user;
	private static String password;
	public static ComboPooledDataSource dataSource;

	private static final ConnectionFactory factory = new ConnectionFactory();

	private Connection conn = null;

	// 初始化数据库连接池
	static {
		Properties prop = new Properties();
		try {
			InputStream inputStream = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties");
			prop.load(inputStream);
			driver = prop.getProperty("driver");
			dburl = prop.getProperty("dburl");
			user = prop.getProperty("user");
			password = prop.getProperty("password");
			dataSource = new ComboPooledDataSource();
			dataSource.setUser(user);
			dataSource.setPassword(password);
			dataSource.setJdbcUrl(dburl);
			dataSource.setDriverClass(driver);
			dataSource.setInitialPoolSize(10);
			dataSource.setMinPoolSize(5);
			dataSource.setMaxPoolSize(50);
			dataSource.setMaxStatements(100);
			dataSource.setMaxIdleTime(60);
		} catch (IOException e) {
			System.out.println("the file of datasouce configuration is not exist!");
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			System.out.println("datasouce config error!");
			e.printStackTrace();
		}
		System.out.println("datasouce init finish........!");
	}

	private ConnectionFactory() {
	}

	public static ConnectionFactory getInstance() {
		return factory;
	}

	public static ComboPooledDataSource getDataSouce(){
		return dataSource;
	}
	//获取连接
	public Connection getConnection() {
		if (null != dataSource) {
			try {
				conn = dataSource.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}

}
