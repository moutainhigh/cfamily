

<style type="text/css">
	.catagoryText{width:100px;}
	.table{text-align:center;margin-left:2%;}
	.table th,.table td{text-align:center;}
	.btnC{margin-top:10px;margin-left:2%;}
	.catagory{margin-top:10px;}
</style>
<script type="text/javascript" src="../resources/cfamily/js/add_dlq_info.js"></script>

<#assign uid = b_page.getReqMap()["zw_f_uid"] >
<#assign pageType = b_page.getReqMap()["zw_f_page_type"] />
<#assign pageNum = b_page.getReqMap()["zw_f_page_number"] />
<#assign tv_number = b_page.getReqMap()["zw_f_tv_number"] />
<#assign show_page_type = b_page.getReqMap()["show_page_type"] >
<input type="hidden" name="zw_f_page_type" id="zw_f_page_type" value="${pageType}" />
<input type="hidden" name="zw_f_page_number" id="zw_f_page_number" value="${pageNum}" />
<input type="hidden" name="zw_f_tv_number" id="zw_f_tv_number" value="${tv_number}" />

<#-- 展示对应的TV名称 -->
	<#assign dlqService=b_method.upClass("com.cmall.familyhas.service.DLQService")>
	<#if pageType == "1000" >
	
		<#assign tvList = dlqService.getTvList() >
		<#if tvList?? && (tvList?size > 0) >
			<#assign isHasTv = false />
			<#list tvList as tv>
				<#if tv_number == tv["tv_number"]>
					<#assign isHasTv = true />
					<h4 style="width:100%;text-align:center;color:#008299;">电视节目:&nbsp;&nbsp;&nbsp;&nbsp;${tv["tv_name"]}</h4>
				</#if>
			</#list>
			<#if isHasTv == false >
				<h4 style="width:100%;text-align:center;color:#008299;">电视节目:&nbsp;&nbsp;&nbsp;&nbsp;默认节目</h4>
			</#if>
		<#else>
			<h4 style="width:100%;text-align:center;color:#008299;">电视节目:&nbsp;&nbsp;&nbsp;&nbsp;默认节目</h4>
		</#if>
		
	<#elseif pageType == "1001" >
		<#assign tvList = dlqService.getClsList() >
		<#if tvList?? && (tvList?size > 0) >
			<#assign isHasTv = false />
			<#list tvList as tv>
				<#if tv_number == tv["tv_number"]>
					<#assign isHasTv = true />
					<h4 style="width:100%;text-align:center;color:#008299;">渠道:&nbsp;&nbsp;&nbsp;&nbsp;${tv["tv_name"]}</h4>
				</#if>
			</#list>
			<#if isHasTv == false >
				<h4 style="width:100%;text-align:center;color:#008299;">渠道:&nbsp;&nbsp;&nbsp;&nbsp;默认节目</h4>
			</#if>
		<#else>
			<h4 style="width:100%;text-align:center;color:#008299;">渠道:&nbsp;&nbsp;&nbsp;&nbsp;默认节目</h4>
		</#if>
	</#if>

<#assign contentDate = b_method.upControlPage("page_chart_v_fh_dlq_content","zw_p_sql_where=page_number='${pageNum}' and tv_number='${tv_number}' and delete_state = '1001'&zw_p_size=-1").upChartData()>

<@m_zapmacro_common_page_edit b_page contentDate pageNum show_page_type tv_number/>
	


