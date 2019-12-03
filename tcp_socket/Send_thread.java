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
			OutputStream out = sock.getOutputStream();											// 데이터 송신에 사용할 출력 스트림 객체를 리턴
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));						//OutputStream -> PrintWriter
			String line;
			BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));		// 데이터 입력을 위한 버퍼
			
			while(true) {
				if((line=keyboard.readLine()) != null) {										// 입력된 데이터를 
					pw.println(line);															// 모두 출력 스트림에 써줌	
					pw.flush();
				}
				if(line.equals("quit"))
					break;
			}
			
			pw.close();																			// 스트림 닫아줌
		}
		catch(IOException e) {																	// 예외처리
			e.printStackTrace();
		}
	}

}
