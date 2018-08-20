package com.briup.environment.gui;

import java.util.List;

import com.briup.environment.bean.UserBean;


public interface Login {
	/*
	 * 管理员信息验证通过进入物联网数据中心后台操作页面，按照时间和环境种类查询和统计采集数据
	 */
	public List<UserBean> login();

}
