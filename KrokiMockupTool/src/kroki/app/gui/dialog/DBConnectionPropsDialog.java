package kroki.app.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import net.miginfocom.swing.MigLayout;

import kroki.app.utils.ImageResource;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.profil.utils.DatabaseProps;

public class DBConnectionPropsDialog extends JDialog {

	private JLabel lblTitle;
	private JLabel lblProfile;
	private JComboBox cbProfile;
	private JLabel lblDriver;
	private JTextField tfDriver;
	private JLabel lblDialect;
	private JComboBox cbDialect;
	private JLabel lblHost;
	private JTextField tfHost;
	private JLabel lblPort;
	private JTextField tfPort;
	private JLabel lblSchema;
	private JTextField tfSchema;
	private JLabel lblUsername;
	private JTextField tfUsername;
	private JLabel lblPassword;
	private JPasswordField pfPassword;
	private JPanel testPane;
	private JLabel lblStatus;
	private JButton btnTest;
	private JButton btnOK;
	private JButton btnCancel;

	private String[] mySQLDialects = {"MySQLDialect", "MySQL5Dialect", "MySQL5InnoDBDialect", "MySQLInnoDBDialect", "MySQLMyISAMDialect"};
	private String[] postgreSQLDialects = {"PostgreSQLDialect", "PostgresPlusDialect", "ProgressDialect"};
	private String[] msSQLDialects = {"SQLServerDialect", "SQLServer2008Dialect"};
	private String[] h2Dialects = {"H2Dialect", };

