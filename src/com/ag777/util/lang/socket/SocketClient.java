package com.ag777.util.lang.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.ag777.util.lang.IOUtils;

/**
 * socket客户端工具类
 * 
 * 
 * @author ag777
 * @version create on 2018年05月30日,last modify at 2018年05月30日
 */
public class SocketClient {

	private Socket socket;
	private PrintWriter out = null;
	private BufferedReader in = null;
	
	public SocketClient(Socket socket, OutputStream out, InputStream in) {
		this.socket = socket;
		this.out = new PrintWriter(out);
		this.in = new BufferedReader(new InputStreamReader(in));
	}

	public void disposed() {
		IOUtils.close(out); // 关闭Socket输出流
		IOUtils.close(in); // 关闭Socket输入流
		IOUtils.close(socket); // 关闭Socket
	}
	
	public static SocketClient build(String ip, int port) throws UnknownHostException, IOException {
		Socket s = new Socket(ip, port);
		return new SocketClient(s, s.getOutputStream(), s.getInputStream());
	}
	

	public String sendMsg(String msg) throws IOException {
		out.println(msg);
		out.flush();
        return in.readLine();
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		SocketClient client = build("127.0.0.1", 1523);
		System.out.println(client.sendMsg("123"));
		client.disposed();
	}
}
