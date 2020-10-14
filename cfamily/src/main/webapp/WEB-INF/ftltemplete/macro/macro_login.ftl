<style type="text/css">
	.checkcode-warp {
	    position: relative;
	    width: 300px;
	}
	.getcheckcode {
    position: absolute;
    right: 25px;
    top: 9px;
    background: #fff;
    border: 0;
    cursor: pointer;
	}
	.sendfont{
	    font: 12px/1.5 "Microsoft YaHei",tahoma,arial,'Hiragino Sans GB','\5b8b\4f53',sans-serif;
	    color:#f40;
	}
</style>

	<div class="c_login_box">
		<div class=" w_m_auto c_login_header c_login_width">

			<div class="w_fl w_mt_15">

				<div class="c_login_logo"></div>

			</div>
			<div class="w_fr w_mt_30">
				<ul class="w_ul">
					<li><a href="https://www.huijiayou.cn" target="_blank">官网首页</a></li>
				</ul>
			</div>
		</div>
		<div class="c_login_bg">
			<div class=" w_m_auto c_login_center c_login_width">
				<div class="w_h_40"></div>
				<div class="c_login_sign" style="height: 430px">
					<div class="c_login_fix w_opacity_90"></div>
					<div class="c_login_info">

						<div>
							<div class="c_login_pos">
								<div class="c_login_title"><#if a_macro_const_manage_home_title??>${a_macro_const_manage_home_title}<#else>后台登陆</#if></div>
								<div class="w_h_20"></div>

								<form>
									用户名： <input type="text" class="c_login_text"
										id="zw_f_login_name" name="zw_f_login_name"
										class="input-block-level" placeholder="请输入用户名" value="">
									<div class="w_h_20"></div>
									密&nbsp;&nbsp;&nbsp;&nbsp;码： <input type="password"
										class="c_login_text" id="zw_f_login_pass"
										name="zw_f_login_pass" class="input-block-level"
										placeholder="请输入密码" value="">
									<div class="w_h_20"></div>
									<div class="checkcode-warp">
										验证码：
										<input type="text" class="c_login_text" id="zw_f_mobile_verify" name="zw_f_mobile_verify" class="input-block-level" placeholder="请输入验证码" value="">
										<input class="getcheckcode sendfont" type="button"zapweb_attr_operate_id="bd967f07c03447c6ba1bc96a996d3a74"onclick="zapjs.zw.login_send_verify(this)" value="获取验证码"/>
								    </div>
											
									<div class="w_h_20"></div>
									
									<div class="w_al_center">
										<input class="btn btn-large btn-danger" id="id_manage_login_login_submit" type="button"
											zapweb_attr_operate_id="4e6b04cd2fed48b4a674129afe87658b"
											onclick="zapjs.zw.login_verify_post(this)" value="登录"/>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a>忘记密码</a>
									</div>
									<input type="hidden" id="zapjs_zw_login_sucess_target"
										name="zapjs_zw_login_sucess_target" value="../manage/home" />

								</form>
								<div class="w_h_20"></div>
								<div class="c_login_desc">官方联系电话：010-56919898</div>
							</div>
							
							<div class="c_login_version" style="padding-top: 50px;">当前系统版本：${b_method.upClass("com.srnpr.zapcom.basesupport.VersionSupport").upVersionFullName()}</div>

						</div>

					</div>

				</div>
			</div>
		</div>



		<div class=" w_m_auto c_login_footer c_login_width">

			<div class="w_fl w_mt_15">北京惠家有电子商务有限公司  版权所有</div>
			<div class="w_fr w_mt_15">
				<ul class="w_ul">
				</ul>
			</div>
		</div>
	</div>

<script type="text/javascript">
$(function(){zapadmin.login_page()});
</script>


