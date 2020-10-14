<#assign member_code = b_page.getReqMap()["zw_f_member_code"] >

<div class="zab_info_page_title  w_clear">
<span>基本信息</span>&nbsp;&nbsp;&nbsp;
</div>
<#assign logData=b_method.upControlPage("page_chart_v_v_fh_agent_memberinfo","zw_f_member_code=${member_code}&zw_p_size=-1").upChartData()>

<#assign  e_pageField=logData.getPageField() />
<#assign  e_pagedata=logData />

<table  class="table  table-condensed table-striped table-bordered table-hover" id="table11">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		      	 <#if (e_list_index < 9)>
		      	 <th style="text-align:center;">
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
  				<#if (e_index < 9)>
  				<td> 
  					${e?default("")}
	      		</td>
	      		</#if>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>



<div class="zab_info_page_title  w_clear">
<span>分销收益</span>&nbsp;&nbsp;&nbsp;
</div>
<table  class="table  table-condensed table-striped table-bordered table-hover" id="table11">
	<thead>
	    <tr>
	      	<th>分销总收入</th>
	      	<th>分享有效商品数</th>
	      	<th>分享点击次数</th>
	      	<th>分销订单数</th>
	      	<th>销售额</th>
	      	<th>订单成功数</th>
	      	<th>订单成功金额</th>
	      	<th>订单取消数</th>
	      	<th>订单取消金额</th>
	    </tr>
  	</thead>
  	<tbody>
			<#assign  tempList=b_method.upDataBysql("fh_agent_member_info","SELECT mi.member_code,format((SELECT sum(CASE WHEN `ap`.`profit_source` = '4497484600040001' THEN `ap`.`profit` ELSE 0 END) AS `agent_money` FROM `familyhas`.`fh_agent_profit_detail` `ap`"+
" WHERE `ap`.`member_code` = `mi`.`member_code`),2) AS `agent_money`,(SELECT count(DISTINCT di.distribution_product_code) pro_num FROM logcenter.lc_distribution_info di WHERE di.distribution_member_id = mi.member_code) as pro_num,"+
" (select count(di.zid) FROM logcenter.lc_distribution_info di WHERE di.distribution_member_id = mi.member_code) as pro_dj,sum(CASE WHEN oi.order_code is not null THEN 1 ELSE 0 END) AS `order_num`,"+
" format(sum(CASE WHEN oi.order_code is not null THEN `oo`.`sell_price` * `oo`.`sku_num` ELSE 0 END),2) AS `order_value`,"+
" sum(CASE WHEN `oi`.`order_status` = '4497153900010005' THEN 1 ELSE 0 END) AS `order_success_num`,format(sum(CASE WHEN `oi`.`order_status` = '4497153900010005' THEN `oo`.`sell_price` * `oo`.`sku_num` ELSE 0 END),2) AS `order_success_value`,"+
" sum(CASE WHEN `oi`.`order_status` = '4497153900010006' THEN 1 ELSE 0 END) AS `order_fail_num`,"+
" format(sum(CASE WHEN `oi`.`order_status` = '4497153900010006' THEN `oo`.`sell_price` * `oo`.`sku_num` ELSE 0 END),2) AS `order_fail_value`"+
" FROM fh_agent_member_info mi left join fh_agent_order_detail od on od.agent_code = mi.member_code left join ordercenter.oc_orderdetail oo on oo.order_code = od.order_code"+
" left join ordercenter.oc_orderinfo oi on oi.order_code = oo.order_code WHERE	mi.member_code = :member_code","member_code","${member_code}") />			
			<tr>
	      		<td>${tempList[0]['agent_money']?default(0)}</td>
	      		<td>${tempList[0]['pro_num']?default(0)}</td>
	      		<td>${tempList[0]['pro_dj']?default(0)}</td>
	      		<td>${tempList[0]['order_num']?default(0)}</td>
	      		<td>${tempList[0]['order_value']?default(0)}</td>
	      		<td>${tempList[0]['order_success_num']?default(0)}</td>
	      		<td>${tempList[0]['order_success_value']?default(0)}</td>
	      		<td>${tempList[0]['order_fail_num']?default(0)}</td>
	      		<td>${tempList[0]['order_fail_value']?default(0)}</td>
	      	</tr>
		</tbody>
