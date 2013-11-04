package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import Mail.SentTask;
public class SentTaskDB {

	public static List<SentTask> getSentTaskFromDB()
	{
		String sql = "select title_index,template_index,hidden_index,user_index,type from mail_sent_task " ;
		Connection conn = DBCon.getCon();
		Statement stmt = null;
		ResultSet rs = null;
		List<SentTask> sts = new ArrayList<SentTask>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				SentTask st = new SentTask();
				st.setHiddenIndex(rs.getInt("hidden_index"));
				st.setTemplateIndex(rs.getInt("template_index"));
				st.setTitelIndex(rs.getInt("title_index"));
				st.setUserIndex(rs.getInt("user_index"));
				st.setType(rs.getInt("type"));
				sts.add(st);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(rs, stmt, conn);
		}
		return sts;
	}
	
	public static int getIndexFromDB(String indexType,int type)
	{
		String sql = "select " + indexType + " from mail_sent_task where type=" + type ;
		Connection conn = DBCon.getCon();
		Statement stmt = null;
		ResultSet rs = null;
		int index = 0;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				index = rs.getInt(indexType);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(rs, stmt, conn);
		}
		return index;
	}
	
	public static int updateIndex(String indexType,int value,int type)
	{
		String sql = "update mail_sent_task set " + indexType + "=" + indexType + "+"
					 + value + " where type=" + type ;
		Connection conn = DBCon.getCon();
		PreparedStatement pstmt = null;
		int index = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(pstmt, conn);
		}
		return index;
	}
	
	public static int setIndex(String indexType,long value,int type)
	{
		String sql = "update mail_sent_task set " + indexType + "=" + value + " where type=" + type ;
		Connection conn = DBCon.getCon();
		PreparedStatement pstmt = null;
		int index = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBCon.close(pstmt, conn);
		}
		return index;
	}
	
}
