
<@m_zapmacro_common_page_add b_page />






 
<script>
    $(function(){
        $("#zw_f_list_pic")[0].parentElement.id="div_zw_f_list_pic";
        $("#zw_f_info_pic")[0].parentElement.id='div_zw_f_info_pic';
        $("#zw_f_info_activity_pic")[0].parentElement.id='div_zw_f_info_activity_pic';
        $("#zw_f_event_label_pic_skip")[0].parentElement.id="div_zw_f_event_label_pic_skip";
        $("#div_zw_f_list_pic").before('<span id="descrip" style="float:left;margin-left:370px;position: absolute;line-height:32px;">左上尺寸（建议）：80*80</span>');
        $("#div_zw_f_info_pic").before('<span style="float:left;margin-left:370px;position: absolute;line-height:32px;">尺寸（限定）：183*57</span>');
        $("#div_zw_f_info_activity_pic").before('<span style="float:left;margin-left:370px;position: absolute;line-height:32px;">尺寸（限定）：1080*132 </span>');
        $("#zw_f_event_label_pic_skip").before('<span style="float:left;margin-left:370px;position: absolute;line-height:32px;">未设置商品详情活动标签, 请勿填写此跳转 </span>');
        
        $("#zw_f_label_position").change(function(){
        	if($(this).val()=='449748430001'){
        		$("#descrip").html("左上尺寸（建议）：80*80");
        	}else if($(this).val()=='449748430005'){
        		$("#descrip").html("通栏下尺寸（建议）：710*100");
        	}
        });
    });

</script>
















