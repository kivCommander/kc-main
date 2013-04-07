package core;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import cz.zcu.kiv.kc.plugin.Plugin;
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
		UIManager.setLookAndFeel(UIManager
				.getCrossPlatformLookAndFeelClassName());
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setPreferredSize(new Dimension(800, 600));
				JPanel content = new JPanel(new GridBagLayout());
				frame.getContentPane().add(content);
				content.add(shell.getView(), new GridBagConstraints());
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

	public void stop(BundleContext context) throws Exception {
		frame.setVisible(false);
	}
}
