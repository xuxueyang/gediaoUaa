/**
 *
 */
package uaa.web.rest.upload;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.naming.SizeLimitExceededException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author xiongp
 *
 */
public class UploadUtils {

	private static final String DOT = ".";

	private static final long MAX_FILE = 20 * 1024 * 1024;
	/**
	 * 上传限制为20M
	 *
	 * @param request
	 * @return
	 *
	 * 		<pre>
	 * 使用方式
	 * MultipartFile file = multiRequest.getFile("file");获取文件
	 * InputStream input = file.getInputStream();获取文件流
	 * File source = new File(filePath);file.transferTo(source);保存文件到本地
	 * String name = multiRequest.getParameter("name");获取参数
	 *         </pre>
	 */
	public static MultipartHttpServletRequest createMultipartHttpServletRequest(HttpServletRequest request) throws MaxUploadSizeExceededException,SizeLimitExceededException{
		return createMultipartHttpServletRequest(request, MAX_FILE);
	}

	/**
	 * @param request
	 * @param maxSize
	 *            上次限制大小 单位 Byte
	 * @return
	 *
	 * <pre>
	 * 使用方式
	 * MultipartFile file = multiRequest.getFile("file");获取文件
	 * InputStream input = file.getInputStream();获取文件流
	 * File source = new File(filePath);file.transferTo(source);保存文件到本地
	 * String name = multiRequest.getParameter("name");获取参数
	 * </pre>
	 */
	public static MultipartHttpServletRequest createMultipartHttpServletRequest(HttpServletRequest request, long maxSize) throws MaxUploadSizeExceededException,SizeLimitExceededException{
		CommonsMultipartResolver resolver = null;
		MultipartHttpServletRequest multiRequest = null;
		resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("UTF-8");
		resolver.setMaxUploadSize(maxSize);// 20M
		resolver.setServletContext(request.getSession().getServletContext());
		resolver.setMaxInMemorySize(1024 * 1024);
		multiRequest = resolver.resolveMultipart(request);
		return multiRequest;
	}

	public static JFile tranFile(MultipartFile file, String relativePath) throws IOException{
		String realName = file.getOriginalFilename();
		JFile jfile = new JFile();
		String fileNameNoExtension = null;
		String fileExtension = null;
		if(StringUtils.contains(file.getOriginalFilename(), DOT)){
			int lastDot = StringUtils.lastIndexOf(realName, DOT);
			fileNameNoExtension = realName.substring(0, lastDot);
			fileExtension = realName.substring(lastDot+1, realName.length());
		}
		jfile.setRelativePath(relativePath);
		jfile.setFileNameNoExtension(fileNameNoExtension);
		jfile.setFileExtension(fileExtension);
		jfile.setFileContent(file.getBytes());
		return jfile;
	}
	public static JFile tranFile(MultipartFile file) throws IOException{
		return tranFile(file, null);
	}
}
