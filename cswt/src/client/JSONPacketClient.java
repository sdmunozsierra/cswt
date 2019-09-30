package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import org.json.JSONObject;

import cswt.Ticket;
import cswt.User;
import json.JSONPacketReader;
import json.JSONPacketWriter;
import json.JSONReader;
import json.JSONWriter;

/**
 * Push encoded JSON data packets through a file byte stream.
 *
 *
 * @author Ryan Beckett, Sergio Sierra
 * @version 1.0
 * @since Dec 23, 2011
 */
public class JSONPacketClient {
	
    private static final String CREATE_TICKET = "Create ticket";
    private static final String OPEN_TICKET = "Open ticket";
    private static final String MARK_TICKET_AS_FIXED = "Mark ticket as fixed";
    private static final String CLOSE_TICKET = "Close ticket";
    private static final String REJECT_TICKET = "Reject ticket";
    private static final String ASSIGN_TICKET = "Assign ticket";
    private static final String SET_TICKET_SEVERITY = "Set ticket severity";
    private static final String SET_TICKET_PRIORITY = "Set ticket priority";
    private static final String UPDATE_TICKET_RESOLUTION = "Update ticket resolution";
    private static final String UPDATE_TIME_SPENT = "Update time spent";
    private static final String GET_ALL_TICKETS = "Get all tickets";
    private static final String SUCCESSFUL = "Successful";
    private static final String FAILED = "Failed";
    private static final String INVALID = "Invalid";
    private static final String COMPLETE = "Complete";
    private static final String CREATE_ACCOUNT = "Create account";
    private static final String VALIDATE_USER = "Validate user";
    private static final String UPDATE_PERMISSIONS = "Update permissions";
    private static final String DELETE_USER = "Delete User";
    private static final String GET_ALL_USERS = "Get all users.";
    private Socket socket = null;
    private DataOutputStream oos = null;
    private DataInputStream ois = null;
    private static final String ENCODING = "UTF-8";
    private static final int PORT = 9880;
    private JSONWriter wrtr = null;
    private JSONReader rdr = null;
    private ClientTicketManager ticketManager = null;
    private ClientUserManager userManager = null;
    
    public JSONPacketClient() {
    	try {
	        InetAddress host = InetAddress.getLocalHost();
	        socket = new Socket(host.getHostName(), PORT);
	        oos = new DataOutputStream(socket.getOutputStream());
	        wrtr = new JSONPacketWriter(oos, ENCODING);
	        ois = new DataInputStream(socket.getInputStream());
	        rdr = new JSONPacketReader(ois, ENCODING);
	        userManager = new ClientUserManager();
	        ticketManager = new ClientTicketManager();
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    }
	/** Sends a create ticket request to server.  
	 * @param title The title of the ticket to be added
	 * @param description The description of the ticket to be added
	 * @param client The client of the ticket to be added
	 * @param severity The severity of the ticket to be added
	 * @return A String that represents the result of the request
	 * */
    public synchronized String createTicket(String title, String description, String client, String severity) {
        String sendJson = "{\"request\": "+ CREATE_TICKET + ", \"title\": " + title + ", \"description\": " + description + ", \"client\": " + client + ", \"severity\": " + severity +"}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return SUCCESSFUL;
        }
        return FAILED;
    }
    
