package com.zf.s13;//创建一个包
import java.io.*;//引入类
import java.net.*;
class SocketServer {//Socket服务器端
	private int port;//端口
	public SocketServer(int port) {//带参数的构造方法进行初始化
		this.port = port;
		start();//调用启动服务器端的方法
	}
	public String infoUpperCase(String line) {//处理信息
		return line.toUpperCase();//将字符串大写
	}
	public void start() {//启动服务器端
		try {
			ServerSocket serverSocket = new ServerSocket(port);//根据端口创建服务器端Socket对象
			System.out.println("服务器已启动，监听端口号为：" + port);// 显示连接信息
			System.out.println("正在等待客户端连接.........");
			Socket socketAccept = serverSocket.accept();//挂起等待客户的请求
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socketAccept.getInputStream()));// 获取读取客户端的数据流
			PrintWriter out = new PrintWriter(socketAccept.getOutputStream(),
					true);// 获取写往客户端的数据输出流,true:自动刷新
			out.println("服务器端连接成功.........");//向客户发送连接信息
			out.println("输入exit断开与服务器的连接");
			boolean done = false;
			while (!done) {
				String line = in.readLine();//读取客户端每行的内容
				if (line == null) {//没有写则不读取
					done = true;
				} else {
					System.out.println("客户端传来的内容：" + line);//显示客户端发送的内容
					String message = infoUpperCase(line);// 信息处理
					out.println("从服务器端口发送的内容：" + message);//向客户端发送信息
					if (line.trim().equals("exit"))//退出判断
						done = true;
				}
			}
			socketAccept.close();// 关闭通信资源
		} catch (Exception e) {//捕获异常
			System.out.println("启动服务器端出现错误："+e.getMessage());
		}
	}
}
public class TextSocketServer {//操作Socket服务器端的类
	public static void main(String[] args) {//java程序主入口处
		try {
			SocketServer server=new SocketServer(8080);//传入端口号实例化对象
		} catch (Exception e) {//捕获异常
			System.out.println("测试服务器端监听出错："+e.getMessage());
		}
	}
}