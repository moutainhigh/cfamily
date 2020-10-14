<div class="zab_info_page">
	<div class="zab_info_page_title  w_clear">
		<span>栏目信息</span>
	</div>
	<@m_zapmacro_common_page_book b_page />
</div>


<#assign uid = b_page.getReqMap()["zw_f_uid"] >
<#assign t_code = b_page.getReqMap()["zw_f_template_number"] >
<#assign t_type = b_page.getReqMap()["zw_f_template_type"] >

<div class="form-horizontal control-group">
	<a href="../page/page_add_v_fh_data_commodity?zw_f_template_number=${t_code}&zw_f_template_type=${t_type}" class="btn btn-link"  target="_blank">
		<#if t_type="449747500001" || t_type="449747500006" || t_type="449747500007" || t_type="449747500008" || t_type="449747500015" || t_type="449747500016" || t_type="449747500018"|| t_type="449747500019" || t_type="449747500020" || t_type="449747500021" || t_type="449747500024">
			<input id="selectType" class="btn btn-success" type="button" value="新增栏目"/>
		<#elseif t_type="449747500023" || t_type="449747500026">
		    <input id="selectType" class="btn btn-success" type="button" value="新增拼团活动"/>
		<#else>
			<input id="selectType" class="btn btn-success" type="button" value="新增商品"/>
		</#if>
	</a>
	<#if t_type == "449747500009"><span style="color:red;">在有效的时间范围内，按位置(从小到大排列)排列只展示第一条数据 ！！！</span></#if>
	
	<#if  t_type="449747500002" || t_type="449747500003">
		<input id="selectType" class="btn btn-success" onclick="zapadmin.window_url('../show/page_import_v_fh_data_commodity?zw_f_template_number=${t_code}');" type="button" value="导入商品"/>
		<script type="text/javascript">
			function closeImportWindow (flg,sId,msg) {
				zapjs.f.window_close(sId);
				if(flg == true) {
					zapadmin.model_message('导入商品成功！');
					zapjs.f.autorefresh();
				} else {
					zapadmin.model_message(msg);
				}
			}
		</script>
	</#if>
	<#if  t_type="449747500022">
		<input id="selectType" class="btn btn-success" onclick="zapadmin.window_url('../show/page_import_v_fh_data_commodity3?zw_f_template_number=${t_code}');" type="button" value="导入商品"/>
		<script type="text/javascript">
			function closeImportWindow (flg,sId,msg) {
				zapjs.f.window_close(sId);
				if(flg == true) {
					zapadmin.model_message('导入商品成功！');
					zapjs.f.autorefresh();
				} else {
					zapadmin.model_message(msg);
				}
			}
		</script>
	</#if>
	
	<#if  t_type="449747500010" || t_type="449747500011">
		<input id="importProd" class="btn btn-success" onclick="zapadmin.window_url('../show/page_import_v_fh_data_commodity2?zw_f_template_number=${t_code}&zw_f_template_type=${t_type}&zw_f_uid=${uid}');" type="button" value="导入商品"/>
	</#if>
</div>

<#assign searchCommodity=b_method.upClass("com.cmall.familyhas.service.CommodityService")/>
<#assign e_timelist=searchCommodity.getDistinctTime(t_code)>

<div align="right">
	<select id="scode" name="scode" onchange="mohuSearch(${t_type})">
		<option value="">---请选择开始时间---</option>
		<#list e_timelist as data>
			<option value="${data}">${data}</option>
		</#list>
	</select>
</div>

<div class="zab_info_page_title  w_clear">
<#if t_type == "449747500018"||t_type == "449747500019"||t_type == "449747500021">
<span>关联模板列表</span>&nbsp;&nbsp;&nbsp;
<#else>
<span>关联商品列表</span>&nbsp;&nbsp;&nbsp;
</#if>
<#-- 判断是否为不可维护商品模板   此页面共3处判断 -->
<#if t_type != "449747500001" && t_type != "449747500007" && t_type != "449747500008" && t_type != "449747500013" && t_type != "449747500014" && t_type != "449747500018"&& t_type != "449747500019" && t_type != "449747500021" && t_type != "449747500025" && t_type != "449747500024">
<input id="batchDel" class="btn btn-small" type="button" value="批量删除">
</#if>
<#if t_type == "449747500002" || t_type == "449747500003"|| t_type == "449747500009"|| t_type == "449747500010"|| t_type == "449747500022">
<input class="btn btn-small" type="button" value="导表删除" onclick="show_import_windows()"/>
</#if>
</div>


