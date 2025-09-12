package myProject;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MainClass {

	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		FinanceManager manager = new FinanceManager();
		String defaultFile = "transactions.csv";
		
		manager.loadFromFile(defaultFile);
		
		boolean running = true;
		
		while(running) {
			System.out.println("\n*** Personal Finance Tracker ***");
			System.out.println("1. Add Transaction");
			System.out.println("2. View All Transactions");
			System.out.println("3. View Total income and Expenses");
			System.out.println("4. Delete a Transaction");
			System.out.println("5. Filter Transactions by Type");
			System.out.println("6. Filter Transactions by Category");
			System.out.println("7. Exit");
			System.out.print("Choose an option: ");
			
			int choice;
			try {
				choice = Integer.parseInt(in.nextLine());
			}catch(NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.");
				continue;
			}
			
			switch(choice) {
				case 1 -> { //adds transaction
					String date = getValidDate(in);
					
					String type = getValidType(in);
					
					System.out.print("Category: ");
					String category = in.nextLine();
					
					System.out.print("Description: ");
					String description = in.nextLine();
					
					double amount = getValidAmount(in);
					
					Transaction newTransaction = new Transaction(date, type, category, description, amount);
					manager.addTransaction(newTransaction);
					System.out.println("Transaction added.");
				}
				case 2 -> { //views all transactions
					List<Transaction> all = manager.getAllTransactions();
					if(all.isEmpty()) {
						System.out.println("No transactions found.");
					}else {
						printTransactions(all);
					}
				}
				case 3 -> { //views total income and expenses
					System.out.printf("Total Income: $%.2f%n", manager.getTotalIncome());
					System.out.printf("Total Expenses: $%.2f%n", manager.getTotalExpenses());
					System.out.printf("Net Balance: $%.2f%n", manager.getNetBalance());
				}
				case 4 -> { //delete a transaction
					List<Transaction> all = manager.getAllTransactions();
					
					if(all.isEmpty()) {
						System.out.println("No transactions to delete:");
						break;
					}
					
					printTransactionsWithIndex(all);
					
					System.out.print("Enter the number of the transaction to delete: ");
					int index;
					try {
						index = Integer.parseInt(in.nextLine()) - 1;
					}catch(NumberFormatException e){
						System.out.println("Invalid number.");
						break;
					}
					
					if(index < 0 || index >= all.size()) {
						System.out.println("Invalid number.");
					}else {
						manager.deleteTransaction(index);
						System.out.println("Transaction deleted.");
					}
				}
				case 5 ->{ //filters by type
					System.out.print("Enter type to filter by (income/expense): ");
					String type = in.nextLine().toLowerCase();
					List<Transaction> filtered = manager.filterByType(type);
					if(filtered.isEmpty()) {
						System.out.println("No transactions of type: " + type);
					}else {
						printTransactions(filtered);
					}
				}
				case 6 ->{ //filters by category
					System.out.print("Enter category to filter by: ");
					String category = in.nextLine();
					List<Transaction> filtered = manager.filterByCategory(category);
					if(filtered.isEmpty()) {
						System.out.println("No transactions in category: " + category);
					}else {
						printTransactions(filtered);
					}
				}
				case 7 ->{
					running = false;
				}
				default -> {
					System.out.println("Invalid option. Please try again.");
				}
			}
			
		}
		manager.saveToFile(defaultFile);
		System.out.println("GoodBye!");
		in.close();
	}
	
	private static void printTransactions(List<Transaction> list) {
		System.out.printf("%-12s %-8s %-15s %-20s %s%n", "Date", "Type", "Category", "Description", "Amount");
		for(Transaction element : list) {
			System.out.println(element);
		}
	}
	
	private static void printTransactionsWithIndex(List<Transaction> list) {
        System.out.printf("%-5s %-12s %-8s %-15s %-20s %s%n", "No.", "Date", "Type", "Category", "Description", "Amount");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%-5d %s%n", i + 1, list.get(i));
        }
	}
	
	private static String getValidDate(Scanner scanner) {
		while(true) {
			System.out.print("Enter date (yyyy-mm-dd): ");
			String input = scanner.nextLine();
			try {
				LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				return input;
			}catch(DateTimeParseException e) {
				System.out.println("Invalid date format. Please use yyyy-mm-dd");
			}
		}
	}
	
	private static String getValidType(Scanner scanner) {
		while(true) {
			System.out.print("Enter type (income/expense): ");
			String input = scanner.nextLine().trim().toLowerCase();
			if(input.equals("income") || input.equals("expense")) {
				return input;
			}
			System.out.println("Invalid type. Please enter 'income' or 'expense'.");
		}
	}
	
	private static double getValidAmount(Scanner scanner) {
		while(true) {
			System.out.print("Enter amount: ");
			String input = scanner.nextLine();
			try {
				double value = Double.parseDouble(input);
				if(value >= 0) {
					return value;
				}
				else {
					System.out.println("Amount must be positive. Try again");
				}
			}catch(NumberFormatException e) {
				
				System.out.println("Invalid amount. Please Try again");
			}
		}
	}

}
