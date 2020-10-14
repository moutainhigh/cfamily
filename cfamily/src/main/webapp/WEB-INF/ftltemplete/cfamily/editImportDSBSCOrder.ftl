<@m_zapmacro_common_page_edit b_page />
<#-- 修改页 -->
<#macro m_zapmacro_common_page_edit e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upEditData()   e_page  />
	<div class="control-group">
		<div class="controls">
			<input type="button" class="btn  btn-success" zapweb_attr_operate_id="e4a1d8e3813e11e69a3700505692798f" onclick="editSub(this)" value="提交修改">
	    </div>
	</div>
</form>
<script>
function editSub(obj){
var oForm = $(obj).parents("form");
zapjs.f.ajaxsubmit(
		oForm, 
		"../func/" + $(obj).attr('zapweb_attr_operate_id'), 
		function(data){
		var o = data;
		switch (o.resultType) {
			case "116018010":
				eval(o.resultObject);
				break;
			default:
				if (o.resultCode == "1") {

					if (o.resultMessage == "") {
						o.resultMessage = "操作成功";
					}

					zapjs.zw.modal_show({
						content : o.resultMessage,
						okfunc : 'refresh()'
					});

				} else {
					zapjs.zw.modal_show({
						content : o.resultMessage,
						okfunc : 'refresh()'
					});
				}
				break;
		}
		}, function(data){
			alert('系统出现错误，请联系技术，谢谢！');
			parent.zapjs.f.autorefresh();
		 	parent.zapjs.f.window_close("editImportDSBSCOrder");
		});
}
function refresh(){
	parent.zapjs.f.autorefresh();
	parent.zapjs.f.window_close("editImportDSBSCOrder");
}
</script>
</#macro>