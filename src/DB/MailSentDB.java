package DB;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import Account.MailAccount;
import Mail.MailReceiver;
import Mail.MailTitle;
import Mail.MailTemplate;


public class MailSentDB {
	
	public static boolean insertSentRecord(MailAccount ma,MailReceiver mr,MailTitle title,MailTemplate temp)
	{
		String sql = "insert into mail_sent(sent_addr,receive_addr,"
					+ "title_id,template_id,sent_date)" + "values(?,?,?,?,?)";
		Connection conn = DBCon.getCon();
		PreparedStatement ps = null;
		boolean result = false;
		try {
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, ma.getUsername());
			ps.setString(2, mr.getAddr());
			ps.setInt(3,title.getId());
			ps.setInt(4, temp.getId());
			ps.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
			
			result =  ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(ps, conn);
		}
		return result;
		
	}
	
	public static void insertSentRecord(MailAccount ma,List<MailReceiver> mrs,MailTitle title,MailTemplate temp)
	{
		String sql = "insert into mail_sent(sent_addr,receive_addr,"
			+ "title_id,template_id,sent_date)" + "values(?,?,?,?,?)";
		Connection conn = DBCon.getCon();
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			for (MailReceiver mr : mrs) {
				ps.setString(1, ma.getUsername());
				ps.setString(2, mr.getAddr());
				ps.setInt(3, title.getId());
				ps.setInt(4, temp.getId());
				ps.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
				
				ps.addBatch();
			}
			ps.executeBatch();			
			conn.commit();
			conn.setAutoCommit(true);
		}
		catch (BatchUpdateException e) {
		
		}
		catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(ps, conn);
		}
	}
}
