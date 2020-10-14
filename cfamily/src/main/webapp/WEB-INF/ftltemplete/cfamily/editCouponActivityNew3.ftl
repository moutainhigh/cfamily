<script>
$(document).ready(function() { 
	onProvideTypeChange();
	onTriggerChange();
	if(($("#zw_f_assign_line2").val()!=0)||($('#zw_f_assign_line2_bar')!=0)){
        $("#zw_f_blocked").parent().parent().hide();
	 	$("#zw_f_break_blocked").parent().parent().hide();	
	}
　　$("#zw_f_provide_type").change(function(){ 
　　　onProvideTypeChange();
　　});
   $('input[type=radio][name=zw_f_assign_trigger]').change(function() {
        onTriggerChange(this.value);

      });
    //点击首次下单,清空下单满额字段
    $(":radio").click(function(){
   		if($(this).val() != "4497471600340001") {
   			$("#zw_f_assign_line").val("0");
   		}
   		if($(this).val() != "4497471600340003") {
   			$("#zw_f_assign_line2").val("0");
   		}
   		if($(this).val() != "4497471600340004") {
   			$("#zw_f_assign_line2").val("0");
   			$("#zw_f_assign_line2_bar").val("0");
   			$("#zw_f_assign_line3").val("0");
   		}
  	});
	
	function onProvideTypeChange() {
		　if($("#zw_f_provide_type").val() == "4497471600060001") {
			$("#zw_f_newer_given").parent().parent().show();
			
			$("#zw_f_provide_num").parent().parent().show();
		 	$("#zw_f_assign_trigger").parent().parent().hide();
		 	$("#zw_f_blocked").parent().parent().hide();
		 	$("#zw_f_break_blocked").parent().parent().hide();
		 	$("#zw_f_is_detail_show").parent().parent().hide();
		 	$("#zw_f_is_detail_show").val('449748350001');
		 	$("#zw_f_rebate_ratio").parent().parent().hide();
		 	$("#zw_f_validate_time").parent().parent().hide();
		 } else if($("#zw_f_provide_type").val() == "4497471600060002") {
		 	
			$("#zw_f_provide_num").parent().parent().show();
		 	$("#zw_f_assign_trigger").parent().parent().hide();
		 	$("#zw_f_blocked").parent().parent().hide();
		 	$("#zw_f_break_blocked").parent().parent().hide();	
		 	
		 	$("#zw_f_newer_given").parent().parent().hide();
		 	$("#zw_f_newer_given").val("449746250002");
		 	$("#zw_f_is_detail_show").parent().parent().show();
		 	
		 	// 系统发放需要显示添加时的选中值
		 	//$("#zw_f_is_detail_show").val('449748350001');	
		 	$("#zw_f_rebate_ratio").parent().parent().hide();	
		 	$("#zw_f_validate_time").parent().parent().hide(); 	
		 } else if($("#zw_f_provide_type").val() == "4497471600060003") {
		 	//条件触发
			$("#zw_f_provide_num").parent().parent().show();
		 	$("#zw_f_assign_trigger").parent().parent().show();
		 	$("#zw_f_blocked").parent().parent().show();
		 	$("#zw_f_break_blocked").parent().parent().show();
			
			$("#zw_f_newer_given").parent().parent().hide();
			$("#zw_f_newer_given").val("449746250002");
			$("#zw_f_is_detail_show").parent().parent().hide();
		 	$("#zw_f_is_detail_show").val('449748350001');
		 	$("#zw_f_rebate_ratio").parent().parent().hide();
		 	$("#zw_f_validate_time").parent().parent().hide();
		 } else if($("#zw_f_provide_type").val() == "4497471600060004") {
		 	//兑换码兑换
		 	$("#zw_f_assign_trigger").parent().parent().hide();
		 	$("#zw_f_blocked").parent().parent().hide();
		 	$("#zw_f_break_blocked").parent().parent().hide();
			$("#zw_f_newer_given").parent().parent().hide();
			$("#zw_f_is_detail_show").parent().parent().hide();
		 	$("#zw_f_is_detail_show").val('449748350001');
			$("#zw_f_provide_num").parent().parent().hide();
			$("#zw_f_provide_num").val(0);
			$("#zw_f_rebate_ratio").parent().parent().hide();
			$("#zw_f_validate_time").parent().parent().hide();
		 }else if($("#zw_f_provide_type").val() == "4497471600060005") {
		 	//系统返利
		 	$("#zw_f_assign_trigger").parent().parent().hide();
		 	$("#zw_f_blocked").parent().parent().hide();
		 	$("#zw_f_break_blocked").parent().parent().hide();
			$("#zw_f_newer_given").parent().parent().hide();
			$("#zw_f_is_detail_show").parent().parent().hide();
		 	$("#zw_f_is_detail_show").val('449748350001');
			$("#zw_f_provide_num").parent().parent().hide();
			$("#zw_f_provide_num").val(0);
			$("#zw_f_rebate_ratio").parent().parent().show();
			$("[for='zw_f_rebate_ratio']").html('<span class="w_regex_need">*</span>返利比例');
			$("#zw_f_validate_time").parent().parent().show();
			$("[for='zw_f_validate_time']").html('<span class="w_regex_need">*</span>有效时间');
		 }
	}
	   function onTriggerChange(v) {
		　if(v == "4497471600340003"||v == "4497471600340004") {
			//连续启动
		 	$("#zw_f_blocked").parent().parent().hide();
		 	$("#zw_f_break_blocked").parent().parent().hide();
		 }else {
		 	$("#zw_f_blocked").parent().parent().show();
		 	$("#zw_f_break_blocked").parent().parent().show();
		 }
	}
}); 

