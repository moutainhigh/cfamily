<div class="w_left zw_page_tree_left" style="width:40%;">

<@m_common_html_script "require(['cfamily/js/categorySelectEditNewProduct'],function(a){a.tree_init();});" />

	<div class="zw_page_tree_box">
	
	<ul class="easyui-tree" id="zw_page_common_tree" ></ul>
	</div>
</div>

<form class="form-horizontal" method="POST">
<div class="zw_page_tree_right" id="zw_page_tree_right" style="width:60%;">
<br>
	
	
	<span style="float:right;width:65%;height:80%"">
	
	<input type="hidden" name="zw_f_product_code" value="">
	<ul class="unstyled" id="v_v">
	</span>
	</ul>

</div>


	<div class="control-group">
    	<div class="controls">
    	<br><br>
		<input type="button" class="btn  btn-success" onclick="category_tree.tree_selectCategroy()" value="确定">
		</div>
	</div>


</from>



	<input type="hidden" name="zw_f_cuid" id="zw_f_cuid" value="">
	<input type="hidden" name="zw_f_caname" id="zw_f_caname" value="">

