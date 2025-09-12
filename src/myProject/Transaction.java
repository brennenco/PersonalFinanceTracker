package myProject;

public class Transaction {
	
	private String date;
	private String type;
	private String category;
	private String description;
	private double amount;
	
	public Transaction(String date, String type, String category, String description, double amount) {
		this.date = date;
		this.type = type;
		this.category = category;
		this.description = description;
		this.amount = amount;
	}
	
	public String getDate() {
		return this.date;
	}
	public String getType() {
		return this.type;
	}
	public String getCategory() {
		return this.category;
	}
	public String getDescription() {
		return this.description;
	}
	public double getAmount() {
		return this.amount;
	}
	
	public String toString() {
		return String.format("%-12s %-8s %-15s %-20s $%.2f", date, type, category, description, amount);
	}
	

}
