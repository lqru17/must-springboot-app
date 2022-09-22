package org.must.mapper.oreg.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Select;

public interface S_SortDao {

	@Select("select * from s_sort")
	List<HashMap> getAll();
}