function presubmit(obj) {
    if($("#zw_f_provide_type").val() == "4497471600060005") {
           //系统返利
          var digReg = /^[1-9]{0,1}[0-9]{1}$/;
			if(!digReg.test($("#zw_f_rebate_ratio").val())||$("#zw_f_rebate_ratio").val()<1||$("#zw_f_rebate_ratio").val()>99) {
				zapjs.f.modal({
					content : '返利比例请输入1~99!'
				});
				return;
			}
			if(!digReg.test($("#zw_f_validate_time").val())||$("#zw_f_validate_time").val()<1||$("#zw_f_validate_time").val()>99) {
			zapjs.f.modal({
				content : '有效时间请输入1~99!'
			});
			return;
			}
    }
	if($("#zw_f_provide_type").val() == "4497471600060003") {
		//条件触发
		if($("input[type='radio']:checked").val() == "4497471600340001") {
			//下单满X元
			var digReg = /^[0-9]+([.]\d{1,2})?$/;
			if(!digReg.test($("#zw_f_assign_line").val())) {
				zapjs.f.modal({
					content : '请输入正确的下单金额!'
				});
				return;
			}
		}
			if($("input[type='radio']:checked").val() == "4497471600340003") {
			//下单满X元
			var digReg = /^[0-9]+([.]\d{1,2})?$/;
			if(!digReg.test($("#zw_f_assign_line2").val())) {
				zapjs.f.modal({
					content : '请输入正确的天数!'
				});
				return;
			}
		}
		if($("input[type='radio']:checked").val() == "4497471600340004") {
			//下单满X元
			var digReg = /^[0-9]+([.]\d{1,2})?$/;
			if(!digReg.test($("#zw_f_assign_line2_bar").val())) {
				zapjs.f.modal({
					content : '请输入正确的天数!'
				});
				return;
			}else{
			 $("#zw_f_assign_line2").val($("#zw_f_assign_line2_bar").val());
			}
			if(!digReg.test($("#zw_f_assign_line3").val())) {
				zapjs.f.modal({
					content : '请输入正确的件数!'
				});
				return;
			}
		}
	}
	zapjs.zw.func_edit(obj);
}
</script>
<#assign self_flag = 0>
<@m_zapmacro_common_page_edit b_page />


<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
		
	  	<@m_zapmacro_common_auto_field e e_page/>
	  	
	</#list>
	</#if>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
	
		<#if e_field.getFieldTypeAid()=="104005008">
	  		<@m_zapmacro_common_field_hidden e_field/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005001">
	  		  <#-- 内部处理  不输出 -->
	  	<#elseif  e_field.getFieldTypeAid()=="104005003">
	  		<@m_zapmacro_common_field_component  e_field  e_page/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005004">
	  		<@m_zapmacro_common_field_date  e_field />
		<#elseif  e_field.getFieldTypeAid()=="104005022">
  			<@m_zapmacro_common_field_datesfm  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005019">
	  		<#if e_field.getPageFieldName() == 'zw_f_is_marketing'>
	  			<@m_zapmacro_common_field_select  e_field  e_page "请选择"/>
	  		<#else>
	  			<@m_zapmacro_common_field_select  e_field  e_page ""/>
	  		</#if>
	  	<#elseif  e_field.getFieldTypeAid()=="104005103">
	  		<@m_zapmacro_common_field_checkbox  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005020">
	  		<@m_zapmacro_common_field_textarea  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005005">
	  		<@m_zapmacro_common_field_editor  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005021">
	  		<@m_zapmacro_common_field_upload  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>


