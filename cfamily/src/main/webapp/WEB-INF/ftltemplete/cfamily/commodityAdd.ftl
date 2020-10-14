<link rel="stylesheet" href="../resources/cfamily/js/colpick.css" type="text/css"/>
<link rel="stylesheet" href="../resources/lib/datepicker/laydate.css" type="text/css"/>
<script type="text/javascript" src="../resources/cfamily/js/colpick.js"></script>
<script type="text/javascript" src="../resources/lib/datepicker/dateTime.js"></script>

<#assign t_type = b_page.getReqMap()["zw_f_template_type"] >
<@m_zapmacro_common_page_add b_page />

<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<form id="1122334" class="form-horizontal" method="POST" >
	<input type="hidden" value="${t_type}" name="zw_f_template_type" id="zw_f_template_type"/>
	<@m_zapmacro_common_auto_list  e_page.upAddData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>
		<#if e_pagedata??>
			<#if '${t_type}' == '449747500018'>
				<@m_zapmacro_common_field_span_ymmb e_pagedata e_page/>
			<#elseif '${t_type}' == '449747500019'>
			    <@m_zapmacro_common_field_span_djsmb e_pagedata e_page/>
			<#elseif '${t_type}' == '449747500021'>
			    <@m_zapmacro_common_field_span_dwhh e_pagedata e_page/>
			<#else>
			<#list e_pagedata as e>
				<#-- 由于定位模板的标签，只有页面定位模板(449747500018)才有的字段，因此其他模板都进行屏蔽 -->
				<#if e.getPageFieldName() == "zw_f_sub_template_number">
				<#else>					
					<@m_zapmacro_common_auto_field e e_page/>
				</#if>
			</#list>
		  	</#if>
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
	  		<#-- 精编商品模板:只允许选择商品 -->
	  		<#if  e_field.getPageFieldName() = "zw_f_skip" && '${t_type}' == '449747500025'>
	  			<div class="control-group">
					<label class="control-label" for="zw_f_skip">
						<span class="w_regex_need">*</span>打开方式选择：
					</label>
					<div class="controls">
						<select name="zw_f_skip" id="zw_f_skip">
							<option value="449747550002"  selected="selected" >商品</option>
						</select>
					</div>
				</div>
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

<#-- 字段：日期 时分秒-->
<#macro m_zapmacro_common_field_datesfm e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#-- onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})"  -->
		<#-- onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',realDateFmt:'yyyy-MM-dd HH-mm-ss',realTimeFmt:'HH:mm:ss HH-mm-ss'})"   -->
		<input type="text"  autocomplete="off" onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})"  id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
	<@m_zapmacro_common_field_end />
<#-- zapjs.f.require(['lib/datepicker/dateTime'],function(a){})-->
<#-- zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});-->
	<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){})" />
	  
</#macro>

<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		<#if e_field.getPageFieldName()=="zw_f_title">
			<span style="line-height:30px;color:#F37121;">（建议6个字以内）</span>
		</#if>
	<@m_zapmacro_common_field_end />
</#macro>

<#-- 字段：长文本框 -->
<#macro m_zapmacro_common_field_textarea e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<textarea id="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} name="${e_field.getPageFieldName()}">${e_field.getPageFieldValue()}</textarea>
		<#if e_field.getPageFieldName()=="zw_f_describes">
			<span style="line-height:30px;color:#F37121;">（建议10个字以内）</span>
		</#if>
	<@m_zapmacro_common_field_end />
</#macro>

