package lms.member;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import lms.MainFrame;
import lms.db.ConnectionManager;

public class MemberContents extends JPanel{
	//메서드 모임 클래스
	ConnectionManager connectionManager;
	MemMethod mem_method;
	Connection con;
	
	//RegistDialog에 MemberContents 넘기려고 만든 변수
	MemberContents me;
	
	//왼쪽 테이블 패널
	JPanel p_west;
	JPanel p_west1;
	JPanel p_west2;
	JPanel p_west3;
	JLabel l_memList;
	JButton bt_excel;
	//테이블 관련 부품들 정의할 것
	JTable table;
	JScrollPane scroll;
	MemberTableModel model;
	JLabel l_search;
	Choice ch_search;
	JTextField t_search;
	JButton bt_search;
	//오른쪽 상태입력 패널
	JPanel p_east, p_east2;
	JLabel l_info;
	JLabel l_number, l_name, l_phon, l_addres, l_email;
	JTextField t_number, t_name, t_phon, t_addres, t_email;
	JLabel l_state, l_rent, l_reserve;
	Choice ch_state;
	JLabel l_birth;
	Choice ch_year, ch_month, ch_date;
	JTextField t_rent, t_reserve;
	
	JButton bt_join, bt_alter, bt_del, bt_revive, bt_delCommit; 
	JPanel p_bt, p_btDel;
	
	JPanel p_eastLabelCenter;
	JPanel p_center;	//가운데 여백용 패널
	JPanel eastflow;	//회원관리<->삭제한 회원관리 페이지 전환할 때 버튼 담아둔 패널
	
	Boolean flag;
	
