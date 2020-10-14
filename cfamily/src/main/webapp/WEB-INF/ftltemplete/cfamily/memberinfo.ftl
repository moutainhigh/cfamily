<#assign member_code = b_page.getReqMap()["zw_f_member_code"]!"" >
<script>
require(['cfamily/js/memberinfo'],

function()
{
	zapjs.f.ready(function()
		{
			query(null);
		}
	);
}

);
</script>

<div class="zw_page_common_inquire">
	<form class="form-horizontal" method="POST">

<input type="hidden" id="zw_f_member_code" name="zw_f_member_code" value="${member_code}">

<div class="control-group">
	<label class="control-label" for="zw_f_phonenumber">手机号</label>
	<div class="controls">
		<input type="text" id="zw_f_phonenumber" name="zw_f_phonenumber" value="">
	</div>
</div>
	<input style="display:none;">
	<div class="control-group">
    	<div class="controls">
	<input type="button" class="btn  btn-success" zapweb_attr_operate_id="4a94e88dcfd94dd0967a1a576e13e01e" onclick="query(this)" value="查询" id="search">
    	</div>
	</div>
	</form>
	</div>
<div class="zab_info_page" id="userinfo">
	<div class="zab_info_page_title  w_clear">
		<span>用户基本信息</span>
	</div>
	<form class="form-horizontal" method="POST">
		<div class="control-group">
			<label class="control-label" for="">手机号:</label>
			<div class="controls">
				<div class="control_book" id="phonenumber">
			  		
				</div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="">昵称:</label>
			<div class="controls">
				<div class="control_book" id="nickname">
				</div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="">用户编号:</label>
			<div class="controls">
				<div class="control_book" id="usercode">
				</div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="">注册时间:</label>
			<div class="controls">
				<div class="control_book" id="registertime">
				</div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="">可用优惠券:</label>
			<div class="controls">
				<div class="control_book" id="youhuijuan">
				</div>
			</div>
		</div>
	</form>
</div>

<div class="zab_info_page" id="edu">
	<div class="zab_info_page_title  w_clear">
		<span>用户详细基本信息</span>
	</div>
	<table class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
		      	 <th>
		      	 	积分总额
		      	 <th>
		      	 	暂存款总额
		      	 </th>
		      	 <th>
		      	 	储值金总额
		      	 </th>
	    </tr>
  	</thead>
  
	<tbody>
			<tr>
	      		<td id="jifen">
	      		</td>
	      		<td id="zancunkuan">
	      		</td>
	      		<td id="chuzhijin">
	      		</td>
	      	</tr>
		</tbody>
</table>
</div>

<div class="zab_info_page" id="shouhuo">
	<div class="zab_info_page_title  w_clear">
		<span>收货信息</span>
		<a class="btn btn-small" href="javascript:void(0);" id="addAddress">新增收货信息</a>
	</div>
	<table class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
		      	 <th>
		      	 	收货人
		      	 <th>
		      	 	收货手机号
		      	 </th>
		      	 <th>
		      	 	收货人地址
		      	 </th>
		      	 <th>
		      	 	是否默认
		      	 </th>
		      	 <th>
		      	 	操作
		      	 </th>
	    </tr>
  	</thead>
  
	<tbody id="tbody">
	</tbody>
</table>
</div>