<div class="alert alert-error">
 <h4>警告!</h4>
 1. 一级目录和二级目录及生活家下的所有节点一律禁止删除，支持修改。<br>
 2.首页说明、友情链接下的节点可以随意删除、修改。
</div>

<div class="w_left zw_page_tree_left" style="width:40%;">

<@m_common_html_script "require(['cfamily/js/messageCategory'],function(a){a.tree_init();});" />


<@m_zapmacro_common_format_map_hidden  b_page.getReqMap() "zw_page_tree_" />

<@m_zapmacro_common_set_operate   b_page.getWebPage().getPageOperate() "116001016"  "btn btn-small" />
  

	<div class="zw_page_tree_box">
	
	<ul class="easyui-tree" id="zw_page_common_tree"></ul>
	</div>
</div>



<div class="zw_page_tree_right" id="zw_page_tree_right" style="width:60%;">
</div>
