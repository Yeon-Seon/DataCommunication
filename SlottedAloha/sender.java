package Aloha;
/*
	Program for Slotted Aloha Simulation

	Written by Rahul Kejriwal
	Modified by Sang-woo Lee
*/

import java.util.*;
import java.io.*;
import java.lang.*;

import java.util.concurrent.ConcurrentLinkedQueue;

/*
	Class for modelling a single node in a Slotted Aloha MAC based framework
*/
class Node{
	// Node Parameters
	// 각각의 노드가 갖는 파라매터들
	int cw; // CW
	boolean mach_friendly;
	int max_retry_attempts;
	
	// Internal State Vars
	int buffer_count;
	int backoff_ctr;
	int pkt_delay_1;
	int pkt_delay_2;
	int retry_attempts;
	boolean backlogged;

	// Construct new node
	Node(int def_cw, int mra, boolean mf){
		
		// Initialize node params
		cw = def_cw;
		mach_friendly = mf;
		max_retry_attempts = mra;

		// Initialize internal state vars
		buffer_count = 0;
		backoff_ctr = 0;
		pkt_delay_1 = 0;
		pkt_delay_2 = 0;
		retry_attempts = 0;
		backlogged = false;
	}

	// Generate new pkt
	void gen_pkt(){
		if(buffer_count == 2)
			return;

		buffer_count ++;
		backlogged = true;		
	}

	/*
		Returns 1 if wants to xmit
		Returns 0 for pass
	*/
	int slot_action(){
		if(!backlogged)
			return 0;

		pkt_delay_1 ++;
		if(buffer_count == 2)
			pkt_delay_2 ++;

		if(backoff_ctr == 0)
			return 1;
		else{
			backoff_ctr --;
			return 0;
		}
	}

	// Update state with Succesful Xmission
	int update_success(){
		int delay = pkt_delay_1;
		cw = (int) Math.max(2, cw * 0.75);
		
		buffer_count --;
		
		if(buffer_count == 0)
			backlogged = false;
		else
			backlogged = true;
		if(buffer_count == 1) {
			pkt_delay_1 = pkt_delay_2;
			pkt_delay_2 = 0;
		}
		else {
			pkt_delay_1 = 0;
			pkt_delay_2 = 0;
		}
		retry_attempts = 0;
		backoff_ctr = 0;
		
		return delay;
	}

	// Update state with Failed Xmission
	boolean update_fail(){
		backoff_ctr = new Random().nextInt(cw);
		cw = Math.min(256, cw * 2);
		
		if(retry_attempts == max_retry_attempts) {
			if(!mach_friendly)
				System.out.println("Exceeded 10 retries for node!");
			return true;
		}
		
		retry_attempts ++;
		return false;
	}
}

/*
	Class for simulating Slotted Aloha for N users
*/
class SlottedAlohaSender{
	// Simulation Params 
	int num_users; // 노드의 갯수
	int def_cw; // CW 파라메터
	double pkt_gen_rate; // 패킷 생성률
	int max_pkts; // 최대로 생산하는 패킷 갯수

	// Xmitter Nodes
	Node[] nodes; // 노드를 저장할 리스트

	// Print machine friendly?
	boolean mach_friendly; // 그래프를 그릴때 쓰는 기능을 트리거링 함

	SlottedAlohaSender(int n, int dcw, double pgr, int mp, int mra, boolean mf){ // 생성자
		num_users = n;
		def_cw	  = dcw;
		pkt_gen_rate = pgr;
		max_pkts     = mp;
		mach_friendly = mf;
		// 인자값을 셋업
		
		
		nodes = new Node[num_users]; // 입력받은 노드의 갯수 크기만큼 객체 배열 생성
		for(int i=0; i < num_users; i++)
			nodes[i] = new Node(def_cw, mra, mach_friendly); // 노드 객체 성성해서 선언한 배열에 넣어주기

		run_simulation();
	}

	boolean genBoolWithProb(){
		return Math.random() < pkt_gen_rate;
	}

