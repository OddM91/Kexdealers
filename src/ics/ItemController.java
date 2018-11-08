package ics;

import java.util.HashMap;

public class ItemController {
	
	private final HashMap<Integer, Item> items = new HashMap<>();
	
	public int generateItem() {
		int i = 0;
		while(items.get(i) != null) {
			i++;
		}
		items.put(i, new Item(i));
		return i;
	}
	
	public void destroyItem(int itemID) {
		items.remove(itemID);
	}
	
	public void directGenerateItem(int itemID) {
		items.put(itemID, new Item(itemID));
	}
	
	public Item getItem(int itemID) {
		return items.get(itemID);
	}
	
	// --- EXCHANGE ---
	public Item emitItem(int itemID) {
		return items.remove(itemID);
	}
	
	public Item copyItem(int itemID) {
		return items.get(items).clone();
	}
	
	public void integrateItem(Item item) {
		int i = 0;
		while(items.get(i) != null) {
			i++;
		}
		items.put(i, item);
	}
}
