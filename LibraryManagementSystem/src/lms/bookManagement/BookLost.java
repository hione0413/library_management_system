package lms.bookManagement;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import lms.MainFrame;

public class BookLost extends JPanel {
	public static final int SHOW_ALL_BOOKS = 0;
	public static final int SHOW_RENTALABLE_BOOKS = 1;
	public static final int SHOW_RENTALED_BOOKS = 2;
	JPanel p_west;
	JPanel p_west_north;
	JButton bt_all;
	JButton bt_jurisdiction;
	JButton bt_rental;

	JPanel p_west_center;
	JTable table;
	JScrollPane scroll;

	JPanel p_east;
	JPanel p_east_north;
	JTextField t_search;
	JButton bt_search;
	JPanel p_east_center;
	JLabel b_num;
	JTextField t_num;
	JLabel b_category;
	JLabel b_name;
	JLabel b_publisher;
	JLabel b_writer;
	JLabel b_registDay;
	JLabel b_amount;
	JLabel b_condition;
	JLabel b_officer;
	JLabel b_reason;
	JLabel b_detailInfo;

	Choice ch_category;
	JTextField t_name;
	JTextField t_publisher;
	JTextField t_writer;
	JTextField t_registDay;
	JTextField t_amount;
	Choice ch_condition;
	JTextField t_officer;
	JTextField t_reason;

	JTextArea t_detailInfo;
	JScrollPane t_scroll;

	JPanel p_east_south;
	JButton bt_new;
	JButton bt_edit;
	JButton bt_del;

	LibModel model;
	Connection conn;
	ResultSet rs;
	MainFrame main;

	int book_id;
	String[] columnName = { "도서 번호", "분류", "도서 명", "출판사", "저자", "보유 도서 수량" };

	ArrayList<JLabel> l_list = new ArrayList<JLabel>();
	ArrayList<JTextField> t_list = new ArrayList<JTextField>();

	Dimension d1 = new Dimension(1200, 50);
	Dimension d2 = new Dimension(550, 550);
	Dimension d3 = new Dimension(400, 370);

	Dimension d_bt = new Dimension(200, 40);
	Dimension d_label = new Dimension(110, 25);
	Dimension d_text = new Dimension(200, 25);
	Dimension d_search = new Dimension(400, 30);

