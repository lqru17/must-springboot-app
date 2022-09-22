package org.must.mapper.fds.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.must.mapper.fds.entity.S_Admin;

public interface S_AdminDao {
	
	/** 同XML ID名稱 **/
	List<HashMap> selectAll();
	
	/** 自訂SQL **/
	@Select("select * from s_admin")
	List<HashMap> selAdminAll();
	
	@Select("select * from s_admin where ad_user = #{ad_user}")
	List<HashMap> getAdminOne(String ad_user);
	
	@Insert("insert into s_admin (ad_user, limits) values ( #{ad_user}, #{limits})")
	void addAdmin(S_Admin s_admin);
	
	@Delete("delete from s_admin where ad_user= #{ad_user}")
	void deleteAdmin(String ad_user);
}
