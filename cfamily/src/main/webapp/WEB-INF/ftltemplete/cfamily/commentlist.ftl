
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
                <#if  (e_list_index > 0) >
                	<#if  (e_list_index = 18) >
                		<th>
	                    	评价审核
	                    </th>
                	<#elseif (e_list_index = 19)>
                	<#elseif (e_list_index = 21) >
                		<th>
	                    	图片视频素材审核
	                    </th>
                	<#elseif (e_list_index = 22)>
                	<#else>
	                    <th>
	                    	${e_list}
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
                    <#if e_index = 3>
                        <td>
                            <div class="w_left w_w_100">
                                <#if e=="">
                                  	  暂无图片
                                <#else>
                                    <a href="javascript:void(0)" onclick="zapadmin.window_url('page_chart_v_jl_nc_order_evaluation?zw_f_uid=${e_list[0]}')">查看</a>
                                </#if>
                            </div>
                        </td>
                    <#else>
                        <#if e_index = 9>
                            <td>
                         		<#if (e == "" || e?length lt 11)>
	                            	暂无手机号
	                            <#else>
	                            	${e[0..2] + "******" + e[9..10]}
	                            </#if>
                            </td>
                        </#if>
                        
                        <#if e_index = 4>
                            <td>
                         		<#if e == "">
	                            	暂无视频
	                            <#else>
	                            	<a href="javascript:void(0)" onclick="zapadmin.window_url('page_nc_order_evaluation_video?zw_f_ccvids=${e}')">查看</a>
	                            </#if>
                            </td>
                        </#if>
                     
                        <#if  (e_index > 0 && e_index != 9 && e_index != 4) >
                        	<#if  (e_index = 18) >
		                		<td>
			                    	 ${e_list[18]?default("")}
			                    	 ${e_list[19]?default("")}
			                    </td>
		                	<#elseif (e_index = 19)>
		                	<#elseif (e_index = 21) >
		                		<td>
		                    	 	${e_list[22]?default("")}
			                    	${e_list[21]?default("")}
			                    </td>
		                	<#elseif (e_index = 22)>
		                	<#elseif (e_index = 16)>
		                		<td>
			                		<#if (e == "0")>
			                			否
			                		<#else>
			                			是
			                		</#if>
	                            </td>
		                	<#else>
	                            <td>
	                                ${e?default("")}
	                            </td>
	                        </#if>
                        </#if>
                    </#if> 
                    
                </#list>
            </tr>
        </#list>
    </tbody>
</table>
</#macro>
