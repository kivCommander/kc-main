package cz.zcu.kiv.kc.shell;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;

public class DirectoryPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 840871288858771069L;

	private boolean refreshInProgress = false;

	private FileListModel listModel = new FileListModel();
	private JList<File> list = new JList<File>(listModel);
	private JTextField field = new JTextField("c:/Users/");

	private static final int REFRESH_DELAY = 1000;

	public DirectoryPanel() { 
		Timer timer = new Timer(REFRESH_DELAY, this);
		timer.start();
		setLayout(new BorderLayout());
		JButton go = new JButton("GO");
		go.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listModel.setDirPath(field.getText());

			}
		});
		JPanel menu = new JPanel(new BorderLayout());
		menu.add(field, BorderLayout.CENTER);
		menu.add(go, BorderLayout.LINE_END);
		add(menu, BorderLayout.PAGE_START);
		add(new JScrollPane(list), BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (refreshInProgress) {
			// previous refresh is in process, skip this round
		} else {
			refreshInProgress = true;
			refreshLists();
			refreshInProgress = false;
		}
	}

	private void refreshLists() {
		listModel.refresh();
	}

	public String getCurrentFolder() {
		return field.getText();
	}

	public List<File> getSelectedFiles() {
		return list.getSelectedValuesList();
	}
}
