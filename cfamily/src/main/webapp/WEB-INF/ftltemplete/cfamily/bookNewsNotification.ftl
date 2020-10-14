<#assign uid = b_page.getReqMap()["zw_f_uid"] >
<style>
.zab_page_default_header{margin-bottom:0;}
</style>
<script>
	require(['cfamily/js/bookNewsNotification'],
	
	function()
	{
		zapjs.f.ready(function()
			{
				bookNewsNotification.init('${uid}');
			}
		);
	}

);
</script>

<#assign newsNotificationService=b_method.upClass("com.cmall.familyhas.service.NewsNotificationService")>
<#assign infos  = newsNotificationService.getNewsInfos(uid)>

<link href="/cfamily/resources/cfamily/css/style.css" rel="stylesheet">
<script src="/cfamily/resources/cfamily/js/jquery-1.8.2.min.js"></script>
<#if infos??>

	<div class="head">
		<h4><span>${infos.noticeTopic}</span></h4>
		<p>发送时间：<span>${infos.publishTime}</span></p>
	</div>
	<#if infos.noticeType == "4497471600370001">
		   <div class="content">
				<div class="article">
					<h2>${infos.noticeTopic}</h2>
					<p class="txt">${infos.noticeContent}</p>
				</div>
			</div>   				
	<#elseif infos.noticeType == "4497471600370002">
	
		<div class="content">
	<div class="cf_list">
		<h2>${infos.noticeTopic}</h2>
		<table class="cf_tab">
			<thead>
				<tr>
					<td>订单编号</td>
					<td>下单时间</td>
					<td>sku编号</td>
					<td>商品名称</td>
					<td>下单成本</td>
					<td>商品售价</td>
					<td>处罚金额</td>
					<td>处罚原因</td>
				</tr>
			</thead>
			<tbody>
				<#list infos.list as e>
					<tr>
						<td>${e.orderCode}</td>
						<td>${e.orderTime}</td>
						<td>${e.productCode}</td>
						<td>${e.productName}</td>
						<td>${e.productCost?string('0.00')}</td>
						<td>${e.productSellPrice?string('0.00')}</td>
						<td>${e.punishMoney?string('0.00')}</td>
						<td>${e.punishReason}</td>
					</tr>
				</#list>
			</tbody>
		</table>
	</div>
</div>
	
	
	
		
	</#if>
	
</#if>