	/** Sends an open ticket request to server.  
	 * @param id The id of the ticket
	 * @param priority The priority of ticket
	 * @param assingedTo The assignee of the ticket
	 * @return A String that represents the result of the request
	 * */
    public synchronized String openTicket(String id, String priority, String assignedTo) {
        String sendJson = "{\"request\": "+ OPEN_TICKET + ", \"id\": " + id + ", \"priority\": " + priority + ", \"assignedTo\": " + assignedTo + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return SUCCESSFUL;
        }
        return FAILED;
    }
    
	/** Sends a close ticket request to server.  
	 * @param id The id of the ticket
	 * @return A String that represents the result of the request
	 * */
    public synchronized String closeTicket(String id) {
        String sendJson = "{\"request\": "+ CLOSE_TICKET + ", \"id\": " + id + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return SUCCESSFUL;
        }
        return FAILED;
    }
    
	/** Sends a fix ticket request to server.  
	 * @param id The id of the ticket
	 * @param resolution The resolution of ticket
	 * @param timeSpent The time spent on the ticket
	 * @return A String that represents the result of the request
	 * */
    public synchronized String markTicketAsFixed(String id, String resolution, String timeSpent) {
    	String sendJson = "{\"request\": "+ MARK_TICKET_AS_FIXED + ", \"id\": " + id + ", \"resolution\": " + resolution + ", \"timeSpent\": " + timeSpent + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return SUCCESSFUL;
        }
        return FAILED;
    }
    
	/** Sends a reject ticket request to server.  
	 * @param id The id of the ticket
	 * @return A String that represents the result of the request
	 * */
    public synchronized String rejectTicket(String id) {
        String sendJson = "{\"request\": "+ REJECT_TICKET + ", \"id\": " + id + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return SUCCESSFUL;
        }
        return FAILED;
    }
    
	/** Sends an assign request to server.  
	 * @param id The id of the ticket
	 * @param assignedTo The assignee of the ticket
	 * @return A String that represents the result of the request
	 * */
    public synchronized String assignTicket(String id, String assignedTo) {
    	String sendJson = "{\"request\": "+ ASSIGN_TICKET + ", \"id\": " + id + ", \"assignedTo\": " + assignedTo + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return SUCCESSFUL;
        }
        return FAILED;
    }
    
	/** Sends a set ticket severity request to server.  
	 * @param id The id of the ticket
	 * @param severity The severity of the ticket
	 * @return A String that represents the result of the request
	 * */
    public synchronized String setTicketSeverity(String id, String severity) {
    	String sendJson = "{\"request\": "+ SET_TICKET_SEVERITY + ", \"id\": " + id + ", \"severity\": " + severity + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return SUCCESSFUL;
        }
        return FAILED;
    }
    
	/** Sends a set ticket priority request to server.  
	 * @param id The id of the ticket
	 * @param priority The severity of the ticket
	 * @return A String that represents the result of the request
	 * */
    public synchronized String setTicketPriority(String id, String priority) {
    	String sendJson = "{\"request\": "+ SET_TICKET_PRIORITY + ", \"id\": " + id + ", \"priority\": " + priority + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return SUCCESSFUL;
        }
        return FAILED;
    }
    
	/** Sends a update ticket resolution request to server.  
	 * @param id The id of the ticket
	 * @param resolution The new resolution of the ticket
	 * @return A String that represents the result of the request
	 * */
    public synchronized String updateTicketResolution(String id, String resolution) {
    	String sendJson = "{\"request\": "+ UPDATE_TICKET_RESOLUTION + ", \"id\": " + id + ", \"resolution\": " + resolution + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return SUCCESSFUL;
        }
        return FAILED;
    }
    
	/** Sends an update time spent request to server  
	 * @param id The id of the ticket
	 * @param timeSpent The new time spent on the ticket
	 * @return A String that represents the result of the request
	 * */
    public synchronized String updateTimeSpent(String id, String timeSpent) {
    	String sendJson = "{\"request\": "+ UPDATE_TIME_SPENT + ", \"id\": " + id + ", \"timeSpent\": " + timeSpent + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return SUCCESSFUL;
        }
        return FAILED;
    }
    
	/** Sends a get all tickets request to server.  
	 * */
    public synchronized void updateAllTickets() {
    	ticketManager.clearManager();
    	String sendJson = "{\"request\": "+ GET_ALL_TICKETS + "}";
        wrtr.write(sendJson);
        while(true) {
        	String retrievedJSON = rdr.read();
        	JSONObject message = new JSONObject(retrievedJSON);
        	if (message.get("response").toString().equals(COMPLETE)) {
        		break;
        	}
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        }
    }
    
	/** Sends a create account request to server.
	 * @param username The username of the new user
	 * @param password The password of the new user
	 * @param type The type of the new user
	 * @param actualName The actual name of the new user
	 * @param email The email of the new user
	 * @return A String that represents the result of the request
	 * */
    public synchronized String createAccount(String username, String password, String type, String actualName, String email) {
    	if (userManager.hasUser(username)) {
    		return FAILED;
    	}
        String sendJson = "{\"request\": "+ CREATE_ACCOUNT + ", \"username\": " + username + ", \"password\": " + password + ", \"type\": " + type + ", \"actualName\": " + actualName + ", \"email\": " + email +"}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	userManager.addUser(userManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return SUCCESSFUL;
        }
        return FAILED;
    }
    
	/** Sends a validate user request to server.
	 * @param username The username of the user
	 * @param password The password of the user
	 * @return A String that represents the result of the request
	 * */
    public synchronized String validateUser(String username, String password) {
    	String sendJson = "{\"request\": "+ VALIDATE_USER + ", \"username\": " + username + ", \"password\": " + password + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	return SUCCESSFUL;
        }
        return FAILED;
    }
    
	/** Sends an update permissions request to server.
	 * @param username The username of the user
	 * @param newType The new permissions of the user
	 * @return A String that represents the result of the request
	 * */
    public synchronized String updatePermissions(String username, String newType) {
    	String sendJson = "{\"request\": "+ UPDATE_PERMISSIONS + ", \"username\": " + username + ", \"newType\": " + newType + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	userManager.removeUser(username);
        	userManager.addUser(userManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return SUCCESSFUL;
        }
        return FAILED;
    }
    
	/** Sends a delete user request to server.
	 * @param username The username of the user
	 * @return A String that represents the result of the request
	 * */
    public synchronized String deleteUser(String username) {
    	String sendJson = "{\"request\": "+ DELETE_USER + ", \"username\": " + username + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	userManager.removeUser(username);
        	return SUCCESSFUL;
        }
        return FAILED;
    }
    
	/** Sends a get all users request to server.  
	 * */
    public synchronized String updateAllUsers() {
    	userManager.clearManager();
    	String sendJson = "{\"request\": "+ GET_ALL_USERS + "}";
        wrtr.write(sendJson);
        while(true) {
        	String retrievedJSON = rdr.read();
        	JSONObject message = new JSONObject(retrievedJSON);
        	if (message.get("response").toString().equals(COMPLETE)) {
        		break;
        	}
        	userManager.addUser(userManager.fromJSON(new JSONObject(message.get("result").toString())));
        }
        return SUCCESSFUL;
    }    
    
	/** Gets all tickets.
	 * @return A List of all tickets
	 * */
    public synchronized List<Ticket> getAllTickets() {
    	return ticketManager.getAllTickets();
    }
    
	/** Gets a ticket from the local ticket manager
	 * @param id The id of the ticket
	 * @return The ticket or null if no ticket with that id exists
	 * */
    public synchronized Ticket getTicket(String id) {
    	return ticketManager.getTicket(id);
    }
    
	/** Gets all users.
	 * @return A List of all users
	 * */
    public synchronized List<User> getAllTicket() {
    	return userManager.getAllUsers();
    }
    
	/** Gets a user from the local user manager.
	 * @param username The username of the user
	 * @return The user or null if no user with that username exists
	 * */
    public synchronized User getUser(String username) {
    	return userManager.getUser(username);
    }
    
    /** Closes client readers and writers
	 * */
    public synchronized void closeClient() {
        wrtr.close();
        rdr.close();
    }
}
