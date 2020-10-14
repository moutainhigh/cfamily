<div class="zw_page_common_inquire">
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_inquire b_page />
	
	<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate() "116001009" />
</form>
</div>
<hr/>
<#--
-->
	<#assign nav_code = b_page.getReqMap()["zw_f_nav_code"]?default("") >
	<#assign nav_name = b_page.getReqMap()["zw_f_nav_name"]?default("") >

<#assign columnTypeParentCode = "449747160001" ><#-- 栏目类型 -->
<#assign isReleaseParentCode = "44974625" ><#-- 是否发布 -->
<#assign columnService=b_method.upClass("com.cmall.familyhas.service.HomeColumnService")>
<#assign defineColumnTypeMap=columnService.getColumnTypeCodeRELName(columnTypeParentCode ) >
<#assign defineReleaseMap=columnService.getColumnTypeCodeRELName(isReleaseParentCode ) >

<#assign e_pagedata=b_page.upChartData()>
<div class="zw_page_common_data">

<#--
-->
<input type="hidden" value="${nav_code}" id="zw_f_nav_code" name="zw_f_nav_code"> 

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	        <#if (e_list_index <= 14)&&(e_list_index > 1)>
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
	  		 	<#if (e_index <= 14)&&(e_index > 1)>
		      		<td>
			      		<#if e_index=12>
			      			<#if defineColumnTypeMap[e_list[4]] = "4497471600010010" || defineColumnTypeMap[e_list[4]] = "4497471600010011" || defineColumnTypeMap[e_list[4]] = "4497471600010016"><#-- TV直播和闪购 过滤 不能维护内容 -->
			      			<#--<#if e_list[4] = "TV直播" || e_list[4] = "闪购">-->
			      			<#else>
			      				${e?default("")}
			      			</#if>
		  			    <#elseif e_index=13>
		  			    	<#if defineReleaseMap[e_list[9]] = "449746250001">
		  			    		<input class="btn btn-small" type="button" value="取消发布" onclick="zapjs.zw.func_do(this, null, { zw_f_uid : '${e_list[0]}'});" zapweb_attr_operate_id="5421d3ed57a44d6b9dbd189b6821ec4e">
			      			<#else>
			      				<input class="btn btn-small" type="button" value="发布" onclick="zapjs.zw.func_do(this, null, { zw_f_uid : '${e_list[0]}'});" zapweb_attr_operate_id="5421d3ed57a44d6b9dbd189b6821ec4e">
			      			</#if>
			      	   <#elseif e_index=14>
			      			<#if defineColumnTypeMap[e_list[4]] = "4497471600010001" || defineColumnTypeMap[e_list[4]] = "4497471600010002" || defineColumnTypeMap[e_list[4]] = "4497471600010004" || defineColumnTypeMap[e_list[4]] = "4497471600010012" || defineColumnTypeMap[e_list[4]] = "4497471600010010" || defineColumnTypeMap[e_list[4]] = "4497471600010011" || defineColumnTypeMap[e_list[4]] = "4497471600010006" ><#--轮播广告、一栏广告、导航栏、通知模板、TV直播、闪购、右两栏推荐 以上支持复制 功能-->
			      				${e?default("")}
			      			</#if>
		  			   <#else>
		  				    ${e?default("")}
		  			   </#if>
	      		   </td>
	      		</#if>
	      	</#list>
	      	</tr>
	 	</#list>
</tbody>
</table>
<@m_zapmacro_common_page_pagination b_page  e_pagedata />
</div>



<script type="text/javascript">
	$(function(){
		var navCode = $("#zw_f_nav_code").val();
        var arr = $(".btn-group a");   // 为添加按钮的herf添加值
        var href_ = $(arr[0]).attr("href") + "?zw_f_nav_code=" + navCode;
        $(arr[0]).attr("href" , href_);
        
        
        if("${nav_code}" != "" && "${nav_name}" != ""){
	        $(".zab_page_default_header_title")[0].innerText = "首页导航【${nav_name}】所属版式维护列表";
        }
        
    })

</script>








