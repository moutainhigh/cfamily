<style type="text/css">
.redColor{
	color:#FF0000;
}
</style>
<div class="zab_info_page">
<div class="w_clear">

<#-- 库存及上下架数量取得  -->
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

</div>
</div>
</br>
<#-- 页面主内容显示  -->
	<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
	<#assign upchartDataCustom=b_method.upClass("com.cmall.familyhas.service.UpchartDataCustom")>
	<#assign category_code = b_page.getReqMap()["zw_f_category_code"]!'1' >
	
<#assign e_page=b_page>


<#macro m_zapmacro_common_auto_inquire e_page>
	<#list e_page.upInquireData() as e>
	
		<#if e.getQueryTypeAid()=="104009002">
			<@m_zapmacro_common_field_between e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009020">
			<@m_zapmacro_common_field_betweensfm e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009001">
			<#-- url专用  不显示 -->

	  	<#elseif  e.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e  e_page "请选择"/>
	  	<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
	  		
	  	</#if>
	  	
	</#list>
	
	<div class="control-group">
		<label class="control-label" for="zw_f_product_cate_one">商品一级分类</label>
		<div class="controls">
			<select id="zw_f_product_cate_one" name="zw_f_product_cate_one">
				<option value="">请选择</option>
				<#assign categoryMap=sellercategoryService.getCateGoryLevel2()>
				<#list categoryMap as map>
					<#if RequestParameters['zw_f_product_cate']?default('')==map['category_code']>
						<option value="${map['category_code']?trim}" selected>${map['category_name']?trim}</option>
					<#else>
						<option value="${map['category_code']?trim}">${map['category_name']?trim}</option>
					</#if>					
				</#list>
			</select>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="zw_f_product_cate">商品二级分类</label>
		<div class="controls">
			<select id="zw_f_product_cate_two" name="zw_f_product_cate_two">
				<option value="">请选择</option>
			</select>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="zw_f_product_cate">商品三级分类</label>
		<div class="controls">
			<select id="zw_f_product_cate_three" name="zw_f_product_cate_three">
				<option value="">请选择</option>
			</select>
		</div>
	</div>	
	<input id = "zw_f_category_code" name="zw_f_category_code" style="display:none;"/>
	<#--兼容form中input如果只有一个自动提交的情况-->
	<input style="display:none;"/>
</#macro>


	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
		
		<script type="text/javascript">
		
		var zw_f_product_code= $('#zw_f_product_code');
		if(zw_f_product_code){
			
			$("input[zapweb_attr_operate_id='9f1c883799f54237ac1e0e195aca7b25']").css("display","none");
			zw_f_product_code.after('&nbsp;<input type="button" class="btn  btn-success" zapweb_attr_operate_id="9f1c883799f54237ac1e0e195aca7b25" onclick="zapjs.zw.func_form(this)" value="手工同步商品">');
		}
		
</script>
	</div>
	<hr/>
	<#assign e_pagedata=upchartDataCustom.upChartData(e_page)>
	<div class="zw_page_common_data">
	
	<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
		      	 	<#if e_list_index==2 >
					 	 <th>商品分类</th>
					 	 <th>预览</th>
					</#if>
		      	 
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
			<#assign productCode=e_list[0]>
	  		 <#list e_list as e>
	      		<td>
	      			${e?default("")}
	      		</td>
	      		
	      			<#if e_index==2 >
					 		 <td>
							
										<#assign categoryMap=sellercategoryService.getCateGoryByProduct(productCode,"SI2003")>
										<#assign keys=categoryMap?keys>
									<#list keys as key>
									${categoryMap[key]?trim}<br>
								</#list>
							
							
							
							</td>
							<td>
								<a href="page_preview_v_pc_productDetailInfo?zw_f_product_code=${productCode}" target="_blank">预览</a>
							</td>
					</#if>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
	
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>
	
<script type="text/javascript">
	var category_code = '${category_code}';
	if(category_code.length == 12){
		$("#zw_f_product_cate_one").val(category_code);
		onechange(0);
	}
	if(category_code.length == 16){
		var one =  category_code.substring(0,12);
		$("#zw_f_product_cate_one").val(one);
		onechange(category_code);
	}
	if(category_code.length == 20){
		var one =  category_code.substring(0,12);
		var two =  category_code.substring(0,16);
		$("#zw_f_product_cate_one").val(one);
		onechange(two,category_code);
	}
function sub(su){
	var three = $("#zw_f_product_cate_three").val();
	var two = $("#zw_f_product_cate_two").val();
	var one = $("#zw_f_product_cate_one").val();
	if(three != ''){
		$("#zw_f_category_code").val(three);
	}else if(two != ''){
		$("#zw_f_category_code").val(two);
	}else if(one != ''){
		$("#zw_f_category_code").val(one);
	}
	$('#zw_f_product_cate_one').attr("disabled",true);
	$('#zw_f_product_cate_two').attr("disabled",true);
	$('#zw_f_product_cate_three').attr("disabled",true);
	zapjs.zw.func_inquire(su);
}
String.prototype.format=function(){  
	 if(arguments.length==0) 
	 	return this;  
	 for(var s=this, i=0; i<arguments.length; i++)  
		s=s.replace(new RegExp("\\{"+i+"\\}","g"), arguments[i]);  
	 	return s;  
};
function onechange(category_code,three){
	$("#zw_f_product_cate_two").html("<option value=''>请选择</option>");
		$("#zw_f_product_cate_three").html("<option value=''>请选择</option>");
		var data = {};
		data.cate = $("#zw_f_product_cate_one").val(); 
		zapjs.f.ajaxjson("../func/7f2faf1bf65c4f4f818215a90164ad22", data, function(data) {
			var text = "<option value=''>请选择</option>";
			if(data.resultCode == 1){
				var datarelist = data.resultObject;
				for(j = 0,len=datarelist.length; j < len; j++) {
					var r = datarelist[j];
					text =  text+"<option value='{0}'>{1}</option>".format(r.category_code,r.category_name);
				}
				$("#zw_f_product_cate_two").html(text);
				if(typeof(category_code) == 'string'){
					$("#zw_f_product_cate_two").val(category_code);
					if(typeof(three) == 'string'){
						twochange(three);	
					}else{
						twochange();
					}
					
				}
			}
		});
}
function twochange(category_code){
	$("#zw_f_product_cate_three").html("<option value=''>请选择</option>");
		var data = {};
		data.cate = $("#zw_f_product_cate_two").val(); 
		zapjs.f.ajaxjson("../func/3dab5ecc1dde4127bf698ffa2c725a5a", data, function(data) {
			var text = "<option value=''>请选择</option>";
			if(data.resultCode == 1){
				var datarelist = data.resultObject;
				for(j = 0,len=datarelist.length; j < len; j++) {
					var r = datarelist[j];
					text =  text+"<option value='{0}'>{1}</option>".format(r.category_code,r.category_name);
				}
				$("#zw_f_product_cate_three").html(text);
				
				if(typeof(category_code) == 'string'){
					$("#zw_f_product_cate_three").val(category_code);
				}
				
			}
		});
}
$("input[zapweb_attr_operate_id='7f2faf1bf65c4f4f818215a90164ad22']").css("display","none");
$("input[zapweb_attr_operate_id='3dab5ecc1dde4127bf698ffa2c725a5a']").css("display","none");
	$("#zw_f_product_cate_one").change(onechange);
	$("#zw_f_product_cate_two").change(twochange);
</script>
	