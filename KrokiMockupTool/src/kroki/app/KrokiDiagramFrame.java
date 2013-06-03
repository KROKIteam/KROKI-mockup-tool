package kroki.app;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import kroki.app.model.Workspace;
import kroki.app.utils.ImageResource;
import kroki.common.copy.DeepCopy;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class KrokiDiagramFrame extends JFrame {

	private JSVGCanvas canvas;
	private JPanel statusPanel;
	private JLabel nameLabel;
	private JLabel statLabel;
	private JLabel coordLabel;
	private Cursor handCursor = new Cursor(Cursor.MOVE_CURSOR);
	private Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);

	private MenuBar mainMenu;
	private Menu fileMenu;

	private Point clickLocation;
	private double zoomFactor = 1;

	private String svgXML;

	public KrokiDiagramFrame() {
		setTitle("KROKI project visualizer");
		setSize(new Dimension(600, 400));
		setLocationRelativeTo(null);
		Image headerIcon = ImageResource.getImageResource("app.logo32x32");
		setIconImage(headerIcon);
		initGUI();
	}

	private void initGUI() {
		mainMenu = new MenuBar();;
		fileMenu = new Menu("File");

		MenuItem saveAsSVGItem = new MenuItem("Save as SVG image...");
		MenuItem saveAsPNGItem = new MenuItem("Sava as PNG image...");

		saveAsSVGItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("Scalable vector graphic files", "svg");
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.setFileFilter(filter);
				int retValue = jfc.showSaveDialog(KrokiDiagramFrame.this);
				if (retValue == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					PrintWriter pw;
					try {
						pw = new PrintWriter(new FileOutputStream(new File(file.getPath() + ".svg")));
						pw.print(svgXML);
						pw.close();
						statLabel.setText("SVG image saved successfully!");
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}

				} else {
					System.out.println("SVG save canceled: ");
				}

			}
		});

		saveAsPNGItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser jfc = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("Portable network graphic files", "png");
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.setFileFilter(filter);
				int retValue = jfc.showSaveDialog(KrokiDiagramFrame.this);
				if (retValue == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();

					PNGTranscoder transcoder = new PNGTranscoder();
					TranscoderInput input = new TranscoderInput(canvas.getSVGDocument());
					OutputStream ostream;
					try {
						ostream = new FileOutputStream(new File(file.getPath() + ".png"));
						TranscoderOutput output = new TranscoderOutput(ostream);
						try {
							transcoder.transcode(input, output);
							ostream.flush();
							ostream.close();
							statLabel.setText("PNG image saved successfully!");
						} catch (TranscoderException e1) {
							e1.printStackTrace();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					} catch (FileNotFoundException e3) {
						e3.printStackTrace();
					}


				} else {
					System.out.println("PNG save canceled: ");
				}
			}
		});

		fileMenu.add(saveAsSVGItem);
		fileMenu.add(saveAsPNGItem);
		//fileMenu.addSeparator();
		//fileMenu.add(editPrefferencesItem);
		mainMenu.add(fileMenu);

		setMenuBar(mainMenu);

		nameLabel = new JLabel();
		statLabel = new JLabel();
		coordLabel = new JLabel("0,0");

		statusPanel = new JPanel();
		initStatusBar();
		canvas = new JSVGCanvas();
		initCanvas();

		add(canvas, BorderLayout.CENTER);
		add(statusPanel, BorderLayout.SOUTH);
	}

	private void initCanvas() {
		canvas.setAutoscrolls(true);
		canvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {
			public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
				statLabel.setText("Rendering...");
			}
			public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
				statLabel.setText("Rendered ok.");
			}
		});


		/*
		 * ZOOM IN AND OUT IMPLEMENTED WITH MOUSE WHEEL LISTENER (NOT QUITE)
		 */
		canvas.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				AffineTransform matrix = canvas.getRenderingTransform();
				int notches = e.getWheelRotation();
				//SCROLL UP -> ZOOM IN
				if(notches < 0) {
					if(zoomFactor < 10) {
						zoomFactor -= e.getWheelRotation() * 0.2;
						Point2D old = e.getPoint();
						pointToUserSpace(old, matrix);
						matrix.setToScale(zoomFactor, zoomFactor);
						Point2D newP = e.getPoint();
						pointToUserSpace(newP, matrix);
						matrix.translate(newP.getX()-old.getX(), newP.getY()-newP.getY());
						canvas.setRenderingTransform(matrix, true);
					}
				}else {
					//ZOOM OUT
					if(zoomFactor > 1) {
						zoomFactor -= e.getWheelRotation() * 0.2;
						Point2D old = e.getPoint();
						pointToUserSpace(old, matrix);
						matrix.setToScale(zoomFactor, zoomFactor);
						Point2D newP = e.getPoint();
						pointToUserSpace(newP, matrix);
						matrix.translate(newP.getX()-old.getX(), newP.getY()-newP.getY());
						canvas.setRenderingTransform(matrix, true);
					}
				}
			}
		});

		canvas.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				canvas.setCursor(defaultCursor);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				clickLocation = e.getPoint();
				pointToUserSpace(clickLocation, canvas.getRenderingTransform());
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		canvas.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				Point p = e.getPoint();
				//int xOffset = scrollPane.getHorizontalScrollBar().getValue();
				//int yOffset = scrollPane.getVerticalScrollBar().getValue();
				int x = p.x;
				int y = p.y;
				coordLabel.setText(x + "," + y);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				canvas.setCursor(handCursor);
				AffineTransform matrix = canvas.getRenderingTransform();
				Point p = e.getPoint();
				pointToUserSpace(p, matrix);
				int xOffset = -(clickLocation.x - p.x);
				int yOffset = -(clickLocation.y - p.y);
				matrix.translate(xOffset, yOffset);
				canvas.setRenderingTransform(matrix, true);
			}
		});
	}


	private void initStatusBar() {
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));

		//sections of status bar
		JPanel leftSection = new JPanel();
		leftSection.setBorder(new BevelBorder(BevelBorder.LOWERED));
		leftSection.setAlignmentX(Component.LEFT_ALIGNMENT);
		leftSection.setLayout(new FlowLayout(FlowLayout.LEFT));

		JPanel middleSection = new JPanel();
		middleSection.setBorder(new BevelBorder(BevelBorder.LOWERED));
		middleSection.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel rightSection = new JPanel();
		rightSection.setBorder(new BevelBorder(BevelBorder.LOWERED));
		rightSection.setAlignmentX(Component.RIGHT_ALIGNMENT);
		rightSection.setLayout(new FlowLayout(FlowLayout.LEFT));

		//labels
		JLabel projectLabel = new JLabel("Project:");
		Font f1 = new Font(projectLabel.getFont().getName(),Font.BOLD,projectLabel.getFont().getSize());  
		projectLabel.setFont(f1);

		JLabel statusLabel = new JLabel("Image status:");
		Font f2 = new Font(statusLabel.getFont().getName(),Font.BOLD,statusLabel.getFont().getSize());  
		statusLabel.setFont(f2);

		leftSection.add(projectLabel);
		leftSection.add(nameLabel);

		//middleSection.add(coordLabel);

		rightSection.add(statusLabel);
		rightSection.add(statLabel);

		statusPanel.add(leftSection);
		statusPanel.add(middleSection);
		statusPanel.add(rightSection);

	}

	public void setProjectName(String name) {
		nameLabel.setText(name);
		nameLabel.paintImmediately(nameLabel.getBounds());
	}

	public void setCanvasDocument(String description) {
		SourceStringReader reader = new SourceStringReader(description);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			// Write the image to "os"
			String desc = reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
			os.close();
			// The XML is stored into svg
			String svg = new String(os.toByteArray());
			//Create dom document from generated string
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new ByteArrayInputStream(svg.getBytes("utf-8"))));
			svgXML = svg;
			//System.out.println(svg);
			//display created xml as SVG image
			canvas.setDocument(doc);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	private void pointToUserSpace(Point2D deviceSpace, AffineTransform affineTransform){
		try {
			affineTransform.inverseTransform(deviceSpace, deviceSpace);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
	}
}
