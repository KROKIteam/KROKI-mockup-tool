package kroki.app.gui.settings;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * Dialog enabling users to choose an item from a list 
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ListDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private Object selectedValue = null;
	private JList jList = new JList();
	private JScrollPane jScrollPane = new JScrollPane();
	private JPanel infoPanel = new JPanel();
	private JLabel infoLable = new JLabel("Info");
	private JPanel actionPanel = new JPanel();
    private JButton okButton = new JButton();
    private JButton cancelButton = new JButton();

    public ListDialog(JFrame frame) {
        super(frame);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(250, 300);
        init();
    }

    public ListDialog() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(250, 300);
        init();
    }

    private void init() {
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(infoLable);

        okButton.setText("OK");
        cancelButton.setText("Cancel");
        okButton.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                okButtonActionPreformed(e);
            }
        });

        cancelButton.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                cancelButtonActionPreformed(e);
            }
        });

        actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        actionPanel.add(okButton);
        actionPanel.add(cancelButton);

        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setViewportView(jList);

        this.setLayout(new BorderLayout());
        this.add(infoPanel, BorderLayout.NORTH);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(actionPanel, BorderLayout.SOUTH);
    }

    private void okButtonActionPreformed(ActionEvent e) {
        selectedValue = jList.getSelectedValue();
        this.dispose();
    }

    private void cancelButtonActionPreformed(ActionEvent e) {
        selectedValue = null;
        this.dispose();
    }

    public static Object showDialog(Object[] objectList, String info) {

        ListDialog listDialog = new ListDialog();
        listDialog.getInfoLable().setText(info);
        listDialog.getjList().setListData(objectList);
        listDialog.setLocationRelativeTo(null);
        listDialog.setModal(true);
        listDialog.setVisible(true);
        return listDialog.getSelectedValue();
    }

    public JPanel getActionPanel() {
        return actionPanel;
    }

    public void setActionPanel(JPanel actionPanel) {
        this.actionPanel = actionPanel;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(JButton cancelButton) {
        this.cancelButton = cancelButton;
    }

    public JList getjList() {
        return jList;
    }

    public void setjList(JList jList) {
        this.jList = jList;
    }

    public JScrollPane getjScrollPane() {
        return jScrollPane;
    }

    public void setjScrollPane(JScrollPane jScrollPane) {
        this.jScrollPane = jScrollPane;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public void setOkButton(JButton okButton) {
        this.okButton = okButton;
    }

    public Object getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(Object selectedValue) {
        this.selectedValue = selectedValue;
    }

    public JLabel getInfoLable() {
        return infoLable;
    }

    public void setInfoLable(JLabel infoLable) {
        this.infoLable = infoLable;
    }

    public JPanel getInfoPanel() {
        return infoPanel;
    }

    public void setInfoPanel(JPanel infoPanel) {
        this.infoPanel = infoPanel;
    }
}
