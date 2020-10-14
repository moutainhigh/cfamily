
<#assign alipay=b_method.upClass("com.cmall.familyhas.alipay.process.WeChatProcess")>

<#assign param11 = "100">  <#--微信移动支付标示-->
<#assign formstring= alipay.resultForm(param11)>

${formstring}