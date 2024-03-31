package ex7;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.HashMap;
import java.util.HashSet;

public class ServerThread extends Thread {
    private Socket socket;
	private static int id = 0;
	private static HashMap<Integer, Socket> connex = new HashMap<>();

	private int currentId;
    ServerThread(Socket socket) {
		this.currentId = id;
		this.socket = socket;
		connex.put(currentId, socket);
		++id;
    }
    public void run() {
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.writeInt(currentId);
			out.flush();
			//create a thread that continuously sends PING to the client
			ExecutorService es= Executors.newSingleThreadExecutor();
			DataOutputStream finalOut = out;
			es.submit(()->{
				while(true){
					// finalOut.writeUTF("PING");
					//finalOut.flush();
					// sleep(5000);
				}
			});
			es.shutdown();
			while(true){
				String incoming = in.readUTF();
				int index = incoming.indexOf(':');
				String newIncome = incoming.substring(index + 1);
				
				for(int i = 0; i < connex.size(); ++i)
				{
					//In my opinion, would've been better to only send it to the ppl with matching ID
					//But the problem asks to send to all the ppl
					out = new DataOutputStream(new BufferedOutputStream(connex.get(i).getOutputStream()));
					out.writeUTF(newIncome);
					out.flush();

				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
}






