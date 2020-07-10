package net.buttology.lwjgl.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import net.buttology.lwjgl.swt.input.BridgeKeyboardInputManagerGeneric;
import net.buttology.lwjgl.swt.input.BridgeKeyboardInputManagerWin32;
import net.buttology.lwjgl.swt.input.BridgeKeyboardState;
import net.buttology.lwjgl.swt.input.BridgeMouseState;

/**
 * Instances of this class contains a full-sized canvas for drawing OpenGL content to.
 * @author Mudbill
 * @version 2.0.0
 */
public class GLComposite extends Composite {

	/** Stores the configuration for this canvas regarding the OpenGL context */
	private BridgeConfig config;

	/** The canvas widget in SWT that the OpenGL calls are rendered to */
	private GLCanvas canvas;
	
	/** Stores the GLData required by GLCanvas */
	private GLData glData;
	
	/** Whether or not the active context has been initalized */
	private boolean hasInit = false;
	
	/** Stores the thread runnable responsible for updating the OpenGL context continuously */
	private final GLCompositeUpdater UPDATER;
	
	/** Used for tracking keyboard key pressed state */
	private BridgeKeyboardState keyboardStates;
	
	/** Used for tracking mouse button pressed state */
	private BridgeMouseState mouseStates;
	
	/** The stack layout which displays the canvas on top and keeps listening widgets below and hidden */
	private FormLayout layout;
	
	/**
	 * Constructs a new instance of this class given its parent, a style value describing its behavior, appearance and drawing calls.<br><br>
	 * The style value is either one of the style constants defined in class SWT which is applicable to instances of {@link Composite}, or must be built by bitwise OR'ing together (that is, using the int "|" operator) two or more of those SWT style constants. The class description of <link>Composite</link> lists the style constants that are applicable to the class. 
	 * @see org.eclipse.swt.widgets.Composite
	 * @param parent - the parent of this widget (cannot be null).
	 * @param style - the styles applied to this widget.
	 */
	public GLComposite(Composite parent, int style) {
		super(parent, style);
		this.config = new BridgeConfig();
		this.glData = new GLData();
		this.glData.doubleBuffer = true;
		this.UPDATER = new GLCompositeUpdater(this);
		this.layout = new FormLayout();
		this.setLayout(layout);
	}
	
	/**
	 * Constructs a new instance of this class given its parent, a style value and the OpenGL context config describing its behavior, appearance and drawing calls.<br><br>
	 * The style value is either one of the style constants defined in class SWT which is applicable to instances of {@link Composite}, or must be built by bitwise OR'ing together (that is, using the int "|" operator) two or more of those SWT style constants. The class description of <link>Composite</link> lists the style constants that are applicable to the class. 
	 * @see org.eclipse.swt.widgets.Composite
	 * @param parent - the parent of this widget (cannot be null).
	 * @param style - the styles applied to this widget.
	 * @param config - the config specifying OpenGL render calls.
	 */
	public GLComposite(Composite parent, int style, BridgeConfig config) {
		this(parent, style);
		this.config = config;
	}
	
	/**
	 * Initialize the current context.
	 * @throws NullPointerException if context has not been set
	 * @throws BridgeException if context has already been initialized
	 */
	public void init() throws BridgeException, NullPointerException {
		if(config.getContext() == null) {
			throw new NullPointerException("Cannot init non-existent context. Context missing from config.");
		}
		
		if(hasInit) {
			throw new BridgeException("Call to init on already initialized context.");
		}
		
		// Create layout data to fill the entire parent.
		FormData fdata = new FormData();
		fdata.top = new FormAttachment(0);
		fdata.left = new FormAttachment(0);
		fdata.bottom = new FormAttachment(100);
		fdata.right = new FormAttachment(100);
		
		canvas = new GLCanvas(this, SWT.NONE, glData);
		canvas.setLayoutData(fdata);
		canvas.setCurrent();
		GL.createCapabilities();
		
		canvas.addListener(SWT.Dispose, e -> {
			config.getContext().shutdown();
		});
		
		canvas.addListener(SWT.Resize, e -> {
			final Rectangle bounds = canvas.getClientArea();
			canvas.setCurrent();
			GL11.glViewport(0, 0, bounds.width, bounds.height);
		});
		
		if(config.hasKeyboardListener()) {
			if(System.getProperty("os.name").toLowerCase().contains("windows")) {				
				keyboardStates = new BridgeKeyboardInputManagerWin32(this);
			}
			else {
				keyboardStates = new BridgeKeyboardInputManagerGeneric(this);
			}
			
			canvas.addListener(SWT.FocusIn, e -> {
				keyboardStates.setFocus();
			});
			
			canvas.addListener(SWT.MouseDown, e -> {
				canvas.setFocus();
			});
		}
		
		if(config.hasMouseListener()) {
			mouseStates = new BridgeMouseState(this);
			UPDATER.postUpdate(() -> {
				mouseStates.resetScrollAmount();
			});
		}
		
		config.getContext().init();
		hasInit = true;
		getParent().getDisplay().asyncExec(UPDATER);
	}
	
	/**
	 * Set or replace the active config for this GLComposite's canvas.
	 * @param config the config object for the new context
	 */
	public void setConfig(BridgeConfig config) {
		if(hasInit) {
			hasInit = false;
			canvas.dispose();
		}
		this.config = config;
	}
		
	/**
	 * Get the current configuration for this composite.
	 * @return the current config object
	 */
	public BridgeConfig getConfig() {
		return config;
	}
	
	/**
	 * Whether this canvas is the active OpenGL context.
	 * @return
	 */
	public boolean isCurrent() {
		return canvas.isCurrent();
	}
	
	/**
	 * Sets this canvas as the active context for OpenGL calls.
	 */
	public void setCurrent() {
		canvas.setCurrent();
	}
	
	/**
	 * Get the time, in seconds, since last rendered frame.
	 * @return delta time
	 */
	public double getDeltaTime() {
		return UPDATER.getDeltaTime();
	}
	
	/**
	 * Get the number of recorded frames from the last second.
	 * @return frames per second
	 */
	public int getFramerate() {
		return UPDATER.getFramerate();
	}
	
	/**
	 * Get the keyboard capturing class, if created.
	 * @return keyboard handler
	 * @throws NullPointerException if not specified to be created in the config
	 */
	public BridgeKeyboardState getKeyboard() throws NullPointerException {
		return keyboardStates;
	}
	
	/**
	 * Get the mouse capturing class, if created.
	 * @return mouse handler
	 * @throws NullPointerException if not specified to be created in the config
	 */
	public BridgeMouseState getMouse() throws NullPointerException {
		return mouseStates;
	}
	
	/**
	 * Get a reference directly to the canvas.
	 * @return the raw GLCanvas
	 */
	public GLCanvas getCanvas() {
		return canvas;
	}
}
