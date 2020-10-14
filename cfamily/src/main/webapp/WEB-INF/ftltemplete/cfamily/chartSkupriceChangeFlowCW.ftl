
<@m_zapmacro_common_page_chart b_page />

<#-- 列表页 -->
<#macro m_zapmacro_common_page_chart e_page>

	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
	</div>
	<hr/>
	
	<#local e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
	<@m_zapmacro_common_table e_pagedata />
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>
</#macro>

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	        <#if e_list_index!=0&&e_list_index!=14>
		    
		      	 <th>
		      	 	${e_list}
		      	 </th>
		    </#if>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
		
			<tr>
	  		 <#list e_list as e>
	  		    <#if e_index==0||e_index==14>
	  		    <#else>
	      		<td>
	      			<#if (e_list[0]=="1")&& e_index==13>
	      			  系统
	      			<#else>
	      				${e?default("")}
	      			</#if >
	      			
	      		</td>
	      		</#if>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
<input type=hidden  class="hideData" value = '${b_method.bConfig('systemcenter.maxExportNum')}'/>
</#macro>
<script>

var href_url = "";

$("[zapweb_attr_operate_id='bc8f820c26bc46289cdeecedbe8f76ae']").focus(function(){

setTimeout("href_qc()",500);

});

function href_qc(){
   href_url = $("#zapjs_f_id_window_box").children(":last").children(":last").attr("href");
   
   $("#zapjs_f_id_window_box").children(":last").children(":last").attr("href","javascript:href_pd()");
}

function href_pd(){
  var maxExportNum= $(".hideData").val();
  var numStr = $(".pagination_label").text();
  var num = parseInt(numStr.substring(1,numStr.length-1));
  if(num>maxExportNum){
  zapjs.f.modal({
		content : '导出数据过多,请不要超过'+maxExportNum+'条'
		});
	return;
  }else{
  window.open(href_url);
  }
  
}

</script>