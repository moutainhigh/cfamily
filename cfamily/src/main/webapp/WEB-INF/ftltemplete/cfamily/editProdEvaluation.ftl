
<#assign product_code = b_page.getReqMap()["product_code"] >
<#assign evaluation_uid = b_page.getReqMap()["evaluation_uid"] >
<#assign evaService=b_method.upClass("com.cmall.newscenter.service.EvaluationService")>
<#assign coverImg=evaService.getCoverImg(product_code) >

<@m_zapmacro_common_page_edit b_page />

<#-- 修改页 -->
<#macro m_zapmacro_common_page_edit e_page>
<form class="form-horizontal" id="edit_form" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upEditData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<#-- 页面按钮的自动输出 -->
<#macro m_zapmacro_common_auto_operate     e_list_operates  e_area_type>
	<div class="control-group">
    	<div class="controls">
    		<@m_zapmacro_common_show_operate e_list_operates  e_area_type "btn  btn-success" />
    	</div>
	</div>
</#macro>


<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
		<div class="control-group">
			<label class="control-label" for="">评价：</label>
			<a href="javascript:;" id="changeEva" class="btn btn-link"  target="">
				<input class="btn btn-success" type="button" value="更换评价"/>
			</a>
		</div>
		<input id="evaluation_uid" type="hidden" name="zw_f_evaluation_uid" value="${evaluation_uid}">
		<#list e_pagedata as e>
		  	<@m_zapmacro_common_auto_field e e_page/>
		  	
		</#list>
	</#if>
	
</#macro>

