var addSellerInfo = {
	
	init_page:function (){
		
		$('#zw_f_small_seller_code,#zw_f_small_seller_name').attr("readonly","readonly");
		
	}};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/addSellerInfo",["zapjs/zapjs","zapjs/zapjs.zw","zapadmin/js/zapadmin_single"],function() {
		return addSellerInfo;
	});
}
