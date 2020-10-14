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



<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>
<#assign e_pageField=e_pagedata.getPageField() />
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	        <#if e_pageField[e_list_index] != 'category_limit'
	        		&& e_pageField[e_list_index] != 'uc_seller_type_limit'
	        		&& e_pageField[e_list_index] != 'small_seller_limit'
	        		&& e_pageField[e_list_index] != 'sell_price_limit'
	        		&& e_pageField[e_list_index] != 'product_stock_limit'>
		      	 <th>
		      	 	${e_list}
		      	 </th>
		        <#if e_pageField[e_list_index] == 'creator'>	
		      	 <th>
		      	 	商品数量
		      	 </th>		        
	        	</#if>	      	 
		    </#if>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
		        <#if e_pageField[e_index] != 'category_limit'
		        		&& e_pageField[e_index] != 'uc_seller_type_limit'
		        		&& e_pageField[e_index] != 'small_seller_limit'
		        		&& e_pageField[e_index] != 'sell_price_limit'
		        		&& e_pageField[e_index] != 'product_stock_limit'>
					<#if e_pageField[e_index] == 'category_codes' >
						<td>
							<#if e_list[e_pageField?seq_index_of("category_limit")] == "无限制">
								无限制
							<#else>
								${m_page_helper("com.cmall.familyhas.pagehelper.CategoryNameHelper",e)}
							</#if>
						</td>	  		 
					<#elseif e_pageField[e_index] == 'uc_seller_type_codes' >
						<td>
							<#if e_list[e_pageField?seq_index_of("uc_seller_type_limit")] == "无限制">
								无限制
							<#else>
								${m_page_helper("com.cmall.familyhas.pagehelper.ScDefineNameHelper",'44974747',e)}
							</#if>					
						</td>
					<#elseif e_pageField[e_index] == 'small_seller_codes' >
						<td>
							<#if e_list[e_pageField?seq_index_of("small_seller_limit")] == "无限制">
								无限制
							<#else>
								${m_page_helper("com.cmall.familyhas.pagehelper.SmallSellerNameHelper",e)}
							</#if>						
						</td>
					<#elseif e_pageField[e_index] == 'sell_price_range' >
						<td>
							<#if e_list[e_pageField?seq_index_of("sell_price_limit")] == "无限制">
								无限制
							<#else>
								${e}
							</#if>							
						</td>	
					<#elseif e_pageField[e_index] == 'product_status_limit' >
						<td>
							<#if e == '' >
								无限制
							<#else>
								${m_page_helper("com.cmall.familyhas.pagehelper.ScDefineNameHelper",'449715390006',e)}
							</#if>					
						</td>
					<#elseif e_pageField[e_index] == 'product_stock_range' >
						<td>
							<#if e_list[e_pageField?seq_index_of("product_stock_limit")] == "无限制">
								无限制
							<#else>
								${e}
							</#if>						
						</td>
					<#elseif e_pageField[e_index] == 'creator' >
						<td>
						${e?default("")}
						</td>	
						<td>
						<a href="javascript:void(0)" onclick="showProductCount(this,'${e_list[e_pageField?seq_index_of("xp_code")]}')">查询商品数量</a>
						</td>											
					 <#else>
						<td>
						${e?default("")}
						</td>
					</#if>		        		
			    </#if>	  		 
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
</#macro>

<script>
function showProductCount(target,xpCode){
	var $target = $(target);
	if($target.attr('resultFlag')) {
		return;
	}
	
	zapjs.zw.modal_process();
	zapjs.f.ajaxjson('../func/bd94521dd86711e9abac005056165069', {xpCode: xpCode}, function(data){
		zapjs.f.modal_close();
		if(data.resultCode == 1) {
			$target.attr('resultFlag',true);
			$target.html(data.resultMessage);
			zapjs.f.message("查询成功，数量："+data.resultMessage);
		} else {
			zapjs.f.message(data.resultMessage);
		}
	});
}
</script>