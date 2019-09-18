package JDBC;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class JdbcUtils {
	private static Properties props = null;
	
	static{
		//加载配置文件	 
		try {
			InputStream in = JdbcUtils.class.getClassLoader().getResourceAsStream("dbconfig.properties");
			props = new Properties();
			props.load(in);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		try {
			// 加载驱动类 
			Class.forName(props.getProperty("driverClassName"));
		} catch (Exception e) {
			throw new RuntimeException();
		}
				
	}
	
	public static Connection getConnection() throws Exception {
		/*
		 * 1.加载配置文件
		 * 2.加载驱动类
		 * 3.调用DriverManager.getConnection()
		 */
		
		
		// 得到Connection
		return DriverManager.getConnection(props.getProperty("url"), 
				props.getProperty("username"), 
				props.getProperty("password"));
	}
}