<#assign logData=b_method.upControlPage("page_chart_v_fh_data_commodity","zw_f_template_number=${t_code}&zw_p_size=-1").upChartData()>
<#assign  e_pageField=logData.getPageField() />
<#assign  e_pagedata=logData />

<@m_zapmacro_common_table e_pagedata />

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

<table  class="table  table-condensed table-striped table-bordered table-hover" id="table11">
	<thead>
	    <tr>
        	
	        <#list e_pagedata.getPageHead() as e_list>
		        	<#if e_list_index == 0>
		        		<#-- 判断是否为不可维护商品模板   此页面共3处判断 -->
		        		<#if t_type != "449747500001" && t_type != "449747500007" && t_type != "449747500008" && t_type != "449747500013" && t_type != "449747500014" && t_type != "449747500018" && t_type != "449747500021" && t_type != "449747500025"  && t_type != "449747500024" >
			        		<th>
			  		 			<input id="checkAllOrNone" type="checkbox" xuanzhong="1" value="123456" />
			  		 		</th>
		  		 		</#if>
		  		 	<#elseif e_list_index == 1 || e_list_index == 2 || e_list_index==12 || e_list_index==13 || e_list_index==14><#-- 所有模版都需要显示部分-->
		  		 		<th>
				      	 	${e_list}
				      	</th>
		  		 	<#elseif e_list_index == 8>
		  		 		<#if t_type == "449747500022">
		  		 			<th>
				      	 		${e_list}
				      		</th>
		  		 		</#if>
		  		 	<#else>
		  		 		<#if t_type == "449747500001" || t_type == "449747500024" >
		      				<#if e_list_index==4 || e_list_index==5 || e_list_index==6 || e_list_index==7 || e_list_index==8 || e_list_index==9 || e_list_index==11 || e_list_index==21 || e_list_index==31 || e_list_index==32 >
					  			<th>
						      	 	${e_list}
						      	</th>
					  		</#if>
					 	<#elseif t_type == "449747500002">
					 		<#-- 两栏多行模板 -->
					 		<#if  e_list_index==3 ||  e_list_index==7 || e_list_index==8 || e_list_index==9 || e_list_index==10 || e_list_index==11 || e_list_index==31 || e_list_index==32  >
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>
					 	<#elseif t_type == "449747500003">
					 		<#-- 一栏多行 -->
					 		<#if e_list_index==3  ||  e_list_index==7 || e_list_index==8 || e_list_index==9 || e_list_index==10 || e_list_index==11 || e_list_index==31 || e_list_index==32 >
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>
					 	<#elseif t_type == "449747500004" || t_type == "449747500005" >
					 		
					 	<#elseif t_type == "449747500006">
					 		<#-- 两栏广告 -->
					 		<#if e_list_index==4 || e_list_index==7 || e_list_index==8 || e_list_index==9 || e_list_index==10 || e_list_index==31 || e_list_index==32 >
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>
					 	<#elseif t_type == "449747500007"  >
					 		<#if e_list_index==15 || e_list_index==31 || e_list_index==32>
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>
						<#elseif t_type == "449747500008"  >
					 		<#if e_list_index==15 || e_list_index==16 || e_list_index==31 || e_list_index==32>
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>
					 	<#elseif t_type == "449747500009">
					 		<#-- 普通视频模板 -->
					 		<#if  e_list_index==7 || e_list_index==8 || e_list_index==9  || e_list_index==17 || e_list_index==18 || e_list_index==31 || e_list_index==32 >
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>
					 	<#elseif t_type == "449747500010">
					 		<#-- 一行多栏，横滑 -->
					 		<#if  e_list_index==4 || e_list_index==5 || e_list_index==6 || e_list_index==7 || e_list_index==8 || e_list_index==9  || e_list_index==21 || e_list_index==31 || e_list_index==32 >
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>
					 	<#elseif t_type == "449747500011">
					 		<#-- 一行三栏 -->
					 		<#if  e_list_index==4 || e_list_index==5 || e_list_index==6 || e_list_index==7 || e_list_index==8 || e_list_index==9 || e_list_index==21 || e_list_index==31 || e_list_index==32 >
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>
						<#elseif t_type == "449747500012">
							<#-- 分类模板 -->
					 		<#if  e_list_index==4 || e_list_index==17 || e_list_index==19 || e_list_index==31 || e_list_index==32 >
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>
						<#elseif t_type == "449747500013">
					 		<#if  e_list_index==17 || e_list_index==20 || e_list_index==23 || e_list_index==24 || e_list_index==31 || e_list_index==32 >
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>
						<#elseif t_type == "449747500014" || t_type == "449747500015" || t_type == "449747500016">
							<#if e_list_index==4 || e_list_index==5 || e_list_index==6 || e_list_index==7 || e_list_index==8 || e_list_index==9 || e_list_index==11 || e_list_index==21 || e_list_index==22 || e_list_index==23 || e_list_index==25 || e_list_index==26 || e_list_index==31 || e_list_index==32 >
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>
						<#elseif t_type == "449747500017">
							<#-- 扫码购 -->
					 		<#if e_list_index==7 || e_list_index==8 || e_list_index==9 || e_list_index==27 || e_list_index==31 || e_list_index==32 >
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>
						<#elseif t_type == "449747500018">
							<#if e_list_index==22 || e_list_index==23 || e_list_index==24 || e_list_index==28 || e_list_index==31 || e_list_index==32 >
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>
						<#elseif t_type == "449747500019">
							<#if e_list_index==22|| e_list_index==23 || e_list_index==19|| e_list_index==31 || e_list_index==32 >
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>
						<#elseif t_type == "449747500020">
							<#if e_pageField[e_list_index]=="template_number" 
									|| e_pageField[e_list_index]=="commodity_location"
									|| e_pageField[e_list_index]=="coupon"
									|| e_pageField[e_list_index]=="start_time"
									|| e_pageField[e_list_index]=="end_time"
									|| e_pageField[e_list_index]=="create_time"
									|| e_list == "修改"
									|| e_list == "删除">
						 		<th>
						 			<#if e_pageField[e_list_index]=="coupon" >
						 				优惠券类型编码
						 			<#else>
						 				${e_list}
						 			</#if>
						      	</th>
						    </#if>	
						<#elseif t_type == "449747500021">
							<#if e_list_index==22  || e_list_index==28 || e_list_index==31 || e_list_index==32 >
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>	
						<#elseif t_type == "449747500022">
							<#-- 兑换商品模板 -->
							<#if  e_list_index==3 ||  e_list_index==7 || e_list_index==8 || e_list_index==9 || e_list_index==10 || e_list_index==11 || e_list_index==31 || e_list_index==32  >
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>
						<#elseif t_type == "449747500025">
					 		<#-- 精编商品模板 -->
					 		<#if  e_list_index==5 ||  e_list_index==7 || e_list_index==9 || e_list_index==11 || e_list_index==31 || e_list_index==32>
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>
						<#elseif t_type == "449747500023" ||  t_type=="449747500026">
					 		<#-- 拼团模板-->
					 		<#if   e_list_index==30 ||  e_list == "修改"|| e_list == "删除">
						 		<th>
						      	 	${e_list}
						      	</th>
						    </#if>   
					 	</#if>
	        	</#if>
	      	</#list>
			
	    </tr>
  	</thead>
  
	<tbody>
		<#-- <@test1 e_pagedata  t_type /> -->
		
	 	<#list e_pagedata.getPageData() as e_list>
				<tr>
		  		 <#list e_list as e>
	  		 		<#if e_index == 0>
	  		 			<#-- 判断是否为不可维护商品模板   此页面共3处判断 -->
	  		 			<#if t_type != "449747500001" && t_type != "449747500007" && t_type != "449747500008" && t_type != "449747500013" && t_type != "449747500014" && t_type != "449747500018" && t_type != "449747500021" && t_type != "449747500025" && t_type != "449747500024" >
		  		 			<td>
		  		 				<input name="delCheck" type="checkbox" value="${e?default("")}"/>
		  		 			</td>
	  		 			</#if>
	  		 		<#elseif e_index == 1 || e_index == 2 || e_index==12 || e_index==13 || e_index==14><#-- 所有模版都需要显示部分 -->
	  		 			<td>
			  				${e?default("")}
			  			</td>
	      			<#elseif e_index == 8>
	      				<#if t_type == "449747500022">
		  		 			<td>
			  					${e?default("")}
			  				</td>
		  		 		</#if>
	      			<#elseif t_type == "449747500001" || t_type == "449747500024" >
	      				<#if e_index==4 || e_index==5 || e_index==6 || e_index==7 || e_index==8 || e_index==9 || e_index==11 || e_index==21 || e_index==31 || e_index==32>
				  			<#if (e_index==4 || e_index==11) && e?default("") != "">
						 		<td>
					      			<img src="${e?default("")}" style="width:150px;" />
					      		</td>	
					      	<#elseif e_index == 31>
	  		 					<td>
			      					<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
			  		 			</td>
						 	<#else>
								<td>
					  				${e?default("")}
					  			</td>
							</#if>
				  		</#if>
				 	<#elseif t_type == "449747500002">
				 		<#-- 两栏多行模板 -->
				 		<#if  e_index==3 ||  e_index==7 || e_index==8 || e_index==9 || e_index==10 || e_index==11 || e_index==31 || e_index==32>
				  			<#if (e_index==4 || e_index==11) && e?default("") != "">
						 		<td>
					      			<img src="${e?default("")}" style="width:150px;" />
					      		</td>
					      	<#elseif e_index == 31>
	  		 					<td>
			      					<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
			  		 			</td>					      			
						 	<#else>
								<td>
					  				${e?default("")}
					  			</td>
							</#if>
				  		</#if>
				 	<#elseif t_type == "449747500003">
				 		<#-- 一栏多行 -->
				 		<#if e_index==3  ||  e_index==7 || e_index==8 || e_index==9 || e_index==10 || e_index==11 || e_index==31 || e_index==32 >
				  			<#if (e_index==4 || e_index==11) && e?default("") != "">
						 		<td>
					      			<img src="${e?default("")}" style="width:150px;" />
					      		</td>
					      	<#elseif e_index == 31>
	  		 					<td>
			      					<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
			  		 			</td>					      			
						 	<#else>
								<td>
					  				${e?default("")}
					  			</td>
							</#if>
				  		</#if>
				 	<#elseif t_type == "449747500004" || t_type == "449747500005"><!-- 不会出现，标题和视频模板没有维护内容按钮-->
					      	<#if e_index == 29>
	  		 					<td>
			      					<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
			  		 			</td>
			  		 		<#elseif e_index == 31>
			  		 			<td>
					  				${e?default("")}
					  			</td>
			  		 		</#if>			
				 	<#elseif t_type == "449747500006">
				 		<#-- 两栏广告 -->
				 		<#if e_index==4 || e_index==7 || e_index==8 || e_index==9 || e_index==10 || e_index==31 || e_index==32>
				  			<#if (e_index==4 || e_index==11) && e?default("") != "">
						 		<td>
					      			<img src="${e?default("")}" style="width:150px;" />
					      		</td>	
					      	<#elseif e_index == 31>
	  		 					<td>
			      					<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
			  		 			</td>					      		
						 	<#else>
								<td>
					  				${e?default("")}
					  			</td>
							</#if>
				  		</#if>
				 	<#elseif t_type == "449747500007" >
				 		<#if e_index==15 || e_index==32>
							<td>
				  				${e?default("")}
				  			</td>
					    <#elseif e_index == 31>
	  		 				<td>
			      				<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
			  		 		</td>				  			
				  		</#if>
				  	<#elseif  t_type == "449747500008">
				 		<#if e_index==15 || e_index==16 || e_index==32>
							<td>
				  				${e?default("")}
				  			</td>
					    <#elseif e_index == 31>
	  		 				<td>
			      				<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
			  		 		</td>				  			
				  		</#if>
				 	<#elseif t_type == "449747500009">
				 		<#-- 普通视频模板 -->
				 		<#if  e_index==7 || e_index==8 || e_index==9  || e_index==17 || e_index==18 || e_index==31 || e_index==32>
				  			<#if (e_index==4 || e_index==11) && e?default("") != "">
						 		<td>
					      			<img src="${e?default("")}" style="width:150px;" />
					      		</td>	
					      	<#elseif e_index == 31>
	  		 					<td>
			      					<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
			  		 			</td>					      		
						 	<#else>
								<td>
					  				${e?default("")}
					  			</td>
							</#if>
				  		</#if>
				 	<#elseif t_type == "449747500010">
				 		<#-- 一行多栏，横滑 -->
				 		<#if  e_index==4 || e_index==5 || e_index==6 || e_index==7 || e_index==8 || e_index==9  || e_index==21 || e_index==31 || e_index==32>
				  			<#if (e_index==4 || e_index==11) && e?default("") != "">
						 		<td>
					      			<img src="${e?default("")}" style="width:150px;" />
					      		</td>	
					      	<#elseif e_index == 31>
	  		 					<td>
			      					<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
			  		 			</td>					      		
						 	<#else>
								<td>
					  				${e?default("")}
					  			</td>
							</#if>
				  		</#if>
				 	<#elseif t_type == "449747500011">
				 		<#-- 一行三栏 -->
				 		<#if  e_index==4 || e_index==5 || e_index==6 || e_index==7 || e_index==8 || e_index==9 || e_index==21 || e_index==31 || e_index==32>
				  			<#if (e_index==4 || e_index==11) && e?default("") != "">
						 		<td>
					      			<img src="${e?default("")}" style="width:150px;" />
					      		</td>
					      	<#elseif e_index == 31>
	  		 					<td>
			      					<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
			  		 			</td>					      			
						 	<#else>
								<td>
					  				${e?default("")}
					  			</td>
							</#if>
				  		</#if>
				  	<#elseif t_type == "449747500012">
				  		<#-- 分类模板 -->
				 		<#if  e_index==4 || e_index==17 || e_index==19 || e_index==31 || e_index==32>
				  			<#if (e_index==4 || e_index==11 || e_index==19) && e?default("") != "">
						 		<td>
					      			<img src="${e?default("")}" style="width:150px;" />
					      		</td>	
					      	<#elseif e_index == 31>
	  		 					<td>
			      					<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
			  		 			</td>					      		
						 	<#else>
								<td>
					  				${e?default("")}
					  			</td>
							</#if>
				  		</#if>
					<#elseif t_type == "449747500013">
				 		<#if  e_index==17 || e_index==20 || e_index==23 || e_index==24 || e_index==31 || e_index==32>
				  			<#if (e_index==4 || e_index==11) && e?default("") != "">
						 		<td>
					      			<img src="${e?default("")}" style="width:150px;" />
					      		</td>
					      	<#elseif e_index == 31>
	  		 					<td>
			      					<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
			  		 			</td>					      			
						 	<#else>
								<td>
					  				${e?default("")}
					  			</td>
							</#if>
				  		</#if>
				  	<#elseif t_type == "449747500014" || t_type == "449747500015" || t_type == "449747500016">
				  		<#if e_index==4 || e_index==5 || e_index==6 || e_index==7 || e_index==8 || e_index==9 || e_index==11 || e_index==21 || e_index==22 || e_index==23 || e_index==25 || e_index==26 || e_index==31 || e_index==32 >
					 		<#if (e_index==4 || e_index==11) && e?default("") != "">
						 		<td>
					      			<img src="${e?default("")}" style="width:150px;" />
					      		</td>
					      	 <#elseif e_index == 31>
						 		<td>
						  			<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
						  		</td>
					      	<#else>
						 		<td>
						  			${e?default("")}
						  		</td>
						  	</#if>
					  	</#if>
				  	<#elseif t_type == "449747500017">
				  		<#-- 扫码购 -->
				 		<#if e_index==7 || e_index==8 || e_index==9 || e_index==27 || e_index==32 >
					 		<td>
					  			${e?default("")}
					  		</td>
					    <#elseif e_index == 31>
					 		<td>
					  			<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
					  		</td>
					    </#if>
					<#elseif t_type == "449747500018">
						<#if e_index==22 || e_index==23 || e_index==24 || e_index==28 || e_index==32 >
					 		<td>
					  			${e?default("")}
					  		</td>
					    </#if>
					    <#if e_index==31>
					 		<td>
					  			<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
					  		</td>
					    </#if>
					<#elseif t_type == "449747500019">
						<#if (e_index==19) >
						        <#if e?default("") != "">
						 		<td>
					      			<img src="${e?default("")}" style="width:150px;" />
					      		</td>
					      		<#else>
					      		<td>
					      			${e?default("")}
					      		</td>
					      		</#if>
						<#elseif e_index==22|| e_index==23 || e_index==32 >
						 	<td>
					  			${e?default("")}
					  		</td>
						<#elseif e_index == 31>
					 		<td>
					  			<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
					  		</td>
					    </#if>
					<#elseif t_type == "449747500020">
					
						<#if e_pageField[e_index]=="template_number" 
								|| e_pageField[e_index]=="commodity_location"
								|| e_pageField[e_index]=="coupon"
								|| e_pageField[e_index]=="start_time"
								|| e_pageField[e_index]=="end_time"
								|| e_pageField[e_index]=="create_time"
								|| e_pageField[e_index] == "修改"
								|| e_pageField[e_index] == "删除">
						 	<td>
						 		<#if e_pageField[e_index] == "修改">
						 			<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
						 		<#else>
							 		${e?default("")}
						 		</#if>
					  		</td>
					    </#if>
					<#elseif t_type == "449747500021">
						<#if e_index==22 || e_index==28 || e_index==32 >
					 		<td>
					  			${e?default("")}
					  		</td>
					    </#if>
					    <#if e_index==31>
					 		<td>
					  			<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
					  		</td>
					    </#if>
					<#elseif t_type == "449747500022">
						<#-- 兑换商品模板 -->
				 		<#if  e_index==3 ||  e_index==7 || e_index==8 || e_index==9 || e_index==10 || e_index==11 || e_index==31 || e_index==32>
				  			<#if (e_index==4 || e_index==11) && e?default("") != "">
						 		<td>
					      			<img src="${e?default("")}" style="width:150px;" />
					      		</td>
					      	<#elseif e_index == 31>
	  		 					<td>
			      					<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
			  		 			</td>					      			
						 	<#else>
								<td>
					  				${e?default("")}
					  			</td>
							</#if >
				  		</#if>
					<#elseif t_type == "449747500025">
					 	<#-- 精编商品模板 -->
				 		<#if  e_index==5 ||  e_index==7 || e_index==9 || e_index==11 || e_index==31 || e_index==32 >
					 		<#if (e_index==11) && e?default("") != "">
						 		<td>
					      			<img src="${e?default("")}" style="width:150px;" />
					      		</td>	
					      	<#elseif e_index == 31>
	  		 					<td>
			      					<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
			  		 			</td>
						 	<#else>
								<td>
					  				${e?default("")}
					  			</td>
							</#if>
					    </#if>
				  <#elseif t_type == "449747500023" || t_type=="449747500026">
					 	<#-- 拼团模板数据 -->
				 		     <#if   e_index==30 || e_pageField[e_index] == "修改" || e_pageField[e_index] == "删除">
				 		     <td>
								<#if e_pageField[e_index] == "修改">
						 			<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
						 		<#else>
							 		${e?default("")}
						 		</#if>
						 	 </td>
							</#if>
				 	</#if>	
		      	</#list>
		      	</tr>
	 	</#list>
	</tbody>
