
<#assign alipay=b_method.upClass("com.cmall.familyhas.alipay.process.WeChatProcess")>

<#assign param11 = "101">  <#--微信WAP支付标示-->
<#assign formstring= alipay.resultForm(param11)>

${formstring}