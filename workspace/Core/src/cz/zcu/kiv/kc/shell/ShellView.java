package cz.zcu.kiv.kc.shell;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ShellView extends JPanel {

	private static final long serialVersionUID = 3313986206312859936L;
	private DirectoryPanel leftDirectoryView = new DirectoryPanel();
	private DirectoryPanel rightDirectoryView = new DirectoryPanel();
	private JPanel actions = new JPanel();

	public ShellView() {
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(new GridBagLayout());
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		leftDirectoryView.setBorder(BorderFactory.createTitledBorder("Left panel"));
		add(leftDirectoryView, gbc);	
		rightDirectoryView.setBorder(BorderFactory.createTitledBorder("Right panel"));
		gbc.gridx = 1;
		add(rightDirectoryView, gbc);
		actions.setBorder(BorderFactory.createTitledBorder("Actions"));
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(actions, gbc);
	}

	public String getDestinationFolder() {
		return leftDirectoryView.getCurrentFolder();
	}

	public List<File> getSelectedFiles() {
		return leftDirectoryView.getSelectedFiles();
	}

	public void addButton(JButton button) {
		// TODO
		actions.add(button);
	}

	public void removeButton(JButton button) {
		// TODO
		actions.remove(button);
	}

}
