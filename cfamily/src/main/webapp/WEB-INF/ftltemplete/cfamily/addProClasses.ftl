<@m_zapmacro_common_page_add b_page />






 
<script>

    $(function(){
     $.ajax({
		type:'post',
		dataType:'json',
		data:{},
		url:'../../cfamily/jsonapi/com_cmall_familyhas_api_CheckExistedData?api_key=betafamilyhas&uid=',
		success:function(data){
		   if(data.resultList.length>0){
		     var array = new Array();
		     for(var i=0;i<data.resultList.length;i++){
		     var productParam = data.resultList[i].attribute_product;
		     if(productParam.indexOf(",")!=-1){
		        var temArray = new Array();
		        temArray = productParam.split(",");
		        for(var j=0;j<temArray.length;j++){
		        array.push(temArray[j]);
		        }
		     }
		     else{
		     array.push(productParam);
		         } 
            }
		   	 var paramElements = $("input:checkbox");
		   	 for(var i=0;i<paramElements.length;i++){
		   	 if($.inArray(paramElements[i].value,array)>-1){
		   	   $(paramElements[i]).prop("disabled",true);
		   	 }
		   	 }
		  
		   }
		}	
	});
    });

</script>
















