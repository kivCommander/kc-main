package core;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import cz.zcu.kiv.kc.plugin.Plugin;
import cz.zcu.kiv.kc.plugin.TestPlugin;
import cz.zcu.kiv.kc.shell.ShellController;

public class Activator implements BundleActivator {
	private final ShellController shell = new ShellController();
	private final JFrame frame = new JFrame("kivCommander");

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		Bundle[] bundles = context.getBundles();
		for (Bundle budle : bundles) {
			ServiceReference<?>[] services = budle.getRegisteredServices();
			if (services != null) {
				for (ServiceReference<?> serviceReference : services) {
					Object service = context.getService(serviceReference);
					if (service instanceof Plugin) {
						shell.addPlugin((Plugin) service);
					}
				}
			}
		}
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				frame.setPreferredSize(new Dimension(800, 600));
				frame.setLayout(new BorderLayout());
				frame.add(shell.getView());
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
				frame.setVisible(true);
			}
		});
		
		// TODO
		shell.addPlugin(new TestPlugin());
	}

	public void stop(BundleContext context) throws Exception {
		frame.setVisible(false);
	}
}
