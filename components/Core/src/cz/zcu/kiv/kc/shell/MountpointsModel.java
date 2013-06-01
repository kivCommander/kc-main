package cz.zcu.kiv.kc.shell;

import java.io.File;

import javax.swing.DefaultComboBoxModel;

/**
 * data model for volume selection in directory panel
 * @author Michal
 *
 */
public class MountpointsModel extends DefaultComboBoxModel<File>
{
	private static final long serialVersionUID = 2538077815865507115L;

	public MountpointsModel()
	{
		super(File.listRoots());
	}
}
