<#assign  data=b_page.upBookData()/> 

<form class="form-horizontal" method="POST">


<div class="control-group">
	<label class="control-label" for="">${data[0].fieldNote}:</label>
	<div class="controls">

	      		<div class="control_book">
		      		${data[0].pageFieldValue}
	      		</div>
	</div>
</div>


<div class="control-group">
	<label class="control-label" for="">${data[1].fieldNote}:</label>
	<div class="controls">

	      		<div class="control_book">
		      		${data[1].pageFieldValue}
	      		</div>
	</div>
</div>
	  	

<div class="control-group">
	<label class="control-label" for="">${data[2].fieldNote}:</label>
	<div class="controls">

	      		<div class="control_book">
	      			<#if data[2].pageFieldValue?? &&data[2].pageFieldValue!=''>
	      				<img src="${data[2].pageFieldValue}">
	      			</#if>
		      		
	      		</div>
	</div>
</div>
	  	
	  	
	
	
</form>