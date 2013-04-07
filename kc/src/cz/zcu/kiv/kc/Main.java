package cz.zcu.kiv.kc;

import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import cz.zcu.kiv.kc.shell.ShellController;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final ShellController shell = new ShellController();
		try {
			SwingUtilities.invokeAndWait(new Runnable() {				
				@Override
				public void run() {					
					JFrame frame = new JFrame("kivCommander");
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.getContentPane().add(shell.getView(), BorderLayout.CENTER);
					frame.pack();
					frame.setVisible(true);
				}
			});
		} catch (InvocationTargetException e) {
			// TODO
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO
			e.printStackTrace();
		}
	}
}
