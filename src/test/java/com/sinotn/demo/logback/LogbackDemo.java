package com.sinotn.demo.logback;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackDemo {

	@Test
	public void debug(){
		Logger log=LoggerFactory.getLogger("com.sinotn.file.daily");
		log.debug("com.sinotn.file.daily debug");
	}
}
