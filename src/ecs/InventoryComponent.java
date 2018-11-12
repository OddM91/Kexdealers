package ecs;

import java.util.ArrayList;

public class InventoryComponent extends Component{
	
	private boolean isCharacterInventory;
	private int capacity = 1;
	private int usedCapacity = 0;
	private ArrayList<Integer> content = new ArrayList<>();
	
	public InventoryComponent(int eID) {
		super(eID);
	}
	
	@Override
	public int getEID() {
		return eID;
	}

	@Override
	public void setEID(int eID) {
		this.eID = eID;
	}

	@Override
	public Component clone() {
		InventoryComponent deepCopy = new InventoryComponent(this.eID)
				.setIsCharacterInventory(this.isCharacterInventory)
				.setCapacity(this.capacity)
				.setUsedCapacity(this.usedCapacity)
				.setContent(this.content);
		return deepCopy;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("InventoryComponent<").append(eID).append(">");
		s.append("(");
		s.append(" I: ").append(isCharacterInventory);
		s.append(" C: ").append(capacity);
		s.append(" U: ").append(usedCapacity);
		s.append(" CONT: ");
		for(int i : content) {
			s.append(i + ", ");
		}
		s.append(" )");
		return s.toString();
	}
	
	public boolean isCharacterInventory() {
		return isCharacterInventory;
	}
	
	public InventoryComponent setIsCharacterInventory(boolean isCharacterInventory) {
		this.isCharacterInventory = isCharacterInventory;
		return this;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public InventoryComponent setCapacity(int capacity) {
		this.capacity = capacity;
		return this;
	}
	
	public int getUsedCapacity() {
		return usedCapacity;
	}
	
	public InventoryComponent setUsedCapacity(int usedCapacity) {
		this.usedCapacity = usedCapacity;
		return this;
	}
	
	public ArrayList<Integer> getContent() {
		return content;
	}
	
	public InventoryComponent setContent(ArrayList<Integer> content) {
		this.content = content;
		return this;
	}
	
	public int getContent(int slot) {
		return content.get(slot);
	}
	
	public InventoryComponent addContent(int slot, int itemID) {
		content.add(slot, itemID);
		return this;
	}
	
	public boolean hasItemID(int itemID) {
		return content.contains(itemID);
	}
	
	public int getRemainingCapacity() {
		return capacity - usedCapacity;
	}
	
	public boolean addItem(int itemID) {
		if(usedCapacity < capacity) {
			content.add(itemID);
			usedCapacity++;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean removeItem(int itemID) {
		if(hasItemID(itemID)) {
			content.remove(itemID);
			usedCapacity--;
			return true;
		} else {
			return false;
		}
	}
	
}