<#-- 加必填标识 -->
<#macro m_zapmacro_common_field_start text="" for="">

	<div class="control-group" style="position: relative;">
		<label class="control-label" for="${for}">
			<#if (for=="zw_f_programa_picture")>
				<span class="w_regex_need">*</span>
			</#if>
			<#if (for=="zw_f_width")>
				<span class="w_regex_need">*</span>
				<span style="position: absolute;left: 420px;line-height:30px;color:#BFBCBC;"> %　　所填数字小于100</span>
			</#if>
			
			<#if (for=="zw_f_url")>
				<span class="w_regex_need">*</span>
				<span style="position: absolute;left: 430px;line-height:30px;color:#BFBCBC;top: 30px;">若填写的视频连接是ccvid,请以填写ccvid的编号,例如：3F394F2ACC69682D9C33DC5901307465</span>
				<span style="position: absolute;left: 430px;line-height:30px;color:#BFBCBC;">若填写的是页面链接，请填写正确的页面连接</span>
			</#if>
			<#if (for=="zw_f_city_name")>
				<span class="w_regex_need">*</span>
			</#if>
			<#if (for=="zw_f_event_code")>
				<span class="w_regex_need">*</span>
			</#if>
			<#if (for=="zw_f_img")>
				<span class="w_regex_need">*</span>
			</#if>
			<#if (for=="zw_f_template_desc")>
				<span style="position: absolute;left: 430px;line-height:30px;color:#BFBCBC;">描述字段（字数不得超过32字）</span>
			</#if>
			<#if (for=="zw_f_live_mobile")>
				<span class="w_regex_need">*</span>
			</#if>
			<#if '${t_type}' == '449747500015' >
				<#if for=="zw_f_commodity_location">
					<span class="w_regex_need">*</span>
					
						<#-- 暂为非必填
							<#elseif for=="zw_f_title">
								<span class="w_regex_need">*</span>
							<#elseif for=="zw_f_title_color">
								<span class="w_regex_need">*</span>
							<#elseif for=="zw_f_describes">
								<span class="w_regex_need">*</span>
							<#elseif for=="zw_f_describe_color">
								<span class="w_regex_need">*</span>
						-->
						
				</#if>
			</#if>
			<#if '${t_type}' == '449747500016'>
				<#if for=="zw_f_commodity_location">
					<span class="w_regex_need">*</span>
					
				<#-- 暂为非必填
					<#elseif for=="zw_f_title">
						<span class="w_regex_need">*</span>
					<#elseif for=="zw_f_title_color">
						<span class="w_regex_need">*</span>
					<#elseif for=="zw_f_describes">
						<span class="w_regex_need">*</span>
					<#elseif for=="zw_f_describe_color">
						<span class="w_regex_need">*</span>
				-->
				
				</#if>
			</#if>
			<#if '${t_type}' == '449747500018'||'${t_type}' == '449747500021'>
				<#if for=="zw_f_title">
					<span class="w_regex_need">*</span>
				</#if>
				<#if (for=="zw_f_sub_template_number")>
					<span class="w_regex_need">*</span>
				</#if>
			</#if>
			<#if '${t_type}' == '449747500019'>
				<#if for=="zw_f_title">
					<span class="w_regex_need">*</span>
				</#if>
			</#if>
			<#if (for=="zw_f_commodity_number")>
				<span class="w_regex_need">*</span>
			</#if>
			<#if (for=="zw_f_preferential_desc")>
				<span class="w_regex_need">*</span>
				<span style="position: absolute;left: 430px;line-height:30px;color:#F37121;">（优惠描述最多4个字！）</span>
			</#if>
			<#if '${t_type}' == '449747500025'>
				<#if for=="zw_f_commodity_picture">
					<span class="w_regex_need">*</span>
				</#if>
			</#if>
			${text}
		</label>
	<div class="controls">

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
	
	<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="addCommodity.submit(this);"  value="${e_operate.getOperateName()}" />
</#macro>

