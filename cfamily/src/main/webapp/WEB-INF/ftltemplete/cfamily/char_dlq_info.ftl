<#assign page_type = "1000" />
<#assign tv_number = b_page.getReqMap()["zw_f_tv_number"] />
<script type="text/javascript" >
	/*
	* flg 0:本窗口跳转; 1: 新窗口跳转;
	*/
	function forwardFunc(obj,url,flag){
		if(flag == 0) {
			if(url.indexOf('?') < 1) {
				url += "?zw_f_tv_number=${tv_number }&zw_f_page_type=${page_type }";
			} else {
				url += "&zw_f_tv_number=${tv_number }&zw_f_page_type=${page_type }";
			}
			document.location.href = url;
		} else {
			if(url.indexOf('?') < 1) {
				url += "?tvNumber=${tv_number }&p_type=${page_type }";
			} else {
				url += "&tvNumber=${tv_number }&p_type=${page_type }";
			}
			window.open(url);
		}
	}
	/*
	* flg 0:本窗口跳转; 1: 新窗口跳转;
	*/
	function forwardfun2(obj,url,flag){
		if(flag == 0) {
			
			if(url.indexOf('?') < 1) {
				url += "?zw_f_page_type=${page_type }&zw_f_tv_number=${tv_number }";
			} else {
				url += "&zw_f_page_type=${page_type }&zw_f_tv_number=${tv_number }";
			}
			document.location.href = url;
		} else {
			if(url.indexOf('?') < 1) {
				url += "?zw_f_page_type=${page_type }&zw_f_tv_number=${tv_number }";
			} else {
				url += "&zw_f_page_type=${page_type }&zw_f_tv_number=${tv_number }";
			}
			window.open(url);
		}
	}
</script>

<@m_zapmacro_common_page_chart b_page b_method tv_number/> 


<#-- 列表页 -->
<#macro m_zapmacro_common_page_chart e_page b_method tv_number>

	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
	</div>
	<hr/>
	
	<#local e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
	<@m_zapmacro_common_table e_pagedata b_method tv_number/>
	<#-- <@m_zapmacro_common_page_pagination e_page  e_pagedata /> -->
	
	</div>
</#macro>

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata b_method tv_number>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	        	 <#if e_list_index == 5 || e_list_index == 0 || e_list_index == 9>
	        	 
	        	 <#else>
			      	 <th>
			      	 	${e_list}
			      	 </th>
	        	 </#if>
	      </#list>
    	 <#-- 只为选择对应的电视节目 -->
    	 <#--  屏蔽   
    	 <th>电视节目</th>
    	 -->
	    </tr>
  	</thead>
  
	<tbody>
 		<#assign dlqService=b_method.upClass("com.cmall.familyhas.service.DLQService")>
		<#assign pageStatusMap = dlqService.getPageTvStatus(tv_number) >
		<#list e_pagedata.getPageData() as e_list>
	  		 
			<tr>
	  		 <#list e_list as e>
	  		 
	  		 	<#-- 区别是不是这个渠道关联的  -->
	  		 	<#assign relClsNums = dlqService.getPageRelClsOrTvList(tv_number) >
  		 		<#list relClsNums as clsNum>
		  		 	<#if e_list[1] == clsNum>
  		 				
			  		 	<#if e_index == 5 || e_index == 0 || e_index == 9>
			  		 	
			  		 	<#--  发布之后不能维护内容功能取消
			  		 	<#elseif e_list[5] == "1" && e_index == 9>
			  		 		<td></td>
			  		 	-->
			  		 	<#elseif e_index == 4>
			  		 		<#if (e_list[4]?length>0) >
			  		 			<td><img src="${e_list[4]}" style="width:150px;"></td>
			  		 		<#else>
			  		 			<td></td>
			  		 		</#if>
			  		 	<#elseif e_index == 12>
			  		 		<#-- 如果12：分享图片;  如果是否分享为“否”的话，则不展示图片 -->
			  		 		<#if (e_list[9] == "是") >
			  		 			<td>${e_list[12]}</td>
			  		 		<#else>
			  		 			<td></td>
			  		 		</#if>
			  		 	<#else>
				      		<td>
					  		 	<#if e_index == e_list?size -1>
		      						<#if pageStatusMap?? >
		      							<#if pageStatusMap[e_list[1]]?? && pageStatusMap[e_list[1]] == "1001" >
		      								<input class="btn btn-small" onclick="editStatus(this,'1')" type="button" attr_web_page_num="${e_list[1]}" value="取消发布">
		      							<#else>
		      								<input class="btn btn-small" onclick="editStatus(this,'0')" type="button" attr_web_page_num="${e_list[1]}" value="发布">
		      							</#if>
		      						<#else>
		      							<input class="btn btn-small" onclick="editStatus(this,'0')" type="button" attr_web_page_num="${e_list[1]}" value="发布">
		      						</#if>
					  		 		
					  		 	<#else>
					      			${e?default("")}
					  		 	</#if>
				      		</td>
			      		</#if>
		  		 	</#if>
  		 		</#list>
	  		 
	      	</#list>
      		<#-- 只为选择对应的电视节目 
      		<td>
      			<#assign dlqService=b_method.upClass("com.cmall.familyhas.service.DLQService")>
      			<#assign tvList = dlqService.getTvList() >
      			<select style="width:150px;" class="selectedTv">
      				<option value="">默认节目</option>
      				<#if tvList?? && (tvList?size > 0) >
      					<#list tvList as tv>
	      					<option value="${tv["tv_number"]}">${tv["tv_name"]}</option>
      					</#list> 
      				</#if>
      			</select>
      		</td>
      		-->
	      	</tr>
	 	</#list>
		</tbody>
</table>
</#macro>

<script type="text/javascript">
	
	//   发布/取消发布
	function editStatus(obje,curntStatus) {
	
		var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
		$(document.body).append(sModel);
		$('#zapjs_f_id_modal_message').html('<div class="w_p_20">您确认发布该条数据吗？</div>');
		var aButtons = [];
		aButtons.push({
			text : '是',
			handler : function() {
					$('#zapjs_f_id_modal_message').dialog('close');
					$('#zapjs_f_id_modal_message').remove();
					
					//处理
					var attr_web_page_num = $(obje).attr("attr_web_page_num");
					var obj = {};
					obj.paramType = "1006";
					obj.editStatus = curntStatus;
					obj.page_number = attr_web_page_num;
					obj.tv_number = "${tv_number }";
					zapjs.zw.api_call('com.cmall.familyhas.api.ApiDLQAddTools',obj,function(result) {
						if(result.resultCode==1){
							zapjs.f.autorefresh();
						}else{
							zapadmin.model_message('操作失败!!!');
						}
					});
				}
		},{
			text : '否',
			handler : function() {
					$('#zapjs_f_id_modal_message').dialog('close');
					$('#zapjs_f_id_modal_message').remove();
				}
		});
		
		$('#zapjs_f_id_modal_message').dialog({
			title : '提示消息',
			width : '400',
			resizable : true,
			closed : false,
			cache : false,
			modal : true,
			buttons : aButtons
		});
		
	}
	
</script>



