<link rel="stylesheet" href="../resources/lib/datepicker/laydate.css" type="text/css"/>
<script type="text/javascript" src="../resources/lib/datepicker/dateTime.js"></script>
<link type="text/css" href="../resources/lib/datepicker/laydate.css" rel="stylesheet">
<@m_zapmacro_common_page_add b_page />
<#macro m_zapmacro_common_page_add e_page>
<form class="form-horizontal" method="POST" >
	  <@m_zapmacro_common_auto_list  e_page.upAddData()   e_page  />
      <@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

    <#if e_pagedata??>
    <#list e_pagedata as e>
          <@m_zapmacro_common_auto_field e e_page/>    
    </#list>
    </#if>
</#macro>


<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
	
		<#if e_field.getFieldTypeAid()=="104005008">
	  		<@m_zapmacro_common_field_hidden e_field/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005001">
	  		  <#-- 内部处理  不输出 -->
	  	<#elseif  e_field.getFieldTypeAid()=="104005003">
	  		<@m_zapmacro_common_field_component  e_field  e_page/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005004">
	  		<@m_zapmacro_common_field_date  e_field />
		<#elseif  e_field.getFieldTypeAid()=="104005022">
  			<@m_zapmacro_common_field_datesfm  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e_field  e_page ""/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005103">
	  		<@m_zapmacro_common_field_checkbox  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005020">
	  		<@m_zapmacro_common_field_textarea  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005005">
	  		<@m_zapmacro_common_field_editor  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005021">
	  		<@m_zapmacro_common_field_upload  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>

<#-- 字段：日期 时分秒-->
<#macro m_zapmacro_common_field_datesfm e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#-- onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})"  -->
		<#-- onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',realDateFmt:'yyyy-MM-dd HH-mm-ss',realTimeFmt:'HH:mm:ss HH-mm-ss'})"   -->
		<input type="text"  autocomplete="off" onClick="laydate({istime:true,format:'YYYY-MM-DD hh:mm:ss'})"  id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
	<@m_zapmacro_common_field_end />
<#-- zapjs.f.require(['lib/datepicker/dateTime'],function(a){})-->
<#-- zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});-->
	<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){})" />
	  
</#macro>

