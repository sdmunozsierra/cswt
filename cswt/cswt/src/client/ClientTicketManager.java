package client;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import cswt.Ticket;

public class ClientTicketManager {
	
	private List<Ticket> tickets;
	private List<String> ids;
	
	public ClientTicketManager() {
		this.tickets = new ArrayList<Ticket>();
		this.ids = new ArrayList<String>();
	}

	
	/** Adds a ticket to the ticket manager. 
	 * @param ticket The ticket to be added
	 * */
	public synchronized void addTicket(Ticket ticket) {
		this.tickets.add(ticket);
		this.ids.add(ticket.getId());
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
	
	/** Removes a ticket from the ticket manager and storage. 
	 * @param username The id of the ticket to be removed
	 * */
	public synchronized void removeTicket(String id) {
		int index = this.ids.indexOf(id);
		if (index == -1) {
			return;
		}
		this.tickets.remove(index);
		this.ids.remove(index);
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

