
<#assign uid = b_page.getReqMap()["zw_f_uid"] >

<#assign columnService=b_method.upClass("com.cmall.familyhas.service.HomeColumnService")>

<#assign result = columnService.copyColumnToWx(uid) >

<#if result.resultCode == "1" >
	<div class="alert alert-success">
        <strong>
           	 复制成功
        </strong>
    </div>
<#else>
	<div class="alert">
        <strong>
           	 复制失败
        </strong>
    </div>
</#if>

