<#assign product_code = b_page.getReqMap()["zw_f_product_code"]>
<#-- 原始商品属性 -->
<#assign productInfoBackup=b_method.upClass("com.cmall.productcenter.txservice.TxProductService").getProductInfoBackup(product_code)!''>
<#assign productMap=b_method.upDataOne("pc_productinfo","","","","product_code",product_code)>
<#assign tplNameMap=b_method.upDataOneOutNull("uc_freight_tpl","*","","is_default = '449746250001' and isDisable = '449746250002'")>
<#assign productExtMap=b_method.upDataOne("pc_productinfo_ext","","","","product_code",product_code)>
<#assign merchant_seller_type=b_method.upClass("com.cmall.usercenter.service.SellerInfoService")>
<@m_common_html_css ["cfamily/js/select2/select2.css"] />
<@m_common_html_css ["css/csb_base.css"] />
<@m_common_html_css ["css/icon-min.css"] />
<@m_common_html_css ["cfamily/css/video-js.min.css"] />
<style>
 .video-js .vjs-big-play-button{
        top:45%!important;
        left:50%!important;
        margin-left:-1.5em!important;
        }
</style>
<script>
// 海外购商品分类编码
var categoryHWG = '${b_page.upConfig('productcenter.category_haiwaigou')}';
require(['cfamily/js/editNewProduct','cfamily/js/select2/select2'],
function(p)
{
	<#assign product_support=b_method.upClass("com.cmall.productcenter.service.ProductService")>
	var product=${product_support.upProductInfoJson(product_code+"_1")};
	<#if tplNameMap['uid']??>
		cshop_product.defaultuid = "${tplNameMap['uid']}";
	</#if>
	<#-- 商户全部品类资质 -->
	<#assign qualification = b_method.upDataQueryToJson("pc_seller_qualification","brand_code,category_code,qualification_name,DATEDIFF(SYSDATE(),end_time) expired","","","small_seller_code",productMap["small_seller_code"])>
	<#if qualification??>
	product.qualification = ${qualification};
	<#else>
	product.qualification = [];
	</#if>	
	zapjs.f.ready(function()
	{
		p.init_product(product);
		
		<!-- 商品标签 -->
		<#assign autylogListChecked=b_method.upDataBysql("pc_product_authority_logo","select authority_logo_uid from pc_product_authority_logo where product_code = '${product_code}'")>
		<#if autylogListChecked??>
			<#list autylogListChecked as e>
				$('input[name="zw_f_authority_logo"][value="${e["authority_logo_uid"]!""}"]').prop("checked",true);
			</#list>
		</#if>
	});
});
function editProductSubmit(type,obj){
	//商品分类必须为四级
	var x = $('#xlNameId').html();
	var category = x.split('-&gt;');
	if(category.length!=3){
		zapjs.f.message("商品所属类目必须为四级");
		return false;
	}

	if(type){
		$("#zw_f_savetype").val(type);
	}
	zapjs.zw.func_edit(obj);
}

