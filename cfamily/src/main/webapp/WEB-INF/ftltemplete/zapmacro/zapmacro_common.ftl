<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upAddData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>


<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<form class="form-horizontal" method="POST" >

	
	<#list e_page.upBookData()  as e>
		
	  	<@m_zapmacro_common_book_field e e_page/>
	  	
	</#list>
	
	
</form>
</#macro>




<#-- 修改页 -->
<#macro m_zapmacro_common_page_edit e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upEditData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>




<#-- 异步加载列表页 -->
<#macro m_zapmacro_common_ajax_chart e_page>

	<div class="zw_page_common_ajax_inquire">
		<@m_zapmacro_common_page_inquire e_page />
	</div>
	
	

</#macro>







<#-- 列表页 -->
<#macro m_zapmacro_common_page_chart e_page> 

	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
	</div>
	<hr/>
	
	<#local e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
	<@m_zapmacro_common_table e_pagedata />
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>
</#macro>

<#-- 查询区域 -->
<#macro m_zapmacro_common_page_inquire e_page>
	<form class="form-horizontal" method="POST" >
		<@m_zapmacro_common_auto_inquire e_page />
		<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate() "116001009" />
	</form>
</#macro>



<#--查询的自动输出判断 -->
<#macro m_zapmacro_common_auto_inquire e_page>
	<#list e_page.upInquireData() as e>
	
		<#if e.getQueryTypeAid()=="104009002">
			<@m_zapmacro_common_field_between e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009020">
			<@m_zapmacro_common_field_betweensfm e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009001">
			<#-- url专用  不显示 -->

	  	<#elseif  e.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e  e_page "请选择"/>
	  	<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
	  		
	  	</#if>
	  	
	</#list>
	<#--兼容form中input如果只有一个自动提交的情况-->
	<input style="display:none;"/>
</#macro>




<#-- 分页 -->
<#macro m_zapmacro_common_page_pagination e_page  e_pagedata>

<div class="pagination">

	<#if (e_pagedata.getPageSize()>0)>
  <ul>
	<#if (e_pagedata.getPageIndex()>1)>
		<li><a href="<@m_zapmacro_common_page_pagination_href  e_page  e_pagedata   e_pagedata.getPageIndex()-1/>">上一页</a></li>
	<#else>
		<li class="disabled"><a>上一页</a></li>
	</#if>

    <#local max=(e_pagedata.getPageIndex()+5)>
    <#if (max>e_pagedata.getPageMax())><#local max=e_pagedata.getPageMax()></#if>
    
    <#local min=(e_pagedata.getPageIndex()-(5+e_pagedata.getPageIndex()-max))>
    <#if (min<1)><#local min=1></#if>
    
	<#list min..max as e>
		<#if (e>0)>
		<li <#if e==e_pagedata.getPageIndex()> class="active"</#if> ><a href="<@m_zapmacro_common_page_pagination_href  e_page  e_pagedata   e/>">${e}</a></li>
		</#if>
	</#list>

    <#if (e_pagedata.getPageIndex()<e_pagedata.getPageMax())>
		<li><a href="<@m_zapmacro_common_page_pagination_href  e_page  e_pagedata   e_pagedata.getPageIndex()+1/>">下一页</a></li>
	<#else>
		<li class="disabled"><a>下一页</a></li>
	</#if>
  </ul>
  </#if>
  
  
	<span class="pagination_label">共${e_pagedata.getPageCount()}条</span>
</div>

</#macro>

<#macro m_zapmacro_common_page_pagination_href  e_page  e_pagedata  e_pageindex>
${e_page.upReplaceUrl("",[(e_page.upConst("126022016","count="))+(e_pagedata.getPageCount()),(e_page.upConst("126022016","index="))+(e_pageindex)])}
</#macro>



<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	      		<td>
	      			${e?default("")}
	      		</td>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
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
	  		<@m_zapmacro_common_field_select  e_field  e_page ""/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005103">
	  		<@m_zapmacro_common_field_checkbox  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005020">
	  		<@m_zapmacro_common_field_textarea  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005005">
	  		<@m_zapmacro_common_field_editor  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005021">
	  		<@m_zapmacro_common_field_upload  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005023">
	  		<@m_zapmacro_common_field_upload_upgrade  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005024">
	  		<@m_zapmacro_common_field_upload_modify  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005025">
	  		<@m_zapmacro_common_field_upload_appendix  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>



