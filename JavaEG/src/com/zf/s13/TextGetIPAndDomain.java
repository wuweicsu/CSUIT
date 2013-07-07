package com.zf.s13;//����һ����
import java.net.InetAddress;//������
import java.net.UnknownHostException;
public class TextGetIPAndDomain {//������ȡIP��ַ����������
	public static void getLocalIP() {//��ȡ������IP��ַ
		try {
			InetAddress addr = InetAddress.getLocalHost();//������������IP��ַ����
			String hostAddr=addr.getHostAddress();//��ȡIP��ַ
			String hostName=addr.getHostName();//��ȡ���ػ�����
			System.out.println("����IP��ַ��" +hostAddr);
			System.out.println("���ػ�������" +hostName);
		} catch (UnknownHostException e) {//����δ֪�����쳣
			System.out.println("���ܻ������IP��ַ��"+e.getMessage());
			System.exit(1);
		}
		
	}
	public static void getIPByName(String hostName){//�����������������IP��ַ
		InetAddress addr;
		try {
			addr = InetAddress.getByName(hostName);//������������������ַ����
			String hostAddr=addr.getHostAddress();//��ȡ����IP��ַ
			System.out.println("����Ϊ��"+hostName+"������IP��ַ����"+hostAddr);
		} catch (UnknownHostException e) {//����δ֪�����쳣
			System.out.println("���ܸ���������ȡ����IP��ַ��"+e.getMessage());
			System.exit(1);
		}
	}
	public static void getAllIPByName(String hostName){//������������������е�IP��ַ
		InetAddress[] addrs;
		try {
			addrs = InetAddress.getAllByName(hostName);//������������������ַ����
			String[] ips = new String[addrs.length];
			System.out.println("����Ϊ"+hostName+"���������е�IP��ַΪ��");
			for (int i = 0; i < addrs.length; i++) {
				ips[i] = addrs[i].getHostAddress();//��ȡ����IP��ַ
				System.out.println(ips[i]);
			}
		} catch (UnknownHostException e) {//����δ֪�����쳣
			System.out.println("���ܸ���������ȡ��������IP��ַ��"+e.getMessage());
			System.exit(1);
		}
		
	}
	public static void main(String[] args) {//java��������ڴ�
		getLocalIP();//���÷�����ñ�����IP��ַ
		String hostName="www.sohu.com";//�Ѻ�����
		getIPByName(hostName);//��ȡ�Ѻ�������IP��ַ
		getAllIPByName(hostName);//��ȡ�Ѻ������������е�IP��ַ
	}
}
