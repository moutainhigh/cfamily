<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list_s  b_page.upEditData()   b_page  />
	
	<@m_zapmacro_common_auto_operate_ship_ment   b_page.getWebPage().getPageOperate()  "116001016" />
</form>


<#macro m_zapmacro_common_auto_list_s e_pagedata   e_page>
	<#if e_pagedata??>
	<#list e_pagedata as e>
	  	<@m_zapmacro_common_auto_field_shipment e e_page/>
	</#list>
	</#if>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field_shipment e_field   e_page>

		<#if e_field.getFieldTypeAid()=="104005008">
	  		<@m_zapmacro_common_field_hidden e_field/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005001">
	  		  <#-- 内部处理  不输出 -->
	  	<#elseif  e_field.getFieldTypeAid()=="104005003">
	  		<@m_zapmacro_common_field_component  e_field  e_page/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005004">
	  		<@m_zapmacro_common_field_date  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select_shipments  e_field  e_page "自定义"/>
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


<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_select_shipments   e_field    e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}" >
	      			<#if e_text_select!="">
	      					<option value="define_self">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSource(e_field) as e_key>

						<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
					</#list>
	      		</select>
	<@m_zapmacro_common_field_end />
</#macro>

<#macro m_zapmacro_common_auto_operate_ship_ment     e_list_operates  e_area_type>
	<div class="control-group">
    	<div class="controls">
    		<input type='button' style='margin-right:5px;' class='btn  btn-primary' 
    		zapweb_attr_operate_id='fe6a625a37f611e5aad9005056925439' onclick="add(this)"  value='提交修改'>
    	</div>
	</div>
</#macro>


<script>
	
	$('#zw_f_order_code').attr("disabled","");
		
	function autoChangeEnable(){
			if($("#zw_f_logisticse_code").val() == "define_self"){
				$("#zw_f_define_self_name").removeAttr("disabled");
			}else{
				$("#zw_f_define_self_name").attr("disabled","");
			}
	}
	
	autoChangeEnable();
	
	if($("#zw_f_logisticse_code").val() != "define_self"){
		$("#zw_f_define_self_name").val("");
	}
	
	$("#zw_f_logisticse_code").change(function (){
		autoChangeEnable();
	});
	
	function add(obj){
	
		if($("#zw_f_logisticse_code").val() == "define_self"){
			if($.trim($("#zw_f_define_self_name").val()) == ""){
				alert("如果选中自定义 自定义物流商家名称不能为空!");
				return;
			}
		}
	
		var text = $("#zw_f_logisticse_code").find("option:selected").text();
		
		$("#zw_f_logisticse_name").val(text);
		zapjs.zw.func_add(obj);
	}
	
	
</script>
