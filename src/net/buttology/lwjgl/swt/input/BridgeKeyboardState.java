package net.buttology.lwjgl.swt.input;

import java.util.HashMap;
import java.util.Map;

import net.buttology.lwjgl.swt.GLComposite;

public abstract class BridgeKeyboardState {
	
	/** The GLComposite that this keyboard is associated with */
	protected GLComposite composite;
	
	/** Map used to track state of keyboard keys, using the <code>char</code> representation of it. */
	private Map<Character, Boolean> charMap = new HashMap<Character, Boolean>();

	/** Map used to track state of special keyboard keys which SWT uses integers for, such as modifier keys. */
	private Map<Integer, Boolean> maskMap = new HashMap<Integer, Boolean>();

	/**
	 * Create a new container for tracking keyboard states.
	 * @param composite the parent 
	 */
	public BridgeKeyboardState(GLComposite composite) {
		this.composite = composite;
	}
	
	/**
	 * Check whether a keyboard key is currently pressed.
	 * @param key the charCode representation of the key
	 * @return true if down
	 */
	public boolean isKeyDown(char key) {
		if(charMap.containsKey(key)) return charMap.get(key);
		return false;
	}
	
	/**
	 * Check whether a keyboard key is currently pressed.
	 * @param key the keyCode representation of the key
	 * @return true if down
	 */
	public boolean isKeyDown(int key) {
		if(maskMap.containsKey(key)) return maskMap.get(key);
		return false;
	}
	
	/**
	 * Pass the focus to the required control for capturing keyboard events.
	 */
	public abstract void setFocus();
	
	void setKeyDown(char key, boolean value) {
		this.charMap.put(key, value);
	}
	
	void setKeyDown(int key, boolean value) {
		this.maskMap.put(key, value);
	}
	
}
