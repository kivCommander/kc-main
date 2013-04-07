package cz.zcu.kiv.kc.shell;

import java.awt.Component;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;

import cz.zcu.kiv.kc.plugin.Plugin;

public class ShellController {
	private Map<String, JButton> buttons = new HashMap<String, JButton>();
	private ShellView view;
	
	public ShellController() {
		view = new ShellView();
	}

	public List<File> getSelectedFiles() {
		return view.getSelectedFiles();
	}

	public String getDestiantionPath() {
		return view.getDestinationFolder();
	}

	public void addPlugin(Plugin plugin) {
		JButton button = new JButton();
		button.setText(plugin.getName());
		button.addActionListener(new PluginButtonListener(plugin, this));
		buttons.put(plugin.getId(), button);
		view.addButton(button);		
	}

	public void removePlugin(Plugin plugin) {
		JButton button = buttons.remove(plugin.getId());
		view.removeButton(button);
	}

	public Component getView() {
		return view;
	}
}
