<div class="zab_info_page">
	<div class="zab_info_page_title  w_clear">
		<span>优惠券类型管理</span>
	</div>
	<@m_zapmacro_common_page_book b_page />
</div>

<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<form class="form-horizontal" method="POST" >
	<#list e_page.upBookData()  as e>
	  	<@m_zapmacro_common_book_field e e_page/>
	  	<#if (e.getFieldName() == "creator")>
	  		<#assign creator = e.getPageFieldValue() >
	  	</#if>
	</#list>
	<#assign activityCode = b_page.getReqMap()["zw_f_activity_code"] >
	<#assign product_support=b_method.upClass("com.cmall.ordercenter.util.CouponUtil")>
		<#assign flag=product_support.getActivityStatus(activityCode)>
	<div class="control-group">
		<label class="control-label" for="">状态:</label>
		<div class="controls">
			<div class="control_book">${flag}</div>
		</div>
	</div>
</form>
</#macro>

<#-- 显示页的自动输出判断 -->
<#macro m_zapmacro_common_book_field e_field   e_page>
    <@m_zapmacro_common_field_show e_field e_page/>
</#macro>

<#-- 字段：显示专用 -->
<#macro m_zapmacro_common_field_show e_field e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote()+":" />
	      		<div class="control_book">
		      		<#if  e_field.getFieldTypeAid()=="104005019">
		      			<#list e_page.upDataSource(e_field) as e_key>
							<#if  e_field.getPageFieldValue()==e_key.getV()> ${e_key.getK()} </#if>
						</#list>
					<#elseif e_field.getFieldTypeAid()=="104005021">
						<#if  e_field.getPageFieldValue()!="">
							<div class="w_left w_w_100">
									<a href="${e_field.getPageFieldValue()}" target="_blank">
									<img src="${e_field.getPageFieldValue()}">
									</a>
							</div> 
						</#if>
		      		<#else>
		      			<#if (e_field.getFieldName() == "assign_trigger")>
		      				<#assign activityCode = b_page.getReqMap()["zw_f_activity_code"] >
		      			    <#assign product_support=b_method.upClass("com.cmall.ordercenter.util.CouponUtil")>
		      				<#assign assignTrigger=product_support.getActivityAssignTrigger(activityCode)>
							${assignTrigger}
						<#elseif (e_field.getFieldName() == "creator")>
							<#assign creator = e_field.getPageFieldValue()>
							${e_field.getPageFieldValue()?default("")}
		      			<#else>
		      				${e_field.getPageFieldValue()?default("")}
		      		    </#if>
		      		</#if>
	      		</div>
	<@m_zapmacro_common_field_end />
</#macro>

<#assign activity_code = b_page.getReqMap()["zw_f_activity_code"] >

<div class="zab_info_page_title  w_clear" style="height: 30px; clear: both; margin-left: 0px;margin-bottom: 15px;">
<span>商品维护</span>&nbsp;&nbsp;&nbsp;
	<a class="btn btn-success" href="javascript:void(0)" onclick="getEventStatus(1,this)" zapweb_attr_operate_id="7208db9fc668457b96ac8b833e983826"> 添加商品</a>	
	<input type="button" value="批量作废" onclick="getEventStatus(4,this)" class="btn btn-success" zapweb_attr_operate_id="390342a3b04f41dd8cd1991980ad4350">
	<input type="button" value="批量初始化" onclick="getEventStatus(5,this)" class="btn btn-success" zapweb_attr_operate_id="390342a3b04f41dd8cd1991980ad4350">
	<input zapweb_attr_operate_id="a74555e0ca144791b732b9ca6d255a15" class="btn btn-success" onclick="getEventStatus(7)" type="button" value="导入商品">
	<input type="button" value="导表作废" onclick="getEventStatus(8);" class="btn btn-success" zapweb_attr_operate_id="390342a3b04f41dd8cd1991980ad4350">
	
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="text" name="code" id="code" style="margin-bottom:0px" placeholder="商品编号查询">&nbsp;&nbsp;<input type="button" value="搜索" onclick="mohuSearch()" class="btn btn-success">
</div>
<#assign logData=b_method.upControlPage("page_chart_v_oc_activity_agent_product","zw_f_activity_code=${activity_code}&zw_p_size=-1").upChartData()>

