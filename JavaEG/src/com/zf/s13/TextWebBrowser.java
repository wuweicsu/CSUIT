package com.zf.s13;//����һ����
import java.awt.BorderLayout;//������
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileFilter;
public class TextWebBrowser extends JFrame implements HyperlinkListener,
		PropertyChangeListener {// ʵ��Web�������֧��HTMLҳ�����ʾ
	private static final long serialVersionUID = 1L;
	JEditorPane Pane; // ��ʾHTML�����
	JLabel messageLine; // ����µ�״̬��
	JTextField url; // ��ַURL������
	JFileChooser fileChooser;// �ļ�ѡ����
	JButton back;// ǰ����ť
	JButton forward;// ���˰�ť
	java.util.List historyReport = new ArrayList(); // ������ʷ��¼���б�
	int current = -1; // ��ǰҳ�������ʷ��¼�б���λ��
	public static final int maxHistory = 50;// ������50ʱ������ʷ��¼
	static int count = 0; // ��ǰ�Ѿ��򿪵������������
	static boolean exit = false;
	String home = "http://www.baidu.com"; // Ĭ�ϵ���ҳ
	public TextWebBrowser() {// Ĭ�Ϲ��췽��
		super("TextWebBrowser");
		Pane = new JEditorPane(); // �¼����
		Pane.setEditable(false); // ���ɱ༭
		Pane.addHyperlinkListener(this); // ע���¼������������ڳ������¼���
		Pane.addPropertyChangeListener(this);// ���ڴ������Ըı��¼�
		this.getContentPane().add(new JScrollPane(Pane),// ����������������
				BorderLayout.CENTER);// ������
		messageLine = new JLabel(" ");// ����״̬��
		this.getContentPane().add(messageLine, BorderLayout.SOUTH);
		this.initMenu();// ���÷�����ʼ���˵�
		this.initToolbar();// ���÷�����ʼ��������
		TextWebBrowser.count++;
		this.addWindowListener(new WindowAdapter() { // ���رմ���ʱ�������¼�
					public void windowClosing(WindowEvent e) {
						close();
					}
				});
	}
	private void initMenu() {// ��ʼ���˵���
		JMenu fileMenu = new JMenu("�ļ�");// �����ļ��˵���
		fileMenu.setMnemonic('F');// ���ÿ�ݼ�
		JMenuItem newMenuItem = new JMenuItem("�½�");// �����½���
		newMenuItem.setMnemonic('N');// ���ÿ�ݼ�
		newMenuItem.addActionListener(new ActionListener() {// ѡ���½������򿪴����¼�
					public void actionPerformed(ActionEvent e) {
						newBrowser();// ���ô��µĴ��ڷ���
					}
				});
		JMenuItem openMenuItem = new JMenuItem("��");// ��������
		openMenuItem.setMnemonic('O');// ���ÿ�ݼ�
		openMenuItem.addActionListener(new ActionListener() {// ѡ��򿪴����򿪴����¼�
					public void actionPerformed(ActionEvent e) {
						openLocalPage();// ���ô��ļ�����
					}
				});
		JMenuItem closeMenuItem = new JMenuItem("�ر�");// �����ر���
		closeMenuItem.setMnemonic('C');// ���ÿ�ݼ�
		closeMenuItem.addActionListener(new ActionListener() {// ѡ��رմ����򿪴����¼�
					public void actionPerformed(ActionEvent e) {
						close();// ���ùرմ��ڷ���
					}
				});
		JMenuItem exitMenuItem = new JMenuItem("�˳�");// �����˳���
		exitMenuItem.setMnemonic('E');// ���ÿ�ݼ�
		exitMenuItem.addActionListener(new ActionListener() {// ѡ���˳������򿪴����¼�
					public void actionPerformed(ActionEvent e) {
						exit();// �����˳�����
					}
				});
		fileMenu.add(newMenuItem);// ���½�����ӵ��ļ��˵���
		fileMenu.add(openMenuItem);// ��������ӵ��ļ��˵���
		fileMenu.add(closeMenuItem);// ���ر�����ӵ��ļ��˵���
		fileMenu.add(exitMenuItem);// ���˳�����ӵ��ļ��˵���
		JMenuBar menuBar = new JMenuBar();// �����˵���
		menuBar.add(fileMenu);// ���ļ��˵�����˵���
		this.setJMenuBar(menuBar);// ���ò˵�����������
	}
	private void initToolbar() {// ��ʼ��������
		back = new JButton("����");// �������˰�ť
		back.setEnabled(false);// ������
		back.addActionListener(new ActionListener() {// ѡ����˴��������¼�
					public void actionPerformed(ActionEvent e) {
						back();// ���ú��˷���
					}
				});
		forward = new JButton("ǰ��");// ����ǰ����ť
		forward.setEnabled(false);// ������
		forward.addActionListener(new ActionListener() {// ѡ��ǰ������ǰ���¼�
					public void actionPerformed(ActionEvent e) {
						forward();// ����ǰ������
					}
				});
		JButton refreshButton = new JButton("ˢ��");// ����ˢ�°�ť
		refreshButton.addActionListener(new ActionListener() {// ѡ��ˢ�´���ˢ���¼�
					public void actionPerformed(ActionEvent e) {
						reload();// ����ˢ�·���
					}
				});
		JToolBar toolbar = new JToolBar();// ����������
		toolbar.add(back);// �����˰�ť��ӵ�������
		toolbar.add(forward);// ��ǰ����ť��ӵ�������
		toolbar.add(refreshButton);// ��ˢ�°�ť��ӵ�������
		url = new JTextField();// �����ı���
		url.addActionListener(new ActionListener() {// �����ַ�س������¼�
					public void actionPerformed(ActionEvent e) {
						displayPage(url.getText());
					}
				});
		toolbar.add(new JLabel("         ��ַ��"));// ��ӵ�ַ��ǩ
		toolbar.add(url);// ���ı�����ӵ�������
		this.getContentPane().add(toolbar, BorderLayout.NORTH);// �����������������ڵ��ϲ�
	}
	public static void closeWindowWhenAllExit(boolean b) {// �����д��ڹر�ʱ������˳�
		exit = b;
	}

	public void setHome(String home) {// ������ҳ
		this.home = home;
	}

	public String getHome() {
		return home;
	}
	private boolean visitURL(URL source) {// ������ַURL
		try {
			String href = source.toString();// ��ȡ��ַ
			Pane.setPage(source); // ���ô����ʵ�URL
			this.setTitle(href); // ҳ��򿪺󣬽���������ڵı�����ΪURL
			url.setText(href); // ��ַ����������Ҳ����ΪURL
			return true;
		} catch (IOException ex) {
			messageLine.setText("���ܴ�ҳ�棺" + ex.getMessage());
			return false;
		}
	}
	public void displayPage(URL url) {// ��URLָ����ҳ��,URL������ʷ�б���
		if (visitURL(url)) { // ����ҳ���Ƿ�ɹ�
			historyReport.add(url); // URL������ʷ�б���
			int numentries = historyReport.size();
			if (numentries > maxHistory + 10) {
				historyReport = historyReport.subList(numentries - maxHistory,
						numentries);
				numentries = maxHistory;
			}
			current = numentries - 1;
			if (current > 0) {// ���ǵ�ǰҳ
				back.setEnabled(true);// ����ʹ�ú��˰�ť
			}
		}
	}
	public void displayPage(String href) {// �������ָ��ҳ��
		try {
			if (!href.startsWith("http://")) {// Ĭ��ΪHTTPЭ��
				href = "http://" + href;
			}
			displayPage(new URL(href));// ���÷���
		} catch (MalformedURLException ex) {
			messageLine.setText("�������ַ: " + href);
		}
	}
	public void openLocalPage() {// �򿪱����ļ�
		if (fileChooser == null) {
			fileChooser = new JFileChooser();// �����ļ�ѡ����
			FileFilter filter = new FileFilter() {// �ļ�����������ֻ����HTML��HTM�ļ�
				public boolean accept(File f) {
					String fn = f.getName();
					if (fn.endsWith(".html") || fn.endsWith(".htm")) {
						return true;
					} else {
						return false;
					}
				}

				public String getDescription() {
					return "HTML Files";
				}
			};
			fileChooser.setFileFilter(filter);
			fileChooser.addChoosableFileFilter(filter);// ֻ����ѡ��HTML��HTM�ļ�
		}
		int result = fileChooser.showOpenDialog(this);// ���ļ�ѡ����
		if (result == JFileChooser.APPROVE_OPTION) {// ѡ��ȷ����ť
			File selectedFile = fileChooser.getSelectedFile();// ���ѡ����ļ�
			try {
				displayPage(selectedFile.toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
	public void back() {// ���˷���
		if (current > 0) {
			visitURL((URL) historyReport.get(--current));// ����ǰһҳ
		}
		back.setEnabled((current > 0));// ��ǰҳ�±�>0,�ɺ���
		forward.setEnabled((current < historyReport.size() - 1));// �±겻�����һҳ����ǰ��
	}
	public void forward() {// ǰ������
		if (current < historyReport.size() - 1) {
			visitURL((URL) historyReport.get(++current));
		}
		back.setEnabled((current > 0));// ��ǰҳ���±�>0���ɺ���
		forward.setEnabled((current < historyReport.size() - 1));// ��ǰҳ���±겻����󣬿�ǰ��
	}
	public void reload() {// ���¼���ҳ��
		if (current != -1) {
			Pane.setDocument(new javax.swing.text.html.HTMLDocument());// ��ʾ�հ�ҳ
			visitURL((URL) historyReport.get(current));// ���ʵ�ǰҳ
		}
	}
	public void home() {// ��ʾ��ҳ����
		displayPage(getHome());
	}
	public void newBrowser() {// ���µ����������
		TextWebBrowser b = new TextWebBrowser();
		b.setSize(this.getWidth(), this.getHeight());//�����뵱ǰ����һ����
		b.setVisible(true);
	}
	public void close() {//�رյ�ǰ����
		this.setVisible(false);// ���ص�ǰ���ڣ����ٴ����е����
		this.dispose();
		synchronized (TextWebBrowser.class) {
			TextWebBrowser.count--;
			if ((count == 0) && exit) {
				System.exit(0);
			}
		}
	}
	public void exit() {//�˳����ڳ���
		if ((JOptionPane.showConfirmDialog(this, "��ȷ���˳�Web�������", "�˳�",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) {//�ж��Ƿ��˳�
			System.exit(0);
		}
	}
	public void hyperlinkUpdate(HyperlinkEvent e) {//���������¼�
		HyperlinkEvent.EventType type = e.getEventType();// ��ȡ�¼�����
		if (type == HyperlinkEvent.EventType.ACTIVATED) {//���������
			displayPage(e.getURL());
		}
		else if (type == HyperlinkEvent.EventType.ENTERED) {//����ƶ���������
			messageLine.setText(e.getURL().toString());//״̬����ֵ
		}
		else if (type == HyperlinkEvent.EventType.EXITED) {//����뿪������
			messageLine.setText(" ");//״̬����ֵ
		}
	}
	public void propertyChange(PropertyChangeEvent e) {//�������Ըı��¼�
		
	}
	public static void main(String[] args) throws IOException {//java��������ڴ�
		TextWebBrowser.closeWindowWhenAllExit(true);//�������������������������ڶ����ر�ʱ���˳�Ӧ�ó���
		TextWebBrowser browser = new TextWebBrowser(); // ����һ�����������
		browser.setSize(500, 400); // ������������ڵ�Ĭ�ϴ�С
		browser.setVisible(true); // ��ʾ����
		browser.displayPage(browser.getHome()); //����ҳ
	}
}
