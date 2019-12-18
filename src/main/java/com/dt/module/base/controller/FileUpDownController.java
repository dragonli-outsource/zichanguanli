package com.dt.module.base.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseController;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.sql.Insert;
import com.dt.core.tool.util.DbUtil;
import com.dt.core.tool.util.SmartImageScalr;
import com.dt.core.tool.util.ToolUtil;

@Controller
@RequestMapping("/api/file")
public class FileUpDownController extends BaseController {

	private static Logger _log = LoggerFactory.getLogger(FileUpDownController.class);

	// curl http://127.0.01:8080/dt/api/file/fileupload.do?uuid=image_111&bus=news
	// -F "file[0]=@/Users/algernonking/Desktop/a.jpeg" -H
	// "Content-Type:multipart/form-data"
	@RequestMapping("/fileupload.do")
	@ResponseBody
	@Acl(value = Acl.ACL_ALLOW, info = "上传")
	public R fileUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String bus = request.getParameter("bus");
		if (ToolUtil.isEmpty(bus)) {
			return R.FAILURE("请输入业务号");
		}
		String uuid = request.getParameter("uuid");
		if (ToolUtil.isEmpty(uuid)) {
			uuid = db.getUUID();
		}

		Rcd fileinfo = db.uniqueRecord("select * from sys_file_conf where id=? ", bus);
		if (fileinfo == null) {
			return R.FAILURE("数据库并未配置");
		}
		if (!"Y".equals(fileinfo.getString("is_used"))) {
			return R.FAILURE("该上传配置选型未启用");
		}
		String limit = fileinfo.getString("limit_str") == null ? "*" : fileinfo.getString("limit_str").trim();
		String type = fileinfo.getString("type") == null ? "unknow" : fileinfo.getString("type");
		String keepname = fileinfo.getString("keepname") == null ? "0" : fileinfo.getString("keepname");
		// 从数据库中获取
		String bus_path = fileinfo.getString("path");
		String end_path = "";
		File f = null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("file[0]");
		String OriginalFilename = file.getOriginalFilename();
		String curFilename = "";
		String dir = getWebRootDir() + ".." + File.separatorChar;
		Calendar cale = Calendar.getInstance();
		String path = "upload" + File.separatorChar + bus_path + File.separatorChar + cale.get(Calendar.YEAR) + ""
				+ File.separatorChar + "" + (cale.get(Calendar.MONTH) + 1) + "" + File.separatorChar + ""
				+ cale.get(Calendar.DAY_OF_MONTH) + "" + File.separatorChar;
		_log.info("Upload Dir:" + dir);
		_log.info("Type:" + type + ",Upload File:" + OriginalFilename);
		_log.info("Upload Path:" + path);

		if ("image".equals(type)) {
			// 获得第1张图片（根据前台的name名称得到上传的文件）
			if (!(OriginalFilename.toUpperCase().endsWith("JPEG") || OriginalFilename.toUpperCase().endsWith("GIF")
					|| OriginalFilename.toUpperCase().endsWith("JPG") || OriginalFilename.toUpperCase().endsWith("PNG")
					|| OriginalFilename.toUpperCase().endsWith("BMP"))) {
				return R.FAILURE("图片后缀名不正确");
			}
			curFilename = "Image_" + uuid + ".png";
			end_path = path + curFilename;
			f = new File(dir + end_path);
			R vaf = valid(f);
			if (!vaf.isSuccess()) {
				return vaf;
			}

		} else if ("file".equals(type)) {
			// 判断上传类型
			if (!ifFileRight(OriginalFilename, limit)) {
				return R.FAILURE("上传的文件后缀不正确");
			}
			if ("1".equals(keepname)) {
				curFilename = OriginalFilename;
				end_path = path + OriginalFilename;
			} else {
				curFilename = "File_" + uuid + ".file";
				end_path = path + curFilename;
			}
			f = new File(dir + end_path);
			R vaf = valid(f);
			if (!vaf.isSuccess()) {
				return vaf;
			}
		} else {
			return R.FAILURE("不支持该上传类型");
		}

