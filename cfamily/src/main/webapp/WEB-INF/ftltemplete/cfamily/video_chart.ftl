<link rel="stylesheet" href="../resources/lib/datepicker/laydate.css" type="text/css"/>
<script type="text/javascript" src="../resources/lib/datepicker/dateTime.js"></script>
<link type="text/css" href="../resources/lib/datepicker/laydate.css" rel="stylesheet">
<#--活动设置页面-->
<@m_zapmacro_common_page_chart b_page />
<@m_common_html_js ["cfamily/js/video_chart.js"]/>
<#-- 列表页 -->
<#macro m_zapmacro_common_page_chart e_page>

	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
	</div>

	<hr/>

	<#assign e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
	<@m_zapmacro_common_table e_pagedata />

	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>
</#macro>

<#-- 分页 -->
<#macro m_zapmacro_common_page_pagination e_page  e_pagedata>
<div class="pagination">

	<#if (e_pagedata.getPageSize()>0)>
  <ul>
	<#if (e_pagedata.getPageIndex()>1)>
		<li><a href="<@m_zapmacro_common_page_pagination_href  e_page  e_pagedata   e_pagedata.getPageIndex()-1/>">上一页</a></li>
	<#else>
		<li class="disabled"><a>上一页</a></li>
	</#if>

    <#local max=(e_pagedata.getPageIndex()+5)>
    <#if (max>e_pagedata.getPageMax())><#local max=e_pagedata.getPageMax()></#if>
    
    <#local min=(e_pagedata.getPageIndex()-(5+e_pagedata.getPageIndex()-max))>
    <#if (min<1)><#local min=1></#if>
    
	<#list min..max as e>
		<#if (e>0)>
		<li <#if e==e_pagedata.getPageIndex()> class="active"</#if> ><a href="<@m_zapmacro_common_page_pagination_href  e_page  e_pagedata   e/>">${e}</a></li>
		</#if>
	</#list>

    <#if (e_pagedata.getPageIndex()<e_pagedata.getPageMax())>
		<li><a href="<@m_zapmacro_common_page_pagination_href  e_page  e_pagedata   e_pagedata.getPageIndex()+1/>">下一页</a></li>
	<#else>
		<li class="disabled"><a>下一页</a></li>
	</#if>
  </ul>
  </#if>
  
  
	<span class="pagination_label">共${e_pagedata.getPageCount()}条</span>
</div>

</#macro>

<#-- 查询区域 -->
<#macro m_zapmacro_common_page_inquire e_page>
	<form class="form-horizontal" method="POST" >
		<@m_zapmacro_common_auto_inquire e_page />
		<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate() "116001009" />

	</form>
</#macro>

<#-- 页面按钮的自动输出 -->
<#macro m_zapmacro_common_auto_operate     e_list_operates  e_area_type>
	<div class="control-group">
    	<div class="controls">
    		<@m_zapmacro_common_show_operate e_list_operates  e_area_type "btn  btn-success" />
    		<input type="button" class="btn  btn-success"  onclick="video_chart.batchDelVideo()"   value="批量删除">
    		<input type="button" class="btn  btn-success"  onclick="video_chart.batchChangeVideoStatus('4497471600600003')"   value="批量通过">
    		<input type="button" class="btn  btn-success"  onclick="video_chart.batchChangeVideoStatus('4497471600600002')"   value="批量驳回">
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
	<#list e_page.upInquireData() as e>
		<#if e.getQueryTypeAid()=="104009002">
			<@m_zapmacro_common_field_between e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009020">
			<@m_zapmacro_common_field_datesfm e e_page/>
		<#elseif e.getFieldName()=="channel_limit">
			<#-- 走特殊处理 -->
		<#elseif e.getQueryTypeAid()=="104009001">
			<#-- url专用  不显示 -->

	  	<#elseif  e.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e  e_page "请选择"/>
	  	<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
	  		
	  	</#if>
	  	
	</#list>
	
	<#--兼容form中input如果只有一个自动提交的情况-->
	<input style="display:none;"/>
