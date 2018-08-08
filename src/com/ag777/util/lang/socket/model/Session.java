package com.ag777.util.lang.socket.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.ag777.util.lang.IOUtils;

/**
 * 客户端请求处理类,socket服务端配套
 * 
 * 
 * @author ag777
 * @version create on 2018年05月30日,last modify at 2018年05月30日
 */
public class Session {

	private String id;
	private Socket socket;
	
	public Session(String id, Socket socket) {
		this.id = id;
		this.socket = socket;
	}

	public String getId() {
		return id;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void handle(Handler handler) throws IOException {
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        out = new PrintWriter(socket.getOutputStream());
	        while(true) {
        		String msg = handler.readMsg(in);	//收到的信息
	        	if(msg == null) {
	        		break;
	        	}
        		String outMsg = handler.handler(msg, id);	//准备回复的信息
        		out.println(outMsg);
        		out.flush();
        		if(!handler.hasNext(msg)) {
        			break;
        		}
	        }
		} catch(Exception ex) {
    		throw ex;
    	} finally {
    		IOUtils.close(in);
    		IOUtils.close(out);
    		socket.close();	//断开连接
    	}
		System.out.println("通信结束:"+id);
	}
	
}
