package cz.zcu.kiv.kc.plugin;

import java.util.HashMap;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

public abstract class AbstractPlugin implements Plugin {

	private EventAdmin eventAdmin;

	protected void sendEvent(String dirToRefresh) {
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("dir", dirToRefresh);
		eventAdmin.sendEvent(new Event("refresh", properties));
	}

	public void setEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}
}