<#-- 显示页的自动输出判断 -->
<#macro m_zapmacro_common_book_field e_field   e_page>
	
	
	  		<@m_zapmacro_common_field_show e_field e_page/>
	  	
</#macro>




<#macro m_zapmacro_common_field_start text="" for="">

<div class="control-group">
	<label class="control-label" for="${for}">${text}</label>
	<div class="controls">

</#macro>
<#macro m_zapmacro_common_field_end>
	</div>
</div>
</#macro>


<#-- 字段：显示专用 -->
<#macro m_zapmacro_common_field_show e_field e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote()+":" />
	      		<div class="control_book">
		      		<#if  e_field.getFieldTypeAid()=="104005019">
		      			<#list e_page.upDataSource(e_field) as e_key>
							<#if  e_field.getPageFieldValue()==e_key.getV()> ${e_key.getK()} </#if>
						</#list>
					<#elseif e_field.getFieldTypeAid()=="104005021">
						<#if  e_field.getPageFieldValue()!="">
							<div class="w_left w_w_100">
									<a href="${e_field.getPageFieldValue()}" target="_blank">
									<img src="${e_field.getPageFieldValue()}">
									</a>
							</div> 
						</#if>
		      		<#else>
		      		${e_field.getPageFieldValue()?default("")}
		      		</#if>
	      		</div>
	<@m_zapmacro_common_field_end />
</#macro>




<#-- 字段：隐藏域 -->
<#macro m_zapmacro_common_field_hidden e_field>
	<input type="hidden" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}" />
</#macro>


<#-- 字段：组件框 -->
<#macro m_zapmacro_common_field_component e_field e_page>  	
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		${e_field.getPageFieldValue()?default("")}
	<@m_zapmacro_common_field_end />
</#macro>


<#-- 字段：纯展示 -->
<#macro m_zapmacro_common_field_span e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		${e_field.getPageFieldValue()?default("")}
	<@m_zapmacro_common_field_end />
</#macro>



<#-- 字段：日期 -->
<#macro m_zapmacro_common_field_date e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="text"  autocomplete="off" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
	<@m_zapmacro_common_field_end />
	  
	  
	<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});" />
	  
</#macro>
<#-- 字段：日期 时分秒-->
<#macro m_zapmacro_common_field_datesfm e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#-- onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})"  -->
		<#-- onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',realDateFmt:'yyyy-MM-dd HH-mm-ss',realTimeFmt:'HH:mm:ss HH-mm-ss'})"   -->
		<input type="text"  autocomplete="off" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',realDateFmt:'yyyy-MM-dd HH-mm-ss',realTimeFmt:'HH:mm:ss HH-mm-ss'})" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
	<@m_zapmacro_common_field_end />
<#-- zapjs.f.require(['lib/datepicker/dateTime'],function(a){})-->
<#-- zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});-->
	<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){})" />
	  
</#macro>
<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
	<@m_zapmacro_common_field_end />
</#macro>








<#-- 字段：长文本框 -->
<#macro m_zapmacro_common_field_textarea e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<textarea id="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} name="${e_field.getPageFieldName()}">${e_field.getPageFieldValue()}</textarea>
	<@m_zapmacro_common_field_end />
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
	  
	  
	  
	  
</#macro>


<#-- 字段：上传  展示时间-->
<#macro m_zapmacro_common_field_upload_upgrade e_field e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="hidden" zapweb_attr_target_url="${e_page.upConfig("zapweb.upload_upgrade_target")}" zapweb_attr_set_params="${e_field.getSourceParam()}"    id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
		<span class="control-upload_iframe"></span>
		<span class="control-upload_process"></span>
		<span class="control-upload"></span>
	<@m_zapmacro_common_field_end />
	
	<@m_zapmacro_common_html_script "zapjs.f.ready(function(){zapjs.f.require(['zapweb/js/zapweb_upload_upgrade'],function(a){a.upload_file('"+e_field.getPageFieldName()+"','"+e_page.upConfig("zapweb.upload_upgrade_target")+"');}); });" />
	  
	  
	  
	  
