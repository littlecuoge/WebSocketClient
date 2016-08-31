package com.zsq.ioclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class client {
	public static void main(String[] args) {
		for(int i=0;i<200;++i){
			new Thread(new ClientThread(i)).start();
		}
	}
}

class ClientThread implements Runnable {
	int port = 8080;
	Socket socket = null;
	BufferedReader in = null;
	PrintWriter out = null;
	private int id;

	public ClientThread(int id) {
		this.id = id;
	}
	@Override
	public void run() {
		System.out.println("client begin..." + id);
		try {
			socket = new Socket("127.0.0.1", port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println("hello,client id:"+id);
			System.out.println("send to server successfully."+id);
			String resp = in.readLine();
			System.out.println("response is:" + resp+", id:"+id);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				in = null;
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				socket = null;
			}
		}

	}
}
