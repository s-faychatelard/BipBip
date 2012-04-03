package fr.univmlv.IG.BipBip;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Scanner;

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
        public ArrayList<Event> handle(SocketChannel sc, Scanner scanner) throws IOException {
            if (!scanner.hasNextLine()) throw new IOException("Invalid command");
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
            ArrayList<Event> list=new ArrayList<Event>();
            for (int i=0;i<n;i++) {
                if (!scanner.hasNext() || !scanner.next().equals(ServerCommand.INFO.name())) {
                    throw new IOException("Missing INFO answer");
                }
                list.add((Event) ServerCommand.INFO.handle(sc,scanner));
            }
            return list;
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
        public Event handle(SocketChannel sc, Scanner scanner) throws IOException {
            if (!scanner.hasNext()) throw new IOException("Invalid command");
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
            return new Event(event,x,y);
        }
        
    };
    
    public abstract Object handle(SocketChannel sc,Scanner scanner) throws IOException;
    
    public static void sendInfos(SocketChannel sc,ArrayList<Event> list) throws IOException {
        NetUtil.writeLine(sc,"INFOS "+list.size());
        for (Event e:list) {
            sendEventInfo(sc,e);
        }
    }

    private static void sendEventInfo(SocketChannel sc, Event e) throws IOException {
        NetUtil.writeLine(sc,"INFO "+e.getType().name()+" "+e.getX()+" "+e.getY());
    }
    

}
