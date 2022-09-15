package org.must.moneytools.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.must.moneytools.service.WrkTmcaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/moneytools")
@Slf4j
public class WrkTmcaExcelImpController {
	@Resource
	private WrkTmcaService WrkTService;
	
	@GetMapping("imp_tmca_to_money") //首頁
	public void imp_tmca_to_money(HttpServletRequest request) {
		log.info("imp_tmca_to_money page...");
		//return "/moneytools/imp_tmca_to_money";
	}
	
	/**
	 * 回傳進度用
	 * @param request
	 * @return
	 */
	@RequestMapping("/get_progress")
	@ResponseBody
	public String getProgress(HttpServletRequest request) {
		return WrkTService.getLodingProgress();
	}
	
	/**
	 * TMCA Excel 分析，Json Data POST Api Url to Money System
	 * @param request
	 * @param response
	 * @param ontest Y:測試機 N:正式機
	 * @param file
	 * @return
	 */
	@RequestMapping("/upload")
	@ResponseBody
	public String upload(HttpServletRequest request, HttpServletResponse response, @RequestParam("ontest") String ontest, @RequestParam("file") MultipartFile file) {
		log.info("upload page...");
		return WrkTService.uploadFile(request, response, ontest, file);
	}
	
	/**
	 * 檔案下載
	 * @param request
	 * @param response
	 * @param filetime
	 * @return String
	 */
	@RequestMapping("/download")
	@ResponseBody
	public String download(HttpServletRequest request, HttpServletResponse response, @RequestParam("filetime") String filetime) {
		return WrkTService.downloadFile(request, response, filetime);
	}
}
