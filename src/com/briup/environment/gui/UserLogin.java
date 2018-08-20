package com.briup.environment.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.briup.environment.bean.UserBean;
import com.briup.environment.util.ConnectionFactory;

/**
 * 用户登录
 * @author ASUS
 *
 */
public class UserLogin extends JFrame{

	private static final long serialVersionUID = 1L;
	private JFrame jFrame;
	
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	public static String userflag = null;
	String userName, password;
	boolean flag = false;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserLogin frame = new UserLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// 创建frame
	public UserLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(700, 500, 850,650);
		contentPane = new win();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		jFrame = new JFrame();
		// 设置窗体左上角图标
		Image a = jFrame.getToolkit().getImage(
				"src/images/picture/user/登录窗口图标.png");
		jFrame.setIconImage(a);
		
		JLabel label = new JLabel();
		label.setBounds(0, -1, this.getWidth(), this.getHeight());
		JPanel imagePanel = (JPanel) this.getContentPane();
		imagePanel.setOpaque(false);
		this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		JButton btnNewButton = new JButton("注册账号");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				UserRegister frame = new UserRegister();
				frame.setVisible(true);
				System.out.println("注册账号");
				dispose();
			}
		});

		btnNewButton.setBorderPainted(false);
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setForeground(Color.lightGray);
		btnNewButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		btnNewButton.setBounds(632, 275, 112, 25);
		btnNewButton.addMouseMotionListener(new MouseMotionAdapter() {

		});
		contentPane.setLayout(null);
		contentPane.add(btnNewButton);
		textField = new JTextField();
		textField.setBounds(310, 265, 302, 45);
		contentPane.add(textField);
		textField.setForeground(Color.BLACK);
		/*textField.setColumns(10);
		textField.setBorder(null);*/
		textField_1 = new JPasswordField();
		textField_1.setBounds(310,350,302,45);
		textField_1.setForeground(Color.BLACK);
		contentPane.add(textField_1);
		/*textField_1.setColumns(10);
		textField_1.setBorder(null);*/
		JLabel lblNewLabel = new JLabel("用户名:");
		lblNewLabel.setBounds(160, 270, 120, 30);
		lblNewLabel.setFont(new Font("微软雅黑", Font.BOLD, 32));
		lblNewLabel.setForeground(Color.GRAY);
		contentPane.add(lblNewLabel);
		JLabel lblNewLabel_1 = new JLabel("密码:");
		lblNewLabel_1.setBounds(160,350,122,30);
		lblNewLabel_1.setForeground(Color.GRAY);
		lblNewLabel_1.setFont(new Font("微软雅黑", Font.BOLD, 32));
		contentPane.add(lblNewLabel_1);
		final JLabel label1 = new JLabel("");
		label1.setBounds(370, 400, 302, 45);
		contentPane.add(label1);
		final JButton btnNewButton_1 = new JButton("登录");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				userName = textField.getText();
				password = textField_1.getText();
				User user = new UserImpl();
				boolean flag = user.login(userName, password);
				contentPane.requestFocus(true);
				btnNewButton_1.setEnabled(true);
				if (flag == true) {
					userflag = userName;
					MainWindow window = new MainWindow();
					// 初始化一下数据
					window.selectDataInfo();
					dispose();
				} else {
					label1.setText("用户名或密码不正确!");
					label1.setForeground(Color.red);
					contentPane.add(label1);
					label1.setFont(new Font("微软雅黑", Font.PLAIN, 16));
				}
			}
		});

		btnNewButton_1.setBounds(310, 440, 300, 40);
		btnNewButton_1.setFont(new Font("微软雅黑", Font.BOLD, 24));
		btnNewButton_1.setContentAreaFilled(false);
		btnNewButton_1.setForeground(Color.GRAY);
		btnNewButton_1.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		contentPane.add(btnNewButton_1);

		JButton button = new JButton("更改密码");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserChange frame = new UserChange();
				frame.setVisible(true);
				dispose();
			}
		});
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setForeground(Color.lightGray);
		button.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		button.setBounds(632, 360, 112, 25);
		contentPane.add(button);
	}

	class win extends JPanel {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			ImageIcon icon = new ImageIcon("src/images/picture/user/用户登录头像.png");
			g.drawImage(icon.getImage(), 0, 0, this);
		}
	}
}
