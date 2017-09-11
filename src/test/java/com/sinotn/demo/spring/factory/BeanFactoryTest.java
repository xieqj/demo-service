package com.sinotn.demo.spring.factory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class BeanFactoryTest {
	private ClassPathXmlApplicationContext appCtx;
	@Autowired
	private BeanFactory beanFactory;

	@Before
	public void init(){
		this.appCtx=new ClassPathXmlApplicationContext("classpath:/com/sinotn/demo/spring/factory/spring-factory.xml");
		appCtx.getAutowireCapableBeanFactory().autowireBean(this);
	}

	@After
	public void after(){
		if(this.appCtx!=null){
			this.appCtx.close();
		}
	}

	@Test
	public void testNewBeanService(){
		for(int i=0;i<10;i++){
			System.out.println(this.beanFactory.newBeanService());
		}
	}
}
