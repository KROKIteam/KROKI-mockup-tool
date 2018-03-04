package framework;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.h2.tools.Server;

import gui.menu.MyMenuBar;
import util.ResourceLoader;

public class MainFrame extends JDialog {

	
	private static MainFrame instance;
	private Map<String, String> panelType = new HashMap<String,String>();
	static Server server = null;
	public static MainFrame getInstance() {
		if (instance == null)
			instance = new MainFrame(true);
		return instance;
	}
	
	public static MainFrame getNewInstance(boolean showApp) {
		instance = new MainFrame(showApp);
		return instance;
	}
	
	private MainFrame(boolean showApp) {
		if(showApp)
			initialize();
		runDb();
		HibernateUtil.getSessionfactory();
	}
	
	public static void runDb() {
		if (server != null)
			return;
		try {
		    server = Server.createTcpServer("-tcpSSL").start();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initialize() {
		setTitle("KROKI Administration Subsystem");
		Container content = getContentPane();
		content.setBackground(Color.WHITE);
		//setExtendedState(Frame.MAXIMIZED_BOTH);
		setSize(800,600);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
//		menu  = new GenericMenu();
		

		ImageIcon image = new ResourceLoader().loadImageIcon("users-icon.png");
		JLabel label = new JLabel("", image, JLabel.CENTER);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add( label, BorderLayout.CENTER );
		panel.setOpaque(true);
		panel.setBackground(Color.WHITE);

		setJMenuBar(new MyMenuBar());
		add(panel, BorderLayout.CENTER);
		setLocationRelativeTo(null);
	}
	
	

	public Map<String, String> getPanelType() {
		return panelType;
	}

	public void setPanelType(Map<String, String> panelType) {
		this.panelType = panelType;
	}
	
	WindowListener listener = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			server.stop();
		}
	};

}
