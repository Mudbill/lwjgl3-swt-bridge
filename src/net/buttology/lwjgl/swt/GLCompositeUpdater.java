package net.buttology.lwjgl.swt;

import java.util.concurrent.TimeUnit;

public class GLCompositeUpdater implements Runnable {

	/** Represents one second in number of nanoseconds */
	private static final int ONE_SECOND_IN_NANOS = 1000000000;
	
	/** The GLComposite that this updater is associated with */
	private GLComposite composite;
	
	/** Stores the time, in milliseconds, between frames */
	private double deltaTime;
	
	/** Stores the previously recorded timestamp in nanoseconds */
	private long lastTime = System.nanoTime();
		
	/** Used to keep track of when a full second has passed */
	private long oneSecondAgo = lastTime;
	
	/** Real framerate, updated every second */
	private int framerate = 0;
	
	/** Buffer to keep track of real frames between seconds */
	private int fpsBuffer = 0;

	/**
	 * Creates a new instance of this class for the given GLComposite. Only GLComposite should instantiate this class.
	 * @param composite the composite holding the drawable canvas
	 */
	GLCompositeUpdater(GLComposite composite) {
		this.composite = composite;
	}
	
	/**
	 * Get the current delta time, in seconds, AKA the time between this frame and the last one.
	 * @return deltaTime
	 */
	public double getDeltaTime() {
		return deltaTime;
	}
	
	/**
	 * Get the number of rendered frames per second.
	 * @return current FPS
	 */
	public int getFramerate() {
		return framerate;
	}
	
	@Override
	public void run() {
		if(!composite.isDisposed()) {
			// Get current time to compare with previous time
			long thisTime = System.nanoTime();
			
			// Get the time, in nanoseconds, since last frame
			long currentNanoDiff = thisTime - lastTime;
						
			// Calculate what the minimum difference since last frame should be in nanoseconds
			long desiredNanoDiff = ONE_SECOND_IN_NANOS / composite.getConfig().getFPSLimit();
			
			// Calculate the difference between the current and desired differences.
			long comparedNanoDiff = desiredNanoDiff - currentNanoDiff;
			
			// If the difference should be greater, add a delay to sync up
			if(comparedNanoDiff > 0) {
				sleep(comparedNanoDiff);
				currentNanoDiff += comparedNanoDiff;
				thisTime += comparedNanoDiff;
			}
			
			// Update the delta time (in seconds)
			deltaTime = currentNanoDiff / (double) ONE_SECOND_IN_NANOS;
			
			// Check if the current time is at least 1 second more than the last stored time
			if(thisTime - oneSecondAgo >= ONE_SECOND_IN_NANOS) {
				// Update timestamps and FPS counters
				oneSecondAgo = thisTime;
				framerate = fpsBuffer;
				fpsBuffer = 0;
				
//				System.out.println(
//						"FPS: " + framerate
//						+ ", delta: " + deltaTime
//						+ ", lastTime: " + lastTime
//						+ ", thisTime: " + thisTime
//						+ ", currentNanoDiff: " + currentNanoDiff
//						+ ", comparedNanoDiff: " + comparedNanoDiff
//						+ ", desiredNanoDiff: " + desiredNanoDiff 
//						);
			}
			
			// Update the previous timestamp and increment buffer
			lastTime = thisTime;
			fpsBuffer++;

			// Set the current drawable canvas and call the update for it
			composite.setCurrent();
			composite.getConfig().getContext().update();
			composite.getCanvas().swapBuffers();
			
			if(composite.getConfig().isLoopingEnabled()) {
				composite.getParent().getDisplay().asyncExec(this);
			}
		}
	}

	/**
	 * Sleep for the specified nanoseconds, while suppressing errors.
	 * @param nanos amount of nanoseconds
	 */
	private void sleep(long nanos) {
		try {
			TimeUnit.NANOSECONDS.sleep(nanos);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
