package com.dt.core.tool.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.imageio.ImageIO;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class ScanUtils {

	// http://www.codified.org.leefj.framework/maven-build-error-java-lang-runtimeexception-could-not-generate-dh-keypair/
	public static void download(String urlString, String filename, String savePath) throws Exception {
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 设置请求超时为5s
		con.setConnectTimeout(5 * 1000);
		// 输入流
		InputStream is = con.getInputStream();

		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		File sf = new File(savePath);
		if (!sf.exists()) {
			sf.mkdirs();
		}

		OutputStream os = new FileOutputStream(sf.getPath() + File.separator + filename);
		// 开始读取

		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();
	}

	// http://www.codified.org.leefj.framework/maven-build-error-java-lang-runtimeexception-could-not-generate-dh-keypair/
	public static String sbQrcode(String path) throws Exception {
		try {
			File f = new File(path);

			InputStream inputStream = new FileInputStream(f);

			BufferedImage image = ImageIO.read(inputStream);
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Result result = new MultiFormatReader().decode(binaryBitmap);// 对图像进行解码

			return result.getText();
		} catch (Exception e) {
			return "";
		}
	}

}
