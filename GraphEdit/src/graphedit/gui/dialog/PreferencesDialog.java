package graphedit.gui.dialog;

import graphedit.app.MainFrame;
import graphedit.properties.Preferences;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PreferencesDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	public static final int CANCEL = 0, APPLY = 1;

	public PreferencesDialog() {
		parent = MainFrame.getInstance();
		prefs = Preferences.getInstance();
		setTitle(TITLE);
		setMinimumSize(new Dimension(640,480));
		setSize(parent.getWidth() >> 1, parent.getHeight() >> 1);
		setLocationRelativeTo(parent);
		setModal(true);
		setResizable(false);
		guiInit();
		actionInit();
		pack();
	}
	
	private void guiInit() {
		int preferredWidth = getWidth() - 100;
		
		classColor1 = prefs.parseColor(Preferences.CLASS_COLOR_1);
		classColor2 = prefs.parseColor(Preferences.CLASS_COLOR_2);
		interfaceColor1 = prefs.parseColor(Preferences.INTERFACE_COLOR_1);
		interfaceColor2 = prefs.parseColor(Preferences.INTERFACE_COLOR_2);
		
		minimumZoomFactorText = new JTextField(prefs.getProperty(Preferences.MIN_ZOOM));
		maximumZoomFactorText = new JTextField(prefs.getProperty(Preferences.MAX_ZOOM));
		bestFitFactorText = new JTextField(prefs.getProperty(Preferences.BESTFIT_FACTOR));
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		graphElementPanel.setBorder(BorderFactory.createTitledBorder("Graph element"));
		graphElementPanel.setLayout(new BoxLayout(graphElementPanel, BoxLayout.Y_AXIS));
		
		classColor1Label.setForeground(Color.BLUE);
		classColor1Label.setPreferredSize(new Dimension(preferredWidth, 20));
		classColor1Panel.setPreferredSize(new Dimension(SQUARE, SQUARE));
		classColor1Panel.setBackground(classColor1);
		classColor1Panel.setName("Choose 1st class color: ");
		classColor1Panel.setBorder(BorderFactory.createLineBorder(Color.black));
		p1 = new JPanel();
		p1.add(classColor1Label);
		p1.add(classColor1Panel);
		graphElementPanel.add(p1);
		
		classColor2Label.setForeground(Color.BLUE);
		classColor2Label.setPreferredSize(new Dimension(preferredWidth, 20));
		classColor2Panel.setPreferredSize(new Dimension(SQUARE, SQUARE));
		classColor2Panel.setBackground(classColor2);
		classColor1Panel.setName("Choose 2nd class color: ");
		classColor2Panel.setBorder(BorderFactory.createLineBorder(Color.black));
		p2 = new JPanel();
		p2.add(classColor2Label);
		p2.add(classColor2Panel);
		graphElementPanel.add(p2);
		
		interfaceColor1Label.setForeground(Color.BLUE);
		interfaceColor1Label.setPreferredSize(new Dimension(preferredWidth, 20));
		interfaceColor1Panel.setPreferredSize(new Dimension(SQUARE, SQUARE));
		interfaceColor1Panel.setBackground(interfaceColor1);
		interfaceColor1Panel.setName("Choose 1st interface color: ");
		interfaceColor1Panel.setBorder(BorderFactory.createLineBorder(Color.black));
		p3 = new JPanel();
		p3.add(interfaceColor1Label);
		p3.add(interfaceColor1Panel);
		graphElementPanel.add(p3);
		
		interfaceColor2Label.setForeground(Color.BLUE);
		interfaceColor2Label.setPreferredSize(new Dimension(preferredWidth, 20));
		interfaceColor2Panel.setPreferredSize(new Dimension(SQUARE, SQUARE));
		interfaceColor2Panel.setBackground(interfaceColor2);
		interfaceColor2Panel.setName("Choose 2nd interface color: ");
		interfaceColor2Panel.setBorder(BorderFactory.createLineBorder(Color.black));
		p4 = new JPanel();
		p4.add(interfaceColor2Label);
		p4.add(interfaceColor2Panel);
		graphElementPanel.add(p4);
		
		linkPanel.setBorder(BorderFactory.createTitledBorder("Links"));
		linkPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		rightAngle.setForeground(Color.BLUE);
		rightAngle.setSelected(prefs.getProperty(Preferences.RIGHT_ANGLE).equalsIgnoreCase("true"));
		linkPanel.add(rightAngle);

		canvasPanel.setBorder(BorderFactory.createTitledBorder("Canvas"));
		canvasPanel.setLayout(new BoxLayout(canvasPanel, BoxLayout.Y_AXIS));
		
		minimumZoomFactorLabel.setForeground(Color.BLUE);
		minimumZoomFactorLabel.setPreferredSize(new Dimension(preferredWidth, 20));
		minimumZoomFactorText.setAlignmentY(RIGHT_ALIGNMENT);
		minimumZoomFactorText.setPreferredSize(new Dimension(SQUARE << 1, 20));
		p5 = new JPanel();
		p5.add(minimumZoomFactorLabel);
		p5.add(minimumZoomFactorText);
		canvasPanel.add(p5);
		
		maximumZoomFactorLabel.setForeground(Color.BLUE);
		maximumZoomFactorLabel.setPreferredSize(new Dimension(preferredWidth, 20));
		maximumZoomFactorText.setAlignmentY(RIGHT_ALIGNMENT);
		maximumZoomFactorText.setPreferredSize(new Dimension(SQUARE << 1, 20));
		p6 = new JPanel();
		p6.add(maximumZoomFactorLabel);
		p6.add(maximumZoomFactorText);
		canvasPanel.add(p6);

		bestFitFactorLabel.setForeground(Color.BLUE);
		bestFitFactorLabel.setPreferredSize(new Dimension(preferredWidth, 20));
		bestFitFactorText.setAlignmentY(RIGHT_ALIGNMENT);
		bestFitFactorText.setPreferredSize(new Dimension(SQUARE << 1, 20));
		p8 = new JPanel();
		p8.add(bestFitFactorLabel);
		p8.add(bestFitFactorText);
		canvasPanel.add(p8);
		
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
		cancelButton.setMnemonic(KeyEvent.VK_C);
		applyButton.setMnemonic(KeyEvent.VK_A);
		p7 = new JPanel();
		p7.add(cancelButton);
		p7.add(applyButton);
		buttonPanel.add(p7);
		
		checkBoxPanel.add(loadDefaults);
		
		add(graphElementPanel);
		add(linkPanel);
		add(canvasPanel);
		add(checkBoxPanel);
		add(buttonPanel);
	}

	private void actionInit() {
		classColor1Panel.addMouseListener(colorPanelListener);
		classColor2Panel.addMouseListener(colorPanelListener);
		interfaceColor1Panel.addMouseListener(colorPanelListener);
		interfaceColor2Panel.addMouseListener(colorPanelListener);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PreferencesDialog.this.setVisible(false);
				PreferencesDialog.this.returnCode = CANCEL;
			}
		});
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PreferencesDialog.this.setVisible(false);
				PreferencesDialog.this.returnCode = APPLY;
			}
		});
		minimumZoomFactorText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					if (Double.parseDouble(minimumZoomFactorText.getText()) < 0.05) {
						minimumZoomFactorText.setText("0.05");
						minimumZoomFactorText.setForeground(Color.RED);
						minimumZoomFactorText.requestFocus();
						
					} else {
						minimumZoomFactorText.setForeground(Color.BLACK);
					}
				} catch (NumberFormatException nfe) {
					minimumZoomFactorText.setText("0.05");
					minimumZoomFactorText.setForeground(Color.RED);
					minimumZoomFactorText.requestFocus();
				}
			}
		});
		maximumZoomFactorText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {	
					if (Double.parseDouble(maximumZoomFactorText.getText()) > 10.0) {
						maximumZoomFactorText.setText("10.0");
						maximumZoomFactorText.setForeground(Color.RED);
						maximumZoomFactorText.requestFocus();
						
					} else {
						maximumZoomFactorText.setForeground(Color.BLACK);
					}
				} catch (NumberFormatException nfe) {
					maximumZoomFactorText.setText("10.0");
					maximumZoomFactorText.setForeground(Color.RED);
					maximumZoomFactorText.requestFocus();
				}
			}
		});
		bestFitFactorText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {	
					double factor = Double.parseDouble(bestFitFactorText.getText());
					if (factor > 1.0) {
						bestFitFactorText.setText("1.0");
						bestFitFactorText.setForeground(Color.RED);
						bestFitFactorText.requestFocus();
					} else if (factor < 0.5) {	
						bestFitFactorText.setText("0.5");
						bestFitFactorText.setForeground(Color.RED);
						bestFitFactorText.requestFocus();
					} else {
						bestFitFactorText.setForeground(Color.BLACK);
					}
				} catch (NumberFormatException nfe) {
					bestFitFactorText.setText(prefs.getProperty(Preferences.BESTFIT_FACTOR));
					bestFitFactorText.setForeground(Color.RED);
					bestFitFactorText.requestFocus();
				}
			}
		});
	}
	
	public Color getClassColor1() {
		return classColor1Panel.getBackground();
	}

	public Color getClassColor2() {
		return classColor2Panel.getBackground();
	}

	public Color getInterfaceColor1() {
		return interfaceColor1Panel.getBackground();
	}
	
	public Color getInterfaceColor2() {
		return interfaceColor2Panel.getBackground();
	}

	public String getMinimumZoomFactor() {
		return minimumZoomFactorText.getText();
	}

	public String getMaximumZoomFactor() {
		return maximumZoomFactorText.getText();
	}

	public String getBestFitFactor() {
		return bestFitFactorText.getText();
	}
	
	public boolean isLoadDefaultsChecked() {
		return loadDefaults.isSelected();
	}
	
	public String isRightAngleChecked() {
		return rightAngle.isSelected() ? "true" : "false";
	}
	
	public int getReturnCode() {
		return returnCode;
	}
	
	public boolean isColorChanged() {
		return !(classColor1.equals(classColor1Panel.getBackground()) && classColor2.equals(classColor2Panel.getBackground())
					&& interfaceColor1.equals(interfaceColor1Panel.getBackground()) && interfaceColor2.equals(interfaceColor2Panel.getBackground()));
	}

	private static final String TITLE = "GraphEdit Preferences";
	private static final int SQUARE = 30;
	private int returnCode;
	private Preferences prefs;
	private MainFrame parent;
	private Color classColor1;
	private Color classColor2;
	private Color interfaceColor1;
	private Color interfaceColor2;
	private JLabel classColor1Label = new JLabel("Class 1st color: ");
	private JLabel classColor2Label = new JLabel("Class 2nd color: ");
	private JLabel interfaceColor1Label = new JLabel("Interface 1st color: ");
	private JLabel interfaceColor2Label = new JLabel("Interface 2nd color: ");
	private JPanel classColor1Panel = new JPanel();
	private JPanel classColor2Panel = new JPanel();
	private JPanel interfaceColor1Panel = new JPanel();
	private JPanel interfaceColor2Panel = new JPanel();
	private JLabel minimumZoomFactorLabel = new JLabel("Minimum Zoom factor (\u2265 0.05):");
	private JLabel maximumZoomFactorLabel = new JLabel("Maximum Zoom factor (\u2264 10.0):");
	private JLabel bestFitFactorLabel = new JLabel("Bestfit Zoom factor (0.5, 1.0):");
	private JPanel graphElementPanel = new JPanel();
	private JPanel linkPanel = new JPanel();
	private JPanel canvasPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JPanel checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JButton cancelButton = new JButton("Cancel");
	private JButton applyButton = new JButton("Apply");
	private JTextField minimumZoomFactorText;
	private JTextField maximumZoomFactorText;
	private JTextField bestFitFactorText;
	private JPanel p1, p2, p3, p4, p5, p6, p7, p8;
	private JCheckBox loadDefaults = new JCheckBox("Restore default settings");
	private JCheckBox rightAngle = new JCheckBox("Draw right angled links");
	private ColorPanelListener colorPanelListener = new ColorPanelListener();
}
