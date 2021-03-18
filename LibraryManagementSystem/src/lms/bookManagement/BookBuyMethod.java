package lms.bookManagement;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

public class BookBuyMethod {
	BookBuy buyMain;
	GregorianCalendar calendar;	//생년월일 입력을 위한 부품
	Object buyMain_id;	//최근에 선택해서 오른쪽에 값이 들어가있는 ID
	
	public BookBuyMethod(BookBuy buyMain) {
		this.buyMain=buyMain;
		calendar = new GregorianCalendar(Locale.KOREA);
	}
	
	//처음, 검색값 변경할 때마다 테이블 세팅하기
	public void setTable(int flag) {
		Connection con=buyMain.con;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		StringBuffer sb=new StringBuffer();
		
		sb.append("SELECT b.buy_id, g.genre, b.buy_bookname, p.publisher");
		sb.append(" ,b.buy_writer, b.buy_date, b.buy_ea, b.buy_price, b.buy_charge, s.buy_state");
		sb.append(" FROM lib_book_buy b, lib_buy_state s, lib_genre g, LIB_PUBLISHER p");
		sb.append(" WHERE b.genre=g.genre_id and b.publisher=p.PUBLISHER_ID and b.buy_state=s.buy_state_id");
		if(flag==1) {
			sb.append(" AND buy_date<='"+setdateRangeMax()+"' And buy_date>='"+setdateRangeMin()+"'");
		}else if(flag==2) {
			sb.append(" and buy_bookname like '%"+buyMain.t_search.getText()+"%'");
		}
		sb.append(" order by buy_id asc");
		//System.out.println(sb);
		
		try {
			pstmt=con.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=pstmt.executeQuery();
			
			rs.last();
			int total=rs.getRow();
			Object data[][]=new Object[total][buyMain.model.columnTitle.length];
			rs.beforeFirst();
			
			//3. 배열값 집어넣기
			for(int i=0;i<total;i++) {
				rs.next();
				data[i][0]=rs.getString("buy_id");
				data[i][1]=rs.getString("genre");
				data[i][2]=rs.getString("buy_bookname");
				data[i][3]=rs.getString("publisher");
				data[i][4]=rs.getString("buy_writer");
				data[i][5]=rs.getString("buy_date");
				data[i][6]=rs.getString("buy_ea");
				data[i][7]=rs.getString("buy_price");
				data[i][8]=rs.getString("buy_charge");
				data[i][9]=rs.getString("buy_state");
			}
			buyMain.model.data=data;
			buyMain.table.updateUI();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			buyMain.connectionManager.disconnect(pstmt,rs);
		}
		clearEast();
	}
	
	public String setdateRangeMax() {
		String y=buyMain.c_termYear.getSelectedItem();
		String m=buyMain.c_termMonth.getSelectedItem()+32;
		return y+m;
	}
	public String setdateRangeMin() {
		String y2=buyMain.c_termYear2.getSelectedItem();
		String m2=buyMain.c_termMonth2.getSelectedItem();
		return y2+m2;
	}
	
