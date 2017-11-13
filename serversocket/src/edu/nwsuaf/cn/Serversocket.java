package edu.nwsuaf.cn;

import java.io.*;
import java.net.*;

/**
 * echo服务器 功能：将客户端发送的内容反馈给客户端
 */
public class Serversocket {
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket socket = null;
		OutputStream os = null;
		InputStream is = null;
		// 监听端口号
		int port = 10000;
		try {
			// 建立连接
			serverSocket = new ServerSocket(port);
			// 获得连接
			socket = serverSocket.accept();
			// 接收客户端发送内容
			is = socket.getInputStream();
			byte[] b = new byte[1024];
			int n = is.read(b);
			System.out.println(serverSocket.getLocalPort());
			// 输出
			System.out.println("客户端发送内容为：" + new String(b, 0, n));
			// 向客户端发送反馈内容
			os = socket.getOutputStream();
			os.write(b, 0, n);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流和连接
				os.close();
				is.close();
				socket.close();
				serverSocket.close();
			} catch (Exception e) {
			}
		}
	}
}