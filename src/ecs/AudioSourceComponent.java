package ecs;

import utility.StringUtility;

public class AudioSourceComponent extends AbstractComponent{
	
	private String assetName = "default";
	
	private int sourceID = -1;
	
	private float gain = 1.0f;
	private float pitch = 1.0f;
	
	private float referenceDistance = 10.0f;
	private float rolloffFactor = 1.0f;
	private float maxDistance = 50.0f;
	
	private float startPos = 0.0f; // in seconds
	
	public AudioSourceComponent(int eID) {
		super(eID);
	}
	
	@Override
	public AudioSourceComponent clone() {
		final AudioSourceComponent deepCopy = new AudioSourceComponent(eID)
				.setAudioSourceFileName(this.assetName)
				.setSourceID(this.sourceID)
				.setGain(this.gain)
				.setPitch(this.pitch)
				.setReferenceDistance(this.referenceDistance)
				.setRolloffFactor(this.rolloffFactor)
				.setMaxDistance(this.maxDistance);
		return deepCopy;
	}
	
	@Override
	public String toString() {
		final String[] tags = {"Name", "sID", "Gain", "Pitch", "RefDist", "Rolloff", "MaxDist", "StartPos"};
		final Object[] data = {assetName, sourceID, gain, pitch, referenceDistance, rolloffFactor, maxDistance, startPos};
		return StringUtility.toStringHelper("AudioSourceComponent", eID, tags, data);
	}
	
	public String getAudioSourceFileName() {
		return assetName;
	}

	public AudioSourceComponent setAudioSourceFileName(String assetName) {
		this.assetName = assetName;
		return this;
	}

	public int getSourceID() {
		return sourceID;
	}

	public AudioSourceComponent setSourceID(int sourceID) {
		this.sourceID = sourceID;
		return this;
	}

	public float getGain() {
		return gain;
	}

	public AudioSourceComponent setGain(float gain) {
		this.gain = gain;
		return this;
	}

	public float getPitch() {
		return pitch;
	}

	public AudioSourceComponent setPitch(float pitch) {
		this.pitch = pitch;
		return this;
	}

	public float getReferenceDistance() {
		return referenceDistance;
	}

	public AudioSourceComponent setReferenceDistance(float referenceDistance) {
		this.referenceDistance = referenceDistance;
		return this;
	}

	public float getRolloffFactor() {
		return rolloffFactor;
	}

	public AudioSourceComponent setRolloffFactor(float rolloffFactor) {
		this.rolloffFactor = rolloffFactor;
		return this;
	}

	public float getMaxDistance() {
		return maxDistance;
	}

	public AudioSourceComponent setMaxDistance(float maxDistance) {
		this.maxDistance = maxDistance;
		return this;
	}

	public float getStartPos() {
		return startPos;
	}

	public void setStartPos(float startPos) {
		this.startPos = startPos;
	}

}
