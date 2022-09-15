package org.must.moneytools.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface WrkTmcaService {
	/**
	 * 回傳進度用
	 * @param request
	 * @return
	 */
	String getLodingProgress();
	
	/**
	 * 檔案上傳及檔案處理，配合 getProgress 可取回處理進度
	 * @param request
	 * @param response
	 * @param file
	 * @return
	 */
	String uploadFile(HttpServletRequest request, HttpServletResponse response, String ontest, MultipartFile file);

	/**
	 * 檔案下載
	 * @param request
	 * @param response
	 * @param filetime
	 * @return
	 */
	String downloadFile(HttpServletRequest request, HttpServletResponse response, String filetime);
}
