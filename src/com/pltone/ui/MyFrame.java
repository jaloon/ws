package com.pltone.ui;

import com.pltone.cnf.ServiceProperties;
import com.pltone.job.ForwordSchedule;
import com.pltone.util.NetUtil;
import com.pltone.ws.Elock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.xml.ws.Endpoint;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 自定义窗体
 *
 * @author chenlong
 * @version 1.0 2018-02-12
 */
public class MyFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(MyFrame.class);
	private static final Font LABEL_FONT = new Font("微软雅黑", Font.BOLD, 14);
	private static final Font CHECKBOX_FONT = new Font("黑体", Font.PLAIN, 16);
	private static final Font TEXTFIELD_FONT = new Font("Consolas", Font.PLAIN, 16);
	private static final Font NORMAL_BUTTON_FONT = new Font("黑体", Font.PLAIN, 13);
	private static final Font MAX_BUTTON_FONT = new Font("黑体", Font.BOLD, 18);
	private static final Font TEXTAREA_FONT = new Font("宋体", Font.PLAIN, 12);
	private static final Color START_COLOR = new Color(71, 141, 228);
	private static final Color STOP_COLOR = new Color(232, 38, 52);
	private static final String[] HTTP_SELECT = { "http://", "https://" };
	private static final String[] PATH_SELECT = { "", "Elock_Service.asmx", "elock/Elock_Service.asmx",
			"service/Elock_Service.asmx" };
	private Elock elock;
	private Endpoint endpoint;
	private ScheduledExecutorService service;
	private boolean serviceStart;
	private MyTrayIcon trayIcon;
	private int frameState = -1;
	private Image icon;
	private JTextField ipText;
	private MyNumberField portText;
	private JComboBox<String> pathCombo;
	private JTextField rtIp;
	private MyNumberField rtPort;
	private JTextField pltIp;
	private MyNumberField pltPort;
	private JButton ipBtn;
	private JButton resetBtn;
	private JButton startBtn;
	private JButton stopBtn;
	private JCheckBox forwordRt;
	private JCheckBox forwordPlt;
	private JComboBox<String> rtHttp;
	private JComboBox<String> pltHttp;
	private JComboBox<String> rtWsPath;
	private JComboBox<String> pltWsPath;
	private JTextArea textArea;

	public JTextArea getTextArea() {
		return textArea;
	}

	public MyFrame() {
		super("普利通电子签封安全监管系统物流配送转发子系统");
		icon = new ImageIcon(MyFrame.class.getClassLoader().getResource("img/icon.png")).getImage();
		setIconImage(icon);
		setMinimumSize(new Dimension(520, 540));
		setLocationRelativeTo(null);
		setResizable(false);
		// 必须设置窗体默认关闭动作为DO_NOTHING_ON_CLOSE，否则弹出提示窗后，
		// 若关闭提示窗，在窗体关闭事件中写的方法无法达到预期效果（窗体会执行默认关闭动作）
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (!SystemTray.isSupported()) {
					System.exit(0);
				}
				if (frameState < 0) {
					Object[] options = { "退出程序", "最小化" };
					frameState = JOptionPane.showOptionDialog(null, "选择关闭按钮动作", "提示", JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
				}
				switch (frameState) {
					case JOptionPane.YES_OPTION:
						stopService();
						System.exit(0);
						break;
					case JOptionPane.NO_OPTION:
						setVisible(false);
						break;
					default:
						break;
				}
			}
		});
	}

	/**
	 * 初始化界面
	 */
	public void initUI() {
		// 将本机系统外观设置为窗体当前外观
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// no exception expected
		}

        JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

        JLabel ipLabel = new JLabel("服务器IP地址");
		ipLabel.setBounds(15, 15, 100, 30);
		ipLabel.setFont(LABEL_FONT);
		contentPane.add(ipLabel);

        JLabel portLabel = new JLabel("服务器端口号");
		portLabel.setBounds(15, 65, 100, 30);
		portLabel.setFont(LABEL_FONT);
		contentPane.add(portLabel);

        JLabel pathLabel = new JLabel("服务器路径");
		pathLabel.setBounds(15, 115, 100, 30);
		pathLabel.setFont(LABEL_FONT);
		contentPane.add(pathLabel);

		ipText = new JTextField();
		ipText.setColumns(10);
		ipText.setBounds(120, 15, 255, 30);
		ipText.setFont(TEXTFIELD_FONT);
		contentPane.add(ipText);

		portText = new MyNumberField();
		portText.setColumns(10);
		portText.setBounds(120, 65, 255, 30);
		portText.setFont(TEXTFIELD_FONT);
		contentPane.add(portText);

		ipBtn = new JButton("获取本机IP");
		ipBtn.setBounds(395, 15, 100, 30);
		ipBtn.setFont(NORMAL_BUTTON_FONT);
		contentPane.add(ipBtn);

		resetBtn = new JButton("重置服务器");
		resetBtn.setBounds(395, 65, 100, 30);
		resetBtn.setFont(NORMAL_BUTTON_FONT);
		contentPane.add(resetBtn);

		pathCombo = new JComboBox<>(PATH_SELECT);
		pathCombo.setBounds(120, 115, 375, 30);
		pathCombo.setEditable(true);
		pathCombo.setSelectedIndex(1);
		contentPane.add(pathCombo);

        JLabel rtAddrLabel = new JLabel("瑞通系统物流配送接口地址（ip:port/path）");
		rtAddrLabel.setBounds(15, 160, 300, 30);
		rtAddrLabel.setFont(LABEL_FONT);
		contentPane.add(rtAddrLabel);

		forwordRt = new JCheckBox("转发到瑞通系统");
		forwordRt.setBounds(340, 160, 155, 30);
		forwordRt.setFont(CHECKBOX_FONT);
		forwordRt.setBackground(Color.LIGHT_GRAY);
		forwordRt.setSelected(true);
		contentPane.add(forwordRt);

		rtHttp = new JComboBox<>(HTTP_SELECT);
		rtHttp.setBounds(15, 200, 80, 35);
		contentPane.add(rtHttp);

		rtIp = new JTextField();
		rtIp.setColumns(10);
		rtIp.setBounds(95, 200, 130, 35);
		contentPane.add(rtIp);

		JLabel label1 = new JLabel(":");
		label1.setBounds(225, 200, 10, 35);
		label1.setFont(TEXTFIELD_FONT);
		contentPane.add(label1);

		rtPort = new MyNumberField();
		rtPort.setColumns(10);
		rtPort.setBounds(235, 200, 50, 35);
		contentPane.add(rtPort);

		JLabel label2 = new JLabel("/");
		label2.setBounds(285, 200, 10, 35);
		label2.setFont(TEXTFIELD_FONT);
		contentPane.add(label2);

		rtWsPath = new JComboBox<>(PATH_SELECT);
		rtWsPath.setBounds(295, 200, 200, 35);
		rtWsPath.setEditable(true);
		rtWsPath.setSelectedIndex(1);
		contentPane.add(rtWsPath);

        JLabel pltAddrLabel = new JLabel("普利通系统物流配送接口地址（ip:port/path）");
		pltAddrLabel.setBounds(15, 250, 300, 30);
		pltAddrLabel.setFont(LABEL_FONT);
		contentPane.add(pltAddrLabel);

		forwordPlt = new JCheckBox("转发到普利通系统");
		forwordPlt.setBounds(340, 250, 155, 30);
		forwordPlt.setFont(CHECKBOX_FONT);
		forwordPlt.setBackground(Color.LIGHT_GRAY);
		contentPane.add(forwordPlt);

		pltHttp = new JComboBox<>(HTTP_SELECT);
		pltHttp.setBounds(15, 290, 80, 35);
		contentPane.add(pltHttp);

		pltIp = new JTextField();
		pltIp.setColumns(10);
		pltIp.setBounds(95, 290, 130, 35);
		contentPane.add(pltIp);

		JLabel label3 = new JLabel(":");
		label3.setBounds(225, 290, 10, 35);
		label3.setFont(TEXTFIELD_FONT);
		contentPane.add(label3);

		pltPort = new MyNumberField("8080");
		pltPort.setColumns(10);
		pltPort.setBounds(235, 290, 50, 35);
		contentPane.add(pltPort);

		JLabel label4 = new JLabel("/");
		label4.setBounds(285, 290, 10, 35);
		label4.setFont(TEXTFIELD_FONT);
		contentPane.add(label4);

		pltWsPath = new JComboBox<>(PATH_SELECT);
		pltWsPath.setBounds(295, 290, 200, 35);
		pltWsPath.setEditable(true);
		pltWsPath.setSelectedIndex(1);
		contentPane.add(pltWsPath);

		startBtn = new JButton("启动服务");
		startBtn.setBounds(60, 340, 120, 35);
		startBtn.setFont(MAX_BUTTON_FONT);
		// startBtn.setBackground(START_COLOR);
		startBtn.setForeground(START_COLOR);
		// startBtn.setOpaque(false);//设置控件是否透明，true为不透明，false为透明
		contentPane.add(startBtn);

		stopBtn = new JButton("停止服务");
		stopBtn.setBounds(315, 340, 120, 35);
		stopBtn.setEnabled(false);
		stopBtn.setFont(MAX_BUTTON_FONT);
		contentPane.add(stopBtn);

		textArea = new JTextArea();
		// textArea.setBounds(15, 390, 480, 110);
		textArea.setEditable(false);
		textArea.setFont(TEXTAREA_FONT);
		// textArea.setLineWrap(true);// 激活自动换行功能
		// textArea.setWrapStyleWord(true);// 激活断行不断字功能
		// 把定义的JTextArea放到JScrollPane里面去
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setBounds(15, 390, 480, 110);
		// 分别设置水平和垂直滚动条自动出现
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		contentPane.add(scroll);

		addSystemTray();
		setVisible(true);
	}

	/**
	 * 初始化服务器配置
	 */
	public void initConf() {
		ipText.setText(ServiceProperties.getServiceIp());
		ipText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				ipText.selectAll();
			}
		});

		portText.setNumber(ServiceProperties.getServicePort());
		portText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				portText.selectAll();
			}
		});

		ipBtn.addActionListener(e -> ipText.setText(NetUtil.getLocalHostLANAddress().getHostAddress()));

		resetBtn.addActionListener(e -> {
			ipText.setText(ServiceProperties.getServiceIp());
			portText.setNumber(ServiceProperties.getServicePort());
            pathCombo.setSelectedItem(ServiceProperties.getServicePath());
		});

		pathCombo.setSelectedItem(ServiceProperties.getServicePath());

		forwordRt.setSelected(ServiceProperties.isRtForward());
		rtHttp.setSelectedItem(ServiceProperties.getRtHttp());
		rtIp.setText(ServiceProperties.getRtIp());
		rtIp.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				rtIp.selectAll();
			}
		});
		rtPort.setNumber(ServiceProperties.getRtPort());
		rtPort.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				rtPort.selectAll();
			}
		});
		rtWsPath.setSelectedItem(ServiceProperties.getRtPath());

		forwordPlt.setSelected(ServiceProperties.isPltForward());
		pltHttp.setSelectedItem(ServiceProperties.getPltHttp());
		pltIp.setText(ServiceProperties.getPltIp());
		pltIp.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				pltIp.selectAll();
			}
		});
		pltPort.setNumber(ServiceProperties.getPltPort());
		pltPort.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				pltPort.selectAll();
			}
		});
		pltWsPath.setSelectedItem(ServiceProperties.getPltPath());

		startBtn.addActionListener(event -> {
			logger.info("WebService start...");
			String _serviceIp = ipText.getText();
			int _servicePort = portText.getNumber(ServiceProperties.DEFAUT_PORT);
			String _servicePath = pathCombo.getSelectedItem().toString();
			boolean _rtForward = forwordRt.isSelected();
			String _rtHttp = rtHttp.getSelectedItem().toString();
			String _rtIp = rtIp.getText();
			int _rtPort = rtPort.getNumber(ServiceProperties.DEFAUT_PORT);
			String _rtPath = rtWsPath.getSelectedItem().toString();
			boolean _pltForward = forwordPlt.isSelected();
			String _pltHttp = pltHttp.getSelectedItem().toString();
			String _pltIp = pltIp.getText();
			int _pltPort = pltPort.getNumber(ServiceProperties.DEFAUT_PORT);
			String _pltPath = pltWsPath.getSelectedItem().toString();

			String address = new StringBuffer("http://").append(_serviceIp).append(':').append(_servicePort)
					.append('/').append(_servicePath).toString();
			String rtWsAddr = new StringBuffer().append(_rtHttp).append(_rtIp).append(':').append(_rtPort)
					.append('/').append(_rtPath).toString();
			String pltWsAddr = new StringBuffer().append(_pltHttp).append(_pltIp).append(':').append(_pltPort)
					.append('/').append(_pltPath).toString();

			elock = new Elock();
			elock.setForwordRt(forwordRt.isSelected());
			elock.setForwordPlt(forwordPlt.isSelected());
			elock.setRtWsAddr(rtWsAddr);
			elock.setPltWsAddr(pltWsAddr);
			elock.setTrayIcon(trayIcon);

			// 启动服务
			serviceStart = startService(address);
			if (!serviceStart) {
				return;
			}

			ServiceProperties.setServiceIp(_serviceIp);
			ServiceProperties.setServicePort(_servicePort);
			ServiceProperties.setServicePath(_servicePath);
			ServiceProperties.setRtForward(_rtForward);
			ServiceProperties.setRtHttp(_rtHttp);
			ServiceProperties.setRtIp(_rtIp);
			ServiceProperties.setRtPort(_rtPort);
			ServiceProperties.setRtPath(_rtPath);
			ServiceProperties.setPltForward(_pltForward);
			ServiceProperties.setPltHttp(_pltHttp);
			ServiceProperties.setPltIp(_pltIp);
			ServiceProperties.setPltPort(_pltPort);
			ServiceProperties.setPltPath(_pltPath);
			ServiceProperties.save();

			// startBtn.setBackground(null);
			startBtn.setForeground(null);
			startBtn.setEnabled(false);
			ipText.setEnabled(false);
			portText.setEnabled(false);
			pathCombo.setEnabled(false);
			ipBtn.setEnabled(false);
			resetBtn.setEnabled(false);
			rtHttp.setEnabled(false);
			rtIp.setEnabled(false);
			rtPort.setEnabled(false);
			// rtWsPath.setEditable(false);
			rtWsPath.setEnabled(false);
			pltHttp.setEnabled(false);
			pltIp.setEnabled(false);
			pltPort.setEnabled(false);
			// pltWsPath.setEditable(false);
			pltWsPath.setEnabled(false);
			forwordRt.setEnabled(false);
			forwordPlt.setEnabled(false);

			// stopBtn.setBackground(STOP_COLOR);
			stopBtn.setForeground(STOP_COLOR);
			stopBtn.setEnabled(true);

			logger.info("转发服务器地址: {}", address);
			if (_rtForward) {
				logger.info("瑞通接口地址: {}", rtWsAddr);
			}
			if (_pltForward) {
				logger.info("普利通接口地址: {}", pltWsAddr);
			}
			logger.info("WebService started.");
		});

		stopBtn.addActionListener(e -> {
			logger.info("WebService stop...");
			// stopBtn.setBackground(null);
			stopBtn.setForeground(null);
			stopBtn.setEnabled(false);

			// 关闭服务
			stopService();

			ipText.setEnabled(true);
			portText.setEnabled(true);
			pathCombo.setEnabled(true);
			ipBtn.setEnabled(true);
			resetBtn.setEnabled(true);
			rtHttp.setEnabled(true);
			rtIp.setEnabled(true);
			rtPort.setEnabled(true);
			// rtWsPath.setEditable(true);
			rtWsPath.setEnabled(true);
			pltHttp.setEnabled(true);
			pltIp.setEnabled(true);
			pltPort.setEnabled(true);
			// pltWsPath.setEditable(true);
			pltWsPath.setEnabled(true);
			forwordRt.setEnabled(true);
			forwordPlt.setEnabled(true);
			// startBtn.setBackground(START_COLOR);
			startBtn.setForeground(START_COLOR);
			startBtn.setEnabled(true);
			logger.info("WebService stoped.");
		});
	}

	/**
	 * 添加系统托盘
	 */
	private void addSystemTray() {
		// 判断当前平台是否支持系统托盘
		if (SystemTray.isSupported()) {
			try {
				// 得到当前系统托盘
				SystemTray systemTray = SystemTray.getSystemTray();
				// 定义弹出菜单
				MyPopupMenu popupMenu = new MyPopupMenu();
				// 定义弹出菜单项
				JMenuItem openmenu = new JMenuItem("打开主面板",
						new ImageIcon(MyFrame.class.getClassLoader().getResource("img/frame.png")));
				JMenuItem exitmenu = new JMenuItem("退出",
						new ImageIcon(MyFrame.class.getClassLoader().getResource("img/exit.png")));
				// 添加弹出菜单项到弹出菜单
				popupMenu.add(openmenu);
				// popupMenu.addSeparator();// 添加分割线
				popupMenu.add(exitmenu);
				// 创建带指定图像、工具提示和弹出菜单的 MyTrayIcon
				trayIcon = new MyTrayIcon(icon, "物流配送接口", popupMenu);
				// 设置托盘的图标
				systemTray.add(trayIcon);
				// 设置单击击系统托盘图标显示主窗口
				trayIcon.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// 鼠标左键点击,设置窗体状态，正常显示
						if (e.getButton() == MouseEvent.BUTTON1) {
							setExtendedState(JFrame.NORMAL);
							setVisible(true);
						}
					}
				});
				// 为弹出菜单项添加监听器
				openmenu.addActionListener(e -> {
					setExtendedState(JFrame.NORMAL);
					setVisible(true);
				});
				exitmenu.addActionListener(e -> {
					systemTray.remove(trayIcon);
					System.exit(0);
				});
			} catch (AWTException e) {
				logger.error("添加系统托盘图标异常：{}", e.getMessage());
			}
			return;
		}
		logger.info("主机系统不支持系统托盘！");
	}

	/**
	 * 启动服务
	 *
	 * @param address
	 *            {@link String} WebSevice发布地址
	 * @return
	 */
	private boolean startService(String address) {
		try {
			endpoint = Endpoint.publish(address, elock);
			service = ForwordSchedule.executeForwordSchedule(elock);
			return true;
		} catch (Exception e) {
			logger.error("启动服务异常：\n{}", e.toString());
			JOptionPane.showMessageDialog(null, "启动服务异常：" + e.getMessage(), "警告", JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}

	/**
	 * 关闭服务
	 */
	private void stopService() {
		if (serviceStart) {
			try {
				endpoint.stop();
				service.shutdown();
				elock.closeThreadPool();
			} catch (Exception e) {
				logger.error("关闭服务异常：\n{}", e.toString());
			}
		}
	}
}
