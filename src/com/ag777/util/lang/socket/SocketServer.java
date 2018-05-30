package com.ag777.util.lang.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import com.ag777.util.lang.IOUtils;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.collection.MapUtils;
import com.ag777.util.lang.socket.model.Handler;
import com.ag777.util.lang.socket.model.Session;

/**
 * socket服务端工具类
 * 
 * 
 * @author ag777
 * @version create on 2018年05月30日,last modify at 2018年05月30日
 */
public class SocketServer {

	private ServerSocket server;
	private int port;
	private boolean isRunning;
	
	private Map<String, Session> sessionMap;
	
	public ServerSocket getServer() {
		return server;
	}

	public int getPort() {
		return port;
	}

	public Map<String, Session> getSessionMap() {
		return sessionMap;
	}

	/**
	 * 停止监听客户端请求
	 * <p>
	 * 注意，服务端并不是马上销毁，需要等待轮询结束
	 * </p>
	 */
	public void dispose() {
		isRunning = false;
	}
	
	public SocketServer(ServerSocket server, Handler handler) {
		this.server = server;
		this.port = server.getLocalPort();
		sessionMap = MapUtils.newHashTable();
		isRunning = true;
		handler.onSeverCreate(port);
		
		while(isRunning){
			try {
	            Socket socket = server.accept();
	            String sessionId = StringUtils.uuid();
	            Session session = new Session(sessionId, socket);
	            sessionMap.put(session.getId(), session);
	            /*
	             *连接建立判断是否需要断开连接 
	             */
	            if(!handler.onPreConnect(socket, sessionId)) {
	            	IOUtils.close(socket);
	            	continue;
	            }
	            
	            /*
	             * 连接建立后执行
	             */
	            handler.onConnect(socket, sessionId);
	            
	            /*
	             * 允许本次连接新建线程执行处理客户端传来的信息
	             */
	            Thread sessionThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							session.handle(handler);
						} catch (IOException ex) {
							handler.onErr(socket, sessionId, ex);
						} catch(Exception ex) {
							handler.onErr(socket, sessionId, ex);
						} finally {
							sessionMap.remove(sessionId);
							handler.onDisConnect(socket, sessionId);
						}
					}
				});
	            sessionThread.start();
	            
			} catch(Exception ex) {
				handler.onServerErr(ex);
			}
        }
		
		IOUtils.close(server);
		handler.onServerDispose(port);
	}
	
	public static SocketServer build(int port, Handler handler) throws IOException {
		ServerSocket server=new ServerSocket(port);
		return new SocketServer(server, handler);
	}
	
	public static void main(String[] args) throws IOException {
		build(1523, new Handler() {
			
			@Override
			public boolean onPreConnect(Socket socket, String sessionId) {
				System.out.println("连接中:"+sessionId);
				return false;
			}
			
			@Override
			public void onConnect(Socket socket, String sessionId) {
				System.out.println("建立:"+sessionId);
			}
			
			@Override
			public void onDisConnect(Socket socket, String sessionId) {
				super.onDisConnect(socket, sessionId);
				System.out.println("断开:"+sessionId);
			}
			
			@Override
			public String handler(String msg) {
				return "得到的消息"+msg;
			}
		});
		System.out.println("服务端建立");
	}
	
}
