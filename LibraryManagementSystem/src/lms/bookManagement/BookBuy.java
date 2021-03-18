package lms.bookManagement;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import lms.db.ConnectionManager;

public class BookBuy extends JPanel {
	// 메서드 보유 클래스
	BookBuyMethod method;
	ConnectionManager connectionManager;
	Connection con;
	BookBuyTableModel model;

	JPanel p_west;
	JPanel p_west_north;
	JLabel l_term;
	// JTextField t_term1;
	Choice c_termYear;
	Choice c_termMonth;

	JLabel l_l;
	// JTextField t_term2;
	Choice c_termYear2;
	Choice c_termMonth2;
	JButton bt_north_search;

	// JButton bt_before;
	// JLabel current;
	// JButton bt_next;

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
	JLabel b_buyDay;
	JLabel b_buyAmount;
	JLabel b_sum;
	JLabel b_officer;
	JLabel b_condition;

	// JTextField t_category;
	Choice c_category;

	JTextField t_name;

	JTextField t_publisher;
	// Choice c_publisher;

	JTextField t_writer;
	// JTextField t_buyDay;
	Choice c_buyYear;
	Choice c_buyMonth;
	Choice c_buyDate;

	JTextField t_buyAmount;
	JTextField t_sum;
	JTextField t_officer;
	Choice c_condition;

	JPanel p_east_south;
	JButton bt_regist;
	JButton bt_edit;
	JButton bt_del;

	ArrayList<JLabel> l_list = new ArrayList<JLabel>();
	ArrayList<JTextField> t_list = new ArrayList<JTextField>();

	Dimension d1 = new Dimension(1200, 50);

	Dimension d2 = new Dimension(550, 500);
	Dimension d3 = new Dimension(400, 350);

	Dimension d_bt = new Dimension(200, 40);
	Dimension d_label = new Dimension(100, 25);
	Dimension d_text = new Dimension(200, 25);
	Dimension d_search = new Dimension(400, 30);

