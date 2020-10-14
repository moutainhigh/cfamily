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
			            <td>
			                <input id="seller-a" name="seller4497478100050001" type="checkbox" value="4497478100050001@普通商户">普通商户
			            </td>
			            <td>
			                <input id="seller-b" name="seller4497478100050002" type="checkbox" value="4497478100050002@跨境商户">跨境商户
			            </td>
			            <td>
			                <input id="seller-c" name="seller4497478100050003" type="checkbox" value="4497478100050003@跨境直邮">跨境直邮
			            </td>
			            <td>
			                <input id="seller-d" name="seller4497478100050004" type="checkbox" value="4497478100050004@平台入驻">平台入驻
			            </td>
			            <td>
			                <input id="seller-e" name="seller4497478100050000" type="checkbox" value="4497478100050000@LD商品">LD商品
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
			<input type="button" class="btn  btn-success" value="保  存" style="width:100px;" zapweb_attr_operate_id="58f18e98592945fcb52a354ab1c136a3" onclick="zapjs.zw.func_add(this)">
		</div>
	</form>
</#macro>



