<#include "../zapmacro/zapmacro_common.ftl" />
<#include "../macro/macro_common.ftl" />
<@m_common_page_head_common e_title="API测试接口" e_bodyclass="w_h_100p"  />
<style>
.zab_home_home_html{height:100%;}
</style>



<div class="easyui-layout" style="width:100%;height:100%;">






 	<div   data-options="region:'west',split:true" title="API" style="width:200px;">
 	
 	
 	<div class="zw_page_tree_box">
	
	<ul class="easyui-tree" id="zw_page_common_tree" ></ul>
	</div>
 	</div>
	<div      data-options="region:'center'" title="测试吧，少年" >
      <form class="form-horizontal" id="manage_apitest_form">
      
        <legend>API测试</legend>
        
         <div class="control-group">
	    	<label class="control-label" for="api_target">接口url：</label>
	    	<div class="controls">
	    		<input type="text" class="c_md5" style="width:80%;" id="api_target" name="api_target"  placeholder="api名称" value="">
	    	</div>
	  	</div>

	  	
	  	<div class="control-group">
	    	<label class="control-label" for="api_input">Json数据：</label>
	    	<div class="controls">
	    		<textarea class=" c_md5"  style="width:80%;" id="api_input" name="api_input" rows="8" ></textarea>
	    	</div>
	  	</div>


         <div class="control-group">
	         <label class="control-label" for=""></label>
	         <div class="controls">
		    	
	        	<button class="btn btn-large btn-success" type="button" onclick="zapadmin_outbind.call_api()">调用API</button>
	        </div>
	  	</div>
	  
        
      </form>

    </div> 
    
    
    <div     data-options="region:'east',split:true" title="模拟请求"  style="width:400px;"> 
    
    	<div style="width:360px;margin:10px;">

		<div>
			<div>请求链接</div>
			<div id="manage_apitest_link"></div>
		</div>
		<div>
			<div>请求内容</div>
			<div id="manage_apitest_submit" >
			
			
			</div>
		</div>
		<div>
			<div>返回信息</div>
			<div >
			<textarea id="manage_apitest_return" rows="20" style="width:100%;">
			</textarea>
			
			</div>
		</div>
    	</div>
    
    </div>
    
    </div>
    
 	 
	<script type="text/javascript">
		require(['zapadmin/js/zapadmin_outbind'],function(a){a.init();});
	</script>
	       
    </div>
<@m_common_page_foot_base  />
