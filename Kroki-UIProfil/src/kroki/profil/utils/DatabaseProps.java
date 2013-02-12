package kroki.profil.utils;

import java.io.Serializable;

public class DatabaseProps implements Serializable {
	
	private static final long serialVersionUID = 4393631741627929274L;
	private int profile;
	private String driverClass;
	private String host;
	private int port;
	private String schema;
	private String username;
	private String password;
	private String dialect;
		
	public DatabaseProps() {
		profile = 5;
		driverClass = "org.h2.Driver";
		host = "localhost";
		port = 8082;
		dialect = "H2Dialect";
		schema = "test";
		username = "test";
		password = "test";
	}
	
	public DatabaseProps(int profile, String driverClass, String host, int port, String schema, String username, String password, String dialect) {
		super();
		this.profile = profile;
		this.driverClass = driverClass;
		this.host = host;
		this.port = port;
		this.schema = schema;
		this.username = username;
		this.password = password;
		this.dialect = dialect;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getProfile() {
		return profile;
	}

	public void setProfile(int profile) {
		this.profile = profile;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
}