package framework;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

import net.sourceforge.jdatepicker.impl.JDatePickerImpl;

@SuppressWarnings({ "serial", "rawtypes" })
public class GenericStandardForm extends JDialog implements IStandardForm {

	private GenericToolbar toolbar;

	private GenericTableModel model;
	private JTable table;

	private GenericDataPanel dataPanel;

	final int ADD_STATE = 0;
	final int EDIT_STATE = 1;
	final int SEARCH_STATE = 2;
	int current_state = EDIT_STATE;

	private JPanel fieldsPanel;
	private GenericDao dao;
	private IEntity entity;

	private JComboBox cmbForZoom;

	private static final String DAO_SUFFIX = "HibernateDao";
	private static final String DAO_PACKAGE = "dao";
	private static final String PANEL_SUFFIX = "Panel";
	private static final String PANEL_PACKAGE = "standardForm";

	@SuppressWarnings("unchecked")
	public GenericStandardForm(IEntity entity, final GenericDao dao, JPanel fieldsPanel) {
		this.entity = entity;
		this.dao = dao;
		this.fieldsPanel = fieldsPanel;
		setTitle(entity.getClass().getSimpleName());
		setSize(800, 800);

		toolbar = new GenericToolbar(this);

		String[] columns = getFieldLabels(fieldsPanel);

		model = new GenericTableModel(dao, columns);
		model.fillData();

		table = new JTable();
		table.setModel(model);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				dataSync();
			}
		});

		// sakrivam kolonu ID
		int index = table.getColumnModel().getColumnIndex("ID");
		table.getColumnModel().removeColumn(table.getColumnModel().getColumn(index));

		JScrollPane scroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(500, 500));

		dataPanel = new GenericDataPanel(this, fieldsPanel);

		setLayout(new BorderLayout());

		add(toolbar, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		add(dataPanel, BorderLayout.SOUTH);

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GenericStandardForm(IEntity entity, final GenericDao dao,
			JPanel fieldsPanel, String reportPath) {
		this.entity = entity;
		this.dao = dao;
		this.fieldsPanel = fieldsPanel;
		setTitle(entity.getClass().getSimpleName());
		setSize(600, 600);

		toolbar = new GenericToolbar(this);
		
		if (reportPath != null) {
			JButton btnReport = new JButton("Report");
			btnReport.addActionListener(new ReportAction(reportPath));	
			toolbar.add(btnReport);
		}

		String[] columns = getFieldLabels(fieldsPanel);

		model = new GenericTableModel(dao, columns);
		model.fillData();

		table = new JTable();
		table.setModel(model);
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent arg0) {
						dataSync();
					}
				});

		// sakrivam kolonu ID
		int index = table.getColumnModel().getColumnIndex("ID");
		table.getColumnModel().removeColumn(
				table.getColumnModel().getColumn(index));

		JScrollPane scroll = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(500, 500));

		dataPanel = new GenericDataPanel(this, fieldsPanel);

		setLayout(new BorderLayout());

		add(toolbar, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		add(dataPanel, BorderLayout.SOUTH);

	}
	/**
	 * Metoda koja sa panela za konkretnu perzistentnu klasu kupi labele i
	 * dodaje u table model
	 */
	public String[] getFieldLabels(JPanel panel) {
		ArrayList<String> labels = new ArrayList<String>();

		for (Component cmp : panel.getComponents()) {
			if (cmp instanceof JLabel) {
				JLabel label = (JLabel) cmp;
				String text = label.getText();

				if (!text.isEmpty()) {
					if (text.contains("*")) {
						text = text.replace("*", "").trim();
					}
					if (text.contains(" ")) {
						text = text.replace(" ", "");
					}
					labels.add(text);
				}
			}
		}

		String[] retVal = new String[labels.size() + 1];
		if (!labels.isEmpty()) {
			retVal[0] = "ID";
		}
		for (int i = 1; i < retVal.length; i++) {
			retVal[i] = labels.get(i - 1);
		}

		return retVal;
	}

	@Override
	public void add() {
		dataPanel.clearDataPanel(dataPanel);
		current_state = ADD_STATE;
		dataPanel.changeStatus(current_state);
	}

	@Override
	public void delete() {
		int row = table.getSelectedRow();
		if (row == -1)
			return;

		model.deleteRow(row);
	}

	@Override
	public void next(JComponent caller) {
		int rowCount = table.getSelectedRow();
		if (rowCount == -1)
			return;

		String entityClassName = entity.getClass().getSimpleName();
		System.out.println("entityClassName: " + entityClassName);
		Field[] fields = entity.getClass().getDeclaredFields();
		// moram entity-ju da set-ujem vrednosti,
		// jer je trenutno prazan
		for (Field f : fields) {
			for (Component c : fieldsPanel.getComponents()) {
				if (f.getName().equals(c.getName())) {
					String methodName = "set" + fieldNameWithUpperCase(f.getName());
					if (c instanceof JTextField) {
						invokeMethodWithParameter(methodName, ((JTextField) c).getText());
					}
				}
			}
		}
		ArrayList<Field> nextCandidates = new ArrayList<Field>();
		for (Field f : fields) {
			Annotation[] annotations = f.getAnnotations();
			for (Annotation a : annotations) {
				if (a.annotationType().equals(javax.persistence.OneToMany.class)) {
					nextCandidates.add(f);
				}
			}
		}

		if (nextCandidates.size() == 0) {
			return;
		} else if (nextCandidates.size() > 1) {
			JPopupMenu popUp = new JPopupMenu();
			for (final Field field : nextCandidates) {
				JMenuItem item = new JMenuItem(field.getName());
				popUp.add(item);
				
				preprareNextForm(entityClassName, fields, field, item);
			}

			popUp.show(caller, 0, 0);
		} else {
			preprareNextForm(entityClassName, fields, nextCandidates.get(0), null);
		}
	}

	private void preprareNextForm(String entityClassName, Field[] fields, final Field field, JMenuItem item) {
		
		ParameterizedType typeInSet = (ParameterizedType) field.getGenericType();
        Class<?> classInSet = (Class<?>) typeInSet.getActualTypeArguments()[0];
		
        String fieldName = fieldNameWithUpperCase(field.getName());
		
		String classPackage = classInSet.getPackage().getName();
		String panelPackage = getPackageName(classPackage, PANEL_PACKAGE);
		String daoPackage =getPackageName(classPackage, DAO_PACKAGE);

		final IEntity nextEntity = (IEntity) getClassInstance(classPackage, fieldName, "");
		final GenericDao nextDao = (GenericDao) getClassInstance(daoPackage, fieldName, DAO_SUFFIX);
		final JPanel panel = (JPanel) getClassInstance(panelPackage, fieldName, PANEL_SUFFIX);

		//deo u kom selektujem parent entity na child entity-ju.
		for (Component c : panel.getComponents()) {
			if (c instanceof JComboBox) {
				if (c.getName().equals(entityClassName.toLowerCase())) {
					ComboBoxModel cmbModel = ((JComboBox) c).getModel();
					for (int i = 0; i < cmbModel.getSize(); i++) {
						Object entityAt = cmbModel.getElementAt(i);
						if (entity.toString().equals(entityAt.toString())) {
							cmbModel.setSelectedItem(entityAt);
							c.setEnabled(false);
						}
					}
				}
			}

		}
		
		if(item != null){
			setItemListener(item, nextEntity, nextDao, panel);
		} else {
			instantiationOfNextForm(nextEntity, nextDao, panel);
		}
	}


	/**
	 * Metoda koja vraca ime atributa klase sa velikim prvim slovom.
	 * Primer: enterprise -> Enterprise.
	 * @param fieldName
	 * @return String
	 */
	private String fieldNameWithUpperCase(String fieldName) {
		// prvo slovo velikim
		String fl = fieldName.substring(0, 1).toUpperCase();
		// ostala malim
		String ol = fieldName.substring(1, fieldName.length());
		return fl + ol;
	}

	/**
	 * Metoda koja vraca instancu odredjene klase.
	 * 
	 * @param packageName - ime paketa u kom se nalazi klasa ciji se objekat zeli instancirati.
	 * @param fieldName - naziv atributa po cijem nazivu se trazi odgovarajuca klasa.
	 * @param suffix - dodatak na ime klase, npr za panel suffix je 'Panel', za dao je 'HibernateDao'.
	 * @return
	 */
	private Object getClassInstance(String packageName, String fieldName, String suffix) {
		Object retVal = null;
		try {
			String className = packageName + "." + fieldName + suffix;
			Class c = Class.forName(className.trim());
			retVal = c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	/**
	 * Metoda koja vraca naziv paketa na osnovu zadatog parametra.
	 * S obzirom da se generisani kod paketira uvek po istom sablonu,
	 * dovoljno je da se uradi replace stringa 'ejb' sa odredjenim parametrom,
	 * da bi se dobio zeljeni paket.
	 * @param parameter
	 * @return
	 */
	private String getPackageName(String className, String parameter) {
		return className.replace("ejb", parameter);
	}
	
	/**
	 * Klasa koja instancira genericku standardnu formu prilikom koriscenja next mehanizma unutar metoda
	 * {@link #preprareNextForm(String, Field[], Field, JMenuItem)}, te
	 * {@link #setItemListener(JMenuItem, IEntity, GenericDao, JPanel)}.
	 * 
	 * @param nextEntity - child entitet.
	 * @param nextDao - dao bean od child entiteta.
	 * @param panel - panel od child entiteta.
	 */
	private void instantiationOfNextForm(final IEntity nextEntity, final GenericDao nextDao, final JPanel panel) {
		GenericStandardForm form = new GenericStandardForm(nextEntity, nextDao, panel);
		form.setModal(true);
		form.setLocationRelativeTo(null);
		form.setVisible(true);
	}
	
	/**
	 * Metoda koja prosledjenom JMenuItem-u set-uje listener, koji instancira
	 * genericku standardnu formu prilikom koriscenja next mehanizma pomocu metode 
	 * {@link #instantiationOfNextForm(IEntity, GenericDao, JPanel)}.
	 * @param item
	 * @param nextEntity
	 * @param nextDao
	 * @param panel
	 */
	private void setItemListener(JMenuItem item, final IEntity nextEntity, final GenericDao nextDao, final JPanel panel) {
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				instantiationOfNextForm(nextEntity, nextDao, panel);
			}
		});
	}

	@Override
	public void firstRow() {
		int rowCount = table.getRowCount();
		if (rowCount == 0)
			return;

		table.setRowSelectionInterval(0, 0);
		table.scrollRectToVisible(new Rectangle(table.getCellRect(0, 0, true)));
	}

	@Override
	public void previousRow() {
		int rowCount = table.getRowCount();
		int row = table.getSelectedRow();
		if (rowCount == 0 || row == -1)
			return;

		if (row == 0) {
			table.setRowSelectionInterval(rowCount - 1, rowCount - 1);
			table.scrollRectToVisible(new Rectangle(table.getCellRect(rowCount - 1, 0, true)));
		} else {
			table.setRowSelectionInterval(row - 1, row - 1);
			table.scrollRectToVisible(new Rectangle(table.getCellRect(row - 1, 0, true)));
		}

	}

	@Override
	public void nextRow() {
		int rowCount = table.getRowCount();
		int row = table.getSelectedRow();
		if (rowCount == 0 || row == -1)
			return;

		if (row == rowCount - 1) {
			table.setRowSelectionInterval(0, 0);
			table.scrollRectToVisible(new Rectangle(table.getCellRect(0, 0, true)));
		} else {
			table.setRowSelectionInterval(row + 1, row + 1);
			table.scrollRectToVisible(new Rectangle(table.getCellRect(row + 1, 0, true)));
		}

	}

	@Override
	public void lastRow() {
		int rowCount = table.getRowCount();
		if (rowCount == 0)
			return;

		table.setRowSelectionInterval(rowCount - 1, rowCount - 1);
		table.scrollRectToVisible(new Rectangle(table.getCellRect(rowCount - 1, 0, true)));

	}

	@Override
	public void search() {
		dataPanel.clearDataPanel(dataPanel);
		current_state = SEARCH_STATE;
		dataPanel.changeStatus(current_state);
	}

	@Override
	public void refresh() {
		model.setRowCount(0);
		model.fillData();
	}

	@Override
	public void previousForm() {

		int row = table.getSelectedRow();
		if (row == -1 || cmbForZoom == null)
			return;

		Integer id = (Integer) model.getValueAt(row, 0);
		IEntity entity = dao.findById(id);

		// radim ovako da se ne bi morala preklapati equals(),
		// mada bi bilo bolje sa equals().
		ComboBoxModel cmbModel = cmbForZoom.getModel();
		for (int i = 0; i < cmbModel.getSize(); i++) {
			Object entityAt = cmbModel.getElementAt(i);
			if (entity.toString().equals(entityAt.toString())) {
				cmbModel.setSelectedItem(entityAt);
			}
		}

		setVisible(false);
		dispose();
	}

	@Override
	public void help() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void commit() {
		switch (current_state) {
		case ADD_STATE:
			if(!validation()){
				break;
			}
			setEntityValuesFromFields();
			model.insertRow((AbstractEntity) entity);
			dataPanel.clearDataPanel(dataPanel);
			entity = makeNewInstance(entity);
			break;

		case EDIT_STATE:
			int row = table.getSelectedRow();
			if (row == -1)
				return;

			Integer id = (Integer) model.getValueAt(row, 0);
			setEntityId(entity, id);
			if(!validation()){
				break;
			}
			setEntityValuesFromFields();
			model.updateRow((AbstractEntity) entity, row);
			break;

		case SEARCH_STATE:
			SearchEngine engine = new SearchEngine(entity, fieldsPanel, model);
			engine.search();
			break;
		}
	}
	
	private IEntity makeNewInstance(IEntity currentEntity){
		try {
			return currentEntity.getClass().newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; 
	}

	@Override
	public void cancel() {
		table.clearSelection();
		dataPanel.clearDataPanel(dataPanel);
		current_state = EDIT_STATE;
		dataPanel.changeStatus(current_state);
	}

	/**
	 * Metoda koja prolazi kroz sve komponente na panelu sa podacima, te vrsi
	 * set-ovanje tih vrednosti entitetu koji je potrebno perzistirati.
	 */
	private void setEntityValuesFromFields() {
		for (Component cmp : fieldsPanel.getComponents()) {
			if (cmp instanceof JTextField) {
				String methodName = nameBuilder(cmp);
				invokeMethodWithParameter(methodName, ((JTextField) cmp).getText());

			} else if (cmp instanceof JComboBox) {
				JComboBox comboBox = (JComboBox) cmp;
				String methodName = nameBuilder(comboBox);
				Object object = comboBox.getSelectedItem();
				invokeMethodWithParameter(methodName, object);
			} else if(cmp instanceof JScrollPane){
				JTextArea txtArea = (JTextArea)((JScrollPane)cmp).getViewport().getView();
				String methodName = nameBuilder(txtArea);
				invokeMethodWithParameter(methodName, txtArea.getText());
			} else if(cmp instanceof JDatePickerImpl){
				Date date = (Date) ((JDatePickerImpl)cmp).getModel().getValue();
				String methodName = nameBuilder(cmp);
				invokeMethodWithParameter(methodName, date);
			} else if (cmp instanceof JCheckBox) {
				JCheckBox cBox = (JCheckBox)cmp;
				String methodName = nameBuilder(cBox);
				invokeMethodWithParameter(methodName, (cBox.isSelected()) ? "true" : "false");
			}
		}

	}

	/**
	 * Metoda koja vrsi pozivanje set metode entiteta, pri cemu joj se prosledi
	 * i parametar koji se set-uje.
	 * 
	 * @param methodName
	 *            - naziv set metode
	 * @param parameter
	 *            - parametar koji se set-uje.
	 */
	private Object invokeMethodWithParameter(String methodName, Object... parameter) {
		for (Method m : entity.getClass().getMethods()) {
			if (methodName.equals(m.getName())) {
				try {
					Class[] parameterTypes = m.getParameterTypes();
					if(parameterTypes[0].equals(Integer.class)){
						Integer value = Integer.parseInt((String)parameter[0]);
						return m.invoke(entity, value);
					}
					
					if(parameterTypes[0].equals(Float.TYPE)){
						Float value = Float.parseFloat((String)parameter[0]);
						return m.invoke(entity, value);
					}
					
					if(parameterTypes[0].equals(Boolean.class)){
						Boolean value = new Boolean((String)parameter[0]);
						return m.invoke(entity, value);
					}
					
					return m.invoke(entity, parameter);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	

	/**
	 * Metoda koja na osnovu naziva komponente kreira naziv set metode za
	 * atribut sa istim nazivom.
	 * 
	 * @param cmp
	 *            - Komponenta za koju se kreira naziv set metode
	 * @return naziv set metode koju je potrebno pozvati
	 */
	private String nameBuilder(Component cmp) {
		StringBuilder builder = new StringBuilder();
		builder.append("set");
		// prvo slovo veliko
		String firstLetter = cmp.getName().substring(0, 1).toUpperCase();
		// ostala malim
		String otherLetters = cmp.getName().substring(1, cmp.getName().length());
		builder.append(firstLetter);
		builder.append(otherLetters);

		String methodName = builder.toString();
		return methodName;
	}

	/**
	 * Pozivanje setId() metode za entitet koji se perzistira. Koristi se
	 * prilikom izmene ili brisanja entiteta.
	 * 
	 * @param entity
	 *            - entitet koji se zeli perzistirati
	 * @param id
	 *            - ID entiteta.
	 */
	private void setEntityId(IEntity entity, Integer id) {
		try {
			Method method = entity.getClass().getMethod("setId", Integer.class);
			method.invoke(entity, id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void dataSync() {
		if(current_state == ADD_STATE)
			return;
		
		int row = table.getSelectedRow();
		if (row == -1)
			return;

		for (int i = 1; i < model.getColumnCount(); i++) {
			for (Component cmp : fieldsPanel.getComponents()) {
				if (cmp instanceof JTextField) {
					processTextComponent(row, i, (JTextComponent) cmp);
				} else if (cmp instanceof JComboBox) {
					processComboBox(row, i, (JComboBox)cmp);
				} else if(cmp instanceof JScrollPane){
					JTextArea txtArea = (JTextArea)((JScrollPane)cmp).getViewport().getView();
					processTextComponent(row, i, txtArea);
				}else if (cmp instanceof JDatePickerImpl){
					processDateComponent(row, i, (JDatePickerImpl) cmp);
				} else if (cmp instanceof JCheckBox) {
					processJCheckBoxComponent(row, i, (JCheckBox)cmp);
				}

			}
		}
	}
	
	/**
	 * Metoda proverava da li se unutar imena komponente nalazi znak '*'.
	 * Ukoliko se nalazi, znak se izbacuje.
	 * @param name
	 * @return
	 */
	private String procesRequired(String name) {
		if (name.contains("*")) {
			name = name.replace("*", "").trim();
		}
		return name;
	}
	
	
	private void processComboBox(int row, int i, JComboBox cmp) {
		String name = procesRequired(cmp.getName());
		String columnName = extractColumnName(i);
		if (name.equals(columnName)) {
			String value = (String) model.getValueAt(row, i);
			ComboBoxModel cmbModel = ((JComboBox) cmp).getModel();
			for (int j = 0; j < cmbModel.getSize(); j++) {
				Object object = cmbModel.getElementAt(j);
				if (object.toString().equalsIgnoreCase(value)) {
					cmbModel.setSelectedItem(object);
				}
			}
		}
	}
	
	/**
	 * Metoda koja vrsi sinhronizaciju zadatu tekstualnu komponentu.
	 * Poziva se iz {@link #dataSync()} metode.
	 * @param row
	 * @param i
	 * @param cmp
	 */
	private void processTextComponent(int row, int i, JTextComponent cmp) {
		String name = procesRequired(cmp.getName());

		String columnName = extractColumnName(i);
		if (name.equals(columnName)) {
			Object value = model.getValueAt(row, i);
			if(value != null)
				((JTextComponent) cmp).setText(value.toString());
		}
	}
	
	private void processJCheckBoxComponent(int row, int i, JCheckBox cmp) {
		String name = procesRequired(cmp.getName());
		
		String columnName = extractColumnName(i);
		if (name.equals(columnName)) {
			Object value = model.getValueAt(row, i);
			if (value != null) {
				if (value.toString().equals("true"))
					 cmp.setSelected(true);
				else
					cmp.setSelected(false);
			}
		}
	}
	
	/**
	 * Metoda koja vrsi sinhronizaciju zadatu datumsku komponentu.
	 * Poziva se iz {@link #dataSync()} metode.
	 * @param row
	 * @param i
	 * @param cmp
	 */
	private void processDateComponent(int row, int i, JDatePickerImpl cmp){
		String name = procesRequired(cmp.getName());
		
		String columnName = extractColumnName(i);
		if (name.equals(columnName)) {
			String dateString = String.valueOf(model.getValueAt(row, i));
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				date = dateFormat.parse(dateString);
				((JDatePickerImpl)cmp).getModel().setDate(date.getYear()+1900, date.getMonth(), date.getDate());
				((JDatePickerImpl)cmp).getModel().setSelected(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Metoda na osnovu imena kolone unutar tabele kreira
	 * naziv komponente koju je potrebno naci na panelu za unos.
	 * @param i
	 * @return
	 */
	private String extractColumnName(int i) {
		String columnName = model.getColumnName(i);
		if(columnName.contains("*")){
			columnName = columnName.replace("*", "").trim();
		}
		if(columnName.contains(" ")){
			StringBuffer buffer = new StringBuffer();
			StringTokenizer tokenizer = new StringTokenizer(columnName);
			int firstToken = 0;
			while(tokenizer.hasMoreElements()){
				String t = (String) tokenizer.nextElement();
				if(firstToken == 0){
					t = t.toLowerCase();
					firstToken++;
				}else{
					//svako sledeca rec pocinje velikim slovom, camel note.
					t = t.substring(0,1).toUpperCase() + t.substring(1, t.length());
				}
				buffer.append(t);
			}
			columnName = buffer.toString();
		}else{
			columnName = columnName.toLowerCase();
		}
		return columnName;
	}

	public void setCmbForZoom(JComboBox cmbForZoom) {
		this.cmbForZoom = cmbForZoom;
	}
	
	/**
	 * Metoda vrsi validaciju nad formom
	 * 
	 * @return true ako je sve uredu, u suprotnom false
	 * */
	private boolean validation(){
		boolean retVal = true;
		retVal = requiredValidation();
		if(!retVal){
			return false;
		}
		retVal = numberValidation();
		if(!retVal){
			return false;
		}
		return retVal;
	}
	/** 
	 * Metoda proverava da li su tekstualna polja i combobox-evi popunjeni ako isprednjih stoji labela koja sadrzi *
	 * 
	 * @return true ako je sve uredu, false ako neko tekstualno polje ili combobox nije popunjeno.
	 * */
	private boolean requiredValidation(){
		Map<String,String> labels = new HashMap<String,String>();
		for (Component cmp : fieldsPanel.getComponents()) {
			if (cmp instanceof JLabel) {
				JLabel label = (JLabel) cmp;
				String text = label.getText();
				if (!text.isEmpty()) {
					if(text.endsWith("*")){
						labels.put(label.getName(),text.substring(0,text.length()-1));
					}
				}
			}
		}
		for (Component cmp : fieldsPanel.getComponents()) {
			if (cmp instanceof JTextField) {
					if(labels.containsKey(cmp.getName()) && (((JTextField) cmp).getText().equals("")  || ((JTextField) cmp).getText()== null)){
						JOptionPane.showMessageDialog(null, "Textualno polje "+"'"+labels.get(cmp.getName())+"'"+" ne sme da bude prazno!");
						return false;
					}
			}else if (cmp instanceof JComboBox) {
					@SuppressWarnings("rawtypes")
					JComboBox comboBox = (JComboBox) cmp;
					Object object = comboBox.getSelectedItem();
					if(labels.containsKey(cmp.getName()) && (((JComboBox) cmp).getSelectedItem() == null)){
						JOptionPane.showMessageDialog(null, "Combobox "+"'"+labels.get(cmp.getName())+"'"+" ne sme da bude prazan!");
						return false;
					}
			}else if(cmp instanceof JScrollPane) {
				JTextArea tempArea = (JTextArea) ((JScrollPane)cmp).getViewport().getView();
				if(labels.containsKey(tempArea.getName()) && (tempArea.getText().equals("")  || tempArea.getText()==null)){
					JOptionPane.showMessageDialog(null, "Textualno polje "+"'"+labels.get(tempArea.getName())+"'"+" ne sme da bude prazno!");
					return false;
				}
			}else if(cmp instanceof JDatePickerImpl){
				Date selectedDate = (Date) ((JDatePickerImpl)cmp).getModel().getValue();
				if(selectedDate == null){
					JOptionPane.showMessageDialog(null, "Polje za datum "+"'"+labels.get(((JDatePickerImpl)cmp).getName())+"'"+" ne sme da bude prazno!");
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * Metoda provera da li se u tekstualnim poljima koja su predvidjena za unos celih ili realnih brojeva
	 * nalazi takav sadrzaj
	 * 
	 * @return true ako je sve uredu, false ako u tekstualno polje nije upisan trazeni broj.
	 * */
	private boolean numberValidation(){
		Map<String,String> numberFields = new HashMap<String,String>();
		for (Component cmp : fieldsPanel.getComponents()) {
			if (cmp instanceof TextField) {
				TextField fieldNumber = (TextField) cmp;
				String text = fieldNumber.getText();
				numberFields.put(fieldNumber.getName(), text);
			}
		}
		Map<String,String> labels = new HashMap<String,String>();
		for (Component cmp : fieldsPanel.getComponents()) {
			if (cmp instanceof JLabel) {
				JLabel label = (JLabel) cmp;
				if(numberFields.containsKey(label.getName())){
					String text = label.getText();
					if (!text.isEmpty()) {
						if(text.endsWith("*")){
							labels.put(label.getName(),text.substring(0,text.length()-1));
						}else{
							labels.put(label.getName(),text);
						}
					}
				}
			}
		}
		for (Component cmp : fieldsPanel.getComponents()) {
			if (cmp instanceof JTextField) {
				JTextField fieldNumber = (JTextField) cmp;
				if(numberFields.containsKey(fieldNumber.getName())){
					if(numberFields.get(fieldNumber.getName()).equals("I")){ // da li je polje Integer
						try{
							Integer.parseInt(fieldNumber.getText());
						}catch(NumberFormatException e){
							JOptionPane.showMessageDialog(null, "Textualno polje "+"'"+labels.get(fieldNumber.getName())+"'"+" mora biti ceo broj!");
							return false;
						}
					}else if(numberFields.get(fieldNumber.getName()).equals("R")){ // da li je polje Real
						try{
							Double.parseDouble(fieldNumber.getText());
						}catch(NumberFormatException e){
							JOptionPane.showMessageDialog(null, "Textualno polje "+"'"+labels.get(fieldNumber.getName())+"'"+" mora biti realan broj!");
							return false;
						}
					}
				}
			}
		}
		return true;
	}
}