</table>

<div class="zab_info_page_title  w_clear">
<span>粉丝收益</span>&nbsp;&nbsp;&nbsp;
</div>
<table  class="table  table-condensed table-striped table-bordered table-hover" id="table11">
	<thead>
	    <tr>
	      	<th>粉丝手机号</th>
	      	<th>粉丝昵称</th>
	      	<th>收益</th>
	      	<th>订单数</th>
	      	<th>销售额</th>
	      	<th>订单成功数</th>
	      	<th>订单成功金额</th>
	      	<th>订单取消数</th>
	      	<th>订单取消金额</th>
	      	<th>上级收益</th>
	    </tr>
  	</thead>
  	
	<tbody>
		<#assign  ttList=b_method.upDataBysql("fh_agent_member_info","SELECT mi.member_code,li.login_name,li.nickname,format((SELECT sum(CASE WHEN `ap`.`profit_source` = '4497484600040001' THEN `ap`.`profit` ELSE 0 END) AS `agent_money` FROM `familyhas`.`fh_agent_profit_detail` `ap`"+
" WHERE `ap`.`member_code` = `mi`.`member_code`),2) AS `agent_money`,format(round(sum(CASE WHEN pd.profit_source = '4497484600040002' THEN pd.profit ELSE 0 END),1),2) as fssy,"+
" sum(CASE WHEN oi.order_code is not null THEN 1 ELSE 0 END) AS `order_num`,"+
" format(sum(CASE WHEN oi.order_code is not null THEN `oo`.`sell_price` * `oo`.`sku_num` ELSE 0 END),2) AS `order_value`,"+
" sum(CASE WHEN `oi`.`order_status` = '4497153900010005' THEN 1 ELSE 0 END) AS `order_success_num`,"+
" format(sum(CASE WHEN `oi`.`order_status` = '4497153900010005' THEN `oo`.`sell_price` * `oo`.`sku_num` ELSE 0 END),2) AS `order_success_value`,"+
" sum(CASE WHEN `oi`.`order_status` = '4497153900010006' THEN 1 ELSE 0 END) AS `order_fail_num`,"+
" format(sum(CASE WHEN `oi`.`order_status` = '4497153900010006' THEN `oo`.`sell_price` * `oo`.`sku_num` ELSE 0 END),2) AS `order_fail_value`"+
" FROM fh_agent_member_info mi LEFT JOIN membercenter.mc_member_sync li ON li.member_code = mi.member_code"+
" left join fh_agent_order_detail od on od.agent_code = mi.member_code"+
" left join ordercenter.oc_orderdetail oo on oo.order_code = od.order_code"+
" left join ordercenter.oc_orderinfo oi on oi.order_code = oo.order_code"+
" left join (SELECT order_code,profit_source,SUM(profit) profit from fh_agent_profit_detail where profit_source = '4497484600040002' GROUP BY order_code,profit_source) pd ON pd.order_code = oi.order_code"+
" WHERE	mi.member_code in (SELECT member_code from fh_agent_member_info WHERE parent_code = :member_code)  GROUP BY mi.member_code","member_code","${member_code}") />
		<#list ttList as e_list>
			<tr>
	  		 	<td>${e_list['login_name']?default('')}</td>
	      		<td>${e_list['nickname']?default('')}</td>
	      		<td>${e_list['agent_money']?default(0)}</td>
	      		<td>${e_list['order_num']?default(0)}</td>
	      		<td>${e_list['order_value']?default(0)}</td>
	      		<td>${e_list['order_success_num']?default(0)}</td>
	      		<td>${e_list['order_success_value']?default(0)}</td>
	      		<td>${e_list['order_fail_num']?default(0)}</td>
	      		<td>${e_list['order_fail_value']?default(0)}</td>
	      		<td>${e_list['fssy']?default(0)}</td>
	      	</tr>
	 	</#list>
		</tbody>
</table>