package com.panelcomposer.core;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/***
 * User that can be logged into the application.
 * 
 */
@Entity
@Table(name = "User")
public class User {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	protected int id;
	/***
	 * User's username
	 */
	@Column(name = "username", nullable = false, unique = true, length = 20)
	protected String username;
	/***
	 * User's password
	 */
	@Column(name = "password", nullable = false, unique = false, length = 40)
	protected String password;

	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

}
