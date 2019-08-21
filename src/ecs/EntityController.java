package ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class EntityController {
	
	public enum CompType {
		TRANSFORMABLE,
		RENDERABLE,
		POINTLIGHTCOMPONENT,
		AUDIOSOURCECOMPONENT,
		FPPCAMERACOMPONENT,
		PLAYERCONTROLLERCOMPONENT,
		PHYSICSCOMPONENT,
		INVENTORYCOMPONENT,
		ANIMATIONCOMPONENT
	}
	
	private final HashMap<Integer, ArrayList<CompType>> entities = new HashMap<>();

	private final HashMap<Integer, Transformable> transformable = new HashMap<>();
	private final HashMap<Integer, Renderable> renderable = new HashMap<>();
	private final HashMap<Integer, PointLightComponent> pointLightComponent = new HashMap<>();
	private final HashMap<Integer, AudioSourceComponent> audioSourceComponent = new HashMap<>();
	private final HashMap<Integer, FPPCameraComponent> fppCameraComponent = new HashMap<>();
	private final HashMap<Integer, PlayerControllerComponent> playerControllerComponent = new HashMap<>();
	private final HashMap<Integer, PhysicsComponent> physicsComponent = new HashMap<>();
	private final HashMap<Integer, InventoryComponent> inventoryComponent = new HashMap<>();
	private final HashMap<Integer, AnimationComponent> animationComponent = new HashMap<>();
	
	// --- eID de-/allocation ---
	
	public int allocEID(){
		int i = 0;
		while(entities.get(i) != null){
			i++;
		}
		// Initialize the entity 
		// Transformable must always exist for each entity.
		ArrayList<CompType> comps = new ArrayList<CompType>();
		comps.add(CompType.TRANSFORMABLE);
		entities.put(i, comps);
		addTransformable(i);
		return i;
	}
	
	public void freeEID(int eID){
		entities.get(eID).clear();
		entities.put(eID, null);
	}
	
	public void directAllocEID(int eID) {
		// Initialize the entity 
		// Transformable must always exist for each entity.
		ArrayList<CompType> comps = new ArrayList<CompType>();
		comps.add(CompType.TRANSFORMABLE);
		entities.put(eID, comps);
		addTransformable(eID);
	}
	// --- ADDERS ---
	
	public Transformable addTransformable(int eID){// TODO: Make not required
		entities.get(eID).add(CompType.TRANSFORMABLE);
		Transformable comp = new Transformable(eID);
		transformable.put(eID, comp);
		return comp;
	}
	
	public Renderable addRenderable(int eID){
		entities.get(eID).add(CompType.RENDERABLE);
		Renderable comp = new Renderable(eID);
		renderable.put(eID, comp);
		return comp;
	}
	
	public PointLightComponent addPointLightComponent(int eID){
		entities.get(eID).add(CompType.POINTLIGHTCOMPONENT);
		PointLightComponent comp = new PointLightComponent(eID);
		pointLightComponent.put(eID, comp);
		return comp;
	}
	
	public AudioSourceComponent addAudioSourceComponent(int eID) {
		entities.get(eID).add(CompType.AUDIOSOURCECOMPONENT);
		AudioSourceComponent comp = new AudioSourceComponent(eID);
		audioSourceComponent.put(eID, comp);
		return comp;
	}
	
	public FPPCameraComponent addFPPCameraComponent(int eID) {
		entities.get(eID).add(CompType.FPPCAMERACOMPONENT);
		FPPCameraComponent comp = new FPPCameraComponent(eID);
		fppCameraComponent.put(eID, comp);
		return comp;
	}
	
	public PlayerControllerComponent addPlayerControllerComponent(int eID) {
		entities.get(eID).add(CompType.PLAYERCONTROLLERCOMPONENT);
		PlayerControllerComponent comp = new PlayerControllerComponent(eID);
		playerControllerComponent.put(eID, comp);
		return comp;
	}
	
	public PhysicsComponent addPhysicsComponent(int eID) {
		entities.get(eID).add(CompType.PHYSICSCOMPONENT);
		PhysicsComponent comp = new PhysicsComponent(eID);
		physicsComponent.put(eID, comp);
		return comp;
	}
	
	public InventoryComponent addInventoryComponent(int eID) {
		entities.get(eID).add(CompType.INVENTORYCOMPONENT);
		InventoryComponent comp = new InventoryComponent(eID);
		inventoryComponent.put(eID, comp);
		return comp;
	}
	
	public AnimationComponent addAnimationComponent(int eID) {
		entities.get(eID).add(CompType.ANIMATIONCOMPONENT);
		AnimationComponent comp = new AnimationComponent(eID);
		animationComponent.put(eID, comp);
		return comp;
	}
	
	public void addComponentOfType(int eID, CompType type, AbstractComponent component) {
		component.setEID(eID);
		switch (type){
			case TRANSFORMABLE: transformable.put(eID, (Transformable) component); break;
			case RENDERABLE: renderable.put(eID, (Renderable) component); break;
			case POINTLIGHTCOMPONENT: pointLightComponent.put(eID, (PointLightComponent) component); break;
			case AUDIOSOURCECOMPONENT: audioSourceComponent.put(eID, (AudioSourceComponent) component); break;
			case FPPCAMERACOMPONENT: fppCameraComponent.put(eID, (FPPCameraComponent) component); break;
			case PLAYERCONTROLLERCOMPONENT: playerControllerComponent.put(eID, (PlayerControllerComponent) component); break;
			case PHYSICSCOMPONENT: physicsComponent.put(eID, (PhysicsComponent) component); break;
			case INVENTORYCOMPONENT: inventoryComponent.put(eID, (InventoryComponent) component); break;
			case ANIMATIONCOMPONENT: animationComponent.put(eID, (AnimationComponent) component); break;
			default: System.err.println("Failed to add component of type " + type + " to entity " + eID + "! Unknown type!");
		}
	}
	
	// --- REMOVERS ---
	
	public Transformable removeTransformable(int eID){// Will probably crash the engine
		entities.get(eID).remove(CompType.TRANSFORMABLE);
		return transformable.remove(eID);
	}
	
	public Renderable removeRenderable(int eID){
		entities.get(eID).remove(CompType.RENDERABLE);
		return renderable.remove(eID);
	}
	
	public PointLightComponent removePointLightComponent(int eID){
		entities.get(eID).remove(CompType.POINTLIGHTCOMPONENT);
		return pointLightComponent.remove(eID);
	}
	
	public AudioSourceComponent removeAudioSourceComponent(int eID) {
		entities.get(eID).remove(CompType.AUDIOSOURCECOMPONENT);
		return audioSourceComponent.remove(eID);
	}
	
	public FPPCameraComponent removeFPPCameraComponent(int eID) {
		entities.get(eID).remove(CompType.FPPCAMERACOMPONENT);
		return fppCameraComponent.remove(eID);
	}
	
	public PlayerControllerComponent removePlayerControllerComponent(int eID) {
		entities.get(eID).remove(CompType.PLAYERCONTROLLERCOMPONENT);
		return playerControllerComponent.remove(eID);
	}
	
	public PhysicsComponent removePhysicsComponent(int eID) {
		entities.get(eID).remove(CompType.PHYSICSCOMPONENT);
		return physicsComponent.remove(eID);
	}
	
	public InventoryComponent removeInventoryComponent(int eID) {
		entities.get(eID).remove(CompType.INVENTORYCOMPONENT);
		return inventoryComponent.remove(eID);
	}
	
	public AnimationComponent removeAnimationComponent(int eID) {
		entities.get(eID).remove(CompType.ANIMATIONCOMPONENT);
		return animationComponent.remove(eID);
	}
	
	public AbstractComponent removeComponentOfType(int eID, CompType type) {
		if(!hasComponent(eID, type)) {
			return null;
		}else {
			switch (type){
				case TRANSFORMABLE: return removeTransformable(eID);
				case RENDERABLE: return removeRenderable(eID);
				case POINTLIGHTCOMPONENT: return removePointLightComponent(eID);
				case AUDIOSOURCECOMPONENT: return removeAudioSourceComponent(eID);
				case FPPCAMERACOMPONENT: return removeFPPCameraComponent(eID);
				case PLAYERCONTROLLERCOMPONENT: return removePlayerControllerComponent(eID);
				case PHYSICSCOMPONENT: return removePhysicsComponent(eID);
				case INVENTORYCOMPONENT: return removeInventoryComponent(eID);
				case ANIMATIONCOMPONENT: return removeAnimationComponent(eID);
				default: System.err.println("Failed to remove component of type " + type + " from entity " + eID + "! Unknown type!"); return null;
			}
		}
	}
	
	// --- GETTERS ---
	
	public Transformable getTransformable(int eID){
		return transformable.get(eID);
	}
	
	public HashSet<Transformable> getTransformables(){
		return new HashSet<Transformable>(transformable.values());
	}
	
	public Renderable getRenderable(int eID){
		return renderable.get(eID);
	}
	
	public HashSet<Renderable> getRenderables(){
		return new HashSet<Renderable>(renderable.values());
	}
	
	public PointLightComponent getPointLightComponent(int eID){
		return pointLightComponent.get(eID);
	}
	
	public HashSet<PointLightComponent> getPointLightComponents(){
		return new HashSet<PointLightComponent>(pointLightComponent.values());
	}
	
	public AudioSourceComponent getAudioSourceComponent(int eID) {
		return audioSourceComponent.get(eID);
	}
	
	public HashSet<AudioSourceComponent> getAudioSourceComponents(){
		return new HashSet<AudioSourceComponent>(audioSourceComponent.values());
	}
	
	public FPPCameraComponent getFPPCameraComponent(int eID) {
		return fppCameraComponent.get(eID);
	}
	
	public HashSet<FPPCameraComponent> getFPPCameraComponents() {
		return new HashSet<FPPCameraComponent>(fppCameraComponent.values());
	}
	
	public PlayerControllerComponent getPlayerControllerComponent(int eID) {
		return playerControllerComponent.get(eID);
	}
	
	public HashSet<PlayerControllerComponent> getPlayerControllerComponents() {
		return new HashSet<PlayerControllerComponent>(playerControllerComponent.values());
	}
	public PhysicsComponent getPhysicsComponent(int eID) {
		return physicsComponent.get(eID);
	}
	
	public HashSet<PhysicsComponent> getPhysicsComponents() {
		return new HashSet<PhysicsComponent>(physicsComponent.values());
	}
	
	public InventoryComponent getInventoryComponent(int eID) {
		return inventoryComponent.get(eID);
	}
	
	public HashSet<InventoryComponent> getInventoryComponents() {
		return new HashSet<InventoryComponent>(inventoryComponent.values());
	}
	
	public AnimationComponent getAnimationComponent(int eID) {
		return animationComponent.get(eID);
	}
	
	public HashSet<AnimationComponent> getAnimationComponents() {
		return new HashSet<AnimationComponent>(animationComponent.values());
	}
	
	public AbstractComponent getComponentOfType(int eID, CompType type) {
		if(!hasComponent(eID, type)) {
			return null;
		}else {
			switch (type){
				case TRANSFORMABLE: return getTransformable(eID);
				case RENDERABLE: return getRenderable(eID);
				case POINTLIGHTCOMPONENT: return getPointLightComponent(eID);
				case AUDIOSOURCECOMPONENT: return getAudioSourceComponent(eID);
				case FPPCAMERACOMPONENT: return getFPPCameraComponent(eID);
				case PLAYERCONTROLLERCOMPONENT: return getPlayerControllerComponent(eID);
				case PHYSICSCOMPONENT: return getPhysicsComponent(eID);
				case INVENTORYCOMPONENT: return getInventoryComponent(eID);
				case ANIMATIONCOMPONENT: return getAnimationComponent(eID);
				default: System.err.println("Failed to get component of type " + type + " from entity " + eID + "! Unknown type!"); return null;
			}
		}
	}
	
	// --- QUERY ---
	
	public boolean hasComponent(int eID, CompType type){
		return entities.get(eID).contains(type);
	}
	
	public ArrayList<CompType> getComponentsFor(int eID) {
		return entities.get(eID);
	}
	
	public boolean isEntity(int eID) {
		return entities.containsKey(eID);
	}
	
	// --- EXCHANGE ---
	
	public Entity emitEntity(int eID) {
		// copy and delete components
		ArrayList<CompType> componentTypes = entities.get(eID);
		Entity entity = new Entity(eID);
		for(int i = 0; i < componentTypes.size(); i++) {
			entity.addComponent(
					componentTypes.get(i), 
					removeComponentOfType(eID, componentTypes.get(i)));
		}
		// free eID
		freeEID(eID);
		return entity;
	}
	
	public Entity copyEntity(int eID) {
		ArrayList<CompType> componentTypes = entities.get(eID);
		Entity entity = new Entity(eID);
		for(int i = 0; i < componentTypes.size(); i++) {
			entity.addComponent(
					componentTypes.get(i), 
					getComponentOfType(eID, componentTypes.get(i)));
		}
		return entity;
	}
	
	public void integrateEntity(Entity entity) {
		int newEID = allocEID();
		ArrayList<CompType> componentTypes = entity.getComposition();
		for(int i = 0; i < componentTypes.size(); i++) {
			addComponentOfType(
					newEID, 
					componentTypes.get(i), 
					entity.getComponentOfType(componentTypes.get(i)));
		}
	}
	
}
