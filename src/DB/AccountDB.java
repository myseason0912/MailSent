package DB;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.BasicCookieStore;

import Account.Mail163Account;
import Account.MailAccount;
import Account.QQMailAccount;
import Account.SinaMailAccount;
import Account.AccountUtil;
import Account.LikeBuyMailAccount;

public class AccountDB {
	/*
	 * @根据用户的类型获取数据库用于发送邮件的用户
	 * @num 要获得的用户个数
	 */
	public  static List<MailAccount> getAccount(int num)
	{

		String sql = "select id,username,password,type,cookie,is_smtp  from mail_account_lili where status=0 "+ " limit " + num;
		Connection conn = DBCon.getCon();
		Statement stmt = null;
		ResultSet rs = null;
		List<MailAccount> ws = new ArrayList<MailAccount>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int type = rs.getInt("type");
				MailAccount ma = null;
				switch(type)
				{
					case AccountUtil.sina_account:
						SinaMailAccount sma = new SinaMailAccount(rs.getString("username"),rs.getString("password"),
																 type,AccountUtil.sina_smtp_host,AccountUtil.sina_pop3_host);
						ma = sma;
						break;
					case AccountUtil.mail163_account:
						Mail163Account m163 = new Mail163Account(rs.getString("username"),rs.getString("password"),
																type,AccountUtil.mail163_smtp_host,AccountUtil.mail163_pop3_host);
						ma = m163;
						break;
					case AccountUtil.qq_account:
						QQMailAccount qma = new QQMailAccount(rs.getString("username"),rs.getString("password"),
																type,AccountUtil.qq_smtp_host,AccountUtil.qq_pop3_host);
						ma = qma;
						break;
					case AccountUtil.like_account:
						LikeBuyMailAccount lma = new LikeBuyMailAccount(rs.getString("username"),rs.getString("password"),
																type,AccountUtil.like_smtp_host,AccountUtil.like_imap_host);
						ma = lma;
						break;
					default:
						break;
				}
				ma.setIsSmtp(rs.getInt("is_smtp"));
				Blob cookie_blob = rs.getBlob("cookie");
				if(cookie_blob!=null){
					ObjectInputStream ois;
					BasicCookieStore bcs = null;
					try {
						ois = new ObjectInputStream(cookie_blob.getBinaryStream());
						bcs = (BasicCookieStore)ois.readObject();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					ma.setCookies(bcs);
					ma.getClient().setCookieStore(ma.getCookies());
				}
				ws.add(ma);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(rs, stmt, conn);
		}
		return ws;
	}

	/*
	 * @根据用户的类型获取数据库用于发送邮件的用户
	 * @num 要获得的用户个数
	 * @type 要获得的用户类型
	 */
	public  static List<MailAccount> getAccount(int num,int type)
	{

		String sql = "select id,username,password,type,cookie,is_smtp  from mail_account_lili where status=0 and type=" + type + " limit " + num;
		Connection conn = DBCon.getCon();
		Statement stmt = null;
		ResultSet rs = null;
		List<MailAccount> ws = new ArrayList<MailAccount>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				MailAccount ma = null;
				switch(type)
				{
					case AccountUtil.sina_account:
						SinaMailAccount sma = new SinaMailAccount(rs.getString("username"),rs.getString("password"),
																type,AccountUtil.sina_smtp_host,AccountUtil.sina_pop3_host);
						ma = sma;
						break;
					case AccountUtil.mail163_account:
						Mail163Account m163 = new Mail163Account(rs.getString("username"),rs.getString("password"),
																type,AccountUtil.mail163_smtp_host,AccountUtil.mail163_pop3_host);
						ma = m163;
						break;
					case AccountUtil.qq_account:
						QQMailAccount qma = new QQMailAccount(rs.getString("username"),rs.getString("password"),
																type,AccountUtil.qq_smtp_host,AccountUtil.qq_pop3_host);
						ma = qma;
						break;
					case AccountUtil.like_account:
						LikeBuyMailAccount lma = new LikeBuyMailAccount(rs.getString("username"),rs.getString("password"),
																type,AccountUtil.like_smtp_host,AccountUtil.like_imap_host);
						ma = lma;
						break;
					default:
						break;
				}
				ma.setIsSmtp(rs.getInt("is_smtp"));
				Blob cookie_blob = rs.getBlob("cookie");
				if(cookie_blob!=null){
					ObjectInputStream ois;
					BasicCookieStore bcs = null;
					try {
						ois = new ObjectInputStream(cookie_blob.getBinaryStream());
						bcs = (BasicCookieStore)ois.readObject();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					ma.setCookies(bcs);
					ma.getClient().setCookieStore(ma.getCookies());
				}
				ws.add(ma);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(rs, stmt, conn);
		}
		return ws;
	}
	
