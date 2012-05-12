package fr.univmlv.IG.BipBip;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;


import fr.univmlv.IG.BipBip.Bottombar.BottomBar;
import fr.univmlv.IG.BipBip.Command.ClientCommand;
import fr.univmlv.IG.BipBip.Command.NetUtil;
import fr.univmlv.IG.BipBip.Event.EventModelImpl;
import fr.univmlv.IG.BipBip.Map.Map;
import fr.univmlv.IG.BipBip.Map.MapPanel;
import fr.univmlv.IG.BipBip.Map.TimelineMap;
import fr.univmlv.IG.BipBip.Table.Table;

public class BipbipServer {

    private final static int MAX_CONNECTIONS = 32;

    private final ServerSocketChannel ssc;

    private static final ImageIcon time = new ImageIcon(BipbipServer.class.getResource("icon-time.png"));
	private static final ImageIcon realtime = new ImageIcon(BipbipServer.class.getResource("icon-realtime.png"));
    
    public BipbipServer(int port) throws IOException {
        ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(port), MAX_CONNECTIONS);
    }

    public void serve() {
        System.out.println("Starting server");
        for (int i = 0; i < MAX_CONNECTIONS; i++) {
            new Thread() {
                @Override
                public void run() {
                    for (;;) {
                        synchronized (ssc) {
                            SocketChannel sc;
                            try {
                                System.out.println("Accept");
                                sc = ssc.accept();
                            } catch (IOException e) {
                                e.printStackTrace();
                                continue;
                            }
                            try {
                                serveClient(sc);
                            } finally {
                                try {
                                    sc.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    continue;
                                }
                            }
                        }

                    }
                }
            }.start();
        }
    }

    private void serveClient(final SocketChannel sc) {
        System.out.println("Dealing with client...");
        Scanner scanner = new Scanner(sc,NetUtil.getCharset().name());
        try {
            while (scanner.hasNextLine()) {
                System.out.println("lecture depuis le rŽseau");
                String line = scanner.nextLine();
                Scanner tmp_scanner=new Scanner(line);
                if (!tmp_scanner.hasNext()) break;
                String foo=tmp_scanner.next();
                try {
                    ClientCommand cmd=ClientCommand.valueOf(foo);
                    cmd.handle(sc,tmp_scanner);
                } catch (IllegalArgumentException e) {
                    throw new IOException("Invalid command: "+line);
                }
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        } finally {
            System.out.println("...end of client connection");
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
        JFrame frame = new JFrame("BipBip Server");
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
        
        /* Events */
        EventModelImpl events = new EventModelImpl();
        
        /* Left panel */
        final Table table = new Table(events);
        
        /* Center panel */
        final Map map = new Map(events);
        map.getMapPanel().setMinimumSize(new Dimension(300, 300));
        
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
        bottomBar.addText("Pour ajouter une nouvelle alerte, faites un clic prolongŽ sur le lieu de l'alerte, puis choisissez son type.");
        content.add(bottomBar, BorderLayout.SOUTH);
        
        /* Switcher between Time view and Current view */
        final JButton btn = new JButton(time);
        btn.setToolTipText("Ouvrir la timeline");
        btn.setSize(34, 30);
        btn.setLocation(20, 20);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusable(false);
        btn.setRolloverEnabled(false);
        map.getMapPanel().add(btn);
        btn.addActionListener(new ActionListener() {
        	
        	//TODO need to be put on time
        	private long startTime = new Date().getTime();
			
        	@Override
			public void actionPerformed(ActionEvent e) {
				for (Component component : content.getComponents()) {
					if (component instanceof JSplitPane) {
						btn.setIcon(realtime);
						btn.setToolTipText("Revenir ˆ la vue en temps rŽel");
						timelineMap.getMapPanel().add(btn);
						
						content.remove(component);
						bottomBar.getTimeSlider().setMinAndMax(startTime, new Date().getTime());
						bottomBar.addTimeSlider(bottomBar.getTimeSlider());
						bottomBar.repaint();
						content.add(timelineMap.getMapPanel(), BorderLayout.CENTER);
						content.revalidate();
						content.repaint();
					}
					else if (component instanceof MapPanel) {
						btn.setIcon(time);
						btn.setToolTipText("Ouvrir la timeline");
						map.getMapPanel().add(btn);
						
						content.remove(component);
				        bottomBar.addText("Pour ajouter une nouvelle alerte, faites un clic prolongŽ sur le lieu de l'alerte, puis choisissez son type.");
				        bottomBar.repaint();
				        content.add(splitPane, BorderLayout.CENTER);
				        content.revalidate();
				        content.repaint();
					}
				}
			}
		});
       
        frame.setVisible(true);
        
        JFrame.getWindows()[0].addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
				System.out.println("windowOpened");
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				System.out.println("windowIconified");
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				System.out.println("windowDeiconified");
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				System.out.println("windowDeactivated");
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("windowClosing");
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				System.out.println("windowClosed");
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				System.out.println("windowActivated");
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