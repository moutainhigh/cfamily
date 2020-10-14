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
	<#assign nav_type = b_page.getReqMap()["zw_f_nav_type"]?default("") >

<#assign columnTypeParentCode = "449747160001" ><#-- 栏目类型 -->
<#assign isReleaseParentCode = "44974625" ><#-- 是否发布 -->
<#assign columnService=b_method.upClass("com.cmall.familyhas.service.HomeColumnService")>
<#assign defineColumnTypeMap=columnService.getColumnTypeCodeRELName(columnTypeParentCode ) >
<#assign defineReleaseMap=columnService.getColumnTypeCodeRELName(isReleaseParentCode ) >

<#assign e_pagedata=b_page.upChartData()>

<#-- 微商城首页地址 -->
<#assign webAppHomeUrl=b_method.upConfig("cfamily.webAppHomeUrl") >

<#-- 页面按钮的自动输出 -->
<#macro m_zapmacro_common_auto_operate     e_list_operates  e_area_type>
	<div class="control-group">
    	<div class="controls">
    		<@m_zapmacro_common_show_operate e_list_operates  e_area_type "btn  btn-success" />
    		
    		 &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" class="btn  btn-success" onclick="yuLan(this);" value="预览" > 
            
            <#-- <a href="javascript:void(0);" onclick="yuLan(this);" class="btn btn-success"  target="_blank">预览</a>-->
       
    	</div>

	</div>
</#macro>

<#-- 按钮显示 -->
<#macro m_zapmacro_common_show_operate     e_list_operates  e_area_type  e_style_css >

			<#list e_list_operates as e>
    			<#if e.getAreaTypeAid()==e_area_type>
    		
	    			<#if e.getOperateTypeAid()=="116015010">
	    				<@m_zapmacro_common_operate_button e  e_style_css/>
	    			<#else>
	    				<@m_zapmacro_common_operate_link e  e_style_css/>
	    			</#if>
    		
    			</#if>
    		</#list>

</#macro>


<#--查询的自动输出判断 -->
<#macro m_zapmacro_common_auto_inquire e_page>
<#assign sub_nav_code = b_page.getReqMap()["zw_f_nav_code"]?default("") >

	<#list e_page.upInquireData() as e>
	
		<#if e.getQueryTypeAid()=="104009002">
			<@m_zapmacro_common_field_between e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009020">
			<@m_zapmacro_common_field_betweensfm e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009001">
			<#-- url专用  不显示 -->
	  	<#elseif  e.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e  e_page "请选择"/>
	  		
      
				
	  	<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
	  		
	  	</#if>
	  	
	</#list>

</#macro>





<div class="zw_page_common_data">

