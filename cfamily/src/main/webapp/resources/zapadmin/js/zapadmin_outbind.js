var zapadmin_outbind = {	
		
		
	init : function() {
			
			
	},
	
	replace_url:function(sSource)
	{
		
		sSource=sSource.replace(/\./g, '_1_1');
		
		sSource=sSource.replace(/[A-Z]/g, function(m){return '_'+m.toLowerCase();});
		
		return sSource;
	},

	call_api : function() {

		if ($('#api_target').val() == "") {
			alert('路径呢？亲！');
			return;
		}
		
		if ($('#api_input').val() == "") {
			alert('请求数据呢？亲！');
			return;
		}
		
		var sUrl = $("#api_target").val();

		var sInput = $("#api_input").val();
		
		$.post("/cfamily/outbind", {
			api_input : $('#api_input').val(),
			api_target : $('#api_target').val()
		}, function(data, status) {

			$('#manage_apitest_link').html(data.url);
			$('#manage_apitest_return').text(data.data);
			
		});
		

	},
	
	date_time : function(formatStr) {

		var dthis = new Date();

		var str = formatStr;
		var Week = ['日', '一', '二', '三', '四', '五', '六'];

		str = str.replace(/yyyy|YYYY/, dthis.getFullYear());
		str = str.replace(/yy|YY/, (dthis.getYear() % 100) > 9 ? (dthis.getYear() % 100).toString() : '0' + (dthis.getYear() % 100));

		var iMonth = dthis.getMonth() + 1;

		str = str.replace(/MM/, iMonth > 9 ? iMonth.toString() : '0' + iMonth);
		str = str.replace(/M/g, iMonth);

		str = str.replace(/w|W/g, Week[dthis.getDay()]);

		str = str.replace(/dd|DD/, dthis.getDate() > 9 ? dthis.getDate().toString() : '0' + dthis.getDate());
		str = str.replace(/d|D/g, dthis.getDate());

		str = str.replace(/hh|HH/, dthis.getHours() > 9 ? dthis.getHours().toString() : '0' + dthis.getHours());
		str = str.replace(/h|H/g, dthis.getHours());
		str = str.replace(/mm/, dthis.getMinutes() > 9 ? dthis.getMinutes().toString() : '0' + dthis.getMinutes());
		str = str.replace(/m/g, dthis.getMinutes());

		str = str.replace(/ss|SS/, dthis.getSeconds() > 9 ? dthis.getSeconds().toString() : '0' + dthis.getSeconds());
		str = str.replace(/s|S/g, dthis.getSeconds());

		return str;
	}
};

if ( typeof define === "function" && define.amd) {
	define("zapadmin/js/zapadmin_outbind", function() {
		return zapadmin_outbind;
	});
}