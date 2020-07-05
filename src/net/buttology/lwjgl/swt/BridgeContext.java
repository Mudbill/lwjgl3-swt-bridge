package net.buttology.lwjgl.swt;

public interface BridgeContext {

	/**
	 * Called upon creating this OpenGL context
	 */
	public void init();
	
	/**
	 * Called every frame this OpenGL context is updated
	 */
	public void update();
	
	/**
	 * Called upon destroying this OpenGL context
	 */
	public void shutdown();
	
}
