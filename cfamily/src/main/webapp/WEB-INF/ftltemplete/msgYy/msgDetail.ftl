<style type="text/css">
.form-horizontal .msgDetail {
    float: left;
    width: 49%;
    clear: none;
    margin-bottom: 10px;
}
.msgImg img {
	transition: all 0.6s;
}
.msgImg img:hover{  
    transform: scale(2.5);  
} 
</style>

<#assign yyMsgService=b_method.upClass("com.cmall.newscenter.service.YyMsgService")>
<@m_zapmacro_common_detail_book b_page />

<#macro m_zapmacro_common_detail_book e_page>
	<#assign oneData=e_page.upOneData()>
	<#assign num = yyMsgService.changeSeeFlag(oneData["uid"]?default(''))>
	<form class="form-horizontal" method="POST" >
		<div class="msgDetail control-group">
			<label class="control-label" for="">编号ID:</label>
			<div class="controls">
				<div class="control_book">${oneData["msg_code"]?default('')}</div>
			</div>
		</div>
		<div class="msgDetail control-group">
			<label class="control-label" for="">用户手机号:</label>
			<div class="controls">
				<div class="control_book">${oneData["user_mobile"]?default('')}</div>
			</div>
		</div>
		<#assign msgType=oneData["msg_type"]?default('')>
		<div class="msgDetail control-group">
			<label class="control-label" for="">问题类型:</label>
			<div class="controls">
				<div class="control_book">
					<#if msgType == '449748250001'>
						文字留言
					<#else>
						语音留言
					</#if>
				</div>
			</div>
		</div>
		<div class="msgDetail control-group">
			<label class="control-label" for="">留言时间:</label>
			<div class="controls">
				<div class="control_book">${oneData["create_time"]?default('')}</div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="">留言内容:</label>
			<div class="controls">
				<div class="control_book">
					<#if msgType == '449748250001'>
						${oneData["msg_content"]?default('')}
					<#else>
						<audio controls="controls">
				    		<source src="${oneData['msg_content']?default('')}" type="audio/mp3" />
						</audio>
					</#if>
				</div>
			</div>
		</div>
		<div class="control-group">
			<#assign imagePathList=yyMsgService.getImagePaths(oneData["uid"]?default(''))>
			<label class="control-label" for="">图片:</label>
			<div class="controls">
				<div class="control_book msgImg">
					<#list imagePathList as imagePath>
						<img src="${imagePath}" style="width: 200px; height: 286px;"/>
					</#list>
				</div>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="">处理内容:</label>
			<div class="controls">
				<div class="control_book">
					${oneData["deal_content"]?default('')}
				</div>
			</div>
		</div>
	</form>
</#macro>