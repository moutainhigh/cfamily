

<@m_zapmacro_common_page_book b_page />

<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<form class="form-horizontal" method="POST" >
	<#list e_page.upBookData()  as e>
	  	<@m_zapmacro_common_book_field e e_page/>
	</#list>
</form>
</#macro>

<div class="zab_info_page_title  w_clear">
</div>

<div class="control-group">
	<div class="controls">
		<div>
			
			<script type="text/javascript">
				zapjs.f.require(['zapadmin/js/program_product'],function(a){a.init({"text":"","source":"page_chart_v_cf_pc_productinfo_multiSelect","id":"zw_f_product_code","value":"","max":"1"});});
			</script>
			<div class="w_left">
				<input id="zw_f_selectedProduct_id" class="btn" type="button" onclick="program_product.show_box('zw_f_product_code','SI2003')" value="添加商品">
			</div>
		</div>
	</div>
</div>
<br><br><br>

<#assign program_prodcut=b_method.upControlPage("page_chart_v_fh_program_product","zw_f_program_code=" + program_code).upChartData()>
<@m_zapmacro_common_table program_prodcut />


<#-- 显示页的自动输出判断 -->
<#macro m_zapmacro_common_book_field e_field   e_page>
	 <@m_zapmacro_common_field_show e_field e_page/>
</#macro>



<#-- 字段：显示专用 -->
<#macro m_zapmacro_common_field_show e_field e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote()+":" />
				<#if e_field.getFieldName() == "program_code">
					<#assign program_code=e_field.getPageFieldValue()?default("")>
					<input id="hide_program_code" type="hidden" value="${program_code}" >
				</#if>
	      		<div class="control_book">
		      		${e_field.getPageFieldValue()?default("")}
	      		</div>
	<@m_zapmacro_common_field_end />
</#macro>


<#macro m_zapmacro_common_field_start text="" for="">

<div class="control-group" style="float:left;width:49%;float: left;width: 49%;clear: none;">
	<label class="control-label" for="${for}">${text}</label>
	<div class="controls">

</#macro>
<#macro m_zapmacro_common_field_end>
	</div>
</div>
</#macro>



<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		      	 <#if (e_list_index <=6)&&(e_list_index > 1)>
			      	 <th>
			      	 	${e_list}
			      	 </th>
		      	</#if>
	      </#list>
	      <th colspan="2" width="10%" style="text-align: center;">操作</th>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	  		 	<#if (e_index <=6)&&(e_index > 1)>
		      		<td>
		      			${e?default("")}
		      		</td>
		      	</#if>
	      	</#list>
	      	<th colspan="2">
	      		<input zapweb_attr_operate_id="4698d540f49211e48d2d005056925439" class="btn btn-small" onclick="zapadmin.window_url('../show/page_edit_v_fh_program_product?zw_f_uid=${e_list[0]}')" type="button" value="设置排序">
				<input zapweb_attr_operate_id="4698d15ff49211e48d2d005056925439"  class="btn btn-small" onclick="zapjs.zw.func_delete(this,'${e_list[0]}')" type="button" value="删除">
			</th>
	      	</tr>
	 	</#list>
		</tbody>
</table>
</#macro>


