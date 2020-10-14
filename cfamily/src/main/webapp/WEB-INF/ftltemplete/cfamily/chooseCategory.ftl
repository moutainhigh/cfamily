<@m_common_page_head_common e_title=b_page.getWebPage().getPageName() e_bodyclass="zab_page_default_body" />
<#assign categoryCodes = b_page.getReqMap()["zw_f_category_code"]?default('') >
<#assign maxLevel = b_page.getReqMap()["zw_f_maxLevel"]?default('0') >

<div class="w_left zw_page_tree_left" style="width:40%;">

<@m_common_html_script "require(['cfamily/js/chooseCategory'],function(a){chooseCategory.init('${b_page.upReqValue('zw_s_iframe_choose_callback')?default('')}','${maxLevel}');});" />

	<div class="zw_page_tree_box">
	
	<ul class="easyui-tree" id="zw_page_common_tree" ></ul>
	</div>
</div>

<#assign coupon_support=b_method.upClass("com.cmall.systemcenter.service.CategoryService")>
<#assign categoryMap=coupon_support.getCategoryFullInfo(categoryCodes)>
<form class="form-horizontal" method="POST">
<div class="zw_page_tree_right" id="zw_page_tree_right" style="width:60%;">
  <input type="hidden" value="SI2003" id="sellerCodeCategory"/>
<br>
	<span style="float:left;width:20%;height:80%" align="center">
		<input type="button" class="btn" value="&gt;" id="tgc_but" onclick="chooseCategory.choosedItem()">
	</span>
	
	
	<span style="float:right;width:65%;height:80%"">
	
	<ul class="unstyled" id="v_v">
		<#assign keys=categoryMap?keys>
		<#list keys as key>
			<li id="li${key}">${categoryMap[key]}&nbsp;&nbsp; <input type="hidden" value="${key}" name="zw_f_category_code" class="a_v_v_a"> <a href="javascript:void(0)" onclick="chooseCategory.deleteItem('${key}')">删除</a></li>
		</#list>
	</span>
	</ul>

</div>


	<div class="control-group">
    	<div class="controls">
    	<br><br>
		<input type="button" class="btn  btn-success" onclick="chooseCategory.submit(this)" value="确认选择">
		</div>
	</div>


</from>