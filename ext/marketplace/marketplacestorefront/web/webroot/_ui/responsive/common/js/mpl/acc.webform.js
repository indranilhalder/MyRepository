ACC.WebForm = {

	sendTicket : function(){
		$(".webfromTicketSubmit").on("click",function(e){
			
			if(ACC.WebForm.validateCRMForm()){
				
				var dataStr=$("#customerWebForm").serialize();
				$.ajax({
					url: ACC.config.encodedContextPath + "/ticketForm",
					data:  dataStr ,
					type: 'POST',
					success: function (data)
					{
						ACC.colorbox.open(title,{
							html: data,
							width:"500px"
						});
					},
					 error : function(resp) { 
						 console.log("Error in crmChildNodes"+resp);	
					 } 
				});
				
			}else{
				$div=$('.customCareError');
				$div.html('<div class="errors">Please fill up the mandatory fields.</div>');
			}
			
		});
		
	},
	validateCRMForm : function () {
		
		return true;
	},
	issueDropDown : function(){
		
		$(".node").on("change",function(e){
			var nodeParent=$(this).is(":selected").val();
			var nodeType=$(this).attr("name");
			var htmlOption="";
			$.ajax({
				url: ACC.config.encodedContextPath + "/crmChildrenNodes",
				data: {"nodeParent": nodeParent },
				type: 'GET',
				success: function (data)
				{
					if (nodeType.startsWith("nodeL1")) {
						$.each(data.nodes.nodes, function (index, data) {
					        htmlOption+="<option value='"+data.nodeCode+"'>"+data.nodeDesc + "</option>";
					    });
						
					    $("nodeL2").html(htmlOption);
					}
				},
				 error : function(resp) { 
					 console.log("Error in crmChildNodes"+resp);	
				 } 
			});
		});
		
	},
	xhrFileUpload : function () {
		
		var url="ticketForm/fileUpload";
		var allowedTypes = ['png', 'jpg', 'jpeg', 'gif'];   
		
		$("#attachmentFile").on("change",function(e){
			
			$('input[type="file"]').on('ajax', function(){
				  var $this = $(this);
				  $.ajax({
				    'type':'POST',
				    'data': (new FormData()).append('file', this.files[0]),
				    'contentType': false,
				    'processData': false,
				    'xhr': function() {  
				       var xhr = $.ajaxSettings.xhr();
				       if(xhr.upload){ 
				         xhr.upload.addEventListener('progress', progressbar, false);
				       }
				       return xhr;
				     },
				    'success': function(data){
				    	console.log(data);
				    	$this.siblings('#attachmentFile').trigger('ajax');
				    	$this.remove(); // remove the field so the next call won't resend the same field
				    }
				  });
				}).trigger('ajax');  // Execute only the first input[multiple] AJAX, we aren't using $.each
			
		});
		
	},
	
	
};

$(function() {
	
	ACC.WebForm.xhrFileUpload();
	ACC.WebForm.issueDropDown();
	ACC.WebForm.sendTicket();
	
	$('.contCustCareBtn').click(function(){
		$(this).toggleClass('dActive');
		$('.custmCareQrySec').slideToggle();
		$('#closeCustCareSec').addClass('active');
	});
	$('#closeCustCareSec').click(function(){
		$(this).removeClass('active');
		$('.custmCareQrySec').slideToggle();
		$('.contCustCareBtn').removeClass('dActive');
	});
});


