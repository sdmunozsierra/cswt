package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import org.json.JSONObject;

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
    private JSONWriter wrtr = null;
    private JSONReader rdr = null;
    public ClientTicketManager ticketManager = null;
    public ClientUserManager userManager = null;
    
    public JSONPacketClient() {
    	try {
	        InetAddress host = InetAddress.getLocalHost();
	        socket = new Socket(host.getHostName(), 9880);
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

    public synchronized boolean createTicket(String title, String description, String client, String severity) {
        String sendJson = "{\"request\": "+ CREATE_TICKET + ", \"title\": " + title + ", \"description\": " + description + ", \"client\": " + client + ", \"severity\": " + severity +"}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return true;
        }
        return false;
    }
    
    public synchronized boolean openTicket(String id, String priority, String assignedTo) {
        String sendJson = "{\"request\": "+ OPEN_TICKET + ", \"id\": " + id + ", \"priority\": " + priority + ", \"assignedTo\": " + assignedTo + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return true;
        }
        return false;
    }
    
    public synchronized boolean closeTicket(String id) {
        String sendJson = "{\"request\": "+ CLOSE_TICKET + ", \"id\": " + id + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return true;
        }
        return false;
    }
    
    public synchronized boolean markTicketAsFixed(String id, String resolution, String timeSpent) {
    	String sendJson = "{\"request\": "+ MARK_TICKET_AS_FIXED + ", \"id\": " + id + ", \"resolution\": " + resolution + ", \"timeSpent\": " + timeSpent + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return true;
        }
        return false;
    }
    
    public synchronized boolean rejectTicket(String id) {
        String sendJson = "{\"request\": "+ REJECT_TICKET + ", \"id\": " + id + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return true;
        }
        return false;
    }
    
    public synchronized boolean assignTicket(String id, String assignedTo) {
    	String sendJson = "{\"request\": "+ ASSIGN_TICKET + ", \"id\": " + id + ", \"assignedTo\": " + assignedTo + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return true;
        }
        return false;
    }
    
    public synchronized boolean setTicketSeverity(String id, String severity) {
    	String sendJson = "{\"request\": "+ SET_TICKET_SEVERITY + ", \"id\": " + id + ", \"severity\": " + severity + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return true;
        }
        return false;
    }
    
    public synchronized boolean setTicketPriority(String id, String priority) {
    	String sendJson = "{\"request\": "+ SET_TICKET_PRIORITY + ", \"id\": " + id + ", \"priority\": " + priority + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return true;
        }
        return false;
    }
    
    public synchronized boolean updateTicketResolution(String id, String resolution) {
    	String sendJson = "{\"request\": "+ UPDATE_TICKET_RESOLUTION + ", \"id\": " + id + ", \"resolution\": " + resolution + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return true;
        }
        return false;
    }
    
    public synchronized boolean updateTimeSpent(String id, String timeSpent) {
    	String sendJson = "{\"request\": "+ UPDATE_TIME_SPENT + ", \"id\": " + id + ", \"timeSpent\": " + timeSpent + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	ticketManager.removeTicket(id);
        	ticketManager.addTicket(ticketManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return true;
        }
        return false;
    }
    
    public synchronized boolean getAllTickets(String id, String timeSpent) {
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
        return true;
    }
    
    public synchronized boolean createAccount(String username, String password, String type, String actualName, String email) {
    	if (userManager.hasUser(username)) {
    		return false;
    	}
        String sendJson = "{\"request\": "+ CREATE_ACCOUNT + ", \"username\": " + username + ", \"password\": " + password + ", \"type\": " + type + ", \"actualName\": " + actualName + ", \"email\": " + email +"}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	userManager.addUser(userManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return true;
        }
        return false;
    }
    
    public synchronized boolean validateUser(String username, String password) {
    	String sendJson = "{\"request\": "+ VALIDATE_USER + ", \"username\": " + username + ", \"password\": " + password + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	return true;
        }
        return false;
    }
    
    public synchronized boolean updatePermissions(String username, String newType) {
    	String sendJson = "{\"request\": "+ UPDATE_PERMISSIONS + ", \"username\": " + username + ", \"newType\": " + newType + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	userManager.removeUser(username);
        	userManager.addUser(userManager.fromJSON(new JSONObject(message.get("result").toString())));
        	return true;
        }
        return false;
    }
    
    public synchronized boolean deleteUser(String username) {
    	String sendJson = "{\"request\": "+ DELETE_USER + ", \"username\": " + username + "}";
        wrtr.write(sendJson);
        String retrievedJSON = rdr.read();
        JSONObject message = new JSONObject(retrievedJSON);
        if (message.get("response").toString().equals(SUCCESSFUL)) {
        	userManager.removeUser(username);
        	return true;
        }
        return false;
    }
    
    public synchronized boolean getAllUsers(String id, String timeSpent) {
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
        return true;
    }    
    public synchronized void closeClient() {
        wrtr.close();
        rdr.close();
    }
}
