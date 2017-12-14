ACC.WebForm = {

	sendTicket : function() {
		$(".webfromTicketSubmit").click(function(e) {
			e.preventDefault();

			if (ACC.WebForm.validateCRMForm()) {

				var dataStr = $("#customerWebForm").serialize();
				$.ajax({
					url : ACC.config.encodedContextPath+ "/ticketForm",
					data : dataStr,
					type : 'POST',
					success : function(data) {
						ACC.colorbox.open("", {
							html : data,
							width : "500px"
						});
					},
					error : function(resp) {
						console.log("Error in crmChildNodes"+ resp);
					}
				});

			} else {
				$div = $('.customCareError');
				$div.html('<div class="error">Please fill up the mandatory fields.</div>');
			}

		});

	},
	validateCRMForm : function() {

		var isValid = true;
		$('#customerWebForm input, select, textarea').each(function() {

					if ($(this).val() === ''
							&& ($(this).attr("type") === 'radio'
									|| $(this).attr("type") === 'text')) {
						isValid = false;
						$(this).removeClass("feildSuccess");
						$(this).addClass("feildError");
						$(this).focus();
						//console.log("radio");
					}
					if ($(this).is("textarea") && $(this).val() === '') {
						isValid = false;
						$(this).removeClass("feildSuccess");
						$(this).addClass("feildError");
						$(this).focus();
						//console.log("textarea");
					}
					// for all nodes
					if (($(this).attr("name") === 'nodeL2' || $(this).attr("name") === 'nodeL3')
							&& $(this).attr("displayAllow") === true
							&& $(this).val() === '') {
						isValid = false;
						$(this).removeClass("feildSuccess");
						$(this).addClass("feildError");
						$(this).focus();
						//console.log("nodeL2");
					}
					// validation for l4 node
					if ($(this).attr("name") == 'nodeL4' && $(this).val() === '') {
						isValid = false;
						$("select[name='nodeL3']").removeClass("feildSuccess");
						$("select[name='nodeL3']").addClass("feildError");
						$("select[name='nodeL3']").focus();
						//console.log("nodeL4");
					}
					
					// mobile validation
					if ($(this).attr("name") == 'contactMobile') {
						var mobNum = $(this).val();
						var filter = /^\d*(?:\.\d{1,2})?$/;
						if (filter.test(mobNum)) {
							if (mobNum.length == 10) {
								$(this).removeClass("feildError");
								$(this).addClass("feildSuccess");
							} else {
								$(this).removeClass("feildSuccess");
								$(this).addClass("feildError");
								isValid = false;
								$(this).focus();
								//console.log("mobile 1");
							}
						} else {
							$(this).removeClass("feildSuccess");
							$(this).addClass("feildError");
							isValid = false;
							$(this).focus();
							//console.log("mobile 2");
						}
					}
					// email validation
					if ($(this).attr("name") == 'contactEmail') {
						var email = $(this).val();
						var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
						if (emailReg.test(email)) {
							$(this).removeClass("feildError");
							$(this).addClass("feildSuccess");
						} else if(email==='' || email!== undefined){
							$(this).removeClass("feildSuccess");
							$(this).addClass("feildError");
							isValid = false;
							$(this).focus();
							//console.log("email");
						}
					}
					

				});
		return isValid;
	},
	issueDropDown : function() {

		$(".node").on("change",	function(e) {
			// for select dropdown
			var nodeValue = $("option:selected", this).val();
			var nodeText = $("option:selected", this).attr("nodeText");
			var nodeDisplayAllowed = $("option:selected", this).attr("nodeDisplayAllowed");
			var createTicketAllowed = $("option:selected", this).attr("createTicketAllowed");
			var ticketAnswer = $("option:selected", this).attr("ticketAnswer");
			
			var nodeType = $(this).attr("name");
			// for radio
			if (nodeValue == undefined) {
				nodeValue = $(this).val();
			}
			if(nodeValue!=='' && nodeValue.indexOf("Select") == -1){
				var htmlOption = "<option value=''>Select</option>";
				$.ajax({
					url : ACC.config.encodedContextPath
							+ "/ticketForm/crmChildrenNodes?nodeParent="
							+ nodeValue,
					type : 'GET',
					success : function(data) {
						if (nodeType.startsWith("nodeL1")) {
							$.each(data.nodes, function(index, data) {
								htmlOption += "<option value='"+ data.nodeCode+ "' " +
								" nodeText='"+ data.nodeDesc+ "' " +
								" nodeDisplayAllowed='"+ data.nodeDisplayAllowed + "' "+
								" createTicketAllowed='"+ data.createTicketAllowed + "' ";
								if(data.ticketAnswer!=null){
									htmlOption+=" ticketAnswer='"+data.ticketAnswer+ "' ";
								}
								htmlOption+=" >"+ data.nodeDesc+ "</option>";
								
							});
							$("select[name=nodeL2]").html(htmlOption);
							$("select[name=nodeL3]").show();
						}
						if (nodeType.startsWith("nodeL2")) {
							var check = false;
							
							$.each(data.nodes, function(index, data) {
	
								htmlOption += "<option value='"+ data.nodeCode+ "' " +
								" nodeText='"+ data.nodeDesc+ "' " +
								" nodeDisplayAllowed='"+ data.nodeDisplayAllowed + "' "+
								" createTicketAllowed='"+ data.createTicketAllowed + "' "; 
								if(data.ticketAnswer!=null){
									htmlOption+=" ticketAnswer='"+data.ticketAnswer+ "' ";
								}
								htmlOption+=" >"+ data.nodeDesc+ "</option>";
								
								check= true;
							});
	
							$("select[name=nodeL3]").html(htmlOption);
							$("#nodeL2Text").val(nodeText);
							
							if(check){
								$("#subIssueDiv").show();
							}
							
						}
						if (nodeType.startsWith("nodeL3")) {
							var l4val = "";
							var ticketType = "";
							$.each(data.nodes, function(index, data) {
								l4val = data.nodeCode;
								ticketType=data.ticketType;
							});
							
							//console.log(l4val);
							$("#nodeL4").val(l4val);
							$("#ticketType").val(ticketType);
							$("#nodeL3Text").val(nodeText);
						}
					},
					error : function(resp) {
						console.log("Error in crmChildNodes" + resp);
					}
				});
			}
			
			if (createTicketAllowed && nodeType.startsWith("nodeL3")) {
				$('#subIssueDiv').after(ticketAnswer);
			}
			
			if(!createTicketAllowed){
				$(".webfromTicketSubmit").prop('disabled',true);
			}
			ACC.WebForm.attachSelectEvent();
		});

	},
	simpleAjaxUpload : function(){
		var uploadurl = ACC.config.encodedContextPath + "/ticketForm/fileUpload";

		$("#attachmentFile").on("change",function(e) {
			e.preventDefault();
		    //Disable submit button
		    $(".webfromTicketSubmit").prop('disabled',true);
		    
		    var file = $(this)[0].files[0];
		    var formData = new FormData();
		    formData.append('uploadFile', file);
		    
		    // Ajax call for file uploaling
		    var ajaxReq = $.ajax({
		      url : uploadurl,
		      type : 'POST',
		      data : formData,
		      async: false,
	        cache: false,
	        contentType: false,
	        enctype: 'multipart/form-data',
	        processData: false,
		      xhr: function(){
		        //Get XmlHttpRequest object
		         var xhr = $.ajaxSettings.xhr() ;
		        
		        //Set onprogress event handler 
		         xhr.upload.onprogress = function(event){
		          	var perc = Math.round((event.loaded / event.total) * 100);
		          	$('#progressBar').text(perc + '%');
		          	$('#progressBar').css('width',perc + '%');
		         };
		         return xhr ;
		    	},
		    	beforeSend: function( xhr ) {
		    		//Reset alert message and progress bar
		    		$('#customCareError').text('');
		    		$('#progressBar').text('');
		    		$('#progressBar').css('width','0%');
		         }
		    });
		  
		    // Called on success of file upload
		    ajaxReq.done(function(data) {
		      $('#attachmentFile').val('');
		      $('.webfromTicketSubmit').prop('disabled',false);
		      
		      if (data!=='error') {
					// the uploaded file
					var inputHidden=$('<input id="attachmentFiles" type="hidden" name="attachmentFiles[]" />').val(data);
					$("#customerWebForm").append(inputHidden);
				} else {
					// our application returned an error
					var errorDiv = $('<div class="error"></div>').text("File not uploaded!!!");
					$("#customCareError").append(errorDiv);
				}
		    });
		    
		    // Called on failure of file upload
		    ajaxReq.fail(function(jqXHR) {
		      $('#customCareError').text(jqXHR.responseText+'('+jqXHR.status+
		      		' - '+jqXHR.statusText+')');
		      $('.webfromTicketSubmit').prop('disabled',false);
		    });
		  });
		 
	},
	loadOrderLines : function(currentPage) {
		var htmlOption="";
		$.ajax({
			url : ACC.config.encodedContextPath+ "/ticketForm/webOrderlines",
			type : 'GET',
			success : function(data) {
				if( $.isArray(data.orderLineDatas) ||  data.orderLineDatas.length ) {
					$.each(data.orderLineDatas, function(index, dataLine) {
	
						htmlOption += "<li data-orderCode='"+ dataLine.orderCode+"' "+
										"data-subOrderCode='"+ dataLine.subOrderCode+"' "+
										"data-transactionId='"+dataLine.transactionId +"'>" +
										"<div class='prodImg'><img src='"+ dataLine.prodImageURL+"' alt='' /></div>"+
											"<div class='prodInfo'>"+
												"<div class='prodTxt'>"+
													"<p class='orderDate'>Order on: "+ dataLine.orderDate+"</p>"
													"<p class='prodName'>"+dataLine.prodTitle +"</p>"+
													"<p class='prodPrice'>Price: "+ dataLine.prodTotalPrice+"</p>"+
													"<span class='prodShiping'>"+dataLine.customerOrderStatus+"</span>"+
												"</div></div></li>";
						
					});
					
					$(".orderDrop").html(htmlOption);
					ACC.WebForm.attachOrderDropEvent();
				}
				$("#totalPages").val(data.totalOrderLines);
				$("#currentPage").val(currentPage);
				// no of orderlins in one page
				if(data.totalOrderLines > 5){
					$('#viewMoreLink').show();
				}
				if(currentPage > 1 ){
					$('#viewBackLink').show();
				}
				
				
			},
			error : function(resp) {
				console.log("Error in crmChildNodes"+ resp);
			}
		});
	},
	loadPaginationLink : function() {
		var current=$('#currentPage').val();
		var total=$('#totalPages').val();
		
		if ( $(".selectOrderSec").length ) {
			if(parseInt(total) <= parseInt(current) ){
				$('#viewMoreLink').attr("href","ACC.WebForm.loadOrderLines('"+parseInt(current + 1)+"')");
			}else{
				$('#viewMoreLink').hide();
			}
			
			if(parseInt(current) > 1){
				$('#viewBackLink').attr("href","ACC.WebForm.loadOrderLines('"+parseInt(current - 1)+"')");
			}else{
				$('#viewBackLink').hide();
			}
			//call default first page
			ACC.WebForm.loadOrderLines(current);
			ACC.WebForm.attachOrderDropEvent();
		}
	},
	attachOrderDropEvent : function(){
		$('.selectOrders .orderDrop li').each(function(){
			$(this).on("click",function(){
				var prodHtml = $(this).html();
				$('.orderDrop').prev('.selectedProduct').addClass('filled').html(prodHtml);
				$('.orderDrop').toggle();
				// set value
				$('#orderCode').val($(this).attr("data-orderCode"));
				$('#subOrderCode').val($(this).attr("data-subOrderCode"));
				$('#transactionId').val($(this).attr("data-transactionId"));
			});

		});
	},
	attachSelectEvent : function(){
		/*custum selecbox*/
		 $(".customSelect").each(function(){
	            $(this).wrap("<span class='customSelectWrap'></span>");
	            $(this).after("<span class='holder'></span>");
	            $(".holder").removeClass('active');
	        });
	    $(".holder").removeClass('active');
	},

};

$(document).ready(function() {

	ACC.WebForm.simpleAjaxUpload();
	ACC.WebForm.issueDropDown();
	ACC.WebForm.sendTicket();
	ACC.WebForm.loadPaginationLink();
	//ACC.WebForm.attachOrderDropEvent();
	ACC.WebForm.attachSelectEvent();
	
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
	
	$('.selectedProduct').on("click",function(){
    	$('.orderDrop').toggle();
    });
	 $(".customSelect").change(function(){
	        var selectedOption = $(this).find(":selected").text();
	        $(this).next(".holder").addClass('active');
	        $(this).next(".holder").text(selectedOption);
	    }).trigger('change');
         
});
