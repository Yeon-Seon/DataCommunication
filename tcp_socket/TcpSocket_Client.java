package task1_tcp_socket;

import java.net.Socket;

public class TcpSocket_Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Socket sock = new Socket("120.0.0.1", 10001);		// 클라이언트 소캣 생성 new Socekt(server IP, server port);
			
			Send_thread send = new Send_thread(sock);			
			Recieve_thread recieve = new Recieve_thread(sock);
			
			send.start();
			recieve.start();
		}
		catch(Exception e) {
			System.out.println(e);
		}

	}
}
