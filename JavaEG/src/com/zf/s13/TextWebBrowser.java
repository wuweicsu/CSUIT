package com.zf.s13;//创建一个包
import java.awt.BorderLayout;//引入类
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
		PropertyChangeListener {// 实现Web浏览器，支持HTML页面的显示
	private static final long serialVersionUID = 1L;
	JEditorPane Pane; // 显示HTML的面板
	JLabel messageLine; // 最底下的状态栏
	JTextField url; // 网址URL输入栏
	JFileChooser fileChooser;// 文件选择器
	JButton back;// 前进按钮
	JButton forward;// 后退按钮
	java.util.List historyReport = new ArrayList(); // 保存历史记录的列表
	int current = -1; // 当前页面的在历史记录列表中位置
	public static final int maxHistory = 50;// 当超过50时消除历史记录
	static int count = 0; // 当前已经打开的浏览器窗口数
	static boolean exit = false;
	String home = "http://www.baidu.com"; // 默认的主页
	public TextWebBrowser() {// 默认构造方法
		super("TextWebBrowser");
		Pane = new JEditorPane(); // 新键面板
		Pane.setEditable(false); // 不可编辑
		Pane.addHyperlinkListener(this); // 注册事件处理器，用于超连接事件。
		Pane.addPropertyChangeListener(this);// 用于处理属性改变事件
		this.getContentPane().add(new JScrollPane(Pane),// 将面板放入主窗口中
				BorderLayout.CENTER);// 面板居中
		messageLine = new JLabel(" ");// 创建状态栏
		this.getContentPane().add(messageLine, BorderLayout.SOUTH);
		this.initMenu();// 调用方法初始化菜单
		this.initToolbar();// 调用方法初始化工具栏
		TextWebBrowser.count++;
		this.addWindowListener(new WindowAdapter() { // 当关闭窗口时，触发事件
					public void windowClosing(WindowEvent e) {
						close();
					}
				});
	}
	private void initMenu() {// 初始化菜单栏
		JMenu fileMenu = new JMenu("文件");// 创建文件菜单项
		fileMenu.setMnemonic('F');// 设置快捷键
		JMenuItem newMenuItem = new JMenuItem("新建");// 创建新建项
		newMenuItem.setMnemonic('N');// 设置快捷键
		newMenuItem.addActionListener(new ActionListener() {// 选择新建触发打开窗口事件
					public void actionPerformed(ActionEvent e) {
						newBrowser();// 调用打开新的窗口方法
					}
				});
		JMenuItem openMenuItem = new JMenuItem("打开");// 创建打开项
		openMenuItem.setMnemonic('O');// 设置快捷键
		openMenuItem.addActionListener(new ActionListener() {// 选择打开触发打开窗口事件
					public void actionPerformed(ActionEvent e) {
						openLocalPage();// 调用打开文件方法
					}
				});
		JMenuItem closeMenuItem = new JMenuItem("关闭");// 创建关闭项
		closeMenuItem.setMnemonic('C');// 设置快捷键
		closeMenuItem.addActionListener(new ActionListener() {// 选择关闭触发打开窗口事件
					public void actionPerformed(ActionEvent e) {
						close();// 调用关闭窗口方法
					}
				});
		JMenuItem exitMenuItem = new JMenuItem("退出");// 创建退出项
		exitMenuItem.setMnemonic('E');// 设置快捷键
		exitMenuItem.addActionListener(new ActionListener() {// 选择退出触发打开窗口事件
					public void actionPerformed(ActionEvent e) {
						exit();// 调用退出方法
					}
				});
		fileMenu.add(newMenuItem);// 将新建项添加到文件菜单下
		fileMenu.add(openMenuItem);// 将打开项添加到文件菜单下
		fileMenu.add(closeMenuItem);// 将关闭项添加到文件菜单下
		fileMenu.add(exitMenuItem);// 将退出项添加到文件菜单下
		JMenuBar menuBar = new JMenuBar();// 创建菜单栏
		menuBar.add(fileMenu);// 将文件菜单放入菜单栏
		this.setJMenuBar(menuBar);// 设置菜单栏到主窗口
	}
	private void initToolbar() {// 初始化工具栏
		back = new JButton("后退");// 创建后退按钮
		back.setEnabled(false);// 不可用
		back.addActionListener(new ActionListener() {// 选择后退触发后退事件
					public void actionPerformed(ActionEvent e) {
						back();// 调用后退方法
					}
				});
		forward = new JButton("前进");// 创建前进按钮
		forward.setEnabled(false);// 不可用
		forward.addActionListener(new ActionListener() {// 选择前进触发前进事件
					public void actionPerformed(ActionEvent e) {
						forward();// 调用前进方法
					}
				});
		JButton refreshButton = new JButton("刷新");// 创建刷新按钮
		refreshButton.addActionListener(new ActionListener() {// 选择刷新触发刷新事件
					public void actionPerformed(ActionEvent e) {
						reload();// 调用刷新方法
					}
				});
		JToolBar toolbar = new JToolBar();// 创建工具栏
		toolbar.add(back);// 将后退按钮添加到工具栏
		toolbar.add(forward);// 将前进按钮添加到工具栏
		toolbar.add(refreshButton);// 将刷新按钮添加到工具栏
		url = new JTextField();// 创建文本框
		url.addActionListener(new ActionListener() {// 输入地址回车触发事件
					public void actionPerformed(ActionEvent e) {
						displayPage(url.getText());
					}
				});
		toolbar.add(new JLabel("         地址："));// 添加地址标签
		toolbar.add(url);// 将文本框添加到工具栏
		this.getContentPane().add(toolbar, BorderLayout.NORTH);// 将工具栏放在主窗口的南部
	}
	public static void closeWindowWhenAllExit(boolean b) {// 当所有窗口关闭时浏览器退出
		exit = b;
	}

	public void setHome(String home) {// 设置主页
		this.home = home;
	}

	public String getHome() {
		return home;
	}
	private boolean visitURL(URL source) {// 访问网址URL
		try {
			String href = source.toString();// 获取网址
			Pane.setPage(source); // 设置待访问的URL
			this.setTitle(href); // 页面打开后，将浏览器窗口的标题设为URL
			url.setText(href); // 网址输入框的内容也设置为URL
			return true;
		} catch (IOException ex) {
			messageLine.setText("不能打开页面：" + ex.getMessage());
			return false;
		}
	}
	public void displayPage(URL url) {// 打开URL指定的页面,URL放入历史列表中
		if (visitURL(url)) { // 访问页面是否成功
			historyReport.add(url); // URL放入历史列表中
			int numentries = historyReport.size();
			if (numentries > maxHistory + 10) {
				historyReport = historyReport.subList(numentries - maxHistory,
						numentries);
				numentries = maxHistory;
			}
			current = numentries - 1;
			if (current > 0) {// 不是当前页
				back.setEnabled(true);// 允许使用后退按钮
			}
		}
	}
	public void displayPage(String href) {// 浏览器打开指定页面
		try {
			if (!href.startsWith("http://")) {// 默认为HTTP协议
				href = "http://" + href;
			}
			displayPage(new URL(href));// 调用方法
		} catch (MalformedURLException ex) {
			messageLine.setText("错误的网址: " + href);
		}
	}
	public void openLocalPage() {// 打开本地文件
		if (fileChooser == null) {
			fileChooser = new JFileChooser();// 创建文件选择器
			FileFilter filter = new FileFilter() {// 文件过滤器限制只接受HTML和HTM文件
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
			fileChooser.addChoosableFileFilter(filter);// 只允许选择HTML和HTM文件
		}
		int result = fileChooser.showOpenDialog(this);// 打开文件选择器
		if (result == JFileChooser.APPROVE_OPTION) {// 选择确定按钮
			File selectedFile = fileChooser.getSelectedFile();// 获得选择的文件
			try {
				displayPage(selectedFile.toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
	public void back() {// 后退方法
		if (current > 0) {
			visitURL((URL) historyReport.get(--current));// 访问前一页
		}
		back.setEnabled((current > 0));// 当前页下标>0,可后退
		forward.setEnabled((current < historyReport.size() - 1));// 下标不是最后一页允许前进
	}
	public void forward() {// 前进方法
		if (current < historyReport.size() - 1) {
			visitURL((URL) historyReport.get(++current));
		}
		back.setEnabled((current > 0));// 当前页面下标>0，可后退
		forward.setEnabled((current < historyReport.size() - 1));// 当前页面下标不是最后，可前进
	}
	public void reload() {// 重新加载页面
		if (current != -1) {
			Pane.setDocument(new javax.swing.text.html.HTMLDocument());// 显示空白页
			visitURL((URL) historyReport.get(current));// 访问当前页
		}
	}
	public void home() {// 显示主页方法
		displayPage(getHome());
	}
	public void newBrowser() {// 打开新的浏览器窗口
		TextWebBrowser b = new TextWebBrowser();
		b.setSize(this.getWidth(), this.getHeight());//窗口与当前窗口一样大
		b.setVisible(true);
	}
	public void close() {//关闭当前窗口
		this.setVisible(false);// 隐藏当前窗口，销毁窗口中的组件
		this.dispose();
		synchronized (TextWebBrowser.class) {
			TextWebBrowser.count--;
			if ((count == 0) && exit) {
				System.exit(0);
			}
		}
	}
	public void exit() {//退出窗口程序
		if ((JOptionPane.showConfirmDialog(this, "你确定退出Web浏览器？", "退出",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) {//判断是否退出
			System.exit(0);
		}
	}
	public void hyperlinkUpdate(HyperlinkEvent e) {//处理超链接事件
		HyperlinkEvent.EventType type = e.getEventType();// 获取事件类型
		if (type == HyperlinkEvent.EventType.ACTIVATED) {//点击超链接
			displayPage(e.getURL());
		}
		else if (type == HyperlinkEvent.EventType.ENTERED) {//鼠标移动到超连接
			messageLine.setText(e.getURL().toString());//状态栏设值
		}
		else if (type == HyperlinkEvent.EventType.EXITED) {//鼠标离开超连接
			messageLine.setText(" ");//状态栏设值
		}
	}
	public void propertyChange(PropertyChangeEvent e) {//处理属性改变事件
		
	}
	public static void main(String[] args) throws IOException {//java程序主入口处
		TextWebBrowser.closeWindowWhenAllExit(true);//设置浏览器，当所有浏览器窗口都被关闭时，退出应用程序
		TextWebBrowser browser = new TextWebBrowser(); // 创建一个浏览器窗口
		browser.setSize(500, 400); // 设置浏览器窗口的默认大小
		browser.setVisible(true); // 显示窗口
		browser.displayPage(browser.getHome()); //打开主页
	}
}
