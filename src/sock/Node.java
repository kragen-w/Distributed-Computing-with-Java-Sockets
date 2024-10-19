package sock;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Node {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
    // Constructor to initialize the Node with a server and port
    public Node(String host, int port) throws IOException {
        System.out.println("Attempting to connect to the server...");
        socket = new Socket(host, port);
        System.out.println("Connected to " + host + " on port " + port);
        
        // Initialize ObjectOutputStream and ObjectInputStream for communication
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }
    
    // Method to send an integer and receive a response
    public void sendInteger(int number) throws IOException, ClassNotFoundException {
        oos.writeObject(number); // Send the number

    }
    
    // Method to close the connection
    public void closeConnection() throws IOException {
        oos.close();
        ois.close();
        socket.close();
        System.out.println("Connection closed");
    }
    
    // Method to read input from the user
    public boolean isPrime(int num) {
		if (num <= 1){
	        return true;
	    }
	    int i = 2;
	    while (num % i != 0 && i < num){
	        i ++;
	    }
	    
	    if (i == num){
    	    	System.out.println(num + " is prime.");
	        return true;
	    } else {
	        return false;
	    }
	}
	public int PrimeInRange(int startRange, int endRange){
	    int primeCount = 0;
	    for (int i = startRange; i < endRange + 1; i++){
	        if (isPrime(i)){
	            primeCount++;
	        }
	    }
	    return primeCount;
	}
	
	public int listenToRangesAndSend() throws ClassNotFoundException, IOException {
		int x = (int)ois.readObject();
		int y = (int)ois.readObject();
		System.out.println("Range start: " + x);
		System.out.println("Range stop: " + y);
		int z = PrimeInRange(x,y);
		sendInteger(z);
		return z;
	}
    

    // Main method to test the Node class
    public static void main(String[] args) {
        try {
            // Create a new instance of Node (connecting to localhost on port 7001)
            Node node1 = new Node("localhost", 7001);
            Node node2 = new Node("localhost", 7001);
            Node node3 = new Node("localhost", 7001);
            Node node4 = new Node("localhost", 7001);
            
           node1.listenToRangesAndSend();
           node2.listenToRangesAndSend();
           node3.listenToRangesAndSend();
           node4.listenToRangesAndSend();

//    
            node1.closeConnection();  // Close the connection
            node2.closeConnection();
            node3.closeConnection();
            node4.closeConnection();
            
   
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
