/*
 * zapbase
 * 基本功能及基本插件
 */

var zapbase = {

	config : {
		cookie_base : 'cookie-zw-',
		// 是否调试模式
		debug : true
	},

	// -------------------- JSON相关操作

	// 格式化json 对象转成字符串
	json_stringify : function(o_obj) {
		return JSON.stringify(o_obj);
	},
	// 将字符串转换成对象
	json_parse : function(s_json) {
		return JSON.stringify(s_json);
	},
	// 获取请求链接中的参数
	up_urlparam : function(sKey, sUrl) {

		var sReturn = "";

		if (!sUrl) {
			sUrl = window.location.href;
			if (sUrl.indexOf('?') < 1) {
				sUrl = sUrl + "?";
			}
		}
		
		var sParams = sUrl.split('?')[1].split('&');

		for (var i = 0, j = sParams.length; i < j; i++) {

			var sKv = sParams[i].split("=");
			if (sKv[0] == sKey) {
				sReturn = sKv[1];
				break;
			}
		}

		return sReturn;

	},
	repalce_urlparam_val : function(sKey,nValue,sUrl){
		
		var sReturn = "";
		
		if (!sUrl) {
			sUrl = window.location.href;
			if (sUrl.indexOf('?') < 1) {
				sUrl = sUrl + "?";
			}
		}
		if(sUrl.indexOf(sKey) == -1){
			return (sUrl.indexOf("?") == -1) ? (sUrl+"?"+sKey+"="+nValue) : (sUrl+"&"+sKey+"="+nValue);
		}
		
		var sParams = sUrl.split('?')[1].split('&');
		
		for (var i = 0, j = sParams.length; i < j; i++) {

			var sKv = sParams[i].split("=");
			if (sKv[0] == sKey) {
				sReturn = sKv[1];
				break;
			}
		}
		var repalceStr = sKey+"="+sReturn;
		return (sUrl.substring(0,sUrl.indexOf(sKey)) +sKey+"="+nValue+sUrl.substring(sUrl.indexOf(repalceStr)+repalceStr.length));
	},

	// -------------------- COOKIE相关操作
	// Cookie操作
	cookie : function(key, value, options) {

		// 判断如果是写操作 写到根目录
		if (value !== undefined) {
			if (!options) {
				options = {};
			}

			options.path = "/";
			if (!options.expires)
				options.expires = 30;
		}

		return $.cookie(zapbase.config.cookie_base + key, value, options);
	},

	// -------------------- DEBUG调试相关
	d : function(o_obj) {
		if (window.console && window.console.log) {
			console.log(zapbase.json_stringify(o_obj));
		}
	}
};

/*
 * ! jQuery Cookie Plugin v1.3.1 https://github.com/carhartl/jquery-cookie
 * 
 * Copyright 2013 Klaus Hartl Released under the MIT license
 */
(function(factory) {
	if (typeof define === 'function' && define.amd && 1 == 2) {
		// AMD. Register as anonymous module.
		define([ 'jquery' ], factory);
	} else {
		// Browser globals.
		factory(jQuery);
	}
}(function($) {

	var pluses = /\+/g;

	function raw(s) {
		return s;
	}

	function decoded(s) {
		return decodeURIComponent(s.replace(pluses, ' '));
	}

	function converted(s) {
		if (s.indexOf('"') === 0) {
			// This is a quoted cookie as according to RFC2068, unescape
			s = s.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, '\\');
		}
		try {
			return config.json ? JSON.parse(s) : s;
		} catch (er) {
		}
	}

	var config = $.cookie = function(key, value, options) {

		// write
		if (value !== undefined) {
			options = $.extend({}, config.defaults, options);

			if (typeof options.expires === 'number') {
				var days = options.expires, t = options.expires = new Date();
				t.setDate(t.getDate() + days);
			}

			value = config.json ? JSON.stringify(value) : String(value);

			return (document.cookie = [
					config.raw ? key : encodeURIComponent(key),
					'=',
					config.raw ? value : encodeURIComponent(value),
					options.expires ? '; expires='
							+ options.expires.toUTCString() : '', // use
					// expires
					// attribute,
					// max-age
					// is
					// not
					// supported
					// by
					// IE
					options.path ? '; path=' + options.path : '',
					options.domain ? '; domain=' + options.domain : '',
					options.secure ? '; secure' : '' ].join(''));
		}

		// read
		var decode = config.raw ? raw : decoded;
		var cookies = document.cookie.split('; ');
		var result = key ? undefined : {};
		for (var i = 0, l = cookies.length; i < l; i++) {
			var parts = cookies[i].split('=');
			var name = decode(parts.shift());
			var cookie = decode(parts.join('='));

			if (key && key === name) {
				result = converted(cookie);
				break;
			}

			if (!key) {
				result[name] = converted(cookie);
			}
		}

		return result;
	};

	config.defaults = {};

	$.removeCookie = function(key, options) {
		if ($.cookie(key) !== undefined) {
			// Must not alter options, thus extending a fresh object...
			$.cookie(key, '', $.extend({}, options, {
				expires : -1
			}));
			return true;
		}
		return false;
	};

}));
