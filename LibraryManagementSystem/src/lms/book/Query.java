package lms.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Query {
	
	public static PreparedStatement pstmt;
	public static ResultSet rs;
	public static ResultSet Query(Connection conn, int barcode) {
		// TODO Auto-generated constructor stub
		StringBuffer sb = new StringBuffer();
		sb.append("select b.book_id, b.genre, b.book_name, b.publisher, b.writer, b.book_same_amount");
		sb.append(" from lib_book b, lib_book_detail d");
		sb.append(" where d.barcode="+barcode);
		sb.append(" and d.book_id=b.book_id");
		try {
			pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
}
