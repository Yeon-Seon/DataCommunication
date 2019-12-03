package task1_tcp_socket;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpSocket_Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ServerSocket server = new ServerSocket(10001);						// ���� ���� ���� new ServerSocket(port);			
			System.out.println("Waiting Connect..");
			
			Socket sock = server.accept();										// Ŭ���̾�Ʈ�κ��� ��û�� ������ ������ �ΰ� Ŭ�����̳�Ʈ ������ ������ ����.
			InetAddress inetaddr = sock.getInetAddress();						// ���Ͽ� ������ Ŭ���̾�Ʈ�� ����  
			System.out.println(inetaddr.getHostAddress()+ " �κ��� �����Ͽ����ϴ�.");
			
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

