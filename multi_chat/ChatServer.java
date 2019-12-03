package multi_chat;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ChatServer {
	
	int port = 6000;
	ServerSocket server = null;
	Socket child = null;
	HashMap<String, PrintWriter> hm;
	
	public ChatServer() {
		ChatServerThread sr;
		Thread t;
		try {
			server = new ServerSocket(port);
			System.out.println("*********************************");
			System.out.println("*       채팅 서버           *");
			System.out.println("*********************************");
			System.out.println("클라이언트의 접속을 기다립니다.");
			hm = new HashMap<String, PrintWriter>();
			while(true) {
				child = server.accept();
				if(child != null) {
					sr = new ChatServerThread(child, hm);
					t = new Thread(sr);
					t.start();
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChatServer();
	}

}
