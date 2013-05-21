package kroki.app;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

	private JSVGCanvas canvas;
	private JPanel statusPanel;
	private JLabel nameLabel;
	private JLabel statLabel;
	
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
		
		statusPanel = new JPanel();
		initStatusBar();
		canvas = new JSVGCanvas();
		initCanvas();
		
		add(canvas, BorderLayout.CENTER);
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
	
}
