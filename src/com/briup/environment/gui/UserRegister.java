package com.briup.environment.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
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
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.briup.environment.bean.UserBean;
import com.briup.environment.util.ConnectionFactory;
import com.briup.environment.util.SystemUtil;

public class UserRegister extends JFrame {
	
	 private static final long serialVersionUID = -4868535842017956748L;
	    private JPanel contentPane;
	    private JTextField yonghu;
	    private JPasswordField mima;
	    private JPasswordField again;
	    String newuser,newkey,newagain;
	    static Point origin = new Point();

	    public static void main(String[] args) {
	        EventQueue.invokeLater(new Runnable() {
	            public void run() {
	                try {
	                    UserRegister frame = new UserRegister();
	                    frame.setVisible(true);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	    }

	    public UserRegister() {
	    	
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       /* int w = (Toolkit.getDefaultToolkit().getScreenSize().width - 250) / 2;
	        int h = (Toolkit.getDefaultToolkit().getScreenSize().height - 400) / 2;
	        setBounds(w, h, 250, 400);*/
	        setBounds(700, 500, 850, 650);
	     //   setUndecorated(true);	
	        contentPane = new Pic();
			setContentPane(contentPane);
	        contentPane.setLayout(null);

	       
	        // 把背景图片显示在一个标签里面
	        JLabel label = new JLabel();
	        // 把标签的大小位置设置为图片刚好填充整个面板
	        label.setBounds(0, -1, this.getWidth(), this.getHeight());
	        // 把内容窗格转化为JPanel，否则不能用方法setOpaque()来使内容窗格透明
	        JPanel imagePanel = (JPanel) this.getContentPane();
	        imagePanel.setOpaque(false);
	        // 把背景图片添加到分层窗格的最底层作为背景
	        this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
	        //设置可见  */
	        this.addMouseListener(new MouseAdapter() {
	            // 按下(mousePressed
	            // 不是点击，而是鼠标被按下没有抬起)
	            public void mousePressed(MouseEvent e) {
	                // 当鼠标按下的时候获得窗口当前的位置
	                origin.x = e.getX();
	                origin.y = e.getY();
	            }
	        });
	        this.addMouseMotionListener(new MouseMotionAdapter() {
	            // 拖动（mouseDragged
	            // 指的不是鼠标在窗口中移动，而是用鼠标拖动）
	            public void mouseDragged(MouseEvent e) {
	                // 当鼠标拖动时获取窗口当前位置
	                Point p = getLocation();
	                // 设置窗口的位置
	                // 窗口当前的位置 + 鼠标当前在窗口的位置 - 鼠标按下的时候在窗口的位置
	                setLocation(p.x + e.getX() - origin.x, p.y + e.getY()
	                        - origin.y);
	            }
	        });

	        JLabel lblNewLabel = new JLabel("用户名");
	        lblNewLabel.setBounds(160, 270, 120, 30);
	        contentPane.add(lblNewLabel);
	        lblNewLabel.setForeground(Color.gray);
	        lblNewLabel.setFont(new Font("黑体", Font.BOLD, 24));

	        yonghu = new JTextField();
	        yonghu.setBounds(310, 265, 302, 45);
	        yonghu.setOpaque(false);
	        yonghu.setFont(new Font("黑体", Font.PLAIN, 22));
	        yonghu.setCaretColor(Color.BLACK);
	        yonghu.setForeground(Color.BLACK);

	        contentPane.add(yonghu);
	        yonghu.setColumns(10);
	        yonghu.setBorder(new LineBorder(Color.BLACK));

	        JLabel lblNewLabel_1 = new JLabel("密码");
	        lblNewLabel_1.setBounds(160,330,122,30);
	        contentPane.add(lblNewLabel_1);
	        lblNewLabel_1.setForeground(Color.GRAY);
	        lblNewLabel_1.setFont(new Font("黑体", Font.BOLD, 24));

	        mima = new JPasswordField();
	        mima.setBounds(310, 325, 302, 45);
	        contentPane.add(mima);
	        mima.setColumns(10);
	        mima.setOpaque(false);
	        mima.setBorder(new LineBorder(Color.BLACK));
	        mima.setFont(new Font("", Font.PLAIN, 22));
	        mima.setCaretColor(Color.BLACK);
	        mima.setForeground(Color.BLACK);

	        JLabel lblNewLabel_2 = new JLabel("确认密码");
	        lblNewLabel_2.setBounds(160,390,122,30);
	        contentPane.add(lblNewLabel_2);
	        lblNewLabel_2.setForeground(Color.GRAY);
	        lblNewLabel_2.setFont(new Font("黑体", Font.BOLD, 24));

	        again = new JPasswordField();
	        again.setBounds(310, 385, 302, 45);
	        contentPane.add(again);
	        again.setColumns(10);
	        again.setOpaque(false);
	        again.setBorder(new LineBorder(Color.BLACK));
	        again.setFont(new Font("", Font.PLAIN, 22));
	        again.setCaretColor(Color.BLACK);
	        again.setForeground(Color.BLACK);

	        final JLabel label1 = new JLabel("");
	        label1.setBounds(370, 440, 302, 45);
	        contentPane.add(label1);

	        JButton btnNewButton_3 = new JButton("注 册");
	        btnNewButton_3.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                newuser = yonghu.getText();
	                newkey = new String(mima.getPassword());
	                newagain =new String(again.getPassword());
	                System.out.println(newkey.equals(newagain)+"*********");
	                if(newkey.equals(newagain)){
	                    User user = new UserImpl();
	                    boolean flag= user.searchByName(newuser);
	                    if (flag==false){
	                        if(user.register(newuser,newkey)){
	                            SystemUtil.alert("注册成功!",1);
	                        }else {
	                            SystemUtil.alert("注册失败!",0);
	                        }
	                    }else {
	                        label1.setText("用户名已存在");
	                        label1.setForeground(Color.red);
	                        label1.setFont(new Font("黑体", Font.PLAIN, 12));
	                    }
	                }else {
	                    again.setText("");
	                    label1.setText("密码不一致");
	                    label1.setForeground(Color.red);
	                    label1.setFont(new Font("黑体", Font.PLAIN, 12));
	                }
	            }
	        });
	        btnNewButton_3.setBounds(320, 480, 300, 40);
	        contentPane.add(btnNewButton_3);
//	        btnNewButton_3.setForeground(Color.BLACK);
//	        btnNewButton_3.setBackground(Color.LIGHT_GRAY);
	        btnNewButton_3.setSize(100, 50);
	        btnNewButton_3.setFont(new Font("黑体",Font.PLAIN, 24));
	        btnNewButton_3.setBorder(BorderFactory.createLineBorder(Color.GRAY));

	        JButton btnNewButton = new JButton("登录");
	        btnNewButton.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                UserLogin frame = new UserLogin();
	                frame.setVisible(true);
	                dispose();
	            }
	        });
	        btnNewButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            }
	        });
	        btnNewButton.setBounds(480, 480, 300, 40);
	        btnNewButton.setFont(new Font("黑体",Font.PLAIN, 24));
	        contentPane.add(btnNewButton);
	        btnNewButton.setSize(100, 50);
	        btnNewButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	    }
	    
	    class Pic extends JPanel {
	    	protected void paintComponent(Graphics g) {
	    		super.paintComponent(g);
	    		ImageIcon icon = new ImageIcon("src/images/picture/User/用户注册头像.png");
	    		g.drawImage(icon.getImage(), 0, 0, this);
	    	}
	    }

}
