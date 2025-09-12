package myProject;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FinanceManager {
	
	private List<Transaction> transactions;
	
	public FinanceManager()
	{	
		transactions = new ArrayList<>();
	}
	
	public void addTransaction(Transaction t) {
		transactions.add(t);
	}
	
	//gets all transactions
	public List<Transaction> getAllTransactions(){
		return transactions;
	}
	
	//filters by type
	public List<Transaction> filterByType(String type){
		List<Transaction> filtered = new ArrayList<>();
		for(Transaction element : transactions) {
			if(element.getType().equalsIgnoreCase(type)) {
				filtered.add(element);
			}
		}
		return filtered;
	}
	
	//filters by category
	public List<Transaction> filterByCategory(String category){
		List<Transaction> filtered = new ArrayList<>();
		for(Transaction element : transactions) {
			if(element.getCategory().equalsIgnoreCase(category)) {
				filtered.add(element);
			}
		}
		return filtered;
	}
	
	//gets the total income
	public double getTotalIncome() {
		double totalIncome = 0;
		for(Transaction element : transactions) {
			if(element.getType().equalsIgnoreCase("income")) {
				totalIncome += element.getAmount();
			}
		}
		return totalIncome;
	}
	
	//gets total expenses
	public double getTotalExpenses() {
		double totalExpenses = 0;
		for(Transaction element : transactions) {
			if(element.getType().equalsIgnoreCase("expense")) {
				totalExpenses += element.getAmount();
			}
		}
		return totalExpenses;
	}
	
	//gets the net balance
	public double getNetBalance() {
		double total = getTotalIncome() - getTotalExpenses();
		return total;
	}
	
	//deletes a transaction
	public void deleteTransaction(int index) {
		if(index >=0 && index < transactions.size()) {
			transactions.remove(index);
		}
	}
	
	//saves the transactions
	public void saveToFile(String filename) {
		try(FileWriter writer = new FileWriter(filename)){
			for(Transaction element : transactions) {
				String safeDescription = element.getDescription()
						.replace(",", "[comma]");
				
				writer.write(String.format("%s,%s,%s,%s,%.2f%n", 
						element.getDate(), 
						element.getType(),
						element.getCategory(),
						safeDescription,
						element.getAmount()
				));
			}
			System.out.println("Transactions saved to file.");
		}catch(IOException e){
			System.out.println("Error saving transactions: " + e.getMessage());
		}
	}
	
	public void loadFromFile(String filename) {
		File file = new File(filename);
		
		if(!file.exists()) {
			System.out.print("No existing file found, starting fresh");
			return; 
		}
		
		try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
			String line;
			while((line = reader.readLine()) != null) {
				String[] parts = line.split(",", 5);
				
				if(parts.length == 5) {
					String date = parts[0];
					String type = parts[1];
					String category = parts[2];
					String description = parts[3]
							.replace("[comma]", ",");
					double amount = Double.parseDouble(parts[4]);
					
					Transaction t = new Transaction(date, type, category, description, amount);
					transactions.add(t);
				}
			}
			System.out.println("Transactions loaded from file.");
		}catch(IOException e) {
			System.out.println("Error loading transactions: " + e.getMessage());
		}
	}
}
