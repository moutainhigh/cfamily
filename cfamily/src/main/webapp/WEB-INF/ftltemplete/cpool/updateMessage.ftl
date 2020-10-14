<form class="form-horizontal" method="POST" >

	<div class="control-group">
		<div class="controls">
	<input class="btn btn-small" onclick="zapadmin.window_url('../show/page_vv_hp_message_cf_type')" type="button" value="类型选择">
		</div>
	</div>
	
		
	<@m_zapmacro_common_auto_list  b_page.upEditData()   b_page  />
	
	<div class="control-group">
    	<div class="controls">
	<input class="btn  btn-success" zapweb_attr_operate_id="d5ec4ac97d6411e68fbd00505692798f" onclick="sub(this)" value="提交修改 " type="button">
    	</div>
	</div>
	
</form>

  <script type="text/javascript">
  
	if($('#zw_f_mess_category_name')){
		$('#zw_f_mess_category_name').attr("readonly","readonly");    
	}
	
	function sub(obj){
		if($('#zw_f_flag_show_time').val()=='449746250001'){
			if($('#zw_f_show_time').val()==''){
				zapadmin.model_message('请填写发布时间！');
				return ;
			}
		}
		
		if($('#zw_f_flag_out_link').val()=='449746250001'){
			if($('#zw_f_out_link').val()==''){
				zapadmin.model_message('请填写外部链接！');
				return ;
			}
		}else{
			if($('#zw_f_mess_note').val()==''){
				zapadmin.model_message('请填写消息内容！');
				return ;
			}
		}
		
		zapjs.zw.func_edit(obj);
	}
  </script>