package fr.univmlv.IG.BipBip;

import java.awt.Point;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JFrame;

import fr.univmlv.IG.BipBip.Command.ClientCommand;
import fr.univmlv.IG.BipBip.Command.NetUtil;
import fr.univmlv.IG.BipBip.Command.ServerCommand;
import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventType;
import fr.univmlv.IG.BipBip.Map.MapPanel;

public class BipbipClient {

	private final InetSocketAddress server;
	Charset charset = Charset.forName("UTF8");
	private SocketChannel sc;
	private Scanner scanner;
    
	public BipbipClient(String host, int port) {
		this.server = new InetSocketAddress(host, port);
	}

	public void connect() throws IOException {
		sc = SocketChannel.open();
		sc.connect(server);
		scanner=new Scanner(sc,NetUtil.getCharset().name());
	}
	
	public void submit(EventType event,double x,double y,Date date) throws IOException {
        ClientCommand.submit(sc, event, x, y, date);
    }

	@SuppressWarnings("unchecked")
    public void getInfo(double x,double y) throws IOException {
	    ClientCommand.getInfo(sc,x,y);
	    if (!scanner.hasNext() || !scanner.next().equals(ServerCommand.INFOS.name())) {
	        throw new IOException("Server did not respond to the GET_INFO query");
	    }
        ArrayList<Event> list=(ArrayList<Event>) ServerCommand.INFOS.handle(sc, scanner);
        System.out.println("SERVER: INFOS "+list.size());
        for (Event e:list) {
            System.out.println("SERVER: INFO "+e.getType().name()+" "+x+" "+y);
        }
	}
	
	public void close() throws IOException {
        sc.close();
    }
	
	public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("BipBip Client");
        frame.setSize(800, 600);
        MapPanel map = new MapPanel(new Point(1063208, 721344), 13);
        map.getOverlayPanel().setVisible(false);
        frame.getContentPane().add(map);
        frame.setVisible(true);
        
        BipbipClient client = new BipbipClient("localhost", 6996);
		client.connect();
		client.submit(EventType.RADAR_FIXE,35,10,new Date());
        client.submit(EventType.TRAVAUX,122,-20,new Date());
        client.getInfo(1,23);
	}
}