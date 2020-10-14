var pxVideoAdd = {
	// 初始化
	init : function() {
		$('#zw_f_uid').parent().prepend('<input type="hidden" id="zw_f_picEdit_flag" name="zw_f_picEdit_flag" value="0">')
		pxVideoAdd.show_edit();
		
	},

	show_edit : function() {
		pxVideoAdd.set_value();

	},

	set_value:function(){


	},
	

}
	
	
	

if (typeof define === "function" && define.amd) {
	define("cfamily/js/pxVideoAdd", [ "zapweb/js/zapweb_upload","zapweb/js/zapweb_upload_appendix","zapjs/zapjs.comboboxc","zapadmin/js/zapadmin_single"], function() {
		return pxVideoAdd;
	});
}
