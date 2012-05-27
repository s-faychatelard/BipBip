package fr.univmlv.IG.BipBip.Command;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.SortedSet;

import fr.univmlv.IG.BipBip.BipbipServer;
import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventType;
import fr.univmlv.IG.Utils.SpatialHashing;

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
        	   
       		double xStart,yStart, xEnd, yEnd;
            if (!scanner.hasNextDouble()) {
                throw new IOException("Missing X start coordinate");
            }
            xStart=scanner.nextDouble();
            
            if (!scanner.hasNextDouble()) {
                throw new IOException("Missing Y start coordinate");
            }
            yStart=scanner.nextDouble();
            
            if (!scanner.hasNextDouble()) {
                throw new IOException("Missing X end coordinate");
            }
            xEnd=scanner.nextDouble();
            
            if (!scanner.hasNextDouble()) {
                throw new IOException("Missing Y end coordinate");
            }
            yEnd=scanner.nextDouble();

            
            Event eMin = Event.createMockEvent(SpatialHashing.compute(yStart, xStart));
        	Event eMax = Event.createMockEvent(SpatialHashing.compute(yEnd, xEnd));

        	//TODO si eMin et eMax existent : ne pas les virer

        	BipbipServer.treeAdapter.tree.add(eMin);
        	BipbipServer.treeAdapter.tree.add(eMax);

        	SortedSet<Event> events = BipbipServer.treeAdapter.tree.subSet(eMin, eMax);
        	
        	BipbipServer.treeAdapter.tree.remove(eMin);
        	BipbipServer.treeAdapter.tree.remove(eMax);
        	events.remove(eMax);
        	events.remove(eMin);

        	//System.out.println(events);        	
           	ServerCommand.sendInfos(sc, events, events.size());
        }
    };
    
    public abstract void handle(SocketChannel sc,Scanner scanner) throws IOException;
    
    /**
     * Submit a new alert
     * 
     * @param sc channel of the client
     * @param event represent the type of alert
     * @param x longitude of the alert
     * @param y latitude of the alert
     * 
     * @throws IOException
     */
    public static void submit(SocketChannel sc,EventType event,double x,double y) throws IOException {
        NetUtil.writeLine(sc,"SUBMIT "+event.name()+" "+x+" "+y);
    }
    
    /**
     * Confirm an alert
     * 
     * @param sc channel of the client
     * @param event represent the type of alert
     * @param x longitude of the alert
     * @param y latitude of the alert
     * 
     * @throws IOException
     */
    public static void confirm(SocketChannel sc,EventType event,double x,double y) throws IOException {
        NetUtil.writeLine(sc,"CONFIRM "+event.name()+" "+x+" "+y);
    }
    
    /**
     * Unconfirm an alert
     * 
     * @param sc channel of the client
     * @param event represent the type of alert
     * @param x longitude of the alert
     * @param y latitude of the alert
     * 
     * @throws IOException
     */
    public static void notSeen(SocketChannel sc,EventType event,double x,double y) throws IOException {
        NetUtil.writeLine(sc,"NOT_SEEN "+event.name()+" "+x+" "+y);
    }

    /**
     * Get all alerts in a specific zone for a specific zoom
     * 
     * @param sc channel of the client
     * @param x longitude of the client map
     * @param y latitude of the client map
     * @param zoom of the client map
     * 
     * @throws IOException
     */
    public static void getInfo(SocketChannel sc, double xStart,double yStart, double xEnd,double yEnd) throws IOException {
        NetUtil.writeLine(sc,"GET_INFO "+xStart+" "+yStart+" "+xEnd+" "+yEnd);

    }
    

}