		_log.info("File:" + f.getAbsolutePath());
		_log.info("FileParent:" + f.getParentFile().getAbsolutePath());
		file.transferTo(f);
		Insert ins = new Insert("sys_files");
		ins.set("id", uuid);
		ins.set("path", end_path);
		ins.set("type", type);
		ins.setIf("filename", curFilename);
		ins.setIf("filename_o", OriginalFilename);
		ins.setSE("cdate", DbUtil.getDbDateString(db.getDBType()));
		ins.set("bus", bus);
		db.execute(ins);
		return R.SUCCESS_OPER();
	}

	public boolean ifFileRight(String name, String limit) {
		if (limit.startsWith("*")) {
			return true;
		}
		String[] limit_arr = limit.split(",");
		for (int i = 0; i < limit_arr.length; i++) {
			if (name.toUpperCase().endsWith(limit_arr[i].toUpperCase())) {
				return true;
			}
		}
		return false;
	}

	@RequestMapping("/filedown.do")
	@Acl(value = Acl.ACL_ALLOW, info = "下载")
	public void filedown(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sql = "select * from sys_files where id=?";
		Rcd set = db.uniqueRecord(sql, id);
		String fileurl = set.getString("path");
		String filename = ToolUtil.isEmpty(set.getString("filename_o")) ? "unknow.file" : set.getString("filename_o");
		String filePath = getWebRootDir() + ".." + File.separatorChar + fileurl;
		File file = new File(filePath);
		System.out.println(file.getName());
		if (file.exists()) {

			response.setHeader("content-type", "application/octet-stream");
			response.setContentType("application/octet-stream");
			try {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + new String(filename.getBytes("utf-8"), "ISO-8859-1"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			byte[] buffer = new byte[1024];
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				OutputStream os = response.getOutputStream();
				int i = bis.read(buffer);
				while (i != -1) {
					os.write(buffer, 0, i);
					i = bis.read(buffer);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	@RequestMapping("/imagedown.do")
	@Acl(value = Acl.ACL_ALLOW, info = "下载")
	public void imagedown(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (ToolUtil.isEmpty(id)) {
			id = "none";
		}
		// id = "F607ABCD1F4583F41EE166A501572FF6";
		String sql = "select * from sys_files where id=?";
		Rcd set = db.uniqueRecord(sql, id);
		File file = null;
		String filePath = "";
		String ct = "";
		try {
			String type = set.getString("type");
			if ("image".equals(type)) {
				ct = "image/jpeg";
			}
			String fileurl = set.getString("path");
			filePath = getWebRootDir() + ".." + File.separatorChar + fileurl;
			_log.info("filePath" + filePath);
			String heightStr = request.getParameter("height");
			if (ToolUtil.isEmpty(heightStr)) {
				heightStr = request.getParameter("h");
			}
			String widthStr = request.getParameter("width");
			if (ToolUtil.isEmpty(widthStr)) {
				widthStr = request.getParameter("w");
			}
			String crop = request.getParameter("crop");
			String fit = request.getParameter("fit");
			fit = "width";
			crop = "TL";
			int width = 0;
			int height = 0;
			try {
				width = Integer.parseInt(widthStr);
			} catch (Exception e) {
			}
			;
			try {
				height = Integer.parseInt(heightStr);
			} catch (Exception e) {
			}
			file = new File(filePath);
			_log.info("图片位置:" + file.getAbsolutePath());
			if (!file.exists()) {
				_log.info("图片不存在,去获取默认图片");
				file = getDefaultImageFile();
			} else {
				_log.info("图片存在,开始裁截");
				int ow = 0;
				int oh = 0;
				_log.info("height" + height);
				_log.info("width" + width);
				file = scale5(id, filePath, height, width, ow, oh, fit, crop);
				if (file == null || !file.exists()) {
					_log.info("裁剪失败,去获取默认图片");
					file = getDefaultImageFile();
				}
			}
		} catch (Exception e) {
			file = getDefaultImageFile();
		}
		try {
			BufferedImage input = ImageIO.read(file);
			response.reset();
			response.setContentType(ct);
			ImageIO.write(input, "png", response.getOutputStream());
		} catch (Exception e) {
			_log.info("获取图片失败:" + filePath);
		}

	}

	public File getDefaultImageFile() {
		_log.info("获取默认图片:" + getWebRootDir() + File.separatorChar + "image" + File.separatorChar + "blank.jpg");
		return new File(getWebRootDir() + File.separatorChar + "image" + File.separatorChar + "blank.jpg");
	}

	public static final String THUMBS = "thumbs";

	public final static File scale5(String id, String srcImageFile, int height, int width, int origWidth, int origHeigh,
			String fitType, String crop) throws IOException {
		if (height <= 0 && width <= 0)
			return new File(srcImageFile);
		File srcFile = new File(srcImageFile);
		String format = null;
		int i = srcFile.getName().lastIndexOf(".");
		if (i > 0) {
			format = srcFile.getName().substring(i + 1);
		}
		if (format == null)
			format = "jpg";
		BufferedImage srcImage = null;
		SmartImageScalr sis = null;
		if (origWidth > 0 && origHeigh > 0) {
			sis = new SmartImageScalr(width, height, fitType, crop, origWidth, origHeigh);
		} else {
			srcImage = ImageIO.read(srcFile);
			sis = new SmartImageScalr(width, height, fitType, crop, srcImage);
		}
		String thumbFilePath = sis.getFileName(srcFile, format);
		thumbFilePath = srcFile.getParentFile().getAbsolutePath() + File.separatorChar + THUMBS + File.separatorChar
				+ thumbFilePath;
		File thumbFile = new File(thumbFilePath);
		if (thumbFile.exists())
			return thumbFile;
		// if (origWidth <= 0 || origHeigh <= 0) {
		//
		// JSONObject info = new JSONObject();
		// info.put("width", srcImage.getWidth());
		// info.put("height", srcImage.getHeight());
		//
		// // DB.instance().execute("update sys_file set infos=? where
		// // id=?",info.toJSONString(),id);
		// }
		if (srcImage == null)
			srcImage = ImageIO.read(srcFile);
		BufferedImage thumbImage = sis.scaleAndCrop(srcImage);
		if (!thumbFile.getParentFile().exists())
			thumbFile.getParentFile().mkdirs();
		ImageIO.write(thumbImage, format, thumbFile);
		return thumbFile;
	}

	private static R valid(File file) {
		File parentPath = file.getParentFile();
		if ((!parentPath.exists()) && (!parentPath.mkdirs())) {
			return R.FAILURE("创建文件失败");
		}
		if (!parentPath.canWrite()) {
			return R.FAILURE("不可写");
		}
		return R.SUCCESS_OPER();
	}

	public static String getWebRootDir() {
		return ToolUtil.getRealPathInWebApp("");
	}
}
