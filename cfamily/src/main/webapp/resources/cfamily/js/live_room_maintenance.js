var live_room_maintenance ={
		temp : {
			//基本信息
			liveRoomBaseInfo:{},
			//数据面板信息
			liveRoomDataInfo:{},
	        //直播控制信息
			liveControlInfo:{},
			productNum :0,
			leftProductNum:0,
			maxNum : 200,
			currentPage:1,
			productCode:"",
			productName :""
		},
		init:function (obj){
			if (!obj) {
			}else{
				live_room_maintenance.temp.liveRoomBaseInfo = obj.liveRoomBaseInfo;
				live_room_maintenance.temp.liveRoomDataInfo = obj.liveRoomDataInfo;
				live_room_maintenance.temp.liveControlInfo = obj.liveControlInfo;
				live_room_maintenance.temp.productNum = obj.liveRoomBaseInfo.productNum
				live_room_maintenance.temp.leftProductNum = live_room_maintenance.temp.maxNum-Number(obj.liveRoomBaseInfo.productNum);
			}
			//进行一系列赋值操作
			live_room_maintenance.initValue();
		},
		initValue:function(){
			live_room_maintenance.baseInfo_init();
			live_room_maintenance.initData(live_room_maintenance.temp.liveRoomBaseInfo.liveRoomId,1);			

		},
		baseInfo_init : function(){
			
			
			$('.zhibo_base').append('<p><span>直播间</span>'+live_room_maintenance.temp.liveRoomBaseInfo.liveTitle+'</p>'
					+'<p><span>直播时间</span>'+live_room_maintenance.temp.liveRoomBaseInfo.startTime+'--'+live_room_maintenance.temp.liveRoomBaseInfo.endTime+'</p>');

			$('.zhibo_sou').append('<span class="numClass">已导入'+live_room_maintenance.temp.productNum+'件商品，本场直播还可导入'+live_room_maintenance.temp.leftProductNum+'件</span>'
					   +'<div class="sou-right">'
					   +'<input type="text"  placeholder="搜索商品编号"  class="sou-input productCode"/>'
					   +'<input type="text"  placeholder="搜索关键词"  class="sou-input  productName"/>'
					   +'<input type="button" value="搜索" onclick="live_room_maintenance.search()" class="sou-button"/>'
					   +'<input type="button" onclick="live_room_maintenance.bachOrdersImport()" value="导入商品" class="sou-button1"/>'
					   +'<input type="button" onclick="live_room_maintenance.show_box()" value="选择商品" class="sou-button2"/>'
					   +'</div>');

		
		},
		bachOrdersImport:function() {
			// zapadmin.window_url('../show/page_liveroomproduct_import?liveRoomId='+live_room_maintenance.temp.liveRoomBaseInfo.liveRoomId)
				zapjs.f.window_box({
					id : 'liveroomproduct',
					content : '<iframe src="../show/page_liveroomproduct_import?liveRoomId='+live_room_maintenance.temp.liveRoomBaseInfo.liveRoomId+'" frameborder="0" style="width:100%;height:500px;"></iframe>',
					width : '700',
					height : '550'
				});
		},
		
		search : function() {
			live_room_maintenance.temp.productCode = $(".productCode").val();
			live_room_maintenance.temp.productName = $(".productName").val();
			live_room_maintenance.initData(live_room_maintenance.temp.liveRoomBaseInfo.liveRoomId,1);
			
		},
		show_box : function(sId) {
			//展示的列表页面
			var source='page_chart_v_pc_productinfo_for_liveroom';
		    var sId="liveRoom";
			zapjs.f.window_box({
				id : sId + 'chooseProduct_showbox',
				content : '<iframe src="../show/'+source+'?zw_f_seller_code=SI2003&zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=' + source + '&zw_s_iframe_max_select=200&zw_s_iframe_select_callback=parent.live_room_maintenance.addLiveRoomProduct" frameborder="0" style="width:100%;height:500px;"></iframe>',
				width : '700',
				height : '550'
			});
		},
		
		addLiveRoomProduct : function(productCodes) {
			var obj = {};
			obj.productCodes = productCodes;
			obj.liveRoomId = live_room_maintenance.temp.liveRoomBaseInfo.liveRoomId;
			zapjs.zw.api_call('com_cmall_familyhas_api_ApiForAddLiveRoomProduct',obj,function(result) {
				if(result.resultCode==1){
					live_room_maintenance.temp.productNum = result.totalNum;
					live_room_maintenance.temp.leftProductNum = result.leftNum;
					console.log($('.numClass').html());
					$('.numClass').html('已导入'+live_room_maintenance.temp.productNum+'件商品，本场直播还可导入'+live_room_maintenance.temp.leftProductNum+'件');
					live_room_maintenance.initData(live_room_maintenance.temp.liveRoomBaseInfo.liveRoomId,1);
					zapjs.f.window_close('liveRoomchooseProduct_showbox');
				}else{
					zapadmin.model_message(result.resultMessage);
				}
			});

		},
		setAllNo:function (){
		    var box = document.getElementById("checkboxSp");
            var loves = document.getElementsByName("checkbox_sp");
            if(box.checked == false){
                for (var i = 0; i < loves.length; i++) {
                    loves[i].checked = false;
                }
            }else{
                for (var i = 0; i < loves.length; i++) {
                    loves[i].checked = true;
                   
                    }
            }

        },
    	doData:function (doFlag,pCode){
    		var opt ={};
			opt.liveRoomId =live_room_maintenance.temp.liveRoomBaseInfo.liveRoomId;
			opt.pCode =pCode;
			opt.doFlag=doFlag;
			opt.pageNum = live_room_maintenance.temp.currentPage;
			if(doFlag=='batchDel'){
				var temCodes=[];
	            var loves = document.getElementsByName("checkbox_sp");
                for (var i = 0; i < loves.length; i++) {
                	if(loves[i].checked ==true){
                		temCodes.push(loves[i].value);
                	}
                 }
                if(temCodes.length==0){
                	zapadmin.model_message('请选择商品!');
                }else{
                	opt.pCode =temCodes.join(",");
                }
			}
			
			zapjs.zw.api_call('com_cmall_familyhas_api_ApiLiveRoomProductsOperate',opt,function(result) {
				if(result.resultCode==1){
					live_room_maintenance.temp.productNum = result.resultMessage;
					live_room_maintenance.temp.leftProductNum = live_room_maintenance.temp.maxNum-Number(result.resultMessage);			
					$('.numClass').html('已导入'+live_room_maintenance.temp.productNum+'件商品，本场直播还可导入'+live_room_maintenance.temp.leftProductNum+'件');
					live_room_maintenance.initData(live_room_maintenance.temp.liveRoomBaseInfo.liveRoomId,live_room_maintenance.temp.currentPage);					
				}else{
					zapadmin.model_message(result.resultMessage);
				}		
				
			})
			

        },
		initData : function(liveRoomId,pageNum){		
			var opt ={};
			opt.liveRoomId =liveRoomId;
			opt.pageNum =pageNum;
			opt.productName =live_room_maintenance.temp.productName;
			opt.productCode =live_room_maintenance.temp.productCode;
			live_room_maintenance.temp.currentPage = pageNum;
			zapjs.zw.api_call('com_cmall_familyhas_api_ApiGetLiveRoomProducts',opt,function(result) {
				if(result.resultCode==1){
					$('.zhibo_right2').html("");
					var temArray = [];	
					temArray.push('<div class="zhibo_biao"><table width="100%" border="1" cellspacing="0" cellpadding="0">'
							   +'<th scope="col"></th>'
							   +'<th scope="col">商品编码</th>'
							   +'<th scope="col">商品信息</th>'
							   +'<th scope="col">价格</th>'
							   +'<th scope="col">操作</th>'
							   +'</tr>');
					var objArray = result.productList;
					for(var p in objArray){
						var item = objArray[p];
						temArray.push('<tr><td><input type="checkbox" name="checkbox_sp" value='+item.product_code+'></td>'
								+'<td>'+item.product_code+'</td>'
							 	+'<td class="td_img">'
							 	+'<img src="'+item.product_picture+'" />'
							 	+''+item.product_name+''
							 	+'</td>'
							 	+'<td>￥'+item.product_price+' 元</td>'
							 	+'<td>'
							 	+'<span class="span-hei" onclick="live_room_maintenance.doData(\'upOne\',\''+item.product_code+'\')">上移</span>'
							 	+'<span class="span-hei" onclick="live_room_maintenance.doData(\'downOne\',\''+item.product_code+'\')">下移</span>'
							 	+'<span class="span-lan" onclick="live_room_maintenance.doData(\'topOne\',\''+item.product_code+'\')">置顶</span>'
							 	+'<span class="span-hong" onclick="live_room_maintenance.doData(\'delOne\',\''+item.product_code+'\')">删除</span>'
							 	+'</td></tr>');
					}
					temArray.push('</table></div>');
					
					temArray.push('<div class="page_zhibo"><div class="page-right">')
					if(pageNum!=1){
						temArray.push('<span class="shangye" onclick= "live_room_maintenance.initData(\''+live_room_maintenance.temp.liveRoomBaseInfo.liveRoomId+'\',\''+(Number(pageNum)-1)+'\')">上一页</span>')
					}
					var totalPage = result.totalPage;
					 if(totalPage-pageNum>=10){
					    	temArray.push('<span class="page_click">'+pageNum+'</span>')
					    	for(var i=1 ;i<10;i++){
					    		temArray.push('<span onclick= "live_room_maintenance.initData(\''+live_room_maintenance.temp.liveRoomBaseInfo.liveRoomId+'\',\''+(Number(pageNum)+i)+'\')">'+(Number(pageNum)+i)+'</span>')
					    	}
						}else{
							temArray.push('<span class="page_click">'+pageNum+'</span>')							
					    	for(var i=1 ;i<=(totalPage-pageNum);i++){
					    		temArray.push('<span onclick= "live_room_maintenance.initData(\''+live_room_maintenance.temp.liveRoomBaseInfo.liveRoomId+'\',\''+(Number(pageNum)+i)+'\')">'+(Number(pageNum)+i)+'</span>')
					    	}
						}
					    if(pageNum!=totalPage){
					    	temArray.push(' <span class="shangye" onclick= "live_room_maintenance.initData(\''+live_room_maintenance.temp.liveRoomBaseInfo.liveRoomId+'\',\''+(Number(pageNum)+1)+'\')">下一页</span>')
					    }
	
					    temArray.push('</div><div class="quanxuan" style="cursor:pointer"> <input type="checkbox" id="checkboxSp" onclick="live_room_maintenance.setAllNo()" /> <a onclick="live_room_maintenance.doData(\'batchDel\')" >全部删除 </a></div>');
					    
					    $('.zhibo_right2').html(temArray.join(''));  	

				}
					
			})
			},
			
	
	
};
