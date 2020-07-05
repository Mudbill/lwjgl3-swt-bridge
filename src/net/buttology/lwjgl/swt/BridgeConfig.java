package net.buttology.lwjgl.swt;

public class BridgeConfig {

	/** Interface implementing the OpenGL calls */
	private BridgeContext context = null;
	
	/** Whether to enable the updater */
	private boolean looping = false;
	
	/** Whether to listen for inputs */
	private boolean listening = false;
	
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
	
	public BridgeConfig createInputListeners(boolean x) {
		this.listening = x;
		return this;
	}
	
	public BridgeConfig setFPSLimit(int x) {
		this.fpsLimit = x;
		return this;
	}
	
	public boolean loopingEnabled() {
		return looping;
	}
	
	public BridgeContext getContext() {
		return context;
	}
	
	public boolean hasInputListeners() {
		return listening;
	}
	
	public int getFPSLimit() {
		return fpsLimit;
	}
	
}
