package kroki.app.action;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import kroki.app.KrokiMockupToolApp;
import kroki.app.gui.console.OutputPanel;
import kroki.app.utils.StringResource;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.VisibleElement;
import kroki.profil.association.Zoom;
import kroki.profil.operation.BussinessOperation;
import kroki.profil.operation.VisibleOperation;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.property.VisibleProperty;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.profil.utils.VisibleClassUtil;

public class ExportLabelsAction extends AbstractAction {

	ArrayList<String> labels;
	
	public ExportLabelsAction() {
		putValue(NAME, "Export labels");
        putValue(SHORT_DESCRIPTION, StringResource.getStringResource("action.export.labels"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				final BussinesSubsystem proj = KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().getCurrentProject();
				if(proj != null) {
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					NamingUtil namer = new NamingUtil();
					JFileChooser jfc = new JFileChooser();
					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					File projectFile = null;
					int retValue = jfc.showSaveDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame());
					if (retValue == JFileChooser.APPROVE_OPTION) {
						projectFile = new File(jfc.getSelectedFile(), namer.toCamelCase(proj.getLabel(), false) + "_LABELS.xls");
						labels = new ArrayList<String>();
						getLabels(proj);
						writeLabels(projectFile);
						KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				}else {
					JOptionPane.showMessageDialog(KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame(), "You must select a project from workspace!");
					KrokiMockupToolApp.getInstance().getKrokiMockupToolFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.start();
	}
	
	private void getLabels(BussinesSubsystem project) {
		addLabel(project.getLabel());
		for (int i = 0; i < project.ownedElementCount(); i++) {
			VisibleElement el = project.getOwnedElementAt(i);
			if (el instanceof BussinesSubsystem) {
				processPackage((BussinesSubsystem)el);
			} else if (el instanceof VisibleClass) {
				processPanel(el);
			}
		}
	}
	
	private void processPackage(BussinesSubsystem pack) {
		addLabel(pack.getLabel());
		for (int i = 0; i < pack.ownedElementCount(); i++) {
			VisibleElement el = pack.getOwnedElementAt(i);
			if (el instanceof BussinesSubsystem) {
				processPackage((BussinesSubsystem)el);
			} else if (el instanceof VisibleClass) {
				processPanel(el);
			}
		}
	}
	
	private void processPanel(VisibleElement panel) {
		addLabel(panel.getLabel());
		if(panel instanceof StandardPanel) {
			StandardPanel standardPanel = (StandardPanel) panel;
			// GET ATTRIBUTE LABELS
			for (VisibleElement element : standardPanel.getVisibleElementList()) {
				if(element instanceof VisibleProperty || element instanceof Zoom) {
					addLabel(element.getLabel());
				}
			}
			// GET OPERATION LABELS
			VisibleClass visibleClass = (VisibleClass)panel;
			if(!VisibleClassUtil.containedOperations(visibleClass).isEmpty()) {
				for(int k=0; k < VisibleClassUtil.containedOperations(visibleClass).size(); k++) {
					VisibleOperation visibleOperation = VisibleClassUtil.containedOperations(visibleClass).get(k);
					addLabel(visibleOperation.getLabel());
				}
			}
		}
	}
	
	private void writeLabels(File labelsFile) {
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(labelsFile));
			String fileContent = "LABEL\tTRANSLATION\n";
			for (String label : labels) {
				fileContent += label + "\t\n";
			}
			writer.write(fileContent);
			writer.close();
			KrokiMockupToolApp.getInstance().displayTextOutput("Labels exported successfully to '" + labelsFile.getAbsolutePath() + "'", OutputPanel.KROKI_FINISHED);
		}catch(Exception e) {
			KrokiMockupToolApp.getInstance().displayTextOutput("Error exporting labels to '" + labelsFile.getAbsolutePath() + "'", OutputPanel.KROKI_ERROR);
		}
	}
	
	private void addLabel(String label) {
		if(!labels.contains(label)) {
			labels.add(label);
		}
	}
}
