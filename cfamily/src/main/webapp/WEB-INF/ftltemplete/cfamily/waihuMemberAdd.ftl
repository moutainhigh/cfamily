
<form class="form-horizontal" method="POST">
<input type="hidden" value="" name="zw_f_member_code" id="zw_f_member_code"/>
<div class="control-group">
	<label class="control-label" for="zw_f_search">查询：</label>
	<div class="controls">
		<input id="zw_f_login_name" name="zw_f_login_name" value="" type="text">
		<input class="btn btn-small" onclick="search(this)" type="button" value="手机号查询">
	</div>
</div>

<div class="control-group">
	<label class="control-label" for="zw_f_nickname"><span class="w_regex_need">*</span>昵称：</label>
	<div class="controls">

		<input type="text" id="zw_f_nickname" name="zw_f_nickname" zapweb_attr_regex_id="469923180002" value="" readonly="readonly">
	</div>
</div>
	  	
		
	

<div class="control-group">
	<label class="control-label" for="zw_f_real_name"><span class="w_regex_need">*</span>姓名：</label>
	<div class="controls">

		<input type="text" id="zw_f_real_name" name="zw_f_real_name" zapweb_attr_regex_id="469923180002" value="">
	</div>
</div>
	  	
<div class="control-group">
	<label class="control-label" for="zw_f_gender"><span class="w_regex_need">*</span>性别：</label>
	<div class="controls">

	      		<select name="zw_f_gender" id="zw_f_gender">

						<option value="449747080001">男</option>

						<option value="449747080002">女</option>
	      		</select>
	</div>
</div>
	  	
	<div class="control-group">
    	<div class="controls">

    		
	
	<input type="button" class="btn  btn-success" zapweb_attr_operate_id="fac0be6f6bef11eaabac005056165069" onclick="zapjs.zw.func_add(this)" value="提交新增">
    		

    	</div>
	</div>
</form>

<script>	
	function search(obj) {
		if($("#zw_f_login_name").val()==''){
			zapjs.f.modal({
					content : '请输入手机号'
				});
				return;
		}
		var syurl="/cfamily/jsonapi/com_cmall_familyhas_api_ApiGetFxrInfo?api_key=betafamilyhas&wxCode="+$("#zw_f_login_name").val();
			$.ajax({
		 		type:"GET",
		 		url:syurl,
		 		dataType:"json",
		 		success:function(data){
		 			if(data.resultCode == 1){
		 				$("#zw_f_nickname").val(data.nickName);
		 				$("#zw_f_member_code").val(data.memberCode);
		 			} else {
						zapjs.f.modal({
							content : data.resultMessage
						});
		 			}
		 		}
		 	});
	}
</script>