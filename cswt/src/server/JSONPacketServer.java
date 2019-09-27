package server;

import json.JSONPacketReader;
import json.JSONPacketWriter;
import json.JSONReader;
import json.JSONWriter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class JSONPacketServer {
    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 9880;

    public static void main(String args[]) throws IOException, ClassNotFoundException{
        String encd = "UTF-8";

        boolean flag = false;

        //create the socket server object
        server = new ServerSocket(port);
        //keep listens indefinitely until receives 'exit' call or program terminates
        while(true){
            try {
                System.out.println("Waiting for client request");
                //creating socket and waiting for client connection
                Socket socket = server.accept();
                //read from socket to ObjectInputStream object
                DataInputStream ois = new DataInputStream(socket.getInputStream());

                //convert InputStream object to String
                JSONReader rdr = new JSONPacketReader(ois, encd);
                String retrievedJSON = rdr.read();
                System.out.println("Retrieved XML: " + retrievedJSON);

                //create OutputStream object
                DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
                //write object to Socket
                JSONWriter wrtr = new JSONPacketWriter(oos, encd);

                JSONObject message = null;

                if (retrievedJSON != null){
                    message = new JSONObject(retrievedJSON);

                    String id = message.get("id")+"";
                    String sendJson = "{\"id\":" + id + ", \"result\": {\"uid\": \"#####\"}}";
                    wrtr.write(sendJson);
                }

                JSONObject message2 = new JSONObject(retrievedJSON);
                String method = (String)message2.get("method");
                if (method.equals("logout"))
                    flag = true;
                if(flag) break;

                //close resources
                rdr.close();
                wrtr.close();
                socket.close();
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        System.out.println("Shutting down Socket server!!");
        //close the ServerSocket object
        server.close();
    }

}