<style>
	.eva_img{margin-top: 10px; margin-right: 10px; float: left;position:relative;}
	.she_fm{ width:100%;text-align:center;margin:10px 0;}
	.she_fm button{ display:block;margin:auto} 
	.fengmian{ width:100%; font-size:20px; background:rgba(0,0,0,0.7);color:#fff;line-height:50px; text-align:center; position:absolute; top:65px; left:0;z-index:20}
	.shipinBQ{ width:180px; height:180px; font-size:20px;color:#fff;line-height:40px; text-align:center; position:absolute; top:0px; left:0;z-index:10}
	.img_class{width: 180px;height: 180px;}
	.shipinBQ img{ width:50px; height:50px; margin-top:65px;}

</style>


<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
	
		<#if e_field.getFieldTypeAid()=="104005008">
	  		<@m_zapmacro_common_field_hidden e_field/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<#if  e_field.getFieldName()=="order_assessment">
	  			<input id="order_assessment" type="hidden" name="zw_f_order_assessment" value="${e_field.getPageFieldValue()}">
	  	 		<div id="content_div" class="controls">
					<p class="">${e_field.getPageFieldValue()}</p>
				</div>
	  		<#elseif  e_field.getFieldName()=="product_code">
	  	 		<input id="product_code" type="hidden" name="zw_f_product_code" value="${e_field.getPageFieldValue()}">
	  		<#elseif  e_field.getFieldName()=="evaluation_uid">
		  		<div id="img_div" class="controls">
		  	 		<#assign photos=evaService.getEvaPhotos(e_field.getPageFieldValue()) >
		  	 		<#if photos != "">
						<#assign arr=("${photos}"?split("|"))>
				 		<#list arr as a>
				 			<#if a == coverImg>
						 		<div class="eva_img" >
						 			<p id="selectP_${a_index}" class="fengmian">封面图</p>
						 			<img src="${a}" class="img_class" alt="">
						 			<p class="she_fm"><button  id="selectImg_${a_index}" onclick="setCoverImg('${a}',${a_index})" disabled="disabled" class="btn bbb">设为封面</button></p>
						 		</div>
				 			<#else>
				 				<#if a != "">
							 		<div class="eva_img" >
							 			<p id="selectP_${a_index}" style="display: none;" class="fengmian">封面图</p>
							 			<img src="${a}" class="img_class" alt="">
							 			<p class="she_fm"><button  id="selectImg_${a_index}" onclick="setCoverImg('${a}',${a_index})" class="btn btn-success">设为封面</button></p>
							 		</div>
						 		</#if>
			  				</#if>
				 		</#list>
			 		</#if>
			 		<#assign ccImgs=evaService.getEvaCcImgs(e_field.getPageFieldValue()) >
			 		<#if ccImgs != "">
			 			<#if ccImgs == coverImg>
					 		<div class="eva_img" >
					 			<p id="selectP_ccImgs" class="fengmian">封面图</p>
					 			<div id="shipinBQ" class="shipinBQ"><img src="../resources/images/bf_03.png"></div>
					 			<img src="${ccImgs}" class="img_class" alt="">
					 			<p class="she_fm"><button  id="selectImg_ccImgs" onclick="setCoverImg('${ccImgs}','ccImgs')" disabled="disabled" class="btn bbb">设为封面</button></p>
					 		</div>
			 			<#else>		  		
					 		<div class="eva_img" >
					 			<p id="selectP_ccImgs" style="display: none;" class="fengmian">封面图</p>
					 			<div id="shipinBQ" class="shipinBQ"><img src="../resources/images/bf_03.png"></div>
					 			<img src="${ccImgs}" class="img_class" alt="">
					 			<p class="she_fm"><button  id="selectImg_ccImgs" onclick="setCoverImg('${ccImgs}','ccImgs')"  class="btn btn-success">设为封面</button></p>
					 		</div>
		  				</#if>
			 		</#if>
		 		</div>
	  		<#elseif  e_field.getFieldName()=="cover_img">
				<input id="cover_img" type="hidden" name="zw_f_cover_img" value="${e_field.getPageFieldValue()}">
		  	<#else>
		  		
		  	</#if>
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>


<#assign zw_p_index = b_page.getReqMap()["zw_p_index"]! >
<#if zw_p_index == "">
	<#assign zw_p_index = "1" >
</#if>
<#assign e_pageData1=b_method.upControlPage("page_chart_v_v_nc_order_evaluation_sh_good","product_code=${product_code}&evaluation_uid=${evaluation_uid}&zw_p_size=999&zw_p_index=${zw_p_index}")>
<#assign logData=e_pageData1.upChartData() >
	
	
	
	
<style>
.tc_div{ width:100%; z-index:9999999; height:100%;position:absolute;top:0;left:0;background-color:rgba(0,0,0,0.7)}
.tc_div1{ width:1080px; height:600px; background-color:#fff;position:absolute;top:50%;left:50%;margin-top:-300px; margin-left:-540px;}
.gb_btn{ position:absolute;top:10px;right:10px;}
.tc_div_t{ width:100%;border-bottom:1px solid #ccc; text-align:center;line-height:50px;height:50px;position:relative;}
.tc_div2{ width:1080px; height:550px;overflow-y:scroll;}
.pagination{ padding:0 5%;}
</style>

<div  class="zw_page_common_data tc_div" id="eva_list" style="display: none;">
   <div class="tc_div1">
        <div class="tc_div_t">商品评价列表   <button onclick="closeEvaList()" class="btn btn-success gb_btn">关闭</button></div>
        <div class="tc_div2">
		<@m_zapmacro_common_table logData />
		<@m_zapmacro_common_page_pagination b_page  logData />
	</div>
	</div>
</div>


<#-- 列表的自动输出 -->

<#macro m_zapmacro_common_table e_pagedata>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>商品评价列表</title>
		<style>
		    .zab_page_default_header_title{font-size: 22px;
                 border-bottom: 1px solid #094ab2;}
			*{ margin: 0;padding: 0; font-size: 12px;}
			ul li{ list-style: none;}
			.center_zt{ width: 96%;}
			.right_l{padding: 20px;}
			.mjdd_h{width: 100%;height: 50px;line-height: 50px; font-size: 22px; border-bottom: 1px solid #094ab2; font-weight: 200px;}
		    .zt_sxuan{ padding: 15px 0; padding-bottom: 0;}
		    .zt_intime{ width: 100px;height: 28px;border: 1px solid #ccc;border-radius: 5px; background: url(/cshop/resources/cshop/images/time_03.jpg) no-repeat; background-position: right center;}
		    .sel_zt{ height: 30px;border: 1px solid #ccc;border-radius: 5px; margin-left: 10px;}
		    .zt_ddbhin{ width: 200px;height: 28px;border: 1px solid #ccc;border-radius: 5px; }
		    .btn_zt{ display: inline-block; padding: 5px 15px; background-color: #094ab2; color: #fff; border-radius: 5px;}
		    .btn_zt1{ display: inline-block; line-height: 20px; padding: 5px 15px; background-color: #179bd6; color: #fff; border-radius: 5px;}
		    .btn_zt2{ display: inline-block; line-height: 20px; padding: 3px 15px; margin: 10px 0; background-color: #094ab2; color: #fff; border-radius: 5px;}
		    .pingjia_zt{ width: 100%; border: 1px solid #ccc; margin-top: 20px;}
		    .pingjia_p{height: 50px; background-color: #fafafa; line-height: 50px; padding: 0 20px;}
		    .pingjia_p span{ margin-left:5%; line-height: 50px; display: inline-block;}
		    .pingjia_p span:first-child{ margin-left: 0;}
		    .pingjia_p span img{ margin-left: 2px; vertical-align: middle; width: 23px;}
		    .mjping_zt{ position: relative; height: auto; overflow: hidden; padding: 20px 0;padding-left: 100px; padding-right: 20px; }
		    .mk_span{ position: absolute; top: 20px; left:20px; display: block;line-height: 25px;margin-left:0;}
		    .pp_pj{ line-height: 25px;position:relative}
		    .pp_pj button{position:absolute; top:-30px; right:0;z-index:10}
		    .pj_pimg .cover_pj{ margin-top: 10px; margin-right: 10px; float: left;position:relative;}
		    .pj_pimg .cover_pj img{width: 180px; height: 180px;}
		    .pj_pimg .cover_pj .shipinBQ img{ width:50px; height:50px; margin-top:65px;}
		    .page_zt{ border: 1px solid #ccc; border-radius: 5px; height: 30px ; margin-top: 20px; width: 366px; float: left;}
		    .page_zt ul li{ float: left; width: 40px; height: 30px;line-height: 30px;; text-align: center; border-right: 1px solid #ccc;}
		    .page_zt ul li:first-child{ width: 80px;}
		    .page_zt ul li:last-child{ width: 80px; border: none;}
		    .page_zt ul li a{ text-decoration: none; color:#094ab2 ;}
		    .page_zt ul li.zt_onli{ background-color:#f5f5f5 ; }
		    .page_zt ul li.zt_onli a{color: #999;}
		    .page_ul .gong_tiao{ float: right; display: block; margin-top: 20px; line-height: 30px;}
		    .page_ul{ width: 100%;height: auto;overflow: hidden;}
		    .mjpingj{ height: auto;overflow: hidden;padding: 30px 20px;}
		    
		</style>
		<link href="/cfamily/resources/cfamily/css/pictureViewer.css" rel="stylesheet">
	</head>
	<body>
		
		<div class="center_zt">
			<div class="right_l">
	
				<#list e_pagedata.getPageData() as e_list>
					<tr>
			      						
						<div class="pingjia_zt">
							<p class="pingjia_p">
								<#-- <span>${e_list[2]}</span> -->
								<span>评价星级：
								<#if e_list[4]="五星">
								<img src="/cfamily/resources/cfamily/images/xing_03.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_03.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_03.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_03.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_03.jpg" />
								<#elseif e_list[4]="四星">
								<img src="/cfamily/resources/cfamily/images/xing_03.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_03.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_03.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_03.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_05.jpg" />
								<#elseif e_list[4]="三星">
								<img src="/cfamily/resources/cfamily/images/xing_03.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_03.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_03.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_05.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_05.jpg" />
								<#elseif e_list[4]="二星">
								<img src="/cfamily/resources/cfamily/images/xing_03.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_03.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_05.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_05.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_05.jpg" />
								<#elseif e_list[4]="一星">
								<img src="/cfamily/resources/cfamily/images/xing_03.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_05.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_05.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_05.jpg" />
								<img src="/cfamily/resources/cfamily/images/xing_05.jpg" />
								</#if>
								</span>
								<span>评价时间：${e_list[1]}</span>
							</p>
							<div class="mjping_zt">
								 <span class="mk_span">评价内容：</span>
								 <div class="mjpingj">
								 	<p class="pp_pj">
								 	    ${e_list[0]}
								 	    <#if e_list[8] == evaluation_uid>
								 	    	<button id="img_${e_list[8]}" disabled="disabled" onclick="selectPj('${e_list[0]}','${e_list[3]}','${e_list[8]}','${e_list[9]}')" class="btn aaa">已选择</button>
								 	    <#else>
								 	    	<button id="img_${e_list[8]}" onclick="selectPj('${e_list[0]}','${e_list[3]}','${e_list[8]}','${e_list[9]}')" class="btn btn-success">选择评价</button>
								 	    </#if>
								 	</p>
								 	<div class="pj_pimg">
								 		<#if e_list[3] != "">
									 		<#assign arr=("${e_list[3]}"?split("|"))>
									 		<#list arr as a>
									 			<#if a != "">
											 		<div class="cover_pj">
											 			<img src="${a}" alt="">
											 		</div>
										 		</#if>
									 		</#list>
									 	</#if>
								 		<#if e_list[9] != "">
								 			<div class="cover_pj">
										 		<div id="shipinBQ" class="shipinBQ"><img src="../resources/images/bf_03.png"></div>
										 		<img src="${e_list[9]}" alt="">
										 	</div>
								 		</#if>
								 	</div>
								 </div>
							</div>
							
							
							<#if e_list[5]?length gt 0>
							<div class="mjping_zt" style="padding:0">
							   <p class="pingjia_p">
							         追加评价：
							     <span style="margin-left:42%">追加评价时间:&nbsp;&nbsp;${e_list[6]}</span>
							   </p>
								 <span class="mk_span"></span>
								 <div class="mjpingj">
								 	<p class="pp_pj" style="padding:15px;line-height:25px;">${e_list[5]}</p>
								 	<div class="pj_pimg" style="padding-left:100px;">
								 		<#if e_list[7] != "">
									 		<#assign arr=("${e_list[7]}"?split("|"))>
									 		<#list arr as a>
										 		<div class="cover_pj">
										 			<img src="${a}" alt="">
										 		</div>
									 		</#list>
									 	</#if>
								 	</div>				 	
								 </div>
							</div>
							</#if>
							
						</div>
		
			      	</tr>
			 	</#list>	
			</div>
		 </div>
		<input type="hidden" id="pinglunZ" value="" /> 
</body>
<script src="/cfamily/resources/cfamily/js/pictureViewer.js"></script>


</html>
</#macro>

<script>
$(document).ready(function(){

	// 更换评价
	$("#changeEva").click(function(){
		$("#eva_list").show();
		
	});
	
});


// 评价列表关闭按钮
function closeEvaList(){
	$("#eva_list").hide();
}

// 设为封面
function setCoverImg(img,index){
	$("#cover_img").val(img);
	$(".bbb").addClass("btn-success").removeClass("bbb").attr("disabled", false);
	$("#selectImg_"+index).attr("disabled", true).addClass("bbb").removeClass("btn-success");
	$(".fengmian").hide();
	$("#selectP_"+index).show();
}


// 选择评价
function selectPj(order_assessment,photos,eva_uid,ccImgs){
	$(".aaa").addClass("btn-success").removeClass("aaa").html("选择评价").attr("disabled", false);
	// 点击变为已选择,置灰且点击无效
	$("#img_"+eva_uid).addClass("aaa").removeClass("btn-success").html("已选择").attr("disabled", true);

	var imgHtml = "";
	if(photos != ""){
		// 评价图片
		var imgs = photos.split('|');
		$.each(imgs,function(index,img){
			if(index == 0){
				$("#cover_img").val(img);			
				imgHtml += '<div class="eva_img" >' +
					'<p id="selectP_'+index+'" class="fengmian">封面图</p>'+
		 			'<img src="' +img+ '" class="img_class" alt="">' +
		 			'<p class="she_fm"><button id="selectImg_'+index+'" onclick="setCoverImg(\''+img+'\','+index+')"  disabled="disabled" class="btn bbb">设为封面</button></p>' +
		 			'</div>';
			}else{
				if(img != ""){
					imgHtml += '<div class="eva_img" >' +
						'<p id="selectP_'+index+'" style="display: none;" class="fengmian">封面图</p>'+
			 			'<img src="' +img+ '" class="img_class" alt="">' +
			 			'<p class="she_fm"><button id="selectImg_'+index+'" onclick="setCoverImg(\''+img+'\','+index+')"  class="btn btn-success">设为封面</button></p>' +
			 			'</div>';
				}
			}
		});
	}
	if(ccImgs != ""){
		if(photos != ""){
			imgHtml += '<div class="eva_img" >' +
					'<p id="selectP_ccImgs" style="display: none;" class="fengmian">封面图</p>'+
					'<div id="shipinBQ" class="shipinBQ"><img src="../resources/images/bf_03.png"></div>' +
		 			'<img src="' +ccImgs+ '" class="img_class" alt="">' +
		 			'<p class="she_fm"><button id="selectImg_ccImgs" onclick="setCoverImg(\''+ccImgs+'\',\'ccImgs\')"  class="btn btn-success">设为封面</button></p>' +
		 			'</div>';
		}else{
			$("#cover_img").val(ccImgs);	
			imgHtml += '<div class="eva_img" >' +
					'<p id="selectP_ccImgs"  class="fengmian">封面图</p>'+
					'<div id="shipinBQ" class="shipinBQ"><img src="../resources/images/bf_03.png"></div>' +
		 			'<img src="' +ccImgs+ '" class="img_class" alt="">' +
		 			'<p class="she_fm"><button id="selectImg_ccImgs" onclick="setCoverImg(\''+ccImgs+'\',\'ccImgs\')"  disabled="disabled" class="btn bbb">设为封面</button></p>' +
		 			'</div>';
		}
	}
	$(".eva_img").remove();
	$("#img_div").append(imgHtml);
	
	// 评价内容
	$("#content_div").find('p').html(order_assessment);
	
	$("#order_assessment").val(order_assessment);
	$("#evaluation_uid").val(eva_uid);
	$("#eva_list").hide();
};

</script>