</#macro>

<#-- 字段：日期 时分秒-->
<#macro m_zapmacro_common_field_datesfm e_field e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#-- onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})"  -->
		<#-- onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',realDateFmt:'yyyy-MM-dd HH-mm-ss',realTimeFmt:'HH:mm:ss HH-mm-ss'})"   -->
		从
		<input type="text"  autocomplete="off" onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})"  id="${e_field.getPageFieldName()}_zw_a_between_from" name="${e_field.getPageFieldName()}_zw_a_between_from" value="${e_page.upReqValue(e_page.upConst("126022001",e_field.getPageFieldName(),"between_from"))?default("")}">
		到
		<input type="text"  autocomplete="off" onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})"  id="${e_field.getPageFieldName()}_zw_a_between_to" name="${e_field.getPageFieldName()}_zw_a_between_to" value="${e_page.upReqValue(e_page.upConst("126022001",e_field.getPageFieldName(),"between_to"))?default("")}">
	<@m_zapmacro_common_field_end />
<#-- zapjs.f.require(['lib/datepicker/dateTime'],function(a){})-->
<#-- zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});-->
	<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){})" />
	  
</#macro>

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
<#assign e_pageField=e_pagedata.getPageField() />
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	   <tr>
	   	 <th><input id="item-all" type="checkbox" onclick = "video_chart.allSelect()"></th>   
	      <#list e_pagedata.getPageHead() as e_list>
	        	<#if (e_list_index <11 )>
	        	        <#if e_list_index==4>
	  			        <#elseif e_list_index==6>
	  			        <td>
				      			${e_list}
			      		</td>
			      		<td>
				      		审核日志
			      		</td>
	  			        <#else>
			      		<td>
				      			${e_list}
			      		</td>
			      		</#if>	
		      	 </#if>
	      </#list>
	        <th colspan="4" width="12%">	审核</th>
	        <th colspan="4" width="18%">	操作</th>
	    </tr>
  	</thead>
  
  	<#assign statusIndex = 7>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
			<td><input type='checkbox'  class='video_class' value= '${e_list[0]}'/>
	  		 <#list e_list as e>
	  			 <#if (e_index <11 )>
	  			        <#if e_index==3>
	  			        <td>
				      		<img onclick="big_img('${e?default("")}')" src="${e?default("")}" style="width:100px;cursor:pointer;" />
				      	</td>
	  			        <#elseif e_index==4>
	  			        <#elseif e_index==6>
	  			        <td>
				      			${e?default("")}
			      		</td>
			      		<td>
				      		<a href="page_chart_v_lc_video_approval_log?zw_f_video_code=${e_list[0]}" class="btn btn-link"  target="_blank">查看日志</a></a>	   
			      		</td>
			      		<#elseif e_index==8>
			      		 <td>
	      			        <a style="cursor: pointer;" onclick="playVideo('${e?default("")}')" >预览</a>
	      	             </td>
			      		
	  			        <#else>
			      		<td>
				      			${e?default("")}
			      		</td>
			      		</#if>			 
	      		 </#if>
	      	</#list>
	      	
	      	<td colspan="4" style="vertical-align:middle; text-align:center;">
				    <#if (e_list[statusIndex] = '待审核')>
					   <input  onclick="video_chart.changeVideoStatus('${e_list[0]}','4497471600600003')" type="button" class="btn btn-small" value="通过">
					   <input  onclick="video_chart.changeVideoStatus('${e_list[0]}','4497471600600002')" type="button" class="btn btn-small" value="驳回">
	                  
				    <#elseif (e_list[statusIndex]=='审核失败')>         
				        <input  onclick="video_chart.changeVideoStatus('${e_list[0]}','4497471600600003')" type="button" class="btn btn-small" value="通过">
				    <#elseif (e_list[statusIndex]=='已发布')>         
				 	    <input  onclick="video_chart.changeVideoStatus('${e_list[0]}','4497471600600002')" type="button" class="btn btn-small" value="驳回">
				 	<#elseif (e_list[statusIndex]=='暂停发布')>         
				 	    <input  onclick="video_chart.changeVideoStatus('${e_list[0]}','4497471600600002')" type="button" class="btn btn-small" value="驳回">
				   </#if>
			</td>
			
			<td colspan="4" style="vertical-align:middle; text-align:center;">
			    <#if (e_list[statusIndex] == '待审核')>
			     <input  onclick="video_chart.editVideo('${e_list[0]}','${e_list[1]}')" type="button" class="btn btn-small" value="编辑">
				 <input  onclick="video_chart.delVideo('${e_list[0]}')" type="button" class="btn btn-small" value="删除">
			    <#elseif (e_list[statusIndex]=='审核失败')>         
				 <input  onclick="video_chart.editVideo('${e_list[0]}','${e_list[1]}')" type="button" class="btn btn-small" value="编辑">
				 <input  onclick="video_chart.delVideo('${e_list[0]}')" type="button" class="btn btn-small" value="删除">
				<#elseif (e_list[statusIndex]=='已发布')>         
				 <input  onclick="video_chart.editVideo('${e_list[0]}','${e_list[1]}')" type="button" class="btn btn-small" value="编辑">
				 <input  onclick="video_chart.delVideo('${e_list[0]}')" type="button" class="btn btn-small" value="删除">
				 <input  onclick="video_chart.publishOrNot('${e_list[0]}','4497471600600004')" type="button" class="btn btn-small" value="暂停发布">
				<#elseif (e_list[statusIndex]=='暂停发布')>         
				 <input  onclick="video_chart.editVideo('${e_list[0]}','${e_list[1]}')" type="button" class="btn btn-small" value="编辑">
				 <input  onclick="video_chart.delVideo('${e_list[0]}')" type="button" class="btn btn-small" value="删除">
				 <input  onclick="video_chart.publishOrNot('${e_list[0]}','4497471600600003')" type="button" class="btn btn-small" value="发布">
				</#if>
		</td>
	      	</tr>
	 	</#list>
	</tbody>
