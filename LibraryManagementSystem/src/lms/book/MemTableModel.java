/*
 * JTable은 껍데기(View)에 불과하기 때문에 데이터(Model)를 제공해주는
 * 컨트롤러에 의존적이다!!
 */

package lms.book;

import javax.swing.table.AbstractTableModel;

public class MemTableModel extends AbstractTableModel {
	String[] columnTitle = { "회원ID", "회원이름", "회원상태", "생일", "연락처", "주소", "이메일"};
	Object[][] data = {};

	public int getColumnCount() {
		return columnTitle.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnTitle[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

}
