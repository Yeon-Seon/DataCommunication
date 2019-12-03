package task1_tcp_socket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Recieve_thread extends Thread{
	Socket sock;
	public Recieve_thread(Socket sock) {
		this.sock = sock;
	}
	
	public void run() {
		try {
			InputStream in = sock.getInputStream();								// ������ ���ſ� ����� �Է� ��Ʈ�� ��ü�� ����
			BufferedReader br = new BufferedReader(new InputStreamReader(in));	// InputStream -> BufferedReader
			String line;
			while(true) {
				if((line=br.readLine()) != null)								// ���ۿ� ����� ���� ��� �о�ͼ�
					System.out.println(sock.getInetAddress() + ":" +line);		// ��� ���
				if(line.equals("quit")) 
					break;
			}
			br.close();															// ���� �ݾ���
		}
		catch(Exception e) {													// ����ó��
			
		}
	}

}

