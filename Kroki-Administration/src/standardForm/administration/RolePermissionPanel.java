package standardForm.administration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import dao.administration.PermissionHibernateDao;
import dao.administration.RoleHibernateDao;
import ejb.administration.Permission;
import ejb.administration.Role;
import framework.GenericStandardForm;
import framework.IEntity;

public class RolePermissionPanel extends JPanel {

	private JComboBox comborole = new JComboBox();
	private JLabel lblrole = new JLabel("Role*");

	private JComboBox combopermission = new JComboBox();
	private JLabel lblpermission = new JLabel("Permission*");

	private JButton btnCancel = new JButton();

	private JButton btnCommit = new JButton();

	public RolePermissionPanel() {

		btnCancel.setIcon(new ImageIcon("images/remove.gif"));
		btnCommit.setIcon(new ImageIcon("images/commit.gif"));

		setLayout(new MigLayout("", "[align l][align l, grow]", ""));
		add(new JLabel(""), "wrap 20");
		add(lblrole, "gapleft 30");
		lblrole.setName("role");
		comborole.setName("role");
		final RoleHibernateDao daorole = new RoleHibernateDao();
		for (Role c : daorole.findAll()) {
			comborole.addItem(c);
		}
		add(comborole, "gapleft 30");
		JButton btnZoomrole = new JButton("...");
		btnZoomrole.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Role role = new Role();
				GenericStandardForm form = new GenericStandardForm(
						(IEntity) role, daorole, new RolePanel());
				form.setCmbForZoom(comborole);
				form.setModal(true);
				form.setLocationRelativeTo(null);
				form.setVisible(true);
			}
		});

		add(btnZoomrole, "grow 0,wrap 20");

		add(lblpermission, "gapleft 30");
		lblpermission.setName("permission");
		combopermission.setName("permission");
		final PermissionHibernateDao daopermission = new PermissionHibernateDao();
		for (Permission c : daopermission.findAll()) {
			combopermission.addItem(c);
		}
		add(combopermission, "gapleft 30");
		JButton btnZoompermission = new JButton("...");
		btnZoompermission.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Permission permission = new Permission();
				GenericStandardForm form = new GenericStandardForm(
						(IEntity) permission, daopermission,
						new PermissionPanel());
				form.setCmbForZoom(combopermission);
				form.setModal(true);
				form.setLocationRelativeTo(null);
				form.setVisible(true);
			}
		});

		add(btnZoompermission, "grow 0,wrap 20");

	}

	public List<String> getTableColumns() {
		List<String> retVal = new ArrayList<String>();
		retVal.add("role");
		retVal.add("permission");
		return retVal;
	}
}
