package client;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import cswt.User;

public class ClientUserManager {
	
	private List<User> users;
	private List<String> usernames;
	
	public ClientUserManager() {
		this.users = new ArrayList<User>();
		this.usernames = new ArrayList<String>();
	}
	

	/** Adds a user to the manager. 
	 * @param user The user to be added
	 * */
	public synchronized void addUser(User user) {
		this.users.add(user);
		this.usernames.add(user.getUsername());
	}
	
	/** Removes a user from the manager and storage. 
	 * @param username The username of the user to be removed
	 * */
	public synchronized void removeUser(String username) {
		int index = this.usernames.indexOf(username);
		this.usernames.remove(index);
		this.users.remove(index);
	}
	
	
	/** Parses an user from a JSONObject 
	 * @param obj The JSONObject to be parsed
	 * */
	public synchronized User fromJSON(JSONObject obj) {
		try {
		    User user = new User();
		    user.setActualName(obj.getString("actualName"));
		    user.setEmail(obj.getString("email"));
		    user.setPassword(obj.getString("password"));
		    user.setType(obj.getString("type"));
		    user.setUsername(obj.getString("username"));
		    return user;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/** Gets a user based on provided username
	 * @param username The provided username
	 * @return The user or null if no user with that username exists
	 * */
	public synchronized User getUser(String username) {
		int index = this.usernames.indexOf(username);
		if (index == -1) {
			return null;
		}
		return this.users.get(index);
	}
	
	/** Checks if a username has been registered in the manager
	 * @param username The provided username
	 * @return If the user is in the manager or not
	 * */
	public synchronized boolean hasUser(String username) {
		int index = this.usernames.indexOf(username);
		if (index == -1) {
			return false;
		}
		return true;
	}
	
	/** Gets all current users
	 * @return The list of all users
	 * */
	public synchronized List<User> getAllUsers() {
		return this.users;
	}
	
	/** Clear manager fields
	 * */
	public synchronized void clearManager() {
		this.usernames.clear();
		this.users.clear();
	}
}
