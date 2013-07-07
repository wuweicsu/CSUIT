package com.zf.s13;//创建一个包
import java.net.InetAddress;//引入类
import java.net.UnknownHostException;
public class TextGetIPAndDomain {//操作获取IP地址和域名的类
	public static void getLocalIP() {//获取本机的IP地址
		try {
			InetAddress addr = InetAddress.getLocalHost();//创建本地主机IP地址对象
			String hostAddr=addr.getHostAddress();//获取IP地址
			String hostName=addr.getHostName();//获取本地机器名
			System.out.println("本地IP地址：" +hostAddr);
			System.out.println("本地机器名：" +hostName);
		} catch (UnknownHostException e) {//捕获未知主机异常
			System.out.println("不能获得主机IP地址："+e.getMessage());
			System.exit(1);
		}
		
	}
	public static void getIPByName(String hostName){//根据域名获得主机的IP地址
		InetAddress addr;
		try {
			addr = InetAddress.getByName(hostName);//根据域名创建主机地址对象
			String hostAddr=addr.getHostAddress();//获取主机IP地址
			System.out.println("域名为："+hostName+"的主机IP地址：　"+hostAddr);
		} catch (UnknownHostException e) {//捕获未知主机异常
			System.out.println("不能根据域名获取主机IP地址："+e.getMessage());
			System.exit(1);
		}
	}
	public static void getAllIPByName(String hostName){//根据域名获得主机所有的IP地址
		InetAddress[] addrs;
		try {
			addrs = InetAddress.getAllByName(hostName);//根据域名创建主机地址对象
			String[] ips = new String[addrs.length];
			System.out.println("域名为"+hostName+"的主机所有的IP地址为：");
			for (int i = 0; i < addrs.length; i++) {
				ips[i] = addrs[i].getHostAddress();//获取主机IP地址
				System.out.println(ips[i]);
			}
		} catch (UnknownHostException e) {//捕获未知主机异常
			System.out.println("不能根据域名获取主机所有IP地址："+e.getMessage());
			System.exit(1);
		}
		
	}
	public static void main(String[] args) {//java程序主入口处
		getLocalIP();//调用方法获得本机的IP地址
		String hostName="www.sohu.com";//搜狐域名
		getIPByName(hostName);//获取搜狐的主机IP地址
		getAllIPByName(hostName);//获取搜狐域名主机所有的IP地址
	}
}
