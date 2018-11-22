package animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import bus.Message;
import bus.MessageBus;
import bus.Recipients;
import ecs.AbstractSystem;
import ecs.AnimationComponent;
import ecs.EntityController;
import ecs.Renderable;
import example.LinkStart;
import loaders.GraphicsLoader;

public class AnimationSystem extends AbstractSystem {
	
	// opcodes
	public static final int PLAY_ANIMATION = 0;
	public static final int PAUSE_ANIMATION = 1;
	public static final int RESET_ANIMATION = 2;
	public static final int STOP_ANIMATION = 3;
	
	public AnimationSystem(MessageBus messageBus, EntityController entityController) {
		super(messageBus, entityController);
	}

	@Override
	public void run() {
		update();
	}

	@Override
	protected void update() {

		// Process messages
		Message message;
		while((message = messageBus.getNextMessage(Recipients.ANIMATION_SYSTEM)) != null) {
			
			final Object[] args = message.getArgs();
			
			switch(message.getBehaviorID()) {
			case PLAY_ANIMATION:
				// targetEID, animationName
				final int A_targetEID = (int) args[0];
				final String A_animationName = (String) args[1];
				entityController.getAnimationComponent(A_targetEID)
						.setAnimationPlaying(A_animationName, true);
			case PAUSE_ANIMATION:
				// targetEID, animationName
				final int B_targetEID = (int) args[0];
				final String B_animationName = (String) args[1];
				entityController.getAnimationComponent(B_targetEID)
						.setAnimationPlaying(B_animationName, false);
			case RESET_ANIMATION:
				// targetEID, animationName
				final int C_targetEID = (int) args[0];
				final String C_animationName = (String) args[1];
				entityController.getAnimationComponent(C_targetEID)
						.setAnimationProgress(C_animationName, 0.0f);
			case STOP_ANIMATION:
				// targetEID, animationName
				final int D_targetEID = (int) args[0];
				final String D_animationName = (String) args[1];
				entityController.getAnimationComponent(D_targetEID)
						.setAnimationPlaying(D_animationName, false)
						.setAnimationProgress(D_animationName, 0.0f);
			default: System.err.println("Animation operation not implemented " 
						+message.getBehaviorID());
			}
		}
		
		// Progress any animations that are currently playing
		for(AnimationComponent animationComponent : entityController.getAnimationComponents()) {
			if(!animationComponent.anyAnimationPlaying()) {
				continue;
			}
			
			Renderable renderable = entityController.getRenderable(animationComponent.getEID());
			
			// TODO: FOR DEBUGGING
			GraphicsLoader graphicsLoader = null;
			for(Entry<String, Float> entry : animationComponent.getAllActiveAnimations().entrySet()) {
				Animation animation = graphicsLoader.getAnimation(renderable.getResourceName(), entry.getKey());
				
				// -> update animation progress time in component
				animationComponent.setAnimationProgress(entry.getKey(), 
						Math.max(entry.getValue() + LinkStart.FRAME_TIME, animation.getLength()));
				
				// -> update mesh data in AnimatedMesh
			}
			
			
			
		}
		
	}

	@Override
	public void cleanUp() {
		// TODO: ???
	}

	@Override
	public void loadBlueprint(ArrayList<String> blueprint) {
		// TODO: ???
	}

}
