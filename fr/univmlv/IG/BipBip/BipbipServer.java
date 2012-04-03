package fr.univmlv.IG.BipBip;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Locale;
import java.util.Scanner;

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