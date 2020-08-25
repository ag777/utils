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
import com.ag777.util.lang.interf.Disposable;

/**
 * socket客户端工具类
 * 
 * 
 * @author ag777
 * @version create on 2018年05月30日,last modify at 2020年08月25日
 */
public class SocketClient implements Disposable {

	private Socket socket;
	private PrintWriter out = null;
	private BufferedReader in = null;
	
	public SocketClient(Socket socket, OutputStream out, InputStream in) {
		this.socket = socket;
		this.out = new PrintWriter(out);
		this.in = new BufferedReader(new InputStreamReader(in));
	}

	/**
	 * 关闭连接,释放资源
	 */
	@Override
	public void dispose() {
		IOUtils.close(out); // 关闭Socket输出流
		IOUtils.close(in); // 关闭Socket输入流
		IOUtils.close(socket); // 关闭Socket
		out = null;
		in = null;
		socket = null;
	}
	
	/**
	 * 创建套接字
	 * @param ip
	 * @param port
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static SocketClient build(String ip, int port) throws UnknownHostException, IOException {
		Socket s = new Socket(ip, port);
		return new SocketClient(s, s.getOutputStream(), s.getInputStream());
	}
	
	/**
	 * 接收消息(行)
	 * @return
	 * @throws IOException
	 */
	public String readLine() throws IOException {
		return in.readLine();
	}
	
	/**
	 * 发送消息
	 * @param msg
	 * @throws IOException
	 */
	public void sendMsg(String msg) throws IOException {
		out.println(msg);
		out.flush();
	}
	
	/**
	 * 发送一条消息，返回紧接着接收到的下一条消息
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	public String sendMsgForReply(String msg) throws IOException {
		sendMsg(msg);
		return readLine();
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		SocketClient client = build("127.0.0.1", 1523);
		System.out.println(client.sendMsgForReply("123"));
		System.out.println(client.sendMsgForReply("结束"));
		System.out.println(client.sendMsgForReply("456"));
		client.dispose();
	}
}
