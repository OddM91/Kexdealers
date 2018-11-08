package ics;

public class Item {
	
	private int itemID;
	private int stackSize = 16;
	
	private String description = "no description";
	
	public Item(int itemID) {
		this.itemID = itemID;
	}
	
	public int getItemID() {
		return itemID;
	}
	
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}
	
	@Override
	public Item clone() {
		Item deepCopy = new Item(this.itemID);
		return deepCopy;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Item<").append(itemID).append(">");
		s.append("(");
		s.append(" STACK: ").append(stackSize);
		s.append(" DESC: ").append(description);
		s.append(" )");
		return s.toString();
	}
	
	public int getStackSize() {
		return stackSize;
	}
	
	public Item setStackSize(int stackSize) {
		this.stackSize = stackSize;
		return this;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Item setDescription(String description) {
		this.description = description;
		return this;
	}
}
