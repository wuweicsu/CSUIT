package com.zf.s13;//创建一个包
import java.io.*;//引入类
import java.net.*;
class Client {//Socket客户端
	private String host;//IP地址（域名）
	private int port;//端口号
	public Client(String host, int port) {//带参数构造方法进行初始化
		this.host = host;
		this.port = port;
		connectSocket();//调用连接方法
	}
	public void connectSocket() {//连接方法
		try {
			Socket socketConn;//声明Socket连接
			if (host.equals("localhost") || host.equals("127.0.0.1")) {//判断IP地址(域名)如果是本机localhost
				socketConn = new Socket(InetAddress.getLocalHost(), port);//创建本地连接
			} else {
				socketConn = new Socket(InetAddress.getByName(host), port);//创建远程连接
			}
			BufferedReader stdin = new BufferedReader(new InputStreamReader(
					System.in));// 获得从键盘输入的流
			PrintWriter out = new PrintWriter(socketConn.getOutputStream(),
					true);// 获得服务器写内容的数据流
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socketConn.getInputStream()));// 获得接收服务器发送内容的缓冲流
			System.out.println("服务器信息："+in.readLine());//从服务器获得信息
			System.out.println("服务器信息：" + in.readLine());
			System.out.print("请输入>");//用户输入
			boolean done = false;
			while (!done) {
				String line = stdin.readLine();//获得从键盘输入的每行字符
				out.println(line);//发送到服务端
				if (line.equalsIgnoreCase("exit"))//读到exit则结束循环
					done = true;
				String info = in.readLine();// 从服务器读取字符串
				System.out.println("服务器信息：" + info);// 显示从服务器发送来的数据
				if (!done)//用户输入
					System.out.print("请输入>");
			}
			socketConn.close();//关闭资源
		} catch (SecurityException e) {//捕获安全性错误时引发的异常
			System.out.println("连接服务器出现安全问题！");
		} catch (IOException e) {//捕获IO流异常
			System.out.println("连接服务器出现I/O错误！");
		}
	}
}
public class TextSocketClient {//操作Socket客户端类
	public static void main(String[] args) {//java程序主入口处
		try {
			new Client("localhost", 8080);//IP地址为本机,端口为80
		} catch (Exception e) {//捕获异常
			System.out.println("测试客户端连接出错："+e.getMessage());
		}
	}
}
