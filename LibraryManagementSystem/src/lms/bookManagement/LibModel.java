package lms.bookManagement;

import javax.swing.table.AbstractTableModel;

public class LibModel extends AbstractTableModel{
	String[] columnName;
	Object[][] data;
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnName.length;
	}
	@Override
	public String getColumnName(int col) {
		// TODO Auto-generated method stub
		return columnName[col];
	}
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return data.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		return data[row][col];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
