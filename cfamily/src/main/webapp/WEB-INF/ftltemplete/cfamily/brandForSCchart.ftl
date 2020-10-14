
<script>

function saveBrand(oTag){
	var brand_code = $.trim(oTag.parentNode.parentNode.children[0].innerHTML);
	var category_code = $.trim(window.parent.$('#zw_page_common_tree').tree('getSelected').id);
	if(brand_code!=""&&category_code!=""){
		 var turnForm = document.createElement("form");  
    	document.body.appendChild(turnForm);
    	turnForm.method = 'POST';
 		turnForm.setAttribute("class","form-horizontal");
 		var n1 = document.createElement("input");
	    n1.setAttribute("id","zw_f_brand_code");
	    n1.setAttribute("name","zw_f_brand_code");
	    n1.setAttribute("type","hidden");
	    n1.setAttribute("value",brand_code);
	    turnForm.appendChild(n1);
	    var n2 = document.createElement("input");
	    n2.setAttribute("id","zw_f_category_code");
	    n2.setAttribute("name","zw_f_category_code");
	    n2.setAttribute("type","hidden");
	    n2.setAttribute("value",category_code);
	    turnForm.appendChild(n2);
	    var n3 = document.createElement("input");
	    n3.setAttribute("class","btn btn-small");
	    n3.setAttribute("style","display:none");
	    n3.setAttribute("type","button");
	    n3.setAttribute("onclick","zapjs.zw.func_add(this)");
	    n3.setAttribute("zapweb_attr_operate_id","7af4a460c7bb11e4b16b005056925439");
	    turnForm.appendChild(n3);	
	    zapjs.zw.func_call(n3);				
	}
	
}

</script>

<@m_zapmacro_common_page_chart b_page />