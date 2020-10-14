	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire b_page />
	</div>
	<hr/>	
	<#assign e_pagedata=b_page.upChartData()>
	<div class="zw_page_common_data">
	<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	      	<th>销售员ID</th>
	      	<th>昵称</th>
	      	<th>姓名</th>
	      	<th>手机号</th>
	      	<th>加入时间</th>
	      	<th>销售额</th>
	      	<th>订单数</th>
	      	<th>性别</th>
	      	
	      	<th>分享有效商品数</th>
	      	<th>分享点击次数</th>
	      	<th>查看订单</th>
	    </tr>
  	</thead>
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
	  		 <#list e_list as e>
	  		 
	  		 <#if e_index = 8>
	  		 <td>
		<#assign  tempList=b_method.upDataBysql("lc_distribution_info","SELECT count(DISTINCT di.distribution_product_code) pro_num FROM logcenter.lc_distribution_info di WHERE di.distribution_member_id  = :member_code","member_code",e_list[0]) />
	      		${tempList[0]['pro_num']?default(0)}	
	      	</td>	 
				 	   
	     <#else>
				<td>
				 ${e?default("")}
				</td>
		</#if>
		</#list>
		<td>
	      	<#assign  tempList=b_method.upDataBysql("lc_distribution_info","select count(di.zid) pro_dj FROM logcenter.lc_distribution_info di WHERE di.distribution_member_id = :member_code","member_code",e_list[0]) />
	      		${tempList[0]['pro_dj']?default(0)}
	  	</td>  
	      <td>${e_list[8]?default("")}</td>
	      	</tr>
	 	</#list>
		</tbody>
</table>

	<@m_zapmacro_common_page_pagination b_page  e_pagedata />
	
	</div>
	