<#-- 修改页 -->
<#macro m_zapmacro_common_page_edit e_page contentDate pageNum show_page_type tv_number>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upEditData()   e_page  />
	
	<@contentInfo contentDate pageNum show_page_type tv_number/>
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata e_page >

	<#if e_pagedata??>
	<#list e_pagedata as e>
		<#assign show_page_type = b_page.getReqMap()["show_page_type"] >
		<#assign pageType = b_page.getReqMap()["zw_f_page_type"] />
		<#if  pageType=="1001" && e.getPageFieldName() == "zw_f_cuisine_name">
			<#-- 如果是非大陆桥渠道，则隐藏菜系名称字段 -->
		<#elseif show_page_type == "show" && e.getPageFieldName() != "zw_f_uid">
			<#if e.getPageFieldName() == "zw_f_cuisine_picture" >
				<@m_zapmacro_common_field_start text=e.getFieldNote() for=e.getPageFieldName() />
					<#if (e.getPageFieldValue()?length > 0) >
						<img src="${e.getPageFieldValue()?default("")}" style="width:150px;"/>
						
					</#if>
				<@m_zapmacro_common_field_end />
			<#else>
				<@m_zapmacro_common_field_span e/>
			</#if>
		<#else>
		  	<@m_zapmacro_common_auto_field e e_page/>
		</#if>
	  	 
	</#list>
	</#if>
</#macro>


<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
	<#if e_field.getPageFieldName() == "zw_f_url">
		<div>
			<@m_zapmacro_common_field_text  e_field />
			<span style="position: absolute;left: 460px;line-height:30px;color:#BFBCBC;top:285px;">若填写的视频连接是ccvid,请已ccvid开头,例如：ccvid:3F394F2ACC69682D9C33DC5901307465</span>
		</div>
	<#else>
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
	  		<@m_zapmacro_common_field_select  e_field  e_page ""/>
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
	</#if>
</#macro>


<#-- 字段：上传 -->
<#macro m_zapmacro_common_field_upload e_field e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="hidden" zapweb_attr_target_url="${e_page.upConfig("zapweb.upload_target")}" zapweb_attr_set_params="${e_field.getSourceParam()}"    id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
		<span class="control-upload_iframe"></span>
		<span class="control-upload_process"></span>
		<span class="control-upload"></span>
	<@m_zapmacro_common_field_end />
	
	<@m_zapmacro_common_html_script "zapjs.f.ready(function(){zapjs.f.require(['zapweb/js/zapweb_upload'],function(a){a.upload_file('"+e_field.getPageFieldName()+"','"+e_page.upConfig("zapweb.upload_target")+"');}); });" />
	<#if e_field.getPageFieldName() == "zw_f_cuisine_picture">
		<span style="position: absolute;left: 460px;line-height: 30px;color: #BFBCBC;top: 216px;">图片建议尺寸: 1080 * 606   </span>
	</#if>
	  
	  
	  
</#macro>