<script>
    $(function(){ 
        $("#zw_f_nav_name").after('</br><span style="line-height:32px; color:red">建议2-5个文字，超过5个只展示5个字</span>');
        
        var html_ = '</br>温馨提示：</br><span style="line-height:32px; color:red">添加2个字时、图片尺寸159*114、图片大小5KB内，</span>';
        html_ += '</br><span style="line-height:32px; color:red">添加3个字时、图片尺寸198*114、图片大小5KB内，</span>';
        html_ += '</br><span style="line-height:32px; color:red">添加4个字时、图片尺寸240*114、图片大小5KB内，</span>';
        html_ += '</br><span style="line-height:32px; color:red">添加5个字时、图片尺寸285*114、图片大小5KB内</span>';
        $($("#zw_f_nav_icon")[0].parentElement).append(html_);  
        $("#zw_f_show_type").attr("readonly", "readonly"); 
        $("#zw_f_nav_name").attr("maxlength", "5"); 
        $("#zw_f_remark").after('</br><span style="line-height:32px; color:red">为支持BI进行转化率统计:  当导航名称为空且仅显示小图标时，此处必填</span>');
        
        
        // 是否开启猜你喜欢 默认否
        $("#zw_f_flag_1").attr("checked","checked");
        // 开始页面初始化
        init();
        
        $("input[name='zw_f_flag']").click(function(e){
			var id_ = "";
			if(e.target.id == "zw_f_flag_0"){
				id_ = "zw_f_flag_1";
			}else{
				id_ = "zw_f_flag_0";
			}	
			
			$("#" + id_).prop("checked", !$(this).prop('checked'));
			
			if($('#zw_f_flag_0').prop('checked')){
				classTypeShow();  
			}else{
				defaultHidden();
			}			
		});
		
		// 指定类别事件绑定
		$("#zw_f_class_type").change(function(e){
			onchangeHide()
			var val = $(this).children('option:selected').val();
			if(val == '4497480100040002'){  // 按分类设置
				$("#zw_f_classify").parent().parent().show();
				$("#zw_f_classify option:first").prop("selected", 'selected'); 
				hiddenCategoryLimit();
				hiddenCategoryCodes();
			}else if(val == '4497480100040003'){   // 按关键字
				$("#zw_f_key_word").parent().parent().show();
				hiddenCategoryLimit();
				hiddenCategoryCodes();
			}else if(val == '4497480100040001'){   // 按默认
				showCategoryLimit();
				hiddenCategoryCodes();
			}  
		});
		
		//分类限制事件绑定
		$("#zw_f_category_limit").change(function(){
			var val = $(this).children('option:selected').val();
			if ('449748560001' == val){
				hiddenCategoryCodes();
			} else if ('449748560002' == val){
				showCategoryCodes();
			}
		});
    });
    
    function defaultHidden(){
    	$("#zw_f_class_type").parent().parent().hide().attr("id","dev_zw_f_position");
    	$("#zw_f_classify").parent().attr("id","dev_classify").parent().hide().attr("id","dev_zw_f_classify");  // 分类设置
    	$("#zw_f_key_word").parent().parent().hide().attr("id","dev_zw_f_key_word");
    	$("#zw_f_classify option:first").prop("selected", 'selected'); 
    	$("#zw_f_class_type option:first").prop("selected", 'selected'); 
    	hiddenCategoryLimit();
    	hiddenCategoryCodes();
    }
    
    function classTypeShow(){
    	$("#zw_f_class_type").parent().parent().show(); 
    	$("#zw_f_class_type option:first").prop("selected", 'selected'); 
    	showCategoryLimit();
    }
    
    function onchangeHide(){
    	$("#zw_f_classify").parent().parent().hide();   
    	$("#zw_f_key_word").parent().parent().hide();  
    }
    
    function init(){
    	defaultHidden();
    	$("#zw_f_nav_type").val("4497471600100005");
    	$("#dev_classify input").remove();
    	$("#dev_classify").append('<select id="zw_f_classify" name="zw_f_classify"></select>');
    	
    	var opt = new Object();
		opt.parentCode = '44971604';
		opt.sellerCode = 'SI2003';
		api_call('com_cmall_familyhas_api_ApiForSellerCategory', opt , function(e){
			var html_ = '';
			if(e.status == 'success' && e.list.length > 0){
				var arr = e.list;
				for(var i = 0 ; i < arr.length ; i ++){
					html_ += '<option value="' + arr[i].category_code + '">' + arr[i].category_name + '</option>';
				}
			}
			$("#zw_f_classify").append(html_);
		}); 
    	
    }
    
    function api_call(sTarget, oData, fCallBack) {
		//判断如果传入了oData则自动拼接 否则无所只传入key认证
		var defaults = oData?{
			api_target : sTarget,
			api_input : zapjs.f.tojson(oData),
			api_key : 'jsapi'
		}:{api_key : 'jsapi',api_input:''};
		
		zapjs.f.ajaxjson("../jsonapi/" + sTarget, defaults, function(data) {
			fCallBack(data);
		});
	} 

	function hiddenCategoryLimit(){
		$("#zw_f_category_limit").parent().parent().hide();
		$('label[for="zw_f_category_limit"]').html('分类限制：');
		$("#zw_f_category_limit option:first").prop("selected", 'selected');
	}
	
	function showCategoryLimit(){
		$("#zw_f_category_limit option:first").prop("selected", 'selected');
		$('label[for="zw_f_category_limit"]').html('<span class="w_regex_need">*</span>分类限制：');
		$("#zw_f_category_limit").parent().parent().show();
	}

	function hiddenCategoryCodes(){
		$("[name=zw_f_category_codes]").eq(0).parent().parent().hide();
		$('label[for="zw_f_category_codes"]').html('分类：');
		$("[name=zw_f_category_codes]").each(function(){
			$(this).prop("checked", false);
		});
	}
	
	function showCategoryCodes(){
		$('label[for="zw_f_category_codes"]').html('<span class="w_regex_need">*</span>分类：');
		$("[name=zw_f_category_codes]").eq(0).parent().parent().show();
	}
	
</script>



























