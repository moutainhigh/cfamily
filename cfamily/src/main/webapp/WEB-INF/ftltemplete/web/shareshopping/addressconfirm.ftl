<#include "../shareshoppingmacro/shareshopping_common.ftl" />
<@ss_common_head/>
	<script type="text/javascript" src="${ss_assign_resources}js/order.js"></script>
<@ss_common_body "ddbg"/>
<header><a href="javascript:;" class="jt" id="backurl_id">返回</a>填写收货人信息<a href="javascript:;" class="bc" id="address_save">保存</a></header>
<section class="submit">
	<ul>
    	<li><b>收货人姓名</b><input type="text" value="" placeholder="请输入收货人姓名" name="name" id="name" maxLength="10"></li>
    	<li><b>手机号码</b><input type="text" value="" placeholder="请输入收货人手机号 " name="mobile" id="mobile" maxLength="11"  onBlur="value=this.value.replace(/[^\d]/g,'')"><button id="getCode">获取验证码</button></li>
    	<li><b>短信验证码</b><input type="text" value="" placeholder="请输入验证码" name="code"  id="code" onkeyup="value=this.value.replace(/[^\d]/g,'')"></li>
    	<li>
        	<b>所在地区</b>
        	<select name="province" id="province">
            	<option value="0">选择省份</option>
            </select>
        	<select name="city" id="city">
            	<option value="0">请选择</option>
            </select>
        	<select name="county" id="county">
            	<option value="0">请选择</option>
            </select>
            <input type="hidden" value="" id="area">
        </li>
    	<li><b>详细地址</b><input type="text" value="" placeholder="请输入详细地址" id="detail" maxLength="50"></li>
    </ul>
</section>
<div class="mask" style="position:fixed;top:0px;width:100%;height:100%;z-index:99999; display:none;">
    <div class="masked" style="position:fixed;background:#000;width:100%;height:100%;filter:alpha(opacity=50); /*IE滤镜，透明度50%*/-moz-opacity:0.5; /*Firefox私有，透明度50%*/opacity:0.5;/*其他，透明度50%*/"></div>
    <div class="maskcon" init-data="正在发送..." result-data="发送成功" style="text-align:center;position:fixed;color:#fff;top:50%;left:50%;margin-left:-50px;width: 120px;font-size:16px;">正在发送...</div>
</div>
<@ss_common_jscall "order.addressconfirm();"/>
<@ss_common_footer />