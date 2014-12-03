/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kroki.app.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kroki.api.profil.panel.ParentChildUtil;
import kroki.api.profil.panel.StandardPanelUtil;
import kroki.app.KrokiMockupToolApp;
import kroki.app.command.ChangeLayoutCommand;
import kroki.app.command.CommandManager;
import kroki.app.utils.StringResource;
import kroki.app.view.Canvas;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.VisibleElement;
import kroki.profil.group.ElementsGroup;
import kroki.profil.group.GroupOrientation;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;
import kroki.profil.panel.container.ParentChild;
import kroki.profil.subsystem.BussinesSubsystem;
import kroki.uml_core_basic.UmlPackage;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class NewFileDialog extends JDialog {

    BussinesSubsystem owner;
    VisibleClass visibleClass;
    JPanel content;
    JPanel action;
    JButton okBtn;
    JButton cancelBtn;
    JLabel nameLbl;
    JTextField nameTf;
    JComboBox projectCb;
    JLabel projectLbl;
    JLabel fileTypeLbl;
    JComboBox fileTypeCb;

    public enum FileType {

        STANDARD_PANEL, PARENT_CHILD_PANEL;

        @Override
        public String toString() {
            return StringResource.getStringResource("dialog.file.type." + name() + ".label");
        }
    }

    public NewFileDialog(JFrame frame, BussinesSubsystem bussinesSubsystem) {
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

        List<UmlPackage> packageList = null;
        if (owner == null) {
            packageList = KrokiMockupToolApp.getInstance().getWorkspace().getPackageList();
        } else {
            if (owner.nestingPackage() != null) {
                packageList = owner.nestingPackage().nestedPackage();
            } else {
                packageList = KrokiMockupToolApp.getInstance().getWorkspace().getPackageList();
            }
        }
        projectCb = new JComboBox(packageList.toArray());
        if (owner != null) {
            projectCb.setSelectedIndex(packageList.indexOf(owner));
            projectCb.setEnabled(false);
        }
        projectLbl = new JLabel(StringResource.getStringResource("dialog.project.name.label"));


        nameLbl = new JLabel(StringResource.getStringResource("dialog.file.name.label"));
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

        fileTypeLbl = new JLabel(StringResource.getStringResource("dialog.file.type.label"));
        fileTypeCb = new JComboBox(FileType.values());

        MigLayout layout = new MigLayout("wrap 2", "fill, grow");
        content.setLayout(layout);
        content.add(projectLbl);
        content.add(projectCb);
        content.add(nameLbl);
        content.add(nameTf);
        content.add(fileTypeLbl);
        content.add(fileTypeCb);

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
        	if (fileTypeCb.getSelectedItem() == FileType.STANDARD_PANEL) {
                visibleClass = new StandardPanel();
                StandardPanelUtil.defaultGuiSettings((StandardPanel)visibleClass);
            } else if (fileTypeCb.getSelectedItem() == FileType.PARENT_CHILD_PANEL) {
                visibleClass = new ParentChild();
                ParentChildUtil.defaultGuiSettings((ParentChild)visibleClass);
            }
            visibleClass.setLabel(nameTf.getText());
            visibleClass.getComponent().setName(nameTf.getText());
            if(visibleClass instanceof StandardPanel) {
            	StandardPanel vc = (StandardPanel) visibleClass;
                vc.getPersistentClass().setName(cc.toCamelCase(visibleClass.getLabel(), false));
                vc.setName(cc.toCamelCase(visibleClass.getLabel(), false));
                BussinesSubsystem proj = KrokiMockupToolApp.getInstance().findProject(owner);
                vc.getPersistentClass().setTableName(cc.toDatabaseFormat(proj.getLabel(), visibleClass.getLabel()));
                vc.getPersistentClass().setLabelToCode(proj.isLabelToCode());
            }
            visibleClass.update();
            //Dodavanje fajla u paket
            ((BussinesSubsystem) projectCb.getSelectedItem()).addOwnedType(visibleClass);
            
            this.dispose();
        }else {
        	JOptionPane.showMessageDialog(NewFileDialog.this, "File name can only start with a letter!");
        }
    }

    private void cancelActionPreformed() {
        this.dispose();
    }

    public VisibleClass getVisibleClass() {
        return visibleClass;
    }
}
