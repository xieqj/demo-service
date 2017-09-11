package com.sinotn.demo.spring.factory;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultBeanFactory implements BeanFactory,ApplicationContextAware {
	private ApplicationContext appCtx;

	public DefaultBeanFactory(){}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appCtx=applicationContext;
	}

	@Override
	public BeanService newBeanService() {
		Map<String,BeanService> map=this.appCtx.getBeansOfType(BeanService.class);
		if(map==null||map.isEmpty()){
			return new DefaultBeanService();
		}
		return map.values().iterator().next();
	}
}
