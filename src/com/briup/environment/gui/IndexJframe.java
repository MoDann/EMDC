package com.briup.environment.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import com.briup.environment.bean.UserBean;

public class IndexJframe extends JFrame implements MouseListener,ActionListener{

	private static Font font = new Font("微软雅黑", Font.PLAIN, 14);
	// 定义用户对象
	private UserBean user;
	String name;
	// 定义辅助变量
	int sign_home = 0;
	int sign_baseData = 0;
	int sign_purchase_sale_stock = 0;
	int sign_userManager = 0;

	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

	// 定义全局组件
	JPanel backgroundPanel, topPanel, topMenu, topPrompt, centerPanel, subPanel, subMenu;
	JTabbedPane jTabbedPane;

	JLabel home, baseData, purchase_sale_stock, userManager;

	public IndexJframe(String name){
	//	name=user.getUname();
		new IndexJframe(user);
	}

	public IndexJframe(UserBean user) {

		this.user = user;
		//窗口淡入淡出
		setUndecorated(true);
		new WindowOpacity(this);
		
		// 设置tab面板缩进
		UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(0, 0, 0, 0));

		try {
			//Image imgae = ImageIO.read(new File("src/images/picture/admin/logo.png"));
			Image image = getToolkit().getImage("src/images/picture/admin/logo.png");
			setIconImage(image);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		initBackgroundPanel();

		setTitle("物联网环境数据监测系统");
		setSize((int) (width * 0.8f), (int) (height * 0.8f));
		setVisible(true);
		setLocationRelativeTo(null);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	// 初始化背景面板
	public void initBackgroundPanel() {

		backgroundPanel = new JPanel(new BorderLayout());
		initTop();
		initCenterPanel();

		backgroundPanel.add(topPanel, "North");
		backgroundPanel.add(centerPanel, "Center");

		this.add(backgroundPanel);
	}

	// 初始化顶部顶部面板
	public void initTop() {

		initTopMenu();
		initTopPrompt();

		topPanel = new JPanel(new BorderLayout());
		topPanel.setPreferredSize(new Dimension(width, 40));

		topPanel.add(topMenu, "West");
		topPanel.add(topPrompt, "East");
	}

	// 初始化顶部菜单
	public void initTopMenu() {

		topMenu = new JPanel();
		topMenu.setPreferredSize(new Dimension(500, 40));
		topMenu.setOpaque(false);

		String[] nameStrings = { "首页", "基础数据", "进销存管理", "用户管理" };

		home = CreateMenuLabel(home, nameStrings[0], "home", topMenu);
		home.setName("home");
		baseData = CreateMenuLabel(baseData, nameStrings[1], "baseData", topMenu);
		baseData.setName("baseData");
		purchase_sale_stock = CreateMenuLabel(purchase_sale_stock, nameStrings[2], "purchase_sale_stock", topMenu);
		purchase_sale_stock.setName("purchase_sale_stock");
		userManager = CreateMenuLabel(userManager, nameStrings[3], "userManager", topMenu);
		userManager.setName("userManager");

	}

	// 创建顶部菜单Label
	public JLabel CreateMenuLabel(JLabel jlb, String text, String name, JPanel who) {
		JLabel line = new JLabel("<html>&nbsp;<font color='#D2D2D2'>|</font>&nbsp;</html>");
		Icon icon = new ImageIcon("src/images/picture/admin/" + name + ".png");
		jlb = new JLabel(icon);
		jlb.setText("<html><font color='black'>" + text + "</font>&nbsp;</html>");
		jlb.addMouseListener(this);
	//	Font font2 = new Font("微软雅黑", Font.PLAIN, 14);
		jlb.setFont(font);
		who.add(jlb);
		if (!"userManager".equals(name)) {
			who.add(line);
		}
		return jlb;
	}

	// 初始化顶部欢迎面板
	public void initTopPrompt() {

		Icon icon = new ImageIcon("src/images/picture/admin/male.png");
		JLabel label = new JLabel(icon);
		if (user != null) {
			label.setText("<html><font color='black'>欢迎您，</font><font color='#336699'><b>" + this.user.getUname()
					+ "</b></font></html>");
		} else {
			label.setText("<html><font color='black'>欢迎您，</font><font color='#336699'><b></b></font></html>");
		}
		label.setFont(font);
		topPrompt = new JPanel();
		topPrompt.setPreferredSize(new Dimension(180, 40));
		topPrompt.setOpaque(false);
		topPrompt.add(label);

	}

	// 初始化中心面板
	public void initCenterPanel() {
		centerPanel = new JPanel(new BorderLayout());
		home.setText("<html><font color='#336699' style='font-weight:bold'>" + "首页" + "</font>&nbsp;</html>");
		creatHome();
		centerPanel.setOpaque(false);
	}

	// 初始化辅助变量
	public void initSign() {
		sign_home = 0;
		sign_baseData = 0;
		sign_purchase_sale_stock = 0;
		sign_userManager = 0;
	}

	// 创建首页面板
	public void creatHome() {

		centerPanel.removeAll();
		try {
			Image bgimg = ImageIO.read(new File("src/images/picture/admin/indexbackground.png"));
			ImagePanel centerBackground = new ImagePanel(bgimg);
			centerPanel.add(centerBackground, "Center");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 创建基础数据面板
	public void creatBaseDataTab() {

		centerPanel.removeAll();
		// 设置tab标题位置
		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// 设置tab布局
		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		jTabbedPane.setFont(font);

		//jTabbedPane.addTab("商品管理", new GoodsManagerJPanel().backgroundPanel);
		centerPanel.add(jTabbedPane, "Center");
	}

	// // 创建进销存管理面板
	public void creatpurchaseSaleStockTab() {

		centerPanel.removeAll();
		// 设置tab标题位置
		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// 设置tab布局
		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		jTabbedPane.setFont(font);

		centerPanel.add(jTabbedPane, "Center");
	}

	// 刷新进销存管理面板
	public void refreshpurchaseSaleStockTab() {

		centerPanel.removeAll();
		// 设置tab标题位置
		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// 设置tab布局
		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		jTabbedPane.setFont(font);

		centerPanel.add(jTabbedPane, "Center");
	}

	// 创建用户管理面板
	public void creatUserManagerTab() {

		centerPanel.removeAll();
		// 设置tab标题位置
		jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// 设置tab布局
		jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		
		jTabbedPane.setFont(font);

	//	jTabbedPane.addTab("用户管理", new UserManagerJPanel(user, this).backgroundPanel);
		centerPanel.add(jTabbedPane, "Center");
	}

	// 鼠标点击事件
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == home) {
			initSign();
			sign_home = 1;
			creatHome();
			home.setText("<html><font color='#336699' style='font-weight:bold'>" + "首页" + "</font>&nbsp;</html>");
			baseData.setText("<html><font color='black'>" + "基础数据" + "</font>&nbsp;</html>");
			purchase_sale_stock.setText("<html><font color='black'>" + "进销存管理" + "</font>&nbsp;</html>");
			userManager.setText("<html><font color='black'>" + "用户管理" + "</font>&nbsp;</html>");
		} else if (e.getSource() == baseData) {
			initSign();
			sign_baseData = 1;
			creatBaseDataTab();
			baseData.setText("<html><font color='#336699' style='font-weight:bold'>" + "基础数据" + "</font>&nbsp;</html>");
			home.setText("<html><font color='black'>" + "首页" + "</font>&nbsp;</html>");
			purchase_sale_stock.setText("<html><font color='black'>" + "进销存管理" + "</font>&nbsp;</html>");
			userManager.setText("<html><font color='black'>" + "用户管理" + "</font>&nbsp;</html>");
		} else if (e.getSource() == purchase_sale_stock) {
			initSign();
			sign_purchase_sale_stock = 1;
			creatpurchaseSaleStockTab();
			purchase_sale_stock.setText(
					"<html><font color='#336699' style='font-weight:bold'>" + "进销存管理" + "</font>&nbsp;</html>");
			home.setText("<html><font color='black'>" + "首页" + "</font>&nbsp;</html>");
			baseData.setText("<html><font color='black'>" + "基础数据" + "</font>&nbsp;</html>");
			userManager.setText("<html><font color='black'>" + "用户管理" + "</font>&nbsp;</html>");
		} else if (e.getSource() == userManager) {
			initSign();
			sign_userManager = 1;
			creatUserManagerTab();
			userManager
					.setText("<html><font color='#336699' style='font-weight:bold'>" + "用户管理" + "</font>&nbsp;</html>");
			home.setText("<html><font color='black'>" + "首页" + "</font>&nbsp;</html>");
			baseData.setText("<html><font color='black'>" + "基础数据" + "</font>&nbsp;</html>");
			purchase_sale_stock.setText("<html><font color='black'>" + "进销存管理" + "</font>&nbsp;</html>");
		} else {
			System.out.println("ok");
		}

	}

	// 鼠标划入事件
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == home) {
			home.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			home.setText("<html><font color='#336699' style='font-weight:bold'>" + "首页" + "</font>&nbsp;</html>");
		} else if (e.getSource() == baseData) {
			baseData.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			baseData.setText("<html><font color='#336699' style='font-weight:bold'>" + "基础数据" + "</font>&nbsp;</html>");
		} else if (e.getSource() == purchase_sale_stock) {
			purchase_sale_stock.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			purchase_sale_stock.setText(
					"<html><font color='#336699' style='font-weight:bold'>" + "进销存管理" + "</font>&nbsp;</html>");
		} else if (e.getSource() == userManager) {
			userManager.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			userManager
					.setText("<html><font color='#336699' style='font-weight:bold'>" + "用户管理" + "</font>&nbsp;</html>");
		}

	}

	// 鼠标划出事件
	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == home) {
			if (sign_home == 0) {
				home.setText("<html><font color='black'>" + "首页" + "</font>&nbsp;</html>");
			}
		} else if (e.getSource() == baseData) {
			if (sign_baseData == 0) {
				baseData.setText("<html><font color='black'>" + "基础数据" + "</font>&nbsp;</html>");
			}
		} else if (e.getSource() == purchase_sale_stock) {
			if (sign_purchase_sale_stock == 0) {
				purchase_sale_stock.setText("<html><font color='black'>" + "进销存管理" + "</font>&nbsp;</html>");
			}
		} else if (e.getSource() == userManager) {
			if (sign_userManager == 0) {
				userManager.setText("<html><font color='black'>" + "用户管理" + "</font>&nbsp;</html>");
			}
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

}