	//입력버튼 -> 이거 좀 문제 많음
	public void insertBt() {
		String name=buyMain.t_name.getText();
		if(name.length()==0) {
			JOptionPane.showMessageDialog(null, "도서명을 입력해주세요.");
			return;
		}
		String[] answer= {"예","아니오"};
		int result=JOptionPane.showOptionDialog(null, "구매도서를 등록하시겠습니까?", "구매도서등록", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, answer, null);
		if(result==1) {
			System.out.println("아니오 누르고 진행 안됨");
			return;
		}
		
		Connection con=buyMain.con;
		PreparedStatement pstmt=null;
		
		StringBuffer sb=new StringBuffer();
		String registDate=buyMain.c_buyYear.getSelectedItem()+buyMain.c_buyMonth.getSelectedItem()+buyMain.c_buyDate.getSelectedItem();
		
		sb.append("INSERT INTO LIB_BOOK_BUY(buy_id, genre, buy_bookname, publisher, buy_writer, buy_date, buy_ea, buy_price, buy_charge, buy_state)");
		sb.append("VALUES (buy_seq.nextval, "+setGenre()+",'"+buyMain.t_name.getText()+"', "+setPublisher()+",'"+buyMain.t_writer.getText()+"'");
		sb.append(" ,'"+registDate+"', "+buyMain.t_buyAmount.getText()+",'"+buyMain.t_sum.getText()+"','"+buyMain.t_officer.getText()+"',1)");
		
		try {
			pstmt=con.prepareStatement(sb.toString());
			int result2=pstmt.executeUpdate();
			if(result2==1) {
				JOptionPane.showMessageDialog(null, "입력이 완료되었습니다.");
			}else {
				JOptionPane.showMessageDialog(null, "입력에 실패했습니다.");
				return;
			}
			setTable(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			buyMain.connectionManager.disconnect(pstmt);
		}
		
	}
	
	//오른쪽 Choice에서 선택한 Genre의 db아이디값 반환
	public String setGenre() {
		Connection con=buyMain.con;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String genre=null;
		
		String sql="select genre_id from lib_genre where genre ='"+ buyMain.c_category.getSelectedItem()+"'";
		//System.out.println("입력할 때 장르값은 "+sql);
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next())genre=rs.getString("genre_id");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			buyMain.connectionManager.disconnect(pstmt,rs);
		}
		return genre;
	}
	
	//오른쪽 Choice에서 선택한 publisher의 db아이디값 반환
	public String setPublisher() {
		Connection con=buyMain.con;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String publisher=null;
		
		String sql="select publisher_id from lib_publisher where publisher ='"+ buyMain.t_publisher.getText()+"'";
		//System.out.println("입력할 때 출판사 값은 "+sql);
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				publisher=rs.getString("publisher_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			buyMain.connectionManager.disconnect(pstmt,rs);
		}
		return publisher;
	}
	
	//수정버튼
	public void updateBt() {
		String name=buyMain.t_name.getText();
		if(name.length()==0) {
			JOptionPane.showMessageDialog(null, "도서명을 입력해주세요.");
			return;
		}
		String[] answer= {"예","아니오"};
		int result=JOptionPane.showOptionDialog(null, "구매도서를 수정하시겠습니까?", "구매도서수정", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, answer, null);
		if(result==1) {
			System.out.println("아니오 누르고 진행 안됨");
			return;
		}
		Connection con=buyMain.con;
		PreparedStatement pstmt=null;
		
		StringBuffer sb=new StringBuffer();
		String registDate=buyMain.c_buyYear.getSelectedItem()+buyMain.c_buyMonth.getSelectedItem()+buyMain.c_buyDate.getSelectedItem();
		
		int buyState=0;
		switch(buyMain.c_condition.getSelectedIndex()){
			case 0:buyState=1;break;	//처리중
			case 1:buyState=2;break;	//처리완료
		}
		
		sb.append("UPDATE lib_book_buy ");
		sb.append(" set genre="+setGenre()+", buy_bookname='"+buyMain.t_name.getText()+"'");
		sb.append(" , publisher="+setPublisher()+", buy_writer='"+buyMain.t_writer.getText()+"'");
		sb.append(" , buy_date='"+registDate+"', buy_ea="+buyMain.t_buyAmount.getText()+"");
		sb.append(" , buy_price='"+buyMain.t_sum.getText()+"', buy_charge='"+buyMain.t_officer.getText()+"'");
		sb.append(" , buy_state="+buyState+"");
		sb.append(" where buy_id="+buyMain_id+"");

		try {
			pstmt=con.prepareStatement(sb.toString());
			int rusult2=pstmt.executeUpdate();
			if(rusult2==1) {
				JOptionPane.showMessageDialog(null, "수정이 완료되었습니다.");
			}else {
				JOptionPane.showMessageDialog(null, "수정에 실패했습니다.");
				return;
			}
			setTable(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			buyMain.connectionManager.disconnect(pstmt);
		}
	}
	
	//삭제버튼
	public void deleteBt() {
		String[] answer= {"예","아니오"};
		int result=JOptionPane.showOptionDialog(null, "구매도서를 삭제하시겠습니까?", "구매도서삭제", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, answer, null);
		if(result==1) {
			System.out.println("아니오 누르고 진행 안됨");
			return;
		}
		Connection con=buyMain.con;
		PreparedStatement pstmt=null;
		
		String sql="DELETE FROM lib_book_buy WHERE buy_id="+buyMain.t_num.getText();

		try {
			pstmt=con.prepareStatement(sql);
			int rusult2=pstmt.executeUpdate();
			if(rusult2==1) {
				JOptionPane.showMessageDialog(null, "삭제가 완료되었습니다.");
			}else {
				JOptionPane.showMessageDialog(null, "삭제에 실패했습니다.");
				return;
			}
			setTable(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			buyMain.connectionManager.disconnect(pstmt);
		}
	}
	
	//선택한 셀 내용 오른쪽 TextField에 입력하기
	public void inputEast() {
		//date 배열에서 값 가져와서 오른쪽에 삽입
		int row=buyMain.table.getSelectedRow();
		buyMain_id=buyMain.table.getValueAt(row, 0);	//이거 String이나 Int로 형변환?->오류난다
		//System.out.println("row값은"+row);
		//System.out.println("선택한 회원의 id는 "+buyMain_id);
		
		for(int i=0;i<buyMain.model.data.length;i++) {
			if(buyMain.model.data[i][0].equals(buyMain_id)) {
				buyMain.t_num.setText(buyMain.model.data[i][0].toString());
				
				for(int a=0;a<buyMain.c_category.getItemCount();a++) {
					if(buyMain.c_category.getItem(a).equals(buyMain.model.data[i][1])){
						buyMain.c_category.select(a);
					}
				}
		
				buyMain.t_name.setText(buyMain.model.data[i][2].toString());
				buyMain.t_publisher.setText(buyMain.model.data[i][3].toString());
				buyMain.t_writer.setText(buyMain.model.data[i][4].toString());
				
				
				String birth=buyMain.model.data[i][5].toString();
				String year=birth.substring(0,4);
				String month=birth.substring(4,6);
				String date=birth.substring(6,8);
				
				for(int a=0;a<buyMain.c_buyYear.getItemCount();a++) {
					if(buyMain.c_buyYear.getItem(a).equals(year)){
						buyMain.c_buyYear.select(a);
					}
				}
				for(int a=0;a<buyMain.c_buyMonth.getItemCount();a++) {
					if(buyMain.c_buyMonth.getItem(a).equals(month)){
						buyMain.c_buyMonth.select(a);
					}
				}
				for(int a=0;a<buyMain.c_buyDate.getItemCount();a++) {
					if(buyMain.c_buyDate.getItem(a).equals(date)){
						buyMain.c_buyDate.select(a);
					}
				}
		
				buyMain.t_buyAmount.setText(buyMain.model.data[i][6].toString());
				buyMain.t_sum.setText(buyMain.model.data[i][7].toString());
				buyMain.t_officer.setText(buyMain.model.data[i][8].toString());
				for(int a=0;a<buyMain.c_condition.getItemCount();a++) {	//4부분 메서드로 변경하기
					if(buyMain.c_condition.getItem(a).equals(buyMain.model.data[i][9])){
						buyMain.c_condition.select(a);
					}
				}
			}
		}
	}
	
	//오른쪽 TextField 내용 비우기
	public void clearEast() {
		buyMain.t_num.setText("");
		buyMain.c_category.select(0);
		buyMain.t_name.setText("");
		buyMain.t_publisher.setText("");
		buyMain.t_writer.setText("");
		buyMain.c_buyYear.select(0);
		buyMain.c_buyMonth.select(0);
		buyMain.c_buyDate.select(0);
		buyMain.t_buyAmount.setText("");
		buyMain.t_sum.setText("");
		buyMain.t_officer.setText("");
		buyMain.c_condition.select(0);
	}
	
	public void inputAlarm() {
		
	}
	
	//Birth의 year, month Choice 세팅
	public void setChoices() {
		for(int i = 0; i <100 ; i++) {
			buyMain.c_termYear.addItem(Integer.toString(calendar.getWeekYear()-i));
			buyMain.c_termYear2.addItem(Integer.toString(calendar.getWeekYear()-i));
			buyMain.c_buyYear.addItem(Integer.toString(calendar.getWeekYear()-i));
		}
		for(int i = 1; i <=12 ; i++) {
			if(i < 10) {
				buyMain.c_termMonth.addItem(0+Integer.toString(i));
				buyMain.c_termMonth2.addItem(0+Integer.toString(i));
				buyMain.c_buyMonth.addItem(0+Integer.toString(i));
			}
			else {
				buyMain.c_termMonth.addItem(Integer.toString(i));
				buyMain.c_termMonth2.addItem(Integer.toString(i));
				buyMain.c_buyMonth.addItem(0+Integer.toString(i));
			}
		}
		setDateChoice(calendar.getWeekYear(), 0);
		buyMain.c_buyYear.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int month = Integer.parseInt(buyMain.c_buyMonth.getSelectedItem());
				int year = Integer.parseInt(buyMain.c_buyYear.getSelectedItem());
				setDateChoice(year, month);
			}
		});
		buyMain.c_buyMonth.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int month = Integer.parseInt(buyMain.c_buyMonth.getSelectedItem());
				int year = Integer.parseInt(buyMain.c_buyYear.getSelectedItem());
				setDateChoice(year, month);
			}
		});
		setGenreCh();
	}
	//선택된 year, month Choice에 따라 date Choice 세팅		
		public void setDateChoice(int year, int month) {
			buyMain.c_buyDate.removeAll();
			calendar.set(year, month-1, 1);
			for(int i = 1; i<=calendar.getActualMaximum(calendar.DATE);i++) {
				if(i<10) {
					buyMain.c_buyDate.addItem(0+Integer.toString(i));
				}
				else {
					buyMain.c_buyDate.addItem(Integer.toString(i));
				}
			}
		}
	
	public void setGenreCh() {
		Connection con=buyMain.con;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql="SELECT genre FROM LIB_GENRE";
		
		try {
			pstmt=con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				buyMain.c_category.addItem(rs.getString("genre"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			buyMain.connectionManager.disconnect(pstmt,rs);
		}
	}
}
