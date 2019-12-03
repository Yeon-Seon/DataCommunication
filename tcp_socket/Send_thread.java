package task1_tcp_socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Send_thread extends Thread{
	Socket sock;
	public Send_thread(Socket sock) {
		this.sock = sock;
	}
	
	
	public void run() {
		try {
			OutputStream out = sock.getOutputStream();											// ������ �۽ſ� ����� ��� ��Ʈ�� ��ü�� ����
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));						//OutputStream -> PrintWriter
			String line;
			BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));		// ������ �Է��� ���� ����
			
			while(true) {
				if((line=keyboard.readLine()) != null) {										// �Էµ� �����͸� 
					pw.println(line);															// ��� ��� ��Ʈ���� ����	
					pw.flush();
				}
				if(line.equals("quit"))
					break;
			}
			
			pw.close();																			// ��Ʈ�� �ݾ���
		}
		catch(IOException e) {																	// ����ó��
			e.printStackTrace();
		}
	}

}
