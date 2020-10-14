var contractTypeAdd = {
		addType:function(){
			$("#cp").append('<br><br><input id="" type="text" value="" zapweb_attr_regex_id="469923180002" name="zw_f_contract_type_name">');
		}
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/contractTypeAdd", function() {
		return contractTypeAdd;
	});
}
