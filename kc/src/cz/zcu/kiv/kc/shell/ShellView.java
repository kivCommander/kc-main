package cz.zcu.kiv.kc.shell;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ShellView extends JPanel implements ActionListener {
	private static final int REFRESH_DELAY = 1000;
	private boolean refreshInProgress = false;

	private static final long serialVersionUID = 3313986206312859936L;

	// TODO could be replaced by components
	private String destFolder;
	private List<File> selectedFiles;

	public ShellView() {
		Timer timer = new Timer(REFRESH_DELAY, this);
		timer.start();
		// TODO build the view
	}

	public String getDestinationFolder() {
		// TODO return value from the JTextField 
		return destFolder;
	}

	public List<File> getSelectedFiles() {
		// TODO from the JList
		return selectedFiles;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (refreshInProgress) {
			// previous refresh is in process, skip this round
		}else{
			refreshInProgress = true;
			refreshLists();
			refreshInProgress = false;
		}
	}

	private void refreshLists() {
		// TODO only refresh the lists here
		destFolder = "" + System.currentTimeMillis();
		selectedFiles = Arrays.asList(
				new File("a" + System.currentTimeMillis()), new File("b"
						+ System.currentTimeMillis()));
	}

	public void addButton(JButton button) {
		// TODO
		add(button);
	}

	public void removeButton(JButton button) {
		// TODO
		remove(button);
	}

}
