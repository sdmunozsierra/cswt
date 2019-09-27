package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import json.JSONPacketReader;
import json.JSONPacketWriter;
import json.JSONReader;
import json.JSONWriter;

import cswt.TicketManager;

/**
 * Push encoded JSON data packets through a file byte stream.
 *
 *
 * @author Ryan Beckett, Sergio Sierra
 * @version 1.0
 * @since Dec 23, 2011
 */
public class JSONPacketClient {

    /** Sends a ticket through sockets.
     * */
    public static void main(String[] args) throws Exception {
        // Create TicketManager
        TicketManager tManager = new TicketManager();

        // Create Test Ticket

        // Add test Ticket
        tManager.createTicket("TestTicket", "test0", "client-test", "low");

        String encd = "UTF-8";  // Set encoding
        String sendJson2 = "{\"id\": 1, \"method\": \"logout\"}}";

        // write JSON to file
//        JSONWriter wrtr = new JSONPacketWriter(new FileOutputStream(file, false), encd);

        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        DataOutputStream oos = null;
        DataInputStream ois = null;

        for (int i = 0; i < 5; i++) {
            socket = new Socket(host.getHostName(), 9880);
            oos = new DataOutputStream(socket.getOutputStream());

            JSONWriter wrtr = new JSONPacketWriter(oos, encd);
            String sendJson = "{\"id\": "+i+", \"method\": \"login\", \"params\": {\"type\": \"token\", \"token\": \"XXXXXXXXXXXXXXXXXXXXXXXXXXXX\"}}";
            if(i==4)wrtr.write(sendJson2);
            else wrtr.write(sendJson);
            System.out.println("Successfully send JSON data to + " + host.getHostName() + ":");
            System.out.println(sendJson);

            ois = new DataInputStream(socket.getInputStream());
            // read JSON from file
//            JSONReader rdr = new JSONPacketReader(new FileInputStream(file), encd);
            JSONReader rdr = new JSONPacketReader(ois, encd);
            String retrievedJSON = rdr.read();
            System.out.println("Retrieved XML: " + retrievedJSON);

            wrtr.close();
            rdr.close();
            Thread.sleep(100);
        }


    }
}
