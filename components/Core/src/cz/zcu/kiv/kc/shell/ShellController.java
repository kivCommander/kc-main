package cz.zcu.kiv.kc.shell;

import java.awt.Component;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;

import cz.zcu.kiv.kc.plugin.I18N;
import cz.zcu.kiv.kc.plugin.Plugin;

/**
 * Controller for the application.
 * @author Michal
 *
 */
public class ShellController
{
	private Map<String, JButton> buttons = new HashMap<String, JButton>();
	private ShellView view;
	
	public ShellController() {
		view = new ShellView();
	}

	/**
	 * current selection getter
	 * @return current selection
	 */
	public List<File> getSelectedFiles() {
		return view.getSelectedFiles();
	}

	/**
	 * current destination path getter
	 * @return current destination path
	 */
	public String getDestiantionPath() {
		return view.getDestinationFolder();
	}

	/**
	 * current source path getter
	 * @return current source path
	 */
	public String getSourcePath() {
		return view.getSourcePath();
	}

	/**
	 * Adds given plug-in to plug-in menu.
	 * @param plugin
	 */
	public void addPlugin(Plugin plugin) {
		JButton button = new JButton();
		button.setText(I18N.getText(plugin.getName()));
		button.addActionListener(new PluginButtonListener(plugin, this));
		buttons.put(I18N.getText(plugin.getName()), button);
		view.addButton(button);		
	}

	/**
	 * Removes given plug-in form plug-in menu.
	 * @param plugin
	 */
	public void removePlugin(Plugin plugin) {
		JButton button = buttons.remove(plugin.getName());
		view.removeButton(button);
	}

	/**
	 * view getter
	 * @return view
	 */
	public Component getView() {
		return view;
	}

	/**
	 * Refreshes the directory panel that is showing directory "dir".
	 * @param dir
	 */
	public void refresh(String dir) {
		view.refresh(dir);			
	}

	/**
	 * Refreshes both directory panels.
	 */
	public void refresh() {
		view.refresh();			
	}
}
