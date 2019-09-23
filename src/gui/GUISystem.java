package gui;

import java.util.ArrayList;

import bus.Message;
import bus.MessageBus;
import bus.Recipients;
import ecs.AbstractSystem;
import ecs.EntityController;

public class GUISystem extends AbstractSystem {

	// op codes
	public static final int TEST = 100;
	
	protected GUISystem(MessageBus messageBus, EntityController entityController) {
		super(messageBus, entityController);
	}

	@Override
	public void run() {
		update();
	}

	@Override
	protected void update() {
		
		// Process Messages
		Message message;
		while((message = messageBus.getNextMessage(Recipients.GUI_SYSTEM)) != null) {
			
			final Object[] args = message.getArgs();
			
			switch(message.getBehaviorID()) {
			default: System.err.println("GUI operation not implemented");
			}
		}
	}
	
	
	
	
	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadBlueprint(ArrayList<String> blueprint) {
		// TODO Auto-generated method stub
		
	}
	
}
