package com.zf.s13;//����һ����
import java.io.*;//������
import java.net.*;
class Client {//Socket�ͻ���
	private String host;//IP��ַ��������
	private int port;//�˿ں�
	public Client(String host, int port) {//���������췽�����г�ʼ��
		this.host = host;
		this.port = port;
		connectSocket();//�������ӷ���
	}
	public void connectSocket() {//���ӷ���
		try {
			Socket socketConn;//����Socket����
			if (host.equals("localhost") || host.equals("127.0.0.1")) {//�ж�IP��ַ(����)����Ǳ���localhost
				socketConn = new Socket(InetAddress.getLocalHost(), port);//������������
			} else {
				socketConn = new Socket(InetAddress.getByName(host), port);//����Զ������
			}
			BufferedReader stdin = new BufferedReader(new InputStreamReader(
					System.in));// ��ôӼ����������
			PrintWriter out = new PrintWriter(socketConn.getOutputStream(),
					true);// ��÷�����д���ݵ�������
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socketConn.getInputStream()));// ��ý��շ������������ݵĻ�����
			System.out.println("��������Ϣ��"+in.readLine());//�ӷ����������Ϣ
			System.out.println("��������Ϣ��" + in.readLine());
			System.out.print("������>");//�û�����
			boolean done = false;
			while (!done) {
				String line = stdin.readLine();//��ôӼ��������ÿ���ַ�
				out.println(line);//���͵������
				if (line.equalsIgnoreCase("exit"))//����exit�����ѭ��
					done = true;
				String info = in.readLine();// �ӷ�������ȡ�ַ���
				System.out.println("��������Ϣ��" + info);// ��ʾ�ӷ�����������������
				if (!done)//�û�����
					System.out.print("������>");
			}
			socketConn.close();//�ر���Դ
		} catch (SecurityException e) {//����ȫ�Դ���ʱ�������쳣
			System.out.println("���ӷ��������ְ�ȫ���⣡");
		} catch (IOException e) {//����IO���쳣
			System.out.println("���ӷ���������I/O����");
		}
	}
}
public class TextSocketClient {//����Socket�ͻ�����
	public static void main(String[] args) {//java��������ڴ�
		try {
			new Client("localhost", 8080);//IP��ַΪ����,�˿�Ϊ80
		} catch (Exception e) {//�����쳣
			System.out.println("���Կͻ������ӳ���"+e.getMessage());
		}
	}
}
