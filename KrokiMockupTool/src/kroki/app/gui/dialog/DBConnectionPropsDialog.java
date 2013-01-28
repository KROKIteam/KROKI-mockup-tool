package kroki.app.gui.dialog;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

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
	private JButton btnOK;
	private JButton btnCancel;
	
	private String[] mySQLDialects = {"MySQLDialect", "MySQL5Dialect", "MySQL5InnoDBDialect", "MySQLInnoDBDialect", "MySQLMyISAMDialect"};
	private String[] postgreSQLDialects = {"PostgreSQLDialect", "PostgresPlusDialect", "ProgressDialect"};
	private String[] msSQLDialects = {"SQLServerDialect", "SQLServer2008Dialect"};
	private String[] h2Dialects = {"H2Dialect", };
	private String[] customDialects = {"Cache71Dialect", "DataDirectOracle9Dialect", "DB2390Dialect", "DB2400Dialect", "DB2Dialect", "DerbyDialect",
			                           "Dialect", "FirebirdDialect", "FrontBaseDialect", "H2Dialect", "HSQLDialect", "HSQLDialect.ReadUncommittedLockingStrategy",
			                           "InformixDialect", "Ingres10Dialect", "Ingres9Dialect", "IngresDialect", "InterbaseDialect", "JDataStoreDialect",
			                           "MckoiDialect", "MimerSQLDialect", "MySQL5Dialect", "MySQL5InnoDBDialect", "MySQLDialect", "MySQLInnoDBDialect",
			                           "MySQLMyISAMDialect", "Oracle10gDialect", "Oracle8iDialect", "Oracle9Dialect", "Oracle9iDialect", "OracleDialect",
			                           "PointbaseDialect", "PostgresPlusDialect", "PostgreSQLDialect", "ProgressDialect", "RDMSOS2200Dialect", "SAPDBDialect",
			                           "SQLServer2008Dialect", "SQLServerDialect", "Sybase11Dialect", "SybaseAnywhereDialect", "SybaseASE15Dialect",
			                           "SybaseDialect", "TeradataDialect", "TimesTenDialect"};
	
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
		cbProfile = new JComboBox(new String[] {"MySQL", "PostgreSQL", "SQL Server (jTDS)", "SQL Server (Microsoft Driver)", "H2", "Custom profile..."});
		cbProfile.setSelectedIndex(project.getDBConnectionProps().getProfile());
		cbProfile.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				int index = cbProfile.getSelectedIndex();
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
				case 5:
					tfDriver.setText("");
					tfDriver.setEnabled(true);
					cbDialect.setModel(new DefaultComboBoxModel(customDialects));
					tfHost.setText("");
					tfPort.setText("");
					tfSchema.setText("");
					tfUsername.setText("");
					pfPassword.setText("");
					break;
				default:
					break;
				}
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
		
		btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
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
		});
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DBConnectionPropsDialog.this.dispose();
				DBConnectionPropsDialog.this.setVisible(false);
			}
		});
		
		add(lblTitle, "span 2, wrap, center, gaptop10, gapbottom 20");
		add(lblProfile);
		add(cbProfile, "wrap, gapbottom 20, left, growx");
		add(lblDriver);
		add(tfDriver, "wrap");
		add(lblDialect);
		add(cbDialect, "wrap, growx");
		add(lblHost);
		add(tfHost, "wrap");
		add(lblPort);
		add(tfPort, "wrap");
		add(lblSchema);
		add(tfSchema, "wrap");
		add(lblUsername);
		add(tfUsername, "wrap");
		add(lblPassword);
		add(pfPassword, "wrap");
		add(new JLabel());
		add(btnOK, "split 2, gaptop 20");
		add(btnCancel);
		
		pack();
	}
	
}
