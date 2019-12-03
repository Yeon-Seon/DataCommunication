package multi_chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

public class ChatServerThread implements Runnable{
	Socket child;
	BufferedReader ois;
	PrintWriter oos;
	
	String user_id;
	HashMap<String, PrintWriter> hm;
	InetAddress ip;
	String ms;
	
	public ChatServerThread(Socket s, HashMap<String, PrintWriter> h) {
		child = s;
		hm = h;
		
		try {
			ois = new BufferedReader(new InputStreamReader (child.getInputStream()));
			oos = new PrintWriter(child.getOutputStream());
			
			user_id = ois.readLine();
			ip = child.getInetAddress();
			System.out.println(ip + "로부터 " +user_id+ "님이 접속하셨습니다.");
			broadcast(user_id+"님이 접속하셨습니다.");
			
			 synchronized(hm) {
				 hm.put(user_id, oos);
			 }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		String receiveData;
		
		try {
			while((receiveData = ois.readLine())!= null){
				if(receiveData.equals("/quit")) {
					synchronized(hm) {
						hm.remove(user_id);
					}
					break;
				}
				else if(receiveData.indexOf("/to") >= 0) {
					sendMsg(receiveData);
				}
				else if(receiveData.indexOf("/userList") >= 0) {
					userList(receiveData);
				}
				else if(receiveData.indexOf("/myName") >= 0) {
					userList(receiveData);
				}
				else {	// 기본
					System.out.println(user_id + " >> " + receiveData);
					oos.print("나>>" +receiveData);
					oos.flush();
					broadcast(user_id+ " >> " +receiveData);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 한명의 유저 입력값을 나머지 유저들의 outputStream에 저장(Iterator)
	public void broadcast(String message) {		// *젤중요*
		synchronized(hm) {
			try {
				Iterator<String> keys = hm.keySet().iterator();	// Iterator = for문
				while(keys.hasNext()) {
					String key = keys.next();
					if(!key.equals(user_id)) {
						PrintWriter temp_oss = hm.get(key);
						temp_oss.println(message);
						temp_oss.flush();
					}
				}
			}catch(Exception e) { 
				
			}
		}
	}
	
	public void sendMsg(String message) {
		int senderID_head = message.indexOf(" ") +1;
		int senderID_tail = message.indexOf(" ", senderID_head);
		
		if(senderID_tail != -1) {
			String id = message.substring(senderID_head, senderID_tail);
			String msg = message.substring(senderID_tail+1);
			PrintWriter oos_whisper = hm.get(id);
			try {
				if(oos_whisper != null) {
					oos_whisper.println(user_id+"님이 다음과 같은 귓속말을 보냇습니다. : " +msg);
					oos_whisper.flush();
				}
			}catch(Exception e){
				
			}
		}
		
		
	}
	
	public void userList(String message) {
		try {
			for(String key : hm.keySet()) {
				oos.print(key);
				oos.flush();
			}
		}
		catch(Exception e) {
			
		}
	}
	
	public void myName(String message) {
		try {
			oos.print("내 이름 : " +user_id);
			oos.flush();
		}
		catch(Exception e) {
			
		}
	}
	

}
