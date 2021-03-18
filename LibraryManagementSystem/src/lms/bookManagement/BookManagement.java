package lms.bookManagement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import lms.MainFrame;

public class BookManagement extends JPanel{
	public static final int BOOK_LIST_TAB = 3;
	public static final int LOST_BOOK_TAB = 4;
	public static final int REQUEST_BOOK_TAB = 5;
	public static final int PURCHASE_BOOK_TAB = 6;
	
	JPanel p_center;
	JPanel p_north;
	JButton bt_list;
	JButton bt_lost;
	JButton bt_request;
	JButton bt_buy;
	MainFrame main;
	BookLost lost;
	
	
	Dimension d1 = new Dimension(1200,50);
	Dimension d2 = new Dimension(550,500);
	Dimension d3 = new Dimension(400,300);

	
	Dimension d_bt = new Dimension(200,40);
	JPanel[] pages = new JPanel[4];
	
	ArrayList<JButton> buttonList = new ArrayList<JButton>();
	
	public BookManagement(MainFrame main) {

		this.main = main;
		main.getConn();
		Font font = new Font("HY엽서L", Font.BOLD, 14);
		create();
		setLayout(new BorderLayout());
		//붙이기	
		add(p_north,BorderLayout.NORTH);
		add(p_center);
		
		for(int i=0;i<pages.length;i++) {
			FontChange(pages[i], font);
			p_center.add(pages[i]);
		}
		
		buttonList.add(bt_list);
		buttonList.add(bt_lost);
		buttonList.add(bt_request);
		buttonList.add(bt_buy);
		
		bt_buy.setContentAreaFilled(false);
		bt_list.setContentAreaFilled(false);
		bt_lost.setContentAreaFilled(false);
		bt_request.setContentAreaFilled(false);
		
		p_north.add(bt_list);
		bt_list.setPreferredSize(d_bt);
		bt_list.setFont(new Font("돋움", Font.BOLD, 30));
		p_north.add(bt_lost);
		bt_lost.setPreferredSize(d_bt);
		bt_lost.setFont(new Font("돋움", Font.BOLD, 30));
		p_north.add(bt_request);
		bt_request.setPreferredSize(d_bt);
		bt_request.setFont(new Font("돋움", Font.BOLD, 30));
		p_north.add(bt_buy);
		bt_buy.setPreferredSize(d_bt);
		bt_buy.setFont(new Font("돋움", Font.BOLD, 30));
		
		

		//리스너
		bt_list.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showPage(0);
				btBackground(0);
				
			}
		});
		bt_lost.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showPage(1);
				btBackground(1);
				getSelectAll();
				
			}
		});
		bt_request.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showPage(2);
				btBackground(2);
				
			}
		});
		bt_buy.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showPage(3);
				btBackground(3);
			}
		});
		setSize(1200,595);
		setVisible(false);
	
	}
	public void create() {

		p_north = new JPanel();
		p_center = new JPanel();
		bt_list = new JButton("도서 목록");
		bt_lost= new JButton("유실 도서");
		bt_request = new JButton("요청 도서");
		bt_buy= new JButton("구매도서");

		pages[0] = new BookList(main);
		pages[1] = new BookLost(main);
		pages[2] = new BookRequest(main);
		pages[3] = new BookBuy();
	}
	public void getSelectAll() {
		lost = new BookLost(main);
		lost.selectAll(0, 2);
	}
	public void showPage(int page) {
		for (int i = 0; i < pages.length; i++) {
			if (i == page) {
				pages[i].setVisible(true);
				// System.out.println(i);
			} else {
				pages[i].setVisible(false);
			}
		}
	}
	
	public void FontChange(Component component, Font font) {
		component.setFont(font);
		if(component instanceof Container) {
			for(Component child : ((Container)component).getComponents()) {
				FontChange(child, font);
			}
		}
	}
	public void btBackground(int page) {
		for (int i = 0; i < pages.length; i++) {
			if (i == page) {
				buttonList.get(i).setBackground(Color.GREEN);
				// System.out.println(i);
			} else {
				buttonList.get(i).setBackground(null);
			}
		}
	}
}
