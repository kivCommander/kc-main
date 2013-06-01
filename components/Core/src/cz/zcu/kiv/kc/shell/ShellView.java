package cz.zcu.kiv.kc.shell;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import cz.zcu.kiv.kc.plugin.I18N;

/**
 * View for the application
 * @author Michal
 *
 */
public class ShellView extends JPanel implements FocusListener {

	private static final long serialVersionUID = 3313986206312859936L;
	private DirectoryPanel leftDirectoryView = new DirectoryPanel();
	private DirectoryPanel rightDirectoryView = new DirectoryPanel();
	private DirectoryPanel directoryViewWithFocus = leftDirectoryView;
	private JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));

	public ShellView() {
		setLayout(new BorderLayout());
		leftDirectoryView.setBorder(BorderFactory
				.createTitledBorder(I18N.getText("leftPanel")));
		leftDirectoryView.addFocusListener(this);
		rightDirectoryView.setBorder(BorderFactory
				.createTitledBorder(I18N.getText("rightPanel")));
		rightDirectoryView.addFocusListener(this);
		JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				leftDirectoryView, rightDirectoryView);
		jSplitPane.setOneTouchExpandable(true);
		jSplitPane.setResizeWeight(0.5);
		add(jSplitPane, BorderLayout.CENTER);
		actions.setBorder(BorderFactory.createTitledBorder(I18N.getText("actions")));
		add(actions, BorderLayout.PAGE_END);
		refresh();
	}

	public String getDestinationFolder() {
		return directoryViewWithFocus == leftDirectoryView ? rightDirectoryView
				.getCurrentFolder() : leftDirectoryView.getCurrentFolder();
	}

	public String getSourcePath() {
		return directoryViewWithFocus == leftDirectoryView ? leftDirectoryView
				.getCurrentFolder() : rightDirectoryView.getCurrentFolder();
	}

	public List<File> getSelectedFiles() {
		return directoryViewWithFocus == leftDirectoryView ? leftDirectoryView
				.getSelectedFiles() : rightDirectoryView.getSelectedFiles();
	}

	public void addButton(JButton button) {
		actions.add(button);
	}

	public void removeButton(JButton button) {
		actions.remove(button);
	}

	public void refresh(String dir) {
		if (new File(leftDirectoryView.getCurrentFolder())
				.equals(new File(dir))) {
			leftDirectoryView.refreshLists();
		}
		if (new File(rightDirectoryView.getCurrentFolder())
				.equals(new File(dir))) {
			rightDirectoryView.refreshLists();
		}
	}

	public void refresh() {
		leftDirectoryView.refreshLists();
		rightDirectoryView.refreshLists();
	}

	@Override
	public void focusGained(FocusEvent e) {
		directoryViewWithFocus = (DirectoryPanel) e.getComponent();
		if(directoryViewWithFocus == leftDirectoryView){
			rightDirectoryView.clearSelection();
		}else{
			leftDirectoryView.clearSelection();
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub

	}
}
