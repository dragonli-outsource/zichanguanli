package com.dt.core.tool.lang;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.LangKit;

/**
 * @author: algernonking
 * @date: 2018年1月25日 下午1:01:12
 * @Description: TODO
 */
public final class CharsetUtil {

	public static final String UTF8 = "UTF-8";
	public static final String GBK = "GBK";
	public static final String GB2312 = "GB2312";
	public static final String ASCII = "US-ASCII";
	public static final String ISO_8859_1 = "ISO-8859-1";
	public static final String UTF16BE = "UTF-16BE";
	public static final String UTF16LE = "UTF-16LE";
	public static final String UTF16 = "UTF-16";
	public static final Charset CHARSET_UTF8 = Charset.forName(UTF8);
	public static final Charset CHARSET_GBK = Charset.forName(GBK);
	public static final Charset CHARSET_GB2312 = Charset.forName(GB2312);
	public static final Charset CHARSET_ASCII = Charset.forName(ASCII);
	public static final Charset CHARSET_ISO_8859_1 = Charset.forName(ISO_8859_1);
	public static final Charset CHARSET_UTF16 = Charset.forName(UTF16);
	public static final Charset CHARSET_UTF16BE = Charset.forName(UTF16BE);
	public static final Charset CHARSET_UTF16LE = Charset.forName(UTF16LE);

	public static String defaultEncoding() {
		return Charset.defaultCharset().name();
	}

	public static String encodeURIComponent(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw LangKit.wrapThrow(e);
		}
	}

	public static String decodeURIComponent(String str) {
		try {
			return URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw LangKit.wrapThrow(e);
		}
	}

	/**
	 * 
	 * 转换为Charset对象
	 * 
	 * @param charsetName
	 *            字符集，为空则返回默认字符集
	 * 
	 * @return Charset
	 * 
	 * @throws UnsupportedCharsetException
	 *             编码不支持
	 * 
	 */
	public static Charset charset(String charsetName) throws UnsupportedCharsetException {
		return ToolUtil.isEmpty(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
	}

	/**
	 * 
	 * 转换字符串的字符集编码
	 * 
	 * @param source
	 *            字符串
	 * 
	 * @param srcCharset
	 *            源字符集，默认ISO-8859-1
	 * 
	 * @param destCharset
	 *            目标字符集，默认UTF-8
	 * 
	 * @return 转换后的字符集
	 * 
	 */
	public static String convert(String source, String srcCharset, String destCharset) {
		return convert(source, Charset.forName(srcCharset), Charset.forName(destCharset));
	}

	/**
	 * 
	 * 转换字符串的字符集编码<br>
	 * 
	 * 当以错误的编码读取为字符串时，打印字符串将出现乱码。<br>
	 * 
	 * 此方法用于纠正因读取使用编码错误导致的乱码问题。<br>
	 * 
	 * 例如，在Servlet请求中客户端用GBK编码了请求参数，我们使用UTF-8读取到的是乱码，此时，使用此方法即可还原原编码的内容
	 * 
	 * <pre>
	
	 * 客户端 -》 GBK编码 -》 Servlet容器 -》 UTF-8解码 -》 乱码
	
	 * 乱码 -》 UTF-8编码 -》 GBK解码 -》 正确内容
	 * 
	 * </pre>
	 * 
	 * 
	 * 
	 * @param source
	 *            字符串
	 * 
	 * @param srcCharset
	 *            源字符集，默认ISO-8859-1
	 * 
	 * @param destCharset
	 *            目标字符集，默认UTF-8
	 * 
	 * @return 转换后的字符集
	 * 
	 */
	public static String convert(String source, Charset srcCharset, Charset destCharset) {
		if (null == srcCharset) {
			srcCharset = StandardCharsets.ISO_8859_1;
		}

		if (null == destCharset) {
			destCharset = StandardCharsets.UTF_8;
		}

		if (ToolUtil.isEmpty(source) || srcCharset.equals(destCharset)) {
			return source;
		}
		return new String(source.getBytes(srcCharset), destCharset);
	}

	/**
	 * 
	 * 系统默认字符集编码
	 * 
	 * 
	 * 
	 * @return 系统字符集编码
	 * 
	 */
	public static String defaultCharsetName() {
		return defaultCharset().name();
	}

	/**
	 * 
	 * 系统默认字符集编码
	 * 
	 * 
	 * 
	 * @return 系统字符集编码
	 * 
	 */
	public static Charset defaultCharset() {
		return Charset.defaultCharset();
	}

}
