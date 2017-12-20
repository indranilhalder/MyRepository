ACC.WebForm = {

	sendTicket : function(){
		$(".webfromTicketSubmit").click(function(e){
			e.preventDefault();
			
			if(ACC.WebForm.validateCRMForm()){
				
				var dataStr=$("#customerWebForm").serialize();
				$.ajax({
					url: ACC.config.encodedContextPath + "/ticketForm",
					data:  dataStr ,
					type: 'POST',
					success: function (data)
					{
						ACC.colorbox.open("",{
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
		
		var isValid = true;
		  $('.formControl').each(function() {
			    if ( $(this).val() === '' && $(this).attr("displayAllow")===true){
			        isValid = false;
			    }
			    //mobiel validation
			    if ( $(this).attr("name") == 'contactMobile' ){
			    	var mobNum = $(this).val();
			    	var filter = /^\d*(?:\.\d{1,2})?$/;
			        if (filter.test(mobNum)) {
			            if(mobNum.length==10){
			            	$(this).removeClass("feildError");
			            	$(this).addClass("feildSuccess");
			             } else {
			            	 $(this).removeClass("feildSuccess");
			            	 $(this).addClass("feildError");
			            	 isValid=false;
			             }
			        }
			        else {
			        	$(this).removeClass("feildSuccess");
			        	$(this).addClass("feildError");
			        	isValid= false;
			        }
			    }
			    //email validation
			    if ( $(this).attr("name") == 'contactEmail' ){
			    	var email = $(this).val();
			    	var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
			    	if(emailReg.test(email)){
			    		$(this).removeClass("feildError");
		            	$(this).addClass("feildSuccess");
			    	}else{
			    		$(this).removeClass("feildSuccess");
		            	$(this).addClass("feildError");
		            	isValid=false;
			    	}
			    }
			    
		  });
		  return isValid;
	},
	issueDropDown : function(){
		
		$(".node").on("change",function(e){
			//for select dropdown 
			var nodeValue=$("option:selected", this).val();
			var nodeText=$("option:selected", this).attr("nodeText");
			var nodeType=$(this).attr("name");
			//for radio 
			if(nodeValue==undefined){
				nodeValue=$(this).val();
			}
			var htmlOption="<option value=''>Select</option>";
			$.ajax({
				url: ACC.config.encodedContextPath + "/ticketForm/crmChildrenNodes?nodeParent="+nodeValue,
				type: 'GET',
				success: function (data)
				{
					if (nodeType.startsWith("nodeL1")) {
						$.each(data.nodes, function (index, data) {
					        htmlOption+="<option value='"+data.nodeCode+"' nodeText='"+data.nodeDesc+"' displayAllow='"+data.nodeDisplayAllowed+"'>"+data.nodeDesc + "</option>";
					    });
					    $("select[name=nodeL2]").html(htmlOption);
					    $("select[name=nodeL3]").show();
					}
					if (nodeType.startsWith("nodeL2")) {
						var check=false;
						var answer="";
						$.each(data.nodes, function (index, data) {
							
							if(data.nodeDisplayAllowed==true){	
								htmlOption+="<option value='"+data.nodeCode+"' nodeText='"+data.nodeDesc+"' displayAllow='"+data.nodeDisplayAllowed+"'>"+data.nodeDesc + "</option>";
							}else{
								check=true;
								answer=data.ticketAnswer;
								htmlOption+="<option value='"+data.nodeCode+"' nodeText='"+data.nodeDesc+"' displayAllow='"+data.nodeDisplayAllowed+"' selected>"+data.nodeDesc + "</option>";
							}
					    });
						
						if(check==false){
							$("select[name=nodeL3]").html(htmlOption);
							$("select[name=nodeL3]").show();
						}else{
							$("select[name=nodeL3]").html(htmlOption);
							$("select[name=nodeL3]").parent(".customSelectWrap").hide();
							$("select[name=nodeL3]").parent(".formGroup").append("<p>"+answer+"</p>");
						}
						
					    $("#nodeL2Text").val(nodeText);
					}
					if (nodeType.startsWith("nodeL3")) {
						var l4val="";
						$.each(data.nodes, function (index, data) {
							l4val=data.nodeCode;
					    });
						console.log(l4val);
					    $("#nodeL4").val(l4val);
					    $("#nodeL3Text").val(nodeText);
					}
				},
				 error : function(resp) { 
					 console.log("Error in crmChildNodes"+resp);	
				 } 
			});
		});
		
	},
	xhrFileUpload : function () {
		
		var url=ACC.config.encodedContextPath + "/ticketForm/fileUpload";
		var allowedTypes = ['png', 'jpg', 'jpeg', 'gif'];   
		
		$("#attachmentFile").on("change",function(e){
			
			if(this.files.length === 0){
		        return;
		    }

		    var data = new FormData();
		    data.append('uploadFile', this.files[0]);

		    var request = new XMLHttpRequest();
		    request.onreadystatechange = function(){
		        if(request.readyState == 4){
		            try {
		                var resp = JSON.parse(request.response);
		            } catch (e){
		                var resp = {
		                    status: 'error',
		                    data: 'Unknown error occurred: [' + request.responseText + ']'
		                };
		            }
		            console.log(resp.status + ': ' + resp.data);
		        }
		    };

		    request.upload.addEventListener('progress', function(e){
		    	$('#progress').width(Math.ceil(e.loaded/e.total) * 100 + '%');
		    }, false);
		    
		    // Create the form with appropriate header
		    request.open('POST', url);
		    request.setRequestHeader("Content-Type", "multipart/form-data; boundary=----12345678wertysdfg");
		    //request.setRequestHeader("Content-Length", data.length);
		    request.setRequestHeader("CelerFT-Encoded", "base64");
		    request.send(data); 
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
	$('#closeCustCarePopBox').click(function(){
		$(this).parent().parent('.issueQuryPopUp').hide();
	});
    $('.selectedProduct').click(function(){
    	$(this).next('.orderDrop').toggle();
    });
	$('.selectOrders .orderDrop li').each(function(){
		$(this).click(function(){
          var prodHtml = $(this).html();
          $(this).parent('.orderDrop').prev('.selectedProduct').addClass('filled').html(prodHtml);
          $('.orderDrop').toggle();
          //set value 
          $('#orderCode').val($(this).attr("data-orderCode"));
          $('#subOrderCode').val($(this).attr("data-subOrderCode"));
          $('#transactionId').val($(this).attr("data-transactionId"));
          
		});

	});
	/*custum selecbox*/
	 $(".customSelect").each(function(){
            $(this).wrap("<span class='customSelectWrap'></span>");
            $(this).after("<span class='holder'></span>");
            $(".holder").removeClass('active');
        });
        $(".customSelect").change(function(){
            var selectedOption = $(this).find(":selected").text();
            $(this).next(".holder").addClass('active');
            $(this).next(".holder").text(selectedOption);
        }).trigger('change');
         $(".holder").removeClass('active');
});


