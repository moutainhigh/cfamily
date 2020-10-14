<#assign phonenumber = b_page.getReqMap()["zw_f_phonenumber"] >
<#assign index = b_page.getReqMap()["zw_p_index"]!"1" >
<#assign flag = b_page.getReqMap()["flag"]!"" >
<#assign columnService=b_method.upClass("com.cmall.familyhas.service.MemberjifenService")>
<#assign e_pagedata=columnService.upChartData(flag,phonenumber,index,"50")>
<script>
require(['cfamily/js/memberinfo'],

function()
{
	zapjs.f.ready(function()
		{
			if('${flag}' == 'chuzhijin'){
				$("div[class='zab_page_default_header_title']").text("用户储值金查看")
			}else if('${flag}' == 'zancunkuan'){
				$("div[class='zab_page_default_header_title']").text("用户暂存款查看")
			}
		}
	);
}

);
</script>
<div class="zw_page_common_data">

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		      	<th>
		      		${e_list}
		      	</th>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
		      		<td>
		  				${e?default("")}
	      		   </td>
	      	</#list>
	      	</tr>
	 	</#list>
</tbody>
</table>
<@m_zapmacro_common_page_pagination b_page  e_pagedata />
</div>