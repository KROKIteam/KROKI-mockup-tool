package kroki.app.gui.settings;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import kroki.intl.Intl;
import kroki.profil.VisibleElement;
import kroki.profil.operation.Report;
import net.miginfocom.swing.MigLayout;

/**
 * Tabbed pane showing report settings
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class ReportSettings extends BussinessOperationSettings {

	private static final long serialVersionUID = 1L;
	
	protected JLabel reportNameLb;
	protected JLabel dataFilterLb;
	protected JLabel sortByLb;
	protected JTextField reportNameTf;
	protected JTextField sortByTf;
	protected JTextArea dataFilterTa;
	protected JScrollPane dataFilterSp;

    public ReportSettings(SettingsCreator settingsCreator) {
        super(settingsCreator);
        initComponents();
        layoutComponents();
        addActions();
    }

    private void initComponents() {
        reportNameLb = new JLabel(Intl.getValue("report.reportName"));
        dataFilterLb = new JLabel(Intl.getValue("report.dataFilter"));
        sortByLb = new JLabel(Intl.getValue("report.sortBy"));
        reportNameTf = new JTextField();
        sortByTf = new JTextField();
        dataFilterTa = new JTextArea(5, 30);
        dataFilterTa.setFont(this.getFont());
        dataFilterSp = new JScrollPane(dataFilterTa);
        dataFilterSp.setMinimumSize(dataFilterTa.getPreferredScrollableViewportSize());
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
        intermediate.add(reportNameLb);
        intermediate.add(reportNameTf);
        intermediate.add(dataFilterLb);
        intermediate.add(sortByTf);
        intermediate.add(sortByLb);
        intermediate.add(dataFilterSp);
    }

    private void addActions() {
        reportNameTf.getDocument().addDocumentListener(new DocumentListener() {

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
                Report bussinessOperation = (Report) visibleElement;
                bussinessOperation.setReportName(text);
                updatePreformed();
            }
        });

        dataFilterTa.getDocument().addDocumentListener(new DocumentListener() {

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
                Report bussinessOperation = (Report) visibleElement;
                bussinessOperation.setDataFilter(text);
                updatePreformed();
            }
        });
        sortByTf.getDocument().addDocumentListener(new DocumentListener() {

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
                Report bussinessOperation = (Report) visibleElement;
                bussinessOperation.setSortBy(text);
                updatePreformed();
            }
        });
    }

    @Override
    public void updateComponents() {
        super.updateComponents();
        Report bussinessOperation = (Report) visibleElement;
        reportNameTf.setText(bussinessOperation.getReportName());
        sortByTf.setText(bussinessOperation.getSortBy());
        dataFilterTa.setText(bussinessOperation.getDataFilter());
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
