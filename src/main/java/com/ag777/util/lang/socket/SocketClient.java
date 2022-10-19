package com.ag777.util.lang.socket;

import com.ag777.util.lang.IOUtils;
import com.ag777.util.lang.interf.Disposable;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * socket客户端工具类
 * 
 * 
 * @author ag777
 * @version create on 2018年05月30日,last modify at 2022年10月19日
 */
public class SocketClient implements Disposable {

	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;

	public SocketClient(Socket socket, OutputStream out, InputStream in) {
		this.socket = socket;
		this.out = new DataOutputStream(out);
		this.in = new DataInputStream(in);
	}

	/**
	 * 关闭连接,释放资源
	 */
	@Override
	public void dispose() {
		IOUtils.close(out, in, socket); // 关闭Socket输出流, 关闭Socket输入流, 关闭Socket
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
		Socket s = new Socket(ip, port);
		return new SocketClient(s, s.getOutputStream(), s.getInputStream());
	}

	public Socket getSocket() {
		return socket;
	}
	public DataOutputStream getOut() {
		return out;
	}
	public DataInputStream getIn() {
		return in;
	}

	/**
	 *
	 * @param length 读取的长度
	 * @return 字节数组
	 * @throws IOException IOException
	 */
	public byte[] readBytes(int length) throws IOException {
		byte[] bytes = new byte[length];
		read(bytes);
		return bytes;
	}

	/**
	 *
	 * @param bytes 容器
	 * @return 是否已经读取完成
	 * @throws IOException IOException
	 */
	public boolean read(byte[] bytes) throws IOException {
		return in.read(bytes) == -1;
	}

	public byte readByte() throws IOException {
		return in.readByte();
	}

	public int readUnsignedByte() throws IOException {
		return in.readUnsignedByte();
	}

	public int readInt() throws IOException {
		return in.readInt();
	}

	public long readLong() throws IOException {
		return in.readLong();
	}

	public float readFloat() throws IOException {
		return in.readFloat();
	}

	public double readDouble() throws IOException {
		return in.readDouble();
	}

	public short readShort() throws IOException {
		return in.readShort();
	}

	public boolean readboolean() throws IOException {
		return in.readBoolean();
	}


	/**
	 * 接收消息(行)
	 * @return String
	 * @throws IOException IOException
	 */
	@Deprecated
	public String readLine() throws IOException {
		return in.readLine();
	}

	/**
	 * 发送消息
	 * @param msg msg
	 * @param charsets 编码
	 * @throws IOException IOException
	 */
	public SocketClient write(String msg, Charset charsets) throws IOException {
		out.write(msg.getBytes(charsets));
		return this;
	}

	public SocketClient write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
		return this;
	}

	public SocketClient write(byte[] b) throws IOException {
		out.write(b);
		return this;
	}

	public SocketClient writeByte(int v) throws IOException {
		out.writeByte(v);
		return this;
	}

	public SocketClient writeInt(int v) throws IOException {
		out.writeInt(v);
		return this;
	}

	public SocketClient writeLong(long v) throws IOException {
		out.writeLong(v);
		return this;
	}

	public SocketClient writeDouble(double v) throws IOException {
		out.writeDouble(v);
		return this;
	}

	public SocketClient writeFloat(float v) throws IOException {
		out.writeFloat(v);
		return this;
	}

	public SocketClient writeShort(short v) throws IOException {
		out.writeShort(v);
		return this;
	}

	public SocketClient writeBoolean(boolean v) throws IOException {
		out.writeBoolean(v);
		return this;
	}

	public SocketClient flush() throws IOException {
		out.flush();
		return this;
	}

	public static void main(String[] args) throws IOException {
		SocketClient client = build("127.0.0.1", 1523);
		try {
			client.write("123", StandardCharsets.UTF_8).flush();
			System.out.println(client.readLine());
			client.write("结束", StandardCharsets.UTF_8).flush();
			System.out.println(client.readLine());
			client.write("456", StandardCharsets.UTF_8).flush();
			System.out.println(client.readLine());
		} finally {
			client.dispose();
		}

	}
}