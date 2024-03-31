package ex7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.midi.SysexMessage;

public class TCPClientSimple {
	
	public static void main(String[] args) {
		DataInputStream in = null; DataOutputStream out = null;
		Socket socket = null;
		Scanner scan=null;
		int id;

		try {
			socket = new Socket("127.0.0.1", 2999);
			out = new DataOutputStream(socket.getOutputStream());
			out.flush();
			in = new DataInputStream(socket.getInputStream());
			ExecutorService es= Executors.newSingleThreadExecutor();
			DataInputStream finalIn = in;
			id = in.readInt();

			es.submit(()->{
				while(true) {
					String incoming = finalIn.readUTF();
					int i = incoming.indexOf(':');
					String msgId = "";
					for(int ind = 0; ind < i; ++ind)
					{
						msgId = msgId + incoming.charAt(ind);
					}
					if(msgId.isEmpty() )//&& Integer.parseInt(sendId) != id)
						System.out.println(incoming);
					else if(Integer.parseInt(msgId) == id)
						System.out.println(incoming.substring(i + 1));

					
				}
			});
			es.shutdown();
			scan = new Scanner(System.in);
			
			while(true) {
				String s = scan.nextLine();
				if(s.equals("END"))
					break;
				
				String toSend = String.valueOf(id) + ':' +s;
				out.writeUTF(toSend);
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		 finally {
			try {
				scan.close();
				out.close();
				in.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}