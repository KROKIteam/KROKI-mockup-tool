package standardForm.administration;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.TextField;
import enumerations.*;
import framework.*;

import net.miginfocom.swing.MigLayout;

public class UserGroupPanel extends JPanel{

	
				private  JTextField tfname = new JTextField(20);
	   		private  JLabel     lblname = new JLabel("name");
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	
	private JButton btnCancel = new JButton();
	
	private JButton btnCommit = new JButton();
	
	public UserGroupPanel(){
		
		btnCancel.setIcon(new ImageIcon("images/remove.gif"));
		btnCommit.setIcon(new ImageIcon("images/commit.gif"));

		setLayout(new MigLayout("", "[align l][align l, grow]", ""));
		add(new JLabel(""),"wrap 20");
					add(lblname,"gapleft 30");
						lblname.setName("name");
						tfname.addKeyListener(new TextFieldWidth(20));
						tfname.setSize(new Dimension(20, tfname.getHeight()));
						tfname.setName("name");
						add(tfname,"grow 0,wrap 20");
		    
		    
		    
		    
		    
		    
		    
		    
		    
		
	}
	 public List<String> getTableColumns(){
		 List<String> retVal = new ArrayList<String>();
		 retVal.add("name");
		 return retVal;
	 }
}
