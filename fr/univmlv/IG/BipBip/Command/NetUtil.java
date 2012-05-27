/**
 * ESIPE Project - IR2 2011/2012 - IG
 * Copyright (C) 2012 ESIPE - Universite Paris-Est Marne-la-Vallee
 *
 * This is a free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Please see : http://www.gnu.org/licenses/gpl.html
 *
 * @author Damien Jubeau <djubeau@etudiant.univ-mlv.fr>
 * @author Sylvain Fay-Chatelard <sfaychat@etudiant.univ-mlv.fr>
 * @version 1.0
 */
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
