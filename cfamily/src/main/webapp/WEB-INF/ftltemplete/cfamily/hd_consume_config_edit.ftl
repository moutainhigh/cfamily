<#assign uuid = b_page.getReqMap()["zw_f_uid"]>
<#assign e = b_method.upClass("com.cmall.familyhas.webfunc.HdConsumeConfigEdit")>

<script>
	$(function(){
	    var entity = ${e.getEntityByUid(uuid)};
	    $("#uid").val(entity.uid); 
	    $("#zid").val(entity.zid); 
	    if(entity.flag == 'success'){
		    $("#min_use").val(entity.min_use)
		    $("#max_percent").val(entity.max_percent.split(".")[0])
		    $("#ratio").val(entity.ratio);
		    var seller_type = entity.seller_type;
		    var arr = seller_type.split(",");
		    for(var i = 0 ; i < arr.length ; i ++){
		    	var s = 'seller' + arr[i]; 
		    	$("#seller" + arr[i])[0].checked="checked";
		    }
	    }
	})
</script>

<style type="text/css">
    .table-show,td{
    	border-bottom: solid #808080 1px;
        border-spacing: 10px;
        border-collapse: collapse;
        padding-top: 25px;
        padding-bottom: 25px;
    }
     .table-show td{
        width: 150px;
    }
    input[type=checkbox]{
        margin: 0px;
    }
</style>
<@m_zapmacro_common_page_edit b_page />
<#macro m_zapmacro_common_page_edit e_page>
	
	<form class="form-horizontal" method="POST" >
		<input id='zid' name="zid" type="hidden" value="">
		<input id='uid' name="uid" type="hidden" value="">
		 <div class="csb_cfamily_addproduct_item">
		    <table  class="table-show ">
		        <tbody>
			        <tr id="first_tr" >
			            <td>
			                商户类型 
			            </td>
			            <td>
			                <input id="seller4497478100050001" name="seller4497478100050001" type="checkbox" value="4497478100050001@普通商户">普通商户
			            </td>
			            <td>
			                <input id="seller4497478100050002" name="seller4497478100050002" type="checkbox" value="4497478100050002@跨境商户">跨境商户
			            </td>
			            <td>
			                <input id="seller4497478100050003" name="seller4497478100050003" type="checkbox" value="4497478100050003@跨境直邮">跨境直邮
			            </td>
			            <td>
			                <input id="seller4497478100050004" name="seller4497478100050004" type="checkbox" value="4497478100050004@平台入驻">平台入驻
			            </td>
			            <td>
			                <input id="seller4497478100050000" name="seller4497478100050000" type="checkbox" value="4497478100050000@LD商品">LD商品
			            </td>
			        </tr>
			        
			        <tr>
			            <td>
			                最小使用
			            </td>
			            <td colspan=5>
			                <input id="min_use" name="min_use" value="" width="200px"> 惠豆<span style="color:red"> (只能使用最小使用的整数倍)</span>
			            </td>
			        </tr>
			        
			        <tr>
			            <td>
			                最大不超过单笔订单的
			            </td>
			            <td colspan=5>
			                <input id="max_percent" name="max_percent" value="" width="200px"> % <span style="color:red"> (百分比不支持小数)</span>
			            </td>
			        </tr>
			        
			        <tr>
			            <td>
			                一个惠豆等于
			            </td>
			            <td colspan=5>
			                <input id="ratio" name="ratio" value="" width="200px"> 元 <span style="color:red"> (请输入正数)</span>
			            </td>
			        </tr>
		        </tbody>
		    </table>
		</div>
		<div style="margin-top: 25px">
		<!--
			zapweb_attr_operate_id="b84a367ba2374924b84f2b999e157850" 这里指向
			select * from zapdata.zw_operate where page_code like '%v_fh_hd_consume_config%'  所对应的uid
		-->
			<input type="button" class="btn  btn-success" value="修  改" style="width:100px;" zapweb_attr_operate_id="b84a367ba2374924b84f2b999e157850" onclick="zapjs.zw.func_edit(this)">
		</div>
	</form>
</#macro>



