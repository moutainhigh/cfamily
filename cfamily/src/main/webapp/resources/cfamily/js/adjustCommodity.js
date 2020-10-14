var adjustCommodity = {
	init : function(t_type){
		
		//获取模版类型编号,对倒计时模板进行改名，列删除和列调整处理
		if(t_type == "449747500019" ){   
		//删除位置和开始时间列
			 $("#table11 tr :nth-child(3)").remove();
			 $("#table11 tr :nth-child(3)").remove();	
	    //对表格列进行重新排序追加并改名
			    var column1 = $("#table11 tr:eq(0) :nth-child(2)");
			   
			    var column2= $("#table11 tr:eq(0) :nth-child(6)");
			   
			    var column3 = $("#table11 tr:eq(0) :nth-child(7)");
			   
			    var column4 = $("#table11 tr:eq(0) :nth-child(5)");
			    
			    var column5 = $("#table11 tr:eq(0) :nth-child(3)");
			   
			    var column6 = $("#table11 tr:eq(0) :nth-child(4)");
			  
			    var column7 = $("#table11 tr:eq(0) :nth-child(8)");
			 
			    var column8 = $("#table11 tr:eq(0) :nth-child(9)");
			    
			    
			    $("#table11 tr:eq(0)").append(column1);
			    
			    $("#table11 tr:eq(0)").append(column2);
			   
			    $("#table11 tr:eq(0)").append(column3);
			   
			    $("#table11 tr:eq(0)").append(column4);
			    
			    $("#table11 tr:eq(0)").append(column5);
			   
			    $("#table11 tr:eq(0)").append(column6);
			    
			    $("#table11 tr:eq(0)").append(column7);
			  
			    $("#table11 tr:eq(0)").append(column8);
			
			
			    $("#table11 tr:eq(0) :nth-child(5)").text("模板背景图");
			    $("#table11 tr:eq(0) :nth-child(6)").text("目标时间");
			    
			    
		
			    for(var i=1;i<$("#table11 tr").length;i++){
			    	
				
				    var column1_ = $("#table11 tr:eq("+i+") :nth-child(2)");
				   
				    var column2_= $("#table11 tr:eq("+i+") :nth-child(6)");
				    
				    var column3_ = $("#table11 tr:eq("+i+") :nth-child(7)");
				   
				    var column4_ = $("#table11 tr:eq("+i+") :nth-child(5)");
				   
				    var column5_ = $("#table11 tr:eq("+i+") :nth-child(3)");
				   
				    var column6_ = $("#table11 tr:eq("+i+") :nth-child(4)");
				  
				    var column7_ = $("#table11 tr:eq("+i+") :nth-child(8)");
				  
				    var column8_ = $("#table11 tr:eq("+i+") :nth-child(9)");
				    
				  
				    $("#table11 tr:eq("+i+")").append(column1_);
				  
				    $("#table11 tr:eq("+i+")").append(column2_);
				   
				    $("#table11 tr:eq("+i+")").append(column3_);
				   
				    $("#table11 tr:eq("+i+")").append(column4_);
				   
				    $("#table11 tr:eq("+i+")").append(column5_);
				   
				    $("#table11 tr:eq("+i+")").append(column6_);
				   
				    $("#table11 tr:eq("+i+")").append(column7_);
				   
				    $("#table11 tr:eq("+i+")").append(column8_);
			    	
			    }
	   
	   // $("#table11 tr").append($("#table11 tr :nth-child(3)").clone(false));

	    

		}
	}
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/adjustCommodity", function() {
		return adjustCommodity;
	});
}

$(document).ready(function(){
	//切换分类时调用
	$("#zw_f_skip").change(function(){
		adjustCommodity.changeType();
	});
});
