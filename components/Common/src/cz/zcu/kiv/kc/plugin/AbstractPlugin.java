package cz.zcu.kiv.kc.plugin;

import java.awt.Window;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

/**
 * Convenient base class for all plugins.
 * @author Michal
 *
 */
public abstract class AbstractPlugin implements Plugin {

	protected EventAdmin eventAdmin;

	protected Window mainWindow;
	
	/**
	 * Sends refresh event for given target directory. Should be called after plug-in's execution.
	 * @param dirToRefresh - target directory to refresh
	 */
	protected void sendEvent(String dirToRefresh) {
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("dir", dirToRefresh);
		eventAdmin.sendEvent(new Event("refresh", properties));
	}

	/**
	 * setter for OSGi event manager
	 * @param eventAdmin
	 */
	public void setEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}

	/**
	 * Convenient method for asking for name.
	 * @param title of the dialog
	 * @return name given by user
	 */
	protected String askForName(String title) {
		return JOptionPane.showInputDialog(title);
	}

	@Override
	public void setMainWindow(Window win)
	{
		this.mainWindow = win;
	}
}
