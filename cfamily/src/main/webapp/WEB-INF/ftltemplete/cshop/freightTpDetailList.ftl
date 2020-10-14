<#-- 获取模板编码-->
<#assign tplUid = b_page.getReqMap()["zw_f_uid"]>
<#assign vType = b_page.getReqMap()["vType"]>

<#if vType=='449746290001'>
	<#assign startV='首件数(个)'/>
	<#assign appendV='续件数(个)'/>
<#elseif vType='449746290002'>		
	<#assign startV='首重量(kg)'/>
	<#assign appendV='续重量(kg)'/>
<#else>	
	<#assign startV='首体积(m³)'/>
	<#assign appendV='续体积(m³)'/>
</#if>
<#--模板明细 -->
<#assign tplDetailList = b_method.upDataQuery("uc_freight_tpl_detail","tpl_uid","tpl_uid='${tplUid?default('')}'")>
<table class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
      	 <th width="30%">
      	 	运送到
      	 </th>
      	 <th width="14%">
      	 	是否可售 
      	 </th>
      	 <th width="14%">
      	 	${startV}
      	 </th>
      	 <th width="14%">
      	 	首费(元) 
      	 </th>
      	 <th width="14%">
      	 	${appendV}
      	 </th>
      	 <th width="14%">
      	 	续费(元) 
      	 </th>
	    </tr>
  	</thead>
  
	<tbody>
		<#list tplDetailList as detail>
		<tr>
			<#assign sUid = detail['uid']>
      		<td>
      			<#assign area = detail['area']>
				<span>${area?default('')}</span>
      		</td>
      		<td>
      			<#assign isEnable = detail['isEnable']>
				<span>
					<#if isEnable=='1'>
						可售
					<#else>
						不可售
					</#if>		
				</span>
      		</td>
      		<td>
      			<#assign expressStart = detail['express_Start']>
				<span>${expressStart?default('')}</span>
      		</td>
      		<td>
      			<#assign expressPostage = detail['express_Postage']>
				<span>${expressPostage?default('')}</span>
      		</td>
      		<td>
      			<#assign expressPlus = detail['express_Plus']>
				<span>${expressPlus?default('')}</span>
      		</td>
      		<td>
      			<#assign expressPostageplus = detail['express_Postage_plus']>
				<span>${expressPostageplus?default('')}</span>
      		</td>
      	</tr>
      	</#list>
	</tbody>
</table>