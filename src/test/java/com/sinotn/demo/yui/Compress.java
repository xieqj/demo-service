package com.sinotn.demo.yui;

import org.junit.Test;

import com.yahoo.platform.yui.compressor.Bootstrap;

public class Compress {

	@Test
	public void testJsCompress() throws Exception{
		String charset="utf-8";
		String srcpath="d:/index.js";
		String destpath="c:/index.min.js";
		String[] args={"--type","js","--charset",charset,"-o",destpath,srcpath};
		Bootstrap.main(args);
	}
}
