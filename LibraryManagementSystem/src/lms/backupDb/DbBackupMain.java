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
	JPanel p_north, p_center, p_l;	//디자인 간격용
	
	MainFrame main;
	JButton bookImp;
	JButton bookExp;
	
	ImageIcon impImage;
	ImageIcon expImage;
	
	GregorianCalendar calendar;
	String dbID="adams";
	String password="1234";
	String chooserDirectory="D:/java_data/DBbackupTest";	//데이터 백업or복구할 기본 디렉토리
	
	
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
		bookExp.setText(" DB 백업");
		
		impImage = new ImageIcon("res/dbImp.png");
		impImage.setImage(impImage.getImage().getScaledInstance(150, 150, Image.SCALE_REPLICATE));
		bookImp.setIcon(impImage);
		bookImp.setText(" DB 복구");
		
		Dimension d=new Dimension(350,200);
		String font="HY엽서L";
		
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
			int result2=JOptionPane.showConfirmDialog(null, "데이터베이스를 백업하시겠습니까?");
			if(result2==1 || result2==2) {return;}
			
			//저장할 파일명(현재 날짜) 설정
			String fileSavePath=setDirectory();
			if(fileSavePath==null) {
				JOptionPane.showMessageDialog(null, "백업이 취소되었습니다");
				return;
			}
			//cmd실행 후 명령어 입력하기
			String exp="exp userid="+dbID+"/"+password+" file='"+fileSavePath+".dmp'";
			exp+=" TABLES=(lib_book, lib_book_Detail, lib_book_request, lib_rental_Table, lib_member)";
			//백업이 필요한 관련 table들 : lib_book, lib_book_Detail, lib_book_request, lib_rental_Table, lib_member;
			//exp+=" log=? 질문1. 이건 뭐죠";
			
			String command = setCmd(exp);
			String result = runCmd(command);
			System.out.println("데이터 백업에서 입력된 cmd 명령어는 : "+command);
			
			JOptionPane.showMessageDialog(null, "백업이 완료되었습니다");
		});
		
		bookImp.addActionListener((e)->{
			int result2=JOptionPane.showConfirmDialog(null, "데이터베이스를 복구하시겠습니까?");
			if(result2==1 || result2==2) {return;}
			
			//1. 관련 Table 전부 Drop
			//질문2. Drop 안하고 테이블 안에 데이터만 복구 가능한지
			//질문3. 현재 백업 복구 명령어 이상한 부분 없는지

			//2. 복구 cmd문 날리기
			String fileImportPath=setImportFile();
			if(fileImportPath==null) {
				JOptionPane.showMessageDialog(null, "복구가 취소되었습니다");
				return;
			}else {
				dropAllTable();	//관련 Table 들 drop시키는 메서드. 취급주의
			}
			String imp="imp userid="+dbID+"/"+password+" full=y file='"+fileImportPath+"'";
			
			String command = setCmd(imp);
			String result = runCmd(command);
			System.out.println("데이터 백업에서 입력된 cmd 명령어는 : "+command);
			
			JOptionPane.showMessageDialog(null, "복구가 완료되었습니다");
			
		});
		
		setSize(800,800);
		setVisible(true);
	}

	//매개변수에 적힌 String을 cmd 명령어로 반환되는 메서드
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
	
	//저장할 디렉토리+파일 이름 설정
	public String setDirectory() {
		File savefile=null;
		String savepathname=null;
		String filename = setBackupFileName();  //파일이름 세팅

		JFileChooser chooser = new JFileChooser();// 객체 생성
		chooser.setCurrentDirectory(new File(chooserDirectory)); // 맨처음경로를 C로 함
		chooser.setFileSelectionMode(chooser.DIRECTORIES_ONLY); // 디렉토리만 선택가능

		int result = chooser.showSaveDialog(null);
		
		if (result == JFileChooser.APPROVE_OPTION) { //디렉토리를 선택했으면
			savefile = chooser.getSelectedFile(); //선택된 디렉토리 저장하고
			savepathname = savefile.getAbsolutePath() + "/" + filename;  //디렉토리결과+파일이름
			System.out.println(savepathname);
			return savepathname;
		}else{
			JOptionPane.showMessageDialog(null, "경로를 선택하지않았습니다.","경고", JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}
	
	//백업할 때 파일명 설정을 위한 날짜 가져오기 
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
		JFileChooser chooser = new JFileChooser();// 객체 생성
		chooser.setCurrentDirectory(new File(chooserDirectory)); // 맨처음경로를 C로 함
		String path=null;
		
		int result=chooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) { //디렉토리를 선택했으면
			path=chooser.getSelectedFile().getPath();
			return path;
		}else{
			JOptionPane.showMessageDialog(null, "파일을 선택하지않았습니다.","경고", JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}
	public void dropAllTable() {
		//아래 table들 drop 하고 복구해야됨
		//백업이 필요한 관련 table들 : lib_book, lib_book_Detail, lib_book_request, lib_rental_Table, lib_member;
		Connection con=main.getConn();
		PreparedStatement pstmt=null;
		
		//이런 식으로 못 씀-1. 하드코딩한다 / 2. 다른 방법을 찾는다
		
		String sql="drop table lib_book";
		String sq2="drop table lib_book_Detail";
		String sq3="drop table lib_book_request";
		String sq4="drop table lib_rental_Table";
		String sq5="drop table lib_member";
		
		System.out.println("drop문 적용되는지 보자 : "+sql);
		
		try {
			pstmt=con.prepareStatement(sql);
			int result1=pstmt.executeUpdate();
			if(result1==1) {
				System.out.println("drop문 적용되는지 보자 : "+sql);
			}
			
			pstmt=con.prepareStatement(sq2);
			int result2=pstmt.executeUpdate();
			if(result1==1) {
				System.out.println("drop문 적용되는지 보자 : "+sq2);
			}
			
			pstmt=con.prepareStatement(sq3);
			int result3=pstmt.executeUpdate();
			if(result1==1) {
				System.out.println("drop문 적용되는지 보자 : "+sq3);
			}
			
			pstmt=con.prepareStatement(sq4);
			int result4=pstmt.executeUpdate();
			if(result1==1) {
				System.out.println("drop문 적용되는지 보자 : "+sq4);
			}
			
			pstmt=con.prepareStatement(sq5);
			int result5=pstmt.executeUpdate();
			if(result1==1) {
				System.out.println("drop문 적용되는지 보자 : "+sq5);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.db.disconnect(pstmt);
		}
	}
}
