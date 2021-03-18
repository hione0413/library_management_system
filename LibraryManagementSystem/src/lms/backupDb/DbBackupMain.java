package lms.backupDb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import lms.MainFrame;

public class DbBackupMain extends JPanel{
	JPanel p_north, p_center, p_l;	//������ ���ݿ�
	
	MainFrame main;
	JButton bookImp;
	JButton bookExp;
	
	ImageIcon impImage;
	ImageIcon expImage;
	
	GregorianCalendar calendar;
	String dbID="adams";
	String password="1234";
	String chooserDirectory="D:/java_data/DBbackupTest";	//������ ���or������ �⺻ ���丮
	
	
	public DbBackupMain(MainFrame main) {
		this.main=main;
		calendar = new GregorianCalendar(Locale.KOREA);
		
		p_north = new JPanel();
		p_center = new JPanel();
		p_l = new JPanel();
		
		bookExp=new JButton();	bookExp.setContentAreaFilled(false);
		bookImp=new JButton();	bookImp.setContentAreaFilled(false);
		
		expImage = new ImageIcon("res/dbexp.jpg");
		expImage.setImage(expImage.getImage().getScaledInstance(150, 150, Image.SCALE_REPLICATE));
		bookExp.setIcon(expImage);
		bookExp.setText(" DB ���");
		
		impImage = new ImageIcon("res/dbImp.png");
		impImage.setImage(impImage.getImage().getScaledInstance(150, 150, Image.SCALE_REPLICATE));
		bookImp.setIcon(impImage);
		bookImp.setText(" DB ����");
		
		Dimension d=new Dimension(350,200);
		String font="HY����L";
		
		bookExp.setPreferredSize(d);
		bookExp.setFont(new Font(font, Font.BOLD, 30));
		bookExp.setHorizontalAlignment(SwingConstants.CENTER);
		//bookExp.setBorder(BorderFactory.createLineBorder(Color.gray));
		
		bookImp.setPreferredSize(d);
		bookImp.setFont(new Font(font, Font.BOLD, 30));
		bookImp.setHorizontalAlignment(SwingConstants.CENTER);
		//bookImp.setBorder(BorderFactory.createLineBorder(Color.gray));
		
		p_north.setPreferredSize(new Dimension(1200,120));	//p_north.setBackground(Color.RED);
		p_l.setPreferredSize(new Dimension(100,300));	//p_l.setBackground(Color.blue);
			
		setLayout(new BorderLayout());
		add(p_north, BorderLayout.NORTH);
		p_center.add(bookExp);
		p_center.add(p_l);
		p_center.add(bookImp);
		add(p_center, BorderLayout.CENTER);
		
		bookExp.addActionListener((e)->{
			int result2=JOptionPane.showConfirmDialog(null, "�����ͺ��̽��� ����Ͻðڽ��ϱ�?");
			if(result2==1 || result2==2) {return;}
			
			//������ ���ϸ�(���� ��¥) ����
			String fileSavePath=setDirectory();
			if(fileSavePath==null) {
				JOptionPane.showMessageDialog(null, "����� ��ҵǾ����ϴ�");
				return;
			}
			//cmd���� �� ��ɾ� �Է��ϱ�
			String exp="exp userid="+dbID+"/"+password+" file='"+fileSavePath+".dmp'";
			exp+=" TABLES=(lib_book, lib_book_Detail, lib_book_request, lib_rental_Table, lib_member)";
			//����� �ʿ��� ���� table�� : lib_book, lib_book_Detail, lib_book_request, lib_rental_Table, lib_member;
			//exp+=" log=? ����1. �̰� ����";
			
			String command = setCmd(exp);
			String result = runCmd(command);
			System.out.println("������ ������� �Էµ� cmd ��ɾ�� : "+command);
			
			JOptionPane.showMessageDialog(null, "����� �Ϸ�Ǿ����ϴ�");
		});
		
		bookImp.addActionListener((e)->{
			int result2=JOptionPane.showConfirmDialog(null, "�����ͺ��̽��� �����Ͻðڽ��ϱ�?");
			if(result2==1 || result2==2) {return;}
			
			//1. ���� Table ���� Drop
			//����2. Drop ���ϰ� ���̺� �ȿ� �����͸� ���� ��������
			//����3. ���� ��� ���� ��ɾ� �̻��� �κ� ������

			//2. ���� cmd�� ������
			String fileImportPath=setImportFile();
			if(fileImportPath==null) {
				JOptionPane.showMessageDialog(null, "������ ��ҵǾ����ϴ�");
				return;
			}else {
				dropAllTable();	//���� Table �� drop��Ű�� �޼���. �������
			}
			String imp="imp userid="+dbID+"/"+password+" full=y file='"+fileImportPath+"'";
			
			String command = setCmd(imp);
			String result = runCmd(command);
			System.out.println("������ ������� �Էµ� cmd ��ɾ�� : "+command);
			
			JOptionPane.showMessageDialog(null, "������ �Ϸ�Ǿ����ϴ�");
			
		});
		
		setSize(800,800);
		setVisible(true);
	}

