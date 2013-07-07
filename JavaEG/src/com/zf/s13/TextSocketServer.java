package com.zf.s13;//����һ����
import java.io.*;//������
import java.net.*;
class SocketServer {//Socket��������
	private int port;//�˿�
	public SocketServer(int port) {//�������Ĺ��췽�����г�ʼ��
		this.port = port;
		start();//���������������˵ķ���
	}
	public String infoUpperCase(String line) {//������Ϣ
		return line.toUpperCase();//���ַ�����д
	}
	public void start() {//������������
		try {
			ServerSocket serverSocket = new ServerSocket(port);//���ݶ˿ڴ�����������Socket����
			System.out.println("�������������������˿ں�Ϊ��" + port);// ��ʾ������Ϣ
			System.out.println("���ڵȴ��ͻ�������.........");
			Socket socketAccept = serverSocket.accept();//����ȴ��ͻ�������
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socketAccept.getInputStream()));// ��ȡ��ȡ�ͻ��˵�������
			PrintWriter out = new PrintWriter(socketAccept.getOutputStream(),
					true);// ��ȡд���ͻ��˵����������,true:�Զ�ˢ��
			out.println("�����������ӳɹ�.........");//��ͻ�����������Ϣ
			out.println("����exit�Ͽ��������������");
			boolean done = false;
			while (!done) {
				String line = in.readLine();//��ȡ�ͻ���ÿ�е�����
				if (line == null) {//û��д�򲻶�ȡ
					done = true;
				} else {
					System.out.println("�ͻ��˴��������ݣ�" + line);//��ʾ�ͻ��˷��͵�����
					String message = infoUpperCase(line);// ��Ϣ����
					out.println("�ӷ������˿ڷ��͵����ݣ�" + message);//��ͻ��˷�����Ϣ
					if (line.trim().equals("exit"))//�˳��ж�
						done = true;
				}
			}
			socketAccept.close();// �ر�ͨ����Դ
		} catch (Exception e) {//�����쳣
			System.out.println("�����������˳��ִ���"+e.getMessage());
		}
	}
}
public class TextSocketServer {//����Socket�������˵���
	public static void main(String[] args) {//java��������ڴ�
		try {
			SocketServer server=new SocketServer(8080);//����˿ں�ʵ��������
		} catch (Exception e) {//�����쳣
			System.out.println("���Է������˼�������"+e.getMessage());
		}
	}
}