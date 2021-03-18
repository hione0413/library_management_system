/*
 * JTable�� ������(View)�� �Ұ��ϱ� ������ ������(Model)�� �������ִ�
 * ��Ʈ�ѷ��� �������̴�!!
 */

package lms.book;

import javax.swing.table.AbstractTableModel;

public class LibTableModel extends AbstractTableModel {
	String[] columnTitle = { "������ȣ", "�帣", "������", "���ǻ�", "����", "���ϵ���" };
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
