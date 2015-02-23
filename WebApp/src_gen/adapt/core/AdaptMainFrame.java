package adapt.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.restlet.Component;
import org.restlet.data.Protocol;

import adapt.util.ejb.PersisenceHelper;
import adapt.util.ejb.SchemaGenerator;
import adapt.util.repository_utils.RepositoryPathsUtil;
import adapt.util.staticnames.Settings;
import adapt.util.xml_readers.AdministrationSubsystemReader;
import adapt.util.xml_readers.EntityReader;
import adapt.util.xml_readers.EnumerationReader;
import adapt.util.xml_readers.MenuReader;
import adapt.util.xml_readers.PanelReader;
import adapt.util.xml_readers.TypeComponenMappingReader;
import ejb.User;

/**
 * Main application frame that starts web application and serves as log console.
 * @author Milorad Filipovic
 */
public class AdaptMainFrame extends JFrame {

	private JScrollPane scrollPane;
	private JTextPane statusPane;
	Component component;

	public AdaptMainFrame() {
		setTitle(Settings.APP_TITLE + " [Server log]");
		setMaximumSize(new Dimension(850, 400));
		setMinimumSize(new Dimension(850, 400));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loadIcon();
		component = new Component();
		init();
	}

	private void init() {
		scrollPane = new JScrollPane();
		statusPane = new JTextPane();
		statusPane.setEditable(false);
		statusPane.setFont(new Font("Monospaced",Font.PLAIN,12));
		statusPane.setForeground(Color.white);
		statusPane.setBackground(Color.black);

		AdaptMainFrame.this.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					component.stop();
					System.exit(ABORT);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.getMessage());
					System.exit(ABORT);
				}
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});

		scrollPane.setViewportView(statusPane);
		add(scrollPane, BorderLayout.CENTER);

		try {
			runApp();
			displayText("Server running...", 0);
		} catch (Exception e1) {
			e1.printStackTrace();
			displayStackTrace(e1);
		}
		try {
			Desktop.getDesktop().browse(new URI("http://localhost:8182/"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void runApp() throws Exception {
		component.getServers().add(Protocol.HTTP, 8182);
		component.getClients().add(Protocol.FILE);
		component.getClients().add(Protocol.HTTP);
		AdaptApplication app = AdaptApplication.getInstance();
		app.setMainFrame(AdaptMainFrame.this);
		PersisenceHelper.createFactory("adapt");

		// Read XML specifications
		loadMappings();
		// Export ejb classes to database tables
		try {
			SchemaGenerator schemaGen = new SchemaGenerator("ejb");
			schemaGen.generate();
			EntityManager em = PersisenceHelper.createEntityManager();

			loadAdministrationSubsytem(em);

			List<User> users = em.createQuery("SELECT u FROM User u").getResultList();
			if (users == null || users.isEmpty()) {
				//If no users defined
				//persist test user
				User u = new User();
				u.setUsername("admin");
				u.setPassword("12345");

				em.getTransaction().begin();
				em.persist(u);
				em.getTransaction().commit();
			}
			em.close();
		}catch(Exception e) {
			displayStackTrace(e);
			e.printStackTrace();
		}

		component.getDefaultHost().attach(app);
		component.start();
		displayText("Starting internal server on port 8182", 0);
	}

	/**
	 * Invokes XML readers' parsing functions
	 */
	private void loadMappings() {
		EntityReader.loadMappings();
		PanelReader.loadMappings();
		TypeComponenMappingReader.mapTypesToComponents();;
		MenuReader.load();
		EnumerationReader.loadEnumerations();
	}

	private void loadAdministrationSubsytem(EntityManager entityManager) {
		AdministrationSubsystemReader.load(entityManager);
	}

	/**
	 * Displays text for logging purposes.
	 * @param text Text to be shown in frame
	 * @param type Message type: 0 - info, 1 - error, 2 - warning
	 */
	public void displayText(String text, int type) {
		StyledDocument document = statusPane.getStyledDocument();
		SimpleAttributeSet set = new SimpleAttributeSet();
		statusPane.setCharacterAttributes(set, true);
		SimpleDateFormat formatter = new SimpleDateFormat(Settings.DATE_TIME_SECONDS_FORMAT);
		Date today = new Date();
		String prefix = "[" + formatter.format(today) + "] ";
		StyleConstants.setForeground(set, Color.white);
		if(type == 1) {
			StyleConstants.setForeground(set, Color.red);
			prefix += "[ERROR] ";
		}
		if(type == 2) {
			StyleConstants.setForeground(set, Color.yellow);
			prefix += "[WARINIG] ";
		}
		try {
			document.insertString(document.getLength(), prefix + text + "\n", set);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		statusPane.setCaretPosition(statusPane.getDocument().getLength());
		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue( vertical.getMaximum() );
	}

	/**
	 * Displays exception stack trace as error message
	 * @param e
	 */
	public void displayStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String stacktrace = sw.toString();
		displayText(stacktrace, 1);
	}

	public void loadIcon() {
		String icoPath = RepositoryPathsUtil.getStaticRepositoryPath() + File.separator + Settings.ICONS_DIR + File.separator + Settings.WEB_MAINFRAME_ICON;
		icoPath = icoPath.replaceAll("/", "\\\\");
		BufferedImage icoImg;
		try {
			icoImg = ImageIO.read(new File(icoPath));
			setIconImage(icoImg);
		} catch (IOException e) {
			// If loading image fails, app is probably running as Eclipse project, so the path needs to be altered
			String newPath = RepositoryPathsUtil.getAppRootPath() + File.separator + "ApplicationRepository" + File.separator + "static" + File.separator + Settings.ICONS_DIR + File.separator + Settings.WEB_MAINFRAME_ICON;
			try {
				icoImg = ImageIO.read(new File(newPath));
				setIconImage(icoImg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
