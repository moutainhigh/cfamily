<style type="text/css">
.redColor{
	color:#FF0000;
}
</style>
<script text="text/javascript" />
	function copyProductClick(formObj){
		 var queryjson = $(formObj).parents("form").serializeArray();
		 var item={name:'targetSystem',value:'SI2003'}
		 queryjson.push(item)
		 zapjs.f.ajaxjson("../func/09cf647b628c11e5aad9005056925439", queryjson, function(data) {
			func_returnMsg(data,formObj);
		});
	}
	function func_returnMsg(data,formObj){
		if(data.resultCode == "1"){
			 if(confirm(data.resultMessage))
			 {
				zapjs.zw.func_form(formObj);
			 }
		}else{
			zapjs.zw.modal_show({
						content : data.resultMessage
					});
		}
	}

</script>

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
	
<#assign e_page=b_page>


	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
		
		<script type="text/javascript">
		
		var zw_f_product_code= $('#zw_f_product_code');
		if(zw_f_product_code){
			
			$("input[zapweb_attr_operate_id='e6e3b2966d6111e5aad9005056925439']").css("display","none");
			zw_f_product_code.after('&nbsp;&nbsp;<input type="button" class="btn  btn-success" zapweb_attr_operate_id="e6e3b2966d6111e5aad9005056925439" onclick="copyProductClick(this)" value="沙皮狗互联互通">');
		}
		
</script>
	</div>
	<hr/>
	
	<#assign e_pagedata=e_page.upChartData()>
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
					</#if>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
	
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>
	

	