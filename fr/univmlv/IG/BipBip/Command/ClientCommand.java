package fr.univmlv.IG.BipBip.Command;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

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
            if (!scanner.hasNext()) throw new IOException("Invalid command");
            EventType event;
            double x,y;
            Date date;
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
            if (!scanner.hasNext()) {
                throw new IOException("Missing time coordinate");
            }
            String d=scanner.nextLine();
            try {
                /* We have to remove leading spaces, because they
                 * will disturb DateFormat.parse()
                 */
                while (d.startsWith(" ")) {
                    d=d.substring(1);
                }
                date=NetUtil.getDateformat().parse(d);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException("Invalid date: "+d);
            }
            /**
             * Customize from here 
             *
             * ...
             */
            System.err.println("CLIENT: SUBMIT "+event.name()+" "+x+" "+y+" "+NetUtil.getDateformat().format(date));
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
            if (!scanner.hasNext()) throw new IOException("Invalid command");
            EventType event;
            double x,y;
            Date date;
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
            if (!scanner.hasNext()) {
                throw new IOException("Missing time coordinate");
            }
            String d=scanner.nextLine();
            try {
                /* We have to remove leading spaces, because they
                 * will disturb DateFormat.parse()
                 */
                while (d.startsWith(" ")) {
                    d=d.substring(1);
                }
                date=NetUtil.getDateformat().parse(d);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException("Invalid date: "+d);
            }
            /**
             * Customize from here 
             *
             * ...
             */
            System.err.println("CLIENT: NOT_SEEN "+event.name()+" "+x+" "+y+" "+NetUtil.getDateformat().format(date));
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
            if (!scanner.hasNextDouble()) throw new IOException("Missing X coordinate");
            x=scanner.nextDouble();
            if (!scanner.hasNextDouble()) throw new IOException("Missing Y coordinate");
            y=scanner.nextDouble();
            /**
             * Customize from here 
             *
             * ...
             */
            System.err.println("CLIENT: GET_INFO "+x+" "+y);
            ArrayList<Event> list =new ArrayList<Event>();
            list.add(new Event(EventType.TRAVAUX,3,4));
            list.add(new Event(EventType.RADAR_FIXE,30,-46));
            list.add(new Event(EventType.RADAR_MOBILE,333,8));
            ServerCommand.sendInfos(sc, list);
        }
        
    };
    
    public abstract void handle(SocketChannel sc,Scanner scanner) throws IOException;
    
    public static void submit(SocketChannel sc,EventType event,double x,double y,Date date) throws IOException {
        NetUtil.writeLine(sc,"SUBMIT "+event.name()+" "+x+" "+y+" "+NetUtil.getDateformat().format(date));
    }
    
    public static void notSeen(SocketChannel sc,EventType event,double x,double y,Date date) throws IOException {
        NetUtil.writeLine(sc,"NOT_SEEN "+event.name()+" "+x+" "+y+" "+NetUtil.getDateformat().format(date));
    }

    public static void getInfo(SocketChannel sc,double x,double y) throws IOException {
        NetUtil.writeLine(sc,"GET_INFO "+x+" "+y);
    }
    

}
