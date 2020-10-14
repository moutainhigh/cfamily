<#assign product_code = b_page.getReqMap()["zw_f_product_code"]>
<@m_common_html_css ["cfamily/js/select2/select2.css"] />
<@m_common_html_css ["css/csb_base.css"] />
<@m_common_html_css ["css/icon-min.css"] />
<script>
require(['cfamily/js/editNewProduct','cfamily/js/select2/select2'],
function(p)
{
	<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>
	var product=${product_support.upProductInfoJson(product_code+"_1")};
	zapjs.f.ready(function()
	{
		p.init_product(product);
	});
});
</script>
<style>
.hint-layer {
    width:420px;
    background-color: #fff;
    border: 1px solid #bed3f1;
    box-shadow: 1px 1px 2px 0 rgba(0, 0, 0, 0.3);
    padding:10px 0 20px;
     position:absolute;
     overflow:hidden
}
.hint-layer .hint-ext-close {
    cursor: pointer;
    height: 13px;
    outline: 0 none;
    overflow: hidden;
    position: absolute;
    right: 5px;
    text-decoration: none;
    top: 5px;
    vertical-align: middle;
    width: 13px;
}
.hint-layer .hint-ext-close:hover {
    background: #fff none repeat scroll 0 0;
    border: 1px solid #ced5e0;
}
.hint-layer .hint-ext-close-x {
    background: rgba(0, 0, 0, 0) url("//img.alicdn.com/tps/i3/T161zXXjXhXXblwoHk-19-100.png") no-repeat scroll -999em -999em;
}
.hint-layer .hint-ext-close-x {
    background-position: -5px -45px;
    display: block;
    height: 9px;
    margin: 2px;
    text-indent: -9999px;
    width: 20px;
}
.hint-layer .batch-header{height:20px; line-height:20px; padding:0 0 0 10px;font-weight:bold; }
.hint-layer .batch-type{float:left; display:inline; width:46%; border-left:1px dotted #ccc; padding:0 0 0 15px}
.hint-layer .first{border:none;}
.hint-layer .batch-type .caption{width:100%; }
.hint-layer .batch-type .list{width:100%; list-style:none; margin:10px 0 0 0}
.hint-layer .batch-type .list input{float:left; display:inline;}
.hint-layer .batch-foot{float:left; display:inline; width:100%; text-align:center; padding:10px 0 0; overflow:hidden}
.hint-layer .batch-foot a{display:inline-block; margin:0 10px;}

.help-pop{position:absolute; left:300px; top:300px; display:none; overflow: hidden;}
.help-pop .hpop-c{width:290px; height:auto; background:#ffffe5; padding:10px; border:1px solid #eee9ba; margin:0 0 8px; font-size:12px; line-height:18px; color:#333; overflow: hidden;}
.help-pop i{display:block; position:absolute;  left:50%; margin-left:-5px; bottom:1px; width: 0;height: 0;border-left: 8px solid transparent;border-right: 8px solid transparent;border-top: 8px solid #ffffe5; z-index:2 }
.help-pop b{display:block; position:absolute;  left:50%; margin-left:-7px; bottom:0; width: 0;height: 0;border-left: 10px solid transparent;border-right: 10px solid transparent;border-top: 9px solid #eee9ba; z-index:1  }
</style>
<input type="hidden" id="cshop_addproduct_uploadurl" value="${b_page.upConfig('zapweb.upload_target')}"/>
<input type="hidden" id="cfamily_upload_iframe_zw_f_description_pic" value="-1_false"/>
<form class="form-horizontal" role="form">
<input type="hidden" id="zw_f_json" name="zw_f_json" value=""/>

<fieldset>
	<div id="catogoryDivInfo">
		<div class="control-group">
			<input name="zw_f_listbox" id="zw_f_listbox" type="hidden" value="">
			<input name="zw_f_category_codes" id="zw_f_category_codes" type="hidden" value="">
		</div>
		
		<h5 id="BreadcrumbNavigation">当前选中的分类 ：</h5>
		<br />
		<div class="control-group">
			<button type="button" class="btn btn-large btn-primary" onclick="cshop_product.SecondStep()">我选好了，下一步</button>
		</div>
	</div>
	<div id="OtherProductInfo" class="w_display">
			<span style="float:left;margin-left:400px;position: absolute;line-height:32px"><input class="btn btn-success" type="button" value="修改分类" onclick="zapadmin.window_url('../show/page_chart_v_uc_sellercategory_editNewProduct')"></span>
			<div class="control-group">
				<label class="control-label">
					商品所属类目：
				</label>
				<div class="controls">
					<div class="control_book" id='xlNameId'></div>
				</div>
			</div>
 			<div id="cshop_product_insert_prop">
	   		 	<div id="cshop_addproduct_pextend"  class="csb_cshop_addproduct_pextend  w_display w_clear">
	   		 		<div class="csb_cshop_addproduct_title">扩展属性</div>
	   		 		<div class="csb_cshop_addproduct_item">
			   		 	<table  class="table ">
				   		 	<thead>
				   		 		<tr>
				   		 		<th>名称
				   		 		</th>
				   		 		<th>内容
				   		 		</th>
				   		 		</tr>
				   		 	</thead>
			   		 	
				   		 	<tbody>
				   		 	
				   		 	</tbody>
			   		 	</table>
	   		 		</div>
	   		 	
	   		 	</div>
	   		 	
	   		 	
	   		 	<div id="cshop_addproduct_custom"  class="csb_cshop_addproduct_pextend   w_clear">
	   		 		<div class="csb_cshop_addproduct_title">自定义属性</div>
	   		 		<div class="csb_cshop_addproduct_item">
			   		 	<table  class="table" id="move">
				   		 	<thead>
				   		 		<tr>
				   		 			<th>名称</th>
				   		 			<th>内容</th>
				   		 			<th>操作</th>
				   		 			<th>位置移动</th>
				   		 		</tr>
				   		 	</thead>
				   		 	<tbody>
				   		 	</tbody>
				   		 	<tfoot>
				   		 	<tr>
					   		 	<td>
					   		 		<input id="cshop_addproduct_custom_text" />
					   		 	</td>
					   		 	<td>
					   		 		<textarea rows="2" id="cshop_addproduct_custom_value"></textarea>
					   		 	</td>
					   		 	<td>
					   		 		<input type="button" class="btn" onclick="cshop_product.add_custom()" value="添加"/>
					   		 	</td>
					   		 	<td>无</td>
				   		 	</tr>
				   		 	</tfoot>
			   		 	</table>
			   		 	
	   		 		</div>
	   		 	
	   		 	</div>
	   		</div>	
			<div id="cshop_addproduct_travel">
	 				<ul class="w_ullist">
	 					<li>
	 					<input type="radio" name="zw_f_tra_select" id="zw_f_tra_select_0" value="0" checked="checked"/><label for="zw_f_tra_select_0">卖家包邮</label>
	 					</li>
	 					<li class="w_mt_15">
	 					<input type="radio" name="zw_f_tra_select"  id="zw_f_tra_select_1" value="1"/><label for="zw_f_tra_select_1">买家承担运费&nbsp;&nbsp;&nbsp;</label><input type="text" id="zw_f_tra_user"   class="span2" value="">
	 					</li>
	 					<li class="w_mt_15">
	 						<div class="w_left"><input type="radio" name="zw_f_tra_select" id="zw_f_tra_select_2" value="2"/><label for="zw_f_tra_select_2"><input class="btn btn-small" type="button" value="选择运费模板" onclick="zapadmin_single.show_box('zw_f_traval')"/></label>
	 						<input id="zw_f_traval_show_text" type="hidden" value="">
	 						<script type="text/javascript">zapjs.f.require(['zapadmin/js/zapadmin_single'],function(a){a.init({"text":"","buttonflag":false,"source":"page_chart_v_chart_uc_freight_tpl","id":"zw_f_traval","value":"","max":"1"});});</script>
	 						</div>
	 						<div class="w_left w_w_70p"><ul class="zab_js_zapadmin_single_ul" id="zw_f_traval_show_ul"></ul></div>
	 					</li>
	 				</ul>
 				</div>
		  <@m_zapmacro_common_page_add b_page />
		<div>
		
		
		
		
		<br />
		
			
	</div>
</fieldset>
</form>

<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upAddData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>
<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
		
	  	<@m_zapmacro_common_auto_field e e_page/>
	  	
	</#list>
	</#if>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
	
		<#if e_field.getFieldTypeAid()=="104005008">
	  		<@m_zapmacro_common_field_hidden e_field/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005001">
	  		  <#-- 内部处理  不输出 -->
	  	<#elseif  e_field.getFieldTypeAid()=="104005003">
	  		<@m_zapmacro_common_field_component  e_field  e_page/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005004">
	  		<@m_zapmacro_common_field_date  e_field />
		<#elseif  e_field.getFieldTypeAid()=="104005022">
  			<@m_zapmacro_common_field_datesfm  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005019">
	  	
	  		<#if e_field.getPageFieldName() == 'zw_f_keyword'>
	  			<@m_zapmacro_common_field_select  e_field  e_page "--请选择--"/>
	  		<#elseif e_field.getPageFieldName() == 'zw_f_qualification_category_code'>
	  			<@m_zapmacro_common_field_select  e_field  e_page "请选择"/>
	  		<#else>
	  			<@m_zapmacro_common_field_select  e_field  e_page ""/>
	  		</#if>
	  		
	  	<#elseif  e_field.getFieldTypeAid()=="104005103">
	  		<@m_zapmacro_common_field_checkbox  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005020">
	  		<@m_zapmacro_common_field_textarea  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005005">
	  		<@m_zapmacro_common_field_editor  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005021">
	  		<@m_zapmacro_common_field_upload  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005024">
	  		<@m_zapmacro_common_field_upload_modify  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>


<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		<#if e_field.getPageFieldName() == 'zw_f_product_name'>
			<br/>
			<#--（商品名称长度不超过40个字符）-->
		<#elseif e_field.getPageFieldName() == 'zw_f_tax_rate'>
			&nbsp;&nbsp;%
		<#else>
			
		</#if>
	
	<@m_zapmacro_common_field_end />
</#macro>

<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_select   e_field    e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
      					<option value="">${e_text_select}</option>
      				</#if>
      				<#if e_field.getPageFieldName() == 'zw_f_tax_rate'>
	  					<option value="0">0</option>
	  					<option value="0.1">10</option>
	  					<option value="0.11">11</option>
	  					<option value="0.13">13</option>
	  					<option value="0.16" selected="selected">16</option>
	  					<option value="0.17">17</option>      					
      				<#else>
      					<#list e_page.upDataSource(e_field) as e_key>
							<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
						</#list>
      				</#if>
	      		</select>
	      		<#if e_field.getPageFieldName() == 'zw_f_tax_rate'>
	      			&nbsp;&nbsp;%
	      		<#else>
      			</#if>
	<@m_zapmacro_common_field_end />
</#macro>

<#-- 字段：上传 -->
<#macro m_zapmacro_common_field_upload e_field e_page>
	<#if e_field.getPageFieldName() == 'zw_f_mainpic_url'>
		<div class="ico-helpw">
				<i class="poptip-attention ico-fques" style="float:left;margin-left:370px;position: absolute;margin-top:12px;line-height:32px"></i>
			<div class="help-pop">
				<div class="hpop-c">
				1.图片清晰，无尺寸变形或者失真情况，无水印，无牛皮癣<br/>
				2.须为所售商品白底商品图或商品使用场景图；<br/>
				3.像素800*800（宽*高）；
				</div>
				<i></i>
				<b></b>
			</div>
		</div>
	<#elseif e_field.getPageFieldName() == 'zw_f_piclist'>
		<div class="ico-helpw">
				<i class="poptip-attention ico-fques" style="float:left;margin-left:370px;position: absolute;margin-top:12px;line-height:32px"></i>
				<div class="help-pop">
					<div class="hpop-c">
						1.图片清晰，无尺寸变形或者失真情况，无水印，无牛皮癣<br/>
						2.须为所售商品白底商品图或商品使用场景图；<br/>
						3.像素800*800（宽*高）；
					</div>
					<i></i>
					<b></b>
				</div>
		</div>
	<#elseif e_field.getPageFieldName() == 'zw_f_description_pic'>
			<div class="ico-helpw">
			<i class="poptip-attention ico-fques" style="float:left;margin-left:370px;position: absolute;margin-top:12px;line-height:32px"></i>
				<div class="help-pop">
					<div class="hpop-c">
						1.字体：必须使用无版权如：微软雅黑，黑体，楷体，幼圆;（使用有版权字体，必须提供版权授权书。）<br/>  
						2.字号： 可以保障用户通过手机正常阅读  <br/>
						3.图片要求：图片清晰，无尺寸变形或者失真情况；无水印或牛皮癣等其他平台标识。<br/>
						4.上传描述图片可以不切图，整张图片宽度不小于750,高不限，如切图，每张图片宽度不小于750,高不大于1200；
					</div>
					<i></i>
					<b></b>
				</div>
			</div>
	<#else>
	</#if>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="hidden" zapweb_attr_target_url="${e_page.upConfig("zapweb.upload_target")}" zapweb_attr_set_params="${e_field.getSourceParam()}"    id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
		<span class="control-upload_iframe"></span>
		<span class="control-upload_process"></span>
		<span class="control-upload"></span>
		
	<@m_zapmacro_common_field_end />
	<@m_zapmacro_common_html_script "zapjs.f.ready(function(){zapjs.f.require(['zapweb/js/zapweb_upload'],function(a){a.upload_file('"+e_field.getPageFieldName()+"','"+e_page.upConfig("zapweb.upload_target")+"');}); });" />
</#macro>

<#-- 页面按钮 -->
<#macro m_zapmacro_common_operate_button  e_operate  e_style_css>
	<#if e_operate.getOperateUid() == 'f69b345e8f3111e5aad9005056925439'>
		<#-- 提交审批 -->
		<input type="button"  class="${e_style_css}" onclick="cshop_product.approveBtnClick('edit',this)"  value="${e_operate.getOperateName()}" />
		<input type="button" id="approveBtnSubmit" style="display:none" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="${e_operate.getOperateLink()}"  value="${e_operate.getOperateName()}" />
	<#elseif e_operate.getOperateUid() == '3b0429c0b63c437ba0c609ee2cd26f31'>
		<#-- 保存到草稿箱 -->
		<input type="button"  class="${e_style_css}" onclick="cshop_product.saveDraftBoxBtnClick()"  value="${e_operate.getOperateName()}" />
		<input type="button" id="draftBoxBtnSubmit" style="display:none" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="${e_operate.getOperateLink()}"  value="${e_operate.getOperateName()}" />
	<#else>
		<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="${e_operate.getOperateLink()}"  value="${e_operate.getOperateName()}" />
	</#if>
</#macro>

<#assign product_flow=b_method.upClass("com.cmall.productcenter.service.ProductFlowFacade")>
<#assign mdMap = product_flow.getMDUserInfoList()>

<div id="MdTable" style="display:none">
	<table  border="1" cellpadding="10" cellspacing="0"  style="width:100%;height:200px;">
		<tr>
			<th>选择框</th>
			<th>姓名</th>
		</tr>
		<#list mdMap as mdata>
		<tr>
			<td><input type="radio" onclick="cshop_product.radioClick(this)" value="${mdata.user_code}"  name="mcheck"></td>
			<td id="m${mdata.user_code}">${mdata.real_name}</td>
		</tr>
		</#list>
	</table>
	<div class="w_p_20">
		<input type="hidden" id="mdId" name="mdId"/>
		<input type="hidden" id="mdName" name="mdName"/>
		<input class="btn window_iframe_btn" type="button" onclick="cshop_product.func_confirm('mdFrame')" value="确认选择">
	</div>
</div>

<#--批量设置弹出层-->
<div class="sku-batchlayer hint-layer hint-overlay hint-ext-position" id="group_operation" style="display:none;">
	<div class="hint-contentbox" id="second_div">
		<div class="batch-header">批量设置</div>
		<div class="batch-body">
			<div class="batch-type first">
				<div class="caption">价格：</div>
				<ul class="list">
					<li>
						<input id="batch-price-0" class="batch-radio" type="radio" name="batch-price" data-index="0" data-type="price" >
						<label for="batch-price-0">同颜色分类价格相同</label>
					</li>
					<li>
						<input id="batch-price-1" class="batch-radio" type="radio" name="batch-price" data-index="1" data-type="price" >
						<label for="batch-price-1">同尺码价格相同</label>
					</li>
				</ul>
			</div>
			<div class="batch-type">
				<div class="caption">库存：</div>
				<ul class="list">
					<li>
						<input id="batch-quantity-0" class="batch-radio" type="radio" name="batch-quantity" data-index="0" data-type="quantity" >
						<label for="batch-quantity-0">同颜色分类库存相同</label>
					</li>
					<li>
						<input id="batch-quantity-1" class="batch-radio" type="radio" name="batch-quantity" data-index="1" data-type="quantity" >
						<label for="batch-quantity-1">同尺码库存相同</label>
					</li>
				</ul>
			</div>
		</div>
		<div class="batch-foot">
			<a id="copyOk" class="btn btn-default" onclick="cshop_product.onclick_ok()">
				<span class="btn-txt">确定</span>
			</a>
			<a class="cancel" href="javascript:void(0)" onclick="cshop_product.onclick_cancle()">取消</a>
		</div>
	</div>
	<a class="hint-ext-close" role="button" href="javascript:void("关闭")" tabindex="0" onclick="cshop_product.onclick_x()">
		<span class="hint-ext-close-x" >关闭</span>
	</a>
</div>
