var liveRoom ={
	temp : {
		//基本信息
		liveRoomBaseInfo:{},
		//数据面板信息
		liveRoomDataInfo:{},
        //直播控制信息
		liveControlInfo:{},
		buttonWord1 :"",
		buttonWord2:""
	},
	init:function (obj){
		if (!obj) {
		}else{
			liveRoom.temp.liveRoomBaseInfo = obj.liveRoomBaseInfo;
			liveRoom.temp.liveRoomDataInfo = obj.liveRoomDataInfo;
			liveRoom.temp.liveControlInfo = obj.liveControlInfo;
		}
		//进行一系列赋值操作
		liveRoom.initDate();
		liveRoom.initValue();
	},

	initDate : function(){},
	initValue:function(){
		liveRoom.baseInfo_init();
		liveRoom.productTable_init(liveRoom.temp.liveRoomBaseInfo.liveRoomId,1);
		
		
	},
	
	
	baseInfo_init : function(){
		
		$("head").append( ' <link href="/cfamily/resources/css/liveRoom.css" rel="stylesheet" type="text/css" />');
		
		$('.zhibo_top').append('<img src="'+liveRoom.temp.liveRoomBaseInfo.liveCoverPicture+'" class="img_top">'
				+'<p class="zhibo_title">'+liveRoom.temp.liveRoomBaseInfo.liveTitle+'</p>'
				+'<p>房间号：<span>'+liveRoom.temp.liveRoomBaseInfo.liveRoomId+'</span></p>'
				+'<p>开播时间：<span>'+liveRoom.temp.liveRoomBaseInfo.startTime+'</span></p>'
				+'<p>结束时间：<span>'+liveRoom.temp.liveRoomBaseInfo.endTime+'</span></p>'
				+'<p>主播：<span>'+liveRoom.temp.liveRoomBaseInfo.anchorNickname+'</span></p>'
				+'<p>手机号：<span>'+liveRoom.temp.liveRoomBaseInfo.anchorPhone+'</span></p>');

		$('.zhibo_left').append('<h1>数据面板</h1>'
				+'<p class="ph_p1">观看数据</p>'
				+'<p>观看人数：<span>'+liveRoom.temp.liveRoomDataInfo.seePepole+'</span></p>'
				+'<p>观看次数：<span>'+liveRoom.temp.liveRoomDataInfo.seeTimes+'</span></p>'
		/*		+'<p class="ph_p1">互动数据</p>'
				+'<p>点赞人数：<span>'+liveRoom.temp.liveRoomDataInfo.likePeople+'</span></p>'
				+'<p>点赞次数：<span>'+liveRoom.temp.liveRoomDataInfo.likeTimes+'</span></p>'
				+'<p>评论人数：<span>'+liveRoom.temp.liveRoomDataInfo.commentsPeople+'</span></p>'
				+'<p>评论次数：<span>'+liveRoom.temp.liveRoomDataInfo.commentsTimes+'</span></p>'*/
				+'<p class="ph_p1">商品数据</p>'
				+'<p>商品点击人数：<span>'+liveRoom.temp.liveRoomDataInfo.productClickPeople+'</span></p>'
				+'<p>商品点击次数：<span>'+liveRoom.temp.liveRoomDataInfo.productClickTimes+'</span></p>'
				+'<p>商品下单数：<span>'+liveRoom.temp.liveRoomDataInfo.orderNum+'</span></p>'
				+'<p>共计下单金额：<span>'+liveRoom.temp.liveRoomDataInfo.orderMoney+'</span></p>');
		
		liveRoom.status_init();
	
	},
	
    
	status_init:function(){
		$('.zhibo_kz').html("");
		$('.zhibo_kz').append(' <div class="zhibo_kz1"><p>当前直播状态</p><p>'+liveRoom.temp.liveControlInfo.liveStatus+'</p></div>'
				+'<div class="zhibo_kz2"><p>直播时长</p><p>'+liveRoom.temp.liveControlInfo.liveTime+'</p></div>'
				+'<div class="zhibo_kz2"><p>回放</p><p>'+liveRoom.temp.liveControlInfo.ifStopReplay+'</p></div>'
				+'<div class="zhibo_kz2"><p>评论</p><p>'+liveRoom.temp.liveControlInfo.ifStopComment+'</p></div>')
				
		if(liveRoom.temp.liveControlInfo.ifStopReplay=='开启'){
			liveRoom.temp.buttonWord1='关闭回放';
		}else{
			liveRoom.temp.buttonWord1='开启回放';
		}
		if(liveRoom.temp.liveControlInfo.ifStopComment=='开启'){
			liveRoom.temp.buttonWord2='禁止评论';
		}else{
			liveRoom.temp.buttonWord2='开启评论';
		}
		$('.zhibo_anniu').html("");
		if(liveRoom.temp.liveControlInfo.liveStatus=='已结束'){
			$('.zhibo_anniu').append('<span style="background: #d6e3f1" >'+liveRoom.temp.buttonWord2+'</span>'
				  	 +'<span onclick ="liveRoom.changeStatus(\'replayFlag\',\''+liveRoom.temp.liveRoomBaseInfo.liveRoomId+'\')">'+liveRoom.temp.buttonWord1+'</span>')
		}else{
			$('.zhibo_anniu').append('<span onclick = "liveRoom.changeStatus(\'commentFlag\',\''+liveRoom.temp.liveRoomBaseInfo.liveRoomId+'\')">'+liveRoom.temp.buttonWord2+'</span>'
				  	 +'<span onclick ="liveRoom.changeStatus(\'replayFlag\',\''+liveRoom.temp.liveRoomBaseInfo.liveRoomId+'\')">'+liveRoom.temp.buttonWord1+'</span>')
		}
		
		
	},
	
	changeStatus:function(flag,liveRoomId){
		var opt ={};
		opt.flag = flag;
		opt.liveRoomId =liveRoomId;
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiChangeCommentOrReplayStatus',opt,function(result) {
			if(result.resultCode==1){
				if(flag=='commentFlag'){
					liveRoom.temp.liveControlInfo.ifStopComment=result.resultMessage;
				}else{
					liveRoom.temp.liveControlInfo.ifStopReplay=result.resultMessage;
				}
				console.log(result.resultMessage);
				liveRoom.status_init();
			}

			
		});

	},
	
	productTable_init : function(liveRoomId,pageNum){		
		var opt ={};
		opt.liveRoomId =liveRoomId;
		opt.pageNum =pageNum;
		opt.productName ="";
		opt.productCode ="";
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiGetLiveRoomProducts',opt,function(result) {
			if(result.resultCode==1){
				$('.zhibo_right2').html("");
				var temArray = [];
				if(liveRoom.temp.liveControlInfo.liveStatus=='已结束'){
					temArray.push('<h1>推送控制</h1><p class="zhibo_anniu1"><span>商品</span> <span style="background: #d6e3f1" >添加商品</span></p>');
				}else{
					temArray.push('<h1>推送控制</h1><p class="zhibo_anniu1"><span>商品</span> <span onclick="liveRoom.show_box()" >添加商品</span></p>');
				}
				temArray.push('<div class="zhibo_biao"><table width="100%" border="1" cellspacing="0" cellpadding="0"><tr><th scope="col">商品编码</th><th scope="col">商品信息</th><th scope="col">点击次数</th></tr>');
				var objArray = result.productList;
				for(var p in objArray){
					var item = objArray[p];
					temArray.push('<tr><td>'+item.product_code+'</td><td class="td_img"><img src="'+item.product_picture+'" />'+item.product_name+'<br /><span>市场价 '+item.product_market_price+'元</span><br /><span>现价'+item.product_price+'元</span></td><td>'+item.clickNum+'</td></tr>');
				}
				temArray.push('</table></div>');
				
				temArray.push('<div class="page_zhibo">')
				if(pageNum!=1){
					temArray.push('<span class="shangye" onclick= "liveRoom.productTable_init(\''+liveRoom.temp.liveRoomBaseInfo.liveRoomId+'\',\''+(Number(pageNum)-1)+'\')">上一页</span>')
				}
				var totalPage = result.totalPage;
			    if(totalPage-pageNum>=10){
			    	temArray.push('<span class="page_click">'+pageNum+'</span>')
			    	for(var i=1 ;i<10;i++){
			    		temArray.push('<span onclick= "liveRoom.productTable_init(\''+liveRoom.temp.liveRoomBaseInfo.liveRoomId+'\',\''+(Number(pageNum)+i)+'\')">'+(Number(pageNum)+i)+'</span>')
			    	}
				}else{
					temArray.push('<span class="page_click">'+pageNum+'</span>')
			    	for(var i=1 ;i<=(totalPage-pageNum);i++){
			    		temArray.push('<span onclick= "liveRoom.productTable_init(\''+liveRoom.temp.liveRoomBaseInfo.liveRoomId+'\',\''+(Number(pageNum)+i)+'\')">'+(Number(pageNum)+i)+'</span>')
			    	}
				}
			    if(pageNum!=totalPage){
			    	temArray.push(' <span class="shangye" onclick= "liveRoom.productTable_init(\''+liveRoom.temp.liveRoomBaseInfo.liveRoomId+'\',\''+(Number(pageNum)+1)+'\')">下一页</span>')
			    }
			    temArray.push('</div>');
			    $('.zhibo_right2').html(temArray.join(''));  		 
			}
			
		});
		
},

	show_box : function(sId) {
		//展示的列表页面
		var source='page_chart_v_pc_productinfo_for_liveroom';
	    var sId="liveRoom";
		zapjs.f.window_box({
			id : sId + 'chooseProduct_showbox',
			content : '<iframe src="../show/'+source+'?zw_f_seller_code=SI2003&zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=' + source + '&zw_s_iframe_max_select=200&zw_s_iframe_select_callback=parent.liveRoom.addLiveRoomProduct" frameborder="0" style="width:100%;height:500px;"></iframe>',
			width : '700',
			height : '550'
		});
	},
	
	addLiveRoomProduct : function(productCodes) {
		var obj = {};
		obj.productCodes = productCodes;
		obj.liveRoomId = liveRoom.temp.liveRoomBaseInfo.liveRoomId;
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForAddLiveRoomProduct',obj,function(result) {
			if(result.resultCode==1){
				liveRoom.productTable_init(liveRoom.temp.liveRoomBaseInfo.liveRoomId,1);
				zapjs.f.window_close('liveRoomchooseProduct_showbox');
			}else{
				zapadmin.model_message(result.resultMessage);
			}
		});

	},

	
};
