<#assign purchase_order_id=b_page.getReqMap()["zw_f_purchase_order_id"] />

<div class="zab_info_page">
 
<#assign a_order_info=b_method.upControlPage("page_book_v_oc_purchase_order","zw_f_purchase_order_id="+purchase_order_id)>
<form class="form-horizontal" method="POST" >

	
	<#list a_order_info.upBookData()  as e>
		
	  	<@m_zapmacro_common_book_field e a_order_info/>
	  	
	</#list>
	
	
</form>








<#assign pService=b_method.upClass("com.cmall.familyhas.service.PurchaseOrderService")>
<#assign pInfoMapList=pService.getpurchaseOrderProducts("${purchase_order_id}")>

<div class="zab_info_page_title  w_clear">
<span>商品信息</span>
</div>

	<table  class="table  table-condensed table-striped table-bordered table-hover" style="margin-left:0%">
		<thead>
		    <tr>
		        <th>订单编号</th>
				<th>产品编号</th>
				<th>商品编号</th>
				<th>产品名称</th>
				<th>规格属性</th>
				<th>商品成本</th>
				<th>商品售价</th>
				<th>产品数量</th>
		    </tr>
	  	</thead>
		<tbody>
			<#if pInfoMapList??>
                <#if pInfoMapList?? && (pInfoMapList?size > 0)>
                    <#list pInfoMapList as fpInfoMap>
					<tr>
					    <td>${fpInfoMap["order_id"]?default("")}</td>
                        <td>${fpInfoMap["sku_code"]?default("")}</td>
						<td>${fpInfoMap["product_code"]?default("")}</td>
						<td>${fpInfoMap["product_name"]?default("")}</td>
						<td>${fpInfoMap["product_property"]?default("")}</td>
                        <td>${fpInfoMap["cost_money"]?default("")}</td>
						<td>${fpInfoMap["sell_money"]?default("")}</td>
						<td>${fpInfoMap["sku_num"]?default("")}</td>
					</tr>
					</#list>
                <#else>
                    <tr>
                        <td></td>
                        <td></td>
						<td></td>
						<td></td>
						<td></td>
                        <td></td>
						<td></td>
						<td></td>
					</tr>
			    </#if>
			</#if>
		</tbody>
	</table>
	
	
<div class="zab_info_page_title" w_clear >
		<span>配送信息</span>
</div>
<#assign addressInfo=pService.getpurchaseOrderAddressInfo("${purchase_order_id}")>
            <#-- 字段：显示专用 -->
			<div class="control-group">
				<label style="margin-left: 105px" class="control-label" for="">地址信息：&nbsp;&nbsp;&nbsp;${addressInfo["detail_addtess"]?default("")}</label>
			</div>
			
			<div class="control-group">
				<label style="margin-left: 105px" class="control-label" for="">收货人：&nbsp;&nbsp;&nbsp;${addressInfo["receiver"]?default("")}</label>
			</div>
			
			<div class="control-group">
				<label style="margin-left: 105px" class="control-label" for="">收货人手机号：&nbsp;&nbsp;&nbsp;${addressInfo["phone"]?default("")}</label>
			</div>
			
			<div class="control-group">
				<label style="margin-left: 105px" class="control-label" for="">邮政编码：&nbsp;&nbsp;&nbsp;${addressInfo["postcode"]?default("")}</label>
			</div>
			
</div>