</table>
</#macro>
<script>
require(['cfamily/js/adjustCommodity'],function(b){b.init(${t_type})});
</script>
<#macro test1 e_pagedata t_type>
	<#list e_pagedata.getPageData() as e_list>
		<tr>
		 <#list e_list as e>
		 	
		 		<#if e_index == 0>
		 	
	  		 	<#elseif (e_index==4 || e_index==10) && e?default("") != "">
	  		 		<td>
		      			<img src="${e?default("")}" style="width:150px;" />
		      		</td>	
	  		 	<#elseif e_index == 21>
		 			<td>
		      			<a href="page_edit_v_fh_data_commodity?zw_f_uid=${e_list[0]}&zw_f_template_type=${t_type}" class="btn btn-link">修改</a>
		      			
		  		 	</td>
	  		 	<#else>
	  				<td>
	      				${e?default("")}
	      			</td>
	  			</#if>
	  			
	  			
	  	</#list>
	  	</tr>
	</#list>
</#macro>
<script>
	$(document).ready(function(){
		
		$("#checkAllOrNone").click(function(){
	       $("tbody input[name='delCheck']").each(function(){
	            this.checked=$("#checkAllOrNone").is(':checked');
	       }); 
	    });
	    
	    $("#batchDel").click(function(){
	 		var checkUid = new Array();
	 		$("tbody input[name='delCheck']").each(function(){
	            if(this.checked){
	            	checkUid.push(this.value);
	            }
	       }); 
	 		if(checkUid.length <= 0){
	 			zapjs.f.message("你还没有选中任何栏目内容！");
	 			return false;
	 		}
	 		var obj = {};
			obj.uids = checkUid;
			func_confirm("这样会删除选中的"+checkUid.length+"个栏目内容，确认要继续吗？",obj)
		 });
		 
		  // 确认是否执行删除操作
		  function func_confirm(tip,obj) {
			var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
			$(document.body).append(sModel);
			$('#zapjs_f_id_modal_message').html('<div class="w_p_20">'+tip+'</div>');
			var aButtons = [];
			aButtons.push({
				text : '是',
				handler : function() {
						$('#zapjs_f_id_modal_message').dialog('close');
						$('#zapjs_f_id_modal_message').remove();
						 zapjs.zw.api_call('com_cmall_familyhas_api_ApiBatchDelRelevanceCommodity',obj,function(result) {
						if(result.resultCode==1){
						zapjs.zw.modal_show({
							content : "删除成功！",
							okfunc : 'zapjs.f.autorefresh()'
						});
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
	});
	<#-- 下载删除模板弹窗-->
		function show_import_windows(){
		var templateCode = document.getElementsByName("delCheck")[0]
		templateCode = templateCode ==null?"":document.getElementsByName("delCheck")[0].parentNode.nextElementSibling.innerHTML;
	
		zapjs.f.window_box({
			id : 'pph_import_product',
			content : '<iframe src="../show/page_import_delProducts_template?zw_f_seller_code=SI2003&templateCode='+templateCode+'&infoCode=" frameborder="0" style="width:100%;height:500px;"></iframe>',
			width : '700',
			height : '550'
		});
	}
	function mohuSearch(t_type){
		$("#table11 tr").show()
		var codeValue = $("#scode").val()
		if(codeValue == null || codeValue == "")
		{
			return;
		}
		var mytable = document.getElementById('table11');
		
		var pos_index = 0;
		var tm_index = 0;
		<#-- 有选择框-->
		if(t_type != "449747500001" && t_type != "449747500007" && t_type != "449747500008" && t_type != "449747500013" && t_type != "449747500014" && t_type != "449747500018" && t_type != "449747500021" && t_type != "449747500025" && t_type != "449747500024"){
			$("table tr").each(function() {
            	$($(this).children('td:eq(0)').children("input")).attr("xuanzhong",'1');
    		});
    		pos_index = 2;
		} else {
			pos_index = 1;
		}
		<#-- 所有模版都需要显示部分-->	
		<#-- 模板编号（1）、位置（2）、-->	
		<#-- 确定开始时间的位置-->
		if(t_type == "449747500001" || t_type == "449747500014" || t_type == "449747500015" || t_type == "449747500016" || t_type == "449747500024"){
			tm_index = pos_index + 7 + 1;
		} else if(t_type == "449747500002" || t_type == "449747500003" || t_type == "449747500010" || t_type == "449747500011" || t_type == "449747500022"){
			tm_index = pos_index + 6 + 1;
		} else if(t_type == "449747500004" || t_type == "449747500005" || t_type == "449747500007" || t_type == "449747500008" || t_type == "449747500013" || t_type == "449747500018"|| t_type == "449747500021"){
			tm_index = pos_index + 1;
		} else if(t_type == "449747500006"){
			tm_index = pos_index + 5 + 1;
		} else if(t_type == "449747500009" || t_type == "449747500017"){
			tm_index = pos_index + 3 + 1;
		} else if(t_type == "449747500012"){
			tm_index = pos_index + 1 + 1;
		}  else if(t_type == "449747500025"){
			tm_index = pos_index + 4 + 1;
		}  else if(t_type == "449747500023" || t_type=="449747500026"){
			tm_index = pos_index  + 1;
		} 
    	
    	for(var i=1,rows=mytable.rows.length; i<rows; i++){
    		var tmDelFlag = false;
    		var begin_time = mytable.rows[i].cells[tm_index].innerHTML;    		
	        if(begin_time.indexOf(codeValue) == -1){
	        	tmDelFlag = true;
	        }
	        if(tmDelFlag){
		      	//隐藏当前行
		      	$("#table11 tr").eq(i).hide();
		      	if(t_type != "449747500001" && t_type != "449747500007" && t_type != "449747500008" && t_type != "449747500013" && t_type != "449747500014" && t_type != "449747500018" && t_type != "449747500021" && t_type != "449747500025" && t_type != "449747500024"){
			      	//选中框改为不选中
			      	var x = $("#table11 tr").eq(i).children("td").eq(0).children("input");
			      	$(x).attr("xuanzhong",'0');
		      	}
		    }
    	}
	}
</script>