	void run_simulation(){
		int SimTime = 0; // 시뮬레이션 타임 초기화 초 단위
		int num_pkts_xmitted = 0; // 전송에 성공한 패킷 갯수
		int total_delay_time = 0; // 전송에 성공한 패킷이 전송까지 걸린 시간

		while(num_pkts_xmitted < max_pkts){ // 전송에 성공한 패킷 갯수가 max_pkts보다 크면 프로세스 종료

			// Advance Simulation Time
			SimTime ++; // 1초 돌때마다 SimTime 늘림

			// Packet Generation Phase
			for(Node node: nodes){
				if(genBoolWithProb())
					node.gen_pkt(); // 각 노드에 정의된 패킷 생성 메쏘드 실행
			}

			// Attempt Transmission Phase
			int num_attempted = 0; // 전송시도하는 횟수를 카운트하는 변수

			// Do Slot Actions
			ArrayList<Node> attemptors = new ArrayList<Node>();
			for(Node node: nodes){
				if(node.slot_action() == 1) // slot_action은 각 노드가 전송을 시도할지 말지를 0 혹은 1로 반환함. 만약 1이면 전송을 시도하는 노드임
					attemptors.add(node); // 전송을 시도하는 노드를 리스트에 담음
			}

			// Check for collisions and update accordingly
			if(attemptors.size() == 1){ // 전송을 시도하는 노드가 하나밖에 없으면 그 노드는 전송 성공
				num_pkts_xmitted ++; // 전송 성공한 패킷 갯수 추가
				total_delay_time += attemptors.get(0).update_success(); // update access 함수를 통해 전송된 패킷이 생성된 시점부터 지금까지 걸린시간 누적계산
			}
			else{
				for(Node node: attemptors)
					if(node.update_fail()){
						// Print human-readable
						if(!mach_friendly){
							System.out.println("\nQuitting after " + Integer.toString(num_pkts_xmitted) + " pkts\n");
							System.out.println("N: " + Integer.toString(num_users) 
								+ ", W: " + Integer.toString(def_cw)
								+ ", P: " + Double.toString(pkt_gen_rate)
								+ ", Utilization: " + Double.toString( (num_pkts_xmitted*1.0)/SimTime )
								+ ", Average Packet Delay: " + Double.toString( (total_delay_time*1.0)/num_pkts_xmitted ));				
						}
						// Print machine-friendly
						else{
							System.out.println(num_pkts_xmitted);
							System.out.println(num_users);
							System.out.println(def_cw);
							System.out.println(pkt_gen_rate);
							System.out.println((num_pkts_xmitted*1.0)/SimTime);
							System.out.println((total_delay_time*1.0)/num_pkts_xmitted);
						}
						System.exit(0);							
					}
			}
		}

		// Print human-readable
		if(!mach_friendly){
			System.out.println("노드 수: " + Integer.toString(num_users) + ", 시뮬레이션 시간:" + Integer.toString(SimTime) +"slots"
				+ ", CW: " + Integer.toString(def_cw)
				+ ", 패킷 생성률: " + Double.toString(pkt_gen_rate)
				+ ", 사용률: " + Double.toString( (num_pkts_xmitted*1.0)/SimTime )
				+ ", 평균패킷 딜래이: " + Double.toString( (total_delay_time*1.0)/num_pkts_xmitted ));				
		}
		// Print machine-friendly
		else{
			System.out.println(-1);
			System.out.println(num_users);
			System.out.println(def_cw);
			System.out.println(pkt_gen_rate);
			System.out.println((num_pkts_xmitted*1.0)/SimTime);
			System.out.println((total_delay_time*1.0)/num_pkts_xmitted);
		}
		System.exit(0);
	}
}

/*
	Main Class:
		Parse cmd line args and start simulator
*/
public class sender{
	static void errorExit(String s){
		System.out.println("Error: " + s);
		System.exit(1);
	}

	public static void main(String[] args){
		// Protocol Params
		int num_users 	 = 20;  // 노드의 갯수
		int def_cw		 = 3; // CW 파라미터
		double pkt_gen_rate = 0.6; // 패킷 생성률
		int max_pkts	 = 400; // 시뮬레이션 동안 전송할 패킷 갯수
		int max_retry_attempts = 10;

		// machine friendly op modes
		boolean mach_friendly = false;

		// Process Command Line Args
		int next_arg = 0;
		for(String arg: args){
			if(next_arg == 0){
				if(arg.equals("-N"))
					next_arg = 3;
				else if(arg.equals("-W"))
					next_arg = 4;
				else if(arg.equals("-p"))
					next_arg = 5;
				else if(arg.equals("-M"))
					next_arg = 6;
				else if(arg.equals("-r"))
					next_arg = 7;
				else if(arg.equals("-m"))
					mach_friendly = true;
				else
					errorExit("Incorrect Usage!");
			}
			else{
				switch(next_arg){			
					case 3: num_users = Integer.parseInt(arg);
							break;

					case 4: def_cw = Integer.parseInt(arg);
							break;

					case 5: pkt_gen_rate = Double.parseDouble(arg);
							break;

					case 6: max_pkts = Integer.parseInt(arg);
							break;

					case 7: max_retry_attempts = Integer.parseInt(arg);
							break;

					default: errorExit("Incorrect Usage!");
				}
				next_arg = 0;
			}
		}

		// Start simulator
		SlottedAlohaSender s = new SlottedAlohaSender(num_users, def_cw, pkt_gen_rate, max_pkts, max_retry_attempts, mach_friendly);
	}
}
