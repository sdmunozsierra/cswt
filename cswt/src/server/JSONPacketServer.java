package server;

import json.JSONPacketReader;
import json.JSONPacketWriter;
import json.JSONReader;
import json.JSONWriter;
import org.json.JSONException;
import org.json.JSONObject;

import cswt.Ticket;
import cswt.TicketManager;
import cswt.User;
import cswt.UserManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class JSONPacketServer {
    //static ServerSocket variable
    private static ServerSocket server;
    
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
    private static final String COMPLETE = "Complete";
    private static final String CREATE_ACCOUNT = "Create account";
    private static final String VALIDATE_USER = "Validate user";
    private static final String UPDATE_PERMISSIONS = "Update permissions";
    private static final String DELETE_USER = "Delete User";
    private static final String GET_ALL_USERS = "Get all users.";
    
    
    //socket server port on which it will listen
    private static int port = 9880;

    public static void main(String args[]) throws IOException, ClassNotFoundException{
        String encd = "UTF-8";
        TicketManager ticketManager = new TicketManager();
        UserManager userManager = new UserManager();
        File ticketDirectory = new File(TicketManager.TICKET_DIR);
        File userDirectory = new File(UserManager.USER_DIR);
        if (!ticketDirectory.exists()) {
        	ticketDirectory.mkdir();
        }
        if (!userDirectory.exists()) {
        	userDirectory.mkdir();
        }
        //create the socket server object
        server = new ServerSocket(port);
        //creating socket and waiting for client connection
        Socket socket = server.accept();
        //read from socket to ObjectInputStream object
        DataInputStream ois = new DataInputStream(socket.getInputStream());

        //convert InputStream object to String
        JSONReader rdr = new JSONPacketReader(ois, encd);
        //create OutputStream object
        DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
        //write object to Socket
        JSONWriter wrtr = new JSONPacketWriter(oos, encd);
        //keep listens indefinitely until receives 'exit' call or program terminates
        try {
            String retrievedJSON = rdr.read();
            System.out.println("Retrieved JSON: " + retrievedJSON);
            JSONObject message = null;
            if (retrievedJSON != null){
                message = new JSONObject(retrievedJSON);
                String method = (String) message.get("request");
                if (method.equals(CREATE_TICKET)) {
                	String title = (String) message.get("title");
                	String description = (String) message.get("description");
                	String client = (String) message.get("client");
                	String severity =  (String) message.get("severity");
                	Ticket ticket = ticketManager.createTicket(title, description, client, severity);
                	if (ticket != null) {
                    	String ticketString = ticket.toJSON().toString();
                    	String sendJson = "{\"response\":" + SUCCESSFUL + ", \"result\": " + ticketString +"}";
                    	wrtr.write(sendJson);
                	}
                	else {
                    	String sendJson = "{\"response\":" + FAILED + "}";
                    	wrtr.write(sendJson);
                	}
                }
                else if (method.equals(OPEN_TICKET)) {
                	String id = (String) message.get("id");
                	String priority =  ((Integer) message.get("priority")).toString();
                	String assignedTo = (String) message.get("assignedTo");
                	Ticket ticket = ticketManager.openTicket(id, priority, assignedTo);
                	if (ticket != null) {
                    	String ticketString = ticket.toJSON().toString();
                    	String sendJson = "{\"response\":" + SUCCESSFUL + ", \"result\": " + ticketString +"}";
                    	wrtr.write(sendJson);
                	}
                	else {
                    	String sendJson = "{\"response\":" + FAILED + "}";
                    	wrtr.write(sendJson);
                	}
                }
                else if (method.equals(MARK_TICKET_AS_FIXED)) {
                	String id = (String) message.get("id");
                	String resolution = (String) message.get("resolution");
                	String timeSpent = (String) message.get("timeSpent");
                	Ticket ticket = ticketManager.markTicketAsFixed(id, resolution, timeSpent);
                	if (ticket != null) {
                    	String ticketString = ticket.toJSON().toString();
                    	String sendJson = "{\"response\":" + SUCCESSFUL + ", \"result\": " + ticketString +"}";
                    	wrtr.write(sendJson);
                	}
                	else {
                    	String sendJson = "{\"response\":" + FAILED + "}";
                    	wrtr.write(sendJson);
                	}
                }
                else if (method.equals(CLOSE_TICKET)) {
                	String id = (String) message.get("id");
                	Ticket ticket = ticketManager.closeTicket(id);
                	if (ticket != null) {
                    	String ticketString = ticket.toJSON().toString();
                    	String sendJson = "{\"response\":" + SUCCESSFUL + ", \"result\": " + ticketString +"}";
                    	wrtr.write(sendJson);
                	}
                	else {
                    	String sendJson = "{\"response\":" + FAILED + "}";
                    	wrtr.write(sendJson);
                	}
                }
                else if (method.equals(REJECT_TICKET)) {
                	String id = (String) message.get("id");
                	Ticket ticket = ticketManager.rejectTicket(id);
                	if (ticket != null) {
                    	String ticketString = ticket.toJSON().toString();
                    	String sendJson = "{\"response\":" + SUCCESSFUL + ", \"result\": " + ticketString +"}";
                    	wrtr.write(sendJson);
                	}
                	else {
                    	String sendJson = "{\"response\":" + FAILED + "}";
                    	wrtr.write(sendJson);
                	}
                }
                else if (method.equals(ASSIGN_TICKET)) {
                	String id = (String) message.get("id");
                	String assignedTo = (String) message.get("assignedTo");
                	Ticket ticket = ticketManager.assignTicket(id, assignedTo);
                	if (ticket != null) {
                    	String ticketString = ticket.toJSON().toString();
                    	String sendJson = "{\"response\":" + SUCCESSFUL + ", \"result\": " + ticketString +"}";
                    	wrtr.write(sendJson);
                	}
                	else {
                    	String sendJson = "{\"response\":" + FAILED + "}";
                    	wrtr.write(sendJson);
                	}
                }
                else if (method.equals(SET_TICKET_SEVERITY)) {
                	String id = (String) message.get("id");
                	String severity = (String) message.get("severity");
                	Ticket ticket = ticketManager.setTicketSeverity(id, severity);
                	if (ticket != null) {
                    	String ticketString = ticket.toJSON().toString();
                    	String sendJson = "{\"response\":" + SUCCESSFUL + ", \"result\": " + ticketString +"}";
                    	wrtr.write(sendJson);
                	}
                	else {
                    	String sendJson = "{\"response\":" + FAILED + "}";
                    	wrtr.write(sendJson);
                	}
                }
                else if (method.equals(SET_TICKET_PRIORITY)) {
                	String id = (String) message.get("id");
                	String priority = ((Integer) message.get("priority")).toString();
                	Ticket ticket = ticketManager.setTicketPriority(id, priority);
                	if (ticket != null) {
                    	String ticketString = ticket.toJSON().toString();
                    	String sendJson = "{\"response\":" + SUCCESSFUL + ", \"result\": " + ticketString +"}";
                    	wrtr.write(sendJson);
                	}
                	else {
                    	String sendJson = "{\"response\":" + FAILED + "}";
                    	wrtr.write(sendJson);
                	}
                }
                else if (method.equals(UPDATE_TICKET_RESOLUTION)) {
                	String id = (String) message.get("id");
                	String resolution = (String) message.get("resolution");
                	Ticket ticket = ticketManager.updateTicketResolution(id, resolution);
                	if (ticket != null) {
                    	String ticketString = ticket.toJSON().toString();
                    	String sendJson = "{\"response\":" + SUCCESSFUL + ", \"result\": " + ticketString +"}";
                    	wrtr.write(sendJson);
                	}
                	else {
                    	String sendJson = "{\"response\":" + FAILED + "}";
                    	wrtr.write(sendJson);
                	}
                }
                else if (method.equals(UPDATE_TIME_SPENT)) {
                	String id = (String) message.get("id");
                	String timeSpent = (String) message.get("timeSpent");
                	Ticket ticket = ticketManager.updateTicketTimeSpent(id, timeSpent);
                	if (ticket != null) {
                    	String ticketString = ticket.toJSON().toString();
                    	String sendJson = "{\"response\":" + SUCCESSFUL + ", \"result\": " + ticketString +"}";
                    	wrtr.write(sendJson);
                	}
                	else {
                    	String sendJson = "{\"response\":" + FAILED + "}";
                    	wrtr.write(sendJson);
                	}
                }
                else if (method.equals(GET_ALL_TICKETS)) {
                	for (Ticket ticket: ticketManager.getAllTickets()) {
                        String ticketString = ticket.toJSON().toString();
                        String sendJson = "{\"response\":" + SUCCESSFUL + ", \"result\": " + ticketString +"}";
                        wrtr.write(sendJson);
                	}
                	wrtr.write("{\"response\":" + COMPLETE + "}");
                }
                else if (method.equals(CREATE_ACCOUNT)) {
                	String username = (String) message.get("username");
                	if (userManager.hasUser(username)) {
                		String sendJson = "{\"response\":" + FAILED + "}";
                    	wrtr.write(sendJson);
                	}
                	String password = (String) message.get("password");
                	String type = (String) message.get("type");
                	String actualName = (String) message.get("actualName");
                	String email = (String) message.get("email");
                	User user = userManager.createAccount(username, password, type, actualName, email);
                	if (user != null) {
                    	String userString = user.toJSON().toString();
                    	String sendJson = "{\"response\":" + SUCCESSFUL + ", \"result\": " + userString +"}";
                    	wrtr.write(sendJson);
                	}
                	else {
                    	String sendJson = "{\"response\":" + FAILED + "}";
                    	wrtr.write(sendJson);
                	}
                }
                else if (method.equals(VALIDATE_USER)) {
                	String username = (String) message.get("username");
                	String password = (String) message.get("password");
                	boolean valid = userManager.validateUser(username, password);
                	if (valid) {
                    	String sendJson = "{\"response\":" + SUCCESSFUL+ "}";
                    	wrtr.write(sendJson);
                	}
                	else {
                    	String sendJson = "{\"response\":" + FAILED + "}";
                    	wrtr.write(sendJson);
                	}
                }
                else if (method.equals(UPDATE_PERMISSIONS)) {
                	String username = (String) message.get("username");
                	String newType = (String) message.get("newType");
                	User user = userManager.updateUserPermissions(username, newType);
                	if (user != null) {
                    	String userString = user.toJSON().toString();
                    	String sendJson = "{\"response\":" + SUCCESSFUL + ", \"result\": " + userString +"}";
                    	wrtr.write(sendJson);
                	}
                	else {
                    	String sendJson = "{\"response\":" + FAILED + "}";
                    	wrtr.write(sendJson);
                	}
                }
                else if (method.equals(DELETE_USER)) {
                	String username = (String) message.get("username");
                	userManager.deleteUser(username);
                    String sendJson = "{\"response\":" + SUCCESSFUL + "}";
                    wrtr.write(sendJson);
                }
                else if (method.equals(GET_ALL_USERS)) {
                	for (User user: userManager.getAllUsers()) {
                        String userString = user.toJSON().toString();
                        String sendJson = "{\"response\":" + SUCCESSFUL + ", \"result\": " + userString +"}";
                        wrtr.write(sendJson);
                	}
                	wrtr.write("{\"response\":" + COMPLETE + "}");
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

}

