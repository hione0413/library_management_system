/*
 * JTable�� ������(View)�� �Ұ��ϱ� ������ ������(Model)�� �������ִ�
 * ��Ʈ�ѷ��� �������̴�!!
 */

package lms.book;

import javax.swing.table.AbstractTableModel;

public class MemTableModel extends AbstractTableModel {
	String[] columnTitle = { "ȸ��ID", "ȸ���̸�", "ȸ������", "����", "����ó", "�ּ�", "�̸���"};
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
