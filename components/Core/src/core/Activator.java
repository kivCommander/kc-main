package core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.springframework.osgi.context.BundleContextAware;

import cz.zcu.kiv.kc.interfaces.ICopyPlugin;
import cz.zcu.kiv.kc.interfaces.ICreateDirPlugin;
import cz.zcu.kiv.kc.interfaces.IDeletePlugin;
import cz.zcu.kiv.kc.interfaces.IMovePlugin;
import cz.zcu.kiv.kc.interfaces.IViewPlugin;
import cz.zcu.kiv.kc.plugin.I18N;
import cz.zcu.kiv.kc.plugin.Plugin;
import cz.zcu.kiv.kc.shell.PluginButtonListener;
import cz.zcu.kiv.kc.shell.ShellController;

/**
 * Main application. Creates main window and registers plug-ins.
 * @author Michal
 *
 */
public class Activator implements EventHandler, BundleContextAware {
	private BundleContext context;

	private final ShellController shell = new ShellController();
	// private Set<Plugin> plugins;
	private IViewPlugin viewPlugin;
	private ICreateDirPlugin createDirPlugin;
	private IDeletePlugin deletePlugin;
	private IMovePlugin movePlugin;
	private ICopyPlugin copyPlugin;

	private Action exitProgram = new AbstractAction() {
		private static final long serialVersionUID = -4656026664533500981L;

		// contructor
		{
			putValue(NAME, I18N.getText("exit"));
			putValue(SHORT_DESCRIPTION, I18N.getText("exitShotDesc"));
			putValue(MNEMONIC_KEY, KeyEvent.VK_T);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4,
					KeyEvent.ALT_DOWN_MASK));
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// dispose of all existing windows
			for (Window win : Window.getWindows()) {
				win.dispose();
			}

			// close OSGi framework
			try {
				Activator.this.context.getBundle(0).stop();
			} catch (BundleException e1) {
				e1.printStackTrace();
			}
		}
	};
	
	private final JFrame frame = new JFrame(I18N.getText("title"));
	{
		this.frame.setPreferredSize(new Dimension(800, 600));
		this.frame.setLayout(new BorderLayout());
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	private JMenuBar menuBar = new JMenuBar();
	{
		this.frame.setJMenuBar(this.menuBar);
	}
	private JMenu fileMenu = new JMenu(I18N.getText("file"));
	{
		this.menuBar.add(this.fileMenu);
		this.fileMenu.add(this.exitProgram);
	}
	private JMenu pluginsMenu = new JMenu(I18N.getText("plugins"));
	{
		this.menuBar.add(this.pluginsMenu);
	}

	/**
	 * bundle initialization method
	 * @throws Exception
	 */
	public void start() throws Exception {
		// necessary for proper working under OSGi environment.  
		UIManager.put("ClassLoader", getClass().getClassLoader());

		SwingUtilities.invokeAndWait(new Runnable() {

			@Override
			public void run() {
				shell.addPlugin(Activator.this.viewPlugin);
				shell.addPlugin(Activator.this.movePlugin);
				shell.addPlugin(Activator.this.copyPlugin);
				shell.addPlugin(Activator.this.createDirPlugin);
				shell.addPlugin(Activator.this.deletePlugin);

				frame.add(shell.getView());
				frame.pack();
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				shell.refresh();
			}
		});
	}

	/**
	 * bundle stop method
	 * @throws Exception
	 */
	public void stop() throws Exception {
		frame.setVisible(false);
	}

	/**
	 * setter for implementations of Plugin interface. Called by spring-dm.
	 * @param plugins
	 */
	public void setPlugins(final Set<Plugin> plugins) {
		// this.plugins = plugins;

		for (Plugin plugin : plugins) {
			plugin.setMainWindow(this.frame);
		}

		JMenuItem plugItem;
		for (Plugin plugin : plugins) {
			plugItem = new JMenuItem(I18N.getText(plugin.getName()));
			plugItem.addActionListener(new PluginButtonListener(plugin, shell));
			this.pluginsMenu.add(plugItem);
		}
	}

	/**
	 * spring-dm: setter for ICreateDirPlugin impl.
	 * @param plugin
	 */
	public void setCreateDirPlugin(ICreateDirPlugin plugin) {
		this.createDirPlugin = plugin;
		plugin.setMainWindow(this.frame);
	}

	/**
	 * spring-dm: setter for IDeletePlugin impl
	 * @param plugin
	 */
	public void setDeletePlugin(IDeletePlugin plugin) {
		this.deletePlugin = plugin;
		plugin.setMainWindow(this.frame);
	}

	/**
	 * spring-dm: setter for IMovePlugin impl
	 * @param plugin
	 */
	public void setMovePlugin(IMovePlugin plugin) {
		this.movePlugin = plugin;
		plugin.setMainWindow(this.frame);
	}

	/**
	 * spring-dm: setter for IViewPlugin impl
	 * @param plugin
	 */
	public void setViewPlugin(IViewPlugin plugin) {
		this.viewPlugin = plugin;
		plugin.setMainWindow(this.frame);
	}
	
	/**
	 * spring-dm: setter for ICopyPlugin impl
	 * @param plugin
	 */
	public void setCopyPlugin(ICopyPlugin plugin) {
		this.copyPlugin = plugin;
		plugin.setMainWindow(this.frame);
	}
	
	/**
	 * Handles event. If event object contains "dir" property, executes model refresh for given directory.
	 */
	@Override
	public void handleEvent(Event event) {
		if (!event.containsProperty("dir"))
		{
			return;
		}
		
		String dir = (String) event.getProperty("dir");
		shell.refresh(dir);
	}

	/**
	 * OSGi: bundle context setter
	 */
	@Override
	public void setBundleContext(BundleContext context) {
		this.context = context;
	}
}
