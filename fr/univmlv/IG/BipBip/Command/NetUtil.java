package fr.univmlv.IG.BipBip.Command;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class NetUtil {

    private final static Charset charset = Charset.forName("UTF-8");

    /**
     * Write a line in the channel
     * 
     * @param sc channel where to write
     * @param line to write
     * 
     * @throws IOException
     */
    public static void writeLine(SocketChannel sc,String line) throws IOException {
        if (!line.endsWith("\n")) {
            line=line+"\n";
        }
        sc.write(ByteBuffer.wrap(line.getBytes(charset)));
    }

    /**
     * Return the current charset
     * 
     * @return the charset
     */
    public static Charset getCharset() {
        return charset;
    }
}
