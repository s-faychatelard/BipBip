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
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventModelImpl;
import fr.univmlv.IG.BipBip.Event.EventType;

public enum ServerCommand {
    
    INFOS {
        /**
         * A INFOS command is supposed to have the following form:
         * 
         * INFOS N
         * line_1
         * ...
         * line_N
         * 
         * where N is the number of lines of information. Each line is of
         * the form:
         * 
         * INFO EVENT_TYPE X Y
         */
        @Override
        public void handle(SocketChannel sc, Scanner scanner) throws IOException {
            if (!scanner.hasNextLine()) throw new IOException("INFOS : Invalid command");
            String line=scanner.nextLine();
            int n;
            try {
                while (line.startsWith(" ")) {
                    line=line.substring(1);
                }
                n=Integer.parseInt(line);
            } catch (NumberFormatException e) {
                throw new IOException("Invalid integer value: "+line);
            }
            for (int i=0;i<n;i++) {
                if (!scanner.hasNext() || !scanner.next().equals(ServerCommand.INFO.name())) {
                    throw new IOException("Missing INFO answer");
                }
                ServerCommand.INFO.handle(sc,scanner);
            }
        }     

    },
    
    INFO {

        /**
         * A INFO command is supposed to have the following form:
         * 
         * INFO EVENT_TYPE X Y
         * 
         * where X and Y are double
         */
        @Override
        public void handle(SocketChannel sc, Scanner scanner) throws IOException {
            if (!scanner.hasNext()) throw new IOException("INFO : Invalid command");
            EventType event;
            double x,y;
            try {
                event=EventType.valueOf(scanner.next());
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid event type");
            }
            String tmp=scanner.next();
            try {
                x=Double.parseDouble(tmp);
            } catch (NumberFormatException e) {
                throw new IOException("Missing X coordinate");
            }
            tmp=scanner.nextLine();
            while (tmp.startsWith(" ")) {
                tmp=tmp.substring(1);
            }
            try {
                y=Double.parseDouble(tmp);
            } catch (NumberFormatException e) {
                throw new IOException("Missing Y coordinate");
            }

            Event evt = new Event(event, x, y);
            for (Event e : EventModelImpl.getInstance().getEvents()) {
    			if (e.isSame(evt)) {
    				return;
    			}
    		}
            EventModelImpl.getInstance().addEvent(new Event(event, x, y));
        }
    },
    
    REMOVE {

        /**
         * A REMOVE command is supposed to have the following form:
         * 
         * REMOVE EVENT_TYPE X Y
         * 
         * where X and Y are double
         */
        @Override
        public void handle(SocketChannel sc, Scanner scanner) throws IOException {
            if (!scanner.hasNext()) throw new IOException("REMOVE : Invalid command");
            EventType event;
            double x,y;
            try {
                event=EventType.valueOf(scanner.next());
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid event type");
            }
            String tmp=scanner.next();
            try {
                x=Double.parseDouble(tmp);
            } catch (NumberFormatException e) {
                throw new IOException("Missing X coordinate");
            }
            tmp=scanner.nextLine();
            while (tmp.startsWith(" ")) {
                tmp=tmp.substring(1);
            }
            try {
                y=Double.parseDouble(tmp);
            } catch (NumberFormatException e) {
                throw new IOException("Missing Y coordinate");
            }
            
            EventModelImpl.getInstance().remove(new Event(event, x, y));
        }
    },
    
    MODIFY {

        /**
         * A MODIFY command is supposed to have the following form:
         * 
         * MODIFY PREVIOUS_EVENT_TYPE PREVIOUS_X PREVIOUS_Y EVENT_TYPE X Y
         * 
         * where X and Y are double
         */
        @Override
        public void handle(SocketChannel sc, Scanner scanner) throws IOException {
            if (!scanner.hasNext()) throw new IOException("MODIFY : Invalid command");
            
            EventType previousEventType;
            double previousX,previousY;
            
            EventType eventType;
            double x,y;
            
            /* Get previous event information */
            try {
            	previousEventType=EventType.valueOf(scanner.next());
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid previous event type");
            }
            String tmp=scanner.next();
            try {
            	previousX=Double.parseDouble(tmp);
            } catch (NumberFormatException e) {
                throw new IOException("Missing previous X coordinate");
            }
            tmp=scanner.next();
            try {
            	previousY=Double.parseDouble(tmp);
            } catch (NumberFormatException e) {
                throw new IOException("Missing previous Y coordinate");
            }
            
            /* Get new event information */
            tmp=scanner.next();
            try {
                eventType=EventType.valueOf(tmp);
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid event type");
            }
            tmp=scanner.next();
            try {
                x=Double.parseDouble(tmp);
            } catch (NumberFormatException e) {
                throw new IOException("Missing X coordinate");
            }
            tmp=scanner.next();
            try {
                y=Double.parseDouble(tmp);
            } catch (NumberFormatException e) {
                throw new IOException("Missing Y coordinate");
            }
            
            EventModelImpl.getInstance().modifyEvent(new Event(previousEventType, previousX, previousY), new Event(eventType, x, y));
        }
    };
    
    public abstract void handle(SocketChannel sc,Scanner scanner) throws IOException;
    
    /**
     * Send all alerts to a client
     * 
     * @param sc of the server
     * @param it is the iterator of the Events
     * @param size represent the number of Events
     * 
     * @throws IOException
     */
    public static void sendInfos(SocketChannel sc, Iterable<Event> it, int size) throws IOException {
        for (Event e : it) {
            sendInfo(sc,e);
		}
    }

    /**
     * Send an alert to a client
     * 
     * @param sc of the server
     * @param e is the event to send
     * 
     * @throws IOException
     */
    public static void sendInfo(SocketChannel sc, Event e) throws IOException {
        NetUtil.writeLine(sc,"INFO "+e.getType().name()+" "+e.getX()+" "+e.getY());
    }
    
    /**
     * Send a modification of an event to a client
     * 
     * @param sc of the server
     * @param previous is the previous event
     * @param e is the new event
     * 
     * @throws IOException
     */
    public static void modify(SocketChannel sc, Event previous, Event e) throws IOException {
        NetUtil.writeLine(sc,"MODIFY "+previous.getType().name()+" "+previous.getX()+" "+previous.getY()+" "+e.getType().name()+" "+e.getX()+" "+e.getY());
    }
    
    /**
     * Send a remove event to a client
     * 
     * @param sc of the server
     * @param e is the event to remove
     * 
     * @throws IOException
     */
    public static void remove(SocketChannel sc, Event e) throws IOException {
        NetUtil.writeLine(sc,"REMOVE "+e.getType().name()+" "+e.getX()+" "+e.getY());
    }
}
