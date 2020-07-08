package net.buttology.lwjgl.swt.input;

import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JRootPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;

import net.buttology.lwjgl.swt.GLComposite;

public class BridgeKeyboardInputManagerWin32 extends BridgeKeyboardState {

	/**
	 * Holds a translation table of AWT keyCodes to SWT keyCodes
	 */
	private static final Map<Integer, Integer> AWT_SWT_MAP = new HashMap<Integer, Integer>();

	static {
		for(int i = KeyEvent.VK_F1; i < KeyEvent.VK_F20; i++) {			
			AWT_SWT_MAP.put(i, SWT.F1 + i);
		}
		AWT_SWT_MAP.put(KeyEvent.VK_PRINTSCREEN, SWT.PRINT_SCREEN);
		AWT_SWT_MAP.put(KeyEvent.VK_SCROLL_LOCK, SWT.SCROLL_LOCK);
		AWT_SWT_MAP.put(KeyEvent.VK_PAUSE, SWT.PAUSE);
		AWT_SWT_MAP.put(KeyEvent.VK_CAPS_LOCK, SWT.CAPS_LOCK);
		AWT_SWT_MAP.put(KeyEvent.VK_SHIFT, SWT.SHIFT);
		AWT_SWT_MAP.put(KeyEvent.VK_CONTROL, SWT.CTRL);
		AWT_SWT_MAP.put(KeyEvent.VK_ALT, SWT.ALT);
		AWT_SWT_MAP.put(KeyEvent.VK_ALT_GRAPH, SWT.ALT_GR);
		AWT_SWT_MAP.put(KeyEvent.VK_INSERT, SWT.INSERT);
		AWT_SWT_MAP.put(KeyEvent.VK_HOME, SWT.HOME);
		AWT_SWT_MAP.put(KeyEvent.VK_END, SWT.END);
		AWT_SWT_MAP.put(KeyEvent.VK_PAGE_UP, SWT.PAGE_UP);
		AWT_SWT_MAP.put(KeyEvent.VK_PAGE_DOWN, SWT.PAGE_DOWN);
		AWT_SWT_MAP.put(KeyEvent.VK_LEFT, SWT.ARROW_LEFT);
		AWT_SWT_MAP.put(KeyEvent.VK_UP, SWT.ARROW_UP);
		AWT_SWT_MAP.put(KeyEvent.VK_RIGHT, SWT.ARROW_RIGHT);
		AWT_SWT_MAP.put(KeyEvent.VK_DOWN, SWT.ARROW_DOWN);
		AWT_SWT_MAP.put(KeyEvent.VK_NUM_LOCK, SWT.NUM_LOCK);
	}
	
	private Panel panel;
	
	public BridgeKeyboardInputManagerWin32(GLComposite composite) {
		super(composite);
		this.createWrapper();
	}
	
	/**
	 * This is a "hack" to allow more reliable keyboard capture on Windows due to SWT 
	 * not properly capturing KeyRelease events when multiple keys are pressed at once.
	 * It utilizes the AWT implementation as this does not have the same issue.
	 */
	private void createWrapper() {
		Composite inputContainer = new Composite(composite, SWT.EMBEDDED);
		inputContainer.setLayoutData(new FormData());
		Frame frame = SWT_AWT.new_Frame(inputContainer);
		panel = new Panel();
		frame.add(panel);
		JRootPane rootPane = new JRootPane();
		panel.add(rootPane);
		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				handleEvent(e, true);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				handleEvent(e, false);
			}
			private void handleEvent(KeyEvent e, boolean pressed) {
				char c = e.getKeyChar();
				if(c != KeyEvent.CHAR_UNDEFINED) {					
					setKeyDown(c, pressed);
				}
				else {
					int k = e.getKeyCode();
					if(AWT_SWT_MAP.containsKey(k)) k = AWT_SWT_MAP.get(k);
					else {
						System.out.println(k);
					}
					setKeyDown(k, pressed);
				}
			}
		});
	}

	@Override
	public void setFocus() {
		panel.requestFocus();
	}

}
