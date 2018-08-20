package com.briup.environment.dao;

import java.util.Collection;
import java.util.List;

import com.briup.environment.bean.Environment;
import com.briup.environment.bean.MaxMinAvg;

/**
 * 后台api
 * @author ASUS
 *
 */
public interface Api {
	Collection<Environment> getData(int day,int type) throws Exception;
    List<MaxMinAvg> getMaxMinAvg(int day, int type) throws Exception;
    List<Integer> getDay();

}
