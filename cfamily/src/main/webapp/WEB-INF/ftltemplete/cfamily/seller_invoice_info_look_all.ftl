<#assign small_seller_code=b_page.getReqMap()["zw_f_small_seller_code"] />
<@m_zapmacro_common_page_book b_page />

<style>
.zab_info_page_title {
    height: 25px;
    margin-bottom: 20px;
    margin-top: 10px;
    border-bottom: solid 1px #008299;
    color: #fff;
}

.zab_info_page_title span {
    background-color: #008299;
    padding: 5px 10px 6px 10px;
    text-align: center;
}
</style>

<#-- 字段：显示专用 -->
<#macro m_zapmacro_common_field_show e_field e_page>
	      <#if  e_field.getPageFieldName()=="zw_f_taxpayer_certificate_input">
      		<div class="zab_info_page_title" w_clear >
				<span>开票信息</span>
			</div>
      	  <#elseif e_field.getPageFieldName()=="zw_f_receiver_address">
      	    <div class="zab_info_page_title" w_clear >
				<span>收件人信息</span>
			</div>
      	  </#if>
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

<#assign fpService=b_method.upClass("com.cmall.ordercenter.service.FaPiaoInfoBookService")>
<#assign fpInfoMapList=fpService.getFaPiaoOperateLog("${small_seller_code}")>

<div class="zab_info_page_title" w_clear >
		<span>操作日志</span>
</div>
<div class="zab_info_page">
	<table  class="table  table-condensed table-striped table-bordered table-hover" style="margin-left:0%">
		<thead>
		    <tr>
		        <th>商户编号</th>
				<th>操作人姓名</th>
				<th>操作时间</th>
				<th>备注</th>
		    </tr>
	  	</thead>
		<tbody>
			<#if fpInfoMapList??>
                <#if fpInfoMapList?? && (fpInfoMapList?size > 0)>
                    <#list fpInfoMapList as fpInfoMap>
					<tr>
					    <td>${fpInfoMap["small_seller_code"]?default("")}</td>
                        <td>${fpInfoMap["operator"]?default("")}</td>
						<td>${fpInfoMap["operate_time"]?default("")}</td>
						<td>${fpInfoMap["remark"]?default("")}</td>
					</tr>
					</#list>
                <#else>
                    <tr>
                        <td></td>
                        <td></td>
						<td></td>
						<td></td>
					</tr>
			    </#if>
			</#if>
		</tbody>
	</table>