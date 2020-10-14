<#assign product_support=b_method.upClass("com.cmall.familyhas.service.ReturnProclamation")>
<#assign proclamation=product_support.getProclamation()>
<style>
		  div,ul,,dl,dd,dt,p{padding:0; margin:0;}
		  .lsub{float:left; display:inline; width:600px;}
		  .rsub{float:left; display:inline; width:50%;}
		   .rsub .notice{ border-right:1px solid #e1e1e1;margin:0 30px 0 0; font-size:14px; padding:0 30px; overflow: hidden;}
		  .rsub .notice .nt{ width:100%; font-size:20px; font-weight: bold; line-height:24px; color:#5f94d7; margin:0 0 30px; overflow: hidden;}
		  .rsub .notice .nc{width:100%; overflow: hidden;}
		  .rsub .notice .nc p{margin:0 0 10px;line-height:24px; width:100%; overflow: hidden;}
		  .rsub .notice .nc-c{padding:20px 0 0;}
		  .rsub .notice .nc-c p{ margin:0 0 15px;}
		  .rsub .notice .nc dl{margin:0 0 30px;}
		  .rsub .notice .nc dl dd, .rsub .notice .nc dl dt{ list-style:none; width:100%; margin:0 0 10px; line-height:22px; overflow: hidden;}
		  .rsub .notice .nc dl dd span{display:inline-block; margin:0 0 0 20px;}
		  .rsub .notice .nc dl dd p{margin:0;}
		  .rsub .notice .nc .red{color:#ec6565 }
		  .rsub .f16{font-size:16px;}
		  .rsub .notice img{display: inline-block; vertical-align: middle;}
</style>

<input type="button" class="btn" onclick="zapadmin.window_url('../show/page_zapadmin_window_change_password')" value="修改密码"/>

<div class="rsub">
		<div class="notice">
		<#if proclamation.proclamation_title?index_of("【紧急】")=0>
			<div class="nt" style="text-align:center">${proclamation.proclamation_title?replace("【紧急】","<font color=red>【紧急】</font>")}</div>
		<#else>
			<div class="nt" style="text-align:center">${proclamation.proclamation_title}</div>
		</#if>
			<div class="nc">
				${proclamation.proclamation_text}
			</div>
		</div>
</div>