package fr.univmlv.IG.BipBip;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import fr.univmlv.IG.BipBip.Bottombar.BottomBar;
import fr.univmlv.IG.BipBip.Command.ClientCommand;
import fr.univmlv.IG.BipBip.Command.NetUtil;
import fr.univmlv.IG.BipBip.Command.ServerCommand;
import fr.univmlv.IG.BipBip.Event.Event;
import fr.univmlv.IG.BipBip.Event.EventModelImpl;
import fr.univmlv.IG.BipBip.Event.EventModelListener;
import fr.univmlv.IG.BipBip.Map.Map;
import fr.univmlv.IG.BipBip.Map.MapPanel;
import fr.univmlv.IG.BipBip.Map.TimelineMap;
import fr.univmlv.IG.BipBip.Resources.ImageNames;
import fr.univmlv.IG.BipBip.Resources.ResourcesManager;
import fr.univmlv.IG.BipBip.Table.Table;
import fr.univmlv.IG.BipBip.Table.TableListener;

public class BipbipServer {

	private final static int MAX_CONNECTIONS = 32;

	private final ServerSocketChannel ssc;

	/* Static object */
	public static EventModelImpl events = new EventModelImpl();
	public static JFrame frame;
	private static final String btnSwitcherSecondaryText = "Revenir à la vue en temps réel";
	private static final String btnSwitcherDefaultText = "Ouvrir la timeline";
	private static final String bottomBarText = "Pour ajouter une nouvelle alerte, faites un clic prolongé sur le lieu de l'alerte, puis choisissez son type.";

