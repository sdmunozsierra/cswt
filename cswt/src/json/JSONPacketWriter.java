package json;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.json.JSONObject;

/**
 * A class for writing a packet of encoded JSON data to a stream.
 * 
 * <p>
 * JSON data is encapsulated in a generic packet. Often servers request to know
 * how much data to expect. The payload (JSON data) is preceded by an unsigned
 * 4-byte big-indian integer that represents the payload length. The packet
 * structure is as follows:
 * </p>
 * 
 * <pre>
 * ----------------------------------------------
 * 1|0|0|0|0|0|1|1|0|0|0|1|0|0|1|0|0|1|1|0|1|1|1|
 * ----------------------------------------------
 * |                    Payload                 |
 * ----------------------------------------------
 * </pre>
 * 
 * 
 * @author Ryan Beckett
 * @version 1.0
 * @since Dec 22, 2011
 */
public class JSONPacketWriter extends JSONWriter {

    public JSONPacketWriter(OutputStream outStrm, String encd) {
        super(outStrm, encd);
    }

    /**
     * <p>
     * Encapsulate the <code>json</code> data in a packet and write it to the
     * underlying stream. An unsigned 4-byte big-indian integer representing the
     * length of the encoded data is pre-appended to the packet.
     * </p>
     * 
     * @param json
     *            The JSON data to be written.
     */
    public void write(String json) {
        writePacket(json);
    }

    private void writePacket(String json) {

        JSONObject jsonObj = null;
        DataOutputStream dataOutStream = null;
        byte jsonBytes[] = null;

        try {

            // create UTF-8 encoded JSON bytes
            jsonObj = new JSONObject(json);
            jsonBytes = jsonObj.toString().getBytes(encd);

            dataOutStream = new DataOutputStream(outStrm);

            // write length header padding
            for (int i = 0; i < 4 - jsonBytes.length; i++)
                dataOutStream.write(0);

            // write length header as bytes
            dataOutStream.write(ByteBuffer.allocate(4).putInt(jsonBytes.length).array());

            // write payload
            dataOutStream.write(jsonBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}