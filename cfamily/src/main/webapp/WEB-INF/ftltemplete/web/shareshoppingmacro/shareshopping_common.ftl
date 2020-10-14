<#-- 资源文件路径 -->
<#assign ss_assign_resources="../../resources/shareshopping/" >
<#assign ss_assign_base_resources="../../resources/" >

<#macro ss_common_head e_title="" from="">
<!DOCTYPE html>
<html lang="en">
<head>
<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" />
<meta content="email=no" name="format-detection" />
<meta charset="UTF-8">
<title><#if e_title?? && e_title?length gt 0>${e_title}<#else>惠家有购物商城</#if></title>
<link  href="${ss_assign_resources}css/style.css" rel='stylesheet'>	
<script type="text/javascript" src="${ss_assign_resources}js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="${ss_assign_base_resources}zapjs/zapbase.js"></script>
<script type="text/javascript" src="${ss_assign_resources}js/cfamily.js"></script>
<#if from?? && from?length gt 0>
    	
</#if>
<style>
	.mark{display:none;background:#000; opacity:.4; width:100%; height:100%; position:fixed; left:0; top:0; z-index:9998;}
	/*tost效果*/
	#toastMessage,#toastMessage2{ top:50%; position:fixed; z-index:10000000000;}
	#toastMessage span,#toastMessage2 span{ display:block;  overflow:hidden}
	#toastMessage .sbg,#toastMessage2 .sbg{ background:#000; color:#fff;opacity:.8; z-index:1; width:100%; height:100%;position:absolute; left:0; top:0;border-radius:5px; -webkit-border-radius:5px; }
	#toastMessage .stxt,#toastMessage2 .stxt{padding:.8em 1em; font-size:15px; line-height:1.2em; z-index:2; color:#fff; position:relative}
	input{-webkit-appearance:none;appearance:none;-webkit-tap-highlight-color:rgba(0,0,0,0);}
	body{-webkit-user-select: none;} 
		
	</style>
</#macro>

<#macro ss_common_body body_class="">
</head>
<body class="${body_class}">
</#macro>


<#macro ss_common_footer>
</body>
</html>
</#macro>


<#-- a链接跳转页面 -->
<#macro ss_common_page_href e_href="">
 href="javascript:cgroup.href_page('${e_href}')"
</#macro>

<#-- 点击函数跳转页面 -->
<#macro ss_common_page_link e_href="">
 onclick="cgroup.href_page('${e_href}')"
</#macro>

<#-- a链接调用js -->
<#macro ss_common_page_a_func e_func="">
 href="javascript:${e_func}" 
</#macro>

<#-- click调用js -->
<#macro ss_common_page_click e_func="">
 onclick="${e_func}" 
</#macro>

<#macro ss_common_jscall e_js="">
<script type="text/javascript">
${e_js}
</script>

</#macro>

<#-- 实现placeholde提示 -->
<#macro ss_common_placeholder msg="" e_css="">
 value="${msg!}" onblur="if(this.value==''){this.value='${msg!}';this.style.color='#999999';}" onfocus="if(this.value=='${msg!}')this.value='';this.style.color='#333';" style="color: rgb(153, 153, 153);${e_css!}"
</#macro>

<#macro ss_common_user_defined_head>
<div class="top" id="top_id">
	<a <@ss_common_page_click "productdetail.closeHead(this)"/> class="btn-close">关闭</a>
    <div class="txt"><div><img src="${ss_assign_resources}img/share-logo.png" alt=""></div><span>您的好友<b id="parent_mobile_id"></b>送您<em>1次返现特权</em>,购买任一商品都可以使用哦~</span></div>
	<div class="btns"><span class='lspan'><a <@ss_common_page_click "openApp(1);"/>>打开APP</a></span><span class='rspan'><a <@ss_common_page_click "productdetail.getPrerogative();"/>>领取返现特权</a></span></div>
</div>
<header><a><span class="fl jt" style="display:none;" id="back_item_id">向左</span></a>商品详情</header>
</#macro>