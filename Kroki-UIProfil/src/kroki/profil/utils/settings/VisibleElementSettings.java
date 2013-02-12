/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.profil.utils.settings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import kroki.commons.camelcase.NamingUtil;
import kroki.intl.Intl;
import kroki.profil.VisibleElement;
import kroki.profil.panel.VisibleClass;
import kroki.profil.property.VisibleProperty;
import net.miginfocom.swing.MigLayout;

/**
 * Panel sa podesavanjima za stereotip VisibleElement
 * @author Vladan Marsenic (vladan.marsenic@gmail.com)
 */
public class VisibleElementSettings extends JTabbedPane implements Settings {

	//vidljivi element na koji se primenjuju podesavanja
	protected VisibleElement visibleElement;
	protected SettingsCreator settingsCreator;
	protected HashMap<String, JPanel> panelMap;
	protected LayoutManager panelLayout;
	protected JLabel labelLb;
	protected JLabel fgColorLb;
	protected JLabel visibleLb;
	protected JLabel bgColorLb;
	protected JLabel fontColorLb;
	protected JTextField labelTf;
	protected JCheckBox visibleCb;
	protected JButton bgColorBtn;
	protected JButton fgColorBtn;
	protected JButton fontColorBtn;

	public VisibleElementSettings(SettingsCreator settingsCreator) {
		panelLayout = new MigLayout("wrap 2,hidemode 3", "[right, shrink][fill, 200]");
		this.settingsCreator = settingsCreator;
		panelMap = new HashMap<String, JPanel>();
		initComponents();
		layoutComponents();
		addActions();
	}

	private void initComponents() {
		labelLb = new JLabel();
		labelLb.setText(Intl.getValue("visibleElement.label"));

		visibleLb = new JLabel();
		visibleLb.setText(Intl.getValue("visibleElement.visible"));

		bgColorLb = new JLabel();
		bgColorLb.setText(Intl.getValue("component.bgColor"));

		fgColorLb = new JLabel();
		fgColorLb.setText(Intl.getValue("component.fgColor"));

		fontColorLb = new JLabel();
		fontColorLb.setText(Intl.getValue("component.fontColor"));

		labelTf = new JTextField(30);
		visibleCb = new JCheckBox();

		bgColorBtn = new JButton();
		bgColorBtn.setPreferredSize(new Dimension(20, 20));
		bgColorBtn.setMaximumSize(bgColorBtn.getPreferredSize());

		fgColorBtn = new JButton();
		fgColorBtn.setPreferredSize(new Dimension(20, 20));
		fgColorBtn.setMaximumSize(fgColorBtn.getPreferredSize());

		fontColorBtn = new JButton();
		fontColorBtn.setPreferredSize(new Dimension(20, 20));
		fontColorBtn.setMaximumSize(fgColorBtn.getPreferredSize());
	}

	private void layoutComponents() {
		JPanel basic = null;
		if (panelMap.containsKey("group.BASIC")) {
			basic = panelMap.get("group.BASIC");
		} else {
			basic = new JPanel();
			basic.setLayout(new MigLayout("wrap 2,hidemode 3", "[right, shrink][fill, 200]"));
			addTab(Intl.getValue("group.BASIC"), basic);
			panelMap.put("group.BASIC", basic);
		}

		basic.add(labelLb);
		basic.add(labelTf);
		basic.add(visibleLb);
		basic.add(visibleCb);
		basic.add(bgColorLb);
		basic.add(bgColorBtn);
		basic.add(fgColorLb);
		basic.add(fgColorBtn);
		basic.add(fontColorLb);
		basic.add(fontColorBtn);
	}

	public void updateComponents() {
		labelTf.setText(visibleElement.getLabel());
		visibleCb.setSelected(visibleElement.isVisible());
		bgColorBtn.setIcon(new ImageIcon(PlainColorImage.getImage(visibleElement.getComponent().getBgColor(), 16, 16)));
		fgColorBtn.setIcon(new ImageIcon(PlainColorImage.getImage(visibleElement.getComponent().getFgColor(), 16, 16)));
	}

	private void addActions() {
		//promena labele
		labelTf.getDocument().addDocumentListener(new DocumentListener() {

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
				visibleElement.setLabel(text);
				NamingUtil namer = new NamingUtil();
				if(visibleElement instanceof VisibleProperty) {
					VisibleProperty visibleProperty = (VisibleProperty) visibleElement;
					visibleProperty.setColumnLabel(namer.toDatabaseFormat(visibleProperty.umlClass().name().replace("_", " "), labelTf.getText().trim()));
					updatePreformed();
				}
				if (visibleElement instanceof VisibleClass) {
					updatePreformedIncludeTree();
				} else {
					updatePreformed();
				}

			}
		});

