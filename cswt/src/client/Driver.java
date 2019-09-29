package client;

import cswt.Ticket;

public class Driver {
    public static void main(String [] args) {
    	JSONPacketClient client = new JSONPacketClient();
    	client.createTicket("title", "description", "client", "severity");
    	for (Ticket ticket: client.ticketManager.getAllTickets()) {
    		System.out.println(ticket.getDescription());
    	}
    
    }
}
