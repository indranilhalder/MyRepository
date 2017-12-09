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
		
		var isValid = true;
		  $('.formControl').each(function() {
		    if ( $(this).val() === '' )
		        isValid = false;
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
					        htmlOption+="<option value='"+data.nodeCode+"'>"+data.nodeDesc + "</option>";
					    });
					    $("select[name=nodeL2]").html(htmlOption);
					}
					if (nodeType.startsWith("nodeL2")) {
						var check=false;
						var answer="";
						$.each(data.nodes, function (index, data) {
							
							if(data.nodeDisplayAllowed==true){	
								htmlOption+="<option value='"+data.nodeCode+"'>"+data.nodeDesc + "</option>";
							}else{
								check=true;
								answer=data.ticketAnswer;
							}
					    });
						if(check==false){
							$("select[name=nodeL3]").html(htmlOption);
						}else{
							$("select[name=nodeL3]").hide();
							$("select[name=nodeL3]").parent(".customSelectWrap").append("<p>"+answer+"</p>");
						}
						
					    $("#nodeL2Text").val(nodeText);
					}
					if (nodeType.startsWith("nodeL3")) {
						var val="";
						$.each(data.nodes, function (index, data) {
					        val=data.nodeCode;
					    });
					    $("#nodeL4").val(val);
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
				  var file = this.files[0];
				  var fd = new FormData();
				  fd.append("uploadFile", file);
				  // These extra params aren't necessary but show that you can include other data.
				  //fd.append("username", "Groucho");
				  //fd.append("accountnum", 123456);
				  var xhr = new XMLHttpRequest();
				  xhr.open('POST', url, true);
				  
				  xhr.upload.onprogress = function(e) {
				    if (e.lengthComputable) {
				      var percentComplete = (e.loaded / e.total) * 100;
				      console.log(percentComplete + '% uploaded');
				    }
				  };
				  xhr.onload = function() {
				    if (this.status == 200) {
				      var resp = JSON.parse(this.response);
				      console.log('Server got:', resp);
				      //var image = document.createElement('img');
				     // image.src = resp.dataUrl;
				      //document.body.appendChild(image);
				    };
				  };
				  xhr.send(fd);
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


