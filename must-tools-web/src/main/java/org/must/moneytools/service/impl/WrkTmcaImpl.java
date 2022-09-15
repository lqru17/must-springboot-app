package org.must.moneytools.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.must.moneytools.common.WrkUtilService;
import org.must.moneytools.method.TmcaMethod;
import org.must.moneytools.model.FileProgressDO;
import org.must.moneytools.model.ImpExcelTmcaDO;
import org.must.moneytools.model.WriteTmcaDO;
import org.must.moneytools.service.WrkTmcaService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WrkTmcaImpl implements WrkTmcaService, TmcaMethod{
	private RestTemplate restTemplate = new RestTemplate();
	/**
	 * 檔案儲存路徑
	 */
	private String filePath;
	private String FolderPath;
	/**
	 * 檔案儲存路徑
	 */
	private String impfilename;
	private LocalDateTime LocalTime;
	private int pageNum = 0;
	private ImpExcelTmcaDO prevT = new ImpExcelTmcaDO();
	private FileProgressDO fpc = new FileProgressDO();
	private String prevSongID = "", nowSongID = "";
	private HashMap<String, Object> tempMap = new HashMap();
	private List tempList = new ArrayList();
	private List<HashMap<String, Object>> songList = new ArrayList<HashMap<String, Object>>();
	
	/**
	 * 回傳進度用
	 * @param request
	 * @return
	 */
	@Override
	public String getLodingProgress() {
		return JSON.toJSONString(fpc);
	}
	
	/**
	 * 檔案上傳及檔案處理，配合 getProgress 可取回處理進度
	 * @param request
	 * @param response
	 * @param file
	 * @return
	 */
	@Override
	public String uploadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("ontest") String ontest,
			@RequestParam("file") MultipartFile file) {
		String result = "";

		log.info("ontest......"+ontest);
		if(ontest == null)
			ontest = "Y"; //預設為 測試
		fpc = new FileProgressDO();
		songList = new ArrayList<HashMap<String, Object>>();
		tempList = new ArrayList();
		tempMap = new HashMap();
		prevT = new ImpExcelTmcaDO();
		prevSongID = "";
		nowSongID = "";
		pageNum = 0;
		// 判斷檔案是否為空
		HashMap retmap = new HashMap();
		retmap.put("code", "200");
		try {
			if (file.isEmpty()) {
				throw new Exception ("檔案為空");
			}
			/**
			 * 更新時間
			 */
			LocalTime = LocalDateTime.now();
			try {
		        
		        //刪除之前的檔案
		        File dfile = new File( WrkUtilService.UPLOAD_DIR + "/MUST_FILE/ImpFileTemp");
		        WrkUtilService.deleteDirectory(dfile); //目錄下所有檔案
		        log.info("刪除之前的檔案...Success");
				
				// 上傳的檔案名稱
				impfilename = file.getOriginalFilename();
				// 檔案儲存路徑
				FolderPath = WrkUtilService.UPLOAD_DIR + "/MUST_FILE/ImpFileTemp/" + LocalTime.format(WrkUtilService.FILE_DTF);
				filePath = FolderPath + "/" + impfilename;
				log.info("filePath: " + filePath);
				File localFile = new File(filePath);
				if (!localFile.getParentFile().exists()) {
					localFile.getParentFile().mkdirs();
					localFile.createNewFile();
				}
				log.info("filePath   : " + filePath);
				log.info("filegetName: " + file.getName());

				/** 通過FileStreams通過流拷貝到新的檔案中去 */
				OutputStream out = null;
				InputStream input = null;
				try {
					out = new FileOutputStream(filePath);
					input = file.getInputStream();
					byte[] buf = new byte[1024];    
					int bytesRead;    
					while ((bytesRead = input.read(buf)) > 0) {
						out.write(buf, 0, bytesRead);
					}
				} finally {
					input.close();
					out.close();
				}
				log.info("FileStreams copy 已經執行！");

				EasyExcel.read(filePath, ImpExcelTmcaDO.class, new PageReadListener<ImpExcelTmcaDO>(dataList -> {
					for (ImpExcelTmcaDO readData : dataList) {
						pageNum++;
						if (pageNum < 2)
							continue;
						if(readData.getTmca_song_id()==null || readData.getTitle()==null)
							continue;
						nowSongID = (readData.getTmca_song_id() + readData.getTitle());
						if ("".equals(prevSongID)) {
							prevSongID = nowSongID;
							prevT = readData; // 記錄上一筆資料
						}

						if (!prevSongID.equals(nowSongID)) {
							tempMap.put("songid", prevSongID);
							tempMap.put("no", prevT.getNo());
							tempMap.put("tmca_song_id", prevT.getTmca_song_id());
							tempMap.put("title", prevT.getTitle());
							tempMap.put("data", tempList);
							songList.add(tempMap);
							tempMap = new HashMap();
							tempList = new ArrayList();
							prevSongID = nowSongID;

							prevT = readData; // 記錄上一筆資料
						}
						tempList.add(readData);
						// log.info(pageNum+" 讀取到的數值{}"+ JSON.toJSONString(readData));
					}
				})).sheet().doRead();

				if (!"".equals(nowSongID)) {
					tempMap.put("songid", nowSongID);
					tempMap.put("no", prevT.getNo());
					tempMap.put("tmca_song_id", prevT.getTmca_song_id());
					tempMap.put("title", prevT.getTitle());
					tempMap.put("data", tempList);
					songList.add(tempMap);
				}

				log.info(JSON.toJSONString(songList));
				if(songList.size()==0)
					throw new Exception("無符合的資料");
				String amendTime = LocalTime.format(WrkUtilService.DTF);

				/**
				 * 執行處理，匯出excel用資料
				 */
				List<WriteTmcaDO> wList = new ArrayList<WriteTmcaDO>();
				int i=0;
				for (HashMap map : songList) {
					i++;
					//System.out.println("map:"+ map);
					if(map==null)
						continue;
					WriteTmcaDO wdto = new WriteTmcaDO();
					wdto.setNo(map.get("no").toString());
					wdto.setTmca_song_id(map.get("tmca_song_id").toString());
					wdto.setTitle(map.get("title").toString());
					
					log.info("no:" + map.get("no"));
					log.info("tmca_id:" + map.get("tmca_song_id"));
					log.info("title:" + map.get("title"));

					try {
						JSONObject wrkDataJson = reWrkDataJson(amendTime, (List) map.get("data"));
						log.info(wrkDataJson.toJSONString());
						// test db
						String resultBody = "";
						if("Y".equals(ontest))
							resultBody = WrkUtilService.postUrlDBTest(wrkDataJson); //測試 db
						else
							resultBody = WrkUtilService.postUrlDB(wrkDataJson); // 正式 db

						JSONObject jsonObject = JSONObject.parseObject(resultBody);
						if (Integer.parseInt(jsonObject.get("code").toString()) == 200) {
							log.info(jsonObject.getJSONObject("data").get("workId").toString());
							log.info(jsonObject.getJSONObject("data").get("workSocietyCode").toString());
							wdto.setWorkid(jsonObject.getJSONObject("data").get("workId").toString());
							wdto.setWorkSocietyCode(jsonObject.getJSONObject("data").get("workSocietyCode").toString());
						} else {
							wdto.setMessage(jsonObject.get("code").toString()+":"+jsonObject.get("message").toString());
						}
						log.info("message:" + jsonObject.get("message"));
					} catch (Exception e) {
						log.info("catch message:" + e.getMessage());
						wdto.setMessage(e.getMessage());
					}
					fpc.setTotal(songList.size());
					fpc.setNow_no(i);
					fpc.setNo(map.get("no").toString());
					fpc.setTmca_song_id(map.get("tmca_song_id").toString());
					fpc.setTitle(map.get("title").toString());
					
					wList.add(wdto);
					log.info("------------");
				}
				log.info("產生結果檔案...");
				exportExcel(response, wList, ontest);
				log.info("產生結果檔案...Success");
				// doXSSF_TOExcel(filePath);
				result = "檔案上傳成功";
				retmap.put("message", result);
				retmap.put("filetime", LocalTime.format(WrkUtilService.FILE_DTF));
			} catch (Exception e) {
				throw new Exception("檔案上傳異常："+e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			retmap.put("code", "500");
			retmap.put("message", result+":"+e.getMessage());
		}
		log.info("return:"+retmap);
		return JSON.toJSONString(retmap);
	}

	/**
	 * 匯出結果
	 * @throws Exception 
	 */
	private void exportExcel(HttpServletResponse response, List wList, String ontest) throws Exception {
		Resource templatTmcaExcel = new ClassPathResource(
				"\\templates\\moneytools\\export_template\\imp_esult_export_TMCA.xlsx");
		File templatFile = templatTmcaExcel.getFile();
		/**
		 * 執行時間格式化(檔名用)
		 */
		String amendTime = LocalTime.format(WrkUtilService.FILE_DTF);
		String filenm = "TMCA執行結果清冊" + amendTime + ".xlsx";

		File exportFile = new File(FolderPath + "/" + filenm);
		if (!exportFile.getParentFile().exists()) {
			exportFile.getParentFile().mkdirs();
			exportFile.createNewFile();
		}

		/* ↓↓Write寫法↓↓
		 * EasyExcel.write(exportFile, WriteTmcaDTO.class).sheet(0).doWrite(wList);
		 * Map<String, Object> map = MapUtils.newHashMap(); map.put("date",
		 * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalTime));
		 * map.put("filename", impfilename); EasyExcel.write(exportFile,
		 * WriteTmcaDTO.class).sheet(0).doWrite(wList);
		 */
		try (ExcelWriter excelWriter = EasyExcel.write(exportFile)
				.withTemplate(templatFile).build()) {
			WriteSheet writeSheet = EasyExcel.writerSheet(0).build();
			// 如果有多個list 模板上必須有{前綴.} 這裡的前綴就是 data1，然後多個list必須用 FillWrapper包裹
			excelWriter.fill(wList, writeSheet);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("date", LocalTime.format(WrkUtilService.DTF));
			map.put("filename", impfilename);
			map.put("ontest", ("Y".equals(ontest)?"測試機":"正式機"));

			excelWriter.fill(map, writeSheet);
			excelWriter.finish();
		}
	}
	
	/**
	 * 檔案下載
	 * @param request
	 * @param response
	 * @param filetime
	 * @return
	 */
	@Override
    public String downloadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("filetime") String filetime){
		try {
			log.info("File Download ...");
			String filenm = "TMCA執行結果清冊" + filetime + ".xlsx";
	        File file = new File( WrkUtilService.UPLOAD_DIR + "/MUST_FILE/ImpFileTemp/" + filetime +'/'+ filenm);
	        if(!file.exists()){
	            return "下載檔案不存在";
	        }
	        String fileName = new String(filenm.getBytes(), "ISO8859-1");  
	        
	        response.reset();
	        response.setContentType("application/octet-stream");
	        response.setCharacterEncoding("utf-8");
	        response.setContentLength((int) file.length());
	        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
	
	        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
	            byte[] buff = new byte[1024];
	            OutputStream os  = response.getOutputStream();
	            int i = 0;
	            while ((i = bis.read(buff)) != -1) {
	                os.write(buff, 0, i);
	                os.flush();
	            }
	        } catch (IOException e) {
	            throw new IOException(e);
	        }

        } catch (IOException e) {
            log.error("{}",e);
            return "下載失敗："+e.getLocalizedMessage();
        }
        return "下載成功";
    }
}