<#--
-->
<input type="hidden" value="${nav_code}" id="zw_f_nav_code" name="zw_f_nav_code"> 
<input type="hidden" value="${nav_name}" id="zw_f_nav_name" name="zw_f_nav_name"> 
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	        <#if (e_list_index <= 17)&&(e_list_index > 1)>
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
	  		 	<#if (e_index <= 17)&&(e_index > 1)>
		      		<td style="text-align:center">
			      		<#if e_index=15>
			      			<#if defineColumnTypeMap[e_list[4]] = "4497471600010010" || defineColumnTypeMap[e_list[4]] = "4497471600010011" || defineColumnTypeMap[e_list[4]] = "4497471600010016" || defineColumnTypeMap[e_list[4]] = "4497471600010019" || defineColumnTypeMap[e_list[4]] = "4497471600010023" || defineColumnTypeMap[e_list[4]] = "4497471600010022"|| defineColumnTypeMap[e_list[4]] = "4497471600010024"|| defineColumnTypeMap[e_list[4]] = "4497471600010034" || defineColumnTypeMap[e_list[4]] = "4497471600010029" || defineColumnTypeMap[e_list[4]] = "4497471600010030" || defineColumnTypeMap[e_list[4]] = "4497471600010036"><#-- TV直播和闪购 过滤 导航栏URL 不能维护内容 -->
			      			<#--<#if e_list[4] = "TV直播" || e_list[4] = "闪购">-->
			      			<#else>
			      				${e?default("")}
			      			</#if>
		  			    <#elseif e_index=16>
		  			    	<#if defineReleaseMap[e_list[9]] = "449746250001">
		  			    		<input class="btn btn-small" type="button" value="取消发布" onclick="zapjs.zw.func_do(this, null, { zw_f_uid : '${e_list[0]}'});" zapweb_attr_operate_id="5421d3ed57a44d6b9dbd189b6821ec4e">
			      			<#else>
			      				<input class="btn btn-small" type="button" value="发布" onclick="zapjs.zw.func_do(this, null, { zw_f_uid : '${e_list[0]}'});" zapweb_attr_operate_id="5421d3ed57a44d6b9dbd189b6821ec4e">
			      			</#if>
			      			
			      	   <#-- 轮播广告、一栏广告、导航栏、通知模板、TV直播、闪购、右两栏推荐 以上支持复制 功能  
				      	   <#elseif e_index=14>
				      			<#if defineColumnTypeMap[e_list[4]] = "4497471600010001" || defineColumnTypeMap[e_list[4]] = "4497471600010002" || defineColumnTypeMap[e_list[4]] = "4497471600010004" || defineColumnTypeMap[e_list[4]] = "4497471600010012" || defineColumnTypeMap[e_list[4]] = "4497471600010010" || defineColumnTypeMap[e_list[4]] = "4497471600010011" || defineColumnTypeMap[e_list[4]] = "4497471600010006" >
				      				${e?default("")}
				      			</#if>
			      			在首页导航维护 这个菜单下面的，不在使用以前的限制，全部都支持复制功能
		      			 -->
		      			<#elseif e_index=17>
		      			 	<#if defineColumnTypeMap[e_list[4]] = "4497471600010019">
		      			 	<#else>
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
        var href_ = $(arr[0]).attr("href") + "?zw_f_nav_code=" + navCode + "&zw_f_nav_type="+${nav_type};
        $(arr[0]).attr("href" , href_);
        
        
        if("${nav_code}" != "" && "${nav_name}" != ""){
	        $(".zab_page_default_header_title")[0].innerText = "首页导航【${nav_name}】所属版式维护列表";
        }
        
        
        var trArr = $("tbody>tr");   
        for(var i = 0 ; i < trArr.length; i ++){
        	var tr_ = trArr[i];
        	var arr = tr_.children;
        	// 
        	$(arr[15].children[0]).attr("href" , $(arr[15].children[0]).attr("href") + "&zw_f_nav_type=4497471600100001&zw_f_nav_code=${nav_code}");
        	//$(arr[14].children[0]).attr("href" , $(arr[14].children[0]).attr("href") + "&zw_f_nav_type=4497471600100002&zw_f_nav_code=${nav_code}");
        }
    })
    
    
function yuLan(e){
var time_point = $("#zw_f_time_point").val();
var column_name = $("#zw_f_column_name").val();
var column_type = $("#zw_f_column_type").val();
var nav_code = $("#zw_f_nav_code").val();
var nav_name = $("#zw_f_nav_name").val();
var view_type = $("#zw_f_view_type").val();

//window.location.href = "../web/template/previewColumn_real.html?time_point="+time_point+"&column_name="+column_name+"&column_type="+column_type+"&nav_code="+nav_code+"&nav_name="+nav_name;
//window.open("../web/template/previewColumn_real.html?time_point="+time_point+"&column_name="+column_name+"&column_type="+column_type+"&nav_code="+nav_code+"&nav_name="+nav_name,"_blank","toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes,left=500,top=30, width=360, height=600")
window.open("${webAppHomeUrl}?yulan="+nav_code+"&time_point="+time_point+"&column_name="+column_name+"&column_type="+column_type+"&nav_code="+nav_code+"&viewType="+view_type,"_blank","toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes,left=500,top=30, width=360, height=600")

}

</script>