	public BookLost(MainFrame main) {
		this.main = main;
		conn = main.getConn();
		create();
		bt_all.setContentAreaFilled(false);
		bt_del.setContentAreaFilled(false);
		bt_edit.setContentAreaFilled(false);
		bt_jurisdiction.setContentAreaFilled(false);
		bt_new.setContentAreaFilled(false);
		bt_rental.setContentAreaFilled(false);
		bt_search.setContentAreaFilled(false);

		add(p_west);
		p_west.setPreferredSize(d2);

		p_west.add(p_west_north);
		p_west_north.add(bt_all);
		p_west_north.add(bt_jurisdiction);
		p_west_north.add(bt_rental);
		p_west.add(p_west_center);
		p_west_center.add(scroll);

		// 리스트에 넣기
		l_list.add(b_num);
		l_list.add(b_category);
		l_list.add(b_name);
		l_list.add(b_publisher);
		l_list.add(b_writer);
		l_list.add(b_registDay);
		l_list.add(b_amount);
		l_list.add(b_condition);
		l_list.add(b_officer);
		l_list.add(b_reason);
		l_list.add(b_detailInfo);

		t_list.add(t_num);

		t_list.add(t_name);
		t_list.add(t_publisher);
		t_list.add(t_writer);
		t_list.add(t_registDay);
		t_list.add(t_amount);

		t_list.add(t_officer);
		t_list.add(t_reason);

		add(p_east);
		p_east.setPreferredSize(d2);

		p_east.add(p_east_north);
		p_east_north.add(t_search);
		t_search.setPreferredSize(d_search);
		p_east_north.add(bt_search);

		for (int i = 0; i < t_list.size(); i++) {
			t_list.get(i).setPreferredSize(d_text);

		}
		t_scroll.setPreferredSize(new Dimension(200, 50));
		for (int i = 0; i < l_list.size(); i++) {

			l_list.get(i).setPreferredSize(d_label);
		}

		p_east.add(p_east_center);
		p_east_center.setPreferredSize(d3);
		p_east_center.add(b_num);
		p_east_center.add(t_num);

		p_east_center.add(b_category);
		p_east_center.add(ch_category);
		ch_category.setPreferredSize(d_text);
		ch_category.add("선택");
		ch_category.add("교육");
		ch_category.add("소설");
		ch_category.add("에세이");
		p_east_center.add(b_name);
		p_east_center.add(t_name);
		p_east_center.add(b_publisher);
		p_east_center.add(t_publisher);
		p_east_center.add(b_writer);
		p_east_center.add(t_writer);
		p_east_center.add(b_registDay);
		p_east_center.add(t_registDay);
		p_east_center.add(b_amount);
		p_east_center.add(t_amount);
		p_east_center.add(b_condition);
		ch_condition.add("선택");
		ch_condition.add("정상");
		ch_condition.add("분실");
		ch_condition.add("파손");
		ch_condition.setPreferredSize(d_text);
		p_east_center.add(ch_condition);

		p_east_center.add(b_officer);
		p_east_center.add(t_officer);
		p_east_center.add(b_reason);
		p_east_center.add(t_reason);
		p_east_center.add(b_detailInfo);
		p_east_center.add(t_scroll);

		p_east.add(p_east_south);

		p_east_south.add(bt_edit);
		p_east_south.add(bt_del);

		// 버튼과 리스너 연결

		bt_del.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(main, "삭제하시겠습니까?") == JOptionPane.OK_OPTION) {
					// delete();
					selectAll(0, BookManagement.LOST_BOOK_TAB);
				}

			}
		});
		bt_edit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(ch_category.getSelectedIndex()!=0 && ch_condition.getSelectedIndex()!=0) {
					if (JOptionPane.showConfirmDialog(main, "수정하시겠습니까?") == JOptionPane.OK_OPTION) {
						edit();
						selectAll(0, BookManagement.LOST_BOOK_TAB);
					}
				}
				else if(ch_category.getSelectedIndex()==0) {
					JOptionPane.showMessageDialog(main, "분류를 선택하세요.");
				}
				else if(ch_condition.getSelectedIndex()==0) {
					JOptionPane.showMessageDialog(main, "도서 상태를 선택하세요.");
				}

			}
		});
		

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				book_id = Integer.parseInt((String) table.getValueAt(row, 0));
				getDetail(row);
			}
		});
		
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				selectAll(0, BookManagement.LOST_BOOK_TAB);
			}
		});

		model = new LibModel();
		selectAll(0, BookManagement.LOST_BOOK_TAB);
		setVisible(false);
	}

	public void create() {

		p_west = new JPanel();
		p_west_north = new JPanel();
		bt_all = new JButton("전체 유실도서");
		bt_jurisdiction = new JButton("처리 대기 도서");
		bt_rental = new JButton("처리 완료 도서");

		p_west_center = new JPanel();
		table = new JTable(6, 10);
		scroll = new JScrollPane(table);

		p_east = new JPanel();
		p_east_north = new JPanel();
		t_search = new JTextField();
		bt_search = new JButton("검색");

		p_east_center = new JPanel();
		b_num = new JLabel("도서번호");
		t_num = new JTextField();
		t_num.setEditable(false);
		b_category = new JLabel("분류");
		b_name = new JLabel("도서명");
		b_publisher = new JLabel("출판사");
		b_writer = new JLabel("저자");
		b_registDay = new JLabel("도서 등록일");
		b_amount = new JLabel("동일 도서 수량");
		b_condition = new JLabel("도서상태");
		b_officer = new JLabel("책임자");
		b_reason = new JLabel("사유");
		b_detailInfo = new JLabel("파손 상세 정보");

		ch_category = new Choice();
		t_name = new JTextField();
		t_publisher = new JTextField();
		t_writer = new JTextField();
		t_registDay = new JTextField();
		t_amount = new JTextField();
		ch_condition = new Choice();
		t_officer = new JTextField();
		t_reason = new JTextField();
		t_detailInfo = new JTextArea();
		t_scroll = new JScrollPane(t_detailInfo);

		p_east_south = new JPanel();
		bt_new = new JButton("신규");
		bt_edit = new JButton("수정");
		bt_del = new JButton("삭제");

	}


	public void edit() {
		PreparedStatement pstmt = null;
		int book_id = Integer.parseInt(t_num.getText());
		String[] data = {Integer.toString(ch_category.getSelectedIndex()), t_name.getText(), t_publisher.getText(), t_writer.getText(), t_amount.getText()};
		String[] detailData = {Integer.toString(ch_condition.getSelectedIndex()), t_registDay.getText()};
		ManageQuery.bookEdit(conn, data, book_id);
		ManageQuery.detailEdit(conn, detailData, book_id, 0);

		String sql = "update lib_memo set officer=?, reason=?, detail=?";
		sql += " where book_id=?";
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, t_officer.getText());
			pstmt.setString(2, t_reason.getText());
			pstmt.setString(3, t_detailInfo.getText());
			pstmt.setInt(4, book_id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void delete() {

		PreparedStatement pstmt = null;
		String sql = "delete from lib_book where book_id=" + book_id;
		// System.out.println(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			// DML 수행에 의해 영향을 받은 레코드 수를 반환
			int result = pstmt.executeUpdate();
			if (result == 0) {
				JOptionPane.showMessageDialog(main, "실패");
			} else {
				JOptionPane.showMessageDialog(main, "삭제성공");
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

	public void selectAll(int bt_index, int book_lost) {

		rs = ManageQuery.showAllBooks(conn, bt_index, book_lost);
		setModelData(rs);
		table.setModel(model);
		table.updateUI();
		System.out.println("완료2");
	}

	public void setModelData(ResultSet rs) {
		try {
			rs.last();
			int total = rs.getRow();
			String[][] data = new String[total][columnName.length];
			rs.first();
			for (int i = 0; i < total; i++) {
				data[i][0] = Integer.toString(rs.getInt("book_id"));
				data[i][1] = rs.getString("genre");
				data[i][2] = rs.getString("book_name");
				data[i][3] = rs.getString("publisher");
				data[i][4] = rs.getString("writer");
				data[i][5] = Integer.toString(rs.getInt("book_same_amount"));
				rs.next();
			}
			model.columnName = columnName;
			model.data = data;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			main.db.disconnect(ManageQuery.pstmt, ManageQuery.rs);
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void searchBook() {
		rs = ManageQuery.searchBook(conn, t_search.getText());
		setModelData(rs);
		table.setModel(model);
		System.out.println("완료");
	}

	public void getDetail(int row) {
		rs = ManageQuery.getBookDetail(conn, model.getValueAt(row, 0).toString());
		t_num.setText(model.getValueAt(row, 0).toString());
		ch_category.select(model.getValueAt(row, 1).toString());
		t_name.setText(model.getValueAt(row, 2).toString());
		t_publisher.setText(model.getValueAt(row, 3).toString());
		t_writer.setText(model.getValueAt(row, 4).toString());
		t_amount.setText(model.getValueAt(row, 5).toString());

		PreparedStatement pstmt = null;
		String sql = "select book_id, officer, reason, detail from lib_memo";
		sql += " where book_id=" + book_id;
		try {
			rs.beforeFirst();
			if (rs.next()) {
				t_registDay.setText(rs.getString("book_regist_date"));
				ch_condition.select(rs.getString("book_state"));
			}
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			rs.beforeFirst();
			if (rs.next()) {
				t_officer.setText(rs.getString("officer"));
				t_reason.setText(rs.getString("reason"));
				t_detailInfo.setText(rs.getString("detail"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}