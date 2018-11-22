package ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import animation.Animation;

public class AnimationComponent extends Component {

	private final HashMap<String, Float> animationProgress = new HashMap<>();
	private final HashMap<String, Boolean> animationState = new HashMap<>();
	
	public AnimationComponent(int eID) {
		super(eID);
	}
	
	@Override
	public AnimationComponent clone() {
		AnimationComponent deepCopy = new AnimationComponent(this.eID);
		return deepCopy;
	}

	@Override
	public String toString() {
		return null;
	}
	
	public float getAnimationProgress(String animationName) {
		return animationProgress.get(animationName);
	}
	
	public AnimationComponent setAnimationProgress(String animationName, float alpha) {
		animationProgress.put(animationName, alpha);
		return this;
	}
	
	public boolean isAnimationPlaying(String animationName) {
		return animationState.get(animationName);
	}
	
	public AnimationComponent setAnimationPlaying(String animationName, boolean playing) {
		animationState.put(animationName, playing);
		return this;
	}
	
	public boolean anyAnimationPlaying() {
		for(boolean v : animationState.values()) {
			if(v) {
				return true;
			}
		}
		return false;
	}
	
	public HashMap<String, Float> getAllActiveAnimations() {
		final HashMap<String, Float> activeAnimations = new HashMap<>();
		for(Entry<String, Boolean> entry : animationState.entrySet()) {
			if(entry.getValue()) {
				activeAnimations.put(
						entry.getKey(), 
						animationProgress.get(entry.getKey())
						);
			}
		}
		return activeAnimations;
	}
	
}