<#macro contentInfo  contentDate  pageNum show_page_type tv_number>
<#-- 获取关联内容 -->
<#assign contentService=b_method.upClass("com.cmall.familyhas.service.DLQService")>
<#assign pcInfo = contentService.getRelProductInfo("${pageNum}","${tv_number}") >
	<#-- 添加优惠券活动编号功能模板  start  -->
	<div class="catagory c8">
		<div style="border-bottom: solid 1px #008299;" class="">
			<#if (contentDate.getPageData()?size > 0 )>
				<#list contentDate.getPageData() as e_list>
					<#if e_list[8] == "N8">
						优惠券活动编号 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText"  <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_activity_code8" type="text" value="${e_list[11]?default("")}"/>
						<#break>
					</#if>
					<#if e_list_index == (contentDate.getPageData()?size-1) >
						优惠券活动编号 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_activity_code8" type="text" value=""/>
					</#if>
				</#list>
			<#else>
				优惠券活动编号 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_activity_code8" type="text" value=""/>
			</#if>
		</div>
	</div>
	<#-- 添加优惠券活动编号功能模板  end  -->
	<#if pageType == '1001' >
		<div class="catagory c9">
			<div style="border-bottom: solid 1px #008299;" class="">
				<#if (contentDate.getPageData()?size > 0 )>
					<#list contentDate.getPageData() as e_list>
						<#if e_list[8] == "N9">
							<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText"  <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_name9" type="text" value="${e_list[0]?default("")}"/>
							<#break>
						</#if>
						<#if e_list_index == (contentDate.getPageData()?size-1) >
							<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_name9" type="text" value="本期介绍"/>
						</#if>
					</#list>
				<#else>
					<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_name9" type="text" value="本期介绍"/>
				</#if>
			</div>
			<div style="margin-top:10px;margin-left:30px;">
				<#list contentDate.getPageData() as e_list>
					<#if e_list[8] == "N9" >
						<#if show_page_type == "show">
							<p>${e_list[13]}</p>
						<#else>
							<textarea  id="zw_f_t_describ" name="zw_f_t_describ" style="width:40%;">${e_list[13]}</textarea>
						</#if>
						<#break>
				    </#if>
				    <#if e_list_index == (contentDate.getPageData()?size-1) >
				    	<#if show_page_type == "show">
				    		<p></p>
				    	<#else>
							<textarea  id="zw_f_t_describ" name="zw_f_t_describ" style="width:40%;"></textarea>
				    	</#if>
					</#if>
			 	</#list>
				<#if (contentDate.getPageData()?size <= 0 )>
			 		<#if show_page_type == "show">
			    		<p></p>
			    	<#else>
				    	<textarea  id="zw_f_t_describ" name="zw_f_t_describ" style="width:40%;"></textarea>
			    	</#if>
			 	</#if>
			</div>
		</div>
		
		<div class="catagory c10">
			<div style="border-bottom: solid 1px #008299;" class="">
				<#if (contentDate.getPageData()?size > 0 )>
					<#list contentDate.getPageData() as e_list>
						<#if e_list[8] == "N10">
							<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText"  <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_name10" type="text" value="${e_list[0]?default("")}"/>
							<#break>
						</#if>
						<#if e_list_index == (contentDate.getPageData()?size-1) >
							<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_name10" type="text" value="栏目介绍"/>
						</#if>
					</#list>
				<#else>
					<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_name10" type="text" value="栏目介绍"/>
				</#if>
			</div>
			<div style="margin-top:10px;margin-left:30px;">
				<#list contentDate.getPageData() as e_list>
					<#if e_list[8] == "N10" >
						<#if show_page_type == "show">
				    		<p>${e_list[12]}</p>
				    	<#else>
							<textarea  id="zw_f_column_desc" name="zw_f_column_desc" style="width:40%;">${e_list[12]}</textarea>
						</#if>
						<#break>
				    </#if>
				    <#if e_list_index == (contentDate.getPageData()?size-1) >
				    	<#if show_page_type == "show">
				    		<p></p>
				    	<#else>
					    	<textarea  id="zw_f_column_desc" name="zw_f_column_desc" style="width:40%;"></textarea>
				    	</#if>
				    </#if>
			 	</#list>
			 	<#if (contentDate.getPageData()?size <= 0 )>
			 		<#if show_page_type == "show">
			    		<p></p>
			    	<#else>
				    	<textarea  id="zw_f_column_desc" name="zw_f_column_desc" style="width:40%;"></textarea>
			    	</#if>
			 	</#if>
			</div>
		</div>
	</#if>
	<#if pageType == '1000'><#-- c1和c2只是大陆桥展示 -->
	<div class="catagory c1">
		<div style="border-bottom: solid 1px #008299;">
			<#if (contentDate.getPageData()?size > 0 )>
				<#list contentDate.getPageData() as e_list>
					<#if e_list[8] == "N1">
						
						<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" type="text" <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_name1" value="${e_list[0]?default("")}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<span class="w_regex_need">*</span>对应英文 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" type="text" <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_english1" value="${e_list[1]?default("")}"/>
						
						<#break>
					</#if>
					<#if e_list_index == (contentDate.getPageData()?size-1) >
						<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" type="text" <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_name1" value="食材准备"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<span class="w_regex_need">*</span>对应英文 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" type="text" <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_english1" value="ingredients"/>
					</#if>
				</#list>
			<#else>
				<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_name1" value="食材准备"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<span class="w_regex_need">*</span>对应英文 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_english1" value="ingredients"/>
			</#if>
		</div>
		<table class="table table-condensed table-striped table-bordered table-hover" style="width:30%;margin-top:10px;">
			<thead>
			    <tr>
			      	 <th>食材名</th>
			      	 <th>份量</th>
			      	 <#if show_page_type == "edit"><th>修改</th><th>操作</th></#if>
			      	 
			    </tr>
		  	</thead>
			<tbody>
				<#list contentDate.getPageData() as e_list>
					<#if e_list[8] == "N1" >
						<#if (e_list[2]?length > 0) >
							<tr>
						  		 <#list e_list as e>
						  		 	<#if e_index == e_list?size -1 && show_page_type == "show">
						  		 	<#elseif (e_index == 2  || e_index == 3 || (e_index == 14 && show_page_type == "edit") || e_index == 15) >
							      		<td> ${e?default("")} </td>
						  		 	</#if>
						      	</#list>
					      	</tr>
					    </#if>
				    </#if>
			 	</#list>
			 	<#if show_page_type == "edit">
			 		<tr>
			      		<td><input type="text" class="catagoryText" value="" /></td>
			      		<td><input type="text" class="catagoryText" value="" /></td>
			      		<td></td>
			      		<td><input class="btn btn-small" onclick="add_dlq_info.subAddContent(this,'N1')" type="button" value="添加"></td>
			      	</tr>
			 		
			 	</#if>
			</tbody>
		</table>
	</div>
	
	
	<div class="catagory c2">
		<div style="border-bottom: solid 1px #008299;">
			<#if (contentDate.getPageData()?size > 0 )>
				<#list contentDate.getPageData() as e_list>
					<#if e_list[8] == "N2">
						栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" type="text" <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_name2" value="${e_list[0]?default("")}"/>
						<#break>
					</#if>
					<#if e_list_index == (contentDate.getPageData()?size-1) >
						栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_name2" value="食材盒"/>
					</#if>
				</#list>
			<#else>
				栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_name2" value="食材盒"/>
			</#if>
		</div>
		<div class="btnC">
			<#if show_page_type == "edit">
				<input type="button" class="btn" value="添加商品" onclick="zapadmin.window_url('../show/page_add_v_fh_dlq_content?zw_f_id_number=N2&zw_f_page_number=${pageNum}') "/>
				<input type="button" class="btn" value="排序设置" onclick="zapadmin.window_url('../show/page_sort_v_fh_dlq_content?zw_f_page_number=${pageNum}&zw_f_id_number=N2&zw_f_tv_number=${tv_number}') "/>
			</#if>
		</div>
		<table  class="table  table-condensed table-striped table-bordered table-hover"  style="width:60%;">
			<thead>
			    <tr>
					<th>商品编号</th>
					<th>商品名称</th>
					<th>商品状态</th>
					<th>图片</th>
					<th>位置</th>
				<#if show_page_type == "edit">
					<th>修改</th>
					<th>删除</th>
				</#if>
			    </tr>
		  	</thead>	  
			<tbody>
				<#list contentDate.getPageData() as e_list>
					<#if e_list[8] == "N2" >
						<#if (e_list[4]?length>0) >
							<tr>
							    <td> ${e_list[4]} </td>
						      	<#list pcInfo?keys as key>
							      	<#if key == "N2-"+e_list[4]>
						                <td>
						               		${pcInfo[key]["product_name"]}
						                </td>
						                <td>
							                <#if '4497153900060002' == pcInfo[key]["product_status"]>
							                	已上架
							                <#else>
							                	已下架
							                </#if>
						                </td>
						                <td>
						                	<img src="${pcInfo[key]["picture"]}" style="width:100px;" />
						                </td>
						                <td>
						                	${e_list[6]}
						                </td>
						                <#if show_page_type == "edit">
							                <td>
							                	${e_list[14]}
							                </td>
							                <td>
							                	${e_list[15]}
							                </td>
						                </#if>
					                
							        </#if>
						        </#list>
					      	</tr>
					    </#if>
				    </#if>
			 	</#list>
			 	
			</tbody>
		</table>
	</div>
	</#if>
	<div class="catagory c3">
		<div style="border-bottom: solid 1px #008299;">
			<#if (contentDate.getPageData()?size > 0 )>
				<#list contentDate.getPageData() as e_list>
					<#if e_list[8] == "N3">
						<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_name3" value="${e_list[0]?default("")}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<span class="w_regex_need">*</span>对应英文 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_english3" value="${e_list[1]?default("")}"/>
						<#break>
					</#if>
					<#if e_list_index == (contentDate.getPageData()?size-1) >
						<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_name3" <#if pageType == "1000">value="美食全记录"<#elseif pageType == "1001">value="主持人"</#if>>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<span class="w_regex_need">*</span>对应英文 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_english3" value="Record"/>
					</#if>
				</#list>
			<#else>
				<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_name3" <#if pageType == "1000">value="美食全记录"<#elseif pageType == "1001">value="主持人"</#if>>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<span class="w_regex_need">*</span>对应英文 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_english3" value="Record"/>
			</#if>
		</div>
		<div class="btnC"><#if show_page_type == "edit"><input type="button" class="btn" <#if pageType == '1000'> value="添加步骤" <#elseif pageType == '1001'> value="添加主持人信息" </#if>  onclick="add_dlq_info.addStep()" /></#if></div>
		<table class="table table-condensed table-striped table-bordered table-hover" style="width:60%;">
			<thead>
			    <tr>
			      	 <th style="width:100px;">图片</th>
			      	 <th>描述</th>
			      	 <#if show_page_type == "edit">
				      	 <th>修改</th>
				      	 <th>删除</th>
				     </#if>
			    </tr>
		  	</thead>
		  
			<tbody>
				
					<#list contentDate.getPageData() as e_list>
				      	<#if e_list[8] == "N3" >
							<#if (e_list[5]?length > 0) >
								<tr>
									<td>
										<img src="${e_list[5]}" style="width:100px;" />
									</td>
									<td>${e_list[7]}</td>
									<#if show_page_type == "edit">
										<td>${e_list[14]}</td>
										<td>${e_list[15]}</td>
									</#if>
						      	</tr>
							</#if>
					    </#if>
				 	</#list>
				 	
			</tbody>
		</table>
	</div>
	
	
	<div class="catagory c4">
		<div style="border-bottom: solid 1px #008299;">
			<#if (contentDate.getPageData()?size > 0 )>
				<#list contentDate.getPageData() as e_list>
					<#if e_list[8] == "N4">
						<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_name4" type="text" value="${e_list[0]?default("")}"/>
						<#break>
					</#if>
					<#if e_list_index == (contentDate.getPageData()?size-1) >
						<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_name4" type="text" value="广告位"/>
					</#if>
				</#list>
			<#else>
				<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_name4" type="text" value="广告位"/>
			</#if>
			
		</div>
		<div class="btnC"><#if show_page_type == "edit"><input type="button" class="btn" value="添加广告" onclick="add_dlq_info.addGuangg()"/></#if></div>
		<table class="table table-condensed table-striped table-bordered table-hover" style="width:80%;">
			<thead>
			    <tr>
			      	 <th>图片</th>
			      	 <th>位置</th>
			      	 <th>链接值</th>
			      	 <th>开始时间</th>
			      	 <th>结束时间</th>
			      	 <#if show_page_type == "edit">
				      	 <th>修改</th>
				      	 <th>删除</th>
			      	 </#if>
			    </tr>
		  	</thead>
		  
			<tbody>
					<#assign advertisementDate = b_method.upControlPage("page_chart_v_fh_dlq_picture","zw_p_sql_where=page_number='${pageNum}' and tv_number='${tv_number}' and id_number='1000' and delete_state = '1001'&zw_p_size=-1").upChartData()>
					
					<#list advertisementDate.getPageData() as e_list>
						<tr>
				      	<td>${e_list[0]}</td>
				      	<td>
				      		<#if e_list[4] == "449747760001" >
				      			上部
				      		<#elseif e_list[4] == "449747760002" >
				      			下部
				      		</#if>				      	
				      	</td>
				      	<td><a target="_blanck" href="${e_list[3]}">${e_list[3]}</a></td>
				      	<td>${e_list[1]}</td>
				      	<td>${e_list[2]}</td>
				      	<#if show_page_type == "edit">
					      	<td>${e_list[6]}</td>
					      	<td>${e_list[7]}</td>
				      	</#if>
				      	</tr>
				 	</#list>
					
			</tbody>
		</table>
	</div>
	
	
	<div class="catagory c5">
		<div style="border-bottom: solid 1px #008299;">
			<#if (contentDate.getPageData()?size > 0 )>
				<#list contentDate.getPageData() as e_list>
					<#if e_list[8] == "N5">
						<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_name5" value="${e_list[0]?default("")}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<span class="w_regex_need">*</span>对应英文 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_english5" value="${e_list[1]?default("")}"/>
						<#break>
					</#if>
					<#if e_list_index == (contentDate.getPageData()?size-1) >
						<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_name5" value="嘉宾厨具直购">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<span class="w_regex_need">*</span>对应英文 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_english5" value="Shopping"/>
					</#if>
				</#list>
			<#else>
				<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_name5" value="嘉宾厨具直购">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<span class="w_regex_need">*</span>对应英文 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_english5" value="Shopping"/>
			</#if>
		
		</div>
		<div class="btnC" style="width:100%;overflow:auto;">
			<div style="display:inline;float:left;">
				<#if show_page_type == "edit">
					<#-- <span class="w_regex_need">*</span>添加商品 :&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="btn" value="选择商品" onclick="add_dlq_info.show_windows(5)"/> -->
					<input type="button" class="btn" value="添加商品" onclick="zapadmin.window_url('../show/page_add_v_fh_dlq_content?zw_f_id_number=N5&zw_f_page_number=${pageNum}') "/>
					<input type="button" class="btn" value="排序设置" onclick="zapadmin.window_url('../show/page_sort_v_fh_dlq_content?zw_f_page_number=${pageNum}&zw_f_id_number=N5&zw_f_tv_number=${tv_number}') "/>
				</#if>
			</div>
		</div>
			<table  class="table  table-condensed table-striped table-bordered table-hover"  style="width:60%;">
				<thead>
				    <tr>
						<th>商品编号</th>
						<th>商品名称</th>
						<th>商品状态</th>
						<th>图片</th>
						<th>位置</th>
					<#if show_page_type == "edit">
						<th>修改</th>
						<th>删除</th>
					</#if>
				    </tr>
			  	</thead>	  
				<tbody>
					<#list contentDate.getPageData() as e_list>
						<#if e_list[8] == "N5" >
							<#if (e_list[4]?length>0) >
								<tr>
								    <td> ${e_list[4]} </td>
							      	<#list pcInfo?keys as key>
								      	<#if key == "N5-"+e_list[4]>
							                <td>
							               		${pcInfo[key]["product_name"]}
							                </td>
							                <td>
								                <#if '4497153900060002' == pcInfo[key]["product_status"]>
								                	已上架
								                <#else>
								                	已下架
								                </#if>
							                </td>
							                <td>
							                	<img src="${pcInfo[key]["picture"]}" style="width:100px;" />
							                </td>
							                <td>
							                	${e_list[6]}
							                </td>
							                <#if show_page_type == "edit">
								                <td>
								                	${e_list[14]}
								                </td>
								                <td>
								                	${e_list[15]}
								                </td>
							                </#if>
						                
								        </#if>
							        </#list>
						      	</tr>
						    </#if>
					    </#if>
				 	</#list>
				</tbody>
			</table>
			
		
			
			<#-- 
			<ul id="showN5SelectedProduct" class="zab_js_zapadmin_single_ul" >
				<#list contentDate.getPageData() as e_list>
					<#if e_list[8] == "N5" >
				      	<#list pcInfo?keys as key>
				      		<#if key == "N5-"+e_list[4] && (key?length > 0)>
								<li style="position: relative;margin-left:9px;margin-top:8.5px">${pcInfo[key]["product_name"]?default("")}
									<#if show_page_type == "edit">
										<i zapweb_attr_operate_id="0b346f9ed93d11e5aad9005056925439" style="position: absolute;background: #f1f1f1;border: solid 1px #ccc;text-align: center;  right: -10px;top: -10px;font-style:normal;  width: 15px;height: 15px;line-height: 15px;cursor: pointer;display: inline-block;" onclick="add_dlq_info.delZGProduct(this,'${pcInfo[key]["uid"]}')">X</i>
									</#if>
								</li>
								<#break>
							</#if>
				        </#list>
				    </#if>
			 	</#list>
			</ul>
			-->
			
			
	</div>
	
	<div class="catagory c6">
		<div style="border-bottom: solid 1px #008299;" class="">
			<#if (contentDate.getPageData()?size > 0 )>
				<#list contentDate.getPageData() as e_list>
					<#if e_list[8] == "N6">
						<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText"  <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_name6" type="text" value="${e_list[0]?default("")}"/>
						<#break>
					</#if>
					<#if e_list_index == (contentDate.getPageData()?size-1) >
						<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_name6" type="text" value="推荐"/>
					</#if>
				</#list>
			<#else>
				<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> name="zw_f_programa_name6" type="text" value="推荐"/>
			</#if>
		</div>
	</div>
	
	<#if pageType == '1000' >
	<div class="catagory c7" style="margin-bottom:20px;">
		<div style="border-bottom: solid 1px #008299;">
			<#if (contentDate.getPageData()?size > 0 )>
				<#list contentDate.getPageData() as e_list>
					<#if e_list[8] == "N7">
						<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_name7" value="${e_list[0]?default("")}"/>
						<#break>
					</#if>
					<#if e_list_index == (contentDate.getPageData()?size-1) >
						<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_name7" value="赞助商"/>
					</#if>
				</#list>
			<#else>
				<span class="w_regex_need">*</span>栏目名称 :&nbsp;&nbsp;&nbsp;&nbsp;<input class="catagoryText" <#if show_page_type == "show">disabled="disabled"</#if> type="text" name="zw_f_programa_name7" value="赞助商"/>
			</#if>
			
		</div>
		<#if show_page_type == "edit">
			<div class="control-group">
				<label class="control-label" for="zw_f_description_pic" style="text-align:left;width:100px;">添加图片 :</label>
				<div class="controls" style="margin-left:80px;">
					<span style="position: absolute;left: 310px;line-height: 30px;color: #BFBCBC;">建议尺寸: 408 * 156</span>
					
					<#if (contentDate.getPageData()?size > 0 )>
						<#list contentDate.getPageData() as e_list>
							<#if e_list[8] == "N7">
								<input type="hidden" zapweb_attr_target_url="../upload/" zapweb_attr_set_params="zw_s_number=100" id="zw_f_description_pic" name="zw_f_description_pic" value='${e_list[5]?default("")}'>
							</#if>
							<#if e_list_index == (contentDate.getPageData()?size-1) >
								<input type="hidden" zapweb_attr_target_url="../upload/" zapweb_attr_set_params="zw_s_number=100" id="zw_f_description_pic" name="zw_f_description_pic" value="">
							</#if>
						</#list>
					<#else>
						<input type="hidden" zapweb_attr_target_url="../upload/" zapweb_attr_set_params="zw_s_number=100" id="zw_f_description_pic" name="zw_f_description_pic" value="">
					</#if>
					<span class="control-upload_iframe"><iframe src="../upload/upload?zw_s_source=zw_f_description_pic&amp;zw_s_description=" class="zw_page_upload_iframe" frameborder="0"></iframe></span>
					<span class="control-upload_process"></span>
					<span class="control-upload">
						<#if (contentDate.getPageData()?size > 0 )>
							<#list contentDate.getPageData() as e_list>
								<#if e_list[8] == "N7" && (e_list[5]?length > 0)>
									<#assign picList = e_list[5]?split("|") />
									<ul>
								         <#if (picList?size > 1) >
											<#list picList as name1>
										         <li class="control-upload-list-li">
													<div>
														<div class="w_left w_w_100">
															<a href="${name1?default("")}" target="_blank">
																<img src="${name1?default("")}" />
															</a>
														</div>
														<div class="w_left w_p_10">
															<a href="javascript:zapweb_upload.change_index('zw_f_description_pic',0,'up')">上移</a>
															<a href="javascript:zapweb_upload.change_index('zw_f_description_pic',0,'down')">下移</a>
															<a href="javascript:zapweb_upload.change_index('zw_f_description_pic',0,'delete')">删除</a>
														</div>
														<div class="w_clear">
														</div>
													</div>
												 </li>
										    </#list>
								         <#elseif picList?size == 1 >
								         	<#list picList as name1>
										         <li class="control-upload-list-li">
													<div>
														<div class="w_left w_w_100">
															<a href="${name1?default("")}" target="_blank">
																<img src="${name1?default("")}">
															</a>
														</div>
														<div class="w_left w_p_10">
															<a href="javascript:zapweb_upload.change_index('zw_f_description_pic',0,'delete')">删除</a>
														</div>
														<div class="w_clear">
														</div>
													</div>
												 </li>
										    </#list>
								         </#if>
									</ul>	
									<#break>
								</#if>
							</#list>
										
						</#if>	
						
					</span>
				</div>
			</div>
		<#else>
			<#if (contentDate.getPageData()?size > 0 )>
				<#list contentDate.getPageData() as e_list>
					<#if e_list[8] == "N7" && (e_list[5]?length > 0)>
						<#assign picList = e_list[5]?split("|") />
						<span class="control-upload">
						<ul>
					         <#if (picList?size > 1) >
								<#list picList as name1>
							         <li class="control-upload-list-li">
										<div>
											<div class="w_left w_w_100">
												<a href="${name1?default("")}" target="_blank">
													<img src="${name1?default("")}" />
												</a>
											</div>
											<div class="w_clear">
											</div>
										</div>
									 </li>
							    </#list>
					         <#elseif picList?size == 1 >
					         	<#list picList as name1>
							         <li class="control-upload-list-li">
										<div>
											<div class="w_left w_w_100">
												<a href="${name1?default("")}" target="_blank">
													<img src="${name1?default("")}">
												</a>
											</div>
											<div class="w_clear">
											</div>
										</div>
									 </li>
							    </#list>
					         </#if>
						</ul>	
						</span>
						<#break>
					</#if>
				</#list>
							
			</#if>			
		</#if>
	</div>
	</#if>
</#macro>



<#-- 页面按钮 -->
<#macro m_zapmacro_common_operate_button  e_operate  e_style_css>
	<#assign show_page_type = b_page.getReqMap()["show_page_type"] >
	<#-- <input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="${e_operate.getOperateLink()}"  value="${e_operate.getOperateName()}" /> -->
	<#if show_page_type == "edit">
		<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="add_dlq_info.btnSubmit()"  value="提交" />
	</#if>
	
</#macro>


<script type="text/javascript">
	add_dlq_info.init();
</script>