<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#if (e_field.getFieldName() == "assign_trigger")>
			<#--发放条件-->
			<input type="radio" id="zw_f_assign_trigger" name="zw_f_assign_trigger" value="4497471600340001" <#if  e_field.getPageFieldValue()=='4497471600340001'><#assign self_flag = 1>checked</#if>>下单满 <input type="text" id="zw_f_assign_line" name="zw_f_assign_line"  style="width:65px">
元，并在线支付，发放此优惠券.<br><br>
            <input type="radio" id="zw_f_assign_trigger" name="zw_f_assign_trigger" value="4497471600340003" <#if  e_field.getPageFieldValue()=='4497471600340003'> <#assign self_flag = 3>checked</#if>>连续启动 <input type="text" id="zw_f_assign_line2" name="zw_f_assign_line2" style="width:65px">
			天<br><br>
			<input type="radio" id="zw_f_assign_trigger" name="zw_f_assign_trigger" value="4497471600340004" <#if  e_field.getPageFieldValue()=='4497471600340004'><#assign self_flag = 4>checked</#if>>连续<input type="text" id="zw_f_assign_line2_bar" name="zw_f_assign_line2_bar" style="width:65px">
			天，每天加入购物车商品数量超过<input type="text" id="zw_f_assign_line3" name="zw_f_assign_line3" style="width:65px">件<br><br>

			<input type="radio" id="zw_f_assign_trigger" name="zw_f_assign_trigger" value="4497471600340002" <#if  e_field.getPageFieldValue()=='4497471600340002'><#assign self_flag = 2>checked</#if>>首次下单
		<#elseif (e_field.getFieldName() == "flag")>
			<select name="zw_f_flag" id="zw_f_flag">
	      			<option value="0" <#if  e_field.getPageFieldValue()=='0'> selected="selected" </#if>>未发布</option>
	      			<option value="1" <#if  e_field.getPageFieldValue()=='1'> selected="selected" </#if>>已发布</option>
      		</select>
      	<#elseif (e_field.getFieldName() == "validate_time")> 
		<input type="text" placeholder="1~99" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
			   分钟
      	<#elseif (e_field.getFieldName() == "rebate_ratio")> 
		<input type="text" placeholder="1~99" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
			   %	
		<#else>
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
			<#if (e_field.getFieldName() == "provide_num")>
				份
			</#if>
		</#if>
	<@m_zapmacro_common_field_end />
</#macro>



<#-- 字段：隐藏域 -->
<#macro m_zapmacro_common_field_hidden e_field>
	<#if (e_field.getFieldName() == "assign_line")>
		<script>$('#zw_f_assign_line').val(${e_field.getPageFieldValue()});</script>
	<#elseif (e_field.getFieldName() == "assign_line2")>
	    <#if self_flag==3>
	        <script>$('#zw_f_assign_line2').val(${e_field.getPageFieldValue()});</script>
	        <script>$('#zw_f_assign_line2_bar').val(0);</script> 
	    <#elseif self_flag==4>
	    	<script>$('#zw_f_assign_line2').val(0);</script>
	    	<script>$('#zw_f_assign_line2_bar').val(${e_field.getPageFieldValue()});</script> 
	    <#else>
	        <script>$('#zw_f_assign_line2').val(0);</script>
	    	<script>$('#zw_f_assign_line2_bar').val(0);</script> 
	    </#if>
	<#elseif (e_field.getFieldName() == "assign_line3")>
	    <script>$('#zw_f_assign_line3').val(${e_field.getPageFieldValue()});</script>
	<#else>
		<input type="hidden" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}" />
	</#if>
</#macro>


<#-- 页面按钮的自动输出 -->
<#macro m_zapmacro_common_auto_operate     e_list_operates  e_area_type>
	<div class="control-group">
    	<div class="controls">
    		<@m_zapmacro_common_show_operate e_list_operates  e_area_type "btn  btn-success" />
    	</div>
	</div>
</#macro>
<#-- 按钮显示 -->
<#macro m_zapmacro_common_show_operate e_list_operates  e_area_type  e_style_css >

			<#list e_list_operates as e>
    			<#if e.getAreaTypeAid()==e_area_type>
	    			<#if e.getOperateTypeAid()=="116015010">
	    				<@m_zapmacro_common_operate_button e  e_style_css/>
	    			<#else>
	    				<@m_zapmacro_common_operate_link e  e_style_css/>
	    			</#if>
    		
    			</#if>
    		</#list>

</#macro>
<#-- 页面按钮 -->
<#macro m_zapmacro_common_operate_button  e_operate  e_style_css>
	<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="presubmit(this)"  value="${e_operate.getOperateName()}" />
</#macro>