</#macro>

<#-- 字段：上传 增加修改按钮-->
<#macro m_zapmacro_common_field_upload_modify e_field e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="hidden" zapweb_attr_target_url="${e_page.upConfig("zapweb.upload_modify_target")}" zapweb_attr_set_params="${e_field.getSourceParam()}"    id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
		<span class="control-upload_iframe"></span>
		<span class="control-upload_process"></span>
		<span class="control-upload"></span>
	<@m_zapmacro_common_field_end />
	
	<@m_zapmacro_common_html_script "zapjs.f.ready(function(){zapjs.f.require(['zapweb/js/zapweb_upload_modify'],function(a){a.upload_file('"+e_field.getPageFieldName()+"','"+e_page.upConfig("zapweb.upload_modify_target")+"');}); });" />
	  
  
	  
</#macro>

<#-- 字段：上传附件 回显带附件名称(上传数<=3 个)-->
<#macro m_zapmacro_common_field_upload_appendix e_field e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="hidden" zapweb_attr_target_url="${e_page.upConfig("zapweb.upload_appendix_target")}" zapweb_attr_set_params="${e_field.getSourceParam()}"    id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
		<span class="control-upload_iframe"></span>
		<span class="control-upload_process"></span>
		<span class="control-upload"></span>
	<@m_zapmacro_common_field_end />
	
	<@m_zapmacro_common_html_script "zapjs.f.ready(function(){zapjs.f.require(['zapweb/js/zapweb_upload_appendix'],function(a){a.upload_file('"+e_field.getPageFieldName()+"','"+e_page.upConfig("zapweb.upload_appendix_target")+"');}); });" />
	  
  
	  
</#macro>



<#-- 字段：编辑框 -->
<#macro m_zapmacro_common_field_editor e_field  e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<textarea class="w_none " id="${e_field.getPageFieldName()}"  name="${e_field.getPageFieldName()}">${e_field.getPageFieldValue()}</textarea>
	<@m_zapmacro_common_field_end />
	  
	<@m_zapmacro_common_html_script "zapjs.f.ready(function(){zapjs.zw.editor_show('"+e_field.getPageFieldName()+"','"+e_page.upConfig("zapweb.upload_target")+"')});" />
	  
</#macro>





<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_select   e_field    e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
	      					<option value="">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSource(e_field) as e_key>

						<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
					</#list>
	      		</select>
	<@m_zapmacro_common_field_end />
</#macro>


<#-- 字段：复选框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_checkbox   e_field    e_page    >
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	
				<#list e_page.upDataSource(e_field) as e_key>
					<input type="checkbox" value="${e_key.getV()}" <#if  (((e_field.getPageFieldValue()+",")?index_of(e_key.getV()+","))>-1)> checked="checked" </#if> name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}_${e_key_index}"/>
					<label for="${e_field.getPageFieldName()}_${e_key_index}">${e_key.getK()}</label>
						
					</#list>
	
	      		
	<@m_zapmacro_common_field_end />
</#macro>








<#-- 字段：文本范围 -->
<#macro m_zapmacro_common_field_between e_field  e_page >

	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_page.upConst("126022001",e_field.getPageFieldName(),"between_from") />

	    		从
	      		<input type="text" <#if e_field.getFieldTypeAid()=="104005004">  onClick="WdatePicker({maxDate:'#F{$dp.$D(\'${e_page.upConst("126022001",e_field.getPageFieldName(),"between_to")}\',{d:-1});}'})"  </#if>   id="${e_page.upConst("126022001",e_field.getPageFieldName(),"between_from")}" name="${e_page.upConst("126022001",e_field.getPageFieldName(),"between_from")}" value="${e_page.upReqValue(e_page.upConst("126022001",e_field.getPageFieldName(),"between_from"))?default("")}">
	      		到
	      		<input type="text" <#if e_field.getFieldTypeAid()=="104005004">  onClick="WdatePicker({minDate:'#F{$dp.$D(\'${e_page.upConst("126022001",e_field.getPageFieldName(),"between_from")}\',{d:1});}'})"</#if>  id="${e_page.upConst("126022001",e_field.getPageFieldName(),"between_to")}" name="${e_page.upConst("126022001",e_field.getPageFieldName(),"between_to")}" value="${e_page.upReqValue(e_page.upConst("126022001",e_field.getPageFieldName(),"between_to"))?default("")}">
	      		
	      		
	<@m_zapmacro_common_field_end />
	  
	<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});" />
	  
