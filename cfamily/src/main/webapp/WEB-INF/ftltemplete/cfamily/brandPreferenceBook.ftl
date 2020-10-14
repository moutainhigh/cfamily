<@m_common_html_script "require(['cfamily/js/brandProduct']);" />
<div class="zab_info_page">
	<div class="zab_info_page_title  w_clear">
		<span>维护专题内容</span>
	</div>
	<@m_zapmacro_common_page_book b_page />
</div>

<#assign infoCode = b_page.getReqMap()["zw_f_info_code"] >
<input id="infoCode" type="hidden" value="${infoCode}">
<div class="form-horizontal control-group">
	<a href="../page/page_add_v_fh_brand_cotent_preference?zw_f_info_code=${infoCode}" 

class="btn btn-link"  target="_blank">
		<input class="btn btn-success" type="button" value="新增广告"/>
	</a>
</div>

<div class="zab_info_page_title  w_clear">
<span>广告图列表</span>&nbsp;&nbsp;&nbsp;
</div>


<#assign logData=b_method.upControlPage("page_chart_v_fh_brand_cotent_preference","zw_f_info_code=${infoCode}&zw_p_size=-1").upChartData()>
<#assign  e_pagedata=logData />
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
				<#assign code=e_list[3]>
		  		 <#list e_list as e>
		  		 	<#if e_index = 0>
		  		 		<#if e?default("") != "">
	      					<td>
		  						<a target="_blank" href="${e?default("")}">
		      						<img style="width:100px;height:100px;max-width:100px;display:block;float:left;" src="${e?default("")}"> 
		      					</a>
			      			</td>
			      		<#else>
				      		<td>
				  				${e?default("")}
				      		</td>
	      				</#if>
	      			<#elseif e_index = 1>
	      				<#if e_list[1]?default("") == "1">
	      					<td>
	      						头部
				      		</td>
	      				<#elseif e_list[1]?default("") == "2">
	      					<td>
	      						尾部
				      		</td>
	      				</#if>
		  		 	<#elseif e_index = 3>
		  		 		<#if e_list[2]?default("") == "分类搜索">
		      				<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
		      				<#assign categoryMap=sellercategoryService.getCateGoryShow(code,"SI2003")>
		      				<td>
		      					${categoryMap.categoryName?default("")}
		      				</td>
		      			<#elseif e_list[2]?default("") == "商品详情">
		      				<#assign productService=b_method.upClass("com.cmall.productcenter.service.ProductService")>
		      				<#assign product=productService.getProductInfoForMabyLoveChart(code)>
		      				<td>
			      				商品编号：${e?default("")}<br/>
			      				商品名称：${product.productName?default("")}
		      				</td>
		      			<#else>
		      				<td>
				  				${e?default("")}
				      		</td>
		      			</#if>
		  		 	<#else>
		  		 		<td>
			  				${e?default("")}
			      		</td>
		  		 	</#if>
		      	</#list>
	      	</tr>
	 	</#list>
	</tbody>
</table>

<div class="form-horizontal control-group">
	<input class="btn btn-success" type="button" value="添加商品" onclick="brandProduct.show_windows()"/>
	<input class="btn btn-success" type="button" value="导入商品" onclick="brandProduct.show_import_windows()"/>
</div>

<div class="zab_info_page_title  w_clear">
<span>商品列表</span>&nbsp;&nbsp;&nbsp;
</div>


<#assign logData=b_method.upControlPage("page_chart_v_v_fh_brand_rel_product","zw_f_info_code=${infoCode}&zw_p_size=-1").upChartData()>
<#assign  e_pagedata=logData />
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	        <#if (e_list_index > 0) >
		       	  <th>
		      	 	${e_list}
		      	 </th>
	      	 </#if>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
		  		<#list e_list as e>
			  		<#if (e_index > 0) >
				  		<#if e_index = 6>
		      				<#if e_list[6]?default("") == "1">
		      					<td>
		      						可用
					      		</td>
		      				<#elseif e_list[6]?default("") == "2">
		      					<td>
		      						作废
					      		</td>
		      				</#if>
				      	<#elseif e_index=8>
		  			    	<#if e_list[6]?default("") == "1">
		  			    		<td>
			  			    		<input class="btn btn-small" type="button" value="作废" onclick="zapjs.zw.func_do(this, null, { zw_f_uid : '${e_list[0]}'});" zapweb_attr_operate_id="059dc9cbb3764e468de99bdfc368340b">
		  			    		</td>
			      			<#else>
			      				<td>
				      				<input class="btn btn-small" type="button" value="启用" onclick="zapjs.zw.func_do(this, null, { zw_f_uid : '${e_list[0]}'});" zapweb_attr_operate_id="059dc9cbb3764e468de99bdfc368340b">
		  			    		</td>
			      			</#if>
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
