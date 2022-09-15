package org.must.moneytools.model;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

@Data
public class WriteTmcaDO {
	@ExcelProperty("序")
	private String no;
	
	@ExcelProperty("歌名編號")
	private String tmca_song_id;
	
	@ExcelProperty("歌名")
	private String title;
	
	@ExcelProperty("work_id")
	private String workid;
	
	@ExcelProperty("work_society_code")
	private String workSocietyCode;
	
	@ExcelProperty("訊息")
	private String message;
	
}
