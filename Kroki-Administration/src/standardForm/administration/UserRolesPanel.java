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

import dao.administration.RoleHibernateDao;
import dao.administration.UserHibernateDao;
import ejb.administration.Role;
import ejb.administration.User;
import framework.GenericStandardForm;
import framework.IEntity;
import net.miginfocom.swing.MigLayout;

public class UserRolesPanel extends JPanel {

	private JComboBox combouser = new JComboBox();
	private JLabel lbluser = new JLabel("User*");

	private JComboBox comborole = new JComboBox();
	private JLabel lblrole = new JLabel("User Role*");

	private JButton btnCancel = new JButton();

	private JButton btnCommit = new JButton();

	public UserRolesPanel() {

		btnCancel.setIcon(new ImageIcon("images/remove.gif"));
		btnCommit.setIcon(new ImageIcon("images/commit.gif"));

		setLayout(new MigLayout("", "[align l][align l, grow]", ""));
		add(new JLabel(""), "wrap 20");
		add(lbluser, "gapleft 30");
		lbluser.setName("user");
		combouser.setName("user");
		final UserHibernateDao daouser = new UserHibernateDao();
		for (User c : daouser.findAll()) {
			combouser.addItem(c);
		}
		add(combouser, "gapleft 30");
		JButton btnZoomuser = new JButton("...");
		btnZoomuser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				User user = new User();
				GenericStandardForm form = new GenericStandardForm(
						(IEntity) user, daouser, new UserPanel());
				form.setCmbForZoom(combouser);
				form.setModal(true);
				form.setLocationRelativeTo(null);
				form.setVisible(true);
			}
		});

		add(btnZoomuser, "grow 0,wrap 20");

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

	}

	public List<String> getTableColumns() {
		List<String> retVal = new ArrayList<String>();
		retVal.add("user");
		retVal.add("role");
		return retVal;
	}
}
