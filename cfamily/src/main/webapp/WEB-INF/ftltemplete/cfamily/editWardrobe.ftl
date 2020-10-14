
<script>

function on_load()
{
 	var oData = {};
	var defaults = oData?{
		api_target : 'com.cmall.familyhas.api.ApiGetWardrobe',
		api_input : zapjs.f.tojson(oData),
		api_key : 'betafamilyhas'
	}:{api_key : 'betafamilyhas',api_input:''};
	zapjs.f.ajaxjson("../jsonapi/com_cmall_familyhas_api_ApiGetWardrobe" , defaults, function(data) {
		if (data.resultCode == "1") {
			var ward = data.wardrobe;
			$('#zw_f_describe').val(ward.describe);
			$('#zw_f_share_img').val(ward.share_img);
			
			$('#zw_f_share_img').nextAll('span').eq(0).html('');
			$('#zw_f_share_img').nextAll('span').eq(2).html('<ul><li class="control-upload-list-li"><div><div class="w_left w_w_100"><a href="'+ward.share_img+'" target="_blank"><img src="'+ward.share_img+'"></a></div><div class="w_left w_p_10"><a href="javascript:zapweb_upload.change_index(\'zw_f_share_img\',0,\'delete\')">删除</a></div><div class="w_clear"></div></div></li></ul>');
			
			$('#zw_f_share_title').val(ward.share_title);
			$('#zw_f_share_content').val(ward.share_content);
			$('#zw_f_share_link').val(ward.share_link);
		} 
		
	});
}
on_load();
</script>
<@m_zapmacro_common_page_edit b_page />
<script type="text/javascript">
	function clearform() {
		zapweb_upload.change_index('zw_f_share_img',0,'delete');
	}
	function rtn_edit(){
		document.getElementById('exit_save').click();
	}
</script>

<#-- 修改页 -->
<#macro m_zapmacro_common_page_edit e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upEditData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<#-- 页面按钮的自动输出 -->
<#macro m_zapmacro_common_auto_operate e_list_operates  e_area_type>
	<div class="control-group">
    	<div class="controls">
    		<@m_zapmacro_common_show_operate e_list_operates  e_area_type "btn  btn-success" />
	        <input type="reset" class="btn  btn-success" onclick="clearform();" value="清空">
	        <input type="button" class="btn  btn-success" onclick="rtn_edit();" value="取消">
	        <a id="exit_save" href="../page/page_chart_v_fh_program" onclick="zapadmin.menu_click(this)" style="display:none;" >取消</a>
    	</div>
	</div>
</#macro>




























