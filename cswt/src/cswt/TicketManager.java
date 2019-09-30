package cswt;

import java.io.File;
import java.util.Date;
import java.sql.Timestamp;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONTokener;

public class TicketManager {
	
	private List<Ticket> tickets;
	private List<String> ids;
	private FileWriter writer;
	// Status constants
	private static final String STATUS_NEW = "NEW";
	private static final String STATUS_OPENED = "OPEN";
	private static final String STATUS_CLOSED = "CLOSED";
	private static final String STATUS_REJECTED = "REJECTED";
	private static final String STATUS_FIXED = "FIXED";
	public static final String TICKET_DIR = Paths.get(System.getProperty("user.dir"), "tickets").toString();
	
	
	public TicketManager() {
		this.tickets = new ArrayList<Ticket>();
		this.ids = new ArrayList<String>();
		loadTickets();
	}
	
	/** Creates a new ticket and stores it. 
	 * @param title The title of the ticket to be added
	 * @param description The description of the ticket to be added
	 * @param client The client of the ticket to be added
	 * @param severity The severity of the ticket to be added
	 * @return The new ticket or null if the system was unable to store the ticket
	 * */
	public synchronized Ticket createTicket(String title, String description, String client, String severity) {
		Ticket ticket = new Ticket();
		ticket.setTitle(title);
		ticket.setDescription(description);
		ticket.setClient(client);
		ticket.setSeverity(severity);
		ticket.setStatus(STATUS_NEW);
		Date date = new Date();
		ticket.setId(Long.toString(date.getTime()));
		boolean added = addTicket(ticket);
		if (added) {
			return ticket;
		}
		return null;
	}
	
	/** Adds an existing ticket to the ticket manager 
	 * @param ticket The ticket to be added
	 * @return The ticket or null if the system was unable to store the ticket
	 * */
	public synchronized Ticket addExistingTicket(Ticket ticket) {
		boolean added = addTicket(ticket);
		if (added) {
			return ticket;
		}
		return null;
	}
	
	/** Marks a ticket as open. 
	 * @param id The id of the ticket
	 * @param priority The priority of the ticket
	 * @param assignedTo The user the ticket will be assigned to
	 * @return The updated ticket or null if the system was unable to store the ticket
	 * */
	public synchronized Ticket openTicket(String id, String priority, String assignedTo) {
		int index = ids.indexOf(id);
		Ticket ticket = tickets.get(index);
		ticket.setPriority(priority);
		ticket.setStatus(STATUS_OPENED);
		ticket.setAssignedTo(assignedTo);
		Timestamp ts = new Timestamp((new Date()).getTime());
		ticket.setOpenedDate(ts.toString());
		boolean updated = updateTicket(ticket);
		if (updated) {
			return ticket;
		}
		return null;
	}
	
	/** Marks ticket as fixed and updates resolution. 
	 * @param id The id of the ticket
	 * @param resolution The resolution of the ticket
	 * @param timeSpent The time spent working on the ticket of the ticket
	 * @return The updated ticket or null if the system was unable to store the ticket
	 * */
	public synchronized Ticket markTicketAsFixed(String id, String resolution, String timeSpent) {
		int index = ids.indexOf(id);
		Ticket ticket = tickets.get(index);
		ticket.setStatus(STATUS_FIXED);
		ticket.setResolution(resolution);
		ticket.setTimeSpent(timeSpent);
		boolean updated = updateTicket(ticket);
		if (updated) {
			return ticket;
		}
		return null;
	}
	
	/** Marks a ticket as closed. 
	 * @param id The id of the ticket
	 * @return The updated ticket or null if the system was unable to store the ticket
	 * */
	public synchronized Ticket closeTicket(String id) {
		int index = ids.indexOf(id);
		Ticket ticket = tickets.get(index);
		// Ticket must be marked fixed before it can be closed
		if (!ticket.getStatus().equals(STATUS_FIXED)) {
			return null;
		}
		ticket.setStatus(STATUS_CLOSED);
		Timestamp ts = new Timestamp((new Date()).getTime());
		ticket.setClosedDate(ts.toString());
		boolean updated = updateTicket(ticket);
		if (updated) {
			return ticket;
		}
		return null;
	}
	
	/** Marks a ticket as rejected. 
	 * @param id The id of the ticket
	 * @return The updated ticket or null if the system was unable to store the ticket
	 * */
	public synchronized Ticket rejectTicket(String id) {
		int index = ids.indexOf(id);
		Ticket ticket = tickets.get(index);
		ticket.setStatus(STATUS_REJECTED);
		boolean updated = updateTicket(ticket);
		if (updated) {
			return ticket;
		}
		return null;
	}
	
	/** Assigns a ticket. 
	 * @param id The id of the ticket
	 * @param assignedTo The user the ticket will be assigned to
	 * @return The updated ticket or null if the system was unable to store the ticket
	 * */
	public synchronized Ticket assignTicket(String id, String assignedTo) {
		int index = ids.indexOf(id);
		Ticket ticket = tickets.get(index);
		ticket.setAssignedTo(assignedTo);
		boolean updated = updateTicket(ticket);
		if (updated) {
			return ticket;
		}
		return null;
	}
	
	/** Sets a tickets severity. 
	 * @param id The id of the ticket
	 * @param severity The new severity of the ticket
	 * @return The updated ticket or null if the system was unable to store the ticket
	 * */
	public synchronized Ticket setTicketSeverity(String id, String severity) {
		int index = ids.indexOf(id);
		Ticket ticket = tickets.get(index);
		ticket.setSeverity(severity);
		boolean updated = updateTicket(ticket);
		if (updated) {
			return ticket;
		}
		return null;
	}
	