	public BookBuy() {
		connectionManager = new ConnectionManager();
		con = connectionManager.getConn();

		create();

		add(p_west);
		p_west.setPreferredSize(d2);

		p_west.add(p_west_north);
		p_west_north.setPreferredSize(new Dimension(500, 80));

		p_west_north.add(l_term);
		p_west_north.add(c_termYear);
		p_west_north.add(c_termMonth);

		p_west_north.add(l_l);

		p_west_north.add(c_termYear2);
		p_west_north.add(c_termMonth2);

		p_west_north.add(bt_north_search);
		// p_west_north.add(bt_before);
		// p_west_north.add(current);
		// p_west_north.add(bt_next);

		p_west.add(p_west_center);
		p_west_center.setPreferredSize(new Dimension(550, 350));
		p_west_center.add(scroll);

		// 리스트에 넣기
		l_list.add(b_num);
		l_list.add(b_category);
		l_list.add(b_name);
		l_list.add(b_publisher);
		l_list.add(b_writer);
		l_list.add(b_buyDay);
		l_list.add(b_buyAmount);
		l_list.add(b_sum);
		l_list.add(b_officer);
		l_list.add(b_condition);

		t_list.add(t_num);
		// t_list.add(c_category);
		t_list.add(t_name);
		t_list.add(t_publisher);
		t_list.add(t_writer);
		// t_list.add(t_buyDay);
		t_list.add(t_buyAmount);
		t_list.add(t_sum);
		t_list.add(t_officer);

		c_condition.setPreferredSize(d_text);
		c_condition.add("처리중");
		c_condition.add("처리완료");

		add(p_east);
		p_east.setPreferredSize(d2);

		p_east.add(p_east_north);
		p_east_north.add(t_search);
		t_search.setPreferredSize(d_search);
		p_east_north.add(bt_search);

		c_category.setPreferredSize(d_text);
		for (int i = 0; i < t_list.size(); i++) {
			t_list.get(i).setPreferredSize(d_text);

		}
		for (int i = 0; i < l_list.size(); i++) {

			l_list.get(i).setPreferredSize(d_label);
		}

		c_termYear.setPreferredSize(new Dimension(100, 25));
		c_termMonth.setPreferredSize(new Dimension(45, 25));

		c_termYear2.setPreferredSize(new Dimension(100, 25));
		c_termMonth2.setPreferredSize(new Dimension(45, 25));

		c_buyYear.setPreferredSize(new Dimension(100, 25));
		c_buyMonth.setPreferredSize(new Dimension(45, 25));
		c_buyDate.setPreferredSize(new Dimension(45, 25));

		scroll.setPreferredSize(new Dimension(530, 300)); // 임시

		p_east.add(p_east_center);
		p_east_center.setPreferredSize(d3);
		p_east_center.add(b_num);
		p_east_center.add(t_num);

		p_east_center.add(b_category);
		p_east_center.add(c_category);
		p_east_center.add(b_name);
		p_east_center.add(t_name);
		p_east_center.add(b_publisher);
		p_east_center.add(t_publisher);
		p_east_center.add(b_writer);
		p_east_center.add(t_writer);
		p_east_center.add(b_buyDay);
		p_east_center.add(c_buyYear);
		p_east_center.add(c_buyMonth);
		p_east_center.add(c_buyDate);

		p_east_center.add(b_buyAmount);
		p_east_center.add(t_buyAmount);
		p_east_center.add(b_sum);
		p_east_center.add(t_sum);
		p_east_center.add(b_officer);
		p_east_center.add(t_officer);
		p_east_center.add(b_condition);
		p_east_center.add(c_condition);

		p_east.add(p_east_south);
		p_east_south.add(bt_regist);
		p_east_south.add(bt_edit);
		p_east_south.add(bt_del);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setRowHeight(25);
		table.getColumn("구매번호").setPreferredWidth(80);
		table.getColumn("장르").setPreferredWidth(80);
		table.getColumn("도서명").setPreferredWidth(150);
		table.getColumn("출판사").setPreferredWidth(80);
		table.getColumn("저자").setPreferredWidth(80);
		table.getColumn("도서구매일").setPreferredWidth(80);
		table.getColumn("구매수량").setPreferredWidth(80);
		table.getColumn("금액").setPreferredWidth(80);
		table.getColumn("책임자").setPreferredWidth(80);
		table.getColumn("처리상태").setPreferredWidth(80);

		method = new BookBuyMethod(this);
		method.setChoices();
		method.setTable(0); // 이거 통합할 때 좀더 상위 클래스로 위치 옮길 것

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				method.inputEast();
			}
		});

		bt_north_search.addActionListener((e) -> {
			method.setTable(1);
		});

		bt_search.addActionListener((e) -> {
			method.setTable(2);
		});

		bt_regist.addActionListener((e) -> {
			method.insertBt();
		});

		bt_edit.addActionListener((e) -> {
			method.updateBt();
		});

		bt_del.addActionListener((e) -> {
			method.deleteBt();
		});

		setVisible(false);

	}

	public void create() {

		Font font = new Font("HY엽서L", Font.PLAIN, 14);
		p_west = new JPanel();
		p_west_north = new JPanel();

		p_west.setFont(font);

		l_term = new JLabel("월별");
		l_term.setFont(font);
		// t_term1 = new JTextField(15);
		c_termYear = new Choice();
		c_termMonth = new Choice();

		l_l = new JLabel("~");
		l_l.setFont(font);
		// t_term2 = new JTextField(15);
		c_termYear2 = new Choice();
		c_termMonth2 = new Choice();

		bt_north_search = new JButton("검색");

		// bt_before = new JButton("◀");
		// current = new JLabel("2019년1월");
		// bt_next = new JButton("▶");

		p_west_center = new JPanel();
		model = new BookBuyTableModel();
		table = new JTable(model);
		scroll = new JScrollPane(table);

		p_east = new JPanel();
		p_east_north = new JPanel();
		t_search = new JTextField();
		bt_search = new JButton("검색");

		p_east_center = new JPanel();
		b_num = new JLabel("구매번호");
		t_num = new JTextField();
		t_num.setEditable(false);
		b_category = new JLabel("장르");
		b_name = new JLabel("도서명");
		b_publisher = new JLabel("출판사");
		b_writer = new JLabel("저자");
		b_buyDay = new JLabel("구매일");
		b_buyAmount = new JLabel("구매 수량");
		b_sum = new JLabel("금액");
		b_officer = new JLabel("책임자");
		b_condition = new JLabel("처리 상태");
		c_category = new Choice();
		t_name = new JTextField();
		t_publisher = new JTextField();
		t_writer = new JTextField();

		c_buyYear = new Choice();
		c_buyMonth = new Choice();
		c_buyDate = new Choice();

		t_buyAmount = new JTextField();
		t_sum = new JTextField();
		t_officer = new JTextField();
		c_condition = new Choice();

		p_east_south = new JPanel();
		bt_regist = new JButton("등록");
		bt_edit = new JButton("수정");
		bt_del = new JButton("삭제");

	}
}