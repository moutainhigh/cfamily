<#include "../zapmacro/zapmacro_common.ftl" />
<#include "../macro/macro_common.ftl" />
<#include "../macro/macro_const.ftl" />

<#assign manage_home_title=a_macro_const_manage_home_title>
<#assign manage_home_menu=a_macro_const_manage_home_menu>
<#assign user_support=b_method.upClass("com.srnpr.zapweb.websupport.UserSupport")>
<#assign checkPassWord = true>
<#if user_support.checkLogin()>
	<#assign user_info=user_support.getUserInfo()>
	<#assign checkPassWord = user_support.checkUpdateTime(user_info)>
<#else>
	<#assign manage_home_addhead="<meta http-equiv=\"Refresh\" content=\"0; url=login\" />" />
</#if>
<@m_common_page_head_common e_title=manage_home_title e_bodyclass="" e_addhead=manage_home_addhead?default("") />

<div class="zab_home_home_body">
	
		<#assign home_menu= user_support.upUserMenu(manage_home_menu+"0")>

		<div    class="zab_home_home_top">
			<div class="w_left c_site">
				<a href="home"><i class="icon-home  icon-white"></i>&nbsp;${manage_home_title}</a>



			</div>
			<div class="w_left c_nav">
				<ul class="w_ul">
					 <#list home_menu as el
					> <#if el['parent_menu']==manage_home_menu>
					<li <#if el_index==0>class="c_active"</#if> ><a href="#" onclick="zapadmin.top_menu(this,${el['menu_code']})">${el['menu_name']}</a></li>
					</#if> </#list>
				</ul>
			</div>


			<div class="w_right c_right">
			<a href="javascript:zapadmin.back_url()"><i class="icon-user  icon-white"></i></a>&nbsp;
			
				<a
					href="javascript:zapjs.zw.func_do(this,'f99848c8f8c011e29b7a000c298b20x')">
		<#if user_info??>${user_info.getRealName()?default("")}</#if></a>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:zapjs.zw.login_out('../manage/logout')">退出系统</a>
			</div>




		</div>

		<div class="zab_home_home_center">
		<div class="zab_home_home_main">
			<div   class=" zab_home_home_left  " style="overflow:scroll">
				<div>
					<ul>
					
					
					<#if  checkPassWord?? && checkPassWord>
					
					<#list home_menu as el > 
						<#if el['menu_code']?length==16>
							</ul>
						 </div>
						 <div id="home_menu_box_${el["menu_code"]}" class="c_item  <#if el_index!=0>w_display</#if>  ">
							<div class="c_title">${el["menu_name"]}</div>
							<ul class="w_ullist">
						<#elseif el['menu_code']?length==20>
							<#if el_index==1>
							<li class="c_header">+&nbsp;后台系统</li>
							<li class="c_active"><a  href="home" >后台首页</a></li>
							</#if>
							<li class="c_header">+&nbsp;${el["menu_name"]}</li> 
						<#elseif el['menu_code']?length==24>
								<li><a id="${el['menu_code']}" href="../${el['menu_link']}" onclick="zapadmin.menu_click(this)" target="main_iframe">${el["menu_name"]}</a></li>
						</#if> 
								
					</#list>
					
					
					<#else>
					<#list home_menu as el > 
						<#if el['menu_code']?length==16>
							</ul>
						 </div>
						 <div id="home_menu_box_${el["menu_code"]}" class="c_item  <#if el_index!=0>w_display</#if>  ">
							<div class="c_title">${el["menu_name"]}</div>
							<ul class="w_ullist">
						<#elseif el['menu_code']?length==20>
							<#if el_index==1>
							<li class="c_header">+&nbsp;后台系统</li>
							<li class="c_active"><a  href="home" >后台首页</a></li>
							</#if>				
						</#if> 	
					</#list>				

					</#if>
					</ul>
				</div>


				<div class="c_bottom" style="position:relative;">CopyRight 2013</div>

			</div>
			<div    class="zab_home_home_split">
			</div>
			<div    class="zab_home_home_right">
			<#if user_support.checkLogin()>
				<iframe src="../page/page_zapadmin_index_center" id="main_iframe" class="zab_home_home_iframe"
					name="main_iframe" width="100%" height="100%" frameborder="0"  onload="zapadmin.load_complate(this)"
					 ></iframe>
			</#if>
			</div>
			</div>
		</div>	
	</div>	
<@m_common_page_foot_base  />
<script type="text/javascript">
var checkPassWord= "${checkPassWord?string('true','false')}";
if(checkPassWord=="false"){
	zapjs.zw.modal_show({content:"您的密码需要变更，请修改密码！"});
}
</script>