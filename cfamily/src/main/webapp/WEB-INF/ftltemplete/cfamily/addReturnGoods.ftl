
<@m_zapmacro_common_page_add b_page />


<script>

	var zw_f_order_code = $('#zw_f_order_code');
	if(zw_f_order_code){
		zw_f_order_code.after('&nbsp;&nbsp;<a class="btn  btn-success" onclick="detail(this)" href="#">订单详情</a>');
	}

	function detail(obj){
	
		var order_code=$('#zw_f_order_code').val();
		if(order_code){
			//$(obj).attr("target","_blank");
			//$(obj).attr("href","page_book_v_cf_oc_orderinfo?zw_f_order_code="+order_code);
			//$(obj).click();
			
			
			window.open ('page_book_v_cf_oc_orderinfo?zw_f_order_code='+order_code);
		}
	}
</script>