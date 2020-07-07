package net.buttology.lwjgl.swt.input;

import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JRootPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;

import net.buttology.lwjgl.swt.GLComposite;

public class BridgeKeyboardInputManagerWin32 extends BridgeKeyboardState {

	private Panel panel;
	
	public BridgeKeyboardInputManagerWin32(GLComposite composite) {
		super(composite);
		this.createWrapper();
	}
	
	private void createWrapper() {
		Composite inputContainer = new Composite(composite, SWT.EMBEDDED);
		Frame frame = SWT_AWT.new_Frame(inputContainer);
		panel = new Panel();
		frame.add(panel);
		JRootPane rootPane = new JRootPane();
		panel.add(rootPane);
		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				setKeyDown(e.getKeyChar(), true);
				setKeyDown(e.getKeyCode(), true);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				setKeyDown(e.getKeyChar(), false);
				setKeyDown(e.getKeyCode(), false);
			}
		});
	}

	public void setFocus() {
		panel.requestFocus();
	}

}
