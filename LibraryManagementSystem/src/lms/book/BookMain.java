package lms.book;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JPanel;

import lms.db.ConnectionManager;

public class BookMain extends JPanel {
	JPanel p_north = new JPanel();
	JPanel p_center = new JPanel();
	JButton bt_loan, bt_Re;

	private Connection con;
	ConnectionManager connectionManager;

	JPanel[] pages = new JPanel[2];

	public BookMain() {
		setLayout(new BorderLayout());
		p_north.setPreferredSize(new Dimension(700, 45));
		bt_loan = new JButton("����");
		bt_loan.setPreferredSize(new Dimension(100, 35));
		bt_loan.setFont(new Font("HY����L", Font.BOLD, 25));

		bt_loan.setContentAreaFilled(false);
		bt_Re = new JButton("�ݳ�");
		bt_Re.setPreferredSize(new Dimension(100, 35));
		bt_Re.setFont(new Font("HY����L", Font.BOLD, 25));
		bt_Re.setContentAreaFilled(false);

		p_north.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		// p_west.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		// p_east.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

		p_north.add(bt_loan);
		p_north.add(bt_Re);
		connectionManager = new ConnectionManager();
		// db �����ϱ�
		con = connectionManager.getConn(); // �гε��� new �ϱ� ���� db������ �̹� �Ǿ�����!!
		pages[0] = new Loan(this);
		pages[1] = new Return(this);
		showPages(0);
		setPages(0);
		bt_loan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPages(0);
				setPages(0);
			}
		});

		bt_Re.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showPages(1);
				setPages(1);
			}
		});
		this.setLayout(new BorderLayout());
		add(p_north, BorderLayout.NORTH);
		add(p_center);

	}

	public void setPages(int i) {
			p_center.add(pages[i]);
	}

	public void showPages(int index) {
		for (int i = 0; i < pages.length; i++) {
			if (i == index) {
				pages[i].setVisible(true);
			} else {
				pages[i].setVisible(false);
			}
		}
	}

	public Connection getCon() {
		return con;
	}

}
