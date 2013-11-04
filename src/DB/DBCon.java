package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.sql.DataSource;

public class DBCon {
	/*
	 * 获取数据库联接
	 */
    public static Connection getCon() {

    	Connection conn = null;
		Context ctx = null;
		DataSource ds = null;
		try {
			ctx = new InitialContext();
			ds=(DataSource)ctx.lookup("java:comp/env/jdbc/wb");
			conn = ds.getConnection();
		}  catch (SQLException e) {
			e.printStackTrace();
		} catch (NoInitialContextException e) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://124.17.88.241:4092/lili?characterEncoding=utf-8", "root", "123456");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} 
		//如果为空 则再请求2次，以防止突然的连接中断与恢复 造成的影响。
		if(conn == null){
			conn = getConnOnce();
		}
		if(conn == null){
			conn = getConnOnce();
		}
		return conn;

    }

    
    public static Connection getConnOnce(){
		Connection conn = null;
		Context ctx = null;
		DataSource ds = null;
		try {
			ctx = new InitialContext();
			ds=(DataSource)ctx.lookup("java:comp/env/jdbc/wb");
			conn = ds.getConnection();
		}  catch (SQLException e) {
			e.printStackTrace();
		} catch (NoInitialContextException e) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://124.17.88.241:4092/lili?characterEncoding=utf-8", "root", "123456");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} 
		return conn;
	}
    
	public static void close(ResultSet rs, Statement s, Connection c) {
		if(rs!=null)
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if(s!=null)
			try {
				s.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if(c!=null)
			try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public static void close(Statement s, Connection c) {
		if(s!=null)
			try {
				s.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if(c!=null)
			try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
    public static void close(Connection conn) {

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }

    }
}
