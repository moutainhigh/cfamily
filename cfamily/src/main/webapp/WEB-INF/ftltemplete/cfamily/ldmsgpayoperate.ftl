<#assign ldMsgPayService=b_method.upClass("com.cmall.familyhas.service.ldmsgpay.LdMsgPayService")>
<#assign puid = ldMsgPayService.getProductOperateUid() >
<#assign cuid = ldMsgPayService.getContantUsUid() >

<div class="" style="margin-bottom: 20px; height: 25px; margin-top: 10px;  color: #fff;">
	<input type="button" value="推荐商品配置" onclick="productSet();" style="" class="btn btn-success" >
	<input type="button" value="联系我们设置" onclick="signSet();"  style="" class="btn btn-success" >
</div>


<#-- 商品配置 -->
<div class="" id="product">
	<#assign operInfo = ldMsgPayService.getProductOperateInfo()>
	<#assign product_codes = ldMsgPayService.getProductCodes() >
	<#assign uid = ldMsgPayService.getProductOperateUid() >
	<#assign if_recommend =operInfo["if_recommend"]  >
	<#assign recommend_type =operInfo["recommend_type"]  >
	<form class="form-horizontal" method="POST" >
	
	<input type="hidden" id="zw_f_product_codes" name="zw_f_product_codes" value="${product_codes}">
	<input type="hidden" id="zw_f_uid" name="zw_f_uid" value="${uid}">
	<div class="control-group">
		<label class="control-label" for="zw_f_if_recommend"><span class='w_regex_need'>*</span>是否推荐：</label>
		<div class="controls">
	  		<select name="zw_f_if_recommend" id="zw_f_if_recommend" ">
					<option value="449748600001">是</option>
					<option value="449748600002">否</option>
	  		</select>
		</div>
	</div>
		  	
			
		
	
	<div class="control-group">
		<label class="control-label" for="zw_f_recommend_type"><span class='w_regex_need'>*</span>为您推荐：</label>
		<div class="controls">
	  		<select name="zw_f_recommend_type" id="zw_f_recommend_type" onchange="onchangeType()">
					<option value="4497471600660001">猜你喜欢</option>
					<option value="4497471600660002">配置商品</option>
	  		</select>
		</div>
	</div>  	
	<div id="productChoose">
	<div class="controls">
	<input class="btn" type="button" value="选择商品" onclick="msgpay.showShooseProductbox()">
	<input class="btn" type="button" value="导入" onclick="msgpay.showImportProductbox()">
	<input class="btn btn-success" type="button" value="批量删除" onclick="msgpay.delSelectedTableRow('productTable')">
	</div>
	<div class="controls">
		<table id="productTable" class="controls">
		    <thead>
	        <tr>
	            <th data-options="field:'skuCode',width:100">Code</th>
	            <th data-options="field:'skuName',width:500">Name</th>
	            <th data-options="field:'opt',width:100,align:'right'">Price</th>
	        </tr>
	    </thead>
		</table>
	</div>
	</div>
	<div class="control-group">
	
	<label class="control-label" for="zw_f_product_limit"></label>
	<div style="height: 20px;"></div>
	<div class="controls">
		<input type="button" class="btn  btn-success" zapweb_attr_operate_id="42d9d5ffe1c111eaabac005056165069"  onclick="zapjs.zw.func_add(this)"  value="提交" />
	</div>
	
	</div>
	</form>
	<script type="text/javascript">
	zapjs.f.require(["cfamily/js/msgpay"],
		function(a){
			msgpay.init({
				product_codes: '${product_codes}'
			});
		}
	);
	</script>
</div>

<#-- 联系我们设置 -->
<div class="" id="sign" style="display:none;">
	<#if cuid == ''>
		<#assign logData2=b_method.upControlPage("page_add_v_sc_contact_us_ldpay","")>
	<#else>
		<#assign logData2=b_method.upControlPage("page_edit_v_sc_contact_us_ldpay","zw_f_uid=${cuid}")>
	</#if>
	
	<@m_zapmacro_common_page_edit logData2 />
	
</div>

<script>
$(function(){
	var recommend_type = ${recommend_type};
	var if_recommend = ${if_recommend};
	$("#zw_f_recommend_type").val(recommend_type);
	$("#zw_f_if_recommend").val(if_recommend);
	onchangeType();
});

// 商品配置
function productSet(){
	$("#sign").hide();
	$("#product").show();
	
}

// 联系我们设置
function signSet(){
	$("#product").hide();
	$("#sign").show();
}

function onchangeType(){
	var recommend_type = $("#zw_f_recommend_type").val();
	if(recommend_type == "4497471600660001"){//猜你喜欢时，需要隐藏商品维护
		$("#productChoose").hide()
	}else{
		$("#productChoose").show()
	}
}

</script>

