package app;

import java.sql.SQLException;

import org.h2.tools.Server;

import framework.MainFrame;

public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		MainFrame frame = MainFrame.getInstance();
		frame.setVisible(true);
	}

}