<#assign  e_pageField=logData.getPageField() />
<#assign  e_pagedata=logData />

<table  class="table  table-condensed table-striped table-bordered table-hover" id="table11">
	<thead>
	    <tr>
	        <th><input name="delCheck" type="checkbox" id="checkAllOrNone" xuanzhong="1" value="123456"/></th>
	        <#list e_pagedata.getPageHead() as e_list>
		    	<#if (e_list_index > 0)>
		      	 <th style="text-align:center;">
		      	 	${e_list}
		      	 </th>
		      	</#if>
	      </#list>
	      <th colspan="3" width="20%" style="text-align:center;">	操作</th>
	    </tr>
  	</thead>
  	
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<#assign provideNumMap = product_support.getCouponTypeProvide(e_list[e_pageField?seq_index_of("coupon_type_code")]) />
			<tr>
				<td><input name="delCheck" type="checkbox"  value="${e_list[2]}" data-product="${e_list[e_pageField?seq_index_of("produt_code")]}"/></td>
	  		 <#list e_list as e>
  				<#if (e_index >0)>
  				<td> 
  					<#if e_pageField[e_index]=="discount">
  						<#if e_list[e_pageField?seq_index_of("money_type")] == "折扣券">
			      			${e?default("")}%			      			
		      			<#else>
		      				<!--金额券时折扣券为空-->
		      			</#if>
	  			   <#else>
	  				    ${e?default("")}
	  			   </#if>
	      		</td>
	      		</#if>
	      	</#list>
		      	<td colspan="3" style="vertical-align:middle; text-align:center;">
					<a class="btn btn-link" href="javascript:void(0)" onclick="getEventStatus(6,this,'${e_list[2]}','${e_list[0]}')">修改</a>
					<a class="btn btn-link" href="javascript:void(0)" onclick="getEventStatus(2,this,'${e_list[2]}','${e_list[0]}')">作废</a>
					<a class="btn btn-link" href="javascript:void(0)" onclick="getEventStatus(3,this,'${e_list[2]}')">初始化</a>
				</td>
	      	</tr>
	 	</#list>
		</tbody>
</table>
<script type="text/javascript">
$(function(){
	$("#checkAllOrNone").click(function(){
	   //判断是否有过查询动作，如果没有，按原来的逻辑走
		var y = $("input[name='delCheck'][xuanzhong=1] ");
		var length = y.length;
	    $("tbody input[name='delCheck']").each(function(){
	    	if(length==1){//没有查询动作
	    		this.checked=$("#checkAllOrNone").is(':checked');
	    	}else{
	    		var x = $(this).attr("xuanzhong");
		    	if(x==1){
			         this.checked=$("#checkAllOrNone").is(':checked');
		    	}
	    	}
	    	
	    }); 
   });
});

function mohuSearch(){
	$("#table11 tr").show()
	var codeValue = $("#code").val()
	var mytable = document.getElementById('table11');
		
	$("table tr").each(function() {
	
			$($(this).children('th:eq(0)').children("input")).attr("xuanzhong",'1');
            $($(this).children('th:eq(0)').children("input")).attr("checked",false);
	
            $($(this).children('td:eq(0)').children("input")).attr("xuanzhong",'1');
            $($(this).children('td:eq(0)').children("input")).attr("checked",false);
    });
    for(var i=1,rows=mytable.rows.length; i<rows; i++){
    	var proDelFlag = false;
    	code = mytable.rows[i].cells[2].innerHTML;
    	if(code.indexOf(codeValue) == -1){
	        proDelFlag = true;
	    }
      if(proDelFlag){
      	//隐藏当前行
      	$("#table11 tr").eq(i).hide();
      	//选中框改为不选中
      	var x = $("#table11 tr").eq(i).children("td").eq(0).children("input");
      	$(x).attr("xuanzhong",'0');
      }
    }
}

