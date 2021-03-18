/*
 * JTable은 껍데기(View)에 불과하기 때문에 데이터(Model)를 제공해주는
 * 컨트롤러에 의존적이다!!
 */

package lms.book;

import javax.swing.table.AbstractTableModel;

public class LibTableModel extends AbstractTableModel {
	String[] columnTitle = { "도서번호", "장르", "도서명", "출판사", "저자", "동일도서" };
	// Object[][] data;
	Object[][] data_lib = {};

	public int getColumnCount() {
		return columnTitle.length;
	}

	public int getRowCount() {
		return data_lib.length;
	}

	public String getColumnName(int col) {
		return columnTitle[col];
	}

	public Object getValueAt(int row, int col) {
		return data_lib[row][col];
	}

}
