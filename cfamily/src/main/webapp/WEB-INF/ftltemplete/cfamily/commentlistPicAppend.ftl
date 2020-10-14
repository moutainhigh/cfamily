<#assign piclist = b_page.getReqMap()["piclist"] >

<div class="zw_page_common_data">
<div class="w_left w_w_100" id="picUrl">

</div>
</div>


<script>
var piclist = "${piclist}".split('|');
for(var i in piclist){
	if(piclist[i] != ''){
		$('#picUrl').append('<a target="_blank" href="'+piclist[i]+'">'+'<img src="'+piclist[i]+'">'+'</a>');
	}
}
</script>