</#macro>
<#-- 字段：文本范围时分秒 -->
<#macro m_zapmacro_common_field_betweensfm e_field  e_page >
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_page.upConst("126022001",e_field.getPageFieldName(),"between_from") />
	    		从
	      		<input type="text" autocomplete="off" <#if e_field.getFieldTypeAid()=="104005022">  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',realDateFmt:'yyyy-MM-dd HH-mm-ss',realTimeFmt:'HH:mm:ss HH-mm-ss',maxDate:'#F{$dp.$D(\'${e_page.upConst("126022001",e_field.getPageFieldName(),"between_to")}\',{d:-1});}'})"  </#if>   id="${e_page.upConst("126022001",e_field.getPageFieldName(),"between_from")}" name="${e_page.upConst("126022001",e_field.getPageFieldName(),"between_from")}" value="${e_page.upReqValue(e_page.upConst("126022001",e_field.getPageFieldName(),"between_from"))?default("")}">
	      		到
	      		<input type="text" autocomplete="off" <#if e_field.getFieldTypeAid()=="104005022">  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',realDateFmt:'yyyy-MM-dd HH-mm-ss',realTimeFmt:'HH:mm:ss HH-mm-ss',minDate:'#F{$dp.$D(\'${e_page.upConst("126022001",e_field.getPageFieldName(),"between_from")}\',{d:1});}'})"</#if>  id="${e_page.upConst("126022001",e_field.getPageFieldName(),"between_to")}" name="${e_page.upConst("126022001",e_field.getPageFieldName(),"between_to")}" value="${e_page.upReqValue(e_page.upConst("126022001",e_field.getPageFieldName(),"between_to"))?default("")}">
	      		
	      		
	<@m_zapmacro_common_field_end />
	  
	<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});" />
	  
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


<#-- 工具栏按钮 -->
<#macro m_zapmacro_common_set_operate     e_list_operates  e_area_type  e_style_css >
			<#list e_list_operates as e>
    			<#if e.getAreaTypeAid()==e_area_type>

    		<a class="${e_style_css}" zapweb_attr_operate_id="${e.getOperateUid()}"  
    			<#if e.getOperateTypeAid()=="116015010"> 
    				 href="javascript:" onclick="${e.getOperateLink()}" 
    			<#else> 
    				 href="${e.getOperateLink()}" 
    			</#if> 
    			<#if e.getOperateTypeAid()=="116015012">
    				 target="_blank"
    			</#if>
    		>
    		<#if e.getOperateName()=="添加"><i class="icon-pencil"></i></#if>
    		${e.getOperateName()}</a>&nbsp;&nbsp;
    			</#if>
    		</#list>
</#macro>


<#-- 页面按钮 -->
<#macro m_zapmacro_common_operate_button  e_operate  e_style_css>
	
	<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="${e_operate.getOperateLink()}"  value="${e_operate.getOperateName()}" />
</#macro>

<#-- 页面按钮 -->
<#macro m_zapmacro_common_operate_link  e_operate  e_style_css>
	<a class="${e_style_css}" href="${e_operate.getOperateLink()}" >${e_operate.getOperateName()}</a>
</#macro>



<#-- 转换map输出 -->
<#macro m_zapmacro_common_format_map_hidden  e_map e_key_left="">

<#list e_map?keys as e_key>
<input type="hidden" name="${e_key_left}${e_key}" id="${e_key_left}${e_key}" value="${e_map[e_key]}"    />
</#list>

</#macro>


<#macro m_zapmacro_common_html_script  e_info >

	<script type="text/javascript">
		${e_info}
	</script>

</#macro>

<#-- 定义函数  计算pagehelper结果并返回值-->
<#-- 需实现接口 com.srnpr.zapweb.helper.PageHelper -->
<#function m_page_helper cls_name args...>
	<#return b_method.upClass(cls_name).upData(args)/>
</#function>















