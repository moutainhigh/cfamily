var addAppHomeDialogNew = {
	init : function() {
		$("#zw_f_channels_0").parents('.control-group').hide();
		$('#zw_f_channel_limit').change(function(){
			addAppHomeDialogNew.changeChannelLimit();
		});	
		$('input[name="zw_f_channels"]').change(function(){
			var allChecked = true;
			$('input[name="zw_f_channels"]').each(function(){
				if(!$(this).prop('checked')) {
					allChecked = false;
				}
			});
			
			// 所有渠道都被选中时自动变更为无限制
			if(allChecked) {
				$('#zw_f_channel_limit').val('449747640001');
				$('input[name="zw_f_channels"]').prop('checked',false);
				addAppHomeDialogNew.changeChannelLimit();
			}
		});
	},
	changeChannelLimit :function (){
		if($("#zw_f_channel_limit").val()=='449747640002'){
			$("#zw_f_channels_0").parents('.control-group').show();
		} else {
			$("#zw_f_channels_0").parents('.control-group').hide();
		}
	}
}
