package net.buttology.lwjgl.swt.input;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

import net.buttology.lwjgl.swt.GLComposite;

public class BridgeKeyboardInputManagerGeneric extends BridgeKeyboardState {

	public BridgeKeyboardInputManagerGeneric(GLComposite composite) {
		super(composite);
		this.createListener();
	}
	
	private void createListener() {
		composite.getCanvas().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				setKeyDown(e.character, true);
				setKeyDown(e.keyCode, true);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				setKeyDown(e.character, false);
				setKeyDown(e.keyCode, false);
			}
		});
	}

	@Override
	public void setFocus() {
		composite.getCanvas().setFocus();
	}
	
}
