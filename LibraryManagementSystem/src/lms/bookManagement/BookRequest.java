package lms.bookManagement;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JTextField;

import lms.MainFrame;

public class BookRequest extends JPanel {

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
	JLabel b_requestDay;
	JLabel b_money;
	JLabel b_condition;
	JLabel b_requestPerson;

	Choice ch_category;
	JTextField t_name;
	JTextField t_publisher;
	JTextField t_writer;
	JTextField t_requestDay;
	JTextField t_money;
	Choice ch_condition;
	JTextField t_requestPerson;

	JPanel p_east_south;

	JButton bt_edit;
	JButton bt_del;

	MainFrame main;

	LibModel model;
	Connection conn;
	ResultSet rs;

	int request_id;
	String[] columnName = { "��û ��ȣ", "�帣", "���� ��", "���ǻ�", "����", "�ݾ�" };

	ArrayList<JLabel> l_list = new ArrayList<JLabel>();
	ArrayList<JTextField> t_list = new ArrayList<JTextField>();

	Dimension d1 = new Dimension(1200, 50);
	Dimension d2 = new Dimension(550, 500);
	Dimension d3 = new Dimension(400, 300);

	Dimension d_bt = new Dimension(200, 40);
	Dimension d_label = new Dimension(110, 25);
	Dimension d_text = new Dimension(200, 25);
	Dimension d_search = new Dimension(400, 30);

