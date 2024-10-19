package sock;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class clientHandlerThread implements Runnable {
	private int tnum;
	private int start;
	private int stop;
	private int[] primeCounts;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Socket socket;
	public clientHandlerThread(int tnum, int start, int stop, int[] primeCounts,Socket socket, ObjectInputStream input, ObjectOutputStream output)
	{
		this.tnum = tnum;
		this.start = start;
		this.stop = stop;
		this.primeCounts = primeCounts;
		this.input = input;
		this.output = output;
		this.socket = socket;

	}
	@Override
	public void run(){
		try {
			System.out.println("in thread " + tnum + " " + start + " - " + stop);
			// Vec add
			
			
			this.output.writeObject(start);
	        this.output.writeObject(stop);
	        
	        int x = (int) this.input.readObject();
	        
			primeCounts[tnum] = x;
		} catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
	}
}
