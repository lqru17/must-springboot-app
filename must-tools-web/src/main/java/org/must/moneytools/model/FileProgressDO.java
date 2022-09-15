package org.must.moneytools.model;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

@Data
public class FileProgressDO {
	@ExcelProperty("總比數")
	private int total;
	
	@ExcelProperty("目前處理筆數")
	private int now_no;
	
	@ExcelProperty("序")
	private String no;
	
	@ExcelProperty("歌名編號")
	private String tmca_song_id;
	
	@ExcelProperty("歌名")
	private String title;
}
