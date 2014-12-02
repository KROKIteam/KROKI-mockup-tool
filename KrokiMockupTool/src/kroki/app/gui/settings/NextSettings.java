/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.gui.settings;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import kroki.api.panel.VisibleClassUtil;
import kroki.api.property.UIPropertyUtil;
import kroki.app.gui.visitor.AllPosibleNexts;
import kroki.app.gui.visitor.Visitor;
import kroki.commons.document.OnlyDigitsDocumentFilter;
import kroki.intl.Intl;
import kroki.profil.VisibleElement;
import kroki.profil.association.Next;
import kroki.profil.association.Zoom;
import kroki.profil.group.ElementsGroup;
import kroki.profil.panel.VisibleClass;
import kroki.uml_core_basic.UmlProperty;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 * @author Renata
 */
public class NextSettings extends VisibleAssociationEndSettings {

	protected JLabel autoActivateLb;
	protected JLabel displayIdentifierLb;
	protected JLabel displayRepresentativeLb;
	protected JLabel positionLb;
	protected JCheckBox autoActivateCb;
	protected JCheckBox displayIdentifierCb;
	protected JCheckBox displayRepresentativeCb;
	protected JTextField positionTf;


	public NextSettings(SettingsCreator settingsCreator) {
		super(settingsCreator);
		initComponents();
		layoutComponents();
		addActions();
	}

	private void initComponents() {
		oppositeLb = new JLabel(Intl.getValue("next.zoom"));
		autoActivateLb = new JLabel(Intl.getValue("next.autoActivate"));
		displayIdentifierLb = new JLabel(Intl.getValue("next.displayRepresentative"));
		displayRepresentativeLb = new JLabel(Intl.getValue("next.displayIdentifier"));
		positionLb = new JLabel(Intl.getValue("next.position"));
		autoActivateCb = new JCheckBox();
		displayIdentifierCb = new JCheckBox();
		displayRepresentativeCb = new JCheckBox();
		oppositeTf = new JTextField();
		oppositeTf.setPreferredSize(new Dimension(30, 20));
		oppositeTf.setEditable(false);
		oppositeBtn = new JButton("...");
		oppositeBtn.setPreferredSize(new Dimension(30, 20));
		oppositeBtn.setMinimumSize(oppositeBtn.getPreferredSize());
		positionTf = new JTextField(5);
	}

	private void layoutComponents() {
		JPanel intermediate = null;
		JScrollPane pane;
		if (panelMap.containsKey("group.INTERMEDIATE")) {
			intermediate = panelMap.get("group.INTERMEDIATE");
		} else {
			intermediate = new JPanel();
			intermediate.setLayout(new MigLayout("wrap 2,hidemode 3", "[right, shrink][fill, 200]"));
			panelMap.put("group.INTERMEDIATE", intermediate);
			pane = new JScrollPane(intermediate);
			this.addTab(Intl.getValue("group.INTERMEDIATE"), pane);
		}
		intermediate.add(oppositeLb);
		intermediate.add(oppositeTf, "split 2, grow");
		intermediate.add(oppositeBtn, "shrink");
		intermediate.add(positionLb);
		intermediate.add(positionTf, "w 30!");
		intermediate.add(autoActivateLb);
		intermediate.add(autoActivateCb);
		intermediate.add(displayIdentifierLb);
		intermediate.add(displayIdentifierCb);
		intermediate.add(displayRepresentativeLb);
		intermediate.add(displayRepresentativeCb);
		((AbstractDocument) positionTf.getDocument()).setDocumentFilter(new OnlyDigitsDocumentFilter());
	}

	@Override
	public void updateComponents() {
		super.updateComponents();
		Next next = (Next) visibleElement;
		autoActivateCb.setSelected(next.isAutoActivate());
		displayIdentifierCb.setSelected(next.isDisplayIdentifier());
		displayRepresentativeCb.setSelected(next.isDisplayRepresentative());

		String oppositeValue = "";
		if (next.opposite() != null) {
			oppositeValue = next.opposite().toString();
		}
		oppositeTf.setText(oppositeValue.toString());


		VisibleClass panel = (VisibleClass) ((UmlProperty) visibleElement).umlClass();
		int position = VisibleClassUtil.containedNexts(panel).indexOf(next) + 1;
		positionTf.setText(position + "");


	}

