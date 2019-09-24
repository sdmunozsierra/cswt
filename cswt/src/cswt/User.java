package cswt;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class User {
	private String username;
	private String password;
	private String type;
	private String actualName;
	private String email;
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the actualName
	 */
	public String getActualName() {
		return actualName;
	}
	/**
	 * @param actualName the actualName to set
	 */
	public void setActualName(String actualName) {
		this.actualName = actualName;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return The user as a JSON Object
	 */
	public JSONObject toJSON() {
		Map<String, Object> map = new HashMap<>();
		map.put("username", username);
		map.put("password", password);
		map.put("type", type);
		map.put("actualName", actualName);
		map.put("email", email);
		return new JSONObject(map);
	}

}
