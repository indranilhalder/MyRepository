$(document).ready(function(){
		$(document).on("click", "#callMe", function(e) {
			e.preventDefault();
			var url = $(this).attr('href');
			$.get(url, function(data) {
				$(data).modal();
			}).success(function(){
				//console.log("Modal loaded");
				//ACC.click2call.bindClick2CallForm();
				//ACC.click2call.clickToCallModalEvents();
			});
		});
	
	//spinner: $("<img src='" + ACC.config.commonResourcePath + "/images/spinner.gif' />"),
	

		/*
		 * AJAX call for generating OTP
		 * 
		 */
		$(document).on("click","#generateOTPBtn",function(){
			$(".error").each(function(){
				$(this).empty();
			});
			$.ajax({
				url: ACC.config.encodedContextPath+"/clickto/generateOTP",
				type:"GET",
				dataType:"JSON",
				data:$("#generateOTP").serialize(),
				success:function(data){
					if(data.otp_generated == "true"){
						$("#validateOTP").show();
						$("#generateOTP").hide();
						//TISPRDT-111 fix
						$("#c2cEmailId").val(data.emailId);
						$("#contactNo").val(data.contactNo);
						$("#customerName").val(data.customerName);
						$("#reason").val(data.reason);
						
					} if(data.error_name != null){
						$("label[for=errorCustomerName]").text(data.error_name);
					} if(data.error_email != null){
						$("label[for=errorCustomerEmail]").text(data.error_email);
					} if(data.error_contact != null){
						$("label[for=errorCustomerMobileNo]").text(data.error_contact);
					}
				},
				fail:function(fail){
					alert("Sorry we are unable to connect to Click 2 Call service. Please try again later.");
				}
			});
		});
		/*
		 * Validate the OTP
		 */
		$(document).on("click","#validateOTPBtn",function(){
			$(".error").each(function(){
				$(this).empty();
			});
			$.ajax({
				url:ACC.config.encodedContextPath+"/clickto/validateOTP",
				type:"GET",
				dataType:"JSON",
				data:$("#validateOTP").serialize(),
				success:function(data){
					if (data.valid_otp == "true" && data.click_to_call_response!= null) {
						/*TPR-691*/
						var selectedOption = $('select[name="reason"] option:selected').val();
						if(selectedOption=="Order enquiry/ Place or cancel order")
						  {
							utag.link(
								{link_obj: this,link_text: 'support_call_Order_enquiry_Place_or_cancel_order_otp_success', event_type : 'support_call_click'}
								);
						  }
						else if(selectedOption=="Return  Product")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Return_Product_otp_success', event_type : 'support_call_click'}
									);
						  }
						else if(selectedOption=="Refund Enquiry")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Refund_Enquiry_otp_success', event_type : 'support_call_click'}
									);
						  }
						else if(selectedOption=="Product Information")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Product_Information_otp_success', event_type : 'support_call_click'}
									);
						  }
						else
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Other_Assitance_otp_success', event_type : 'support_call_click'}
									);
						  }
						/*TPR-691*/
						$("#validateOTP").hide();
						$("#generateOTP").hide();
						
						var responseXML = $.parseXML(data.click_to_call_response);
						var statusMessage = $(responseXML).find("statusMessage");
						var status = $(statusMessage).find("status");
						var ewt = $(statusMessage).find("ewt");
						if($(status).text()!= "undefined" && $(status).text()!= ""){
							$(".validOTP").show();
							$(".validOTP p").append($(ewt).text()+" secs");
						}
					}else if(data.invalid_otp != null){
						$("label[for=errorOTP]").text(data.invalid_otp);
						/*TPR-691*/
						//alert("hi"+selectedOption);
						
						var selectedOption = $('select[name="reason"] option:selected').val();
						if(selectedOption=="Order enquiry/ Place or cancel order")
						  {
							utag.link(
								{link_obj: this,link_text: 'support_call_Order_enquiry_Place_or_cancel_order_invalid_otp', event_type : 'support_call_click'}
								);
						  }
						else if(selectedOption=="Return  Product")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Return_Product_invalid_otp', event_type : 'support_call_click'}
									);
						  }
						else if(selectedOption=="Refund Enquiry")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Refund_Enquiry_invalid_otp', event_type : 'support_call_click'}
									);
						  }
						else if(selectedOption=="Product Information")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Product_Information_invalid_otp', event_type : 'support_call_click'}
									);
						  }
						else
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Other_Assitance_invalid_otp', event_type : 'support_call_click'}
									);
						  }
						/*TPR-691*/
					}else if(data.error_otp!= null){
						$("label[for=errorOTP]").text(data.error_otp);
						/*TPR-691*/
						var selectedOption = $('select[name="reason"] option:selected').val();
						if(selectedOption=="Order enquiry/ Place or cancel order")
						  {
							utag.link(
								{link_obj: this,link_text: 'support_call_Order_enquiry_Place_or_cancel_order_empty_otp', event_type : 'support_call_click'}
								);
						  }
						else if(selectedOption=="Return  Product")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Return_Product_empty_otp', event_type : 'support_call_click'}
									);
						  }
						else if(selectedOption=="Refund Enquiry")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Refund_Enquiry_empty_otp', event_type : 'support_call_click'}
									);
						  }
						else if(selectedOption=="Product Information")
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Product_Information_empty_otp', event_type : 'support_call_click'}
									);
						  }
						else
						  {
							utag.link(
									{link_obj: this,link_text: 'support_call_Other_Assitance_empty_otp', event_type : 'support_call_click'}
									);
						  }
					}/*TPR-691*/
					
					if(data.hasOwnProperty("click_to_call_response") && data.click_to_call_response.length == 0){
						$(".mandetoryFieldMissing p").empty();
						$(".mandetoryFieldMissing").show();
						$(".mandetoryFieldMissing p").text("Sorry we are unable to connect to the service. Please try again later");
					}
					$("#validateOTPBtn").removeAttr("disabled");
					$("#validateOTPBtn").parent().find("#spinner").remove();
				},
				beforeSend:function(){
					$("#validateOTPBtn").attr("disabled","disabled");
					$("#validateOTPBtn").parent().append("<img style='position: absolute; left: 29%;' id='spinner' src='" + ACC.config.commonResourcePath + "/images/spinner.gif' />");
				},
				fail:function(fail){
					alert("Sorry we are unable to connect to Click 2 Call service. Please try again later.");
				}
			});
		});
	
		$(document).on('hide.bs.modal', function () {
            $("#clicktoCallModal").remove();
            $(".modal-backdrop.in").remove();
		});
});
