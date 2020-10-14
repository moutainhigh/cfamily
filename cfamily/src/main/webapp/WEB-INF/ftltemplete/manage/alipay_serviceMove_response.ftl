<#assign param = RequestParameters['out_trade_no']?default("")>   <#--商户网站唯一订单号-->
<#assign param1 = RequestParameters['trade_no']?default("")>	<#--支付宝号交易号-->
<#assign param2 = RequestParameters['trade_status']?default("")>  <#--交易状态-->
<#assign param3 = RequestParameters['notify_time']?default("")>   <#--通知时间-->
<#assign param4 = RequestParameters['notify_type']?default("")>   <#--通知类型-->
<#assign param5 = RequestParameters['notify_id']?default("")>   <#--通知校验ID-->
<#assign param6 = RequestParameters['buyer_email']?default("")>   <#--买家支付宝账号-->
<#assign param7 = RequestParameters['seller_email']?default("")>   <#--卖家支付宝账号-->
<#assign param8 = RequestParameters['sign_type']?default("")>   <#--签名方式-->
<#assign param9 = RequestParameters['sign']?default("")>   <#--签名-->
<#assign param10 = RequestParameters['total_fee']?default("")>  <#--交易金额-->

<#assign param11 = "001">  <#--移动支付标示-->
<#assign param12 = RequestParameters['gmt_payment']?default("")>  <#--交易时间-->
<#assign param13 = RequestParameters['out_channel_inst']?default("")>  <#--支付渠道-->

<#assign alipay=b_method.upClass("com.cmall.familyhas.alipay.process.AlipayProcess")>
<#assign formstring= alipay.resultForm(param11)>

${formstring}