	public BookRequest(MainFrame main) {
		this.main = main;
		conn = main.getConn();
		create();

		bt_all.setContentAreaFilled(false);
		bt_del.setContentAreaFilled(false);
		bt_edit.setContentAreaFilled(false);
		bt_jurisdiction.setContentAreaFilled(false);
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

		// ����Ʈ�� �ֱ�
		l_list.add(b_num);
		l_list.add(b_category);
		l_list.add(b_name);
		l_list.add(b_publisher);
		l_list.add(b_writer);
		l_list.add(b_requestDay);
		l_list.add(b_money);
		l_list.add(b_condition);
		l_list.add(b_requestPerson);

		t_list.add(t_num);

		t_list.add(t_name);
		t_list.add(t_publisher);
		t_list.add(t_writer);
		t_list.add(t_requestDay);
		t_list.add(t_money);
		t_list.add(t_requestPerson);

		ch_condition.setPreferredSize(d_text);
		ch_condition.add("����");
		ch_condition.add("ó����");
		ch_condition.add("ó���Ϸ�");

		add(p_east);
		p_east.setPreferredSize(d2);

		p_east.add(p_east_north);
		p_east_north.add(t_search);
		t_search.setPreferredSize(d_search);
		p_east_north.add(bt_search);

		for (int i = 0; i < t_list.size(); i++) {
			t_list.get(i).setPreferredSize(d_text);

		}
		for (int i = 0; i < l_list.size(); i++) {

			l_list.get(i).setPreferredSize(d_label);
		}

		p_east.add(p_east_center);
		p_east_center.setPreferredSize(d3);
		p_east_center.add(b_num);
		p_east_center.add(t_num);

		p_east_center.add(b_category);
		ch_category.add("����");
		ch_category.add("����");
		ch_category.add("�Ҽ�");
		ch_category.add("������");
		ch_category.setPreferredSize(d_text);
		p_east_center.add(ch_category);
		p_east_center.add(b_name);
		p_east_center.add(t_name);
		p_east_center.add(b_publisher);
		p_east_center.add(t_publisher);
		p_east_center.add(b_writer);
		p_east_center.add(t_writer);
		p_east_center.add(b_requestDay);
		p_east_center.add(t_requestDay);
		p_east_center.add(b_money);
		p_east_center.add(t_money);
		// p_east_center.add(b_condition);
		// p_east_center.add(c_condition);
		p_east_center.add(b_requestPerson);
		p_east_center.add(t_requestPerson);
		p_east_center.add(b_condition);
		p_east_center.add(ch_condition);

		p_east.add(p_east_south);
		p_east_south.setPreferredSize(new Dimension(400, 200));
		p_east_south.add(bt_edit);
		p_east_south.add(bt_del);

		// ��ư�� ������ ����

		bt_del.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(main, "�����Ͻðڽ��ϱ�?") == JOptionPane.OK_OPTION) {
					delete();
					table.updateUI();
				}

			}
		});
		bt_edit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(main, "�����Ͻðڽ��ϱ�?") == JOptionPane.OK_OPTION) {
					edit();

					table.updateUI();
				}

			}
		});

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				request_id = Integer.parseInt((String) table.getValueAt(row, 0));
				getDetail(row);
			}
		});

		model = new LibModel();
		selectRequest();
		setVisible(false);
	}

	public void create() {

		p_west = new JPanel();
		p_west_north = new JPanel();
		bt_all = new JButton("��ü ��û ����");
		bt_jurisdiction = new JButton("ó�� ��� ����");
		bt_rental = new JButton("ó�� �Ϸ� ����");

		p_west_center = new JPanel();
		table = new JTable();
		scroll = new JScrollPane(table);

		p_east = new JPanel();
		p_east_north = new JPanel();
		t_search = new JTextField();
		bt_search = new JButton("�˻�");

		p_east_center = new JPanel();
		b_num = new JLabel("������ȣ");
		t_num = new JTextField();
		t_num.setEditable(false);
		b_category = new JLabel("�з�");
		b_name = new JLabel("������");
		b_publisher = new JLabel("���ǻ�");
		b_writer = new JLabel("����");
		b_requestDay = new JLabel("���� ��û��");
		b_money = new JLabel("���� �ݾ�");
		b_condition = new JLabel("����");
		b_requestPerson = new JLabel("��û��");

		ch_category = new Choice();
		ch_category.setEnabled(false);

		t_name = new JTextField();
		t_name.setEditable(false);
		t_publisher = new JTextField();
		t_publisher.setEditable(false);
		t_writer = new JTextField();
		t_writer.setEditable(false);
		t_requestDay = new JTextField();
		t_requestDay.setEditable(false);
		t_money = new JTextField();
		t_money.setEditable(false);
		ch_condition = new Choice();
		t_requestPerson = new JTextField();
		t_requestPerson.setEditable(false);

		p_east_south = new JPanel();
		bt_edit = new JButton("����");
		bt_del = new JButton("����");

	}

	public void selectRequest() {
		rs = ManageQuery.showRequestBooks(conn);
		setModelData(rs);
		table.setModel(model);
		table.updateUI();
	}

	public void getDetail(int row) {

		rs = ManageQuery.getRequestDetail(conn, model.getValueAt(row, 0).toString());
		t_num.setText(model.getValueAt(row, 0).toString());
		ch_category.select(model.getValueAt(row, 1).toString());
		t_name.setText(model.getValueAt(row, 2).toString());
		t_publisher.setText(model.getValueAt(row, 3).toString());
		t_writer.setText(model.getValueAt(row, 4).toString());
		t_money.setText(model.getValueAt(row, 5).toString());

		try {
			rs.beforeFirst();
			if (rs.next()) {
				t_requestDay.setText(rs.getString("request_date"));
				ch_condition.select(1);
				t_requestPerson.setText(rs.getString("mem_id"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setModelData(ResultSet rs) {
		try {
			rs.last();
			int total = rs.getRow();
			String[][] data = new String[total][columnName.length];
			rs.first();
			for (int i = 0; i < total; i++) {
				data[i][0] = Integer.toString(rs.getInt("req_id"));
				data[i][1] = rs.getString("genre");
				data[i][2] = rs.getString("book_name");
				data[i][3] = rs.getString("publisher");
				data[i][4] = rs.getString("writer");
				data[i][5] = Integer.toString(rs.getInt("pay"));
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

	public void edit() {
		String[] str = new String[6];
		str[0] = t_num.getText();
		str[1] = t_name.getText();
		str[2] = t_publisher.getText();
		str[3] = t_writer.getText();
		str[4] = Integer.toString(ch_category.getSelectedIndex());
		str[5] = Integer.toString(ch_condition.getSelectedIndex());

		ManageQuery.requestEdit(conn, str, request_id);

	}

	public void delete() {

		PreparedStatement pstmt = null;

		String sql = "delete from lib_book_request where request_id=" + request_id;

		// System.out.println(sql);
		try {
			pstmt = conn.prepareStatement(sql);

			// DML ���࿡ ���� ������ ���� ���ڵ� ���� ��ȯ
			int result = pstmt.executeUpdate();

			if (result == 0) {
				JOptionPane.showMessageDialog(main, "����");
			} else {
				JOptionPane.showMessageDialog(main, "��������");
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
}