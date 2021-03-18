package lms.member;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import lms.MainFrame;

public class MemberMain extends JPanel{	
	
	//���� ��ư �δ� �г�
	JPanel p_north, p_center;	
	JButton bt_memList,bt_memDel;
	JPanel[] contents = new JPanel[2];
	MemberContents memcontents;
	public MemberMain() {
		setLayout(new BorderLayout());
		memcontents = new MemberContents();
		String font="HY����L";
		int h1=20;
		
		//���� ��ǰTest
		p_north = new JPanel();
		p_center = new JPanel(); 
		bt_memList=new JButton("ȸ������");
		bt_memList.setFont(new Font(font, Font.BOLD, h1));
		bt_memList.setContentAreaFilled(false);
		bt_memDel=new JButton("������ ȸ������");
		bt_memDel.setFont(new Font(font, Font.BOLD, h1));
		bt_memDel.setContentAreaFilled(false);
		
		//���� ���� - ������ ������� ���߿� ����
		p_north.add(bt_memList);
		p_north.add(bt_memDel);
		//p_north.setSize(MainFrame.WIDTH,50);
		p_north.setPreferredSize(new Dimension(700, 45));
		p_north.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		add(p_north,BorderLayout.NORTH);
		p_center.add(memcontents);
		add(p_center);
		
		
		bt_memList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//memcontents.setVisible(true);
				//ȸ������ ��ư ������ ȸ����� ���
				memcontents.p_bt.setVisible(true);
				memcontents.p_btDel.setVisible(false);
				
				memcontents.mem_method.setTable(true);
				memcontents.mem_method.clearEast();
			}
		});

		bt_memDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//memcontents.setVisible(false);
				memcontents.p_bt.setVisible(false);
				memcontents.p_btDel.setVisible(true);
				
				memcontents.mem_method.setTable(false);
				memcontents.mem_method.clearEast();
			}
		});
		
		setSize(MainFrame.WIDTH,MainFrame.HEIGHT-55);
		setVisible(true);		
	}
}