	public BipbipServer(int port) throws IOException {
		ssc = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress(port), MAX_CONNECTIONS);
	}

	/**
	 * Wait connection and serve a client as soon as possible
	 */
	public void serve() {
		for (int i=0; i<MAX_CONNECTIONS; i++) {
			new Thread() {
				@Override
				public void run() {
					for (;;) {
						synchronized (ssc) {
							SocketChannel sc;
							try {
								sc = ssc.accept();
								serveClient(sc);
							} catch (IOException e) {
								e.printStackTrace();
								continue;
							}
						}

					}
				}
			}.start();
		}
	}

	/**
	 * Serve a client 
	 * 
	 * @param sc channel of the client
	 */
	private void serveClient(final SocketChannel sc) {

		/* Push new information on event */
		events.addEventListener(new EventModelListener() {

			@Override
			public void eventsAdded(List<? extends Event> events) {
				try {
					ServerCommand.sendInfos(sc, new ArrayList<Event>(events));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void eventAdded(Event event) {
				try {
					ServerCommand.sendInfo(sc, event);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void eventModified(Event previousEvent, Event event) {
				try {
					ServerCommand.modify(sc, previousEvent, event);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void eventRemoved(int index) {
				try {
					ServerCommand.remove(sc, events.getEvents().get(index));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void eventConfirmed(int index) {
				// Do nothing, you are the server
			}

			@Override
			public void eventUnconfirmed(int index) {
				// Do nothing, you are the server
			}
		});

		/* Wait command from client */
		Scanner scanner = new Scanner(sc,NetUtil.getCharset().name());
		try {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				Scanner tmp_scanner=new Scanner(line);
				if (!tmp_scanner.hasNext()) break;
				String foo=tmp_scanner.next();
				try {
					ClientCommand cmd=ClientCommand.valueOf(foo);
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

	/**
	 * Create the window
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		/* Global frame */
		frame = new JFrame("BipBip Server");
		frame.setSize(1024, 768);
		frame.setMinimumSize(new Dimension(800, 600));
		frame.setLayout(new BorderLayout());
		frame.setLocationRelativeTo(frame.getRootPane());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Default content panel */
		final JPanel content = new JPanel(new BorderLayout());
		content.setPreferredSize(new Dimension(800, 600));

		/* Overlay panel*/
		final JPanel overlayPanel = new JPanel(null);
		overlayPanel.setPreferredSize(new Dimension(800, 600));
		overlayPanel.setOpaque(false);

		/* Layered Panel */
		JLayeredPane layeredPanel = new JLayeredPane();
		layeredPanel.setLayout(new LayeredLayoutManager());
		layeredPanel.add(content,Integer.valueOf(0));
		layeredPanel.add(overlayPanel, Integer.valueOf(1));
		frame.getContentPane().add(layeredPanel, BorderLayout.CENTER);

		/* Center panel */
		final Map map = new Map(events);
		map.getMapPanel().setMinimumSize(new Dimension(300, 300));
		
		/* Left panel */
		final Table table = new Table(events);
		table.getModel().addTableListener(new TableListener() {
			
			@Override
			public void eventLocateEventAtIndex(int index) {
				int x = MapPanel.lon2position(events.getEvents().get(index).getX(), map.getMapPanel().getZoom()) - map.getMapPanel().getWidth()/2;
				int y = MapPanel.lat2position(events.getEvents().get(index).getY(), map.getMapPanel().getZoom()) - map.getMapPanel().getHeight()/2;
				map.getMapPanel().setMapPosition(x, y);
			}
		});

		/* SplitPane */
		final SplitPane splitPane = new SplitPane();
		splitPane.add(table.getPanel(), JSplitPane.LEFT);
		splitPane.add(map.getMapPanel(), JSplitPane.RIGHT);
		
		content.add(splitPane, BorderLayout.CENTER);

		/* Timeline map */
		final TimelineMap timelineMap = new TimelineMap(events);
		timelineMap.getMapPanel().setMinimumSize(new Dimension(300, 300));

		/* Bottom bar */
		final BottomBar bottomBar = new BottomBar(overlayPanel, timelineMap);
		bottomBar.setPreferredSize(new Dimension(1, 30));
		bottomBar.addText(bottomBarText);
		content.add(bottomBar, BorderLayout.SOUTH);

		/* Switcher between Time view and Current view */
		final JButton btn = new JButton(ResourcesManager.getRessourceAsImageIcon(ImageNames.Icon.TIME));
		btn.setToolTipText(btnSwitcherDefaultText);
		btn.setSize(34, 30);
		btn.setLocation(20, 20);
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setBorderPainted(false);
		btn.setFocusable(false);
		btn.setRolloverEnabled(false);
		map.getMapPanel().add(btn);
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Component component : content.getComponents()) {
					if (component instanceof JSplitPane) {
						btn.setIcon(ResourcesManager.getRessourceAsImageIcon(ImageNames.Icon.REALTIME));
						btn.setToolTipText(btnSwitcherSecondaryText);
						timelineMap.getMapPanel().add(btn);

						content.remove(component);
						bottomBar.addTimeSlider(bottomBar.getTimeSlider());
						bottomBar.repaint();
						content.add(timelineMap.getMapPanel(), BorderLayout.CENTER);
						content.revalidate();
						content.repaint();
					}
					else if (component instanceof MapPanel) {
						btn.setIcon(ResourcesManager.getRessourceAsImageIcon(ImageNames.Icon.TIME));
						btn.setToolTipText(btnSwitcherDefaultText);
						map.getMapPanel().add(btn);

						content.remove(component);
						bottomBar.addText(bottomBarText);
						bottomBar.repaint();
						content.add(splitPane, BorderLayout.CENTER);
						content.revalidate();
						content.repaint();
					}
				}
			}
		});

		frame.setVisible(true);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
				try {
					FileWriter writer = new FileWriter("rawdata.txt");
					BufferedWriter out = new BufferedWriter(writer);
					for (Event evt : events.getEventsFromBeginning()) {
						out.write(evt.toString() + "\n");
					}
					out.flush();
					out.close();
				} catch (IOException e1) {
					throw new RuntimeException("Cannot save cache");
				}
			}
		});

		JFrame.getWindows()[0].addWindowListener(new WindowListener() {
			@Override public void windowIconified(WindowEvent e) {}
			@Override public void windowDeiconified(WindowEvent e) {}
			@Override public void windowDeactivated(WindowEvent e) {}
			@Override public void windowClosing(WindowEvent e) {}
			@Override public void windowClosed(WindowEvent e) {}
			@Override public void windowActivated(WindowEvent e) {}

			@Override public void windowOpened(WindowEvent e) {
				try {
					FileReader reader = new FileReader("rawdata.txt");
					BufferedReader in = new BufferedReader(reader);
					String line;
					while ((line = in.readLine()) != null) {
						Event evt = Event.fromString(line);
						events.addEvent(evt);
					}
				} catch (FileNotFoundException e1) {
					// Nothing to do, it is probably normal
				} catch (IOException e1) {
					throw new RuntimeException("Cannot load cache");
				}
			}
		});

		/* Our protocol requires that we work with the US locale for
		 * both doubles and dates
		 */
		Locale.setDefault(Locale.US);
		BipbipServer server = new BipbipServer(6996);
		server.serve();
	}
}