<style type="text/css">
.redColor{
	color:#FF0000;
}
</style>
<div class="cmb_cmanage_page">
<div class="w_clear">

<#-- 库存取得方法  -->
<#assign e_list = b_method.upDataOne("pc_productinfo","count(1) as stocknum,sum(case product_status when 4497153900060002 then 1 else 0 end  ) as onsalenum,sum(case product_status when 4497153900060003 then 1 else 0 end  ) as offsalenum1,sum(case product_status when 4497153900060004 then 1 else 0 end  ) as offsalenum2","","")>

<#-- 库存取得  -->
<#assign stocknum = e_list["stocknum"]?number>

<#-- 上架数量取得 -->
<#if e_list["onsalenum"]??>
	<#if e_list["onsalenum"] == "">
		<#assign onsalenum = 0>
	<#else>
		<#assign onsalenum = e_list["onsalenum"]?number>
	</#if>
<#else>
	<#assign onsalenum = 0>
</#if> 

<#-- 商家下架数量取得 -->
<#if e_list["offsalenum1"]??>
	<#if e_list["offsalenum1"] == "">
		<#assign offsalenum1 = 0>
	<#else>
		<#assign offsalenum1 = e_list["offsalenum1"]?number>
	</#if>
<#else>
	<#assign offsalenum1 = 0>
</#if>

<#-- 强制下架数量取得   -->
<#if e_list["offsalenum2"]??>
	<#if e_list["offsalenum2"] == "">
		<#assign offsalenum2 = 0>
	<#else>
		<#assign offsalenum2 = e_list["offsalenum2"]?number>
	</#if>
<#else>
	<#assign offsalenum2 = 0>
</#if>

<#-- 库存显示   -->
<span>共</span><span class="redColor">入库${stocknum}件</span><span>商品，</span><span class="redColor">上架${onsalenum}件</span><span>，</span><span class="redColor">下架${offsalenum1+offsalenum2}件</span><span>。</span>
</div>
</div>
</br>
<#-- 页面主内容显示  -->
<@m_zapmacro_common_page_chart b_page />