package DB;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import Mail.MailTitle;
import Mail.MailTemplate;
import Mail.MailReceiver;

public class MailDB {

	public static List<MailTitle> getTitleFromDB()
	{
		String sql = "select id,title from mail_title";
		Connection conn = DBCon.getCon();
		Statement stmt = null;
		ResultSet rs = null;
		List<MailTitle> qts = new ArrayList<MailTitle>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				MailTitle qt = new MailTitle(rs.getInt("id"),rs.getString("title"));
				qts.add(qt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(rs, stmt, conn);
		}
		return qts;
	}
	
	public static boolean insetTitleToDB(MailTitle t)
	{
		String sql = "insert into mail_title (title)" + "values (?)";
		Connection conn = DBCon.getCon();
		PreparedStatement ps = null;
		boolean result = false;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1,t.getTitle());
			
			result =  ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(ps, conn);
		}
		return result;
	}
	
	public synchronized void inserTitleToDB(List<MailTitle> ts){
		String sql ="insert into mail_title (title)" + "values (?)";
		Connection conn = DBCon.getCon();
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			for (MailTitle t : ts) {
				ps.setString(1, t.getTitle());
				ps.addBatch();
			}
			ps.executeBatch();			
			conn.commit();
			conn.setAutoCommit(true);
		}
		catch (BatchUpdateException e) {
			
		}
		catch (SQLException e) {
			for (MailTitle t : ts) {
				System.out.println(t.toString());
			}
			e.printStackTrace();
		} finally{
			DBCon.close(ps, conn);
		}
	}
	
	public static boolean insetTemplateToDB(MailTemplate t)
	{
		String sql = "insert into mail_template (content,mail_account_type)" + "values (?,?)";
		Connection conn = DBCon.getCon();
		PreparedStatement ps = null;
		boolean result = false;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1,t.getContent());
			ps.setInt(2, t.getType());
			
			result =  ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(ps, conn);
		}
		return result;
	}
	
	public synchronized void insertTemplateToDB(List<MailTemplate> ts){
		String sql = "insert into mail_template (content,mail_account_type)" + "values (?,?)";
		Connection conn = DBCon.getCon();
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			for (MailTemplate t : ts) {
				ps.setString(1, t.getContent());
				ps.setInt(2, t.getType());
				ps.addBatch();
			}
			ps.executeBatch();			
			conn.commit();
			conn.setAutoCommit(true);
		}
		catch (BatchUpdateException e) {
			
		}
		catch (SQLException e) {
			for (MailTemplate t : ts) {
				System.out.println(t.toString());
			}
			e.printStackTrace();
		} finally{
			DBCon.close(ps, conn);
		}
	}	
	
	public synchronized static List<MailTemplate> getTemplateFromDB(int type)
	{
		String sql = "select id,content,mail_account_type from mail_template where mail_account_type=" + type;
		Connection conn = DBCon.getCon();
		Statement stmt = null;
		ResultSet rs = null;
		List<MailTemplate> qts = new ArrayList<MailTemplate>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				MailTemplate qt = new MailTemplate(rs.getInt("id"),rs.getString("content"),rs.getInt("mail_account_type"));
				qts.add(qt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(rs, stmt, conn);
		}
		return qts;
	}

	public static boolean insetMailReceiverToDB(MailReceiver r)
	{
		String sql = "insert into mail_receiver (addr,gender,interest)" + "values (?,?,?)";
		Connection conn = DBCon.getCon();
		PreparedStatement ps = null;
		boolean result = false;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1,r.getAddr());
			ps.setInt(2, r.getGender());
			ps.setString(3, r.getInterest());
			
			result =  ps.execute();
		} catch (MySQLIntegrityConstraintViolationException e){
			
		}catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(ps, conn);
		}
		return result;
	}
	


}
