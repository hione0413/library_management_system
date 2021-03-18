package lms.book;

import javax.swing.table.AbstractTableModel;

public class RentalTableModel extends AbstractTableModel {

	String[] columnTitle = { "������ȣ", "������", "���ǻ�", "����" };
	// Object[][] data;
	Object[][] data_rental = {};

	public int getColumnCount() {
		return columnTitle.length;
	}

	public int getRowCount() {
		return data_rental.length;
	}

	public String getColumnName(int col) {
		return columnTitle[col];
	}

	public Object getValueAt(int row, int col) {
		return data_rental[row][col];
	}

}
