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

public class BookList extends JPanel {
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
	JLabel b_rentalCondition;
	JLabel b_rentalPerson;
	JLabel b_reservation;
	Choice ch_category;
	JTextField t_name;
	JTextField t_publisher;
	JTextField t_writer;
	JTextField t_registDay;
	JTextField t_amount;
	Choice ch_condition;
	Choice ch_rentalCondition;
	JTextField t_rentalPerson;
	JTextField t_reservation;
	JPanel p_east_south;
	JButton bt_regist;
	JButton bt_edit;
	JButton bt_del;
	boolean b_btflag = true;
	///////////// 0210 ������ - ����/////////////
	ManageTableModel model;
	Connection conn;
	MainFrame main;
	ResultSet rs;

	String[] columnName = { "���� ��ȣ", "�з�", "���� ��", "���ǻ�", "����", "���� ���� ����" };
	////////////////////////////////////////

	ArrayList<JLabel> l_list = new ArrayList<JLabel>();
	ArrayList<JTextField> t_list = new ArrayList<JTextField>();

	Dimension d1 = new Dimension(1200, 50);
	Dimension d2 = new Dimension(550, 500);
	Dimension d3 = new Dimension(400, 300);

	Dimension d_bt = new Dimension(200, 40);
	Dimension d_label = new Dimension(110, 25);
	Dimension d_text = new Dimension(200, 25);
	Dimension d_search = new Dimension(400, 30);

