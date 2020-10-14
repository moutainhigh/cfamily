<link rel="stylesheet" href="../resources/cfamily/js/colpick.css" type="text/css"/>
<script type="text/javascript" src="../resources/cfamily/js/colpick.js"></script>
<@m_zapmacro_common_page_edit b_page />


<#-- 修改页 -->
<#macro m_zapmacro_common_page_edit e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upEditData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

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
	  		<#if e_field.getPageFieldName()=="zw_f_column_num">
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
  		<#if e_field.getPageFieldName()=="zw_f_template_number">
	  		<div class="control-group" style="display:none;">
				<label class="control-label" for="${e_field.getPageFieldName()}">${e_field.getFieldNote()}</label>
				<div class="controls">
	  				<input type="hidden" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}" >
  				</div>
			</div>
		<#elseif e_field.getPageFieldName()=="zw_f_sell_price_color">
			<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	  			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
	  			<span style="line-height:30px;color:#FF0000;">（用户PC专题价格颜色变更）</span>
  			<@m_zapmacro_common_field_end />
  		<#else>
	  		<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	  			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
  			<@m_zapmacro_common_field_end />
  		</#if>
</#macro>


<#macro m_zapmacro_common_field_start text="" for="">

	<div class="control-group">
		<label class="control-label" for="${for}">
			<#if (for=="zw_f_template_title_name" || for=="zw_f_template_backcolor" || for=="zw_f_template_backcolor_selected"|| for=="zw_f_template_title_color"|| for=="zw_f_template_title_color_selected" || for=="zw_f_event_code")>
				<span class="w_regex_need">*</span>
			</#if>
			${text}
		</label>
	<div class="controls">

</#macro>


<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_select   e_field    e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
			<#if e_field.getPageFieldName() == "zw_f_template_type" >
				<#if e_field.getPageFieldValue() == "449747500001">
					轮播模版
				<#elseif e_field.getPageFieldValue() == "449747500002">
					两栏两行模版
				<#elseif e_field.getPageFieldValue() == "449747500003">
					一栏多行模版
				<#elseif e_field.getPageFieldValue() == "449747500004">
					视频模板
				<#elseif e_field.getPageFieldValue() == "449747500005">
					标题模板
				<#elseif e_field.getPageFieldValue() == "449747500006">
					两栏广告
				<#elseif e_field.getPageFieldValue() == "449747500007">
					优惠券(一行两栏)
				<#elseif e_field.getPageFieldValue() == "449747500008">
					优惠券（一行一栏）
				<#elseif e_field.getPageFieldValue() == "449747500009">
					普通视频模板	
				<#elseif e_field.getPageFieldValue() == "449747500010">
					一行多栏（横滑）
				<#elseif e_field.getPageFieldValue() == "449747500011">
					一行三栏	
				<#elseif e_field.getPageFieldValue() == "449747500012">
					分类模版	
				<#elseif e_field.getPageFieldValue() == "449747500013">
					本地货模版
				<#elseif e_field.getPageFieldValue() == "449747500014">
					两栏多行（推荐）							
				<#elseif e_field.getPageFieldValue() == "449747500015">
					右两栏推荐		
				<#elseif e_field.getPageFieldValue() == "449747500016">
					左两栏推荐
				<#elseif e_field.getPageFieldValue() == "449747500017">
					扫码购模版
				<#elseif e_field.getPageFieldValue() == "449747500018">	
					页面定位模板
				<#elseif e_field.getPageFieldValue() == "449747500019">	
				         倒计时模板
				<#elseif e_field.getPageFieldValue() == "449747500020">	
				         积分兑换模板
				<#elseif e_field.getPageFieldValue() == "449747500021">	
				         定位横滑模板
				<#elseif e_field.getPageFieldValue() == "449747500022">	
				         兑换商品模板
				<#elseif e_field.getPageFieldValue() == "449747500023">	
				         拼团模板
				<#elseif e_field.getPageFieldValue() == "449747500024">	
				         悬浮模板
				<#elseif e_field.getPageFieldValue() == "449747500025">	
				        精编商品模板
				<#elseif e_field.getPageFieldValue() == "449747500026">	
				        拼团模板大图
				</#if>
				<input type="hidden" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}" >
			<#else>
				<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
	      					<option value="">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSource(e_field) as e_key>

						<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
					</#list>
	      		</select>
			</#if>
	<@m_zapmacro_common_field_end />
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
<#macro m_zapmacro_common_show_operate     e_list_operates  e_area_type  e_style_css >

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
	
	<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="editTemplete.submit(this);"  value="${e_operate.getOperateName()}" />
</#macro>





<script>
require(['cfamily/js/editTemplete'],function(a){a.init()});
</script>

<script type="text/javascript">
    $(function(){
    
    		$('#zw_f_template_title_color_selected').colpick({
		layout:'hex',
		submit:0,
		colorScheme:'dark',
		onChange:function(hsb,hex,rgb,el,bySetColor) {
			$(el).css('border-color','#'+hex);
			if(!bySetColor) $(el).val(hex);
		}
	}).keyup(function(){
	
	
	});
	
		$('#zw_f_template_title_color').colpick({
		layout:'hex',
		submit:0,
		colorScheme:'dark',
		onChange:function(hsb,hex,rgb,el,bySetColor) {
			$(el).css('border-color','#'+hex);
			if(!bySetColor) $(el).val(hex);
		}
	}).keyup(function(){
	
	
	});
	
	  $('#zw_f_template_backcolor_selected').colpick({
		layout:'hex',
		submit:0,
		colorScheme:'dark',
		onChange:function(hsb,hex,rgb,el,bySetColor) {
			$(el).css('border-color','#'+hex);
			if(!bySetColor) $(el).val(hex);
		}
	}).keyup(function(){
	
	
	});
	
	
	   $('#zw_f_template_backcolor').colpick({
			layout:'hex',
			submit:0,
			colorScheme:'dark',
			onChange:function(hsb,hex,rgb,el,bySetColor) {
				$(el).css('border-color','#'+hex);
				if(!bySetColor) $(el).val(hex);
			}
		}).keyup(function(){
		
		
		});
		
		 $('#zw_f_commodity_text_value').colpick({
			layout:'hex',
			submit:0,
			colorScheme:'dark',
			onChange:function(hsb,hex,rgb,el,bySetColor) {
				$(el).css('border-color','#'+hex);
				if(!bySetColor) $(el).val(hex);
			}
		}).keyup(function(){
		
		
		});
        $('#zw_f_sell_price_color').colpick({
			layout:'hex',
			submit:0,
			colorScheme:'dark',
			onChange:function(hsb,hex,rgb,el,bySetColor) {
				$(el).css('border-color','#'+hex);
				if(!bySetColor) $(el).val(hex);
			}
		}).keyup(function(){
		
		
		});
    });
</script>





