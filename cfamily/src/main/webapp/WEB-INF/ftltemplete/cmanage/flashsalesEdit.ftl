<@m_zapmacro_common_page_edit b_page />

<script>
	var zw_f_status=$('#zw_f_status');
	if(zw_f_status){
		zw_f_status.attr('disabled','disabled');
	}
	
	var zw_f_activity_code=$('#zw_f_activity_code');
	if(zw_f_activity_code){
		zw_f_activity_code.attr('disabled','disabled');
	}
	
	var zw_f_app_code=$('#zw_f_app_code');
	if(zw_f_status){
		zw_f_app_code.parent().parent().css('display','none');
	}
	
</script>