	public MemberContents() {
		//oracle 접속 메서드. 임시
		connectionManager=new ConnectionManager();
		con=connectionManager.getConn();
		me = this;
		
		setLayout(new BorderLayout());
		String font="HY엽서L";
		int h2=18;
		int h3=15;
		
		//서쪽 부품
		p_west=new JPanel();
		p_west.setLayout(new BorderLayout());
		p_west1=new JPanel();
		p_west2=new JPanel();
		p_west3=new JPanel();
		l_memList=new JLabel("회원목록");
		l_memList.setFont(new Font(font, Font.BOLD, h2));
		p_eastLabelCenter=new JPanel();
		//bt_allSelect=new JButton("전체선택");
		//bt_selectedDel=new JButton("선택삭제");
		bt_excel=new JButton("Excel로 저장");	bt_excel.setFont(new Font(font, Font.BOLD, h3));
		bt_excel.setContentAreaFilled(false);
		
		table=new JTable();		table.setFont(new Font(font, Font.BOLD, h3));
		scroll = new JScrollPane(table);
		
		l_search=new JLabel("검색");	l_search.setFont(new Font(font, Font.BOLD, h2));
		ch_search=new Choice();	ch_search.setFont(new Font(font, Font.BOLD, h3));
		t_search=new JTextField(20);
		bt_search=new JButton("검색");
		bt_search.setContentAreaFilled(false);
		
		//동쪽 부품
		p_east = new JPanel();
		p_east.setLayout(new BorderLayout());
		p_east2 = new JPanel();
		l_info=new JLabel("회원정보");
		l_info.setHorizontalAlignment(SwingConstants.CENTER);	l_info.setFont(new Font(font, Font.BOLD, h2));
		l_number=new JLabel("회원ID");	l_number.setFont(new Font(font, Font.PLAIN, h3));
		t_number=new JTextField();	t_number.setFont(new Font(font, Font.PLAIN, h3));
		l_name=new JLabel("회원이름");	l_name.setFont(new Font(font, Font.PLAIN, h3));
		t_name=new JTextField();	t_name.setFont(new Font(font, Font.PLAIN, h3));
		l_phon=new JLabel("연락처");	l_phon.setFont(new Font(font, Font.PLAIN, h3));
		t_phon=new JTextField();	t_phon.setFont(new Font(font, Font.PLAIN, h3));
		l_addres=new JLabel("주소");	l_addres.setFont(new Font(font, Font.PLAIN, h3));
		t_addres=new JTextField();	t_addres.setFont(new Font(font, Font.PLAIN, h3));
		l_email=new JLabel("이메일");	l_email.setFont(new Font(font, Font.PLAIN, h3));
		t_email=new JTextField();	t_email.setFont(new Font(font, Font.PLAIN, h3));
		l_state=new JLabel("회원상태");	l_state.setFont(new Font(font, Font.PLAIN, h3));
		ch_state=new Choice();	ch_state.setFont(new Font(font, Font.PLAIN, h3));
		l_rent=new JLabel("대출/가능권수");	l_rent.setFont(new Font(font, Font.PLAIN, h3));
		t_rent=new JTextField();	t_rent.setFont(new Font(font, Font.PLAIN, h3));
		l_reserve=new JLabel("연체일");	l_reserve.setFont(new Font(font, Font.PLAIN, h3));
		t_reserve=new JTextField();	t_reserve.setFont(new Font(font, Font.PLAIN, h3));
		l_birth=new JLabel("생년월일");	l_birth.setFont(new Font(font, Font.PLAIN, h3));
		ch_year=new Choice();	ch_year.setFont(new Font(font, Font.PLAIN, h3));
		ch_month=new Choice();	ch_month.setFont(new Font(font, Font.PLAIN, h3));
		ch_date=new Choice();	ch_date.setFont(new Font(font, Font.PLAIN, h3));
		
		eastflow=new JPanel();
		bt_join=new JButton("신규등록");	bt_join.setFont(new Font(font, Font.BOLD, h3));
		bt_join.setContentAreaFilled(false);
		bt_alter=new JButton("정보수정");	bt_alter.setFont(new Font(font, Font.BOLD, h3));
		bt_alter.setContentAreaFilled(false);
		bt_del=new JButton("회원삭제");	bt_del.setFont(new Font(font, Font.BOLD, h3));
		bt_del.setContentAreaFilled(false);
		p_bt=new JPanel();
		
		bt_revive=new JButton("정보복구");		bt_revive.setFont(new Font(font, Font.BOLD, h3));
		bt_revive.setContentAreaFilled(false);
		bt_delCommit=new JButton("회원정보 완전삭제");	bt_delCommit.setFont(new Font(font, Font.BOLD, h3));
		bt_delCommit.setContentAreaFilled(false);
		p_btDel=new JPanel();
		
		//서쪽 조립
		
		//table 관련 설정코드!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		table.setModel(model=new MemberTableModel());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setRowHeight(25);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				mem_method.inputEast();
			}
		});
		//"회원ID","회원이름","회원상태","생년월일","연락처","주소","이메일","대출권수","예약권수","회원등록일"
		table.getColumn("회원ID").setPreferredWidth(80);
		table.getColumn("회원이름").setPreferredWidth(80);
		table.getColumn("회원상태").setPreferredWidth(60);
		table.getColumn("생년월일").setPreferredWidth(80);
		table.getColumn("연락처").setPreferredWidth(120);
		table.getColumn("주소").setPreferredWidth(230);
		table.getColumn("이메일").setPreferredWidth(180);
		table.getColumn("대출권수").setPreferredWidth(60);
		table.getColumn("예약권수").setPreferredWidth(60);
		table.getColumn("회원등록일").setPreferredWidth(80);

		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();

		// 반복문을 이용하여 테이블을 가운데 정렬로 지정

		for (int i = 0; i < tcmSchedule.getColumnCount(); i++) {
			tcmSchedule.getColumn(i).setCellRenderer(tScheduleCellRenderer);
		}
		
		l_memList.setPreferredSize(new Dimension(100, 30));
		p_west1.add(l_memList);
		p_eastLabelCenter.setPreferredSize(new Dimension(350,30));
		p_west1.add(p_eastLabelCenter);
		//p_west1.add(bt_allSelect);
		//p_west1.add(bt_selectedDel);
		p_west1.add(bt_excel);
		scroll.setPreferredSize(new Dimension(650,420));
		p_west2.add(scroll);	
		p_west3.add(l_search);	
		p_west3.setPreferredSize(new Dimension(650,150));
		l_search.setPreferredSize(new Dimension(60,30));
		for(int i=0;i<model.columnTitle.length-3;i++) {
			ch_search.add(model.columnTitle[i]);
		}
		p_west3.add(ch_search);
		p_west3.add(t_search);	t_search.setPreferredSize(new Dimension(250,28));
		p_west3.add(bt_search);
		
		p_west.add(p_west1,BorderLayout.NORTH);
		p_west.add(p_west2);
		p_west.add(p_west3,BorderLayout.SOUTH);

		p_west.setSize(700,500);
		add(p_west,BorderLayout.WEST);
		
		//동쪽 조립
		Dimension l=new Dimension(130,35);
		Dimension t=new Dimension(250,25);
		
		p_east2.add(l_info);	l_info.setPreferredSize(new Dimension(300,60));
		p_east2.add(l_number);	l_number.setPreferredSize(new Dimension(l));
		p_east2.add(t_number);	t_number.setPreferredSize(new Dimension(t));	t_number.setEditable(false);
		p_east2.add(l_name);	l_name.setPreferredSize(new Dimension(l));
		p_east2.add(t_name);	t_name.setPreferredSize(new Dimension(t));
		
		//생년월일 관련
		p_east2.add(l_birth);	l_birth.setPreferredSize(new Dimension(l));
		p_east2.add(ch_year);	ch_year.setPreferredSize(new Dimension(120,25));
		p_east2.add(ch_month);	ch_month.setPreferredSize(new Dimension(60,25));
		p_east2.add(ch_date);	ch_date.setPreferredSize(new Dimension(60,25));
		
		p_east2.add(l_phon);	l_phon.setPreferredSize(new Dimension(l));
		p_east2.add(t_phon);	t_phon.setPreferredSize(new Dimension(t));
		p_east2.add(l_addres);	l_addres.setPreferredSize(new Dimension(l));
		p_east2.add(t_addres);	t_addres.setPreferredSize(new Dimension(t));
		p_east2.add(l_email);	l_email.setPreferredSize(new Dimension(l));
		p_east2.add(t_email);	t_email.setPreferredSize(new Dimension(t));
		p_east2.add(l_state);	l_state.setPreferredSize(new Dimension(l));
		
		p_east2.add(ch_state);	ch_state.setPreferredSize(new Dimension(t));
		
		p_east2.add(l_rent);	l_rent.setPreferredSize(new Dimension(l));
		p_east2.add(t_rent);	t_rent.setPreferredSize(new Dimension(t));	t_rent.setEditable(false);
		p_east2.add(l_reserve);	l_reserve.setPreferredSize(new Dimension(l));
		p_east2.add(t_reserve);	t_reserve.setPreferredSize(new Dimension(t));	t_reserve.setEditable(false);
		
		p_bt.add(bt_join);
		p_bt.add(bt_alter);
		p_bt.add(bt_del);
		
		p_btDel.add(bt_revive);
		p_btDel.add(bt_delCommit);
		
		p_east.add(eastflow, BorderLayout.NORTH);
		eastflow.add(p_bt);
		eastflow.add(p_btDel);	p_btDel.setVisible(false);
		
		p_east.add(p_east2, BorderLayout.CENTER);
		
		p_east2.setPreferredSize(new Dimension(MainFrame.WIDTH*1/3, MainFrame.HEIGHT-100));
		add(p_east,BorderLayout.EAST);
		
		p_center = new JPanel();
		p_center.setPreferredSize(new Dimension(50, MainFrame.HEIGHT-100));
		add(p_center);
		
		//메서드 모음
		mem_method=new MemMethod(this);
		mem_method.setChoices();
		
		//버튼과 이벤트 리스터 조립
		bt_excel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mem_method.MemberOutputExcel();
			}
		});
		
		bt_join.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//mem_method.insertMember();//이거 좀 고민해봐야겠는데
				new MemberRegistDialog(me);
			}
		});
		bt_alter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mem_method.updateMemberInfo();
			}
		});
		bt_del.addActionListener(new ActionListener() {//member_state ->4로 변경
			public void actionPerformed(ActionEvent e) {
				mem_method.updateMemberState(true);
			}
		});
		bt_revive.addActionListener(new ActionListener() {//member_state ->1로 변경
			public void actionPerformed(ActionEvent e) {
				mem_method.updateMemberState(false);
			}
		});
		
		t_search.addKeyListener(new KeyAdapter() {			
			public void keyReleased(KeyEvent e) {
				int key=e.getKeyCode();
				if(key==KeyEvent.VK_ENTER) {
					mem_method.search();
				}
			}
		});
		
		bt_search.addActionListener(new ActionListener() {//member_state ->1로 변경
			public void actionPerformed(ActionEvent e) {
				mem_method.search();
			}
		});
		
		setSize(MainFrame.WIDTH,MainFrame.HEIGHT-55);
		setVisible(true);
	}
	
	
}
