package net.buttology.lwjgl.swt.input;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Point;

import net.buttology.lwjgl.swt.GLComposite;

public class BridgeMouseState {

	/** The GLComposite that this keyboard is associated with */
	protected GLComposite composite;
	
	/** Map used to track state of mouse keys, using the <code>char</code> representation of it. */
	private Map<Integer, Boolean> mouseMap = new HashMap<Integer, Boolean>();
	
	/** The last known X position of the mouse relative to the widget */
	private int x = 0;
	
	/** The last known Y position of the mouse relative to the widget */
	private int y = 0;
	
	/** The amount the scroll wheel on the mouse has changed since last update. */
	private int scrollDelta = 0;

	/**
	 * Create a new container for tracking mouse states.
	 * @param composite the parent 
	 */
	public BridgeMouseState(GLComposite composite) {
		this.composite = composite;
		this.addListeners();
	}
	
	/**
	 * Check whether the given mouse button is currently pressed.
	 * @param button number (Mouse1 = 1)
	 * @return true if down
	 */
	public boolean isButtonDown(int button) {
		if(mouseMap.containsKey(button)) return mouseMap.get(button);
		return false;
	}
	
	/**
	 * Get the last known X position of the mouse relative to the canvas.
	 * @return x position
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Get the last known Y position of the mouse relative to the canvas.
	 * @return y position
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Get the last known X and Y position of the mouse relative to the canvas.
	 * @return x and y position as a point
	 */
	public Point getPosition() {
		return new Point(x, y);
	}
	
	/**
	 * Get the scroll wheel's current amount of scroll. Might change depending on the user's system configuration.
	 * @return 0 if no scroll, a positive value for scroll up, a negative value for scroll down
	 */
	public int getScrollAmount() {
		return scrollDelta;
	}
	
	/**
	 * Gets the scroll wheel's current direction as a fixed value across systems.
	 * @return 1 if up, -1 if down, 0 if no scroll
	 */
	public int getFixedScrollAmount() {
		if(scrollDelta > 0) return 1;
		if(scrollDelta < 0) return -1;
		return 0;
	}
	
	/**
	 * This will reset the scroll amount stored from the last listened event.
	 * This is called automatically after each update and is not necessary to call manually.
	 */
	public void resetScrollAmount() {
		scrollDelta = 0;
	}
	
	private void addListeners() {
		composite.getCanvas().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				mouseMap.put(e.button, true);
			}
			@Override
			public void mouseUp(MouseEvent e) {
				mouseMap.put(e.button, false);
			}
		});
		composite.getCanvas().addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				x = e.x;
				y = e.y;
			}
		});
		composite.getCanvas().addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseScrolled(MouseEvent e) {
				scrollDelta = e.count;
			}
		});
	}
}
