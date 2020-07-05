package net.buttology.lwjgl.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

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
		this.setLayout(new FillLayout());
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
		
		canvas = new GLCanvas(this, SWT.NONE, glData);
		canvas.setCurrent();
		GL.createCapabilities();
		config.getContext().init();
		
		canvas.addListener(SWT.Dispose, e -> {
			config.getContext().shutdown();
		});
		
		canvas.addListener(SWT.Resize, e -> {
			final Rectangle bounds = canvas.getClientArea();
			canvas.setCurrent();
			GL11.glViewport(0, 0, bounds.width, bounds.height);
		});
		
		canvas.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				System.out.println(e);
			}
			public void keyReleased(KeyEvent e) {
				System.out.println(e);
			}
		});
		
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
	
	public boolean isCurrent() {
		return canvas.isCurrent();
	}
	
	public void setCurrent() {
		canvas.setCurrent();
	}
	
	public double getDeltaTime() {
		return UPDATER.getDeltaTime();
	}
	
	public int getFramerate() {
		return UPDATER.getFramerate();
	}
	
	GLCanvas getCanvas() {
		return canvas;
	}
}
