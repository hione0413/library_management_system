package lms.book;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Return extends JPanel {
	JButton bt_loan, bt_Re;
	JPanel p_north, p_west, p_east;
	JLabel la_lib, la_rental;
	JTable table_lib, table_rental;

	LibTableModel model_lib;
	RentalTableModel model_rental;
	JScrollPane scroll_lib, scroll_rental;
	JButton bt_enter;
	BookMain bookMain;

	// 오른쪽(east)정리
	JLabel la_member2, member2_id, member2_name, member2_state, member2_birth, member2_phone, member2_addr,
			member2_email, member2_regist, member2_available;
	JLabel la_lib2, lib2_id, lib2_bar, lib2_genre, lib2_name, lib2_pub, lib2_writer, lib2_count;
	// 텍스트필드 주기!!!!
	JTextField t_member2_id, t_member2_name, t_member2_state, t_member2_birth, t_member2_phone, t_member2_addr,
			t_member2_email, t_member2_available;
	JTextField t_lib2_id, t_lib2_bar, t_lib2_genre, t_lib2_name, t_lib2_pub, t_lib2_writer, t_lib2_count;

	Calendar cal = Calendar.getInstance();
	Calendar cal2 = Calendar.getInstance();
	String strDate, strDate2;
	DateFormat df;

	public Return(BookMain bookMain) {
		this.bookMain = bookMain;
		cal.setTime(new Date());
		cal2.setTime(new Date());
		cal2.add(Calendar.DATE, 14);
		df = new SimpleDateFormat("yyyy-MM-dd");
		strDate = df.format(cal.getTime());
		strDate2 = df.format(cal2.getTime());

		// p_north = new JPanel();
		// p_center = new JPanel();
		// p_south = new JPanel();
		p_west = new JPanel();
		p_east = new JPanel();

		// north 부분
		// p_north.setPreferredSize(new Dimension(700, 45));
		// bt_loan = new JButton("반납");
		// bt_loan.setPreferredSize(new Dimension(100, 35));
		// bt_loan.setFont(new Font("HY엽서L", Font.BOLD, 25));

		// bt_loan.setContentAreaFilled(false);
		// bt_Re = new JButton("반납");
		// bt_Re.setPreferredSize(new Dimension(100, 35));
		// bt_Re.setFont(new Font("HY엽서L", Font.BOLD, 25));
		// bt_Re.setContentAreaFilled(false);

		// west 부분
		p_west.setPreferredSize(new Dimension(700, 400));
		la_lib = new JLabel("반납 도서 목록");
		la_lib.setBounds(295, 247, 108, 18);
		la_lib.setFont(new Font("HY엽서L", Font.BOLD, 15));
		table_lib = new JTable();
		la_rental = new JLabel("대출중인 도서");
		la_rental.setBounds(295, 10, 108, 18);
		la_rental.setFont(new Font("HY엽서L", Font.BOLD, 15));
		table_rental = new JTable();
		scroll_lib = new JScrollPane(table_lib);
		scroll_lib.setBounds(0, 274, 700, 200);
		scroll_lib.setPreferredSize(new Dimension(320, 250));
		scroll_rental = new JScrollPane(table_rental);
		scroll_rental.setBounds(0, 35, 700, 200);
		scroll_rental.setPreferredSize(new Dimension(320, 250));

		// east 부분
		p_east.setPreferredSize(new Dimension(400, 600));
		la_member2 = new JLabel("회원 정보");
		la_member2.setBounds(20, 5, 70, 47);
		la_member2.setFont(new Font("HY엽서L", Font.BOLD, 15));
		la_member2.setPreferredSize(new Dimension(350, 47));
		member2_id = new JLabel("회원ID");
		member2_id.setBounds(20, 57, 60, 27);
		member2_id.setFont(new Font("HY엽서L", Font.PLAIN, 13));
		member2_id.setPreferredSize(new Dimension(60, 27));
		member2_name = new JLabel("회원이름");
		member2_name.setBounds(199, 58, 60, 27);
		member2_name.setFont(new Font("HY엽서L", Font.PLAIN, 13));
		member2_name.setPreferredSize(new Dimension(60, 27));
		member2_state = new JLabel("회원상태");
		member2_state.setBounds(20, 90, 60, 27);
		member2_state.setFont(new Font("HY엽서L", Font.PLAIN, 13));
		member2_state.setPreferredSize(new Dimension(60, 27));
		member2_birth = new JLabel("생년월일");
		member2_birth.setBounds(20, 123, 70, 27);
		member2_birth.setFont(new Font("HY엽서L", Font.PLAIN, 13));
		member2_phone = new JLabel("연락처");
		member2_phone.setBounds(199, 90, 60, 27);
		member2_phone.setFont(new Font("HY엽서L", Font.PLAIN, 13));
		member2_phone.setPreferredSize(new Dimension(70, 27));
		member2_addr = new JLabel("주소");
		member2_addr.setBounds(20, 152, 60, 27);
		member2_addr.setFont(new Font("HY엽서L", Font.PLAIN, 13));
		member2_addr.setPreferredSize(new Dimension(60, 27));
		member2_email = new JLabel("e-mail");
		member2_email.setBounds(190, 123, 50, 27);
		member2_email.setFont(new Font("HY엽서L", Font.PLAIN, 13));
		member2_email.setPreferredSize(new Dimension(80, 27));
		member2_regist = new JLabel("반납/가능권수");
		member2_regist.setBounds(20, 184, 91, 27);
		member2_regist.setFont(new Font("HY엽서L", Font.PLAIN, 13));
		member2_regist.setPreferredSize(new Dimension(60, 27));
		member2_available = new JLabel("/ 5");
		member2_available.setBounds(137, 184, 34, 27);
		member2_available.setFont(new Font("HY엽서L", Font.PLAIN, 13));
		member2_available.setPreferredSize(new Dimension(60, 27));

		t_member2_id = new JTextField();
		t_member2_id.setBounds(85, 60, 100, 21);
		t_member2_id.setEnabled(false);
		t_member2_name = new JTextField();
		t_member2_name.setBounds(266, 60, 100, 21);
		t_member2_name.setEnabled(false);
		t_member2_state = new JTextField();
		t_member2_state.setBounds(85, 92, 100, 21);
		t_member2_state.setEnabled(false);
		t_member2_birth = new JTextField();
		t_member2_birth.setBounds(85, 125, 91, 21);
		t_member2_birth.setEnabled(false);
		t_member2_phone = new JTextField();
		t_member2_phone.setBounds(266, 93, 100, 21);
		t_member2_phone.setEnabled(false);
		t_member2_addr = new JTextField();
		t_member2_addr.setBounds(85, 155, 281, 21);
		t_member2_addr.setEnabled(false);
		t_member2_email = new JTextField();
		t_member2_email.setBounds(242, 125, 124, 21);
		t_member2_email.setEnabled(false);
		t_member2_available = new JTextField();
		t_member2_available.setBounds(115, 188, 20, 21);
		t_member2_available.setEnabled(false);

		la_lib2 = new JLabel("도서정보");
		la_lib2.setBounds(20, 261, 350, 50);
		la_lib2.setFont(new Font("HY엽서L", Font.BOLD, 15));
		la_lib2.setPreferredSize(new Dimension(350, 50));
		lib2_bar = new JLabel("바코드");
		lib2_bar.setBounds(20, 312, 60, 27);
		lib2_bar.setFont(new Font("HY엽서L", Font.PLAIN, 13));
		lib2_bar.setPreferredSize(new Dimension(60, 27));
		lib2_genre = new JLabel("도서장르");
		lib2_genre.setBounds(200, 312, 60, 27);
		lib2_genre.setFont(new Font("HY엽서L", Font.PLAIN, 13));
		lib2_genre.setPreferredSize(new Dimension(60, 27));
		lib2_id = new JLabel("도서번호");
		lib2_id.setBounds(20, 344, 60, 27);
		lib2_id.setFont(new Font("HY엽서L", Font.PLAIN, 13));
		lib2_id.setPreferredSize(new Dimension(60, 27));
		lib2_name = new JLabel("도서명");
		lib2_name.setBounds(199, 344, 55, 27);
		lib2_name.setFont(new Font("HY엽서L", Font.PLAIN, 13));
		lib2_name.setPreferredSize(new Dimension(55, 27));
		lib2_pub = new JLabel("출판사");
		lib2_pub.setBounds(20, 376, 55, 27);
		lib2_pub.setFont(new Font("HY엽서L", Font.PLAIN, 13));
		lib2_pub.setPreferredSize(new Dimension(55, 27));
		lib2_writer = new JLabel("저자명");
		lib2_writer.setBounds(20, 408, 50, 27);
		lib2_writer.setFont(new Font("HY엽서L", Font.PLAIN, 13));
		lib2_writer.setPreferredSize(new Dimension(50, 27));
		lib2_count = new JLabel("반납일");
		lib2_count.setBounds(207, 412, 60, 21);
		lib2_count.setFont(new Font("HY엽서L", Font.PLAIN, 13));
		lib2_count.setPreferredSize(new Dimension(90, 27));

		t_lib2_bar = new JTextField();
		t_lib2_bar.setBounds(85, 315, 100, 21);
		t_lib2_genre = new JTextField();
		t_lib2_genre.setBounds(263, 315, 103, 21);
		t_lib2_genre.setEnabled(false);
		t_lib2_id = new JTextField();
		t_lib2_id.setBounds(85, 347, 100, 21);
		t_lib2_id.setEnabled(false);
		t_lib2_name = new JTextField();
		t_lib2_name.setBounds(263, 347, 103, 21);
		t_lib2_name.setEnabled(false);
		t_lib2_pub = new JTextField();
		t_lib2_pub.setBounds(85, 379, 281, 21);
		t_lib2_pub.setEnabled(false);
		t_lib2_writer = new JTextField();
		t_lib2_writer.setBounds(85, 411, 108, 21);
		t_lib2_writer.setEnabled(false);
		t_lib2_count = new JTextField();
		t_lib2_count.setBounds(266, 412, 100, 21);
		t_lib2_count.setEnabled(false);

		bt_enter = new JButton("반납");
		bt_enter.setBounds(145, 444, 100, 50);
		// bt_enter.setBounds(1000, 450, 100, 50);
		bt_enter.setPreferredSize(new Dimension(100, 50));
		bt_enter.setFont(new Font("HY엽서L", Font.BOLD, 20));
		bt_enter.setContentAreaFilled(false); // 버튼 배경 투명
		p_west.setLayout(null);
		p_west.add(la_lib);
		p_west.add(scroll_lib);
		p_west.add(la_rental);
		p_west.add(scroll_rental);
		p_east.setLayout(null);

		p_east.add(la_member2);
		p_east.add(t_member2_id);
		p_east.add(member2_id);
		p_east.add(t_member2_id);
		p_east.add(member2_name);
		p_east.add(t_member2_name);
		p_east.add(member2_state);
		p_east.add(t_member2_state);
		p_east.add(member2_phone);
		p_east.add(t_member2_phone);
		p_east.add(member2_birth);
		p_east.add(t_member2_birth);
		p_east.add(member2_addr);
		p_east.add(t_member2_addr);
		p_east.add(member2_email);
		p_east.add(t_member2_email);
		p_east.add(member2_regist);
		p_east.add(t_member2_available);
		p_east.add(member2_available);

		p_east.add(la_lib2);
		p_east.add(lib2_bar);
		p_east.add(t_lib2_bar);
		p_east.add(lib2_genre);
		p_east.add(t_lib2_genre);
		p_east.add(lib2_id);
		p_east.add(t_lib2_id);
		p_east.add(lib2_name);
		p_east.add(t_lib2_name);
		p_east.add(lib2_pub);
		p_east.add(t_lib2_pub);
		p_east.add(lib2_writer);
		p_east.add(t_lib2_writer);
		p_east.add(lib2_count);
		p_east.add(t_lib2_count);
		p_east.add(bt_enter);

		this.setLayout(new BorderLayout());
		// add(p_north, BorderLayout.NORTH);
		add(p_east, BorderLayout.EAST);

		add(p_west, BorderLayout.CENTER);
		// add(bt_enter, BorderLayout.EAST);
		// add(p_south, BorderLayout.SOUTH);

		// 테이블과 모델 연결!!
		model_lib = new LibTableModel();
		table_lib.setModel(model_lib);
		model_rental = new RentalTableModel();
		table_rental.setModel(model_rental);

		getLib();

		// 반납버튼 리스너
		bt_enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String state = t_member2_state.getText();

				// 도서를 반납한 회원이 연체회원일 경우 정상 회원으로 되돌리기
				if (state.equals("연체 회원")) {
					return_sameAmount();
					return_rentalTable();
					return_memState();

				} else {
					return_sameAmount();
					return_rentalTable();
					JOptionPane.showMessageDialog(null, "반납되었습니다.");
				}

			}
		});

		t_lib2_bar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String bar = t_lib2_bar.getText();

				getDetail(bar);

			}
		});

		setPreferredSize(new Dimension(1150, 500));

	}

	// 바코드 입력시, 해당 도서 빌려간 회원정보와 도서정보 텍스트필드에 출력
	public void getDetail(String bar) {
		Connection con = bookMain.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuffer sb = new StringBuffer();
		sb.append("select b.book_id, b.genre, b.book_name, b.publisher, b.writer");
		sb.append(", m.mem_id, m.mem_name, m.mem_state, m.mem_phone, m.mem_birth, m.mem_email, m.mem_addr");
		sb.append(" from lib_book b, lib_book_Detail d, lib_member m, lib_rental_Table r");
		sb.append(" where d.barcode = '" + bar
				+ "' and b.book_id = d.book_id and b.book_id=r.book_id and r.member_id = m.mem_id");

		try {
			pstmt = con.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();

			rs.beforeFirst();
			rs.next();

			// 회원 정보
			String mem_id = rs.getString("mem_id");
			String mem_name = rs.getString("mem_name");
			String mem_state = rs.getString("mem_state");
			String mem_phone = rs.getString("mem_phone");
			String mem_birth = rs.getString("mem_birth");
			String mem_email = rs.getString("mem_email");
			String mem_addr = rs.getString("mem_addr");

			// 도서 정보
			int book_id = rs.getInt("book_id");
			int genre = rs.getInt("genre");
			String book_name = rs.getString("book_name");
			int publisher = rs.getInt("publisher");
			String writer = rs.getString("writer");

			String txt_genre = "";
			String txt_state = "";

			if (genre == 1) {
				txt_genre = "교육";
			} else if (genre == 2) {
				txt_genre = "소설";
			} else if (genre == 3) {
				txt_genre = "에세이";
			}

			if (mem_state.equals("1")) {
				txt_state = "정상 회원";
			} else if (mem_state.equals("2")) {
				txt_state = "연체 회원";
			} else if (mem_state.equals("3")) {
				txt_state = "정지 회원";
			} else if (mem_state.equals("4")) {
				txt_state = "삭제 대기 회원";
			} else if (mem_state.equals("5")) {
				txt_state = "관리자";
			}

			t_member2_id.setText(mem_id);
			t_member2_name.setText(mem_name);
			t_member2_state.setText(txt_state);
			t_member2_phone.setText(mem_phone);
			t_member2_birth.setText(mem_birth);
			t_member2_email.setText(mem_email);
			t_member2_addr.setText(mem_addr);

			t_lib2_id.setText(Integer.toString(book_id));
			t_lib2_name.setText(book_name);
			t_lib2_pub.setText(Integer.toString(publisher));
			t_lib2_writer.setText(writer);
			t_lib2_count.setText(strDate2);
			t_lib2_genre.setText(txt_genre);

			getRentalLib(mem_id);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 반납 버튼 누르면 반납 할 도서목록테이블에 데이터 가져오기
	public void getLib() {
		String book_id = t_lib2_id.getText();

		Connection con = bookMain.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select b.book_id,b.genre, b.book_name, b.publisher, b.writer, b.book_same_amount");
		sb.append(" from lib_rental_Table r, lib_book b");
		sb.append(" where r.book_id ='" + book_id + "'");

		try {
			pstmt = con.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			rs = pstmt.executeQuery();
			rs.last();
			int total = rs.getRow();
			rs.beforeFirst();

			Object[][] data_lib = new Object[total][model_lib.columnTitle.length];
			for (int i = 0; i < total; i++) {
				rs.next();
				data_lib[i][0] = rs.getInt("book_id");
				data_lib[i][1] = rs.getInt("genre");
				data_lib[i][2] = rs.getString("book_name");
				data_lib[i][3] = rs.getInt("publisher");
				data_lib[i][4] = rs.getString("writer");
				data_lib[i][5] = rs.getInt("book_same_amount");
				// System.out.println("D");

			}
			model_lib.data_lib = data_lib;
			// System.out.println(model_lib.data_lib[0][0]);
			table_lib.updateUI();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return_sameAmount();
	}

	// 바코드 입력시, 회원의 대출중인 도서 목록 테이블 출력
	public void getRentalLib(String mem_id) {
		// String mem_id = t_member2_id.getText();

		Connection con = bookMain.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select b.book_id, b.book_name, b.publisher, b.writer");
		sb.append(" from lib_member m, lib_rental_Table r, lib_book b");
		sb.append(" where '" + mem_id + "'= r.member_id");
		sb.append(" and r.book_id = b.book_id");

		try {
			pstmt = con.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();

			rs.last();
			int total = rs.getRow();
			rs.beforeFirst();

			Object[][] data_rental = new Object[total][model_rental.columnTitle.length];
			for (int i = 0; i < total; i++) {
				rs.next();
				data_rental[i][0] = rs.getInt("book_id");
				data_rental[i][1] = rs.getString("book_name");
				data_rental[i][2] = rs.getInt("publisher");
				data_rental[i][3] = rs.getString("writer");

			}
			model_rental.data_rental = data_rental;
			System.out.println(table_rental.getRowCount());
			int available = 5 - table_rental.getRowCount();
			t_member2_available.setText(Integer.toString(available));
			table_rental.updateUI();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 반납메서드 - 동일 도서 개수 연산
	public void return_sameAmount() {
		Connection con = bookMain.getCon();
		Statement stmt = null;
		ResultSet rs = null;
		String libId;
		libId = t_lib2_id.getText();
		String sql = "update lib_book set book_same_amount =(book_same_amount+1) where book_id ='" + libId + "'";
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 반납메서드 - 반납목록 테이블에서 delete
	public void return_rentalTable() {
		String mem_id = t_member2_id.getText();
		String book_id = t_lib2_id.getText();
		Connection con = bookMain.getCon();
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "delete from lib_rental_Table where mem_id = '" + mem_id + "' and book_id = '" + book_id + "'";

		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		table_rental.repaint();
		t_member2_available.repaint();
	}

	// 반납메서드 - 정상 회원으로 변경(연체 회원일 경우만 수행!!!)
	public void return_memState() {
		String mem_id = t_member2_id.getText();
		Connection con = bookMain.getCon();
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "update lib_member set mem_state=1 from lib_member where mem_id = '" + mem_id + "'";

		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
			int mem_state = rs.getInt("mem_state");
			if (mem_state == 1) {
				String state = "정상 회원";
				t_member2_state.setText(state);
				t_member2_state.repaint();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "정상회원으로 전환되었습니다.");

	}
}