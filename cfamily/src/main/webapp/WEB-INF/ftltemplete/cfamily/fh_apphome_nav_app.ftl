<div class="zw_page_common_inquire">
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_inquire b_page />
	
	<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate() "116001009" />
</form>
</div>
<hr/>
<#--做个下拉选过滤-->
<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_select   e_field    e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	<#if e_field.getPageFieldName()="zw_f_column_type">
	    <select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
	      					<option value="">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSource(e_field) as e_key>
                        <#if e_key.getV()="4497471600010001"||e_key.getV()="4497471600010002"||e_key.getV()="4497471600010004"||e_key.getV()="4497471600010008"||e_key.getV()="4497471600010010"||e_key.getV()="4497471600010011"||e_key.getV()="4497471600010013"||e_key.getV()="4497471600010014"
                        ||e_key.getV()="4497471600010022"||e_key.getV()="4497471600010024"||e_key.getV()="4497471600010020"||e_key.getV()="4497471600010016"||e_key.getV()="4497471600010025"||e_key.getV()="4497471600010026"||e_key.getV()="4497471600010027"
                        ||e_key.getV()="4497471600010028"||e_key.getV()="4497471600010029"||e_key.getV()="4497471600010030"||e_key.getV()="4497471600010031"||e_key.getV()="4497471600010035"||e_key.getV()="4497471600010036"||e_key.getV()="4497471600010037">
                        <option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
                        </#if>
						
					</#list>
	      		</select>
	<#else>
		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
	      					<option value="">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSource(e_field) as e_key>

						<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
					</#list>
	      		</select>
	</#if>
	      		
	<@m_zapmacro_common_field_end />
</#macro>


	<#assign nav_code = b_page.getReqMap()["zw_f_nav_code"]?default("") >
	<#assign nav_name = b_page.getReqMap()["zw_f_nav_name"]?default("") >
	<#assign nav_type = '4497471600100004' >

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
	        <#if (e_list_index <= 17)&&(e_list_index > 1)&&(e_list_index != 10)>
		      	<th>
		      		${e_list}
		      	</th>
	        	<#if (e_list_index = 2)>
	        		<th>
		      			栏目编号
		      		</th>
	        	</#if>
	      	</#if>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	  		 	<#if (e_index <= 17)&&(e_index > 1)&&(e_index != 10)>
		      		<td style="text-align:center">
			      		<#if e_index=14>

			      			<#if defineColumnTypeMap[e_list[4]] != "4497471600010001" && defineColumnTypeMap[e_list[4]] != "4497471600010004" && defineColumnTypeMap[e_list[4]] != "4497471600010020" && defineColumnTypeMap[e_list[4]] != "4497471600010013"&& defineColumnTypeMap[e_list[4]] != "4497471600010014"&& defineColumnTypeMap[e_list[4]] != "4497471600010002"&& defineColumnTypeMap[e_list[4]] != "4497471600010008"&& defineColumnTypeMap[e_list[4]] != "4497471600010025" && defineColumnTypeMap[e_list[4]] != "4497471600010027"&& defineColumnTypeMap[e_list[4]] != "4497471600010031"&& defineColumnTypeMap[e_list[4]] != "4497471600010035"><#-- 轮播广告和视频播放模板能维护内容 -->
			      			<#--<#if e_list[4] = "TV直播" || e_list[4] = "闪购">-->
			      			<#else>
			      				${e?default("")}
			      			</#if>
		  			    <#elseif e_index=15>
		  			    	<#if defineReleaseMap[e_list[9]] = "449746250001">
		  			    		<input class="btn btn-small" type="button" value="取消发布" onclick="zapjs.zw.func_do(this, null, { zw_f_uid : '${e_list[0]}'});" zapweb_attr_operate_id="5421d3ed57a44d6b9dbd189b6821ec4e">
			      			<#else>
			      				<input class="btn btn-small" type="button" value="发布" onclick="zapjs.zw.func_do(this, null, { zw_f_uid : '${e_list[0]}'});" zapweb_attr_operate_id="5421d3ed57a44d6b9dbd189b6821ec4e">
			      			</#if>
		      			<#elseif e_index=16>
		      			 	<#if defineColumnTypeMap[e_list[4]] = "4497471600010019">
		      			 	<#else>
		      			 		${e?default("")}
		      			 	</#if>
			  			<#else>
			  				${e?default("")}
			  			</#if>
	      		   </td>
	      		    <#if e_index=2>
	  		 			<td style="text-align:center">
	  		 				${e_list[1]}
	  		 			</td>
	  		 		</#if>
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
		// 隐藏展示类型查询字段
		$('#zw_f_view_type').parent().parent().hide();
		var navCode = "${nav_code}";
		if(navCode==''){
			navCode = 'NAV171020100002';
		}
        var arr = $(".btn-group a");   // 为添加按钮的herf添加值
        var href_ = $(arr[0]).attr("href") + "?zw_f_nav_code=" + navCode + "&zw_f_nav_type=4497471600100004";
        $(arr[0]).attr("href" , href_);
        
        
        if("${nav_code}" != "" && "${nav_name}" != ""){
	        var nav_name = "${nav_name}";
	        nav_name = decodeURIComponent(escape(nav_name));
	        $(".zab_page_default_header_title")[0].innerText = "首页导航【"+nav_name+"】所属版式维护列表";
        }
        
        
        var trArr = $("tbody>tr");   
        for(var i = 0 ; i < trArr.length; i ++){
        	var tr_ = trArr[i];
        	var arr = tr_.children;
        	// 
        	//$(arr[11].children[0]).attr("href" , $(arr[11].children[0]).attr("href") + "&zw_f_nav_type=4497471600100004"+"&zw_f_nav_code=NAV171020100002");
        }
    })

</script>