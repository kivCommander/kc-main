package cz.zcu.kiv.kc.shell;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.filechooser.FileSystemView;

public class DirectoryPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 840871288858771069L;

	private boolean refreshInProgress = false;

	private FileListModel listModel = new FileListModel();
	private JList<File> list = new JList<File>(listModel);
	private JTextField field = new JTextField();

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
				list.setSelectedIndex(0);
			}
		});
		JPanel menu = new JPanel(new BorderLayout());
		menu.add(field, BorderLayout.CENTER);
		menu.add(go, BorderLayout.LINE_END);
		add(menu, BorderLayout.PAGE_START);
		add(new JScrollPane(list), BorderLayout.CENTER);

		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				@SuppressWarnings("unchecked")
				JList<File> list = (JList<File>) evt.getSource();
				if (evt.getClickCount() == 2) {
					int index = list.locationToIndex(evt.getPoint());
					File file = list.getModel().getElementAt(index);
					if (file.isDirectory()) {
						field.setText(file.getAbsolutePath());
						listModel.setDirPath(field.getText());
						list.setSelectedIndex(0);
					}
				}
			}
		});
		list.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -8566161868989812047L;
			@Override
			public Component getListCellRendererComponent(
					@SuppressWarnings("rawtypes") JList list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				JLabel jLabel = (JLabel) super.getListCellRendererComponent(
						list, value, index,isSelected, cellHasFocus);
				if (value instanceof FirstFile) {
					jLabel.setText("..");
				} else {
					jLabel.setIcon(FileSystemView.getFileSystemView()
							.getSystemIcon((File)value));
					jLabel.setText(((File)value).getName());
				}
				return jLabel;
			}
		});
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
