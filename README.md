# lwjgl3-swt-bridge

### What is it?

This is a tiny library intended to help implement LWJGL3 contexts into an SWT application. It utilizes the existing GLCanvas widget but sets up most of the basic things you will need to get up and running with rendering calls.

### How do I use it?

First off, you need the dependencies in your project:

* [lwjgl3](https://github.com/LWJGL/lwjgl3/releases)
* [lwjgl3-opengl](https://github.com/LWJGL/lwjgl3/releases)
* [swt](https://www.eclipse.org/swt/)

Once you have these, you can include lwjgl3-swt-bridge in your classpath and use the `GLComposite` class. The `GLComposite` class behaves almost exactly like a regular `Composite` class from SWT and should work in just about all the same situations. You can also use it in WindowBuilderPro to lay out the widget, however it is not designed to hold children of its own.

    GLComposite glComposite = new GLComposite(parent, SWT.NONE);

To configure the OpenGL context for it, use the `BridgeConfig` class. Here you can set different parameters for how it's rendered, like maximum framerate and whether it should listen for user input. Most importantly, you must set the context calls themselves using `setContext`. This takes an argument of the interface `BridgeContext` which only holds the methods `init`, `update` and `shutdown`. You can put OpenGL code directly in here, or redirect the calls to your own implementations. Remember that the `update` method should only include a single iteration, not a loop.

    BridgeConfig config = new BridgeConfig()
      .setFPSLimit(60)
      .setLoopingEnabled(true)
      .setContext(new BridgeContext() {
        public void init() {}
        public void update() {}
        public void shutdown() {}
      });

With your `BridgeConfig` instance, apply it to your `GLComposite` using `setConfig` or the third parameter in the composite's constructor. Don't forget to call `init` on it when it's ready.

    glComposite.setConfig(config);
    glComposite.init();
    
Have a look at the [example class](https://github.com/Mudbill/lwjgl3-swt-bridge/blob/master/src/examples/BridgeFormExample.java) for how it can be implemented.