		labelTf.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				NamingUtil namer = new NamingUtil();
				if(visibleElement instanceof VisibleProperty) {
					VisibleProperty visibleProperty = (VisibleProperty) visibleElement;
					visibleProperty.setColumnLabel(namer.toDatabaseFormat(visibleProperty.umlClass().name().replace("_", " "), labelTf.getText().trim()));
					updatePreformed();
				}
			}

			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});

		//promena vidljivosti
		visibleCb.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean value = checkBox.isSelected();
				visibleElement.setVisible(value);
				updatePreformed();
			}
		});

		//promena boje podzadine
		bgColorBtn.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton) e.getSource();
				Color color = JColorChooser.showDialog(null, "Choose color", visibleElement.getComponent().getBgColor());
				if (color != null) {
					btn.setIcon(new ImageIcon(PlainColorImage.getImage(color, 16, 16)));
					visibleElement.getComponent().setBgColor(color);
					updatePreformed();
				}
			}
		});

		//promena boje predzadine :)
		fgColorBtn.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				JButton btn = (JButton) e.getSource();
				Color color = JColorChooser.showDialog(null, "Choose color", visibleElement.getComponent().getFgColor());
				if (color != null) {
					btn.setIcon(new ImageIcon(PlainColorImage.getImage(color, 16, 16)));
					visibleElement.getComponent().setFgColor(color);
					updatePreformed();
				}
			}
		});


	}

	public void updateSettings(VisibleElement visibleElement) {
		this.visibleElement = visibleElement;
		updateComponents();
	}

	public void updatePreformed() {
		visibleElement.update();
		settingsCreator.settingsPreformed();
	}

	public void updatePreformedIncludeTree() {
		visibleElement.update();
		settingsCreator.settingsPreformedIncludeTree();
	}

	/*****************/
	/*GETERI I SETERI*/
	/*****************/
	public JButton getBgColorBtn() {
		return bgColorBtn;
	}

	public void setBgColorBtn(JButton bgColorBtn) {
		this.bgColorBtn = bgColorBtn;
	}

	public JLabel getBgColorLb() {
		return bgColorLb;
	}

	public void setBgColorLb(JLabel bgColorLb) {
		this.bgColorLb = bgColorLb;
	}

	public JButton getFgColorBtn() {
		return fgColorBtn;
	}

	public void setFgColorBtn(JButton fgColorBtn) {
		this.fgColorBtn = fgColorBtn;
	}

	public JLabel getFgColorLb() {
		return fgColorLb;
	}

	public void setFgColorLb(JLabel fgColorLb) {
		this.fgColorLb = fgColorLb;
	}

	public JButton getFontColorBtn() {
		return fontColorBtn;
	}

	public void setFontColorBtn(JButton fontColorBtn) {
		this.fontColorBtn = fontColorBtn;
	}

	public JLabel getFontColorLb() {
		return fontColorLb;
	}

	public void setFontColorLb(JLabel fontColorLb) {
		this.fontColorLb = fontColorLb;
	}

	public JLabel getLabelLb() {
		return labelLb;
	}

	public void setLabelLb(JLabel labelLb) {
		this.labelLb = labelLb;
	}

	public JTextField getLabelTf() {
		return labelTf;
	}

	public void setLabelTf(JTextField labelTf) {
		this.labelTf = labelTf;
	}

	public LayoutManager getPanelLayout() {
		return panelLayout;
	}

	public void setPanelLayout(LayoutManager panelLayout) {
		this.panelLayout = panelLayout;
	}

	public HashMap<String, JPanel> getPanelMap() {
		return panelMap;
	}

	public void setPanelMap(HashMap<String, JPanel> panelMap) {
		this.panelMap = panelMap;
	}

	public JCheckBox getVisibleCb() {
		return visibleCb;
	}

	public void setVisibleCb(JCheckBox visibleCb) {
		this.visibleCb = visibleCb;
	}

	public VisibleElement getVisibleElement() {
		return visibleElement;
	}

	public void setVisibleElement(VisibleElement visibleElement) {
		this.visibleElement = visibleElement;
	}

	public JLabel getVisibleLb() {
		return visibleLb;
	}

	public void setVisibleLb(JLabel visibleLb) {
		this.visibleLb = visibleLb;
	}
}
