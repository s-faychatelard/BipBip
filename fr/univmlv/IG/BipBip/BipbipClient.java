package fr.univmlv.IG.BipBip;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

import javax.swing.JFrame;

import fr.univmlv.IG.BipBip.Bottombar.BottomBar;
import fr.univmlv.IG.BipBip.Command.ClientCommand;
import fr.univmlv.IG.BipBip.Command.NetUtil;
import fr.univmlv.IG.BipBip.Command.ServerCommand;
import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventModelImpl;
import fr.univmlv.IG.BipBip.Event.EventModelListener;
import fr.univmlv.IG.BipBip.Event.EventType;
import fr.univmlv.IG.BipBip.Map.Map;
import fr.univmlv.IG.BipBip.Map.MapPanel;

public class BipbipClient {

	private final InetSocketAddress server;
	Charset charset = Charset.forName("UTF8");
	private SocketChannel sc;

	/* Events */
	public static EventModelImpl events = new EventModelImpl();

	private static final String bottomBarText = "Pour ajouter une nouvelle alerte, faites un clic prolongé sur le lieu de l'alerte, puis choisissez son type.";

	public BipbipClient(String host, int port) {
		this.server = new InetSocketAddress(host, port);
	}

	public void connect() throws IOException {
		sc = SocketChannel.open();
		sc.connect(server);
	}

	public void submit(EventType event,double x,double y) throws IOException {
		ClientCommand.submit(sc, event, x, y);
	}

	public void confirm(EventType event,double x,double y) throws IOException {
		ClientCommand.confirm(sc, event, x, y);
	}

	public void unconfirm(EventType event,double x,double y) throws IOException {
		ClientCommand.notSeen(sc, event, x, y);
	}

	public void getInfos(double x, double y, int zoom) throws IOException {
		/* Get events for this position */
		ClientCommand.getInfo(sc, x, y, zoom);
	}

	public void serveCommand() {
		Scanner scanner = new Scanner(sc,NetUtil.getCharset().name());

		events.addEventListener(new EventModelListener() {

			@Override
			public void eventAdded(Event event) {
				try {
					BipbipClient.this.submit(event.getType(), event.getX(), event.getY());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void eventRemoved(int index) {
				// Do nothing, you are a client and you cannot remove an event
			}

			@Override
			public void eventModified(Event previousEvent, Event event) {
				// Do nothing, you are a client and you cannot modify an event
			}

			@Override
			public void eventConfirmed(int index) {
				try {
					BipbipClient.this.confirm(events.getEvents().get(index).getType(), events.getEvents().get(index).getX(), events.getEvents().get(index).getY());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void eventUnconfirmed(int index) {
				try {
					BipbipClient.this.unconfirm(events.getEvents().get(index).getType(), events.getEvents().get(index).getX(), events.getEvents().get(index).getY());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		try {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				Scanner tmp_scanner=new Scanner(line);
				if (!tmp_scanner.hasNext()) break;
				String foo=tmp_scanner.next();
				try {
					ServerCommand cmd=ServerCommand.valueOf(foo);
					cmd.handle(sc,tmp_scanner);
				} catch (IllegalArgumentException e) {
					throw new IOException("Invalid command: "+foo);
				}
			}
		} catch (IOException ie) {
			ie.printStackTrace();
		} finally {
			try {
				scanner.close();
				sc.close();
			} catch (IOException ignored) {
				ignored.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		/* Global frame */
		JFrame frame = new JFrame("BipBip Client");
		frame.setSize(1024, 768);
		frame.setMinimumSize(new Dimension(800, 600));
		frame.setLayout(new BorderLayout());
		frame.setLocationRelativeTo(frame.getRootPane());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Center panel */
		final Map map = new Map(events);
		map.getMapPanel().setMinimumSize(new Dimension(300, 300));
		frame.getContentPane().add(map.getMapPanel(), BorderLayout.CENTER);

		/* Bottom bar */
		final BottomBar bottomBar = new BottomBar(null, null);
		bottomBar.setPreferredSize(new Dimension(1, 30));
		bottomBar.addText(bottomBarText);
		frame.getContentPane().add(bottomBar, BorderLayout.SOUTH);

		frame.setVisible(true);

		final BipbipClient client = new BipbipClient("localhost", 6996);
		client.connect();

		map.getMapPanel().addPropertyChangeListener("mapPosition", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				int zoom = map.getMapPanel().getZoom();
				try {
					client.getInfos(MapPanel.position2lat(map.getMapPanel().getMapPosition().y, zoom), MapPanel.position2lon(map.getMapPanel().getMapPosition().y, zoom), zoom);
				} catch (IOException e) {
					e.printStackTrace();
				}
				map.getMapPanel().repaint();
			}
		});
		
		client.getInfos(MapPanel.position2lat(map.getMapPanel().getMapPosition().y, map.getMapPanel().getZoom()), MapPanel.position2lon(map.getMapPanel().getMapPosition().y, map.getMapPanel().getZoom()), map.getMapPanel().getZoom());
		client.serveCommand();
	}
}