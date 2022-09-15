
/**
 * @description 所有關於form的操作皆使用此共用元件，包含了表單資料送出與表單資料繫結。
 * @class 表單工具
 * @author James
 * @constructor
 */
function FormUtil() {
}

/**
 * @default '__temp__'
 */
FormUtil.prototype.defaultTemplateFormName = '__temp__';

FormUtil.prototype.getWebRoot = function() {
	web = window.location.pathname;
	if( web.indexOf("/") != 0 ) 
		web = "/"+web;
	web = web.substring(0, web.indexOf("/", 1)+1);	
	return web;
};

/**
 * @description 表單參數物件
 * */
FormUtil.prototype.getFormOption = function() { 
	var formOptions = {
		  dataObj : null   						//要傳送給後端的參數(json格式)
		, formMethod : 'POST'					//request的形態
		, formObj : null						//指定要將哪個form的內容轉成dataObj(層級大於dataObj)
		, url : null							//request的業面
		, sqlid: null
		, onSuccess : function (jsonResult) {}	//ajax成攻回來後要做的事情(jsonResult是後端傳送回來的資料)
		, onError: function (jsonResult) {}
		, async: false
		, autoClassProcess: true
	};
	
	return formOptions;
};

FormUtil.prototype.submitTo = function (customerOption){

	var formOption = $.extend({}, formUtil.getFormOption(), customerOption);

	if (formOption.formObj != null && typeof(formOption.formObj.attr("enctype"))=="string" && formOption.formObj.attr("enctype").toUpperCase()=="MULTIPART/FORM-DATA") {
		$.ajax({
			url: formOption.url,
			type: formOption.formMethod,
		    data: new FormData(formOption.formObj[0]),
		    async: formOption.async,
			cache:false, 
			processData: false, 
			contentType: false,
			dataType: 'html',
		    error: function(xhr) {
		    	alert(0);
		    	alert("發生錯誤\r\n" +xhr.responseText);
		    	if( formOption.async && formOption.autoClassProcess ) {
		    		try {helpUtil.closeProcessBar();} catch(e) {}
		    	}	
		    },
		    success: function(response) {
		    	var JsonData = $.parseJSON(response);
		    	if( JsonData.status=="SUCCESS" ) {
		    		if( typeof(formOption.onSuccess)=="function" )
		    			formOption.onSuccess(JsonData);
		    	} else if( JsonData.status=="ERROR" ) {
		    		if( typeof(JsonData.messageText)=="string")
		    			alert(JsonData.messageText);
		    		else if( typeof(JsonData.data)=="string")
		    			alert(JsonData.data);
		    	} else if( JsonData.status=="NULL" ) {
		    		top.window.document.location.href = formUtil.getWebRoot() + 'INDEX.jsp';
		    	} else {
		    		try {
			    		helpUtil.showInfoBar("INFO", JsonData.messageText, 8000);
		    		} catch(e) {
		    			alert(JsonData.messageText);
		    		}
		    		formOption.onError(JsonData);
		    	}	
		    	if( formOption.async && formOption.autoClassProcess ) {
		    		try {helpUtil.closeProcessBar();} catch(e) {}
		    	}		    		
		    }
		});
	}else{	
		if (formOption.formObj != null) {
			formOption.dataObj = formUtil.Form2Json(formOption.formObj)
		}
		
		if (formOption.sqlid != null) {
			formOption.dataObj["sqlid"] = formOption.sqlid;
		}
		
		if( formOption.async ) {
			try {helpUtil.showProcessBar();} catch(e) {}
		}
		$.ajax({
			url: formOption.url,
			type: formOption.formMethod,
		    data: formOption.dataObj,
		    async: formOption.async,
		    contenttype : "application/x-www-form-urlencoded; charset=utf-8",
		    error: function(xhr) {
		    	alert(0);
		    	alert("發生錯誤\r\n" +xhr.responseText);
		    	if( formOption.async && formOption.autoClassProcess ) {
		    		try {helpUtil.closeProcessBar();} catch(e) {}
		    	}	
		    },
		    success: function(response) {
		    	var JsonData = $.parseJSON(response);
		    	if( JsonData.status=="SUCCESS" ) {
		    		if( typeof(formOption.onSuccess)=="function" )
		    			formOption.onSuccess(JsonData);
		    	} else if( JsonData.status=="ERROR" ) {
		    		if( typeof(JsonData.messageText)=="string")
		    			alert(JsonData.messageText);
		    		else if( typeof(JsonData.data)=="string")
		    			alert(JsonData.data);
		    	} else if( JsonData.status=="NULL" ) {
		    		top.window.document.location.href = formUtil.getWebRoot() + 'INDEX.jsp';
		    	} else {
		    		try {
			    		helpUtil.showInfoBar("INFO", JsonData.messageText, 8000);
		    		} catch(e) {
		    			alert(JsonData.messageText);
		    		}
		    		formOption.onError(JsonData);
		    	}	
		    	if( formOption.async && formOption.autoClassProcess ) {
		    		try {helpUtil.closeProcessBar();} catch(e) {}
		    	}		    		
		    }
		});
	}
};

