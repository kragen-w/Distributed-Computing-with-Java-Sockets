package sock;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Head {
    private static final int NODES_NEEDED = 4; // The number of nodes needed to start
    private List<Socket> nodeSockets = new ArrayList<>(); // Store the sockets for each node
    private List<ObjectInputStream> nodeInputStreams = new ArrayList<>();
    private List<ObjectOutputStream> nodeOutputStreams = new ArrayList<>();

    public static void main(String[] args) throws ClassNotFoundException {
        new Head().startServer();
    }

    public void startServer() throws ClassNotFoundException {
        try {
            ServerSocket ss = new ServerSocket(7001);
            int numNodes = 0;

            System.out.println("Waiting for " + NODES_NEEDED + " nodes to connect...");

            // Wait for NODES_NEEDED nodes to connect
            while (numNodes < NODES_NEEDED) {
                Socket s = ss.accept(); 
                System.out.println("Node " + (numNodes + 1) + " connected.");
                
                // Add socket to list
                nodeSockets.add(s);

                // Initialize the input/output streams for each node
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                
                // Store the streams for later use
                nodeInputStreams.add(ois);
                nodeOutputStreams.add(oos);

                numNodes++;
            }

            System.out.println("All nodes connected! Now proceeding with communication...");
            long t1 = System.currentTimeMillis();

            // Process messages from nodes or allow interaction
            int start = 1000;
            int stop = 100000;
            int primeTotal = 0;
            Thread[] ths = new Thread[NODES_NEEDED];
            int[] primeCounts = new int[NODES_NEEDED];
            for (int i=0; i<NODES_NEEDED; i++) {
                int startRange = ((stop-start)/NODES_NEEDED)*i + start;
                int stopRange = ((stop-start)/NODES_NEEDED)*(i+1) + start;//1-10,11-20...style threading
                clientHandlerThread va = new clientHandlerThread(i, startRange, stopRange, primeCounts, nodeSockets.get(i), nodeInputStreams.get(i) ,nodeOutputStreams.get(i));
    			Thread th = new Thread(va);
    			ths[i] = th;
    			th.start(); // fork
//                nodeOutputStreams.get(i).writeObject(startRange);
//                nodeOutputStreams.get(i).writeObject(stopRange);
//                int x = (int) nodeInputStreams.get(i).readObject();
//                primeTotal += x;
                
                
            }
            for (int i=0; i<NODES_NEEDED; i++) {
    			try {
    				ths[i].join();
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}

    		for (int i=0; i<NODES_NEEDED; i++) {
    			primeTotal += primeCounts[i];
    		}
            
            System.out.println("The final prime count is: " + primeTotal);

            // Close connections after processing
            closeConnections();
            long t2 = System.currentTimeMillis();
        	System.out.println(t2-t1 + " miliseconds.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to close all connections to the nodes
    private void closeConnections() throws IOException {
        for (int i = 0; i < NODES_NEEDED; i++) {
//            nodeInputStreams.get(i).close();
//            nodeOutputStreams.get(i).close();
            nodeSockets.get(i).close();
        }
        System.out.println("All connections closed.");
    }
    
}
