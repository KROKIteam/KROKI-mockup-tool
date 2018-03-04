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

public class UserPanel extends JPanel {

	private JTextField tfusername = new JTextField(20);
	private JLabel lblusername = new JLabel("Username*");

	private JTextField tfpassword = new JTextField(20);
	private JLabel lblpassword = new JLabel("Password*");

	private JButton btnCancel = new JButton();

	private JButton btnCommit = new JButton();

	public UserPanel() {

		btnCancel.setIcon(new ImageIcon("images/remove.gif"));
		btnCommit.setIcon(new ImageIcon("images/commit.gif"));

		setLayout(new MigLayout("", "[align l][align l, grow]", ""));
		add(new JLabel(""), "wrap 20");
		add(lblusername, "gapleft 30");
		lblusername.setName("username");
		tfusername.addKeyListener(new TextFieldWidth(20));
		tfusername.setSize(new Dimension(20, tfusername.getHeight()));
		tfusername.setName("username");
		add(tfusername, "grow 0,wrap 20");

		add(lblpassword, "gapleft 30");
		lblpassword.setName("password");
		tfpassword.addKeyListener(new TextFieldWidth(20));
		tfpassword.setSize(new Dimension(20, tfpassword.getHeight()));
		tfpassword.setName("password");
		add(tfpassword, "grow 0,wrap 20");

	}

	public List<String> getTableColumns() {
		List<String> retVal = new ArrayList<String>();
		retVal.add("username");
		retVal.add("password");
		return retVal;
	}
}
