package task1_tcp_socket;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpSocket_Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ServerSocket server = new ServerSocket(10001);						// 서버 소켓 생셩 new ServerSocket(port);			
			System.out.println("Waiting Connect..");
			
			Socket sock = server.accept();										// 클라이언트로부터 요청이 들어오면 연결을 맺고 클리아이너트 소켓을 생성해 리턴.
			InetAddress inetaddr = sock.getInetAddress();						// 소켓에 접속한 클라이언트의 정보  
			System.out.println(inetaddr.getHostAddress()+ " 로부터 접속하였습니다.");
			
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

