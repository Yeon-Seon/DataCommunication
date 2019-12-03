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
			InputStream in = sock.getInputStream();								// 데이터 수신에 사용할 입력 스트림 객체를 리턴
			BufferedReader br = new BufferedReader(new InputStreamReader(in));	// InputStream -> BufferedReader
			String line;
			while(true) {
				if((line=br.readLine()) != null)								// 버퍼에 저장된 값을 모두 읽어와서
					System.out.println(sock.getInetAddress() + ":" +line);		// 모두 출력
				if(line.equals("quit")) 
					break;
			}
			br.close();															// 버퍼 닫아줌
		}
		catch(Exception e) {													// 예외처리
			
		}
	}

}

