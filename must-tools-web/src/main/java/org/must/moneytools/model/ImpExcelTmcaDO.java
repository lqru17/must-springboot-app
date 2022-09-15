package org.must.moneytools.model;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

@Data
public class ImpExcelTmcaDO {
	@ExcelProperty("序")
	private String no;
	
	@ExcelProperty("歌名編號")
	private String tmca_song_id;
	
	@ExcelProperty("歌名")
	private String title;
	
	@ExcelProperty("語言")
	private String title_language;
	
	@ExcelProperty("作者名稱")
	private String author_name;
	
	@ExcelProperty("IP Name No")
	private String ip_name_no;
	
	@ExcelProperty("作者協會")
	private String ip_society_code;
	
	@ExcelProperty("作者角色")
	private String work_ip_role;
	
	@ExcelProperty("公開播放比例")
	private String ip_share;
	
	@ExcelProperty("灌錄比例")
	private String recode_ip_share;
	
	@ExcelProperty("演唱人")
	private String sing_name;
	
	@ExcelProperty("副歌名")
	private String refrain_title;
}
