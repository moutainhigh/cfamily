<script>
require(['cfamily/js/selectCouponActivity'],

function()
{
	zapjs.f.ready(function()
		{
			selectCouponActivity.init();
		}
	);
}

);
</script>
<#-- 添加页 -->
<form class="form-horizontal" method="POST" >
	<div class="zab_info_page">
		<div class="zab_info_page_title  w_clear">
			<span>注册登录相关</span>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="">
			注册送券：
		</label>
		<div class="controls">
			<div id="ac1" class="w_left">
				<input class="btn btn-small" type="button" value="选择活动" onclick="selectCouponActivity.show_windows('activities1')">
			</div>
		<input type="hidden" id="zw_f_actictityIds1" name="zw_f_actictityIds1" value="" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="">
			推荐人送券：
		</label>
		<div class="controls">
			<div id="ac7" class="w_left">
				<input class="btn btn-small" type="button" value="选择活动" onclick="selectCouponActivity.show_windows('activities7')">
			</div>
		<input type="hidden" id="zw_f_actictityIds7" name="zw_f_actictityIds7" value="" />
		</div>
	</div>
	
	<div class="zab_info_page">
		<div class="zab_info_page_title  w_clear">
			<span>订单相关</span>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="">
			在线支付下单送：
		</label>
		<div class="controls">
			<div id="ac2" class="w_left">
				<input class="btn btn-small" type="button" value="选择活动" onclick="selectCouponActivity.show_windows('activities2')">
			</div>
		<input type="hidden" id="zw_f_actictityIds2" name="zw_f_actictityIds2" value="" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="">
			在线支付支付送：
		</label>
		<div class="controls">
			<div id="ac3" class="w_left">
				<input class="btn btn-small" type="button" value="选择活动" onclick="selectCouponActivity.show_windows('activities3')">
			</div>
		<input type="hidden" id="zw_f_actictityIds3" name="zw_f_actictityIds3" value="" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="">
			在线支付收货送：
		</label>
		<div class="controls">
			<div id="ac4" class="w_left">
				<input class="btn btn-small" type="button" value="选择活动" onclick="selectCouponActivity.show_windows('activities4')">
			</div>
		<input type="hidden" id="zw_f_actictityIds4" name="zw_f_actictityIds4" value="" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="">
			货到付款下单送：
		</label>
		<div class="controls">
			<div id="ac5" class="w_left">
				<input class="btn btn-small" type="button" value="选择活动" onclick="selectCouponActivity.show_windows('activities5')">
			</div>
		<input type="hidden" id="zw_f_actictityIds5" name="zw_f_actictityIds5" value="" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="">
			货到付款收货送：
		</label>
		<div class="controls">
			<div id="ac6" class="w_left">
				<input class="btn btn-small" type="button" value="选择活动" onclick="selectCouponActivity.show_windows('activities6')">
			</div>
		<input type="hidden" id="zw_f_actictityIds6" name="zw_f_actictityIds6" value="" />
		</div>
	</div>
	<#-- zb  develop -->
	<div class="zab_info_page">
		<div class="zab_info_page_title  w_clear">
			<span>行为相关</span>
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="" >
			连续启动 <input class="start_up1"  id="zw_f_start_up1" name="zw_f_start_up1" style="width:40px" /> 天送：
		</label>
		<div class="controls">
			<div id="ac29" class="w_left">
				<input class="btn btn-small" type="button" value="选择活动" onclick="selectCouponActivity.checkInputIfNull()" onclick="selectCouponActivity.show_windows('activities29')">
			</div>
		<input type="hidden" id="zw_f_actictityIds29"  name="zw_f_actictityIds29" value="" />
		</div>
	</div>	
	
	<div class="control-group">

		<label class="control-label" for="" style="width:100%;text-align:left">
		         连续 <input class="start_up2" style="width:40px" id="zw_f_start_up2" name="zw_f_start_up2"/> 天，每天加入购物车商品数量超过 <input class="add_shop_car" id="zw_f_add_shop_car" name="zw_f_add_shop_car" style="width:40px"/> 件送：
		    <input  id="ac30" class="btn btn-small" type="button" value="选择活动" onclick="selectCouponActivity.checkInputIfNull2()"  onclick="selectCouponActivity.show_windows('activities30')">
		</label>

		<input type="hidden" id="zw_f_actictityIds30" name="zw_f_actictityIds30" value="" />
	</div>	
	
	<#-- wanghao  develop -->
	
	<div class="zab_info_page">
		<div class="zab_info_page_title  w_clear">
			<span>签到相关</span>
		</div>
	</div>
	<div id="sign_big_div">
		<div class="control-group">
			<label class="control-label" for="">
				签到时间：
				<select class="sign_days" name="" style="width:80px">
					<option value="0">请选择</option>
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
					<option value="8">8</option>
					<option value="9">9</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
					<option value="17">17</option>
					<option value="18">18</option>
					<option value="19">19</option>
					<option value="20">20</option>
					<option value="21">21</option>
				</select>
			</label>
			
			<div class="controls">
				<div id="" class="w_left">
					<input class="btn btn-small" type="button" value="选择活动" onclick="selectCouponActivity.checkDaysAlert()">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input class="btn btn-small" type="button" value="新增" onclick="selectCouponActivity.addNewActivity()" />
				</div>
				<div id="" class=""></div>
				<input type="hidden" id="" name="" value="" />
				
			</div>
		</div>
	</div>
	
	<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate()  "116001016" />
	
	
	
	
</form>