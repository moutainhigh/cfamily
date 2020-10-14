	<div class="zw_page_common_inquire">
	<form class="form-horizontal" method="POST" >
		<@m_zapmacro_common_auto_inquire b_page />
		
		
		
		<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate() "116001009" />
	</form>
		

		
		
	</div>
	<hr/>
	
	<#assign e_pagedata=b_page.upChartData()>
	<div class="zw_page_common_data">
	 
<table class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	        	<#if (e_list_index <=8)&&(e_list_index > 1)>
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
	  		 <#if (e_index <=8)&&(e_index > 1)>
	      		<td>
	      			${e?default("")}
	      		</td>
	      		 </#if>
	      	</#list>
	      		
	      	
	      	<th colspan="4">
				<a href="page_edit_v_oc_activity_flashsales?zw_f_uid=${e_list[0]}" class="btn btn-link">修改</a> 
				<a href="page_add_v_oc_flashsales_skuInfo?zw_f_uid=${e_list[0]} " class="btn btn-link">添加商品</a>
				<a href="page_add_v_oc_flashsales_skuInfoo?zw_f_uid=${e_list[0]} " class="btn btn-link">添加SKU</a>
				
				<a href="page_book_v_oc_activity_flashsales?zw_f_uid=${e_list[0]} " class="btn btn-link" target="_blank">查看</a> 
				 
				 <#if (e_list[1]=='449746740001')>
				 
				<input zapweb_attr_operate_id="dceb12adfc0e42aaa6aebdbc86f5f6ed" class="btn btn-link" onclick="zapjs.zw.func_tip(this,'${e_list[0]}','发布')" type="button" value="发布">
				</#if>
				 <#if (e_list[1]='449746740002')>
				<input zapweb_attr_operate_id="e518975c537d43168ab749b716bcb494" class="btn btn-link" onclick="zapjs.zw.func_tip(this,'${e_list[0]}','取消发布') " type="button" value="取消">
				</#if>
			</th>
	      		
	      	</tr>
	 	</#list>
		</tbody>
</table>
	 
	<@m_zapmacro_common_page_pagination b_page  e_pagedata />
	
	</div>
	
