package core;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
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
