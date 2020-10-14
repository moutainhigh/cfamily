<#-- 百度统计宏
     domain_name:ichsy代表加入统计的域名为ichsy.com
                 beta代表加入统计的域名为beta的域名
                                              其他后续添加,敬请期待............
-->
<#macro m_common_tongji domain_name>
	<script type="text/javascript">
		var _hmt = _hmt || [];
		(function() {
		  	var hm = document.createElement("script");
		   <#if domain_name?? && (domain_name == "ichsy")>
	   			hm.src = "//hm.baidu.com/hm.js?4b558c865d49b9bedd272692d183c227";
		   <#elseif domain_name?? && (domain_name == "beta")>
		   		hm.src = "//hm.baidu.com/hm.js?4b558c865d49b9bedd272692d183c227";
		   <#else>
		   		hm.src = "//hm.baidu.com/hm.js?4b558c865d49b9bedd272692d183c227";
		   </#if>
		  var s = document.getElementsByTagName("script")[0]; 
		  s.parentNode.insertBefore(hm, s);
		})();
	</script>
</#macro>


