<#-- 字段：纯展示 -->
<#macro m_zapmacro_common_field_span e_field>
  	<#--过滤拼团模板 -->
	<#if e_field.getFieldName()=="commodity_number"&&'${t_type}' != '449747500023'&&'${t_type}' != '449747500026'>
		<div class="control-group" id="selectPcCtrol">
			<label class="control-label" for="zw_f_product_code">
				商品选择：
			</label>
			<#--
			<div class="controls">
				<div>
					<script type="text/javascript">
						zapjs.f.require(['zapadmin/js/templete_single'],function(a){a.init({"text":"","source":"page_chart_v_cf_pc_productinfo_multiSelect","id":"zw_f_product_code","value":"","max":"1"});});
					</script>
					<div class="w_left">
						<input id="zw_f_selectedProduct_id" class="btn" type="button" onclick="templete_single.show_box('zw_f_product_code','SI2003')" value="选择">
					</div>
				</div>
			</div>
			-->
			<div class="controls">
				<div>
					<input id="zw_f_sku_code" type="hidden" value="" name="zw_f_sku_code">
					<input id="zw_f_sku_code_show_text" type="hidden" value="">
					<#if '${t_type}' != '449747500022'>
					<input class="btn btn-small" type="button" value="选择商品" onclick="addCommodity.show_windows()">
					<#else>
					<script type="text/javascript">
						zapjs.f.require(['zapadmin/js/templete_single'],function(a){eventItemProduct_select.init({"text":"","source":"page_chart_cfamily_v_cf_pc_skuinfo_status","id":"zw_f_sku_code","value":"","seller_code":"SI2003","max":"100"});});
					</script>
					</#if>
				</div>
			</div>
		</div>
		<div id="linkvalueDiv" class="control-group">
			<script type="text/javascript">
	         zapjs.f.require(['zapadmin/js/commoditySelect'],function(a){a.init({"text":"","source":"page_chart_v_cf_pc_productinfo_multiSelect","id":"zw_f_skip_input","value":"","max":"1"});});
           	</script>
			<label class="control-label" for="zw_f_skip_input">
				分类：
			</label>
			<input id="zw_f_showmore_linkvalue" type="hidden">
			<div id="slId" class="controls">
				<input class="btn" type="button" value="选择分类" onclick="zapadmin.window_url('../show/page_chart_v_uc_sellercategory_select')">
			</div>
		</div>
	</#if>
	<#if e_field.getPageFieldName()="zw_f_commodity_number" || e_field.getPageFieldName()="zw_f_template_number" || e_field.getPageFieldName()="zw_f_good_number">
		<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
			<input id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}"  readonly="readonly"/>
		<@m_zapmacro_common_field_end />
	<#else>
		<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
			${e_field.getPageFieldValue()?default("")}
		<@m_zapmacro_common_field_end />
	</#if>
</#macro>
<#-- 页面定位模板专用 2017-02-24 zhy -->
<#macro m_zapmacro_common_field_span_ymmb e_pagedata e_page>
		<@m_zapmacro_common_field_start text=e_pagedata[0].getFieldNote() for="${e_pagedata[0].getPageFieldName()}" />
			<input type="text" id="${e_pagedata[0].getPageFieldName()}" name="${e_pagedata[0].getPageFieldName()}" value="${e_pagedata[0].getPageFieldValue()}"  readonly="readonly"/>
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_field_start text=e_pagedata[1].getFieldNote() for="${e_pagedata[1].getPageFieldName()}" />
			<input type="text" id="${e_pagedata[1].getPageFieldName()}" name="${e_pagedata[1].getPageFieldName()}" value="${e_pagedata[1].getPageFieldValue()}" />
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_field_start text=e_pagedata[20].getFieldNote() for="${e_pagedata[20].getPageFieldName()}" />
			<input type="text" id="${e_pagedata[20].getPageFieldName()}" name="${e_pagedata[20].getPageFieldName()}" value="${e_pagedata[20].getPageFieldValue()}" />
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_field_start text=e_pagedata[21].getFieldNote() for="${e_pagedata[21].getPageFieldName()}" />
			<input type="text" id="${e_pagedata[21].getPageFieldName()}" name="${e_pagedata[21].getPageFieldName()}" value="000000" />
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_field_start text=e_pagedata[22].getFieldNote() for="${e_pagedata[22].getPageFieldName()}" />
			<input type="text" id="${e_pagedata[22].getPageFieldName()}" name="${e_pagedata[22].getPageFieldName()}" value="F37121" />
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_field_start text=e_pagedata[11].getFieldNote() for="${e_pagedata[11].getPageFieldName()}" />
			<input type="text" autocomplete="off" onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})" id="zw_f_start_time" name="zw_f_start_time" value="">
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_field_start text=e_pagedata[12].getFieldNote() for="${e_pagedata[12].getPageFieldName()}" />
			<input type="text" autocomplete="off" onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})" id="zw_f_end_time" name="zw_f_end_time" value="">
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_field_start text=e_pagedata[26].getFieldNote() for="${e_pagedata[26].getPageFieldName()}" />
			
			<input type="text" style="margin-left:10px;display:none;" id="${e_pagedata[26].getPageFieldName()}" name="${e_pagedata[26].getPageFieldName()}" value="${e_pagedata[26].getPageFieldValue()}" readonly="readonly"/>
			
			<span style="color:red;">（不能再关联页面定位模板/定位横滑模板）</span>
			<div style="margin-top: 15px;"></div>
			<input id="zw_f_sub_template_number_show_text" style="margin-left:10px;display:none;" value="${e_pagedata[26].getPageFieldValue()}" readonly="readonly"/>
			<ul id="zw_f_sub_template_number_show_ul" style="list-style: none;">
				
				<#if (e_pagedata[26].getPageFieldValue()?length > 0) >
					<#assign templateNum = e_pagedata[26].getPageFieldValue()?split(",") >
					
					<#list templateNum as num>
						<#if (num?length > 0) >
							<li id="select_num_${num }" class="control-upload-list-li"><div><div class="w_left w_p_10"><span>${num }</span><a href="javascript:void(0);" onclick="pageTemplete_single.change_index(this,'up')">上移</a><a href="javascript:void(0);" onclick="pageTemplete_single.change_index(this,'down')">下移</a><a href="javascript:void(0);" onclick="pageTemplete_single.change_index(this,'delete')">删除</a></div><div class="w_clear"></div></div></li>
						</#if>
					</#list>
				</#if>
			
			</ul>
			
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/dateTime'],function(a){});" />
		<script>
			zapjs.f.require(['zapadmin/js/pageTemplete_single'],function(a){pageTemplete_single.init({"text":"","source":"page_chart_v_fh_pageTemple_single","id":"zw_f_sub_template_number","value":"","seller_code":"SI2003","max":"100"});});
		</script>
