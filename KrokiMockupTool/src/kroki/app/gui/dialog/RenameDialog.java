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

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import kroki.app.utils.StringResource;
import kroki.commons.camelcase.NamingUtil;
import kroki.profil.VisibleElement;
import kroki.profil.panel.StandardPanel;
import kroki.profil.panel.VisibleClass;

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class RenameDialog extends JDialog {

    VisibleElement visibleElement;
    JPanel content;
    JPanel action;
    JButton okBtn;
    JButton cancelBtn;
    JLabel nameLbl;
    JTextField nameTf;

    public RenameDialog(JFrame frame, VisibleElement visibleElement) {
        super(frame);
        this.visibleElement = visibleElement;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        content = new JPanel();
        action = new JPanel();
        this.add(content, BorderLayout.CENTER);
        this.add(action, BorderLayout.SOUTH);

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

        nameLbl = new JLabel(StringResource.getStringResource("dialog.visibleElement.name.label"));
        nameTf = new JTextField(50);
        nameTf.setText(visibleElement.getLabel());

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
        
        content.setLayout(new FlowLayout(FlowLayout.CENTER));
        content.add(nameLbl);
        content.add(nameTf);
    }

    private void okActionPreformed() {
        if (nameTf.getText() == null || nameTf.getText().equals("")) {
            return;
        }
        NamingUtil cc = new NamingUtil();
        if(cc.checkName(nameTf.getText())) {
        	visibleElement.setLabel(nameTf.getText());
            if(visibleElement instanceof VisibleClass) {
            	NamingUtil namer = new NamingUtil();
            	if(visibleElement instanceof StandardPanel) {
            		StandardPanel clas = (StandardPanel) visibleElement;
                	clas.getPersistentClass().setName(namer.toCamelCase(nameTf.getText().trim(), false));
                	visibleElement.update();
            	}
            }
            //visibleElement.getComponent().setName(nameTf.getText());
            this.dispose();
        }else {
        	JOptionPane.showMessageDialog(RenameDialog.this, "New name can only start with a letter!");
        }
    }

    private void cancelActionPreformed() {
        this.dispose();
    }
}