FormUtil.prototype.getData = function (dataObj, url, onSuccess) {
	var formOptions = formUtil.getFormOption();
	formOptions.url = url;
	formOptions.dataObj = dataObj;	
	if (onSuccess != null) {
		formOptions.onSuccess = onSuccess;
	}
	formUtil.submitTo(formOptions);
};

/**
 * @description 將formObj物件裡的欄位，變成Json的格式
 * @ignore
 * @private
 * */
FormUtil.prototype.Form2Json = function (formObj) {
	
	var json = {}; 
	
	$("input[name]", formObj).each(function(){			
		var type = $(this).attr("type").toLowerCase();
		var name =$(this).attr("name");
		var value=$(this).val();	
		
		if( type=="hidden" || type=="text" || type=="password" ) {
			setKeyValue(name, value);
		}else if( type=="checkbox") {
			if( $(this)[0].checked )
				setKeyValue(name, value);
		}else if( type=="radio") {			
			if( $(this)[0].checked ) {
				setKeyValue(name, value);
			}
		}
	});
	
	$("textarea[name]", formObj).each(function(){
		var name=$(this).attr("name");
		var value=$(this).val()
		setKeyValue(name, value);
	});
	
	$("select[name]", formObj).each(function(){	
		var name=$(this).attr("name");
		if( $(this)[0].type=="select-one" ) {
			setKeyValue(name, $(this).val());
		} else {
			$("option[selected]", $(this)).each(function(){
				setKeyValue(name, $(this).attr("value"));
			});
		}
	});
	
	if( typeof(json["isEncode"])=="undefined" )
		setKeyValue("isEncode", "Y");
	
	function setKeyValue(k, v) {
		if( k!="" ) {
			var type = (typeof json[k]);
			if( type=="undefined")
				json[k]=encodeURIComponent(v);
			else if(type=="string") {
				var org = json[k];
				json[k] = new Array();
				json[k][0] = org;
				json[k][1] = encodeURIComponent(v);
			} else if(type=="object") {
				json[k][json[k].length] = encodeURIComponent(v);
			}
		}		
	}
	
	return json;
}

/**
 * @description 將formObj物件裡的欄位，變成GET參數內容
 * @ignore
 * @private
 * */
FormUtil.prototype.Form2QueryString = function (formObj) {
	
	var json = "";	
	
	$("input", formObj).each(function(){			
		var type = $(this).attr("type").toLowerCase();
		var name=$(this).attr("name");
		var value=$(this).attr("value");		
		
		if( type=="hidden" || type=="text" || type=="password" ) {
			setKeyValue(name, value);
		}else if( type=="checkbox") {
			if( $(this)[0].checked )
				setKeyValue(name, value);
		}else if( type=="radio") {			
			if( $(this)[0].checked ) {
				setKeyValue(name, value);
			}
		}
	});
	
	$("textarea", formObj).each(function(){
		var name=$(this).attr("name");
		var value=$(this).attr("value");
		setKeyValue(name, value);
	});
	
	$("select", formObj).each(function(){	
		var name=$(this).attr("name");
		if( $(this)[0].type=="select-one" ) {
			setKeyValue(name, $(this).val());
		} else {
			$("option[selected]", $(this)).each(function(){
				setKeyValue(name, $(this).attr("value"));
			});
		}
	});
	
	if( json.indexOf("isEncode=")==-1 )
		setKeyValue("isEncode", "Y");
	
	function setKeyValue(k, v) {
		if( json!="")
			json += "&";
		json += k + "=" + encodeURIComponent(v);	
	}
	
	return json;
}

/**
 * @description 將某一json物件的key變成小寫
 * @ignore
 * @private
 * */
FormUtil.prototype.convertToLowCaseKey  = function (jsonData) {
	var ret = {};
	for (var attrName in jsonData) {
		ret[attrName.toLowerCase()] = jsonData[attrName];
	}
	return ret;
};

/**
 * @description form表單資料自動對應
 * @ignore
 * @private
 * */
FormUtil.prototype.bindObjectData = function(obj, value) {
	try {		
		if (obj.get(0).tagName.toLowerCase() == 'input') {
			if (obj.attr('type').toLowerCase() == 'radio') {
				formUtil.radioDataBind(obj, value);
			} else if (obj.attr('type').toLowerCase() == 'checkbox') {
				formUtil.checkBoxDataBind(obj, value);
			} else {
				formUtil.textDataBind(obj, value);
			}
		} else if (obj.get(0).tagName.toLowerCase() == 'select') {
			formUtil.selectDataBind(obj, value);
		} else if (obj.get(0).tagName.toLowerCase() == 'textarea') {
			formUtil.textareaDataBind(obj, value);
		} else if (obj.get(0).tagName.toLowerCase() == 'span') {
			formUtil.spanDataBind(obj, value);
		}
	} catch(e) {}
}

/**
 * @description form表單資料自動對應
 * @ignore
 * @private
 * */