</#macro>


<#-- 定位横滑模板 2019-07-04 zhangb -->
<#macro m_zapmacro_common_field_span_dwhh e_pagedata e_page>
		<@m_zapmacro_common_field_start text=e_pagedata[0].getFieldNote() for="${e_pagedata[0].getPageFieldName()}" />
			<input type="text" id="${e_pagedata[0].getPageFieldName()}" name="${e_pagedata[0].getPageFieldName()}" value="${e_pagedata[0].getPageFieldValue()}"  readonly="readonly"/>
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_field_start text=e_pagedata[1].getFieldNote() for="${e_pagedata[1].getPageFieldName()}" />
			<input type="text" id="${e_pagedata[1].getPageFieldName()}" name="${e_pagedata[1].getPageFieldName()}" value="${e_pagedata[1].getPageFieldValue()}" />
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_field_start text=e_pagedata[20].getFieldNote() for="${e_pagedata[20].getPageFieldName()}" />
			<input type="text" id="${e_pagedata[20].getPageFieldName()}" name="${e_pagedata[20].getPageFieldName()}" value="${e_pagedata[20].getPageFieldValue()}" />
		<@m_zapmacro_common_field_end />		
	
		<@m_zapmacro_common_field_start text=e_pagedata[11].getFieldNote() for="${e_pagedata[11].getPageFieldName()}" />
			<input type="text" autocomplete="off" onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})" id="zw_f_start_time" name="zw_f_start_time" value="">
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_field_start text=e_pagedata[12].getFieldNote() for="${e_pagedata[12].getPageFieldName()}" />
			<input type="text" autocomplete="off" onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})" id="zw_f_end_time" name="zw_f_end_time" value="">
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_field_start text=e_pagedata[26].getFieldNote() for="${e_pagedata[26].getPageFieldName()}" />
			
			<input type="text" style="margin-left:10px;display:none;" id="${e_pagedata[26].getPageFieldName()}" name="${e_pagedata[26].getPageFieldName()}" value="${e_pagedata[26].getPageFieldValue()}" readonly="readonly"/>
			
			<span style="color:red;">（不能再关联页面定位模板/定位横滑模板）</span>
			<div style="margin-top: 15px;"></div>
			<input id="zw_f_sub_template_number_show_text" style="margin-left:10px;display:none;" value="${e_pagedata[26].getPageFieldValue()}" readonly="readonly"/>
			<ul id="zw_f_sub_template_number_show_ul" style="list-style: none;">
				
				<#if (e_pagedata[26].getPageFieldValue()?length > 0) >
					<#assign templateNum = e_pagedata[26].getPageFieldValue()?split(",") >
					
					<#list templateNum as num>
						<#if (num?length > 0) >
							<li id="select_num_${num }" class="control-upload-list-li"><div><div class="w_left w_p_10"><span>${num }</span><a href="javascript:void(0);" onclick="pageTemplete_single.change_index(this,'up')">上移</a><a href="javascript:void(0);" onclick="pageTemplete_single.change_index(this,'down')">下移</a><a href="javascript:void(0);" onclick="pageTemplete_single.change_index(this,'delete')">删除</a></div><div class="w_clear"></div></div></li>
						</#if>
					</#list>
				</#if>
			
			</ul>
			
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/dateTime'],function(a){});" />
		<script>
			zapjs.f.require(['zapadmin/js/pageTemplete_single'],function(a){pageTemplete_single.init({"text":"","source":"page_chart_v_fh_pageTemple_single","id":"zw_f_sub_template_number","value":"","seller_code":"SI2003","max":"100"});});
		</script>
