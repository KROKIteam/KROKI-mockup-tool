package standardForm.administration;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import dao.administration.RoleHibernateDao;
import ejb.administration.Resource;
import ejb.administration.Role;
import framework.GenericStandardForm;
import framework.IEntity;
import framework.TextFieldWidth;

public class RolePanel extends JPanel {

	private JTextField tfname = new JTextField(20);
	private JLabel lblname = new JLabel("Name*");

	private JComboBox comborole = new JComboBox();
	private JLabel lblrole = new JLabel("Parent Role");

	public RolePanel() {

		comborole.insertItemAt("", 0);
		
		setLayout(new MigLayout("", "[align l][align l, grow]", ""));
		add(new JLabel(""), "wrap 20");
		add(lblname, "gapleft 30");
		lblname.setName("name");
		tfname.addKeyListener(new TextFieldWidth(20));
		tfname.setSize(new Dimension(20, tfname.getHeight()));
		tfname.setName("name");
		add(tfname, "gapleft 30, wrap 20, span");
		
		add(lblrole, "gapleft 30");
		lblrole.setName("parentRole");
		comborole.setName("parentRole");
		
		final RoleHibernateDao daorole = new RoleHibernateDao();
		for (Role c : daorole.findAll()) {
			comborole.addItem(c);
		}
		add(comborole, "gapleft 30");
		JButton btnZoomresource3 = new JButton("...");
		btnZoomresource3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Role role = new Role();
				GenericStandardForm form = new GenericStandardForm(
						(IEntity) role, daorole, new ResourcePanel());
				form.setCmbForZoom(comborole);
				form.setModal(true);
				form.setLocationRelativeTo(null);
				form.setVisible(true);
			}
		});

		add(btnZoomresource3, "grow 0,wrap 20");

	}

	public List<String> getTableColumns() {
		List<String> retVal = new ArrayList<String>();
		retVal.add("name");
		retVal.add("parentRole");
		return retVal;
	}
}
