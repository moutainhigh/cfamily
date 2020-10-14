<@m_zapmacro_common_page_add b_page />

<script>
	var zw_f_status=$('#zw_f_status');
	if(zw_f_status){
		zw_f_status.val('449746740001');
		zw_f_status.attr('disabled','disabled');
	}
	
	var zw_f_activity_name=$('#zw_f_activity_name');
	if(zw_f_activity_name){
		zw_f_activity_name.attr('maxlength',25);
	}
	
		
	var zw_f_app_code=$('#zw_f_app_code');
	if(zw_f_status){
		zw_f_app_code.parent().parent().css('display','none');
	}
</script>