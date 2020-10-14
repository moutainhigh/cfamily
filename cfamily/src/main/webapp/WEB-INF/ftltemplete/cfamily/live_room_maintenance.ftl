<#assign uid = b_page.getReqMap()["zw_f_uid"] >
<#assign live_room_id = b_page.getReqMap()["zw_f_live_room_id"] >

<#assign lService=b_method.upClass("com.cmall.familyhas.service.LiveVideoService")>
<#assign resultMap=lService.getLiveRoomInfos(live_room_id)>
<script>
require(['cfamily/js/live_room_maintenance'],
function()
{
	zapjs.f.ready(function()
		{
			live_room_maintenance.init(${resultMap});
		}
	);
}

);
</script>

	<head>
		<meta charset="UTF-8">
		<title>直播</title>
		<style>
			*{ margin: 0; padding: 0;}
			.zhibo_gy{background-color: #fff;box-shadow: #E5E5E5 0px 0px 30px 5px; padding: 10px;}
			.zhibi_div{ width: 1200px; padding: 0 30px;padding-bottom: 40px; margin: auto; height: auto; overflow: hidden;} 
			.zhibo_top{ padding: 20px; accelerator: 5px; font-size: 20px; margin-top: 20px; border-bottom: 1px solid #ccc;}
			.zhibo_top p span{ display: inline-block; width: 100px; color: #999;}
			.zhibo_top p{ font-size: 16px; line-height: 30px;}
		    .zhibo_right2 .zhibo_anniu{ height: auto; overflow: hidden; margin-top: 15px; margin-left: -10px;}
			.zhibo_right2 .zhibo_anniu span{width: 100px; height: 30px; display: block; margin-left: 10px; float: left; text-align: center; line-height: 30px; font-size: 14px; color: #222; border:1px solid #eee; border-radius: 5px;}
		    .zhibo_right2 .zhibo_anniu span:last-child{ float: right; height: 30px; display: block; cursor: pointer; margin-left: 10px; text-align: center; line-height: 30px; font-size: 14px; background: #007AFF; color: #FFF; border-radius: 5px;}
		    .zhibo_right2 h1{ font-size: 20px;}
		    .zhibo_right2 .zhibo_biao { margin-top: 15px; height: auto;overflow: hidden;}
		    .zhibo_right2 .zhibo_biao table{ font-size: 20px;border:none;border-collapse: collapse; background-color: #fff;}
		    .zhibo_biao table th{ height: 40px; background-color: #EEEEEE; font-size: 16px; padding: 0 10px;}
		    .zhibo_biao table tr td{padding: 10px; font-size: 16px; text-align: center;}
		    .zhibo_biao table tr td span{ font-size: 14px; color: #999;}
		    .zhibo_biao table tr .td_img{ text-align: left;}
		    .zhibo_biao table tr td img{ width: 80px; height: 80px; float: left; margin-right: 5px;}
		    .page_zhibo{height: 30px; overflow: hidden; padding: 20px; padding-left: 0;}
		    .page_zhibo span{ width:30px; margin-left: 10px; background-color: #fff; height: 25px; display: inline-block; cursor: pointer;  line-height: 25px; text-align: center; border-radius: 5px; border:1px solid #eee }
		    .page_zhibo .shangye{ width: 70px;}
		   .page_zhibo  .page_click{ background-color: #007AFF; color: #fff;} 
		   .zhibo_sou{ height: 40px; padding: 30px; padding-bottom: 20px; padding-left: 0; line-height: 40px;}
		   .sou-right{ float: right;}
		   .sou-right .sou-input{ height: 38px; width: 150px; border:1px solid #ccc; padding: 0 5px; border-radius: 3px;}
		   .sou-right .sou-button{ height: 38px; width: 50px;border-radius: 5px;background-color: #eee;  border:1px solid #ccc; cursor: pointer;}
		   .sou-right .sou-button1{ height: 38px; width: 100px;  background: #007AFF; margin-left: 10px; cursor: pointer;color: #FFF; border:1px solid #007AFF; border-radius: 5px;}
		    .sou-right .sou-button2{ height: 38px; width: 100px;border-radius: 5px; background-color: #eee; border:1px solid #ccc; cursor: pointer; margin-left: 30px;}
		    .page-right{ float: right; width: 500px; text-align: right;}
		    .quanxuan{ float: left; width: 100px;}
		    .zhibo_right2 input[type="checkbox"]{ width: 17px; height: 17px; }
		    .zhibo_biao table tr td span{ margin-left: 10px; cursor: pointer; font-size: 16px;}
		    .zhibo_biao table tr td span.span-hei{ color: #222}
		    .zhibo_biao table tr td span.span-lan{ color: #007AFF}
		    .zhibo_biao table tr td span.span-hong{ color: #EF2A1F}
		    
		</style>
	</head>
	<body style="background-color: #F5F5F5;">
	 <div class="zhibi_div">	
		<div class="zhibo_top zhibo_gy">
		直播间商品列表-维护
		</div>
		<div class="zhibo_top zhibo_gy zhibo_base">
		</div>		
		<div class="zhibo_sou">			
		</div>
	<div class="zhibo_center">	
		<div class="zhibo_right2">
	    </div>	    
     </div>	 
	</body>
	 <script type="text/javascript">
    </script>


