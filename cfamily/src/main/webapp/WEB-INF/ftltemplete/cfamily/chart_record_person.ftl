<#--活动设置页面-->


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
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	   <tr>
	      <#list e_pagedata.getPageHead() as e_list>
	        	<#if (e_list_index <=10)&&(e_list_index >= 0)>
		      	 <th>
		      	 	${e_list}
		      	 </th>
		      	 </#if>
	      </#list>
	        <th colspan="4" width="20%">	操作</th>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	  			 <#if (e_index <=10)&&(e_index >= 0)>
	      		<td>
		      			${e?default("")}
	      		</td>
	      		 </#if>
	      	</#list>
	      	
	      	<td colspan="4" style="vertical-align:middle; text-align:center;">
				    <#if (e_list[9]=='正在处理')>
				    	<#if (e_list[4]=='惠家有')>
				           <a class="btn btn-link" target="_blank" href="page_book_v_mc_record_person?zw_f_record_code=${e_list[0]}&zw_f_account_code=${e_list[2]}&zw_f_tel=${e_list[3]}&zw_f_seller_code=SI2003">查看追加内容</a>
				    	<#elseif (e_list[4]=='家有汇')> 
				    	   <a class="btn btn-link" target="_blank" href="page_book_v_mc_record_person?zw_f_record_code=${e_list[0]}&zw_f_account_code=${e_list[2]}&zw_f_tel=${e_list[3]}&zw_f_seller_code=SI2009">查看追加内容</a>
				    	<#elseif (e_list[4]=='微公社')> 
				    	   <a class="btn btn-link" target="_blank" href="page_book_v_mc_record_person?zw_f_record_code=${e_list[0]}&zw_f_account_code=${e_list[2]}&zw_f_tel=${e_list[3]}&zw_f_seller_code=SI2011">查看追加内容</a>
				    	</#if>
				    	
				    	 <#if (e_list[1]=='主记录')>
				    	 		 <#if (e_list[4]=='惠家有')>
				    	 			 <input class="btn btn-small" onclick="zapadmin.window_url('../page/page_add_v_mc_record_person_new?zw_f_record_code=${e_list[0]}&zw_f_account_code=${e_list[2]}&zw_f_tel=${e_list[3]}&zw_f_seller_code=SI2003') " type="button" value="追加记录">
						    	<#elseif (e_list[4]=='家有汇')> 
						    	    <input class="btn btn-small" onclick="zapadmin.window_url('../page/page_add_v_mc_record_person_new?zw_f_record_code=${e_list[0]}&zw_f_account_code=${e_list[2]}&zw_f_tel=${e_list[3]}&zw_f_seller_code=SI2009') " type="button" value="追加记录">
						    	<#elseif (e_list[4]=='微公社')> 
						    	    <input class="btn btn-small" onclick="zapadmin.window_url('../page/page_add_v_mc_record_person_new?zw_f_record_code=${e_list[0]}&zw_f_account_code=${e_list[2]}&zw_f_tel=${e_list[3]}&zw_f_seller_code=SI2011') " type="button" value="追加记录">
						    	</#if> 
				         </#if> 
				           <input type="button" value="已处理" onclick="zapjs.zw.func_do(this, null, {zw_f_record_code:'${e_list[0]}'});" class="btn btn-small" zapweb_attr_operate_id="d7fb131e44f24c50aaf210dc3bc32060">
				   <#elseif (e_list[9]=='处理完成')>  
				          <#if (e_list[4]=='惠家有')>
				           <a class="btn btn-link" target="_blank" href="page_book_v_mc_record_person?zw_f_record_code=${e_list[0]}&zw_f_account_code=${e_list[2]}&zw_f_tel=${e_list[3]}&zw_f_seller_code=SI2003">查看追加内容</a>
				    	<#elseif (e_list[4]=='家有汇')> 
				    	   <a class="btn btn-link" target="_blank" href="page_book_v_mc_record_person?zw_f_record_code=${e_list[0]}&zw_f_account_code=${e_list[2]}&zw_f_tel=${e_list[3]}&zw_f_seller_code=SI2009">查看追加内容</a>
				    	<#elseif (e_list[4]=='微公社')> 
				    	   <a class="btn btn-link" target="_blank" href="page_book_v_mc_record_person?zw_f_record_code=${e_list[0]}&zw_f_account_code=${e_list[2]}&zw_f_tel=${e_list[3]}&zw_f_seller_code=SI2011">查看追加内容</a>
				    	</#if>       
				          <a class="btn btn-link">无</a>
				   
				</#if>
				
			</td>
	      	</tr>
	 	</#list>
	</tbody>
</table>
</#macro>
	 