function getEventStatus(type,obj,product_code,uid) {
	if(type == 1){
		zapjs.f.window_box({
			id : 'as_productlist_productids',
			content : '<iframe src="../show/page_chart_v_cf_pc_productinfo_multiSelect?zw_f_seller_code=SI2003&zw_s_iframe_select_source=as_productlist_productids&zw_s_iframe_select_page=page_chart_v_cf_pc_productinfo_multiSelect&zw_s_iframe_max_select=0&zw_s_iframe_select_callback=parent.saveProduct" frameborder="0" style="width:100%;height:500px;"></iframe>',
			width : '700',
			height : '550'
		});
	}else if(type == 2){
		var obj = {};
		obj.activity_code = "${activity_code}";
		obj.product_code = product_code;
		obj.do_type = 2;
		zapjs.zw.func_do(obj, '9b2b04c1ca9142afa13444103486077a', {obj: obj});
	}else if(type == 3){
		var obj = {};
		obj.activity_code = "${activity_code}";
		obj.product_code = product_code;
		obj.do_type = 3;
		zapjs.zw.func_do(obj, '9b2b04c1ca9142afa13444103486077a', {obj: obj});
	}else if(type == 4){
		var checkUid = "";
		$("tbody input[name='delCheck']").each(function(){
	        if(this.checked){
	        	checkUid+=this.value+",";
	        }
    	}); 
		if(checkUid.length <= 0){
			zapjs.f.message("你还没有选中任何栏目内容！");
			return false;
		}
		if(confirm("确定要批量作废选中数据吗？")){
			var obj = {};
			obj.activity_code = "${activity_code}";
			obj.product_code = checkUid;
			obj.do_type = 4;
			zapjs.zw.func_do(obj, '9b2b04c1ca9142afa13444103486077a', {obj: obj});
		}
	}else if(type == 5){
		var checkUid = "";
		$("tbody input[name='delCheck']").each(function(){
	        if(this.checked){
	        	checkUid+=this.value+",";
	        }
    	}); 
		if(checkUid.length <= 0){
			zapjs.f.message("你还没有选中任何栏目内容！");
			return false;
		}
		if(confirm("确定要批量初始化选中数据吗？")){
			var obj = {};
			obj.activity_code = "${activity_code}";
			obj.product_code = checkUid;
			obj.do_type = 5;
			zapjs.zw.func_do(obj, '9b2b04c1ca9142afa13444103486077a', {obj: obj});
		}
	}else if(type == 6){
		zapjs.f.window_box({
			id : 'as_productlist_productids',
			content : '<iframe src="../page/page_edit_v_oc_activity_agent_product?zw_f_uid='+uid+'" frameborder="0" style="width:100%;height:500px;"></iframe>',
			width : '700',
			height : '550'
		});
	}else if(type == 7){
		zapadmin.window_url('../show/page_import_fxProduct?zw_f_event_code=${activity_code}&zw_f_do_type=1')
	}else if(type == 8){
		zapadmin.window_url('../show/page_import_fxProduct?zw_f_event_code=${activity_code}&zw_f_do_type=2')
	}	
}
/**
 * 
 * @param sId 
 * @param sVal 商品编号
 * @param a 商品名称
 * @param b
 * @param c
 * @returns {Boolean}
 */
function saveProduct(sId, sVal,a,b,c) {
	var obj = {};
	obj.activity_code = "${activity_code}";
	obj.product_code = sVal;
	zapjs.zw.func_do(obj, 'b3ff775161ad11eaabac005056165069', {obj: obj});
	zapjs.f.window_close(sId);
} 
</script>