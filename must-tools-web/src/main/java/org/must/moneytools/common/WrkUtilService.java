package org.must.moneytools.common;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

public class WrkUtilService {
	public static String UPLOAD_DIR = "D:";

	/**
	 * 日期格式化 yyyy-MM-dd(HHmmss)
	 */
	public static DateTimeFormatter FILE_DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd(HHmmss)");

	/**
	 * 日期格式化 yyyy-MM-dd HH:mm:ss
	 */
	public static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	/**
	 * GroupId編號取號用，只有一碼共  9+26個組合
	 */
	public static String GROUP_NUM_LIST[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "N", "M", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	public static RestTemplate restTemplate = new RestTemplate();
	/**
	 * 測試機連結
	 */
	final static String BASEURL_TEST = "http://180.166.161.210:18075/api/open/addWrkWork";

	/**
	 * 正式機連結
	 */
	final static String BASEURL = "http://192.168.88.203:8080/open/addWrkWork";
	
	/**
	 * 測試機
	 * @param jsonData
	 * @return
	 * @throws URISyntaxException
	 */
	public static String postUrlDBTest(JSONObject jsonData) throws URISyntaxException {
		URI uri = new URI(BASEURL_TEST);
		ResponseEntity<String> responseEnt = restTemplate.postForEntity(uri, jsonData, String.class);
		String resultBody = responseEnt.getBody();
		System.out.println(resultBody);
		return resultBody;
	}
	
	/**
	 * 正式機
	 * @param jsonData
	 * @return
	 * @throws URISyntaxException
	 */
	public static String postUrlDB(JSONObject jsonData) throws URISyntaxException {
		URI uri = new URI(BASEURL);
		ResponseEntity<String> responseEnt = restTemplate.postForEntity(uri, jsonData, String.class);
		String resultBody = responseEnt.getBody();
		System.out.println(resultBody);
		return resultBody;
	}
	
	/*
	 * 如果是檔案 ==》直接刪除
	 * 如果是目錄 ==》必須先刪除裡面每一層目錄裡的所有檔案，最後才能刪除外層的目錄
	 *              原因：不為空的話 刪不了
	 * File[] listFiles() 
     *  返回一個抽象路徑名陣列，這些路徑名錶示此抽象路徑名錶示的目錄中的檔案。 
	 */
	public static boolean deleteFile(File file) {
        System.gc(); // 回收資源
		File[] files = file.listFiles();
		if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("刪除單個檔案" + file.getAbsolutePath() + "成功！");
                return true;
            } else {
                System.out.println("刪除單個檔案" + file.getAbsolutePath() + "失敗！");
                return false;
            }
        } else {
            System.out.println("刪除單個檔案失敗：" + file.getAbsolutePath() + "不存在！");
            return false;
        }
	}
	
	public static boolean deleteDirectory(File dirFile) {
        System.gc(); // 回收資源
        // 如果dir對應的檔案不存在，或者不是一個目錄，則退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("刪除目錄失敗："+dirFile.getAbsolutePath()+" 不存在！");
            return false;
        }
        boolean flag = true;
        // 刪除資料夾中的所有檔案包括子目錄
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 刪除子檔案
            if (files[i].isFile()) {
                flag = deleteFile(files[i]);
                int d=0;
                while(!flag && d<10) {
                	flag = deleteFile(files[i]);
                	d++;
                }
                if (!flag)
                    break;
            }
            // 刪除子目錄
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]);
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("刪除目錄失敗！");
            return false;
        }
        // 刪除當前目錄
        if (dirFile.delete()) {
            System.out.println("刪除目錄" + dirFile.getAbsolutePath() + " 成功！");
            return true;
        } else {
            return false;
        }
	}
}
