package standardForm.administration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dao.administration.ResourceHibernateDao;
import ejb.administration.Resource;
import framework.GenericStandardForm;
import framework.IEntity;
import net.miginfocom.swing.MigLayout;

public class ResourceHierarchyPanel extends JPanel {

	private JComboBox comboresource = new JComboBox();
	private JLabel lblresource = new JLabel("Resource*");

	private JComboBox comboresource2 = new JComboBox();
	private JLabel lblresource2 = new JLabel("Sub Resource");
	
	private JComboBox comboresource3 = new JComboBox();
	private JLabel lblresource3 = new JLabel("Super Resource");


	public ResourceHierarchyPanel() {

		comboresource.insertItemAt("", 0);
		comboresource2.insertItemAt("", 0);
		comboresource3.insertItemAt("", 0);
		
		setLayout(new MigLayout("", "[align l][align l, grow]", ""));
		add(new JLabel(""), "wrap 20");
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

		add(lblresource2, "gapleft 30");
		lblresource2.setName("subResource");
		comboresource2.setName("subResource");
		final ResourceHibernateDao daoresource2 = new ResourceHibernateDao();
		for (Resource c : daoresource2.findAll()) {
			comboresource2.addItem(c);
		}
		add(comboresource2, "gapleft 30");
		JButton btnZoomresource2 = new JButton("...");
		btnZoomresource2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Resource resource2 = new Resource();
				GenericStandardForm form = new GenericStandardForm(
						(IEntity) resource2, daoresource2, new ResourcePanel());
				form.setCmbForZoom(comboresource2);
				form.setModal(true);
				form.setLocationRelativeTo(null);
				form.setVisible(true);
			}
		});

		add(btnZoomresource2, "grow 0,wrap 20");
		
		add(lblresource3, "gapleft 30");
		lblresource3.setName("superResource");
		comboresource3.setName("superResource");
		final ResourceHibernateDao daoresource3 = new ResourceHibernateDao();
		for (Resource c : daoresource3.findAll()) {
			comboresource3.addItem(c);
		}
		add(comboresource3, "gapleft 30");
		JButton btnZoomresource3 = new JButton("...");
		btnZoomresource3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Resource resource3 = new Resource();
				GenericStandardForm form = new GenericStandardForm(
						(IEntity) resource3, daoresource3, new ResourcePanel());
				form.setCmbForZoom(comboresource3);
				form.setModal(true);
				form.setLocationRelativeTo(null);
				form.setVisible(true);
			}
		});

		add(btnZoomresource3, "grow 0,wrap 20");

	}

	public List<String> getTableColumns() {
		List<String> retVal = new ArrayList<String>();
		retVal.add("resource");
		retVal.add("subResource");
		retVal.add("superResource");
		return retVal;
	}
}
