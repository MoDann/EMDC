package com.briup.environment.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.briup.environment.bean.UserBean;
import com.briup.environment.util.SystemUtil;

public class UserChange extends JFrame {
	 private static final long serialVersionUID = 7550087215877587428L;
	    private JPanel contentPane;
	    private JTextField textField;
	    private JPasswordField textField_1;
	    private JPasswordField textField_2;
	    String userName,newkey,oldkey;
	    static Point origin = new Point();
	    /**
	     * Launch the application.
	     */
	    public static void main(String[] args) {
	        EventQueue.invokeLater(new Runnable() {
	            public void run() {
	                try {
	                	UserChange frame = new UserChange();
	                    frame.setVisible(true);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	    }

	    /**
	     * Create the frame.
	     */
	    public UserChange() {
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setBounds(700, 500, 850, 650);
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

	        textField = new JTextField();
	        textField.setBounds(310, 265, 302, 45);
	        textField.setOpaque(false);
	        textField.setFont(new Font("黑体", Font.PLAIN, 22));
	        textField.setCaretColor(Color.BLACK);
	        textField.setForeground(Color.black);

	        contentPane.add(textField);
	        textField.setColumns(10);
	        textField.setBorder(new LineBorder(Color.BLACK));

	        JLabel lblNewLabel_1 = new JLabel("旧密码");
	        lblNewLabel_1.setBounds(160,330,122,30);
	        contentPane.add(lblNewLabel_1);
	        lblNewLabel_1.setForeground(Color.GRAY);
	        lblNewLabel_1.setFont(new Font("黑体", Font.BOLD, 24));

	        textField_1 = new JPasswordField();
	        textField_1.setBounds(310, 325, 302, 45);
	        contentPane.add(textField_1);
	        textField_1.setColumns(10);
	        textField_1.setOpaque(false);
	        textField_1.setBorder(new LineBorder(Color.BLACK));
	        textField_1.setFont(new Font("", Font.PLAIN, 22));
	        textField_1.setCaretColor(Color.BLACK);
	        textField_1.setForeground(Color.BLACK);

	        JLabel lblNewLabel_2 = new JLabel("新密码");
	        lblNewLabel_2.setBounds(160,390,122,30);
	        contentPane.add(lblNewLabel_2);
	        lblNewLabel_2.setForeground(Color.GRAY);
	        lblNewLabel_2.setFont(new Font("黑体", Font.BOLD, 24));

	        textField_2 = new JPasswordField();
	        textField_2.setBounds(310, 385, 302, 45);
	        contentPane.add(textField_2);
	        textField_2.setColumns(10);
	        textField_2.setOpaque(false);
	        textField_2.setBorder(new LineBorder(Color.BLACK));
	        textField_2.setFont(new Font("", Font.PLAIN, 22));
	        textField_2.setCaretColor(Color.black);
	        textField_2.setForeground(Color.black);

	        final JLabel label1 = new JLabel("");
	        label1.setBounds(370, 440, 302, 45);
	        contentPane.add(label1);

	        JButton btnNewButton = new JButton("确 认");
	        btnNewButton.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent arg0) {
	                boolean keyflag = false;
	                userName = textField.getText();
	                oldkey = new String(textField_1.getPassword());
	                newkey = new String(textField_2.getPassword());
	                User user = new UserImpl();
	                UserBean userBean = new UserBean();
	                userBean.setUname(userName);
	                userBean.setUpass(oldkey);
	                keyflag = user.login(userName,oldkey);
	                if(keyflag){
	                    boolean b = user.changePwd(userBean, newkey);
	                    if (b){
	                        SystemUtil.alert("修改成功!",1);
	                        UserLogin frame = new UserLogin();
	                        frame.setVisible(true);
	                        dispose();
	                    }
	                    else {
	                        SystemUtil.alert("修改失败",0);
	                    }
	                }
	                else{
	                    label1.setText("原密码错误");
	                    label1.setForeground(Color.red);
	                    contentPane.add(label1);
	                    label1.setFont(new Font("黑体", Font.PLAIN, 24));
	                }
	            }
	        });
	        btnNewButton.setBounds(320, 480, 300, 40);
	        contentPane.add(btnNewButton);
	        btnNewButton.setForeground(Color.BLACK);
	        btnNewButton.setBackground(Color.LIGHT_GRAY);
	        btnNewButton.setSize(100, 50);
	        btnNewButton.setFont(new Font("黑体",Font.PLAIN, 24));
	        btnNewButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

	        JButton btnNewButton_1 = new JButton("取 消");
	        btnNewButton_1.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                UserLogin frame = new UserLogin();
	                frame.setVisible(true);
	                dispose();
	            }
	        });
	        btnNewButton_1.setBounds(480, 480, 300, 40);
	        contentPane.add(btnNewButton_1);
	        btnNewButton_1.setForeground(Color.BLACK);
	        btnNewButton_1.setBackground(Color.LIGHT_GRAY);
	        btnNewButton_1.setSize(100, 50);
	        btnNewButton_1.setFont(new Font("黑体",Font.PLAIN, 24));
	        btnNewButton_1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
	    }

	    class Pic extends JPanel {
	    	protected void paintComponent(Graphics g) {
	    		super.paintComponent(g);
	    		ImageIcon icon = new ImageIcon("src/images/picture/User/用户注册头像.png");
	    		g.drawImage(icon.getImage(), 0, 0, this);
	    	}
	    }
}
