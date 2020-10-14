<#-- 加价活动详细信息页面 -->
<#assign channel_uid= "${RequestParameters['zw_f_uid']}"/>
<div class="zab_info_page">

<div class="zab_info_page_title  w_clear">
<span>栏目信息</span>
</div>
<@m_zapmacro_common_page_book b_page />
<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<form class="form-horizontal" method="POST" >
	<#assign product_support=b_method.upClass("com.cmall.familyhas.service.apphome.GetChannelInfo")>
	<#assign prchType=product_support.getPrchType(channel_uid)>
	<input type="hidden" id="channel_uid" value="${RequestParameters['zw_f_uid']}"/>
	<div class="control-group">
		<label class="control-label" for="">栏目名称:</label>
		<div class="controls">
			<div class="control_book"> ${prchType.channel_name?if_exists} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">栏目类型:</label>
		<div class="controls">
			<div class="control_book"> ${prchType.type_name?if_exists} </div>
		</div>
	</div>
		<div class="control-group">
		<label class="control-label" for="">开始时间:</label>
		<div class="controls">
			<div class="control_book"> ${prchType.start_time?if_exists} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">结束时间:</label>
		<div class="controls">
			<div class="control_book"> ${prchType.end_time?if_exists} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">位置:</label>
		<div class="controls">
			<div class="control_book"> ${prchType.seq?if_exists} </div>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="">是否发布:</label>
		<div class="controls">
			<div class="control_book"> ${prchType.status_str?if_exists} </div>
			<input type="hidden" id="is_status" value="${prchType.status?if_exists}"/>
		</div>
	</div>
	

</form>
</#macro>
</br>


<div class="zab_info_page_title  w_clear">
<span>栏目内容列表</span><a  class="btn btn-small" href="page_add_v_apphome_exchange?zw_f_channel_uid=${RequestParameters['zw_f_uid']}" style="height:20px;margin-left:10px">新增栏目内容</a>
<a  class="btn btn-small" href="javascript:deleteAll()" style="height:20px;margin-left:10px">批量删除</a><a  class="btn btn-small" onclick="zapadmin.window_url('../show/page_import_v_fh_data_jifen?zw_f_template_number=PTN190808100005&zw_f_channel_uid=${RequestParameters['zw_f_uid']}');" href="javascript:void(0)" style="height:20px;margin-left:10px">批量新增</a>
<script type="text/javascript">
			
function closeImportWindow (flg,sId,msg) {

	zapjs.f.window_close(sId);
	if(flg == true) {
		zapadmin.model_message('导入商品成功！');
		zapjs.f.autorefresh();
	} else {
		zapadmin.model_message(msg);
		setTimeout("zapjs.f.autorefresh();",2000);
	}

}
			
			
</script>
</div>
<#assign channelDetails=b_method.upControlPage("page_chart_v_fh_apphome_channel_exchange","zw_f_channel_uid=" + channel_uid + "&" + "zw_p_size=-1").upChartData()>
<#assign list = channelDetails.getPageData()>
<#if (list?size= 1)>
	<input id="is_one" type="hidden" value="1"/>
<#else>
	<input id="is_one" type="hidden" value="2"/>
</#if>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	    	<th><input name="delCheck" type="checkbox" id="checkAllOrNone" value="123456"/></th>
	        <#list channelDetails.getPageHead() as e_list>
	        	<#if (e_list_index>0)>
			      	 <th>
			      	 	${e_list}
			      	 </th>
	        	</#if>
	      </#list>
	    </tr>
  	</thead>

  
	<tbody>
		<#list channelDetails.getPageData() as e_list>
			<tr class="column">
				<#assign uid = e_list[0]> 
			<td><input name="delCheck" type="checkbox"  value="${e_list[0]}"/> </td>
	  		 <#list e_list as e>
   				<#if (e_index>0)>
	      				<td> ${e?default("")} </td>
	      		</#if>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>

</div>
<script>
require(['cfamily/js/apphome/selectAll'],

function()
{
	$("#checkAllOrNone").click(function(){
	    $("tbody input[name='delCheck']").each(function(){
	         this.checked=$("#checkAllOrNone").is(':checked');
	    }); 
	});
}

);
function deleteAll(){
	 debugger;
	 var checkUid = "";
		$("tbody input[name='delCheck']").each(function(){
      if(this.checked){
   		checkUid+=this.value+",";
      }
 	 }); 
	if(checkUid.length <= 0){
		zapjs.f.message("你还没有选中任何栏目内容！");
		return;
	}
	if(confirm("确定要删除选中数据吗？")){
		var obj = {};
		zapjs.zw.func_do(obj, '94a93273b9254362b5f22c543052c630', { zw_f_uids: checkUid});
	}
	
};
$(function(){
    
   	var isRelease = $("#is_one").val();
   	var status = $("#is_status").val();
    var trArr = $(".column");   // 
    for(var i = 0 ; i < trArr.length; i ++){
    	var tr_ = trArr[i];
    	var arr = tr_.children;
    	if(isRelease == 1 && status == 2){
    		$(arr[8].children[0]).attr("type","hidden");
    		$(arr[8].children[0]).attr("onclick","");
    	}else{
    		$(arr[8].children[0]).val("删除");
    	}
    	var jf = arr[5].innerText*200;
    	arr[5].innerText=jf;
    	
    }
});
</script>
  