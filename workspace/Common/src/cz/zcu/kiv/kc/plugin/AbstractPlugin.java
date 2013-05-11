package cz.zcu.kiv.kc.plugin;

import java.awt.Window;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

public abstract class AbstractPlugin implements Plugin {

	protected EventAdmin eventAdmin;

	protected Window mainWindow;
		
	protected void sendEvent(String dirToRefresh) { 
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("dir", dirToRefresh);
		eventAdmin.sendEvent(new Event("refresh", properties));
	}

	public void setEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}

	protected String askForName(String title) {
		return JOptionPane.showInputDialog(title);
	}
	
	@Override
	public void setMainWindow(Window win)
	{
		this.mainWindow = win;
	}
}
