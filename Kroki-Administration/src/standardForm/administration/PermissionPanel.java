package standardForm.administration;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.administration.OperationHibernateDao;
import dao.administration.ResourceHibernateDao;
import ejb.administration.Operation;
import ejb.administration.Resource;
import framework.GenericStandardForm;
import framework.IEntity;
import framework.TextFieldWidth;
import net.miginfocom.swing.MigLayout;

public class PermissionPanel extends JPanel {

	private JTextField tfname = new JTextField(20);
	private JLabel lblname = new JLabel("Name*");

	private JComboBox combooperation = new JComboBox();
	private JLabel lbloperation = new JLabel("Operation*");

	private JComboBox comboresource = new JComboBox();
	private JLabel lblresource = new JLabel("Resource*");

	private JButton btnCancel = new JButton();

	private JButton btnCommit = new JButton();

	public PermissionPanel() {

		btnCancel.setIcon(new ImageIcon("images/remove.gif"));
		btnCommit.setIcon(new ImageIcon("images/commit.gif"));

		setLayout(new MigLayout("", "[align l][align l, grow]", ""));
		add(new JLabel(""), "wrap 20");
		add(lblname, "gapleft 30");
		lblname.setName("name");
		tfname.addKeyListener(new TextFieldWidth(20));
		tfname.setSize(new Dimension(20, tfname.getHeight()));
		tfname.setName("name");
		add(tfname, "gapleft 30, wrap 20, span");

		add(lbloperation, "gapleft 30");
		lbloperation.setName("operation");
		combooperation.setName("operation");
		final OperationHibernateDao daooperation = new OperationHibernateDao();
		for (Operation c : daooperation.findAll()) {
			combooperation.addItem(c);
		}
		add(combooperation, "gapleft 30");
		JButton btnZoomoperation = new JButton("...");
		btnZoomoperation.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Operation operation = new Operation();
				GenericStandardForm form = new GenericStandardForm(
						(IEntity) operation, daooperation, new OperationPanel());
				form.setCmbForZoom(combooperation);
				form.setModal(true);
				form.setLocationRelativeTo(null);
				form.setVisible(true);
			}
		});

		add(btnZoomoperation, "grow 0,wrap 20");

		add(lblresource, "gapleft 30");
		lblresource.setName("resource");
		comboresource.setName("resource");
		final ResourceHibernateDao daoresource = new ResourceHibernateDao();
		for (Resource c : daoresource.findAll()) {
			comboresource.addItem(c);
		}
		add(comboresource, "gapleft 30");
		JButton btnZoomresource = new JButton("...");
		btnZoomresource.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Resource resource = new Resource();
				GenericStandardForm form = new GenericStandardForm(
						(IEntity) resource, daoresource, new ResourcePanel());
				form.setCmbForZoom(comboresource);
				form.setModal(true);
				form.setLocationRelativeTo(null);
				form.setVisible(true);
			}
		});

		add(btnZoomresource, "grow 0,wrap 20");

	}

	public List<String> getTableColumns() {
		List<String> retVal = new ArrayList<String>();
		retVal.add("name");
		retVal.add("operation");
		retVal.add("resource");
		return retVal;
	}
}
