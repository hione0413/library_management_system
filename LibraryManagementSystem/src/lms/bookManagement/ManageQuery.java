package lms.bookManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.apache.poi.ss.formula.ptg.AddPtg;

public class ManageQuery {
	public static PreparedStatement pstmt;
	public static ResultSet rs;
	private static int lastIndex;

	public static boolean isExistPublisher(Connection conn, String name) {
		String sql = "select publisher_id, publisher from lib_publisher where publisher='" + name + "'";
		boolean b = false;
		try {
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			rs.last();
			if (rs.getRow() == 0)
				b = false;
			else
				b = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}
	public static int getLastPublisher(Connection conn) {
		String sql = "select publisher_id from lib_publisher order by publisher_id";
		try {
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			rs.last();
			lastIndex = rs.getInt("publisher_id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lastIndex;
	}
	public static void addNewPublisher(Connection conn, String pub_name) {
		int newPublisherId = getLastPublisher(conn)+1;
		String sql = "insert into lib_publisher(publisher_id, publisher) values("+newPublisherId+", '"+pub_name+"')";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int getPublisherId(Connection conn, String name) {
		int num = 0;
		String sql = "select publisher_id from lib_publisher where publisher='" + name + "'";
		try {
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			rs.first();
			num = rs.getInt("publisher_id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;
	}

	public static int getLastBookNumber(Connection conn) {
		String sql = "select book_id from lib_book order by book_id";
		try {
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			rs.last();
			if(rs.getRow()==0) {
				lastIndex = 0;
			} else {
				lastIndex = rs.getInt("book_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lastIndex;
	}

	public static ResultSet searchBook(Connection conn, String name) {
		StringBuffer sb = new StringBuffer();
		sb.append("select b.book_id, g.genre, b.book_name, p.publisher, b.writer, b.book_same_amount, d.book_regist_date, s.book_state");
		sb.append(" from lib_book b, lib_genre g, lib_publisher p, lib_book_detail d, lib_book_state s");
		sb.append(" where b.book_name like '%" + name + "%'");
		sb.append(" and b.genre=g.genre_id"); // 분류
		sb.append(" and b.publisher=p.publisher_id"); // 출판사
		sb.append(" and b.book_id=d.book_id"); // 도서 등록일
		sb.append(" and d.book_state=s.book_state_id"); // 도서 상태

		try {
			pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public static ResultSet getBookDetail(Connection conn, String book_id) {
		int id = Integer.parseInt(book_id);
		StringBuffer sb = new StringBuffer();
		System.out.println(id);
		sb.append("select d.book_regist_date, r.rental_state, s.book_state");
		sb.append(" from lib_book_detail d, lib_rental_state r, lib_book_state s");
		sb.append(" where d.book_id=" + id);
		sb.append(" and d.rental_state=r.rental_state_id");
		sb.append(" and d.book_state=s.book_state_id");
		try {
			pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public static String searchWhoRent(Connection conn, int book_id) {
		String name = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select m.mem_name from lib_rental_table r, lib_member m");
		sb.append(" where r.book_id=" + book_id);
		sb.append(" and r.member_id=m.mem_id");
		try {
			pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			rs.first();
			if (rs.next()) {
				name = rs.getString("mem_name");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}

	public static ResultSet showRentableBooks(Connection conn) {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"select b.book_id, g.genre, b.book_name, p.publisher, b.writer, b.book_same_amount, d.book_regist_date, s.book_state");
		sb.append(" from lib_book b, lib_genre g, lib_publisher p, lib_book_detail d, lib_book_state s");
		sb.append(" where b.genre=g.genre_id"); // 분류
		sb.append(" and b.publisher=p.publisher_id"); // 출판사
		sb.append(" and b.book_id=d.book_id"); // 도서 등록일
		sb.append(" and d.book_state=s.book_state_id"); // 도서 상태
		return rs;
	}

	public static ResultSet showAllBooks(Connection conn, int table_field, int tab_field) {
		StringBuffer sb = new StringBuffer();
		sb.append("select b.book_id, g.genre, b.book_name, p.publisher, b.writer, b.book_same_amount, d.book_regist_date, s.book_state");
		sb.append(" from lib_book b, lib_genre g, lib_publisher p, lib_book_detail d, lib_book_state s");
		sb.append(" where( b.genre=g.genre_id"); // 분류
		sb.append(" and b.publisher=p.publisher_id"); // 출판사
		sb.append(" and b.book_id=d.book_id"); // 도서 등록일
		sb.append(" and d.book_state=s.book_state_id"); // 도서 상태
		if (tab_field == BookManagement.LOST_BOOK_TAB) {
			sb.append(" and s.book_state_id in(2,3)"); // 도서 상태
		}else if(tab_field==BookManagement.BOOK_LIST_TAB) {
			sb.append(" and s.book_state_id=1");
		}
		if (table_field == BookList.SHOW_RENTALABLE_BOOKS) {
			sb.append(" and d.rental_state=1");
		}
		else if (table_field == BookList.SHOW_RENTALED_BOOKS) {
			sb.append(" and d.rental_state in(2,3)");
		} else if(table_field ==BookList.SHOW_ALL_BOOKS) {
			sb.append(" and d.rental_state in(1,2,3)");
		}
		sb.append(") order by book_id asc");
		try {
			System.out.println(sb.toString());
			pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public static void registBook(Connection conn, String[] data) {
		int newBookId = getLastBookNumber(conn)+1;
		System.out.println(newBookId);
		StringBuffer sb = new StringBuffer();
		sb.append("insert into lib_book(book_id, genre, book_name, publisher, writer, book_same_amount)");
		sb.append(" values("+newBookId+", "+data[0]+", '"+data[1]+"', "+data[2]+", '"+data[3]+"', "+data[4]+")");
		System.out.println(sb.toString());
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		registBookDetail(conn, newBookId);
	}
	
	private static long createBarcode() {
		Calendar cal = Calendar.getInstance(Locale.KOREA);
		long barcode = cal.getTimeInMillis();
		
		return barcode;
	}
	
	private static void registBookDetail(Connection conn, int book_id) {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into lib_book_detail(barcode, book_id, book_state, book_regist_date, rental_state)");
		sb.append(" values("+createBarcode()+", "+book_id+", 1, default, 1)");
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void bookEdit(Connection conn, String[] data, int book_id) {
		StringBuffer sb = new StringBuffer();
		if(!isExistPublisher(conn, data[2])) {
			addNewPublisher(conn, data[2]);
		}
		sb.append("update lib_book set genre = " + data[0] + "");
		sb.append(" , book_name = '" + data[1] + "'");
		sb.append(" , publisher=" + getPublisherId(conn, data[2]) + "");
		sb.append(" , writer = '" + data[3] + "'");
		sb.append(" , book_same_amount=" + data[4] + "");
		sb.append(" where book_id=" + book_id);
		try {
			pstmt = conn.prepareStatement(sb.toString());
			int result = pstmt.executeUpdate();
			if (result > 0) {
				JOptionPane.showMessageDialog(null, "수정완료");
			} else {
				JOptionPane.showMessageDialog(null, "수정실패");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void detailEdit(Connection conn, String[] data, int book_id, int rental_type) {
		StringBuffer sb = new StringBuffer();
		sb.append("update lib_book_detail set book_state= " + Integer.parseInt(data[0]) + "");
		sb.append(" , book_regist_date = ?");
		if(rental_type!=0) {
			sb.append(", rental_state="+rental_type);
		}
		sb.append(" where book_id =" + book_id);
		
		try {

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, data[1]);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static ResultSet showRequestBooks(Connection conn) {
		StringBuffer sb = new StringBuffer();
		sb.append("select r.req_id,g.genre,r.book_name,r.publisher,r.writer,r.request_date,r.pay");
		sb.append(",m.mem_id,rs.request_state");

		sb.append(" from lib_book_request r, lib_genre g, lib_member m,lib_request_state rs"); // 출판사
		sb.append(" where g.genre_id = r.genre"); // 도서 등록일
		sb.append(" and m.mem_id = r.member_id"); // 도서 상태
		sb.append(" and rs.request_state_id = 1"); // 도서 상태
		System.out.println(sb.toString());
		try {
			pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public static ResultSet getRequestDetail(Connection conn, String book_id) {
		int id = Integer.parseInt(book_id);
		StringBuffer sb = new StringBuffer();
		// System.out.println(id);
		sb.append("select r.req_id,g.genre,r.book_name,r.publisher,r.writer,r.request_date,r.pay");
		sb.append(",m.mem_id,rs.request_state");
		sb.append(" from lib_book_request r, lib_genre g, lib_member m,lib_request_state rs"); // 출판사
		sb.append(" where g.genre_id = r.genre"); // 도서 등록일
		sb.append(" and m.mem_id = r.member_id"); // 도서 상태
		sb.append(" and rs.request_state_id = 1"); // 도서 상태
		try {
			pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public static int getBookAmount(Connection conn, String str) {
		int num = 0;
		try {
			pstmt = conn.prepareStatement(str, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			rs.last();
			num = rs.getRow();
			if (rs.getRow() > 0) {
				num = rs.getRow();
			} else {
				num = 1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return num;
	}

	public static void requestEdit(Connection conn, String[] str, int request_id) {
		int result = 0;
		int result2 = 0;
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		StringBuffer sb3 = new StringBuffer();
		PreparedStatement pstmt2 = null;

		sb2.append("select b.book_name from lib_book b, lib_book_request br where b.book_name = br.book_name");

		sb.append("insert into lib_book(book_id,genre,book_name,publisher,writer,book_same_amount)");
		sb.append("values(seq_book_id.nextval," + Integer.parseInt(str[4]) + ",'" + str[1] + "',"
				+ getPublisherId(conn, str[2]) + ",'" + str[3] + "'," + getBookAmount(conn, sb2.toString()) + ")");

		sb3.append("insert into lib_book_detail(barcode,book_id,book_state,book_regist_date,rental_state)");
		sb3.append("values(seq_barcode.nextval,?" + ",1,?,1)");

		try {

			pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pstmt2 = conn.prepareStatement(sb3.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			if (Integer.parseInt(str[5]) == 2) {
				// pstmt.setInt(1, Integer.parseInt(str[4]));
				// pstmt.setString(2, str[1]);
				// pstmt.setInt(3, getPublisherId(conn, str[2]));
				// pstmt.setString(4, str[3]);
				// pstmt.setInt(4, getBookAmount(conn, sb2.toString()));
				result = pstmt.executeUpdate();
				pstmt2.setInt(1, getBookId(conn));
				pstmt2.setString(2, CurrentDay.getCurrentDate());
				result2 = pstmt2.executeUpdate();
			} else {
				JOptionPane.showMessageDialog(null, "요청 상태를 확인해주세요");
			}
			// int result = pstmt.executeUpdate();

			if (result > 0 && result2 > 0) {
				JOptionPane.showMessageDialog(null, "수정완료");
			} else {
				JOptionPane.showMessageDialog(null, "수정실패");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public static int getBookId(Connection conn) {
		int num = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("select book_id from lib_book");
		try {
			pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			rs.last();
			num = rs.getInt(1);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return num;
	}
}
