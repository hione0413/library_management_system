package lms.bookManagement;

import javax.swing.table.AbstractTableModel;

public class BookBuyTableModel extends AbstractTableModel{
	String[] columnTitle= {"구매번호","장르","도서명","출판사","저자","도서구매일","구매수량","금액","책임자","처리상태"};
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