	//�Ű������� ���� String�� cmd ��ɾ�� ��ȯ�Ǵ� �޼���
	public String setCmd(String cmd) {
		StringBuffer sb=new StringBuffer();
		sb.append("cmd.exe ");
		sb.append("/c ");
		sb.append(cmd);
		
		return sb.toString();
	}
	
	public String runCmd(String cmd) {
		try {
			Process process=Runtime.getRuntime().exec(cmd);
			BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
		
			String line=null;
			StringBuffer sb=new StringBuffer();
			
			while((line=bufferedReader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
	
	//������ ���丮+���� �̸� ����
	public String setDirectory() {
		File savefile=null;
		String savepathname=null;
		String filename = setBackupFileName();  //�����̸� ����

		JFileChooser chooser = new JFileChooser();// ��ü ����
		chooser.setCurrentDirectory(new File(chooserDirectory)); // ��ó����θ� C�� ��
		chooser.setFileSelectionMode(chooser.DIRECTORIES_ONLY); // ���丮�� ���ð���

		int result = chooser.showSaveDialog(null);
		
		if (result == JFileChooser.APPROVE_OPTION) { //���丮�� ����������
			savefile = chooser.getSelectedFile(); //���õ� ���丮 �����ϰ�
			savepathname = savefile.getAbsolutePath() + "/" + filename;  //���丮���+�����̸�
			System.out.println(savepathname);
			return savepathname;
		}else{
			JOptionPane.showMessageDialog(null, "��θ� ���������ʾҽ��ϴ�.","���", JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}
	
	//����� �� ���ϸ� ������ ���� ��¥ �������� 
	public String setBackupFileName() {
		int c=calendar.get(Calendar.YEAR);
		int c1=calendar.get(Calendar.MONTH)+1;
		int c2=calendar.get(Calendar.DATE);
		int c3=calendar.get(Calendar.HOUR);
		int c4=calendar.get(Calendar.MINUTE);
		int c5=calendar.get(Calendar.SECOND);
		int c6=calendar.get(Calendar.MILLISECOND);
		
		ArrayList c_list=new ArrayList();
		c_list.add(c1);
		c_list.add(c2);
		c_list.add(c3);
		c_list.add(c4);
		c_list.add(c5);
		
		StringBuffer sb=new StringBuffer();
		
		sb.append(c);
		
		for(int i=0;i<c_list.size();i++) {
			int c_l=(int)(Math.log10((int)c_list.get(i))+1);
			if(c_l==1) {
				sb.append("0"+(int)c_list.get(i));
			}else {
				sb.append((int)c_list.get(i));
			}
		}
		sb.append(6);
		return sb.toString();
	}
	public String setImportFile() {
		JFileChooser chooser = new JFileChooser();// ��ü ����
		chooser.setCurrentDirectory(new File(chooserDirectory)); // ��ó����θ� C�� ��
		String path=null;
		
		int result=chooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) { //���丮�� ����������
			path=chooser.getSelectedFile().getPath();
			return path;
		}else{
			JOptionPane.showMessageDialog(null, "������ ���������ʾҽ��ϴ�.","���", JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}
	public void dropAllTable() {
		//�Ʒ� table�� drop �ϰ� �����ؾߵ�
		//����� �ʿ��� ���� table�� : lib_book, lib_book_Detail, lib_book_request, lib_rental_Table, lib_member;
		Connection con=main.getConn();
		PreparedStatement pstmt=null;
		
		//�̷� ������ �� ��-1. �ϵ��ڵ��Ѵ� / 2. �ٸ� ����� ã�´�
		
		String sql="drop table lib_book";
		String sq2="drop table lib_book_Detail";
		String sq3="drop table lib_book_request";
		String sq4="drop table lib_rental_Table";
		String sq5="drop table lib_member";
		
		System.out.println("drop�� ����Ǵ��� ���� : "+sql);
		
		try {
			pstmt=con.prepareStatement(sql);
			int result1=pstmt.executeUpdate();
			if(result1==1) {
				System.out.println("drop�� ����Ǵ��� ���� : "+sql);
			}
			
			pstmt=con.prepareStatement(sq2);
			int result2=pstmt.executeUpdate();
			if(result1==1) {
				System.out.println("drop�� ����Ǵ��� ���� : "+sq2);
			}
			
			pstmt=con.prepareStatement(sq3);
			int result3=pstmt.executeUpdate();
			if(result1==1) {
				System.out.println("drop�� ����Ǵ��� ���� : "+sq3);
			}
			
			pstmt=con.prepareStatement(sq4);
			int result4=pstmt.executeUpdate();
			if(result1==1) {
				System.out.println("drop�� ����Ǵ��� ���� : "+sq4);
			}
			
			pstmt=con.prepareStatement(sq5);
			int result5=pstmt.executeUpdate();
			if(result1==1) {
				System.out.println("drop�� ����Ǵ��� ���� : "+sq5);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.db.disconnect(pstmt);
		}
	}
}
