package fr.univmlv.IG.BipBip;

import java.awt.Point;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Locale;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import fr.univmlv.IG.BipBip.Tooltip.Tooltip;
import fr.univmlv.IG.BipBip.Tooltip.TooltipListener;

public class BipbipServer {

    private final static int MAX_CONNECTIONS = 32;

    private final ServerSocketChannel ssc;

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
        /* Our protocol requires that we work with the US locale for
         * both doubles and dates
         */
        Locale.setDefault(Locale.US);
        BipbipServer server = new BipbipServer(6996);
        server.serve();
        Scanner scanner=new Scanner(System.in);
        
        JFrame frame = new JFrame("BipBip Server");
        frame.setSize(800, 600);
        JSplitPane splitPane = new JSplitPane();
        JPanel leftPanel = new JPanel();
        final MapPanel map = new MapPanel(new Point(1063208, 721344), 13);
        map.getOverlayPanel().setVisible(false);
        splitPane.add(leftPanel, JSplitPane.LEFT);
        splitPane.add(map, JSplitPane.RIGHT);
        frame.getContentPane().add(splitPane);
        frame.setVisible(true);
        
        /* Tooltip */
        Tooltip tooltipAlert = new Tooltip();
        tooltipAlert.addButton(new ImageIcon(Main.class.getResource("alert-fixe.png")), "Radar fixe");
        tooltipAlert.addButton(new ImageIcon(Main.class.getResource("alert-mobile.png")), "Radar mobile");
        tooltipAlert.addButton(new ImageIcon(Main.class.getResource("alert-accident.png")), "Accident");
        tooltipAlert.addButton(new ImageIcon(Main.class.getResource("alert-travaux.png")), "Travaux");
        tooltipAlert.addButton(new ImageIcon(Main.class.getResource("alert-divers.png")), "Divers").setLast(true);
        tooltipAlert.setLocation(280, 70);
        map.add(tooltipAlert);
        
        tooltipAlert.addTooltipListener(new TooltipListener() {
			@Override
			public void eventSelectedAtIndex(int index) {
				switch (index) {
				case 0:
					JOptionPane.showMessageDialog(map, "Add fixe");
					break;
				case 1:
					JOptionPane.showMessageDialog(map, "Add mobile");
					break;
				case 2:
					JOptionPane.showMessageDialog(map, "Add accident");
					break;
				case 3:
					JOptionPane.showMessageDialog(map, "Add travaux");
					break;
				case 4:
					JOptionPane.showMessageDialog(map, "Add divers");
					break;
				default:
					assert(0==1);
					break;
				}
			}
		});
        
        while (scanner.hasNextLine()) {
            String line=scanner.nextLine();
            if (line.equalsIgnoreCase("exit")) {
                scanner.close();
                System.exit(0);
            }
            System.err.println("Unknown command: "+line);
        }
    }
}