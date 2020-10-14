<#assign categoryCodes = b_page.getReqMap()["zw_f_category_codes"] >

<div class="w_left zw_page_tree_left" style="width:40%;">

<@m_common_html_script "require(['cfamily/js/couponTypeLimit_category_select'],function(a){a.tree_init();});" />

	<div class="zw_page_tree_box">
	
	<ul class="easyui-tree" id="zw_page_common_tree" ></ul>
	</div>
</div>

<#assign coupon_support=b_method.upClass("com.cmall.ordercenter.service.CouponsService")>
<#assign categoryMap=coupon_support.getCateGoryByCouponTypeLimit(categoryCodes,"","SI2003")>
<form class="form-horizontal" method="POST">
<div class="zw_page_tree_right" id="zw_page_tree_right" style="width:60%;">
<br>
	<span style="float:left;width:20%;height:80%" align="center">
		<input type="button" class="btn" value="&gt;" id="tgc_but">
	</span>
	
	
	<span style="float:right;width:65%;height:80%"">
	
	<ul class="unstyled" id="v_v">
		<#assign keys=categoryMap?keys>
		<#list keys as key>
			<li id="li${key}">${categoryMap[key]}&nbsp;&nbsp; <input type="hidden" value="${key}" name="zw_f_category_code" class="a_v_v_a"> <a href="javascript:void(0)" onclick="couponTypeLimit_category_select.del_category('li${key}')">删除</a></li>
		</#list>
	</span>
	</ul>

</div>


	<div class="control-group">
    	<div class="controls">
    	<br><br>
		<input type="button" class="btn  btn-success" onclick="couponTypeLimit_category_select.func_call(this)" value="确认选择">
		</div>
	</div>


</from>
	<input type="hidden" name="zw_f_cuid" id="zw_f_cuid" value="">
	<input type="hidden" name="zw_f_caname" id="zw_f_caname" value="">
