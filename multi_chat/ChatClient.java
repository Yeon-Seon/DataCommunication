package multi_chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
	String ipAddress;	// 클라이언트의 아이피가 저장될 변수
	static final int port = 6000; // 서버의 포트
	Socket client = null;	
	
	BufferedReader read;
	PrintWriter oos;
	BufferedReader ois;
	String sendData;
	String ReceiveData;
	
	String user_id;
	ReceiveDataThread rt;
	boolean endflag = false;
	
	public ChatClient(String id, String ip) {
		user_id = id;
		ipAddress = ip;
		try {
			System.out.println("*** 클라이언트 ***");
			client = new Socket(ipAddress, port);
			
			read = new BufferedReader(new InputStreamReader(System.in));
			
			ois = new BufferedReader(new InputStreamReader(client.getInputStream()));
			oos = new PrintWriter(client.getOutputStream());
			
			oos.println(user_id);
			oos.flush();
			
			rt = new ReceiveDataThread(client, ois);	// 수신
			Thread t = new Thread(rt);
			t.start();
				
			while(true) {	// 송신
				sendData = read.readLine();
				
				oos.println(sendData);
				oos.flush();
				
				if(sendData.equals("/quit")) {
					endflag = true;
					break;
				}
			}
			System.out.println("클라이언트 접속을 종료합니다. ");
			System.exit(0);
		}
		catch(Exception e) {
			if(!endflag)
				e.printStackTrace();
		}
		finally {
			try {
				ois.close();
				oos.close();
				client.close();
				System.exit(0);
			}catch(IOException e2){
				e2.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new ChatClient("11", "127.0.0.1");
	}
}
