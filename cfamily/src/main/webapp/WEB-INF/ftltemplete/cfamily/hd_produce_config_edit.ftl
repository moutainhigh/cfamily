<#assign uuid = b_page.getReqMap()["zw_f_uid"]>
<#assign e = b_method.upClass("com.cmall.familyhas.webfunc.HdProduceConfigEdit")>

<script>
	$(function(){
	    var entity = ${e.getEntityByUid(uuid)};
	    if(entity.flag == 'success'){
		    $("#uid").val(entity.uid); 
		    $("#zid").val(entity.zid); 
		    $("#percent").val(entity.percent)
		    var seller_type = entity.seller_type;
		    $("#seller_type").find("#" + seller_type).attr("selected",true);
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
			            <td >
			            	<select id="seller_type" name="seller_type">
								<option id="4497478100050000" value ="4497478100050000@LD商品">LD商品</option>
								<option id="4497478100050001" value ="4497478100050001@普通商户">普通商户</option>
								<option id="4497478100050002" value ="4497478100050002@跨境商户">跨境商户</option>
								<option id="4497478100050003" value ="4497478100050003@跨境直邮">跨境直邮</option>
								<option id="4497478100050004" value ="4497478100050004@平台入驻">平台入驻</option>
							</select>
			            </td> 
			            <td >
			            </td>
			            <td >
			            </td>
			        </tr>
			         
			        <tr>
			            <td>
			                返商品实付金额的
			            </td>
			            <td colspan=3>
			                <input id="percent" name="percent" value="" width="200px"> % <span style="color:red"> (百分比不支持小数)</span>
			            </td>
			        </tr>
		        </tbody>
		    </table>
		</div>
		<div style="margin-top: 25px"> 
			<input type="button" class="btn  btn-success" value="修  改" style="width:100px;" zapweb_attr_operate_id="0feffae096704030a4eac8dba889be05" onclick="zapjs.zw.func_edit(this)">
		</div>
	</form>
</#macro>



