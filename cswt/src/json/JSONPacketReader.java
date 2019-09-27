package json;

import java.io.DataInputStream;
import java.io.InputStream;

/**
 * <p>
 * A class for reading a packet of JSON data from a stream. See
 * {@link JSONPacketWriter} for packet structure details.
 * </p>
 * 
 * @author Ryan Beckett
 * @version 1.0
 * @since Dec 22, 2011
 */
public class JSONPacketReader extends JSONReader {

    public JSONPacketReader(InputStream inStrm, String encd) {
        super(inStrm, encd);
    }

    /**
     * Reads a JSON packet from the specified stream. The data is extracted and
     * returned.
     */
    public String read() {
        return readPacket();
    }

    private String readPacket() {

        DataInputStream dataInStream = null;
        int payloadLen;
        byte payloadBuf[] = null;

        try {

            dataInStream = new DataInputStream(inStrm);

            // read payload length
            payloadLen = dataInStream.readInt();

            // read payload
            int totalBytesRead = 0, bytesRead = 0, currBufPos = 0;
            payloadBuf = new byte[payloadLen];
            while (totalBytesRead < payloadLen && bytesRead != -1) {
                bytesRead = dataInStream.read(payloadBuf, currBufPos, payloadLen);
                totalBytesRead += bytesRead;
                currBufPos += bytesRead;
            }

            // Java detects encoding
            return new String(payloadBuf);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}