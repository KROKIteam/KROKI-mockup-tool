package com.panelcomposer.business.procedure;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.panelcomposer.exceptions.DatabaseConnectionException;
import com.panelcomposer.exceptions.StoredProcedureException;
import com.panelcomposer.model.panel.configuration.operation.ParameterType;

/***
 * StoredProcedure gets connection with database and calls stored procedure
 * 
 * @author Darko
 *
 */
public class StoredProcedure {
	
	protected List<ProcedureParameter> parameters;
	protected String procedureName;
	protected Connection conn = null;
	
	protected static ResourceBundle rb = ResourceBundle.getBundle("databases");
	protected static String driver = rb.getString("driver");
	protected static String url = rb.getString("url");
	protected static String username = rb.getString("username");
	protected static String password = rb.getString("password");
	

	/****
	 * Constructor for AbstractProcedure
	 * @param procedureName Name of the stored procedure
	 * @param parameters Input and output parameters declared for the procedure.
	 */
	public StoredProcedure(String procedureName, List<ProcedureParameter> parameters) {
		this.procedureName = procedureName;
		this.parameters = parameters;
	}

	static protected Map<Class<?>, Integer> map = new HashMap<Class<?>, Integer>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			put(Integer.class, java.sql.Types.INTEGER);
			put(String.class, java.sql.Types.VARCHAR);
			put(BigDecimal.class, java.sql.Types.DECIMAL);
			put(Boolean.class, java.sql.Types.BIT);
		}
	};
	


	public List<Object> execute() throws StoredProcedureException {
		List<Object> list = new ArrayList<Object>();
		try {
			conn = getConnection();
			StringBuffer procedure = new StringBuffer("{ call " + procedureName);
			int size = parameters.size();
			if(size > 0) {
				procedure.append("(");
				for (int i = 0; i < size; i++) {
					procedure.append("?,");
				}
				procedure.deleteCharAt(procedure.length()-1);
				procedure.append(")");
			}
			procedure.append(" }");
			System.out.println("procedure: " + procedure);
			CallableStatement cs = conn.prepareCall(procedure.toString());
			for (int i = 0; i < parameters.size(); i++) {
				ParameterType type = parameters.get(i).getType();
				if(type.equals(ParameterType.IN)) {
					cs.setObject(i+1, parameters.get(i).getValue());
				} else if(type.equals(ParameterType.INOUT)) {
					cs.setObject(i+1, parameters.get(i).getValue());
					cs.registerOutParameter((i+1), map.get(parameters.get(i).getValue().getClass()));
				} else if(type.equals(ParameterType.OUT )) {
					cs.setObject(i+1, parameters.get(i).getValue());
					cs.registerOutParameter((i+1), map.get(parameters.get(i).getValue()));
				}
			}
			cs.execute();
			for (int i = 0; i < parameters.size(); i++) {
				if(!parameters.get(i).getType().equals(ParameterType.IN)) {
					list.add(cs.getObject(i+1));
				}
			}
		} catch (Exception e) {
			throw new StoredProcedureException("");
		}
		return list;
	}
	
	/***
	 * 
	 * @return
	 * @throws DatabaseConnectionException
	 */
	protected Connection getConnection() throws DatabaseConnectionException {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			throw new DatabaseConnectionException(this.getClass().getName());
		}
		return conn;
	}

}