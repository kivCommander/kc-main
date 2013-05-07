package core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import cz.zcu.kiv.kc.plugin.Plugin;
import cz.zcu.kiv.kc.shell.ShellController;

public class Activator implements EventHandler {
	private final ShellController shell = new ShellController();
	private final JFrame frame = new JFrame("kivCommander");
	private Set<Plugin> plugins;
	
	public void start() throws Exception {
		UIManager.put("ClassLoader", getClass().getClassLoader());
		
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				//shell.addPlugin(new TestPlugin());
				for (Plugin plugin : plugins) {
					plugin.setMainWindow(frame);
					shell.addPlugin((Plugin) plugin);
				}
				frame.setPreferredSize(new Dimension(800, 600));
				frame.setLayout(new BorderLayout());
				frame.add(shell.getView());
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				shell.refresh();
			}
		});
	}

	public void stop(BundleContext context) throws Exception {
		frame.setVisible(false);
	}

	public void setPlugins(final Set<Plugin> plugins) {
		this.plugins = plugins;
	}

	@Override
	public void handleEvent(Event event) {
		String dir = (String) event.getProperty("dir");
		shell.refresh(dir);
	}
}
