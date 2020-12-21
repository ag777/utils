package com.ag777.util.lang.socket.model;

import com.ag777.util.lang.IOUtils;
import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * 客户端请求处理类,socket服务端配套
 * 
 * 
 * @author ag777
 * @version create on 2018年05月30日,last modify at 2020年12月21日
 */
public class Session {

	private String id;
	private Socket socket;
	private Charset charset;
	
	public Session(String id, Socket socket, Charset charset) {
		this.id = id;
		this.socket = socket;
		this.charset = charset;
	}

	public String getId() {
		return id;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void handle(Handler handler) throws IOException, InterruptedException {
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), charset));
	        while(true) {
        		String msg = handler.readMsg(in);	//收到的信息
	        	if(msg == null) {
	        		break;
	        	}
        		String outMsg = handler.handle(msg, id, out);	//准备回复的信息
				if(outMsg != null) {
					out.println(outMsg);
					out.flush();
				}

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