</#macro>

<#-- 倒计时模板专用 2018-11-28 zhangb -->
<#macro m_zapmacro_common_field_span_djsmb e_pagedata e_page>
		<@m_zapmacro_common_field_start text=e_pagedata[0].getFieldNote() for="${e_pagedata[0].getPageFieldName()}" />
			<input type="text" id="${e_pagedata[0].getPageFieldName()}" name="${e_pagedata[0].getPageFieldName()}" value="${e_pagedata[0].getPageFieldValue()}"  readonly="readonly"/>
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_field_start text=e_pagedata[20].getFieldNote() for="${e_pagedata[20].getPageFieldName()}" />
			<input type="text" id="${e_pagedata[20].getPageFieldName()}" name="${e_pagedata[20].getPageFieldName()}" value="${e_pagedata[20].getPageFieldValue()}" />
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_field_start text=e_pagedata[21].getFieldNote() for="${e_pagedata[21].getPageFieldName()}" />
			<input type="text" id="${e_pagedata[21].getPageFieldName()}" name="${e_pagedata[21].getPageFieldName()}" value="fff" />
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_field_start text=e_pagedata[17].getFieldNote() for="${e_pagedata[17].getPageFieldName()}" />
		 <input type="hidden" zapweb_attr_target_url="${e_page.upConfig("zapweb.upload_target")}" zapweb_attr_set_params="${e_pagedata[17].getSourceParam()}"    id="${e_pagedata[17].getPageFieldName()}" name="${e_pagedata[17].getPageFieldName()}" value="${e_pagedata[17].getPageFieldValue()}">
		 <span class="control-upload_iframe"></span>
		 <span class="control-upload_process"></span>
		 <span class="control-upload"></span>
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_html_script "zapjs.f.ready(function(){zapjs.f.require(['zapweb/js/zapweb_upload'],function(a){a.upload_file('"+e_pagedata[17].getPageFieldName()+"','"+e_page.upConfig("zapweb.upload_target")+"');}); });" />
		<@m_zapmacro_common_field_start text=e_pagedata[12].getFieldNote() for="${e_pagedata[12].getPageFieldName()}" />
			<input type="text" autocomplete="off" onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})" id="zw_f_end_time" name="zw_f_end_time" value="">
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});" />
</#macro>
<script>
require(['cfamily/js/addCommodity'],function(b){b.init(${t_type})});
</script>
<script type="text/javascript">
    $(function(){
	   $('#zw_f_title_color').colpick({
			layout:'hex',
			submit:0,
			colorScheme:'dark',
			onChange:function(hsb,hex,rgb,el,bySetColor) {
				$(el).css('border-color','#'+hex);
				if(!bySetColor) $(el).val(hex);
			}
		}).keyup(function(){});
		 $('#zw_f_describe_color').colpick({
			layout:'hex',
			submit:0,
			colorScheme:'dark',
			onChange:function(hsb,hex,rgb,el,bySetColor) {
				$(el).css('border-color','#'+hex);
				if(!bySetColor) $(el).val(hex);
			}
		}).keyup(function(){});
		//标签选择颜色设置 2017-02-24 zhy
	   $('#zw_f_title_checked_color').colpick({
			layout:'hex',
			submit:0,
			colorScheme:'dark',
			onChange:function(hsb,hex,rgb,el,bySetColor) {
				$(el).css('border-color','#'+hex);
				if(!bySetColor) $(el).val(hex);
			}
		}).keyup(function(){});
		
    });
</script>