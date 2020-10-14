<#assign advertise_code = b_page.getReqMap()["zw_f_advertise_code"] >
<#assign adver_entry_type = b_method.upDataOne("fh_advert","adver_entry_type","","advertise_code=:advertise_code","advertise_code",advertise_code)>
<#assign channelId = b_method.upDataOne("fh_advert","channel_id","","advertise_code=:advertise_code","advertise_code",advertise_code)>
<script>
require(['cfamily/js/advertColumnInfoEdit'],

function()
{
	zapjs.f.ready(function()
		{
			advertisementInfoAdd.init('${adver_entry_type.adver_entry_type}','${channelId.channel_id}');
		}
	);
}

);
</script>
<@m_zapmacro_common_page_edit b_page />
