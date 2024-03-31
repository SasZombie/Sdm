package ex7;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerMult {

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(2999);
			while (true) {
				Socket socket = serverSocket.accept();
				ServerThread sw = new ServerThread(socket);
				sw.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
