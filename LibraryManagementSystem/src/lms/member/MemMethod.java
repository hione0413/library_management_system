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
	Object mem_id;	//�ֱٿ� ������ mem_id
	boolean panelflag;	//flag�� true�̸� ȸ������â, false�̸� ������ ȸ������â
	String lastSelect;	//���� �������� ���� select �� �����صα� -> excel �� ����� �� ������
	GregorianCalendar calendar;	//������� �Է��� ���� ��ǰ
	
	public MemMethod(MemberContents memberContents) {
		this.mem=memberContents;
		calendar = new GregorianCalendar(Locale.KOREA);
	}
	
	//Table �� �������� �޼���
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
		
		//ch_state ���� ä���-���⼭ ä���� ���� �ȳ�
		mem.ch_state.removeAll();
		if(panelflag==true) {
			mem.ch_state.add("����");
			mem.ch_state.add("��ü��");
			mem.ch_state.add("��������");
			mem.ch_state.add("������");
		}else {
			mem.ch_state.add("�������");
		}
		
		try {
			//sql�� ����
			//System.out.println("setTable���� ���� sql select�� :"+sb.toString());
			lastSelect=sb.toString();
			pstmt=con.prepareStatement(sb.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=pstmt.executeQuery();
			
			//data ǥ �����
			//1. row ���ϱ�
			rs.last();
			int total=rs.getRow();
			//2. �迭 �����
			Object data[][]=new Object[total][mem.model.columnTitle.length];
			rs.beforeFirst();
			//3. �迭�� ����ֱ�
			
			for(int i=0;i<total;i++) {
				rs.next();
				data[i][0]=rs.getString("mem_id");
				//System.out.println("data[i][0]�� ����ִ� �� :"+rs.getString("mem_id"));
				data[i][1]=rs.getString("mem_name");
				data[i][2]=rs.getString("mem_state");
				data[i][3]=rs.getString("mem_birth");
				data[i][4]=rs.getString("mem_phone");
				data[i][5]=rs.getString("mem_addr");
				data[i][6]=rs.getString("mem_email");
				data[i][7]=setRental(rs.getString("mem_id"));//mem_id�� �� ���� ��� ����������
				//System.out.println("data[i][7] ���� :"+data[i][7]);
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
	
	//������ �� ���� ������ TextField�� �Է��ϱ�
	public void inputEast() {
		//date �迭���� �� �����ͼ� �����ʿ� ����
		int row=mem.table.getSelectedRow();
		mem_id=mem.table.getValueAt(row, 0);	//�̰� String�̳� Int�� ����ȯ?->��������
		//System.out.println("row����"+row);
		//System.out.println("������ ȸ���� id�� "+mem_id);
		
		for(int i=0;i<mem.model.data.length;i++) {
			if(mem.model.data[i][0].equals(mem_id)) {
				//System.out.println("������ �ǳ� ���°�");
				mem.t_number.setText(mem.model.data[i][0].toString());
				mem.t_name.setText(mem.model.data[i][1].toString());
				
				for(int a=0;a<mem.ch_state.getItemCount();a++) {	//4�κ� �޼���� �����ϱ�
					if(mem.ch_state.getItem(a).equals(mem.model.data[i][2])){
						mem.ch_state.select(a);
					}
				}
				//���ϰ� 
				String birth=mem.model.data[i][3].toString();
				//System.out.println("�� ����� ������ : "+birth);
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
		//��ȹ - lib_rental_table ���� member_id ���ڼ���
		//select count(member_id) from lib_rental_table where member_id='Test1'
		Connection con=mem.con;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String rentalBookCount=null;
		String sql="select count(member_id) from lib_rental_table where member_id='"+member_id+"'";
		//System.out.println("Rental���� ������ mem_id��"+member_id);
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			rs.next();
			String count=rs.getString("count(member_id)");
			
			//System.out.println("�뿩�� å�� :"+count+" ���Դϴ�.");
			//mem.t_rent.setText(count+"/5");
			rentalBookCount=count+"/5";
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			mem.connectionManager.disconnect(pstmt,rs);
		}
		return rentalBookCount;
	}
	
	//������� ���� ��ư�� ������ �޼���
	public void updateMemberInfo() {
		if(mem_id==null) {//������ ����� ���� ���
			JOptionPane.showMessageDialog(null, "������ ������ ȸ���� �������ּ���.", "ȸ������ ����", JOptionPane.INFORMATION_MESSAGE); 
			return;
		}
		String name=mem.t_name.getText();
		if(name.length()==0) {
			JOptionPane.showMessageDialog(null, "�̸��� �Է����ּ���.");
			return;
		}
		String[] answer= {"��","�ƴϿ�"};
		int result=JOptionPane.showOptionDialog(null, "ȸ�� ������ �����Ͻðڽ��ϱ�?", "ȸ������ ����", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, answer, null);
		if(result==1) {System.out.println("�ƴϿ� ������ ���� �ȵ�"); return;}
		//System.out.println("���� ������ �����");
		
		Connection con=mem.con;
		PreparedStatement pstmt=null;
		
		StringBuffer sb=new StringBuffer();
		sb.append("update lib_member");
		sb.append(" set mem_name=?, MEM_PHONE=?, MEM_ADDR=?, MEM_EMAIL=?, mem_state=?, mem_birth=?");
		sb.append(" where mem_id="+"'"+mem_id+"'");
		
		try {
			//System.out.println("mem.t_name�� ���� "+mem.t_name);
			
			pstmt=con.prepareStatement(sb.toString());
			pstmt.setString(1, mem.t_name.getText());
			pstmt.setString(2, mem.t_phon.getText());
			pstmt.setString(3, mem.t_addres.getText());
			pstmt.setString(4, mem.t_email.getText());
			int stateCh=mem.ch_state.getSelectedIndex();
			//System.out.println("������ �� ������ â���� ������ Item��? : "+stateCh);
			pstmt.setInt(5, stateCh+1);
			
			String year=mem.ch_year.getSelectedItem();
			String month=mem.ch_month.getSelectedItem();
			String date=mem.ch_date.getSelectedItem();
			String birth=year+month+date;
			//System.out.println("������ �̻���� ������ : "+birth);
			pstmt.setString(6, birth);
			//System.out.println("������ư ������ �� sql�� : "+sb);
			
			int result2=pstmt.executeUpdate();
			
			if(result2==1) {
				JOptionPane.showMessageDialog(null, "������ �Ϸ�Ǿ����ϴ�.");
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
	
	//��� �������·� �ٲ�ٰ� �����ߴٰ� �ϴ� �޼���
	public void updateMemberState(boolean flag) {//flag�� true�̸� ����, flase�� ����
		String q1=null;
		String q2=null;
		String q3=null;
		int state=0;
		
		if(flag) {
			q1="ȸ������ ����";	q2="������ ȸ�������� �������ּ���.";	q3="ȸ�������� �����Ͻðڽ��ϱ�?";	state=4;
		}else {
			q1="ȸ������ ����";	q2="������ ȸ�������� �������ּ���.";	q3="ȸ�������� �����Ͻðڽ��ϱ�?";	state=1;
		}
		
		if(mem_id==null) {//������ ����� ���� ���
			JOptionPane.showMessageDialog(null, q2, q1, JOptionPane.INFORMATION_MESSAGE); 
			return;
		}
		
		String[] answer= {"��","�ƴϿ�"};
		int result=JOptionPane.showOptionDialog(null, q3, q1, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, answer, null);
		if(result==1) {System.out.println("�ƴϿ� ������ ���� �ȵ�"); return;}
		//System.out.println("���� ������ �����");
		
		//������ �Է�
		Connection con=mem.con;
		PreparedStatement pstmt=null;
		
		String sql="update lib_member set mem_state=? where mem_id="+"'"+mem_id+"'";
		
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, state);
			int result2=pstmt.executeUpdate();
			if(result2==1) {
				if(flag) {
					JOptionPane.showMessageDialog(null, "������ �Ϸ�Ǿ����ϴ�.");
				}else {
					JOptionPane.showMessageDialog(null, "������ �Ϸ�Ǿ����ϴ�.");
				}
			}else {
				return;
			}
			//System.out.println("������ ���� ��� ��ȯ�� ����? "+result2);
			setTable(flag);	//���� -> ���⼭ mem.table.updateUI() �ߴµ� ���� �ȵ�. ��?
			clearEast();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mem.connectionManager.disconnect(pstmt);
		}
	}
	public void search() {
		//��ȹ -> ch_search �� Į�� �̸� ���ؼ� ã�� ->
		//������ȹ -> ȸ��ID, ȸ������, ������� 
		
		boolean searchflag=panelflag;//�ӽ÷� ��� ������ ���� ����
		String state=null;
		if (searchflag==true) {state="1,2,3,5";}else {state="4";}
		
		Connection con=mem.con;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		//ch_search ��������
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
			case "ȸ��ID":searchCol=idCh;break;
			case "ȸ���̸�":searchCol=nameCh;break;
			case "ȸ������":searchCol=stateCh;break;
			case "�������":searchCol=birthCh;break;
			case "����ó":searchCol=phoneCh;break;
			case "�ּ�":searchCol=addrCh;break;
			case "�̸���":searchCol=emailCh;break;
		}
		
		StringBuffer sb=new StringBuffer();
		sb.append("select l.mem_id, l.MEM_NAME, s.mem_state, l.mem_birth, l.mem_phone");
		sb.append(" ,l.mem_addr, l.mem_email, l.mem_regist_date");
		sb.append(" from lib_member l, lib_member_state s");
		sb.append(" where l.mem_state IN ("+state+") and l.mem_state=s.mem_state_id AND "+searchCol+" LIKE '%"+search+"%'");
		
		lastSelect=sb.toString();
		
		//System.out.println("�˻��� �ߴ� sql"+sb);
		try {
			//sql�� ����
			pstmt=con.prepareStatement(sb.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=pstmt.executeQuery();
			
			//data ǥ �����
			//1. row ���ϱ�
			rs.last();
			int total=rs.getRow();
			//2. �迭 �����
			Object data[][]=new Object[total][mem.model.columnTitle.length];
			rs.beforeFirst();
			//3. �迭�� ����ֱ�
			
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
	//������ TextField ���� ����
	public void clearEast() {
		mem.t_number.setText("");
		mem.t_name.setText("");
		mem.ch_state.select(0);
		mem.t_phon.setText("");
		mem.t_addres.setText("");
		mem.t_email.setText("");
		mem_id=null;
	}
	
	//bt_excel�� ���� : excel ����ϱ� ���� �޼���
	public void MemberOutputExcel(){
		if(lastSelect==null) {
			JOptionPane.showMessageDialog(null, "������ ���̺� ������ �����ϴ�.");
			return;
		}
		//Ȯ���� xls�� ���� �����.
		
		
		//�޸𸮿� ������ excel �����ϱ�
		HSSFWorkbook workbook=new HSSFWorkbook();
		HSSFSheet sheet=workbook.createSheet("������ Member���̺�");
		
		Connection con=mem.con;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			pstmt=con.prepareStatement(lastSelect, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs=pstmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			
			int columnCount=meta.getColumnCount();
			
			HSSFRow row=sheet.createRow(0);	//���� ����� Row �����
			//Į�� �����
			for(int i=1;i<columnCount;i++) {
				HSSFCell cell=row.createCell(i-1);
				cell.setCellValue(meta.getColumnName(i));
			}
			
			//���ڵ� ä���
			rs.last();
			int total=rs.getRow();
			rs.beforeFirst();
			
			for(int i=1;i<=total;i++) {
				//���� ���� �����
				HSSFRow row2=sheet.createRow(i);
				rs.next();
				//���� ���� �ȿ� ���� ä������
				for(int a=0;a<columnCount;a++) {
					HSSFCell cell=row2.createCell(a);
					cell.setCellValue(rs.getString(a+1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//���� ���� ���Ϸ� ��������
		
		//Chooser �����ϱ�
		JFileChooser chooser=new JFileChooser("D:/java_data");
		chooser.setDialogTitle("�� �̸����� ����");
		chooser.setFileFilter(new FileNameExtensionFilter("Ȯ���ڸ� xls","xls"));
		
		//fileChooser ����
		int saveResult=chooser.showSaveDialog(null);
		//System.out.println("saveResult ����� : "+saveResult);

		if(saveResult==JFileChooser.APPROVE_OPTION) {
			File file=chooser.getSelectedFile();
			
			//���� Ȯ���ڰ� �̹� xls�� ��� �߰��� xls �� �� �ٿ��� �ǰ� �����
			String fileName=file.getName();
			//System.out.println("������  ������ ���� ���� �� �̸���"+fileName);
			
			/*
			int fileIndex1=fileName.lastIndexOf(".");
			System.out.println("Ȯ���� ������ ������ ��������?"+fileIndex1);
			char test=fileName.charAt(6);
			System.out.println("�� ���ϸ��� 6��° ���ڸ� String���� ����� ��ȯ�ϰ� �ִ°�? : "+test);
			StringBuffer sb=new StringBuffer();
			sb.append(".");
			sb.append(fileName.valueOf(fileIndex1+1));
			sb.append(fileName.valueOf(fileIndex1+2));
			sb.append(fileName.valueOf(fileIndex1+3));
			System.out.println("�� ������ �̸� �� 4���ڴ� : "+sb);
			*/
			
			if(!fileName.endsWith(".xls")) {
				file=new File(chooser.getSelectedFile()+".xls");
			}
			
			//������ ���Ͽ� ������ ���� ������ ����
			try {
				workbook.write(file);
				System.out.println("������ �����Ͽ����ϴ�");//���̾�α׷� ��ü����
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("�������");
			return;
		}
	}
	//Birth�� year, month Choice ����
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
	
	//���õ� year, month Choice�� ���� date Choice ����		
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