	/** Sets a tickets priority. 
	 * @param id The id of the ticket
	 * @param severity The new severity of the ticket
	 * @return The updated ticket or null if the system was unable to store the ticket
	 * */
	public synchronized Ticket setTicketPriority(String id, String priority) {
		int index = ids.indexOf(id);
		Ticket ticket = tickets.get(index);
		ticket.setPriority(priority);
		boolean updated = updateTicket(ticket);
		if (updated) {
			return ticket;
		}
		return null;
	}
	
	/** Updates a tickets resolution. 
	 * @param id The id of the ticket
	 * @param resolution The new resolution of the ticket
	 * @return The updated ticket or null if the system was unable to store the ticket
	 * */
	public synchronized Ticket updateTicketResolution(String id, String resolution) {
		int index = ids.indexOf(id);
		Ticket ticket = tickets.get(index);
		ticket.setPriority(resolution);
		boolean updated = updateTicket(ticket);
		if (updated) {
			return ticket;
		}
		return null;
	}
	
	/** Updates a tickets time spent. 
	 * @param id The id of the ticket
	 * @param timeSpent The time spent working on the ticket
	 * @return The updated ticket or null if the system was unable to store the ticket
	 * */
	public synchronized Ticket updateTicketTimeSpent(String id, String timeSpent) {
		int index = ids.indexOf(id);
		Ticket ticket = tickets.get(index);
		ticket.setTimeSpent(timeSpent);
		boolean updated = updateTicket(ticket);
		if (updated) {
			return ticket;
		}
		return null;
	}
	
	/** Adds a ticket to the ticket manager. 
	 * @param ticket The ticket to be added
	 * @return If the manager was able to add the ticket
	 * */
	private synchronized boolean addTicket(Ticket ticket) {
		try{
			writeTicketToFile(ticket);
			this.tickets.add(ticket);
			this.ids.add(ticket.getId());
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	    
	}
	
	/** Updates a ticket's file. 
	 * @param item The ticket to be updated
	 * @return If the manager was able to update the ticket
	 * */
	private synchronized boolean updateTicket(Ticket ticket) {
		try{
			writeTicketToFile(ticket);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/** Loads in tickets to manager from ticket files. 
	 * */
	private synchronized void loadTickets() {
		File folder = new File(TICKET_DIR);
		if(folder.listFiles() != null) {
			for(File file : folder.listFiles()) {
				if((file.getName()).contains(".json") && readTicketFromFile(file) != null) {
					Ticket ticket = readTicketFromFile(file);
					this.tickets.add(ticket);
					this.ids.add(ticket.getId());
				}
			}
		}
	}
	
	/** Reads a ticket from a ticket file. 
	 * @param file The file to be read
	 * @return The ticket read form the file or null if there was an error
	 * */
	private synchronized Ticket readTicketFromFile(File file) {
		 try {
			 JSONTokener parser = new JSONTokener(new FileReader(file));
			 JSONObject obj = (JSONObject) parser.nextValue();
			 JSONObject itemJSON = (JSONObject) obj;
			 Ticket ticket = fromJSON(itemJSON);
			 return ticket;
		 }
		 catch (Exception e) {
			 e.printStackTrace();
			 return null;
		 }
	}
	
	/** Writes a ticket to a file.
	 * @param ticket The ticket that will be written to a file
	 * @throws IOExcpetion
	 * */
	private synchronized void writeTicketToFile(Ticket ticket) throws IOException {
		String filename = Paths.get(TICKET_DIR, ticket.getId().toString() + ".json").toString();
		File file = new File(filename);
		file.createNewFile();
		writer = new FileWriter(filename);
		writer.write((ticket.toJSON()).toString());
		writer.close();
	}
	
	/** Parses an item from a JSONObject 
	 * @param obj The JSONObject to be parsed
	 * */
	public synchronized Ticket fromJSON(JSONObject obj) {
		try {
		    Ticket ticket = new Ticket();
		    ticket.setTitle(obj.getString("title"));
		    ticket.setDescription(obj.getString("description"));
		    ticket.setAssignedTo(obj.getString("assignedTo"));
		    ticket.setClient(obj.getString("client"));
		    ticket.setClosedDate(obj.getString("closedDate"));
		    ticket.setOpenedDate(obj.getString("openedDate"));
		    ticket.setPriority(obj.getString("priority"));
		    ticket.setStatus(obj.getString("status"));
		    ticket.setResolution(obj.getString("resolution"));
		    ticket.setSeverity(obj.getString("severity"));
		    ticket.setId(obj.getString("id"));
		    ticket.setTimeSpent(obj.getString("timeSpent"));
		    return ticket;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/** Gets a ticket based on provided ticket id
	 * @param id The provided id
	 * @return The ticket or null if no ticket with that username exists
	 * */
	public synchronized Ticket getTicket(String id) {
		int index = this.ids.indexOf(id);
		if (index == -1) {
			return null;
		}
		return this.tickets.get(index);
	}
	
	/** Gets all tickets
	 * @return The list of all tickets
	 * */
	public synchronized List<Ticket> getAllTickets() {
		return this.tickets;
	}
	
	/** Clear manager fields
	 * */
	public synchronized void clearManager() {
		this.tickets.clear();
		this.ids.clear();
	}
}
