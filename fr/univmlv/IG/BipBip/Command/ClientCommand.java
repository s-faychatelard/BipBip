package fr.univmlv.IG.BipBip.Command;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.SortedSet;

import fr.univmlv.IG.BipBip.BipbipServer;
import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventType;

public enum ClientCommand {
    
    SUBMIT {
        /**
         * A SUBMIT command is supposed to have the following form:
         * 
         * SUBMIT EVENT X Y DATE
         * 
         * where X and Y are double, and DATE is a full Date in the US locale.
         * 
         * SUBMIT is used by a client that want to report the existence of something
         */
        @Override
        public void handle(SocketChannel sc, Scanner scanner) throws IOException {
            if (!scanner.hasNext()) throw new IOException("SUBMIT Invalid command");
            EventType eventType;
            double x,y;
            try {
                eventType=EventType.valueOf(scanner.next());
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid event type");
            }
            if (!scanner.hasNextDouble()) {
                throw new IOException("Missing X coordinate");
            }
            x=scanner.nextDouble();
            if (!scanner.hasNextDouble()) {
                throw new IOException("Missing Y coordinate");
            }
            y=scanner.nextDouble();
            
            // A client send a new Event we need to check if is not already on our list
            // TODO can be ameliorate with clustering
            Event evt = new Event(eventType, x, y);
            for (Event e : BipbipServer.events.getEvents()) {
    			if (e.equals(evt)) {
    				return;
    			}
    		}
            BipbipServer.events.addEvent(evt);
        }
    },
    
    CONFIRM {
        /**
         * A CONFIRM command is supposed to have the following form:
         * 
         * CONFIRM EVENT X Y DATE
         * 
         * where X and Y are double, and DATE is a full Date in the US locale.
         * 
         * CONFIRM is used by a client that want to confirm the existence of something
         */
        @Override
        public void handle(SocketChannel sc, Scanner scanner) throws IOException {
            if (!scanner.hasNext()) throw new IOException("CONFIRM Invalid command");
            EventType event;
            double x,y;
            try {
                event=EventType.valueOf(scanner.next());
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid event type");
            }
            if (!scanner.hasNextDouble()) {
                throw new IOException("Missing X coordinate");
            }
            x=scanner.nextDouble();
            if (!scanner.hasNextDouble()) {
                throw new IOException("Missing Y coordinate");
            }
            y=scanner.nextDouble();
            
            // A client confirm an alert
            BipbipServer.events.confirm(new Event(event, x, y));
        }
    },
    
    NOT_SEEN {
        /**
         * A NOT_SEEN command is supposed to have the following form:
         * 
         * NOT_SEEN EVENT X Y DATE
         * 
         * where X and Y are double, and DATE is a full Date in the US locale
         * 
         * NOT_SEEN is used by a client that want to report that he/she didn't
         * see an event reported by the server
         */
        @Override
        public void handle(SocketChannel sc, Scanner scanner) throws IOException {
            if (!scanner.hasNext()) throw new IOException("NOT_SEEN Invalid command");
            EventType event;
            double x,y;
            try {
                event=EventType.valueOf(scanner.next());
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid event type");
            }
            if (!scanner.hasNextDouble()) {
                throw new IOException("Missing X coordinate");
            }
            x=scanner.nextDouble();
            if (!scanner.hasNextDouble()) {
                throw new IOException("Missing Y coordinate");
            }
            y=scanner.nextDouble();
            
            // A client declare an event not seen
            BipbipServer.events.unconfirm(new Event(event, x, y));
        }
    },

    GET_INFO {
        /**
         * A GET_INFO command is supposed to have the following form:
         * 
         * GET_INFO X Y
         * 
         * where X and Y are double
         */
        @Override
        public void handle(SocketChannel sc, Scanner scanner) throws IOException {
        	   
       		double x,y;
    		int zoom;
            if (!scanner.hasNextDouble()) {
                throw new IOException("Missing X coordinate");
            }
            x=scanner.nextDouble();
            if (!scanner.hasNextDouble()) {
                throw new IOException("Missing Y coordinate");
            }
            y=scanner.nextDouble();
            if (!scanner.hasNextInt()) {
                throw new IOException("Missing zoom info");
            }
            zoom=scanner.nextInt();

        	SortedSet<Event> events = BipbipServer.treeAdapter.tree.tailSet(new Event(EventType.ACCIDENT, x, y));
        	ServerCommand.sendInfos(sc, events, events.size());
        	
//        	ServerCommand.sendInfos(sc, new ArrayList<Event>(BipbipServer.events.getEvents()));
        }


    }
    ;
    
    public abstract void handle(SocketChannel sc,Scanner scanner) throws IOException;
    
    public static void submit(SocketChannel sc,EventType event,double x,double y) throws IOException {
        NetUtil.writeLine(sc,"SUBMIT "+event.name()+" "+x+" "+y);
    }
    
    public static void confirm(SocketChannel sc,EventType event,double x,double y) throws IOException {
        NetUtil.writeLine(sc,"CONFIRM "+event.name()+" "+x+" "+y);
    }
    
    public static void notSeen(SocketChannel sc,EventType event,double x,double y) throws IOException {
        NetUtil.writeLine(sc,"NOT_SEEN "+event.name()+" "+x+" "+y);
    }

    public static void getInfo(SocketChannel sc, double x,double y, int zoom) throws IOException {
    	System.out.println("GET_INFO "+x+" "+y+" "+zoom);
        NetUtil.writeLine(sc,"GET_INFO "+x+" "+y+" "+zoom);
    }
    

}
