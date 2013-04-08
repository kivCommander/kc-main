package cz.zcu.kiv.kc.shell;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class ShellView extends JPanel {

	private static final long serialVersionUID = 3313986206312859936L;
	private DirectoryPanel leftDirectoryView = new DirectoryPanel();
	private DirectoryPanel rightDirectoryView = new DirectoryPanel();
	private JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));

	public ShellView() {
		setLayout(new BorderLayout());	
		leftDirectoryView.setBorder(BorderFactory.createTitledBorder("Left panel"));
		rightDirectoryView.setBorder(BorderFactory.createTitledBorder("Right panel"));
		JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftDirectoryView, rightDirectoryView);
		jSplitPane.setOneTouchExpandable(true);
		jSplitPane.setResizeWeight(0.5);
		add(jSplitPane, BorderLayout.CENTER);	
		actions.setBorder(BorderFactory.createTitledBorder("Actions")); 
		add(actions, BorderLayout.PAGE_END);
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
