
<@m_zapmacro_common_page_chart b_page />

<#-- 列表页  -->
<#macro m_zapmacro_common_page_chart e_page >


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
                <#if  (e_list_index > 1) >
                    <th>
                    	${e_list}
                    </th>
                </#if>
                <#if  (e_list_index = 10) >
                	<th>通过/驳回</th>
                </#if>
                
            </#list>
        </tr>
    </thead>

    <tbody>
        <#list e_pagedata.getPageData() as e_list>
            <tr>
                <#list e_list as e>
                	<#if  (e_index > 1) >
	                    <#if e_index = 4>
	                        <td>
	                            <div class="w_left w_w_100">
	                                <#if e=="">
	                                    	暂无图片
	                                <#else>
	                                    <a href="javascript:void(0)" onclick="zapadmin.window_url('page_chart_v_jl_nc_order_evaluation?zw_f_uid=${e_list[1]}')">查看</a>
	                                </#if>
	                            </div>
	                        </td>
	                    <#elseif e_index = 2>
	                            <td>
	                         		 <#if e == "">
		                            	暂无手机号
		                            <#else>
		                            	${e[0..2] + "******" + e[9..10]}
		                            </#if>
	                            </td>
	                        
	                    <#elseif e_index = 5>
	                            <td>
	                         		 <#if e == "">
		                            	暂无视频
		                            <#else>
		                            	<a href="javascript:void(0)" onclick="zapadmin.window_url('page_nc_order_evaluation_video?zw_f_ccvids=${e}')">查看</a>
		                            </#if>
	                            </td>
	                     
	                    <#else>
	                            <td>
	                                ${e?default("")}
	                            </td>
	                    </#if> 
                    </#if>
                    <#if  (e_index = 10) >
	                    <td>
		                    <#if e_list[10] = "已通过">
		  			    		<input class="btn btn-small" type="button" value="驳回" onclick="zapjs.zw.func_do(this, null, { zw_f_uid : '${e_list[0]}',zw_f_check_status:'${e_list[10]}' });" zapweb_attr_operate_id="a9a1f3814195446da547aed5166250f0">
			      			<#else>
			      				<input class="btn btn-small" type="button" value="通过" onclick="zapjs.zw.func_do(this, null, { zw_f_uid : '${e_list[0]}',zw_f_check_status:'${e_list[10]}' });" zapweb_attr_operate_id="a9a1f3814195446da547aed5166250f0">
			      			</#if>
	                    </td>
                    </#if>
                </#list>
            </tr>
        </#list>
    </tbody>
</table>
</#macro>
