<style>
#test
{
	style='background-color: yellow' ;
}

</style>

<div class="w_left zw_page_tree_left" style="width:10%;float:left">
	<@m_common_html_script "require(['cfamily/js/propertyInfo'],function(a){cmanage.propertyInfo.init('propertyInfoId');});" />
	<input type="hidden" value="" id="selectDefineValueId">
	<div class="zw_page_tree_box">
		<select style="width: 150px; height: 200px;" id="propertyInfoId" size="30" name="22222">
			<#assign sc_defineService=b_method.upClass("com.cmall.productcenter.service.PcPropertyinfoService")>
			<#if sc_defineService??>
				<#assign scDefineList = sc_defineService.getFirstProperty("449746200001,449746200002,449746200003")>
				<#if scDefineList??>
					<#list scDefineList as e_list>
						 <#assign property_code=e_list["property_code"]>
						 <#assign property_name=e_list["property_name"]>
				      	<option value="${property_code?default("")}">${property_name?default("")}</option>
				     </#list>
				 </#if>
			</#if>
		</select>
	</div>
</div>
<div id="zw_page_tree_right" style="width:85%;float:right">


	<div id="zw_page_property_add"></div>
	
	<div style="width:55%;height:800px;float:left">
	<a id="zw_page_second_button" class="btn btn-small"  style="visibility:hidden" 
	onclick="cmanage.propertyInfo.addSecondMenu('propertyInfoId')">添加第二级</a>
	<div id="zw_page_property_chart"  style="margin:2px 2px 2px 2px;width:100%; height:800px;background-color:White;"></div>
	</div>
      
    <div style="width:40%;height:800px;float:right">
	<a id="zw_page_three_button" class="btn btn-small"  style="visibility:hidden" 
	onclick="cmanage.propertyInfo.addThirdMenu('zw_page_property_chart')">添加第三级</a>
	<div id="zw_page_property_three_chart"  style="margin:2px 2px 2px 2px;width:100%; height:800px; background-color:White;"></div>
	</div>
</div>

