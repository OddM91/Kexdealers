package ics;

import java.util.ArrayList;

import bus.Message;
import bus.MessageBus;
import bus.Recipients;
import ecs.AbstractSystem;
import ecs.EntityController;
import ecs.InventoryComponent;

public class InventorySystem extends AbstractSystem {

	// op codes
	public static final int TRANSFER_ITEM = 0;
	public static final int SPAWN_ITEM = 1;
	public static final int DELETE_ITEM = 2;
	
	private ItemController itemController;
	
	public InventorySystem(MessageBus messageBus, EntityController entityController, ItemController itemController) {
		super(messageBus, entityController);
		this.itemController = itemController;
	}

	@Override
	public void run() {
		update();
		
	}

	@Override
	protected void update() {
		super.timeMarkStart();
		// message queue
		Message message;
		while((message = messageBus.getNextMessage(Recipients.INVENTORY_SYSTEM)) != null) {
			
			final Object[] args = message.getArgs();
			
			switch(message.getBehaviorID()) {
			case TRANSFER_ITEM:
				final int senderEID = (int) args[0];
				final int recipientEID = (int) args[1];
				final int itemID = (int) args[2];
				InventoryComponent sender = entityController.getInventoryComponent(senderEID);
				InventoryComponent recipient = entityController.getInventoryComponent(recipientEID);
				sender.removeItem(itemID);
				recipient.addItem(itemID);
				break;
			case SPAWN_ITEM:
				final int recipientEID = (int) args[0];
				final Item item = (Item) args[2];
				final int itemID = item.getItemID();
				itemController.integrateItem(item);
				InventoryComponent recipient = entityController.getInventoryComponent(recipientEID);
				recipient.addItem(itemID);
			case DELETE_ITEM:
				final int targetEID = (int) args[0];
				final int itemID = (int) args[1];
				InventoryComponent target = entityController.getInventoryComponent(targetEID);
				target.removeItem(itemID);
			default: System.err.println("Inventory operation not implemented");
			}
		}
		// TODO some memory management here?
	}

	@Override
	public void cleanUp() {
		// :)
	}

	@Override
	public void loadBlueprint(ArrayList<String> blueprint) {
		// :)		
	}
	
	
	
}
