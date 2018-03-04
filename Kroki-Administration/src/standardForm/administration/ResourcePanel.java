package standardForm.administration;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import framework.TextFieldWidth;
import net.miginfocom.swing.MigLayout;

public class ResourcePanel extends JPanel {

	private JTextField tfname = new JTextField(20);
	private JLabel lblname = new JLabel("Name*");

	private JTextField tflink = new JTextField(20);
	private JLabel lbllink = new JLabel("Link*");
	
	private JTextField tfpanelType = new JTextField(20);
	private JLabel lblpanelType = new JLabel("Panel Type*");

	private JButton btnCancel = new JButton();

	private JButton btnCommit = new JButton();

	public ResourcePanel() {

		btnCancel.setIcon(new ImageIcon("images/remove.gif"));
		btnCommit.setIcon(new ImageIcon("images/commit.gif"));

		setLayout(new MigLayout("", "[align l][align l, grow]", ""));
		add(new JLabel(""), "wrap 20");
		add(lblname, "gapleft 30");
		lblname.setName("name");
		tfname.addKeyListener(new TextFieldWidth(20));
		tfname.setSize(new Dimension(20, tfname.getHeight()));
		tfname.setName("name");
		add(tfname, "grow 0,wrap 20");

		add(lbllink, "gapleft 30");
		lbllink.setName("link");
		tflink.addKeyListener(new TextFieldWidth(20));
		tflink.setSize(new Dimension(20, tflink.getHeight()));
		tflink.setName("link");
		add(tflink, "grow 0,wrap 20");
		
		add(lblpanelType, "gapleft 30");
		lblpanelType.setName("paneltype");
		tfpanelType.addKeyListener(new TextFieldWidth(20));
		tfpanelType.setSize(new Dimension(20, tfpanelType.getHeight()));
		tfpanelType.setName("paneltype");
		add(tfpanelType, "grow 0,wrap 20");

	}

	public List<String> getTableColumns() {
		List<String> retVal = new ArrayList<String>();
		retVal.add("name");
		retVal.add("link");
		retVal.add("paneltype");
		return retVal;
	}
}
