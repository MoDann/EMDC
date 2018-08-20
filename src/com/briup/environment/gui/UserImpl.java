package com.briup.environment.gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.briup.environment.bean.UserBean;
import com.briup.environment.util.ConnectionFactory;

public class UserImpl implements User {
	
	private static String loginSql="SELECT * FROM E_USERS WHERE uname=? and upassword=?";
    private static String searchByNameSql="SELECT* FROM E_USERS WHERE uname=?";
    private static String registerSql="insert into E_USERS values(user_seq.nextval,?,?)";
    private static String changePwdSql="update E_USERS set upassword = ? where uname = ?";
    
    public boolean login(String username, String pwd) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(loginSql);
            ps.setString(1,username);
            ps.setString(2,pwd);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean searchByName(String username) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(searchByNameSql);
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(String username, String pwd) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(registerSql);
            ps.setString(1,username);
            ps.setString(2,pwd);
            int i = ps.executeUpdate();
            if (i>0){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean changePwd(UserBean user, String newPwd) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(changePwdSql);
            ps.setString(1,newPwd);
            ps.setString(2,user.getUname());
            int i = ps.executeUpdate();
            if (i>0){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(new UserImpl().login("admin","12456"));
    }
}
