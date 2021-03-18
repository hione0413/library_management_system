package lms.bookManagement;

import javax.swing.table.AbstractTableModel;

public class BookBuyTableModel extends AbstractTableModel{
	String[] columnTitle= {"���Ź�ȣ","�帣","������","���ǻ�","����","����������","���ż���","�ݾ�","å����","ó������"};
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
}
