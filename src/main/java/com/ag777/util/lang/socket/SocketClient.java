package com.ag777.util.lang.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
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

	public Socket getSocket() {
		return socket;
	}

	/**
	 * 重连
	 * @throws IOException 连接异常
	 */
	public void reconnect() throws IOException {
		reconnect(0);
	}

	/**
	 * 重连
	 * @param timeout 连接超时
	 * @throws IOException 连接异常
	 */
	public void reconnect(int timeout) throws IOException {
		IOUtils.close(socket);
		Socket socketTemp = new Socket();
		socketTemp.connect(socket.getRemoteSocketAddress());
		this.socket = socketTemp;
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new PrintWriter(socket.getOutputStream());
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
	 * @param ip ip
	 * @param port port
	 * @return SocketClient
	 * @throws UnknownHostException UnknownHostException
	 * @throws IOException IOException
	 */
	public static SocketClient build(String ip, int port) throws UnknownHostException, IOException {
		return build(ip, port, 0);
	}

	/**
	 *
	 * @param ip ip
	 * @param port port
	 * @param timeout 超时
	 * @return SocketClient
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static SocketClient build(String ip, int port, int timeout) throws UnknownHostException, IOException {
		Socket s = new Socket();
		SocketAddress endpoint = new InetSocketAddress(ip, port);
		s.connect(endpoint, timeout);
		return new SocketClient(s, s.getOutputStream(), s.getInputStream());
	}
	
	/**
	 * 接收消息(行)
	 * @return
	 * @throws IOException IOException
	 */
	public String readLine() throws IOException {
		return in.readLine();
	}
	
	/**
	 * 发送消息
	 * @param msg msg
	 * @throws IOException IOException
	 */
	public void sendMsg(String msg) throws IOException {
		out.println(msg);
		out.flush();
	}
	
	/**
	 * 发送一条消息，返回紧接着接收到的下一条消息
	 * @param msg msg
	 * @return
	 * @throws IOException IOException
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