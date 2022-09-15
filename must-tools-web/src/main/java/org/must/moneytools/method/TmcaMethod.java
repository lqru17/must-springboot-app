package org.must.moneytools.method;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.must.moneytools.common.WrkUtilService;
import org.must.moneytools.model.ImpExcelTmcaDO;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.fastjson.JSONObject;

public interface TmcaMethod {

	public default JSONObject reWrkDataJson(String amendTime, List data) {
		JSONObject wrkWorkAddDto = new JSONObject();
		JSONObject wrkWork = new JSONObject();// wrk_work主表 key value
		JSONObject wwisList = new JSONObject();// wrk_work_ip_share表 json->集合
		JSONObject wrkWorkRightMap = new JSONObject();// wrk_work_right表 json->集合

		List<JSONObject> waListList = new ArrayList<JSONObject>();// wrk_artist表 集合
		JSONObject wwsList = new JSONObject();// wrk_work_source表
		List<JSONObject> wwsListList = new ArrayList<JSONObject>();// wrk_work_source表 集合
		JSONObject wrkWorkOtherSocCodeList = new JSONObject();// wrk_work_other_soc_code表 集合
		List<JSONObject> wrkWorkOtherSocCodeListList = new ArrayList<JSONObject>();// wrk_work_other_soc_code表 集合

		ImpExcelTmcaDO dto = (ImpExcelTmcaDO) data.get(0);
		// 作品主表
		wrkWork.put("amendTime", amendTime);
		wrkWork.put("amendUser", "MUSTIT");
		wrkWork.put("amendUserId", "48");
		wrkWork.put("genre", "POP");
		wrkWork.put("createTime", amendTime);
		wrkWork.put("workType", "ORG");
		wrkWork.put("title", dto.getTitle());
		wrkWork.put("performLanguage", dto.getTitle_language());
		wrkWork.put("status", 1);
		wrkWork.put("distflag", 0);
		wrkWorkAddDto.put("wrkWork", wrkWork);

		// ip_share表
		List<JSONObject> perList = new ArrayList<JSONObject>();
		List<JSONObject> mecList = new ArrayList<JSONObject>();
		boolean checkRoleE = false; //判斷是否有身份為E的，有E API會個行編輯GROUP ID，沒有則須自行增加
		for (Object detail_dto : data) {
			ImpExcelTmcaDO ddto = (ImpExcelTmcaDO) detail_dto;
			if("E".equals(ddto.getWork_ip_role())){
				checkRoleE = true;
			}
		}
		int gpi=0;
		for (Object detail_dto : data) {
			ImpExcelTmcaDO ddto = (ImpExcelTmcaDO) detail_dto;
			// per
			JSONObject PERDetail = new JSONObject();
			PERDetail.put("name", ddto.getAuthor_name());
			PERDetail.put("workIpRole", ddto.getWork_ip_role());
			PERDetail.put("ipShare", ddto.getIp_share());
			PERDetail.put("SD", "N");
			PERDetail.put("ipType", "N");
			PERDetail.put("createTime", amendTime);
			if(checkRoleE == false) {
				PERDetail.put("groupIndicator", WrkUtilService.GROUP_NUM_LIST[gpi]);
				gpi++;
			}
			perList.add(PERDetail);
			// mec
			JSONObject MECDetail = new JSONObject();
			MECDetail.put("name", ddto.getAuthor_name());
			MECDetail.put("workIpRole", ddto.getWork_ip_role());
			MECDetail.put("ipShare", "0");
			MECDetail.put("SD", "N");
			MECDetail.put("ipType", "N");
			MECDetail.put("createTime", amendTime);
			if(checkRoleE == false) {
				MECDetail.put("groupIndicator", WrkUtilService.GROUP_NUM_LIST[gpi]);
				gpi++;
			}
			mecList.add(MECDetail);
		}
		wwisList.put("PER", perList);
		wwisList.put("MEC", mecList);
		wrkWorkAddDto.put("wwisList", wwisList);

		// wrk_work_right
		JSONObject wrkWorkRightPERMap = new JSONObject();
		JSONObject wrkWorkRightMECMap = new JSONObject();
		wrkWorkRightPERMap.put("createTime", amendTime);
		wrkWorkRightPERMap.put("distributable", 0);
		wrkWorkRightPERMap.put("rightType", "PER");
		wrkWorkRightPERMap.put("shareType", "M");
		wrkWorkRightPERMap.put("synIndicator", "SY");
		wrkWorkRightPERMap.put("workDp", "N");
		wrkWorkRightPERMap.put("workSd", "N");
		wrkWorkRightMap.put("PER", wrkWorkRightPERMap);
		wrkWorkRightMECMap.put("createTime", amendTime);
		wrkWorkRightMECMap.put("distributable", 0);
		wrkWorkRightMECMap.put("rightType", "MEC");
		wrkWorkRightMECMap.put("shareType", "M");
		wrkWorkRightMECMap.put("synIndicator", "SY");
		wrkWorkRightMECMap.put("workDp", "N");
		wrkWorkRightMECMap.put("workSd", "N");
		wrkWorkRightMap.put("MEC", wrkWorkRightMECMap);
		wrkWorkAddDto.put("wrkWorkRightMap", wrkWorkRightMap);

		// wrk_artist表
		List<String> tmpSingName = new ArrayList<String>();
		for (Object detail_dto : data) {
			ImpExcelTmcaDO ddto = (ImpExcelTmcaDO) detail_dto;
			String sing_name = ddto.getSing_name();
			if (!"".equals(sing_name)) {
				if (tmpSingName.contains(sing_name) == false) {
					tmpSingName.add(sing_name);

					JSONObject waList = new JSONObject();
					waList.put("artistName", sing_name);
					waList.put("chineseName", sing_name);
					waList.put("iputSoc", "161"); // 新增者協會代碼
					waList.put("createUserName", "MUSTIT");
					waList.put("createUserId", "48");
					waList.put("createTime", amendTime);
					waListList.add(waList);
				}
			}
		}
		if (waListList.size() > 0)
			wrkWorkAddDto.put("waList", waListList);

		// wrk_work_souce表
		wwsList.put("createTime", amendTime);
		wwsList.put("inputSoc", "161");
		wwsList.put("notifyDate", amendTime);
		wwsList.put("notifySouceId", "907");// TMCA當作SOC處理
		wwsList.put("inputSoc", "161");
		wwsList.put("sourceType", "APO");
		wwsListList.add(wwsList);
		wrkWorkAddDto.put("wwsList", wwsListList);

		// wrk_work_other_soc_code表
		wrkWorkOtherSocCodeList.put("createTime", amendTime);
		wrkWorkOtherSocCodeList.put("createUserName", "MUSTIT");
		wrkWorkOtherSocCodeList.put("createUserId", "48");
		wrkWorkOtherSocCodeList.put("sourceCode", "907");
		wrkWorkOtherSocCodeList.put("sourceName", "TMCA");
		wrkWorkOtherSocCodeList.put("sourceType", "APO");
		wrkWorkOtherSocCodeList.put("sourceWorkId", dto.getTmca_song_id());
		wrkWorkOtherSocCodeListList.add(wrkWorkOtherSocCodeList);
		wrkWorkAddDto.put("wrkWorkOtherSocCodeList", wrkWorkOtherSocCodeListList);

		return wrkWorkAddDto;
	}
}
