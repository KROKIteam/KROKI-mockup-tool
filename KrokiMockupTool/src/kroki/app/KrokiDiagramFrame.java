package kroki.app;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import kroki.app.utils.ImageResource;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class KrokiDiagramFrame extends JFrame {

	private JScrollPane scrollPane;
	private JSVGCanvas canvas;
	private JPanel statusPanel;
	private JLabel nameLabel;
	private JLabel statLabel;
	private JLabel coordLabel;
	
	double zoomFactor = 1;
	
	public KrokiDiagramFrame() {
		setTitle("KROKI project visualizer");
		setSize(new Dimension(600, 400));
		setLocationRelativeTo(null);
		Image headerIcon = ImageResource.getImageResource("app.logo32x32");
        setIconImage(headerIcon);
		initGUI();
	}

	private void initGUI() {
		nameLabel = new JLabel();
		statLabel = new JLabel();
		coordLabel = new JLabel("0,0");
		
		statusPanel = new JPanel();
		initStatusBar();
		canvas = new JSVGCanvas();
		initCanvas();

		scrollPane = new JScrollPane(canvas);
		
		add(scrollPane, BorderLayout.CENTER);
		add(statusPanel, BorderLayout.SOUTH);
	}
	
	private void initCanvas() {
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
				AffineTransform matrix = new AffineTransform();
				int notches = e.getWheelRotation();
				//SCROLL UP -> ZOOM IN
				if(notches < 0) {
					if(zoomFactor < 10) {
						zoomFactor -= e.getWheelRotation();
						Point2D old = e.getPoint();
						pointToUserSpace(old, matrix);
						matrix.setToScale(zoomFactor, zoomFactor);
						Point2D newP = e.getPoint();
						pointToUserSpace(newP, matrix);
						matrix.translate(newP.getX()-old.getX(), newP.getY()-newP.getY());
						canvas.setRenderingTransform(matrix, true);
					}
					System.out.println(zoomFactor);
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
		
		canvas.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				Point p = e.getPoint();
				int xOffset = scrollPane.getHorizontalScrollBar().getValue();
				int yOffset = scrollPane.getVerticalScrollBar().getValue();
				int x = p.x - xOffset;
				int y = p.y - yOffset;
				coordLabel.setText(x + "," + y);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
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
		
		middleSection.add(coordLabel);
		
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
