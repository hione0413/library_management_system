package lms.member;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class MemMethod {
	MemberContents mem;
	Object mem_id;	//최근에 선택한 mem_id
	boolean panelflag;	//flag가 true이면 회원정보창, false이면 삭제된 회원정보창
	String lastSelect;	//가장 마지막에 날린 select 문 저장해두기 -> excel 로 출력할 때 쓸거임
	GregorianCalendar calendar;	//생년월일 입력을 위한 부품
	
	public MemMethod(MemberContents memberContents) {
		this.mem=memberContents;
		calendar = new GregorianCalendar(Locale.KOREA);
	}
	
	//Table 값 가져오는 메서드
	public void setTable(boolean flag) {	
		panelflag=flag;
		String state=null;
		if (flag==true) {state="1,2,3,5";}else {state="4";}
		
		Connection con=mem.con;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		StringBuffer sb=new StringBuffer();
		sb.append("select l.mem_id, l.MEM_NAME, s.mem_state, l.mem_birth, l.mem_phone");
		sb.append(" ,l.mem_addr, l.mem_email, l.mem_regist_date");
		sb.append(" from lib_member l, lib_member_state s");
		sb.append(" where l.mem_state IN ("+state+") and l.mem_state=s.mem_state_id");
		//System.out.println(sb);
		
		//ch_state 내용 채우기-여기서 채워야 에러 안남
		mem.ch_state.removeAll();
		if(panelflag==true) {
			mem.ch_state.add("정상");
			mem.ch_state.add("연체중");
			mem.ch_state.add("대출정지");
			mem.ch_state.add("관리자");
		}else {
			mem.ch_state.add("삭제대기");
		}
		
		try {
			//sql문 세팅
			//System.out.println("setTable에서 날린 sql select문 :"+sb.toString());
			lastSelect=sb.toString();
			pstmt=con.prepareStatement(sb.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=pstmt.executeQuery();
			
			//data 표 만들기
			//1. row 구하기
			rs.last();
			int total=rs.getRow();
			//2. 배열 만들기
			Object data[][]=new Object[total][mem.model.columnTitle.length];
			rs.beforeFirst();
			//3. 배열값 집어넣기
			
			for(int i=0;i<total;i++) {
				rs.next();
				data[i][0]=rs.getString("mem_id");
				//System.out.println("data[i][0]에 들어있는 값 :"+rs.getString("mem_id"));
				data[i][1]=rs.getString("mem_name");
				data[i][2]=rs.getString("mem_state");
				data[i][3]=rs.getString("mem_birth");
				data[i][4]=rs.getString("mem_phone");
				data[i][5]=rs.getString("mem_addr");
				data[i][6]=rs.getString("mem_email");
				data[i][7]=setRental(rs.getString("mem_id"));//mem_id에 들어간 값이 없어서 오류난거임
				//System.out.println("data[i][7] 값은 :"+data[i][7]);
				data[i][8]=null;
				data[i][9]=rs.getString("mem_regist_date");
			}
			mem.model.data=data;
			mem.table.updateUI();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			mem.connectionManager.disconnect(pstmt,rs);
		}
	}
	
	//선택한 셀 내용 오른쪽 TextField에 입력하기
	public void inputEast() {
		//date 배열에서 값 가져와서 오른쪽에 삽입
		int row=mem.table.getSelectedRow();
		mem_id=mem.table.getValueAt(row, 0);	//이거 String이나 Int로 형변환?->오류난다
		//System.out.println("row값은"+row);
		//System.out.println("선택한 회원의 id는 "+mem_id);
		
		for(int i=0;i<mem.model.data.length;i++) {
			if(mem.model.data[i][0].equals(mem_id)) {
				//System.out.println("이프문 되나 보는거");
				mem.t_number.setText(mem.model.data[i][0].toString());
				mem.t_name.setText(mem.model.data[i][1].toString());
				
				for(int a=0;a<mem.ch_state.getItemCount();a++) {	//4부분 메서드로 변경하기
					if(mem.ch_state.getItem(a).equals(mem.model.data[i][2])){
						mem.ch_state.select(a);
					}
				}
				//생일값 
				String birth=mem.model.data[i][3].toString();
				//System.out.println("이 사람의 생일은 : "+birth);
				String year=birth.substring(0,4);
				String month=birth.substring(4,6);
				String date=birth.substring(6,8);
				
				for(int a=0;a<mem.ch_year.getItemCount();a++) {
					if(mem.ch_year.getItem(a).equals(year)){
						mem.ch_year.select(a);
					}
				}
				for(int a=0;a<mem.ch_month.getItemCount();a++) {
					if(mem.ch_month.getItem(a).equals(month)){
						mem.ch_month.select(a);
					}
				}
				for(int a=0;a<mem.ch_date.getItemCount();a++) {
					if(mem.ch_date.getItem(a).equals(date)){
						mem.ch_date.select(a);
					}
				}
				
				mem.t_phon.setText(mem.model.data[i][4].toString());
				mem.t_addres.setText(mem.model.data[i][5].toString());
				mem.t_email.setText(mem.model.data[i][6].toString());
				mem.t_rent.setText(mem.model.data[i][7].toString());
			}
		}
	}
	
	public String setRental(String member_id) {
		//계획 - lib_rental_table 에서 member_id 숫자세기
		//select count(member_id) from lib_rental_table where member_id='Test1'
		Connection con=mem.con;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String rentalBookCount=null;
		String sql="select count(member_id) from lib_rental_table where member_id='"+member_id+"'";
		//System.out.println("Rental에서 찍히는 mem_id는"+member_id);
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			rs.next();
			String count=rs.getString("count(member_id)");
			
			//System.out.println("대여한 책은 :"+count+" 권입니다.");
			//mem.t_rent.setText(count+"/5");
			rentalBookCount=count+"/5";
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			mem.connectionManager.disconnect(pstmt,rs);
		}
		return rentalBookCount;
	}
	
	//멤버정보 수정 버튼과 연동된 메서드
	public void updateMemberInfo() {
		if(mem_id==null) {//선택한 멤버가 없는 경우
			JOptionPane.showMessageDialog(null, "정보를 수정할 회원을 선택해주세요.", "회원정보 수정", JOptionPane.INFORMATION_MESSAGE); 
			return;
		}
		String name=mem.t_name.getText();
		if(name.length()==0) {
			JOptionPane.showMessageDialog(null, "이름을 입력해주세요.");
			return;
		}
		String[] answer= {"예","아니오"};
		int result=JOptionPane.showOptionDialog(null, "회원 정보를 수정하시겠습니까?", "회원정보 수정", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, answer, null);
		if(result==1) {System.out.println("아니오 누르고 진행 안됨"); return;}
		//System.out.println("예를 누르고 진행됨");
		
		Connection con=mem.con;
		PreparedStatement pstmt=null;
		
		StringBuffer sb=new StringBuffer();
		sb.append("update lib_member");
		sb.append(" set mem_name=?, MEM_PHONE=?, MEM_ADDR=?, MEM_EMAIL=?, mem_state=?, mem_birth=?");
		sb.append(" where mem_id="+"'"+mem_id+"'");
		
		try {
			//System.out.println("mem.t_name의 값은 "+mem.t_name);
			
			pstmt=con.prepareStatement(sb.toString());
			pstmt.setString(1, mem.t_name.getText());
			pstmt.setString(2, mem.t_phon.getText());
			pstmt.setString(3, mem.t_addres.getText());
			pstmt.setString(4, mem.t_email.getText());
			int stateCh=mem.ch_state.getSelectedIndex();
			//System.out.println("수정할 때 오른쪽 창에서 선택한 Item은? : "+stateCh);
			pstmt.setInt(5, stateCh+1);
			
			String year=mem.ch_year.getSelectedItem();
			String month=mem.ch_month.getSelectedItem();
			String date=mem.ch_date.getSelectedItem();
			String birth=year+month+date;
			//System.out.println("수정할 이사람의 생일은 : "+birth);
			pstmt.setString(6, birth);
			//System.out.println("수정버튼 눌렀을 때 sql문 : "+sb);
			
			int result2=pstmt.executeUpdate();
			
			if(result2==1) {
				JOptionPane.showMessageDialog(null, "수정이 완료되었습니다.");
			}else {
				return;
			}
			
			clearEast();
			setTable(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mem.connectionManager.disconnect(pstmt);
		}
	}
	
	//멤버 삭제상태로 바꿨다가 복구했다가 하는 메서드
	public void updateMemberState(boolean flag) {//flag가 true이면 삭제, flase면 복구
		String q1=null;
		String q2=null;
		String q3=null;
		int state=0;
		
		if(flag) {
			q1="회원정보 삭제";	q2="삭제할 회원정보를 선택해주세요.";	q3="회원정보를 삭제하시겠습니까?";	state=4;
		}else {
			q1="회원정보 복구";	q2="복구할 회원정보를 선택해주세요.";	q3="회원정보를 복구하시겠습니까?";	state=1;
		}
		
		if(mem_id==null) {//선택한 멤버가 없는 경우
			JOptionPane.showMessageDialog(null, q2, q1, JOptionPane.INFORMATION_MESSAGE); 
			return;
		}
		
		String[] answer= {"예","아니오"};
		int result=JOptionPane.showOptionDialog(null, q3, q1, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, answer, null);
		if(result==1) {System.out.println("아니오 누르고 진행 안됨"); return;}
		//System.out.println("예를 누르고 진행됨");
		
		//쿼리문 입력
		Connection con=mem.con;
		PreparedStatement pstmt=null;
		
		String sql="update lib_member set mem_state=? where mem_id="+"'"+mem_id+"'";
		
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, state);
			int result2=pstmt.executeUpdate();
			if(result2==1) {
				if(flag) {
					JOptionPane.showMessageDialog(null, "삭제가 완료되었습니다.");
				}else {
					JOptionPane.showMessageDialog(null, "복구가 완료되었습니다.");
				}
			}else {
				return;
			}
			//System.out.println("삭제나 복구 결과 반환된 값은? "+result2);
			setTable(flag);	//질문 -> 여기서 mem.table.updateUI() 했는데 적용 안됨. 왜?
			clearEast();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mem.connectionManager.disconnect(pstmt);
		}
	}
	public void search() {
		//계획 -> ch_search 랑 칼럼 이름 비교해서 찾기 ->
		//수정계획 -> 회원ID, 회원상태, 생년월일 
		
		boolean searchflag=panelflag;//임시로 잠깐 쓰려고 만든 변수
		String state=null;
		if (searchflag==true) {state="1,2,3,5";}else {state="4";}
		
		Connection con=mem.con;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		//ch_search 가져오기
		String searchItem=mem.ch_search.getSelectedItem();
		String search=mem.t_search.getText();		
		String searchCol=null;	
		String idCh="mem_id";
		String nameCh="mem_name";
		String stateCh="s.mem_state";
		String birthCh="mem_birth";
		String phoneCh="mem_phone";
		String addrCh="mem_addr";
		String emailCh="mem_email";
		
		switch(searchItem){
			case "회원ID":searchCol=idCh;break;
			case "회원이름":searchCol=nameCh;break;
			case "회원상태":searchCol=stateCh;break;
			case "생년월일":searchCol=birthCh;break;
			case "연락처":searchCol=phoneCh;break;
			case "주소":searchCol=addrCh;break;
			case "이메일":searchCol=emailCh;break;
		}
		
		StringBuffer sb=new StringBuffer();
		sb.append("select l.mem_id, l.MEM_NAME, s.mem_state, l.mem_birth, l.mem_phone");
		sb.append(" ,l.mem_addr, l.mem_email, l.mem_regist_date");
		sb.append(" from lib_member l, lib_member_state s");
		sb.append(" where l.mem_state IN ("+state+") and l.mem_state=s.mem_state_id AND "+searchCol+" LIKE '%"+search+"%'");
		
		lastSelect=sb.toString();
		
		//System.out.println("검색에 뜨는 sql"+sb);
		try {
			//sql문 세팅
			pstmt=con.prepareStatement(sb.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=pstmt.executeQuery();
			
			//data 표 만들기
			//1. row 구하기
			rs.last();
			int total=rs.getRow();
			//2. 배열 만들기
			Object data[][]=new Object[total][mem.model.columnTitle.length];
			rs.beforeFirst();
			//3. 배열값 집어넣기
			
			for(int i=0;i<total;i++) {
				rs.next();
				data[i][0]=rs.getString("mem_id");
				data[i][1]=rs.getString("mem_name");
				data[i][2]=rs.getString("mem_state");
				data[i][3]=rs.getString("mem_birth");
				data[i][4]=rs.getString("mem_phone");
				data[i][5]=rs.getString("mem_addr");
				data[i][6]=rs.getString("mem_email");
				data[i][7]=null;
				data[i][8]=null;
				data[i][9]=rs.getString("mem_regist_date");
			}
			mem.model.data=data;
			mem.table.updateUI();
			mem.t_search.setText("");
			clearEast();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			mem.connectionManager.disconnect(pstmt,rs);
		}	
	}
	//오른쪽 TextField 내용 비우기
	public void clearEast() {
		mem.t_number.setText("");
		mem.t_name.setText("");
		mem.ch_state.select(0);
		mem.t_phon.setText("");
		mem.t_addres.setText("");
		mem.t_email.setText("");
		mem_id=null;
	}
	
	//bt_excel와 연결 : excel 출력하기 위한 메서드
	public void MemberOutputExcel(){
		if(lastSelect==null) {
			JOptionPane.showMessageDialog(null, "저장할 테이블 내용이 없습니다.");
			return;
		}
		//확장자 xls일 때만 저장됨.
		
		
		//메모리에 가상의 excel 세팅하기
		HSSFWorkbook workbook=new HSSFWorkbook();
		HSSFSheet sheet=workbook.createSheet("도서관 Member테이블");
		
		Connection con=mem.con;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			pstmt=con.prepareStatement(lastSelect, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs=pstmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			
			int columnCount=meta.getColumnCount();
			
			HSSFRow row=sheet.createRow(0);	//제일 꼭대기 Row 만들기
			//칼럼 만들기
			for(int i=1;i<columnCount;i++) {
				HSSFCell cell=row.createCell(i-1);
				cell.setCellValue(meta.getColumnName(i));
			}
			
			//레코드 채우기
			rs.last();
			int total=rs.getRow();
			rs.beforeFirst();
			
			for(int i=1;i<=total;i++) {
				//엑셀 한줄 만들기
				HSSFRow row2=sheet.createRow(i);
				rs.next();
				//만든 한줄 안에 내용 채워넣자
				for(int a=0;a<columnCount;a++) {
					HSSFCell cell=row2.createCell(a);
					cell.setCellValue(rs.getString(a+1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//만든 엑셀 파일로 내보내기
		
		//Chooser 세팅하기
		JFileChooser chooser=new JFileChooser("D:/java_data");
		chooser.setDialogTitle("새 이름으로 저장");
		chooser.setFileFilter(new FileNameExtensionFilter("확장자명 xls","xls"));
		
		//fileChooser 생성
		int saveResult=chooser.showSaveDialog(null);
		//System.out.println("saveResult 결과는 : "+saveResult);

		if(saveResult==JFileChooser.APPROVE_OPTION) {
			File file=chooser.getSelectedFile();
			
			//파일 확장자가 이미 xls인 경우 추가로 xls 를 안 붙여도 되게 만들기
			String fileName=file.getName();
			//System.out.println("엑셀에  내보낼 파일 세팅 전 이름은"+fileName);
			
			/*
			int fileIndex1=fileName.lastIndexOf(".");
			System.out.println("확장자 가르는 기준의 시작점은?"+fileIndex1);
			char test=fileName.charAt(6);
			System.out.println("이 파일명의 6번째 글자를 String으로 제대로 반환하고 있는가? : "+test);
			StringBuffer sb=new StringBuffer();
			sb.append(".");
			sb.append(fileName.valueOf(fileIndex1+1));
			sb.append(fileName.valueOf(fileIndex1+2));
			sb.append(fileName.valueOf(fileIndex1+3));
			System.out.println("이 파일의 이름 끝 4글자는 : "+sb);
			*/
			
			if(!fileName.endsWith(".xls")) {
				file=new File(chooser.getSelectedFile()+".xls");
			}
			
			//선택한 파일에 가상의 엑셀 데이터 쓰기
			try {
				workbook.write(file);
				System.out.println("파일을 저장하였습니다");//다이얼로그로 교체예정
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("저장실패");
			return;
		}
	}
	//Birth의 year, month Choice 세팅
	public void setChoices() {
		for(int i = 0; i <100 ; i++) {
			mem.ch_year.addItem(Integer.toString(calendar.getWeekYear()-i));
		}
		for(int i = 1; i <=12 ; i++) {
			if(i < 10) {
				mem.ch_month.addItem(0+Integer.toString(i));
			}
			else {
				mem.ch_month.addItem(Integer.toString(i));
			}
		}
		
		setDateChoice(calendar.getWeekYear(), 0);
		mem.ch_year.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int month = Integer.parseInt(mem.ch_month.getSelectedItem());
				int year = Integer.parseInt(mem.ch_year.getSelectedItem());
				setDateChoice(year, month);
			}
		});
		mem.ch_month.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int month = Integer.parseInt(mem.ch_month.getSelectedItem());
				int year = Integer.parseInt(mem.ch_year.getSelectedItem());
				setDateChoice(year, month);
			}
		});
	}
	
	//선택된 year, month Choice에 따라 date Choice 세팅		
	public void setDateChoice(int year, int month) {
		mem.ch_date.removeAll();
		calendar.set(year, month-1, 1);
		for(int i = 1; i<=calendar.getActualMaximum(calendar.DATE);i++) {
			if(i<10) {
				mem.ch_date.addItem(0+Integer.toString(i));
			}
			else {
				mem.ch_date.addItem(Integer.toString(i));
			}
		}
	}
}













