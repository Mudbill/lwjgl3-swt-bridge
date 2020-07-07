package examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.opengl.GL11;

import net.buttology.lwjgl.swt.BridgeConfig;
import net.buttology.lwjgl.swt.BridgeContext;
import net.buttology.lwjgl.swt.BridgeException;
import net.buttology.lwjgl.swt.GLComposite;
import net.buttology.lwjgl.swt.input.BridgeKeyboardState;

public class BridgeFormExample {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BridgeFormExample window = new BridgeFormExample();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private GLComposite composite = null;
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new FillLayout());

		composite = new GLComposite(shell, SWT.NONE);
		
		Composite comp = new Composite(shell, SWT.NONE);
		comp.setLayout(new FillLayout(SWT.VERTICAL));
		Label label = new Label(comp, SWT.NONE);
		label.setText("test");
				
		BridgeConfig config = new BridgeConfig()
				.setFPSLimit(240)
				.withMouseListener()
				.setContext(new BridgeContext() {
					
					private BridgeKeyboardState keyboard = composite.getKeyboard();
					
					@Override
					public void init() {
						System.out.println("Init context...");
						GL11.glClearColor(1, 0, 0, 1);
					}

					@Override
					public void update() {
						GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
						label.setText("Frametime ms: "+composite.getDeltaTime() + "\nFPS: " + composite.getFramerate());
						if(keyboard != null) {							
							if(keyboard.isKeyDown('a')) {
								GL11.glClearColor(0, 1, 0, 1);
							}
							if(keyboard.isKeyDown('w')) {
								GL11.glClearColor(0, 0, 1, 1);
							}
						}
					}

					@Override
					public void shutdown() {
						System.out.println("Shutdown context...");
					}
					
				});
		
		composite.setConfig(config);
		
		try {
			composite.init();
		} catch (BridgeException e) {
			e.printStackTrace();
		}
	}
}
