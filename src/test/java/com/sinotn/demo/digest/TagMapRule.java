package com.sinotn.demo.digest;

import java.util.Map;

import org.apache.commons.digester.Rule;

public class TagMapRule extends Rule {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void body(String namespace, String name, String text) throws Exception {
		Map map=(Map)this.digester.peek();
		map.put(name, text);
	}

}
