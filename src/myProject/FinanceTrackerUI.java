package myProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FinanceTrackerUI extends JFrame {

    private FinanceManager manager;
    private JTable transactionTable;
    private TransactionTableModel tableModel;
    private JLabel totalIncomeLabel;

    public FinanceTrackerUI(FinanceManager manager) {
        this.manager = manager;


        setTitle("Personal Finance Tracker");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

    
        tableModel = new TransactionTableModel(manager.getAllTransactions());
        transactionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        add(scrollPane, BorderLayout.CENTER);

   
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));

        JButton addButton = new JButton("Add Transaction");
        JButton filterButton = new JButton("Filter Transactions");
        JButton deleteButton = new JButton("Delete Transactions");
        JButton exitButton = new JButton("Exit");

        panel.add(addButton);
        panel.add(filterButton);
        panel.add(deleteButton);
        panel.add(exitButton);

        add(panel, BorderLayout.EAST);
        
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalIncomeLabel = new JLabel("Total Income: $0.00");
        bottomPanel.add(totalIncomeLabel);
        add(bottomPanel, BorderLayout.SOUTH);

    
        addButton.addActionListener(e -> addTransaction());
        filterButton.addActionListener(e -> filterTransaction());
        deleteButton.addActionListener(e -> deleteTransaction());
        exitButton.addActionListener(e -> {
            manager.saveToFile("transactions.csv");
            System.exit(0);
        });
        
        updateTotalIncome();

        setVisible(true);
    }
    
    private void updateTotalIncome() {
        double totalIncome = manager.getNetBalance();
        totalIncomeLabel.setText("Total Income: $" + String.format("%.2f", totalIncome));
    }

   
    private void addTransaction() {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        JTextField dateField = new JTextField("2025-08-23"); // default
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"income", "expense"});
        JTextField categoryField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField amountField = new JTextField();

        formPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Type:"));
        formPanel.add(typeBox);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);
        formPanel.add(new JLabel("Amount:"));
        formPanel.add(amountField);

        int result = JOptionPane.showConfirmDialog(
                this, formPanel, "Add Transaction", JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                String date = dateField.getText().trim();
                String type = typeBox.getSelectedItem().toString();
                String category = categoryField.getText().trim();
                String description = descriptionField.getText().trim();
                double amount = Double.parseDouble(amountField.getText().trim());

                Transaction newTransaction = new Transaction(date, type, category, description, amount);
                manager.addTransaction(newTransaction);

                refreshTable();
                
                JOptionPane.showMessageDialog(this, "Transaction added successfully!");
                updateTotalIncome();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Amount must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void refreshTable() {
        tableModel.setTransactions(manager.getAllTransactions());
        updateTotalIncome();
    }

  
    private void deleteTransaction() {
    	  int selectedRow = transactionTable.getSelectedRow();

    	    if (selectedRow == -1) {
    	        JOptionPane.showMessageDialog(this, "Please select a transaction to delete.");
    	        return;
    	    }

    	  
    	    int confirm = JOptionPane.showConfirmDialog(
    	            this,
    	            "Are you sure you want to delete this transaction?",
    	            "Confirm Delete",
    	            JOptionPane.YES_NO_OPTION
    	    );

    	    if (confirm == JOptionPane.YES_OPTION) {
    	   
    	        Transaction toDelete = tableModel.getTransactionAt(selectedRow);

    	 
    	        manager.getAllTransactions().remove(toDelete);

    	        // Refresh the table
    	        refreshTable();

    	        JOptionPane.showMessageDialog(this, "Transaction deleted successfully.");
    	    }
    }


    private void filterTransaction() {
    	 JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

    	    String[] types = {"All", "Income", "Expense"};
    	    JComboBox<String> typeBox = new JComboBox<>(types);

    	   
    	    JTextField categoryField = new JTextField();

    	    panel.add(new JLabel("Type:"));
    	    panel.add(typeBox);
    	    panel.add(new JLabel("Category (leave blank for all):"));
    	    panel.add(categoryField);

    	    int result = JOptionPane.showConfirmDialog(
    	            this,
    	            panel,
    	            "Filter Transactions",
    	            JOptionPane.OK_CANCEL_OPTION
    	    );

    	    if (result == JOptionPane.OK_OPTION) {
    	        String selectedType = (String) typeBox.getSelectedItem();
    	        String categoryText = categoryField.getText().trim();
    	        
    	        ArrayList<Transaction> filtered = new ArrayList<>(manager.getAllTransactions());

    	        
    	        if (!selectedType.equals("All")) {
    	            filtered.removeIf(t -> !t.getType().equalsIgnoreCase(selectedType));
    	        }

    	       
    	        if (!categoryText.isEmpty()) {
    	            filtered.removeIf(t -> !t.getCategory().equalsIgnoreCase(categoryText));
    	        }

    	  
    	        tableModel.setTransactions(filtered);
    	    }
    	    updateTotalIncome();
    }

    
    public static void main(String[] args) {
        FinanceManager manager = new FinanceManager();
        manager.loadFromFile("transactions.csv");
        new FinanceTrackerUI(manager);
    }
}