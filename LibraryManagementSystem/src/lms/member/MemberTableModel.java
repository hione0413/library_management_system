package lms.member;

import javax.swing.table.AbstractTableModel;

public class MemberTableModel extends AbstractTableModel{
	String[] columnTitle= {"ȸ��ID","ȸ���̸�","ȸ������","�������","����ó","�ּ�","�̸���","����Ǽ�","����Ǽ�","ȸ�������"};
	Object[][] data= {};
	
	public int getColumnCount() {
		return columnTitle.length;
	}
	public String getColumnName(int col) {
		return columnTitle[col];
	}
	
	public int getRowCount() {
		return data.length;
	}
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
	public void setValueAt(Object obj, int row, int col) {
		if(col==0) {
			data[row][col]=obj;
		}
	}
	/*
	public Class getColumnClass(int col) {
		// TODO Auto-generated method stub
		return getValueAt(0, col).getClass();
	}*/
	/*public boolean isCellEditable(int row, int col) {
		if(col==0) {return true;}else {return false;}
	}*/
}
