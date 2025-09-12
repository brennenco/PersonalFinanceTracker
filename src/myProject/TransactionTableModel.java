package myProject;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TransactionTableModel extends AbstractTableModel{
	
	private final String[] columnNames = {"Date", "Type", "Category", "Description", "Amount"};
	private List<Transaction> transactions;
	
	public TransactionTableModel(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	@Override
	public int getRowCount() {
		return transactions.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		Transaction t = transactions.get(row);
		return switch(col) {
			case 0 -> t.getDate();
			case 1 -> t.getType();
			case 2 -> t.getCategory();
			case 3 -> t.getDescription();
			case 4 -> t.getAmount();
			default -> null;
		};
	}
	
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
		fireTableDataChanged();
	}
	
	public Transaction getTransactionAt(int row) {
		return transactions.get(row);
	}
}
