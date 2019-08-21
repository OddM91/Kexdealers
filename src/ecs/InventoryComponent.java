package ecs;

import java.util.ArrayList;

import utility.StringUtility;

public class InventoryComponent extends Component{
	
	private boolean isCharacterInventory;
	private int capacity = 1;
	private int usedCapacity = 0;
	private final ArrayList<Integer> content = new ArrayList<>();
	
	public InventoryComponent(int eID) {
		super(eID);
	}
	
	@Override
	public Component clone() {
		final InventoryComponent deepCopy = new InventoryComponent(this.eID)
				.setIsCharacterInventory(this.isCharacterInventory)
				.setCapacity(this.capacity)
				.setUsedCapacity(this.usedCapacity)
				.setContent(this.content);
		return deepCopy;
	}
	
	@Override
	public String toString() {
		final String[] tags = {"I", "C", "U", "CONT"};
		
		final StringBuilder contentString = new StringBuilder();
		for(int i : content) {
			contentString.append(i + ", ");
		}
		
		final Object[] data = {isCharacterInventory,
				capacity,
				usedCapacity,
				contentString.toString()};
		return StringUtility.toStringHelper("InventoryComponent", eID, tags, data);
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
		this.content.clear();
		this.content.addAll(content);
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
