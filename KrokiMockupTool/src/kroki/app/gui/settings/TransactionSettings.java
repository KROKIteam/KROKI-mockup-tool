package kroki.app.gui.settings;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import kroki.intl.Intl;
import kroki.profil.VisibleElement;
import kroki.profil.operation.Transaction;
import net.miginfocom.swing.MigLayout;

/**
 * Tabbed pane showing transaction settings
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class TransactionSettings extends BussinessOperationSettings {

	private static final long serialVersionUID = 1L;
	
	protected JLabel refreshRowLb;
	protected JLabel refreshAllLb;
	protected JLabel askConfirmationLb;
	protected JLabel confirmationMessageLb;
	protected JLabel showErrorsLb;
	protected JCheckBox refreshRowCb;
	protected JCheckBox refreshAllCb;
	protected JCheckBox askConfirmationCb;
	protected JTextArea confirmationMessageTa;
	protected JScrollPane confirmationMessageSp;
	protected JCheckBox showErrorsCb;

    public TransactionSettings(SettingsCreator settingsCreator) {
        super(settingsCreator);
        initComponents();
        layoutComponents();
        addActions();
    }

    private void initComponents() {
        refreshRowLb = new JLabel(Intl.getValue("transaction.refreshRow"));
        refreshAllLb = new JLabel(Intl.getValue("transaction.refreshAll"));
        askConfirmationLb = new JLabel(Intl.getValue("transaction.askConfirmation"));
        confirmationMessageLb = new JLabel(Intl.getValue("transaction.confirmationMessage"));
        showErrorsLb = new JLabel(Intl.getValue("transaction.showErrors"));

        refreshRowCb = new JCheckBox();
        refreshAllCb = new JCheckBox();
        askConfirmationCb = new JCheckBox();
        showErrorsCb = new JCheckBox();
        confirmationMessageTa = new JTextArea(5, 30);
        confirmationMessageTa.setFont(this.getFont());
        confirmationMessageSp = new JScrollPane(confirmationMessageTa);
        confirmationMessageSp.setMinimumSize(confirmationMessageTa.getPreferredScrollableViewportSize());
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
            addTab(Intl.getValue("group.INTERMEDIATE"), pane);
        }
        intermediate.add(refreshRowLb);
        intermediate.add(refreshRowCb);
        intermediate.add(refreshAllLb);
        intermediate.add(refreshAllCb);
        intermediate.add(askConfirmationLb);
        intermediate.add(askConfirmationCb);
        intermediate.add(showErrorsLb);
        intermediate.add(showErrorsCb);
        intermediate.add(confirmationMessageLb);
        intermediate.add(confirmationMessageSp);
    }

    private void addActions() {

        refreshRowCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                Transaction bussinessOperation = (Transaction) visibleElement;
                bussinessOperation.setRefreshRow(value);
                updatePreformed();
            }
        });
        refreshAllCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                Transaction bussinessOperation = (Transaction) visibleElement;
                bussinessOperation.setRefreshAll(value);
                updatePreformed();
            }
        });
        askConfirmationCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                Transaction bussinessOperation = (Transaction) visibleElement;
                bussinessOperation.setAskConfirmation(value);
                updatePreformed();
            }
        });

        showErrorsCb.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                boolean value = checkBox.isSelected();
                Transaction bussinessOperation = (Transaction) visibleElement;
                bussinessOperation.setShowErrors(value);
                updatePreformed();
            }
        });
        confirmationMessageTa.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                contentChanged(e);
            }

            public void removeUpdate(DocumentEvent e) {
                contentChanged(e);
            }

            public void changedUpdate(DocumentEvent e) {
                //nothing
            }

            private void contentChanged(DocumentEvent e) {
                Document doc = e.getDocument();
                String text = "";
                try {
                    text = doc.getText(0, doc.getLength());
                } catch (BadLocationException ex) {
                }
                Transaction bussinessOperation = (Transaction) visibleElement;
                bussinessOperation.setConfirmationMessage(text);
                updatePreformed();
            }
        });

    }

    @Override
    public void updateComponents() {
        super.updateComponents();
        Transaction bussinessOperation = (Transaction) visibleElement;
        refreshRowCb.setSelected(bussinessOperation.isRefreshRow());
        refreshAllCb.setSelected(bussinessOperation.isRefreshAll());
        showErrorsCb.setSelected(bussinessOperation.isShowErrors());
        askConfirmationCb.setSelected(bussinessOperation.isAskConfirmation());
        confirmationMessageTa.setText(bussinessOperation.getConfirmationMessage());
    }

    @Override
    public void updateSettings(VisibleElement visibleElement) {
        super.updateSettings(visibleElement);
    }

    @Override
    public void updatePreformed() {
        super.updatePreformed();
    }
}
