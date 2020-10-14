<script>
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
<@m_zapmacro_common_page_add b_page />
<#macro m_zapmacro_common_page_add e_page>
	<form class="form-horizontal" method="POST" >
		 <div class="csb_cfamily_addproduct_item">
		    <table  class="table-show ">
		        <tbody>
			        <tr id="first_tr" >
			            <td>
			                商户类型
			            </td>
			            <td >
			            	<select name="seller_type">
								<option value ="4497478100050000@LD商品">LD商品</option>
								<option value ="4497478100050001@普通商户">普通商户</option>
								<option value ="4497478100050002@跨境商户">跨境商户</option>
								<option value ="4497478100050003@跨境直邮">跨境直邮</option>
								<option value ="4497478100050004@平台入驻">平台入驻</option>
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
			<input type="button" class="btn  btn-success" value="保  存" style="width:100px;" zapweb_attr_operate_id="83f666466c3c4280adf340df87e43f62" onclick="zapjs.zw.func_add(this)">
		</div>
	</form>
</#macro>