	public BookList(MainFrame main) {
		create();
		this.main = main;
		conn = main.getConn();

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
		l_list.add(b_registDay);
		l_list.add(b_amount);
		l_list.add(b_condition);
		l_list.add(b_rentalCondition);
		l_list.add(b_rentalPerson);
		l_list.add(b_reservation);

		t_list.add(t_num);

		t_list.add(t_name);
		t_list.add(t_publisher);
		t_list.add(t_writer);
		t_list.add(t_registDay);
		t_list.add(t_amount);

		t_list.add(t_rentalPerson);
		t_list.add(t_reservation);

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
		p_east_center.add(b_registDay);
		p_east_center.add(t_registDay);
		p_east_center.add(b_amount);
		p_east_center.add(t_amount);
		p_east_center.add(b_condition);
		p_east_center.add(ch_condition);
		ch_condition.setPreferredSize(d_text);
		ch_condition.add("����");
		ch_condition.add("����");
		ch_condition.add("�н�");
		ch_condition.add("�ļ�");

		////////////////////// 0210 ������ - ����///////////////////////
		p_east_center.add(b_rentalCondition);
		p_east_center.add(ch_rentalCondition);
		ch_rentalCondition.setPreferredSize(d_text);
		ch_rentalCondition.add("����");
		ch_rentalCondition.add("�뿩 ����");
		ch_rentalCondition.add("�뿩 ��");
		ch_rentalCondition.add("��ü");
		//////////////////////////////////////////////////////

		p_east_center.add(b_rentalPerson);
		p_east_center.add(t_rentalPerson);
		p_east_center.add(b_reservation);
		p_east_center.add(t_reservation);

		p_east.add(p_east_south);
		p_east_south.add(bt_regist);
		p_east_south.add(bt_edit);
		p_east_south.add(bt_del);

		// ��ư�� ������ ����

		bt_all.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showBookList(SHOW_ALL_BOOKS);
			}
		});
		bt_jurisdiction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showBookList(SHOW_RENTALABLE_BOOKS);
			}
		});
		bt_rental.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showBookList(SHOW_RENTALED_BOOKS);
			}
		});

		/////////////////////// 0210 ������ - �߰�///////////////////////
		bt_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (t_search.getText().equals("")) {
					showBookList(0);
				} else {
					searchBook();
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				t_registDay.setEditable(true);
				t_rentalPerson.setEditable(true);
				ch_condition.setEnabled(true);
				ch_rentalCondition.setEnabled(true);
				b_btflag = true;
				bt_regist.setText("�ű�");
				getBookDetail(table.getSelectedRow());
			}
		});
		////////////////////////////////////////////////////////////

		bt_del.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(main, "�����Ͻðڽ��ϱ�?") == JOptionPane.OK_OPTION) {
					// delete();
					table.updateUI();
				}

			}
		});
		bt_edit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(ch_category.getSelectedIndex()!=0 && ch_condition.getSelectedIndex()!=0 && ch_rentalCondition.getSelectedIndex()!=0) {
					 if((ch_condition.getSelectedIndex()==2 || ch_condition.getSelectedIndex()==3)&&ch_rentalCondition.getSelectedIndex()!=1) {
						JOptionPane.showMessageDialog(main, "�뿩���� ������ ���� ���¸� ���� �� �� �����ϴ�.");
					 }
					 else {
						if (JOptionPane.showConfirmDialog(main, "�����Ͻðڽ��ϱ�?") == JOptionPane.OK_OPTION) {
							bookModify();
							showBookList(0);
						}
					 }
				} else {
				}

			}
		});
		bt_regist.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(b_btflag == true) {
					t_num.setText("");
					t_name.setText("");
					t_publisher.setText("");
					t_writer.setText("");
					t_registDay.setText("");
					t_amount.setText("");
					t_rentalPerson.setText("");
					ch_category.select(0);
					ch_condition.select(0);
					
					t_registDay.setEditable(false);
					t_rentalPerson.setEditable(false);
					ch_condition.setEnabled(false);
					ch_rentalCondition.setEnabled(false);
					bt_regist.setText("���");
					b_btflag=false;
				}
				else {
					if (JOptionPane.showConfirmDialog(main, "����Ͻðڽ��ϱ�?") == JOptionPane.OK_OPTION) {
						////////////////// 0210 ������ - ����////////////
						if(ch_category.getSelectedIndex()==0) {
							JOptionPane.showMessageDialog(main, "�з��� ������ �ּ���");
						}else if(t_name.getText().equals("") ||t_publisher.getText().equals("") || t_writer.getText().equals("") || t_amount.getText().equals("")) {
							JOptionPane.showMessageDialog(main, "��� �Է��� �ּ���");
						}
						else{
							registBook();
							t_num.setText("");
							t_name.setText("");
							t_publisher.setText("");
							t_writer.setText("");
							t_registDay.setText("");
							t_rentalPerson.setText("");
							t_amount.setText("");
							ch_category.select(0);
							ch_condition.select(0);
							
							t_registDay.setEditable(true);
							t_rentalPerson.setEditable(true);
							ch_condition.setEnabled(true);
							ch_rentalCondition.setEnabled(true);
							showBookList(0);
							bt_regist.setText("�ű�");
							b_btflag=true;
						}
						
					}	
				}

			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				showBookList(0);
			}
			
		});
		

		model = new ManageTableModel();
		showBookList(0);
		setVisible(true);
	}

	public void create() {
		
		p_west = new JPanel();
		p_west_north = new JPanel();
		bt_all = new JButton("��ü����");
		bt_jurisdiction = new JButton("���� ����");
		bt_rental = new JButton("���⵵��");

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
		b_registDay = new JLabel("���� �����");
		b_amount = new JLabel("���� ���� ����");
		b_condition = new JLabel("��������");
		b_rentalCondition = new JLabel("�뿩����");
		b_rentalPerson = new JLabel("�뿩��");
		b_reservation = new JLabel("����");
		ch_category = new Choice();
		t_name = new JTextField();
		t_publisher = new JTextField();
		t_writer = new JTextField();
		t_registDay = new JTextField();
		t_amount = new JTextField();
		ch_condition = new Choice();
		ch_rentalCondition = new Choice();
		t_rentalPerson = new JTextField();
		t_reservation = new JTextField();
		
		p_east_south = new JPanel();
		bt_regist = new JButton("�ű�");
		bt_edit = new JButton("����");
		bt_del = new JButton("����");
		
	}

	

	///////////////////// 0210 ������ - ����/////////////////////
	///////////////////// �ʱ� ȭ�� �� �˻��� ������ ��� å ����Ʈ�� ���̺� ����ϴ� �޼ҵ�/////////////////
	public void showBookList(int index) {
		rs = ManageQuery.showAllBooks(conn, index, BookManagement.BOOK_LIST_TAB);
		setModelData(rs);
		table.setModel(model);
		table.updateUI();
	}

	///////////////////// �˻��� �Է��� �˻��� �ش� �˻�� ���õ� å�� ���̺� ����ϴ� �޼ҵ�///////////////
	public void searchBook() {
		rs = ManageQuery.searchBook(conn, t_search.getText());
		setModelData(rs);
		table.setModel(model);
		table.updateUI();
	}

	public void registBook() {
		String[] data = { Integer.toString(ch_category.getSelectedIndex()), t_name.getText(), t_publisher.getText(),
				t_writer.getText(), t_amount.getText() };
		if (ManageQuery.isExistPublisher(conn, t_publisher.getText())) {
			data[2] = Integer.toString(ManageQuery.getPublisherId(conn, t_publisher.getText()));
			ManageQuery.registBook(conn, data);
		} else {
			ManageQuery.addNewPublisher(conn, t_publisher.getText());
			data[2] = Integer.toString(ManageQuery.getPublisherId(conn, t_publisher.getText()));
			ManageQuery.registBook(conn, data);
		}
	}

	public void bookModify() {
		int book_id = 0;
		try {
			book_id = Integer.parseInt(t_num.getText());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(main, "�ùٸ��� ���� ���� ��ȣ Ÿ���Դϴ�");
		}
		String[] bookData = new String[5];
		String[] bookDetailData = new String[3];
		bookData[0] = Integer.toString(ch_category.getSelectedIndex());
		bookData[1] = t_name.getText();
		bookData[2] = t_publisher.getText();
		bookData[3] = t_writer.getText();
		bookData[4] = t_amount.getText();
		
		bookDetailData[0] = Integer.toString(ch_condition.getSelectedIndex());
		bookDetailData[1] = t_registDay.getText();
		bookDetailData[2] = Integer.toString(ch_rentalCondition.getSelectedIndex());
		ManageQuery.bookEdit(conn, bookData, book_id);
		ManageQuery.detailEdit(conn, bookDetailData, book_id, ch_rentalCondition.getSelectedIndex());
	}

	/////////////////////// ���̺� ���� data�� ���� �ϴ� �޼ҵ�////////////////////////
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

	////////////////////// ���̺��� rowŬ���� detail�� �ҷ����� �޼ҵ�////////////////////
	public void getBookDetail(int row) {
		rs = ManageQuery.getBookDetail(conn, model.getValueAt(row, 0).toString());
		t_num.setText(model.getValueAt(row, 0).toString());
		ch_category.select(model.getValueAt(row, 1).toString());
		t_name.setText(model.getValueAt(row, 2).toString());
		t_publisher.setText(model.getValueAt(row, 3).toString());
		t_writer.setText(model.getValueAt(row, 4).toString());
		t_amount.setText(model.getValueAt(row, 5).toString());

		try {
			rs.beforeFirst();
			if (rs.next()) {
				t_registDay.setText(rs.getString("book_regist_date"));
				ch_condition.select(rs.getString("book_state"));
				ch_rentalCondition.select(rs.getString("rental_state"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!(ch_rentalCondition.getSelectedItem().equals("�뿩 ����"))) {
			String name = ManageQuery.searchWhoRent(conn, Integer.parseInt(t_num.getText()));
			t_rentalPerson.setText(name);
		} else {
			t_rentalPerson.setText("");
		}
	}
}
