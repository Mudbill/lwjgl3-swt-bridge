package net.buttology.lwjgl.swt;

public class BridgeConfig {

	/** Interface implementing the OpenGL calls */
	private BridgeContext context = null;
	
	/** Whether to enable the updater */
	private boolean looping = true;
		
	private boolean listenForKeyboard = false;
	private boolean listenForMouse = false;
	
	/** The rate, per second, at which update calls are executed */
	private int fpsLimit = 60;
	
	public BridgeConfig() {}
	
	public BridgeConfig setLooping(boolean x) {
		this.looping = x;
		return this;
	}
	
	public BridgeConfig setContext(BridgeContext context) {
		this.context = context;
		return this;
	}
	
	public BridgeConfig withKeyboardListener() {
		this.listenForKeyboard = true;
		return this;
	}
	
	public BridgeConfig withMouseListener() {
		this.listenForMouse = true;
		return this;
	}
	
	public BridgeConfig setFPSLimit(int x) {
		this.fpsLimit = x;
		return this;
	}
	
	public boolean isLoopingEnabled() {
		return looping;
	}
	
	public BridgeContext getContext() {
		return context;
	}
	
	public boolean hasKeyboardListener() {
		return listenForKeyboard;
	}
	
	public boolean hasMouseListener() {
		return listenForMouse;
	}
	
	public int getFPSLimit() {
		return fpsLimit;
	}
	
}