FormUtil.prototype.bindFormData  = function (formObj, jsonData) {
	for (var attrName in jsonData) {
		try {
			var obj = $("[id="+attrName+"]", formObj);			
			if( obj.length==0 )
				obj = $("[name="+attrName+"]", formObj);

			for(var i=0;i<obj.length;i++) {
				formUtil.bindObjectData($(obj.get(i)), jsonData[attrName]);
			}
		} catch(e) {alert(Exception )}
	}
};

/**
 * @description 表單資料自動對應
 * @ignore
 * @private
 * */
FormUtil.prototype.bindData  = function (jsonData) {
	for (var attrName in jsonData) {
		try {
			var obj = $("[id="+attrName+"]");
			if( obj.length==0 )
				obj = $("[name="+attrName+"]");
			
			if( obj.length>0 ) {
				formUtil.bindObjectData(obj, jsonData[attrName]);
			}
		} catch(e) {}
	}
};

/**
 * @ignore 
 * @private
 * */
FormUtil.prototype.textDataBind = function (obj, val) {
	try {
		if (typeof(val) == 'object') {
			val = val.val();
		}
		
		obj.val(val);
	} catch(e) {}
};

/**
 * @ignore 
 * @private
 * */
FormUtil.prototype.radioDataBind = function (obj, val) {
	try {
		if (typeof(val) == 'object') {
			val = val.val();
		}
		
		obj.removeAttr("checked");		
		obj.each(function(i, e){
			if($(e).val() == val){
				$(e).attr('checked', 'true');  
			}
		});
	} catch(e) {}
};

/**
 * @description 設定核取方塊(多選)之核取選項
 * @param {jQuery} obj 核取方塊
 * @param {string/jQuery} val 核取方塊選取之值
 * @example
 * 	formUtil.checkBoxDataBind($('#editForm [name=INTEREST]'),$('#editForm [name=INTEREST_CODE]'));
 * */
FormUtil.prototype.checkBoxDataBind = function (obj, val) {
	try {
		if (typeof(val) == 'object') {
			val = val.val();
		}
		def_value = val.split(',');
		
		obj.removeAttr("checked");		
		if (def_value != null && def_value.length == 1) {
			obj.each(function(i, e){
				if($(this).val() == def_value[0]){
					this.checked = true;
				}
			});
		} else if (def_value != null && def_value.length > 1) {
			 
			for (var j = 0 ; j < def_value.length ; j++) {
				obj.each(function(i, e){
					if($(this).val() == def_value[j]){
						this.checked = true;
					}
				});
			}
		}
		
	} catch(e) {
		try {
			obj.each(function(i, e){
				if($(e).val() == val){
					$(e).attr('checked', 'true');
				}
			});
		} catch(e) {}
	}
};

/**
 * @ignore 
 * @private
 * */
FormUtil.prototype.selectDataBind = function (obj, val) {
	try {
		if (typeof(val) == 'object') {
			val = val.val();
		}
		
		$("option", obj).removeAttr("selected");
		if( obj[0].type=="select-one" ) {
			$("option[value='"+val+"']", obj).attr('selected', 'true');
		}else{
			var valary = val.split(",");
			for(var i=0;i<valary.length;i++) {
				$("option[value="+valary[i]+"]", obj).attr('selected', 'true');
			}
		}
	} catch(e) {}
};

/**
 * @ignore 
 * @private
 * */
FormUtil.prototype.textareaDataBind = function (obj, val) {
	try {
		if (typeof(val) == 'object') {
			val = val.val();
		}
		
		obj.val(val);
	} catch(e) {}
};

/**
 * @ignore
 * @private 
 * */
FormUtil.prototype.spanDataBind = function (obj, val) {
	try {
		if (typeof(val) == 'object') {
			val = val.val();
		}
		
		obj.text(val);
	} catch(e) {}
}; 

FormUtil.prototype.getObjectValue = function (obj) {
	try {
		var v = "";
		obj.each(function(){
			if( v!="" )
				v+=",";
			v+=$(this)[0].value;
		})
		return v;
	} catch(e) {}
};

/**
 * @description 清除表單資料
 * */
FormUtil.prototype.clearFormData = function (formObj) {
	var _fromObj = formObj;

	$('select', _fromObj).val('');
	$('input:text', _fromObj).val('');
	$('input:password', _fromObj).val('');
	$('input:radio', _fromObj).removeAttr('checked');
	$('input:checkbox', _fromObj).removeAttr('checked');
	$('textarea', _fromObj).text('');
};

/**
 * @description 樣板字串轉換
 * */
FormUtil.prototype.tranPattern = function(pattern, para) {
	
	while( getKey(pattern) != "" ){
		var Key = getKey(pattern);	
		pattern = pattern.replace("@{"+Key+"}", para[Key]);
	}
		
	function getKey(pattern){
		var k = "";
		if( pattern.indexOf("@{") > -1 ) {
			k = pattern;
			k = k.substr(k.indexOf("@{")+2);
			if( k.indexOf("}") > -1)
				k = k.substr(0, k.indexOf("}"))
			else 
				k = "";
		}
		return k;
	}
	
	return pattern;	
};

//globe object
var formUtil = new FormUtil();