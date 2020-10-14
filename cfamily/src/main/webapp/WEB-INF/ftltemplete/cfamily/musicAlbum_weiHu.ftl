<div class="zab_info_page">
	<div class="zab_info_page_title  w_clear">
		<span>音乐相册模板</span>
	</div>
	<@m_zapmacro_common_page_book b_page />
</div>


<#assign template_id = b_page.getReqMap()["zw_f_template_id"] >

<div class="form-horizontal control-group">
	<a href="../page/page_add_v_hp_music_album_template_imgs?zw_f_template_id=${template_id}" class="btn btn-link"  target="_blank">
		<input id="selectType" class="btn btn-success" type="button" value="新增模板图片"/>
	</a>
</div>



<div class="zab_info_page_title  w_clear">
<span>音乐相册模板图片列表</span>&nbsp;&nbsp;&nbsp;
</div>


<#assign logData=b_method.upControlPage("page_chart_v_hp_music_album_template_imgs","zw_f_template_id=${template_id}&zw_p_size=-1").upChartData()>
<#assign  e_pageField=logData.getPageField() />
<#assign  e_pagedata=logData />

<@m_zapmacro_common_table e_pagedata />

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

<table  class="table  table-condensed table-striped table-bordered table-hover" id="table11">
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
	  		 		
                    <#if (e_index==1) && e?default("") != "">
				 		<td>
			      			<img src="${e?default("")}" style="width:150px;" />
			      		</td>	
				 	<#else>
						<td>
			  				${e?default("")}
			  			</td>
					</#if >
	
		      	</#list>
		      	</tr>
	 	</#list>
	</tbody>
</table>
</#macro>
