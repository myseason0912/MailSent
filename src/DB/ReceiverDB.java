package DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Mail.MailReceiver;

public class ReceiverDB {
	
	public static List<MailReceiver> getQQReceiverFromDB(int total,int num,int gender)
	{
		String sql = "select uid from qq_user where is_reject = 0 and gender=" + gender + " limit " + total + "," + num ;
		Connection conn = DBCon.getCon();
		Statement stmt = null;
		ResultSet rs = null;
		List<MailReceiver> res = new  ArrayList<MailReceiver>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				MailReceiver r = new MailReceiver();
				r.setAddr(rs.getString("uid") + "@qq.com" );
				res.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(rs, stmt, conn);
		}
		return res;
	}
	
	public static List<MailReceiver> getOtherReceiverFromDB(int total,int num)
	{
		String sql = "select addr from mail_receiver where is_reject = 0 " + " limit " + total + "," + num ;
		Connection conn = DBCon.getCon();
		Statement stmt = null;
		ResultSet rs = null;
		List<MailReceiver> res = new  ArrayList<MailReceiver>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				MailReceiver r = new MailReceiver();
				r.setAddr(rs.getString("addr"));
				res.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(rs, stmt, conn);
		}
		return res;
	}

	
	public static List<MailReceiver> getTestAccountFromDB( )
	{
		String sql = "select uid from test_mail ";
		Connection conn = DBCon.getCon();
		Statement stmt = null;
		ResultSet rs = null;
		List<MailReceiver> res = new  ArrayList<MailReceiver>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				MailReceiver r = new MailReceiver();
				r.setAddr(rs.getString("uid"));
				res.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(rs, stmt, conn);
		}
		return res;
	}
	
}
