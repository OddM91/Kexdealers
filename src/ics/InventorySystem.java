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
		
		// process messages
		Message message;
		while((message = messageBus.getNextMessage(Recipients.INVENTORY_SYSTEM)) != null) {
			
			final Object[] args = message.getArgs();
			
			switch(message.getBehaviorID()) {
			case TRANSFER_ITEM:
				final int A_senderEID = (int) args[0];
				final int A_recipientEID = (int) args[1];
				final int A_itemID = (int) args[2];
				InventoryComponent A_sender = entityController.getInventoryComponent(A_senderEID);
				InventoryComponent A_recipient = entityController.getInventoryComponent(A_recipientEID);
				A_sender.removeItem(A_itemID);
				A_recipient.addItem(A_itemID);
				break;
			case SPAWN_ITEM:
				final int B_recipientEID = (int) args[0];
				final Item B_item = (Item) args[2];
				final int B_itemID = B_item.getItemID();
				itemController.integrateItem(B_item);
				InventoryComponent B_recipient = entityController.getInventoryComponent(B_recipientEID);
				B_recipient.addItem(B_itemID);
			case DELETE_ITEM:
				final int C_targetEID = (int) args[0];
				final int C_itemID = (int) args[1];
				InventoryComponent C_target = entityController.getInventoryComponent(C_targetEID);
				C_target.removeItem(C_itemID);
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
