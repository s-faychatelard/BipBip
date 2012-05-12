package fr.univmlv.IG.BipBip.Command;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Scanner;

import fr.univmlv.IG.BipBip.BipbipClient;
import fr.univmlv.IG.BipBip.Event.Event;
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
            
            // Verify if it is not a doubl
            // TODO can be ameliorate with clustering
            Event evt = new Event(event, x, y);
            for (Event e : BipbipClient.events.getEvents()) {
    			if (e.equals(evt)) {
    				return;
    			}
    		}
            BipbipClient.events.addEvent(new Event(event, x, y));
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
            
            BipbipClient.events.remove(new Event(event, x, y));
        }
    };
    
    public abstract void handle(SocketChannel sc,Scanner scanner) throws IOException;
    
    public static void sendInfos(SocketChannel sc, ArrayList<Event> list) throws IOException {
        NetUtil.writeLine(sc,"INFOS "+list.size());
        for (Event e:list) {
            sendInfo(sc,e);
        }
    }

    public static void sendInfo(SocketChannel sc, Event e) throws IOException {
        NetUtil.writeLine(sc,"INFO "+e.getType().name()+" "+e.getX()+" "+e.getY());
    }
    
    public static void remove(SocketChannel sc, Event e) throws IOException {
        NetUtil.writeLine(sc,"REMOVE "+e.getType().name()+" "+e.getX()+" "+e.getY());
    }
}
