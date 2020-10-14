<#assign uid = b_page.getReqMap()["zw_f_uid"] >
<#assign live_room_id = b_page.getReqMap()["zw_f_live_room_id"] >

<#assign lService=b_method.upClass("com.cmall.familyhas.service.LiveVideoService")>
<#assign resultMap=lService.getLiveRoomInfos(live_room_id)>
<script>
require(['cfamily/js/liveRoom'],
function()
{
	zapjs.f.ready(function()
		{
			liveRoom.init(${resultMap});
		}
	);
}

);
</script>

	<head>
		<meta charset="UTF-8">
		<style>
			*{ margin: 0; padding: 0;}
			
		</style>
	</head>
	<body style="background-color: #F5F5F5;">
	 <div class="zhibi_div">
	 	
		<div class="zhibo_top zhibo_gy">
			
		</div>
		
	<div class="zhibo_center">
		<div class="zhibo_left zhibo_gy">
			
		</div>
		
		<div class="zhibo_right">
			<div class="zhibo_gy zhibo_right1">
				  <h1>直播控制</h1>
				  <div class="zhibo_kz">
				  	
				  </div>
				  <p class="zhibo_anniu">
				  	 
				  </p>
		    </div>

		    <div class="zhibo_gy zhibo_right2">
		    	
		    	
		    </div>
		    
	    </div>
	
	</div>	
	</body>