function checkAuthorityLogo(target){
	var current = $(target);
	var currentVal = current.val();
	var vs = ['${b_method.bConfig('productcenter.authority_logo_sevenday')}','${b_method.bConfig('productcenter.authority_logo_sevenday_no')}'];
	if(vs.indexOf(currentVal) < 0) return;
	
	// 7天退货规则必选，且只能选择一个
	if(current.prop('checked')){
		$('input[name="zw_f_authority_logo"]:checked').each(function(){
		if(current.attr('id') != $(this).attr('id')) {
			if(vs.indexOf($(this).val()) >= 0){
			    $(this).prop("checked",false);
			}
		}
		});	
	}else{
		$('input[name="zw_f_authority_logo"]:checked').each(function(){
		if(current.attr('id') != $(this).attr('id')) {
			if(vs.indexOf($(this).val()) >= 0){
			    $(this).prop("checked",false);
			}
		}
		});
	}

}
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
<input type="hidden" id="merchant_seller_type" value="${merchant_seller_type.getSellerType(productMap['small_seller_code'])}"/>
<input type="hidden" id="cshop_addproduct_uploadurl" value="${b_page.upConfig('zapweb.upload_target')}"/>
<input type="hidden" id="cfamily_upload_iframe_zw_f_description_pic" value="-1_false"/>
<input type="hidden" id="cfamily_upload_iframe_zw_f_piclist" value="-1_false"/>
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
			<span style="float:left;margin-left:400px;position: absolute;line-height:32px"><input class="btn btn-success" style="margin-left:40px" type="button" value="修改分类" onclick="zapadmin.window_url('../show/page_chart_v_uc_sellercategory_editNewProduct')"></span>
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
	<input type="hidden" id="zw_f_savetype" name="zw_f_savetype" value="submit">
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
	<div class="control-group">
		<label class="control-label"><span class="w_regex_need">*</span>商品保障：</label>
		<div class="controls">
			<div class="control_book">
			    <#assign autylogList=b_method.upDataBysql("pc_authority_logo","select * from pc_authority_logo where show_type = '449747960002'")>
			    <#list autylogList as e>
			    	<input type="checkbox" name="zw_f_authority_logo" id="zw_f_authority_logo_${e.zid}" value="${e.uid}" onclick="checkAuthorityLogo(this)" tn="${e.logo_content}">
			    	<label for="zw_f_authority_logo_${e.zid}"><img style="width:35px" src="${e.logo_pic}">${e.logo_content}</label>
			    </#list>
			</div>
		</div>
	</div>
	<div class="control-group hide">
		<label class="control-label"><span class="w_regex_need">*</span>贸易类型：</label>
		<div class="controls">
			<select name="zw_f_product_trade_type" id="zw_f_product_trade_type">
				<option value="">请选择</option>
				<option value="0">保税备货</option>
				<option value="1">海外直邮</option>
			</select>
		</div>
	</div>
	<#--如果有值则显示-->
	<#if productExtMap["delivery_store_type"] != "">
		<#assign deliveryStoreType=b_method.upDataOneOutNull("sc_define","","","","define_code", productExtMap["delivery_store_type"],"parent_code","449747160043")>
		<div class="control-group">
			<label class="control-label"><span class="w_regex_need">*</span>配送仓库：</label>
			<div class="controls">
				<input value="${deliveryStoreType.define_name?default("")}" disabled>
			</div>
		</div>	
	</#if>
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
  		<#--添加判断考拉商户过滤-->	 
  		<#elseif  e_field.getFieldTypeAid()=="104005019"&& e_field.getPageFieldName() == 'zw_f_qualification_category_code' &&productMap["small_seller_code"]=='SF03WYKLPT'> 
  		    <@m_zapmacro_common_field_disabled e_field/> 
  		 <#elseif  e_field.getFieldTypeAid()=="104005019"&& e_field.getPageFieldName() == 'zw_f_qualification_category_code' &&productMap["small_seller_code"]=='SF031JDSC'> 
  		    <@m_zapmacro_common_field_disabled e_field/> 
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
	  		<#if e_field.getPageFieldName() == "zw_f_product_desc_video">
	  			<@m_zapmacro_common_field_product_desc_video  e_field/>
	  		<#else>
	  			<@m_zapmacro_common_field_upload  e_field  e_page />
	  		</#if>
	  	<#elseif  e_field.getFieldTypeAid()=="104005024">
	  		<@m_zapmacro_common_field_upload_modify  e_field  e_page />	
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
	  	
  		<#if e_field.getPageFieldName() == "zw_f_piclist">
  			<@m_zapmacro_common_field_create e_field.getPageFieldName()/>
  		</#if>
		<#if e_field.getPageFieldName() == "zw_f_elm_description">
  			<@m_zapmacro_common_field_create e_field.getPageFieldName()/>
  		</#if>	  
  		<#if e_field.getPageFieldName() == "zw_f_description_pic">
  			<@m_zapmacro_common_field_create e_field.getPageFieldName()/>
		</#if>	  			
</#macro>

<#-- 字段：隐藏域 (且不传值)-->
<#macro m_zapmacro_common_field_disabled e_field>
	<input type="hidden" disabled="disabled" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}" />
</#macro>

