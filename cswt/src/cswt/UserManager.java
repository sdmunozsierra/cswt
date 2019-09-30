package cswt;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONTokener;

public class UserManager {
	
	private List<User> users;
	private List<String> usernames;
	private FileWriter writer;
	public static final String USER_DIR = Paths.get(System.getProperty("user.dir"), "users").toString();
	
	public UserManager() {
		this.users = new ArrayList<User>();
		this.usernames = new ArrayList<String>();
		loadUsers();
	}
	
	/** Creates an account for a new user. 
	 * @param username The username of the user to be added
	 * @param password The password of the user to be added
	 * @param type The type of the user to be added
	 * @param actualName The actual name of the user to be added
	 * @param email The email of the user to be added
	 * @return The new user or null if the system was unable to store the user
	 * */
	public synchronized User createAccount(String username, String password, String type, String actualName, String email) {
		User user = new User();
		user.setActualName(actualName);
		user.setEmail(email);
		user.setPassword(password);
		user.setType(type);
		user.setUsername(username);
		boolean added = addUser(user);
		if (added) {
			return user;
		}
		return null;
	}
	
	/** Adds an existing user to the user manager 
	 * @param user The user to be added
	 * @return The user or null if the system was unable to store the user
	 * */
	public synchronized User addExistingUser(User user) {
		boolean added = addUser(user);
		if (added) {
			return user;
		}
		return null;
	}
	
	/** Checks if a user has entered the correct username and password. 
	 * @param username The provided username
	 * @param password The provided password
	 * @return If the username and password are valid
	 * * */
	public synchronized boolean validateUser(String username, String password) {
		int index = this.usernames.indexOf(username);
		if (index == -1) {
			return false;
		}
		User user = this.users.get(index);
		return user.getPassword().equals(password);
	}
	
	/** Updates an existing users permissions.
	 * @param username The username of the user to be updated
	 * @param type The new type of the user to be updated
	 * @return The new user or null if the system was unable to store the user
	 * */
	public synchronized User updateUserPermissions(String username, String newType) {
		int index = this.usernames.indexOf(username);
		User user = this.users.get(index);
		user.setType(newType);
		boolean updated = updateUser(user);
		if (updated) {
			return user;
		}
		return null;
	}

	/** Adds a user to the manager. 
	 * @param user The user to be added
	 * @return If the manager was able to add the user
	 * */
	private boolean addUser(User user) {
		try{
			writeUserToFile(user);
			this.users.add(user);
			this.usernames.add(user.getUsername());
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	/** Removes a user from the manager and storage. 
	 * @param username The username of the user to be removed
	 * */
	public synchronized void deleteUser(String username) {
		int index = this.usernames.indexOf(username);
		this.usernames.remove(index);
		this.users.remove(index);
		File file = new File(Paths.get(USER_DIR, username + ".json").toString());
		file.delete();
	}
	
	/** Updates an user in the  manager. 
	 * @param user The user to be updated
	 * @return If the manager was able to update the user
	 * */
	private synchronized boolean updateUser(User user) {
		try{
			writeUserToFile(user);
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	/** Loads in users to manager from user files. 
	 * */
	private synchronized void loadUsers() {
		File folder = new File(USER_DIR);
		if(folder.listFiles() != null) {
			for(File file : folder.listFiles()) {
				if((file.getName()).contains(".json") && readUserFromFile(file) != null) {
					User user = readUserFromFile(file);
					this.users.add(user);
					this.usernames.add(user.getUsername());
				}
			}
		}
	}
	
	/** Reads a user from a ticket file. 
	 * @param file The file to be read
	 * @return The user read form the file or null if there was an error
	 * */
	private synchronized User readUserFromFile(File file) {
		 try {
			 JSONTokener parser = new JSONTokener(new FileReader(file));
			 JSONObject obj = (JSONObject) parser.nextValue();
			 JSONObject itemJSON = (JSONObject) obj;
			 User user = fromJSON(itemJSON);
			 return user;
		 }
		 catch (Exception e) {
			 return null;
		 }
	}
	
	/** Writes an user to an user file.
	 * @param user The user that will be written to a file
	 * @throws IOExcpetion
	 * */
	private synchronized void writeUserToFile(User user) throws IOException {
		String filename = Paths.get(USER_DIR, user.getUsername().toString() + ".json").toString();
		File file = new File(filename);
		file.createNewFile();
		writer = new FileWriter(filename);
		writer.write((user.toJSON()).toString());
		writer.close();
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
