package com.ag777.util.lang.socket.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 客户端请求处理类,socket服务端配套
 * 
 * 
 * @author ag777
 * @version create on 2018年05月30日,last modify at 2020年12月21日
 */
public abstract class Handler {

	protected void write(String msg, PrintWriter out) {
		out.println(msg);
		out.close();
	}
	
	/*====子类选择重写部分=============*/
	public Charset getCharset() {
		return StandardCharsets.UTF_8;
	}

	/**
	 * 从消息流中读取一条消息,默认一行为一条消息
	 * @param in in
	 * @return
	 * @throws IOException IOException
	 */
	public String readMsg(BufferedReader in) throws IOException {
		return in.readLine();
	}
	
	/**
	 * 接收消息后会继续等待下一个消息
	 * @param msg msg
	 * @return
	 */
	public boolean hasNext(String msg) {
		return true;
	}
	
	/**
	 * 服务端建立时执行
	 * @param port port
	 */
	public void onSeverCreate(int port) {
	}
	
	/**
	 * 服务端销毁时执行
	 * @param port port
	 */
	public void onServerDispose(int port) {
	}
	
	/**
	 * 连接建立时执行,实际上就是请求过滤
	 * <p>
	 * 如果需要立即关闭连接,return false;
	 * </p>
	 * 
	 * @param socket socket
	 * @param sessionId sessionId
	 * @return
	 */
	public boolean onPreConnect(Socket socket, String sessionId) {
		return true;
	}
	
	/**
	 * 连接建立后执行
	 * <p>
	 * 该方法后于onPreConnect()方法执行
	 * </p>
	 * @param socket socket
	 * @param sessionId sessionId
	 */
	public void onConnect(Socket socket, String sessionId) {
	}
	
	/**
	 * 当捕获到异常时执行
	 * @param socket socket
	 * @param sessionId sessionId
	 * @param t t
	 */
	public void onErr(Socket socket, String sessionId, Throwable t) {
		t.printStackTrace();
	}
	
	/**
	 * 服务端本身发生异常时执行
	 * @param ex ex
	 */
	public void onServerErr(Exception ex) {
		ex.printStackTrace();
	}
	
	/**
	 * 断开连接时执行
	 * @param socket socket
	 * @param sessionId sessionId
	 */
	public void onDisConnect(Socket socket, String sessionId) {
	}
	
	/*====子类选择重写部分 结束=============*/
	/**
	 * 
	 * @param msg 客户端发上来的消息
	 * @param sessionId 当前对话的session编号
	 * @return 返回信息
	 */
	public abstract String handle(String msg, String sessionId, PrintWriter out) throws InterruptedException ;
}