<#macro m_zapmacro_common_field_create field_name>
<#if productInfoBackup != ''>
<div class="control-group">
	<#if field_name == "zw_f_piclist">
		<label class="control-label" for="zw_f_piclist">原商品主图：</label>
		<div class="controls">
			<div class="control_book" onclick="$(this).nextAll().toggle()"><a href="javascript:void(0)">点击查看↓</a></div>
			<#assign picList=productInfoBackup.getPcPicList()!''>
			<#list picList as e>
				<div class="w_left w_w_100 w_clear hide">
					<a href="${e.picUrl}" target="_blank"><img src="${e.picUrl}"></a>
					<div style="height:2px"></div>
				</div>
			</#list>
		</div>
	</#if>

	<#if field_name == "zw_f_elm_description">
		<label class="control-label" for="zw_f_elm_description">原商品属性：</label>
		<div class="controls">
			<div class="control_book" onclick="$(this).next().toggle()"><a href="javascript:void(0)">点击查看↓</a></div>
			<div class="control_book hide">
			<#list productInfoBackup.getPcProductpropertyList() as e>
				<b>${e.propertyKey}： </b>${e.propertyValue}；&nbsp;&nbsp;&nbsp;
			</#list>
			</div>
		</div>
	</#if>	
	
	<#if field_name == "zw_f_description_pic">
		<label class="control-label" for="zw_f_description_pic">原详情图片：</label>
		<div class="controls">
			<div class="control_book" onclick="$(this).nextAll().toggle()"><a href="javascript:void(0)">点击查看↓</a></div>
			<#assign description=productInfoBackup.getDescription()!''>
			<#if description != ''>
				<#assign descriptionPic=description.getDescriptionPic()!''>
				<#list descriptionPic?split('|') as e>
					<#if e != ''>
						<div class="w_left w_w_100 w_clear hide">
							<a href="${e}" target="_blank"><img src="${e}"></a>
							<div style="height:2px"></div>
						</div>				
					</#if>
				</#list>				
			</#if>
		</div>
	</#if>		  	
</div>
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

<#-- 视频 -->
<#macro m_zapmacro_common_field_product_desc_video e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="hidden" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		<video id="my-video" class="video-js video_hidden" controls preload="auto" width="600" height="300"  data-setup="{}"><source id="product_desc_video" src="" type="video/mp4"></video>
		<div  class="w_left w_p_10 video_hidden"><a href="javascript:cshop_product.deleteVideo()">删除视频</a></div>
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
	  					<option value="0.09">9</option>
	  					<option value="0.1">10</option>
	  					<option value="0.11">11</option>
	  					<option value="0.13">13</option>
	  					<option value="0.16">16</option>
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
<script type="text/javascript">  
    $(window).load(function() { 
	 	$('img').mouseover(function(obj){
	 		var height_ = obj.target.naturalHeight;
	 		var width_ = obj.target.naturalWidth;
	 		var msg = "图片暂未加载完成，请稍后!"; 
	 		 
	 		var opt = new Object();
			opt.imageUrl = obj.target.currentSrc;
			api_call('com_cmall_familyhas_api_ApiForImageProperty', opt , function(result){
				var size = result.size;
				msg = "该图片高 = " + height_  + "px | 宽 = " + width_ + "px | 大小 = " + size + "Kb";  
	        	$(obj.target.parentElement).prop("title", msg);   
			}); 
	    });
	}); 
 	function api_call(sTarget, oData, fCallBack) {
		//判断如果传入了oData则自动拼接 否则无所只传入key认证
		var defaults = oData?{
			api_target : sTarget,
			api_input : zapjs.f.tojson(oData),
			api_key : 'jsapi'
		}:{api_key : 'jsapi',api_input:''};
		
		//oData = $.extend({}, defaults, oData || {});

		zapjs.f.ajaxjson("../jsonapi/" + sTarget, defaults, function(data) {
			//fCallBack(data);			
			fCallBack(data);
		});
	} 
 
</script>
<script>
define('global/window', [], () => {
    return window;
});

define('global/document', ['global/window'], (window) => {
    return window.document;
});
require(['cfamily/js/video.min'],function(videojs){
	window.videojs = videojs;
})
</script> 