package com.ag777.util.lang.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;

import com.ag777.util.lang.IOUtils;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.collection.MapUtils;
import com.ag777.util.lang.interf.Disposable;
import com.ag777.util.lang.socket.model.Handler;
import com.ag777.util.lang.socket.model.Session;

/**
 * socket服务端工具类
 * 
 * 
 * @author ag777
 * @version create on 2018年05月30日,last modify at 2018年11月22日
 */
public class SocketServer implements Disposable {

	private ServerSocket server;
	private int port;
	private boolean isRunning;
	
	private Map<String, Session> sessionMap;
	private ThreadGroup tg;

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
	 * 
	 */
	@Override
	public void dispose() {
		isRunning = false;
		try {
			server.close();
			tg.interrupt();
		} catch (IOException e) {
//			e.printStackTrace();
		}



	}
	
	public SocketServer(ServerSocket server, Handler handler) {
		this.server = server;
		this.port = server.getLocalPort();
		sessionMap = MapUtils.newConcurrentHashMap();
		isRunning = true;
		handler.onSeverCreate(port);
		tg = new ThreadGroup("socket-server");
		
		while(isRunning){
			try {
	            Socket socket = server.accept();
	            String sessionId = StringUtils.uuid();
	            Session session = new Session(sessionId, socket);
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
	            Thread sessionThread = new Thread(tg, () -> {
					try {
						session.handle(handler);
					} catch(InterruptedException ex) {
						//中断
					} catch (Throwable ex) {
						handler.onErr(socket, sessionId, ex);
					} finally {
						sessionMap.remove(sessionId);
						handler.onDisConnect(socket, sessionId);
					}
				});

				sessionMap.put(session.getId(), session);
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
				return true;
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
			public String handler(String msg, String sessionId) {
				return "得到的消息"+msg;
			}
			
			@Override
			public boolean hasNext(String msg) {
				return !"结束".equals(msg);
			}
		});
		System.out.println("服务端建立");
	}
	
}