	private void addActions() {

		autoActivateCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				Next next = (Next) visibleElement;
				next.setAutoActivate(value);
				updatePreformed();
			}
		});
		displayIdentifierCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				Next next = (Next) visibleElement;
				next.setDisplayIdentifier(value);
				updatePreformed();
			}
		});
		displayRepresentativeCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				Next next = (Next) visibleElement;
				next.setDisplayRepresentative(value);
				updatePreformed();
			}
		});

		oppositeBtn.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				Next next = (Next) visibleElement;
				List<Object> objectList;
				Visitor visitor = null;
				String info = "";

				visitor = new AllPosibleNexts();
				info = Intl.getValue("next.choose.zoom");
				if (visitor != null) {
					visitor.visit(visibleElement);
					objectList = visitor.getObjectList();
					Object selected = ListDialog.showDialog(objectList.toArray(), info);
					Object panel;
					if (selected != null) {
						//target zoom was changed, update zoom's opposite
						if (next.opposite() != null)
							next.opposite().setOpposite(null);
						//set opposite
						Zoom selectedZoom = (Zoom)selected;
						next.setOpposite(selectedZoom);
						next.opposite().setName(selectedZoom.name());
						panel = ((Zoom)selected).getActivationPanel();
						//set zoom's opposite
						((Zoom)selected).setOpposite((Next) visibleElement);
						oppositeTf.setText(((Zoom)selected).getLabel());
						//set target panel if not set
						if (next.getTargetPanel() == null){
							next.setTargetPanel((VisibleClass) panel);
							targetPanelTf.setText(panel.toString());
						}
					}
				}
			}
		});


		//Position
		positionTf.getDocument().addDocumentListener(new DocumentListener() {

			public void insertUpdate(DocumentEvent e) {
				contentChanged(e);
			}

			public void removeUpdate(DocumentEvent e) {
				contentChanged(e);
			}

			public void changedUpdate(DocumentEvent e) {
				//nista se ne desava
			}

			private void contentChanged(DocumentEvent e) {
				Document doc = e.getDocument();
				String text = "";
				try {
					text = doc.getText(0, doc.getLength());
				} catch (BadLocationException ex) {
				}
				if (text.length() == 0)
					return;
				int newPosition = Integer.parseInt(text) - 1;

				VisibleClass panel = (VisibleClass) ((UmlProperty) visibleElement).umlClass();
				Next next = (Next) visibleElement;

				if (!positionValid(newPosition, panel))
					return;
				//else swap

				//find where the next is in the visible elements list...
				//and which is the other one

				int oldPosition = panel.getVisibleElementList().indexOf(next);


				Next otherNext = VisibleClassUtil.containedNexts(panel).get(newPosition);
				newPosition = panel.getVisibleElementList().indexOf(otherNext);


				if (oldPosition != newPosition){

					ElementsGroup gr = (ElementsGroup) panel.getVisibleElementList().get(2);

					insertOn(panel, next, otherNext, gr);

					updatePreformed();
				}
			}
		});


	}

	@Override
	public void updateSettings(VisibleElement visibleElement) {
		super.updateSettings(visibleElement);
	}

	@Override
	public void updatePreformed() {
		super.updatePreformed();
	}

	/**
	 * Checks if entered position of the next element is valid
	 * @param position
	 * @param visClass
	 * @return
	 */
	private boolean positionValid(int position, VisibleClass visClass){
		if (position < 0)
			return false;
		return position < VisibleClassUtil.containedNexts(visClass).size();
	}

	//TODO ovo kasnije ubaciti u commons
	private void swapProperties(VisibleClass visibleClass, VisibleElement p1, VisibleElement p2, ElementsGroup gr){

		int firstIndexGr = gr.indexOf(p1);
		int secondIndexGr = gr.indexOf(p2);


		gr.removeVisibleElement(p1);
		gr.removeVisibleElement(p2);

		if (firstIndexGr < secondIndexGr){
			gr.addVisibleElement(firstIndexGr, p2);
			gr.addVisibleElement(secondIndexGr, p1);
		}
		else{
			gr.addVisibleElement(secondIndexGr, p1);
			gr.addVisibleElement(firstIndexGr, p2);
		}


		int firstIndexCl = visibleClass.getVisibleElementList().indexOf(p1);
		int secondIndexCl = visibleClass.getVisibleElementList().indexOf(p2);

		UIPropertyUtil.removeVisibleElement(visibleClass, p1);
		UIPropertyUtil.removeVisibleElement(visibleClass,p2);


		if (firstIndexCl < secondIndexCl){
			UIPropertyUtil.addVisibleElement(visibleClass, firstIndexCl, p2);
			UIPropertyUtil.addVisibleElement(visibleClass, secondIndexCl, p1);
		}
		else{
			UIPropertyUtil.addVisibleElement(visibleClass, secondIndexCl, p1);
			UIPropertyUtil.addVisibleElement(visibleClass, firstIndexCl, p2);
		}
	}

	private void insertOn(VisibleClass visibleClass, VisibleElement p1, VisibleElement p2, ElementsGroup gr){

		int secondIndexGr = gr.indexOf(p2);


		gr.removeVisibleElement(p1);
		gr.addVisibleElement(secondIndexGr, p1);

		int secondIndexCl = visibleClass.getVisibleElementList().indexOf(p2);

		UIPropertyUtil.removeVisibleElement(visibleClass, p1);

		UIPropertyUtil.addVisibleElement(visibleClass, secondIndexCl, p1);
	}
}