</table>


<div id="img_fd" style="position: fixed;top: 0;left: 0;display:none;width: 100%;height: 100%;background-color: rgba(255,255,255,0.6);">
   <img src=""  style="max-width:600px;max-height:800px;position: absolute;top: 50%;left: 50%;transform: translate(-50%,-50%); display:block; margin:auto" />
</div>



<div id="video_fd" style="position: fixed;top: 0;left: 0;display:none;width: 100%;height: 100%;background-color: rgba(255,255,255,0.6);">
   <div class="video_h" style="position: absolute; top:50%;left:50%;transform: translate(-50%, -50%);">
       <video style="height:800px;border:5px solid #ccc" id="myvideo" controls="" autoplay="" src="" type="video/quicktime" class="media-document mac video">
       </video>
        <div id="video_close" onclick="" style="position: absolute;top: 20px;right: 20px;z-index: 1000;width: 40px;height: 40px;background-color: #fff;border-radius: 30px;overflow: hidden;padding: 10px;cursor: pointer;opacity: 0.7;">
               <img style="width:40px" src="../resources/shareshopping/img/btn-close2.png" />
         </div>
   </div>
</div>




 <script>
  function big_img(imgUrl){
        $("#img_fd img").attr("src",imgUrl);
		$("#img_fd").show();
	}
	
	$("#img_fd").on("click",function(){
	    $("#img_fd").hide();
	})
	
  function playVideo(link_url){
        $("#myvideo").attr("src",link_url);
		$("#video_fd").show();
	}	
	
	$("#video_close").on("click",function(){
	    $("#video_fd").hide();
	    $("#myvideo").attr("src",'');
	})
	
	

</script>
</#macro>




