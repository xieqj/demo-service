package com.sinotn.spring.jdbc;

import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.sinotn.tools.junit.SpringSupperTest;

public class JdbcDaoTest extends SpringSupperTest implements ApplicationContextAware{
	@Autowired
	private JdbcDao jdbcDao;

	@Test
	public void testRunTest(){
		//this.jdbcDao.runTest();
		System.out.println("===================");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println(applicationContext);
	}
}
