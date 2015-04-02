package order.sender;

import java.io.IOException;
import java.io.Writer;

/**
 * Represents a SenderWriter
 */
public class SenderWriter extends Writer {

    private final Sender sender;

    public SenderWriter(Sender sender) {
        this.sender = sender;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        StringBuilder string = new StringBuilder().append(cbuf, off, len);
        sender.send(string);
    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public void close() throws IOException {

    }
}
