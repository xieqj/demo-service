package com.sinotn.demo.java;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageTest {

	@Test
	public void detectImgType()throws Exception{
		String path="d:/cc36b555-9501-3495-bb12-aa23afe58625.gif";
		File file=new File(path);
		ImageInputStream iis = ImageIO.createImageInputStream(file);
		Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
		String formatName="unknown";
		if (iter.hasNext()) {
			formatName=iter.next().getFormatName();
		}
		iis.close();
		System.out.println(formatName);
	}

	@Test
	public void imgRect() throws Exception{
		File file=new File("d:/cc36b555-9501-3495-bb12-aa23afe58625.gif");
		BufferedImage img=ImageIO.read(file);
		System.out.println(img.getWidth());
		System.out.println(img.getHeight());

	}

	@Test
	public void imgBase64() throws Exception{
		String path="d:/socket2_out.jpg";
		FileInputStream fis=new FileInputStream(path);
		byte[] buf=new byte[fis.available()];
		fis.read(buf);
		fis.close();
		System.out.println(buf.length);
		String base64=Base64.encodeBase64String(buf);
		System.out.println("["+base64+"]");
	}



	@Test
	public void imgScale() throws Exception{
		//http://cuisuqiang.iteye.com/blog/2045855
		File src=new File("d:/IMG_20131105_082746.jpg");
		File dest=new File("d:/scale_0.2.jpg");
		float rate=0.5f;

		BufferedImage imgSrc=ImageIO.read(src);

		int width=(int)(imgSrc.getWidth()*rate);
		int height=(int)(imgSrc.getHeight()*rate);
		// 开始读取文件并进行压缩
		BufferedImage imgDest = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		//颜色转换（黑白）
		/*ColorConvertOp cco = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		cco.filter(imgSrc, imgDest);*/

		imgDest.getGraphics().drawImage(imgSrc.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);


		FileOutputStream out = new FileOutputStream(dest);
		//ByteArrayOutputStream out=new ByteArrayOutputStream(1024);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam jep=encoder.getDefaultJPEGEncodeParam(imgDest);
		jep.setQuality(0.2f, false);
		encoder.setJPEGEncodeParam(jep);
		encoder.encode(imgDest);
		//String base64=Base64.encodeBase64String(out.toByteArray());
		//System.out.println(base64);
		out.close();
	}

	public void imgBlackWhite(){

		/*
		 String  srcFileName="x:\\dd\\test.jpg";//源图像路径
String  destFileName="x:\\dd\\test.jpg";//源图像路径
BufferedImage originalPic = ImageIO.read(new FileInputStream(srcFileName));

int imageWidth = originalPic.getWidth();
int imageHeight = originalPic.getHeight();
//产生新的图像缓冲对象
BufferedImage newBufferedImage = new BufferedImage(imageWidth, imageHeight,
  BufferedImage.TYPE_3BYTE_BGR);
//颜色转换（黑白）
ColorConvertOp cco = new ColorConvertOp(ColorSpace
  .getInstance(ColorSpace.CS_GRAY), null);
cco.filter(originalPic, newBufferedImage);

FileOutputStream fos = new FileOutputStream(destFileName);
if (destFileName.toLowerCase().endsWith(".gif")) {
 ImageIO.write(newBufferedImage, "gif", fos);
}
if (destFileName.toLowerCase().endsWith(".png")) {
 ImageIO.write(newBufferedImage, "png", fos);
}
//针对jpeg、jpg设置输出编码
if ((destFileName.toLowerCase().endsWith(".jpg"))
  || (destFileName.toLowerCase().endsWith(".jpeg"))) {
 JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
 JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(newBufferedImage);
 param.setQuality(1.0F, false);
 encoder.encode(newBufferedImage);
}
fos.flush();
fos.close();
重点解释一下：
针对jpeg、jpg设置输出编码
JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
		 */
	}

	@Test
	public void writeJpeg() throws Exception{
		String inputImg="d:/IMG_20131105_082746.jpg";
		String outImg="d:/socket2_out.jpg";
		Iterator<ImageWriter> it = ImageIO.getImageWritersBySuffix("jpg");
		BufferedImage image=ImageIO.read(new File(inputImg));
		if (it.hasNext()) {
			FileImageOutputStream fileImageOutputStream = new FileImageOutputStream(new File(outImg));
			ImageWriter iw = it.next();
			ImageWriteParam iwp = iw.getDefaultWriteParam();
			iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iwp.setCompressionQuality(50 / 100f);
			iw.setOutput(fileImageOutputStream);
			//iw.addIIOWriteProgressListener(listener);
			iw.write(null, new IIOImage(image, null, null), iwp);
			iw.dispose();
			fileImageOutputStream.flush();
			fileImageOutputStream.close();
		}
	}
}
