<@m_zapmacro_common_page_chart b_page />
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



<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	       <#list e_pagedata.getPageHead() as e_list>
		       <#if (e_list_index >0)>
			      	 <th>
			      	 	${e_list}
			      	 </th>
			   </#if>
	      </#list>
	      <th>操作</th>
	      
	    </tr>
  	</thead>
  	
  	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
				<#list e_list as e>
		 			<#if (e_index >0)>
					  	  <#if e_index = 9>
					  	  		<#if e_list[8] == '等待合作' || e_list[8] == '合作中'>
					  				<td style="text-align: center;">
					  					${e?default("")}
									</td>
					  			<#else>
					  				<td>
									</td>
					  			</#if>
					  	  <#else>
					      		<td style="text-align: center;">
					      			${e?default("")}
					      		</td>
					      </#if>
			      	</#if>
		  		</#list>
		  		
		  		<td style="text-align: center;">
			  		<#if e_list[8] == '等待合作'>
			  			<input class="btn btn-small" type="button" value="确认合作" onclick="zapjs.zw.func_tip(this,'${e_list[0]}'+',4497471600560002','合作') " zapweb_attr_operate_id="ef32ecd5d67b4271ae02ae209e939d14">
			  		<#elseif e_list[8] == '合作中'>
			  			<input class="btn btn-small" type="button" value="冻结" onclick="zapjs.zw.func_tip(this,'${e_list[0]}'+',4497471600560004','冻结') " zapweb_attr_operate_id="ef32ecd5d67b4271ae02ae209e939d14">		  				
			  		<#elseif e_list[8] == '已冻结'>
			  			<input class="btn btn-small" type="button" value="解冻" onclick="zapjs.zw.func_tip(this,'${e_list[0]}'+',4497471600560002','解冻') " zapweb_attr_operate_id="ef32ecd5d67b4271ae02ae209e939d14">
			  			<input class="btn btn-small" type="button" value="取消合作" onclick="zapjs.zw.func_tip(this,'${e_list[0]}'+',4497471600560005','取消合作') " zapweb_attr_operate_id="ef32ecd5d67b4271ae02ae209e939d14">
			  		<#elseif e_list[8] == '终止合作'>
			  			<input class="btn btn-small" type="button" value="开启合作" onclick="zapjs.zw.func_tip(this,'${e_list[0]}'+',4497471600560001','开启合作') " zapweb_attr_operate_id="ef32ecd5d67b4271ae02ae209e939d14">
			  		<#else>
					  	
			  		</#if>
		  		</td>
	  		</tr>
	 	</#list>
	</tbody>
</table>
</#macro>