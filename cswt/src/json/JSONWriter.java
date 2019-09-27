package json;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Abstract class for writing JSON data to byte streams.
 * 
 * <p>
 * Subclass <code>JSONWriter</code> or {@link JSONReader} to design a custom
 * reader or writer.
 * </p>
 * 
 * <pre>
 * Socket sock = null;
 * JSONWriter writer = new JSONWriter(sock.getOutputStream(), encd) {
 * 
 *     @Override
 *     public void write(String json) {
 *         try {
 *             outStrm.write(json.getBytes(encd));
 *         } catch (IOException e) {
 *             e.printStackTrace();
 *         }
 *     }
 * };
 * </pre>
 * 
 * @author Ryan Beckett
 * @version 1.0
 * @since Dec 23, 2011
 */
public abstract class JSONWriter {

    protected OutputStream outStrm;
    protected String encd;

    protected JSONWriter(OutputStream outStrm, String encd) {
        this.outStrm = outStrm;
        this.encd = encd;
    }

    /**
     * Write the JSON data to the underlying stream.
     * 
     * @param json
     *            The JSON data to be written.
     */
    public abstract void write(String json);

    /**
     * Close the writer and its underlying stream.
     */
    public void close() {
        try {
            this.outStrm.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}