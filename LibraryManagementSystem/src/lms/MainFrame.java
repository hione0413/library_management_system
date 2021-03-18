package lms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import lms.backupDb.DbBackupMain;
import lms.book.BookMain;
import lms.bookManagement.BookManagement;
import lms.db.ConnectionManager;
import lms.member.MemberMain;

public class MainFrame extends JFrame {
	// public static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	// public static int WIDTH = (int)screen.getWidth()*2/3;
	// public static int HEIGHT = (int)screen.getHeight()*2/3;
	public static int WIDTH = 1200;
	public static int HEIGHT = 700;
	Dimension menuSize;
	JMenuBar bar_menu;
	String[] img_path = { "ryan.gif", "ryan2.gif", "ryan6.gif", "ryan4.gif" };
	String[] str_menu = { "대여 / 반납", "도서 관리", "회원 관리", "DB 관리" };
	JMenu[] menu = new JMenu[str_menu.length];
	ImageIcon[] img_icon = new ImageIcon[img_path.length];
	JPanel container;
	JPanel[] pages = new JPanel[menu.length];
	BookMain loan;
	BookManagement bkmain;
	MemberMain member;
	DbBackupMain backupDb;
	Boolean isLogined = false;
	public ConnectionManager db;
	Connection conn;
	public MainFrame() {
		db = new ConnectionManager();
		conn = db.getConn();
		Font font = new Font("HY엽서L", Font.PLAIN, 14);
		setLayout(new BorderLayout());
		container = new JPanel();
		loan = new BookMain();
		bkmain = new BookManagement(this);
		member = new MemberMain();
		backupDb = new DbBackupMain(this);
		menuSize = new Dimension(WIDTH / 4, 55);
		setImgIcon();
		setMenuBar();
		
		setTabs(loan, 0);
		setTabs(bkmain, 1);
		setTabs(member, 2);
		setTabs(backupDb, 3);
		setMenuAction();
		add(container);

		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		new LoginDialog(this);
	}

	public void setImgIcon() {
		for (int i = 0; i < img_icon.length; i++) {
			img_icon[i] = new ImageIcon("res/" + img_path[i]);
		}
	}

	public void setMenuBar() {
		bar_menu = new JMenuBar();

		for (int i = 0; i < menu.length; i++) {
			menu[i] = new JMenu(str_menu[i]);
			img_icon[i].setImage(img_icon[i].getImage().getScaledInstance(55, 55, Image.SCALE_REPLICATE));
			menu[i].setIcon(img_icon[i]);
			menu[i].setPreferredSize(menuSize);
			menu[i].setFont(new Font("HY엽서L", Font.PLAIN, 24));
			menu[i].setHorizontalAlignment(SwingConstants.RIGHT);
			menu[i].setBorder(BorderFactory.createLineBorder(Color.gray));
			bar_menu.add(menu[i]);
		}

		this.setJMenuBar(bar_menu);
	}
	public Connection getConn() {
		return conn;
	}
	public void setMenuAction() {
		for (int i = 0; i < menu.length; i++) {
			if (pages[i] != null) {
				menu[i].addMouseListener(new MouseAdapter(){
					public void mouseClicked(MouseEvent e) {
						for(int i = 0 ; i < menu.length; i ++) {
							if(e.getSource()==menu[i]) {
								pages[i].setVisible(true);
							}
							else if(pages[i]!=null){
								pages[i].setVisible(false);
							}
						}
					}
				});
			}
		}
	}

	public void setTabs(JPanel p, int index) {
		container.add(p);
		pages[index] = p;
		pages[index].setVisible(false);
	}
	public void close() {
		System.exit(0);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MainFrame();
	}

}
