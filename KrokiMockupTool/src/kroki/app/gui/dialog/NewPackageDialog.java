package kroki.app.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kroki.app.KrokiMockupToolApp;
import kroki.app.utils.StringResource;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.ComponentType;
import kroki.profil.subsystem.BussinesSubsystem;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class NewPackageDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private BussinesSubsystem owner;
	private BussinesSubsystem newSubsystem;
	private JPanel content;
	private JPanel action;
	private JButton okBtn;
	private JButton cancelBtn;
	private JLabel nameLbl;
	private JTextField nameTf;
	private JTextField projectTf;
	private JLabel projectLbl;

	public NewPackageDialog(JFrame frame, BussinesSubsystem bussinesSubsystem) {
		super(frame);
		this.owner = bussinesSubsystem;
		initComponents();
	}

	private void initComponents() {
		this.setLayout(new BorderLayout());
		content = new JPanel();
		action = new JPanel();
		this.add(content, BorderLayout.CENTER);
		this.add(action, BorderLayout.SOUTH);
		projectTf = new JTextField(owner.getLabel());
		projectTf.setEnabled(false);
		projectLbl = new JLabel(StringResource.getStringResource("dialog.project.name.label"));


		nameLbl = new JLabel(StringResource.getStringResource("dialog.package.name.label"));
		nameTf = new JTextField(50);

		nameTf.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					okActionPreformed();
				}
			}
		});

		MigLayout layout = new MigLayout("wrap 2");
		content.setLayout(layout);
		content.add(projectLbl);
		content.add(projectTf, "grow, wrap");
		content.add(nameLbl);
		content.add(nameTf);


		okBtn = new JButton(StringResource.getStringResource("dialog.ok.label"));
		okBtn.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				okActionPreformed();
			}
		});
		cancelBtn = new JButton(StringResource.getStringResource("dialog.cancel.label"));
		cancelBtn.addActionListener(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				cancelActionPreformed();
			}
		});
		action.setLayout(new FlowLayout(FlowLayout.CENTER));
		action.add(okBtn);
		action.add(cancelBtn);

	}

	private void okActionPreformed() {
		NamingUtil cc = new NamingUtil();
		if (nameTf.getText().equals("")) {
			return;
		}
		if(cc.checkName(nameTf.getText())) {
			//find project in which we want to make package
			BussinesSubsystem project = KrokiMockupToolApp.getInstance().findProject(owner);
			//check if  package with same name exists in that project (in any level)
			BussinesSubsystem pr = KrokiMockupToolApp.getInstance().findPackage(nameTf.getText(), owner);
			
			if(pr != null) {
				JOptionPane.showMessageDialog(NewPackageDialog.this, "Package with specified name allready exists!");
			}else {
				newSubsystem = new BussinesSubsystem(nameTf.getText(), true, ComponentType.MENU, owner);
				newSubsystem.setName(nameTf.getText());
				owner.addNestedPackage(newSubsystem);
				this.dispose();
			}
		}else {
			JOptionPane.showMessageDialog(NewPackageDialog.this, "Package name can only start with a letter!");
		}
	}

	private void cancelActionPreformed() {
		this.dispose();
	}
}
