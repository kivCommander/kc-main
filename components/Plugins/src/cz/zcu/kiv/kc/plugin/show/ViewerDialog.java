package cz.zcu.kiv.kc.plugin.show;

import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

/**
 * Empty viewer dialog.
 * @author Michal
 *
 */
public class ViewerDialog extends JDialog
{
	private static final long serialVersionUID = -5943844662778700732L;

	public ViewerDialog()
	{
		super();
		UIManager.put("ClassLoader", getClass().getClassLoader());
	}
	
	public ViewerDialog(Object object, ModalityType applicationModal, JComponent component)
	{
		super((Window) object, applicationModal);
		this.add(new JScrollPane(component));
		this.pack();
		this.setLocationRelativeTo((Window) object);
		this.setVisible(true);
	}
}
