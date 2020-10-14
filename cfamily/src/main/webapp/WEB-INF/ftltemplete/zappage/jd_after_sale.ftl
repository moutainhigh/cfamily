<#-- 京东售后信息  -->
<#macro m_zapmacro_jd_after_sale afterSaleCode >

<@m_common_html_script "require(['cfamily/js/jdAfterSale'],function(a){});" />
<#assign hjyAfterSale = b_method.upDataOne("oc_order_after_sale","","","","asale_code",afterSaleCode) />

<div class="cmb_cmanage_page_title  w_clear">
<span>京东售后详细信息</span>
</div>
<table class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	    <th>京东订单号</th>
	    <th>申请时间</th>
	    <th>预期退还方式</th>
	    <th>服务单号</th>
	    <th>状态</th>
	    <th>处理结果</th>
	    <#if hjyAfterSale["asale_type"] == "4497477800030003">
	    <th>换货新订单</th>
	    </#if>
	    <th>审核意见</th>
	    <th>售后地址</th>
	    <th>异常信息</th>
	    <th>更新时间</th>
	    <th>操作</th>
	    </tr>
  	</thead>
  
	<tbody>
		<#if (b_method.upDataOne("oc_order_jd_after_sale","","","","asale_code",afterSaleCode))??>
			<#assign jdAfterSale = b_method.upDataOne("oc_order_jd_after_sale","","","","asale_code",afterSaleCode) />
			<tr>
				<td>${jdAfterSale.jd_order_id}</td>
				<td>${jdAfterSale.afs_apply_time}</td>
				<td>
				<#if jdAfterSale.pickware_type == '4'>
					上门取件
				</#if>
				<#if jdAfterSale.pickware_type == '40'>
					客户发货
				</#if>
				</td>
				<td>${jdAfterSale.afs_service_id}</td>
				<td>${jdAfterSale.afs_service_step_name}</td>
				<td>${jdAfterSale.approved_result_name}</td>
			    <#if hjyAfterSale["asale_type"] == "4497477800030003">
			    <td>${jdAfterSale.afs_jd_order_id}</td>
			    </#if>				
				<td>${jdAfterSale.approve_notes}</td>
				<td>${jdAfterSale.afs_address} <br>${jdAfterSale.afs_tel} <br>${jdAfterSale.afs_link_man} <br>${jdAfterSale.afs_post_code}</td>
				<td>${jdAfterSale.rsync_message}</td>
				<td>${jdAfterSale.update_time}</td>
				<td>
				<#if jdAfterSale.allow_operations?contains("2") && jdAfterSale.rsync_flag != '2'>
					<#-- 包含2代表允许填写或者修改客户发货信息  -->
					<input type="button" class="btn  btn-success" zapweb_attr_operate_id="e065e7402d4b499b956a37150f3ec9ce" onclick="jdAfterSale.confirmShipmentsInfo(this,'${afterSaleCode}')" value="完善发运信息">
				</#if>
				
				<#-- 已同步则显示刷新按钮 -->
				<#if jdAfterSale.rsync_flag != '0' || jdAfterSale.afs_service_id != ''>
					<input type="button" class="btn  btn-success" zapweb_attr_operate_id="0f9232ec815643b7b8361aff624ba6d8" onclick="jdAfterSale.refreshAfterSaleStatus(this,'${afterSaleCode}')" value="刷新">
				</#if>
				
				<#-- 未同步并且售后单未取消 -->
				<#if (hjyAfterSale.asale_status == '4497477800050005' || hjyAfterSale.asale_status == '4497477800050013' || hjyAfterSale.asale_status == '4497477800050010') && jdAfterSale.afs_service_id == "" && jdAfterSale.rsync_flag == '0'>
					<input type="button" class="btn  btn-success" zapweb_attr_operate_id="80759a6e820911e9abac005056165069" onclick="jdAfterSale.rsyncAfterSaleCreate(this,'${afterSaleCode}')" value="同步">
				</#if>				
				</td>
		  	</tr>			
		</#if>
	</tbody>
</table>

<p>服务单追踪信息：<p>
<#assign trackList = b_method.upDataQuery("oc_order_jd_after_sale_track","create_date","","asale_code",afterSaleCode) />
<table class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	    <th>标题</th>
	    <th>内容</th>
	    <th>时间</th>
	    </tr>
  	</thead>
  
	<tbody>
		<#list trackList as e>
		<tr>
			<td>${e.title}</td>
			<td>${e.context}</td>
			<td>${e.create_date}</td>
	  	</tr>		
		</#list>
	</tbody>
</table>
</#macro>


