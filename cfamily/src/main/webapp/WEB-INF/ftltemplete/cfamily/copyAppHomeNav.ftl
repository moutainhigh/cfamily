
<#assign navCode = b_page.getReqMap()["zw_f_nav_code"] >

<#assign columnService=b_method.upClass("com.cmall.familyhas.service.AppHomeNavService")>

<#assign result = columnService.copyAppHomeNavToWechat(navCode) >

<#if result.resultCode == "1" >
	<div class="alert alert-success">
        <strong>
           	 复制成功。
        </strong>
    </div>
<#elseif result.resultCode == "2">
	<div class="alert">
        <strong>
           	 导航已存在，请核实。
        </strong>
    </div>
<#else>
	<div class="alert">
        <strong>
           	 复制失败。
        </strong>
    </div>
</#if>