	public DBConnectionPropsDialog(BussinesSubsystem project) {
		setSize(300, 330);
		setLocationRelativeTo(null);
		setModal(true);
		setAlwaysOnTop(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Database connection settings");
		Image headerIcon = ImageResource.getImageResource("app.logo32x32");
		setIconImage(headerIcon);

		setLayout(new MigLayout(
				"",
				"[][right, grow]",
				""));
		initGUI(project);
	}

	public void initGUI(final BussinesSubsystem project) {

		lblTitle = new JLabel("Database settings");
		lblTitle.setFont(new Font("sansserif", Font.PLAIN, 16));

		lblProfile = new JLabel("Profile");
		cbProfile = new JComboBox(new String[] {"MySQL", "PostgreSQL", "SQL Server (jTDS)", "SQL Server (Microsoft Driver)", "H2"});
		cbProfile.setSelectedIndex(project.getDBConnectionProps().getProfile());
		cbProfile.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				int index = cbProfile.getSelectedIndex();
				changeProfile(index);
			}
		});

		lblDriver = new JLabel("Driver class");
		tfDriver = new JTextField(30);
		tfDriver.setEnabled(false);
		tfDriver.setText(project.getDBConnectionProps().getDriverClass());

		lblDialect = new JLabel("Dialect");
		cbDialect = new JComboBox();
		cbDialect.setModel(new DefaultComboBoxModel(mySQLDialects));

		lblHost = new JLabel("Host URL");
		tfHost = new JTextField(30);
		tfHost.setText(project.getDBConnectionProps().getHost());

		lblPort = new JLabel("Port");
		tfPort = new JTextField(10);
		tfPort.setText(Integer.toString(project.getDBConnectionProps().getPort()));

		lblSchema = new JLabel("Schema name");
		tfSchema = new JTextField(30);
		tfSchema.setText(project.getDBConnectionProps().getSchema());

		lblUsername = new JLabel("Username");
		tfUsername = new JTextField(20);
		tfUsername.setText(project.getDBConnectionProps().getUsername());

		lblPassword = new JLabel("Password");
		pfPassword = new JPasswordField(20);
		pfPassword.setText(project.getDBConnectionProps().getPassword());

		testPane = new JPanel();
		lblStatus = new JLabel();
		btnTest = new JButton("Test");

		btnTest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String user = tfUsername.getText();
				String pass = new String(pfPassword.getPassword());
				String driver = tfDriver.getText();
				String protocol = getProtocol(cbProfile.getSelectedIndex());
				String schema = tfSchema.getText();
				String port = tfPort.getText();
				String host = tfHost.getText();

				String url = "jdbc:" + protocol + "://" + host  + ":" + port + "/" + schema;
				
				String stat = checkRequiredFields();
				if(!stat.equals("OK")) {
					displayMessage(stat, true);
				}else {
					testConnection(url, user, pass, driver);
				}
			}
		});

		testPane.setLayout(new MigLayout(
				"",
				"[left, grow][right]",
				""));
		testPane.add(lblStatus);
		testPane.add(btnTest, "dock east");

		btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String stat = checkRequiredFields();
				if(!stat.equals("OK")) {
					displayMessage(stat, true);
				}else {
					assignSettingstoProject(project);
				}
			}
		});

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				DBConnectionPropsDialog.this.dispose();
				DBConnectionPropsDialog.this.setVisible(false);
			}
		});

		Dimension separatorDim = new Dimension(320, 5);
		JSeparator topSep = new JSeparator(JSeparator.HORIZONTAL);
		topSep.setPreferredSize(separatorDim);
		JSeparator middleSep = new JSeparator(JSeparator.HORIZONTAL);
		middleSep.setPreferredSize(separatorDim);
		JSeparator bottomSep = new JSeparator(JSeparator.HORIZONTAL);
		bottomSep.setPreferredSize(separatorDim);

		add(lblTitle, "span 2, wrap, center, gaptop10, gapbottom 10");
		add(lblProfile);
		add(cbProfile, "wrap, left, growx");
		add(topSep, "span 2, wrap, gapbottom 5, gaptop 5, growx");
		add(lblDriver);
		add(tfDriver, "wrap, growx");
		add(lblDialect);
		add(cbDialect, "wrap, growx");
		add(lblHost);
		add(tfHost, "wrap, growx");
		add(lblPort);
		add(tfPort, "wrap");
		add(lblSchema);
		add(tfSchema, "wrap, growx");
		add(lblUsername);
		add(tfUsername, "wrap");
		add(lblPassword);
		add(pfPassword, "wrap");
		add(middleSep, "span 2, wrap, gaptop 5, growx");
		add(testPane, "wrap, span 2, grow");
		add(bottomSep, "span 2, wrap, gaptop 5, growx");
		add(new JLabel());
		add(btnOK, "split 2, gaptop 5");
		add(btnCancel);

		pack();
	}

	public void changeProfile(int index) {
		switch (index) {
		case 0:
			tfDriver.setText("com.mysql.jdbc.Driver");
			tfPort.setText("3306");
			tfDriver.setEnabled(false);
			cbDialect.setModel(new DefaultComboBoxModel(mySQLDialects));
			break;
		case 1:
			tfDriver.setText("org.postgresql.Driver");
			tfPort.setText("5432");
			tfDriver.setEnabled(false);
			cbDialect.setModel(new DefaultComboBoxModel(postgreSQLDialects));
			break;
		case 2:
			tfDriver.setText("net.sourceforge.jtds.jdbc.Driver");
			tfPort.setText("1433");
			tfDriver.setEnabled(false);
			cbDialect.setModel(new DefaultComboBoxModel(msSQLDialects));
			break;
		case 3:
			tfDriver.setText("com.microsoft.jdbc.sqlserver.SQLServerDriver");
			tfPort.setText("1433");
			tfDriver.setEnabled(false);
			cbDialect.setModel(new DefaultComboBoxModel(msSQLDialects));
			break;
		case 4:
			tfDriver.setText("org.h2.Driver");
			tfPort.setText("");
			tfDriver.setEnabled(false);
			cbDialect.setModel(new DefaultComboBoxModel(h2Dialects));
			break;
		default:
			break;
		}
	}

	public void assignSettingstoProject(BussinesSubsystem project) {
		int profile = cbProfile.getSelectedIndex();
		String driver = tfDriver.getText().trim();
		String dialect = cbDialect.getSelectedItem().toString();
		String url = tfHost.getText().trim();
		int port = Integer.parseInt(tfPort.getText().trim());
		String schema = tfSchema.getText().trim();
		String username = tfUsername.getText().trim();
		String password = new String(pfPassword.getPassword());

		DatabaseProps props = new DatabaseProps(profile, driver, url, port, schema, username, password, dialect);
		project.setDBConnectionProps(props);
		DBConnectionPropsDialog.this.dispose();
		DBConnectionPropsDialog.this.setVisible(false);
	}

	public void testConnection(String url, String username, String password, String driver) {
		Properties properties = new Properties();
		properties.put("user", username);
		properties.put("password", password);
		properties.put("characterEncoding", "ISO-8859-1");
		properties.put("useUnicode", "true");

		try {
			btnTest.setEnabled(false);
			Class.forName(driver).newInstance();
			Connection c = DriverManager.getConnection(url, properties);
			displayMessage("Your settings seem ok.", false);
			c.close();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			displayMessage("Driver class not found!", true);
		} catch (SQLException e) {
			if(e.getMessage().startsWith("Communication")) {
				displayMessage("Link error! Check host and port settings!", true);
			}else {
				displayMessage(e.getMessage(), true);
			}
		}
		btnTest.setEnabled(true);
	}

	public void displayMessage(String message, boolean error) {
		if(error) {
			lblStatus.setForeground(Color.RED);
		}else {
			lblStatus.setForeground(new Color(0, 102, 51));
		}
		lblStatus.setText("<html><p>" + message + "</p></html>");
	}
	
	public String getProtocol(int profile) {
		switch (profile) {
		case 0:
			return "mysql";
		case 1:
			return "postgresql";
		case 2:
			return "jtds:sqlserver";
		case 3:
			return "microsoft:sqlserver";
		case 4:
			return "h2";
		default:
			return "mysql";
		}
	}
	
	public String checkRequiredFields() {
		String status = "OK";
		
		if(tfHost.getText().equals("")) {
			status = "You must provide host URL!";
		}else if (tfPort.getText().equals("")) {
			status = "You must provide port number!";
		}else if (tfSchema.getText().equals("")) {
			status = "You must provide schema name!";
		}
		
		return status;
	}
}
