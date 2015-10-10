package graphedit.gui.utils;

import graphedit.app.MainFrame;

import javax.swing.JOptionPane;

public class Dialogs {

	public static void showInformationMessage(String message, String title) {
		showMessage(message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void showErrorMessage(String message, String title) {
		showMessage(message, title, JOptionPane.ERROR_MESSAGE);
	}

	private static void showMessage(String message, String title, int type) {
		JOptionPane.showMessageDialog(
				MainFrame.getInstance(), 
				message, 
				title, 
				type
		);
	}
	
	public static int showYesNoDialog(String message, String title) {
		return showConfirmDialog(message, title, JOptionPane.YES_NO_OPTION);
	}
	
	public static int showYesNoCancelDialog(String message, String title) {
		return showConfirmDialog(message, title, JOptionPane.YES_NO_CANCEL_OPTION);
	}
	
	private static int showConfirmDialog(String message, String title, int optionType) {
		return JOptionPane.showConfirmDialog(
				MainFrame.getInstance(), 
				message, 
				title,
				optionType, 
				JOptionPane.QUESTION_MESSAGE
		);
	}
	
	public static String showInputDialog(String message, String title) {
		return JOptionPane.showInputDialog(
				MainFrame.getInstance(), 
				message, 
				title, 
				JOptionPane.QUESTION_MESSAGE
		);
	}
	
}