	public static List<MailAccount> getAccount(int num,int type,int smtp)
	{
		String sql = "select id,username,password,type,cookie,is_smtp from mail_account_lili where status=0 and type="
					+ type  + " and is_smtp=" + smtp + " limit " + num;
		Connection conn = DBCon.getCon();
		Statement stmt = null;
		ResultSet rs = null;
		List<MailAccount> ws = new ArrayList<MailAccount>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				MailAccount ma = null;
				switch(type)
				{
					case AccountUtil.sina_account:
						SinaMailAccount sma = new SinaMailAccount(rs.getString("username"),rs.getString("password"),
																type,AccountUtil.sina_smtp_host,AccountUtil.sina_pop3_host);
						ma = sma;
						break;
					case AccountUtil.mail163_account:
						Mail163Account m163 = new Mail163Account(rs.getString("username"),rs.getString("password"),
																type,AccountUtil.mail163_smtp_host,AccountUtil.mail163_pop3_host);
						ma = m163;
						break;
					case AccountUtil.qq_account:
						QQMailAccount qma = new QQMailAccount(rs.getString("username"),rs.getString("password"),
																type,AccountUtil.qq_smtp_host,AccountUtil.qq_pop3_host);
						ma = qma;
						break;
					case AccountUtil.like_account:
						LikeBuyMailAccount lma = new LikeBuyMailAccount(rs.getString("username"),rs.getString("password"),
																type,AccountUtil.like_smtp_host,AccountUtil.like_imap_host);
						ma = lma;
						break;
					default:
						break;
				}
				ma.setIsSmtp(rs.getInt("is_smtp"));
				Blob cookie_blob = rs.getBlob("cookie");
				if(cookie_blob!=null){
					ObjectInputStream ois;
					BasicCookieStore bcs = null;
					try {
						ois = new ObjectInputStream(cookie_blob.getBinaryStream());
						bcs = (BasicCookieStore)ois.readObject();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					ma.setCookies(bcs);
					ma.getClient().setCookieStore(ma.getCookies());
				}
				ws.add(ma);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(rs, stmt, conn);
		}
		return ws;		
	}
	
	public static void updateCookie(MailAccount ac) 
	{
		String sql = "update mail_account_lili set is_login=?,cookie = ? where username=?";
		Connection conn = DBCon.getCon();
		PreparedStatement pstmt = null;
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, ac.getIsLogin());
			pstmt.setObject(2,ac.getClient().getCookieStore());
			pstmt.setString(3, ac.getUsername());
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBCon.close(pstmt, conn);
		}
	}
	
	public static void updateStatus(MailAccount ac)
	{
		String sql = "update mail_account_lili set status=? where username=?";
		Connection conn = DBCon.getCon();
		PreparedStatement pstmt = null;
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, ac.getStatus());
			pstmt.setString(2, ac.getUsername());
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBCon.close(pstmt, conn);
		}
	}
	
	public static void updateSmtp(MailAccount ac)
	{
		String sql = "update mail_account_lili set is_smtp=? where username=?";
		Connection conn = DBCon.getCon();
		PreparedStatement pstmt = null;
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, ac.getIsSmtp());
			pstmt.setString(2, ac.getUsername());
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBCon.close(pstmt, conn);
		}
	}
	
	public static boolean insetMailSenderToDB(MailAccount r)
	{

		String sql = "insert into mail_account_lili (username,password,type,is_smtp)" + "values (?,?,?,?)";
		Connection conn = DBCon.getCon();
		PreparedStatement ps = null;
		boolean result = false;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1,r.getUsername());
			ps.setString(2, r.getPassword());
			ps.setInt(3, r.getType());
			ps.setInt(4, r.getIsSmtp());
			
			result =  ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(ps, conn);
		}
		return result;
	}
}
