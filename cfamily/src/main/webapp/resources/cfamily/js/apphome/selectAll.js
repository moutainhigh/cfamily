var selectAll ={
	
	init:function (){
		selectAll.init_value();
		 
	},
	
	init_value:function (){
		
		   var uniformPrice = $(".zab_info_page .control_book").eq(-3).html();
		   
		   if(uniformPrice && uniformPrice.indexOf('449746880001') > -1){
			   $(".zab_info_page .control_book").eq(-3).html("是");
		   }else{
			   $(".zab_info_page .control_book").eq(-3).html("否");
		   }
	},
	
	init_delete:function(){
		location.reload();
	}
	
	
};