package com.briup.environment.gui;

import com.briup.environment.bean.UserBean;

public interface User {
	
    boolean login(String username,String pwd);
    boolean searchByName(String username);
    boolean register(String username,String pwd);
    boolean changePwd(UserBean user, String newPwd);

}
