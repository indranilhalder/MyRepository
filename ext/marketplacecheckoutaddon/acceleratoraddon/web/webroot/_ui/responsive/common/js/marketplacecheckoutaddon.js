var isCodSet = false;	//this is a variable to check whether convenience charge is set or not
var binStatus= false;
//var promoAvailable=$("#promoAvailable").val();
//var bankAvailable=$("#bankAvailable").val();

//Display forms based on mode button click
$("#viewPaymentCredit").click(function(){
	$("body").append("<div id='no-click' style='opacity:0.40; background:transparent; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	displayCreditCardForm();
});

$("#viewPaymentDebit").click(function(){
	$("body").append("<div id='no-click' style='opacity:0.40; background:transparent; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	displayDebitCardForm();
});

$("#viewPaymentNetbanking").click(function(){
	$("body").append("<div id='no-click' style='opacity:0.40; background:transparent; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	displayNetbankingForm();
});

$("#viewPaymentCOD").click(function(){
	$("body").append("<div id='no-click' style='opacity:0.40; background:transparent; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	displayCODForm();
});

$("#viewPaymentEMI").click(function(){
	$("body").append("<div id='no-click' style='opacity:0.40; background:transparent; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	displayEMIForm();
}); 
//Mode button click function ends

//Disabling Paste
$('.security_code').on('paste', function () {
	return false;
});

$('#pincode').on('paste', function () {
	return false;
});
//Disabling Paste Ends



function refresh(){
	$(".pay button").prop("disabled",false);
	$(".pay button").css("opacity","1");
	$(".pay .spinner").remove();
	
	$("#paymentMode, #bankNameForEMI, #selectedTerm, #bankCodeSelection").val("select");
	document.getElementById('silentOrderPostForm').reset();
	$(".card_number, .name_on_card, #cardType, #otpNUMField, .security_code").val("");
	$(".card_exp_month").val("month");	
	$(".card_exp_year").val("year");	
	$(".juspay_locker_save").attr('checked', false);
	$('.form-group span').text('');
	$(".new-card").find("#is_emi").val("false");
	$(".new-card").find("#emi_bank").val("");
	$(".new-card").find("#emi_tenure").val("");
	$("#is_emi").val("false");
	$("#emi_bank").val("");
	$("#emi_tenure").val("");
	$("#COD, #emi, #netbanking, #card, #paymentFormButton, #submitPaymentFormButton, #submitPaymentFormCODButton, #mobileNoError, #OTPGenerationErrorMessage, #codMessage, #customerBlackListMessage, #otpValidationMessage, #wrongOtpValidationMessage, #expiredOtpValidationMessage, #fulfillmentMessage, #codItemEligibilityMessage, #emptyOTPMessage, #resendOTPMessage").css("display","none");
	$("#netbankingError, .savedCard, .newCard, #emiRangeError, #juspayconnErrorDiv").css("display","none");
	$("#bankNameForEMI, #listOfEMiBank, #netbankingIssueError, #emiPromoError").css("display","none");
	$("#convChargeFieldId, #convChargeField").css("display","none");
	$(".card_ebsErrorSavedCard, .card_cvvErrorSavedCard, #maestroMessage, #newMaestroMessage").css("display","none");
	$("").css("display","none");
	hideTable();
	var selection = document.silentOrderPostForm.EMIBankCode;
	if(selection!=undefined){
		for (i=0; i<selection.length; i++){
			selection[i].checked = false;
		}
	}
	$('#savedEMICard').find(".credit-card-group").remove();
	var selection2 = $(".NBBankCode");
	for (i=0; i<selection2.length; i++){
		selection2[i].checked = false;
	}
	$("input[name=creditCards]:radio").first().prop("checked", false);
	$("input[name=debitCards]:radio").first().prop("checked", false);
	$("input[name=emiCards]:radio").first().prop("checked", false);
	$("input[name=debitCards]:radio.card_token,input[name=creditCards]:radio.card_token,input[name=emiCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
	$(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
	$(".card_token_hide").parent().find('.card_bank').removeClass("card_bank").addClass("card_bank_hide");
	$(".card_token_hide").parent().find('.card_brand').removeClass("card_brand").addClass("card_brand_hide");
	$(".card_token_hide").parent().find('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
	$(".card_token_hide").parent().find('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
	$(".card_token_hide").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
	//TISEE-5555
	$('.security_code_hide').prop('disabled', true);
	$('.security_code').prop('disabled', false); 
}




function displayNetbankingForm(){
	refresh();
	$("#paymentMode").val("Netbanking");
	//resetConvCharge();	TISPT-29
	$('input:password').val("");
	$(".name_on_card").val("");	
	applyPromotion(null);
	$("#paymentDetails, #netbanking, #make_nb_payment").css("display","block");
	$("#submitButtons, #paymentFormButton, #submitPaymentFormButton, #submitPaymentFormCODButton").css("display","none");
}




function displayEMIForm(){
	refresh();
	var select = document.getElementById("bankNameForEMI");
	var length = select.options.length;
	for (i = 0; i < length; i++) {
	  select.options[i] = null;
	}
	$("#bankNameForEMI option").remove();
	$("#paymentMode").val("EMI");
	$('input:password').val("");
	$(".name_on_card").val("");	
	applyPromotion(null);
	//resetConvCharge();	TISPT-29
	$("#paymentDetails, #emi").css("display","block");
	$("#emi-notice").hide();
	$("#emiNoBankError").hide();
}




function displayCODForm()
{
	var codEligible=$("#codEligible").val();
	refresh();
	$("#paymentMode").val("COD");
	var paymentMode=$("#paymentMode").val();
	$("#COD, #paymentDetails, #otpNUM, #sendOTPNumber, #sendOTPButton").css("display","block");
	/*$("#enterOTP, #submitPaymentFormButton, #submitPaymentFormCODButton, .make_payment, #paymentFormButton, #otpSentMessage").css("display","block");*/	//Modified back as erroneously pushed by performance team
	$("#enterOTP, #submitPaymentFormButton, #submitPaymentFormCODButton, .make_payment, #paymentFormButton, #otpSentMessage").css("display","none");/*modified for pprd testing -- changing back*/
	//setCellNo();
	if(codEligible=="BLACKLISTED")
	{
		$("#customerBlackListMessage").css("display","block");
		$("#otpNUM").css("display","none");
		$("#otpSentMessage").css("display","none");
		$("#no-click").remove();
	}
	else if(codEligible=="NOT_TSHIP")
	{
		$("#fulfillmentMessage").css("display","block");
		$("#otpNUM").css("display","none");
		$("#otpSentMessage").css("display","none");
		$("#no-click").remove();
	}
	else if(codEligible=="ITEMS_NOT_ELIGIBLE")
	{
		$("#codItemEligibilityMessage").css("display","block");
		$("#otpNUM").css("display","none");
		$("#otpSentMessage").css("display","none");
		$("#no-click").remove();
	}
	else if(codEligible=="NOT_PINCODE_SERVICEABLE")
	{
		$("#codMessage").css("display","block");
		$("#otpNUM").css("display","none");
		$("#otpSentMessage").css("display","none");
		$("#no-click").remove();
	}
	else{
		if(isCodSet == false){
		   	$.ajax({
				url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/validateOTP",
				type: "GET",
				data: { 'paymentMode' : paymentMode },
				cache: false,
				success : function(response) {
					if(response==null){
						$(location).attr('href',ACC.config.encodedContextPath+"/cart"); //TISEE-510
					}
					else{
						$("#sendOTPNumber, #convCharge").css("display","block");
						var totalPrice=response.totalPrice.formattedValue;
						var convCharge=response.convCharge.formattedValue;
						$("#convChargeFieldId, #convChargeField").css("display","block");
						if(response.cellNo==""){
							$("#otpMobileNUMField").val("");
						}
						else{
							$("#otpMobileNUMField").val(response.cellNo);
						}
						if(response.convCharge.value!=0){
							document.getElementById("convChargeField").innerHTML=convCharge;
							//alert("Because you have selected COD, convenience charges have been added to your order amount")
							$("#convChargeMessage").css("display","inline-block");
						}
						else
						{
							document.getElementById("convChargeField").innerHTML="Free"; //TISPRD-146
							$("#convChargeMessage").css("display","none");
						}
						//document.getElementById("totalWithConvField").innerHTML=totalPrice; TISPT-29
						if(paymentMode!=null){
							applyPromotion(null);
						}
						isCodSet = true;
					}
				},
				error : function(resp) {
					alert("COD is not available at this time. Please select another payment mode and proceed");	
					$("#no-click").remove();
				}
			});
		}
		else{
			$("#convChargeFieldId, #convChargeField").css("display","block");
			$("#no-click").remove();
		}
	}
	//$("#no-click").remove();
}




function displayDebitCardForm(){
	refresh();
	$("#paymentMode").val("Debit Card");
	//resetConvCharge();	TISPT-29
	$('input:password').val("");
	$(".name_on_card").val("");	
	displayDCForm();
}



function displayDCForm(){
	$("#is_emi").val("false");
	$("#card, #dcHeader, #savedCard, #savedDebitCard, .make_payment").css("display","block");
	$("#ccHeader, #savedCreditCard, #billingAddress").css("display","none");
	$("input[name=debitCards]:radio.card_token,input[name=creditCards]:radio.card_token,input[name=emiCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
	$("input[name=debitCards]:radio").first().removeClass("card_token_hide").addClass("card_token");
	$(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
	$(".card_token_hide").parent().find('.card_bank').removeClass("card_bank").addClass("card_bank_hide");
	$(".card_token_hide").parent().find('.card_brand').removeClass("card_brand").addClass("card_brand_hide");
	$(".card_token_hide").parent().find('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
	$(".card_token_hide").parent().find('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
	$(".card_token_hide").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
	$(".card_token").parent().parent().parent().find(".cvv").find("security_code_hide").removeClass("security_code_hide").addClass("security_code");
	$(".card_token").parent().find('.card_bank_hide').removeClass("card_bank_hide").addClass("card_bank");
	$(".card_token").parent().find('.card_brand_hide').removeClass("card_brand_hide").addClass("card_brand");
	$(".card_token").parent().find('.card_is_domestic_hide').removeClass("card_is_domestic_hide").addClass("card_is_domestic");
	$(".card_token").parent().find('.card_ebsErrorSavedCard_hide').removeClass("card_ebsErrorSavedCard_hide").addClass("card_ebsErrorSavedCard");
	$(".card_token").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard_hide').removeClass("card_cvvErrorSavedCard_hide").addClass("card_cvvErrorSavedCard");
	if(document.getElementsByName("debitCards")[0]==undefined){
		$("#savedCard, #savedCreditCard, #savedDebitCard, .newCard, .savedCard, .saved-card-button").css("display","none");
		$("#newCard, .newCardPayment").css("display","block");
		$(".accepted-cards .maestro").parent().css("display","inline-block");
		$(".accepted-cards .visa").parent().css("display","inline-block");
		$(".accepted-cards .master").parent().css("display","inline-block");
		$(".accepted-cards .amex").parent().css("display","none");
		applyPromotion(null);
	}	
	else
	{
		$("#savedCreditCard, .savedCard, .newCardPayment, #newCard, .newCardPayment").css("display","none");
		$("#savedDebitCard, .saved-card-button").css("display","block");
		$(".newCard").css("display","table-cell");
		$("input[name=creditCards]:radio").first().prop("checked", false);
		$("input[name=debitCards]:radio").first().prop("checked", true);
		$(".card_token").parent().parent().parent().find(".cvv").find(".security_code_hide").removeClass("security_code_hide").addClass("security_code");
		$(".card_token").parent().find('.card_bank_hide').removeClass("card_bank_hide").addClass("card_bank"); 
		$(".card_token").parent().find('.card_brand_hide').removeClass("card_brand_hide").addClass("card_brand");
		$(".card_token").parent().find('.card_is_domestic_hide').removeClass("card_is_domestic_hide").addClass("card_is_domestic");
		$(".card_token").parent().find('.card_ebsErrorSavedCard_hide').removeClass("card_ebsErrorSavedCard_hide").addClass("card_ebsErrorSavedCard");
		$(".card_token").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard_hide').removeClass("card_cvvErrorSavedCard_hide").addClass("card_cvvErrorSavedCard");
		$("#card li.header ul").append($("#emi li.newCard"));
		$("#card li.header ul").append($("#emi li.savedCard"));
		if($(".card_bank").val()=="AMEX")
		{
			$(".security_code").attr('maxlength','4');
		}
		else
		{
			$(".security_code").attr('maxlength','3');
		}
		var bankName=$('.card_bank').val();
		setBankForSavedCard(bankName);
		if($(".card_brand").val()=="MAESTRO"){
			$("#maestroMessage").css("display","block");
		}
		else{
			$("#maestroMessage").css("display","none");
		}
	}
	
	//TISEE-5555
	$('.security_code_hide').prop('disabled', true);
	$('.security_code').prop('disabled', false); 
}




function displayCreditCardForm(){		
	refresh();
	$("#paymentMode").val("Credit Card");
	$("#is_emi").val("false");
	$("#card, #ccHeader").css("display","block");
	$("#dcHeader").css("display","none");	
	//resetConvCharge();	TISPT-29
	$('input:password').val("");
	$(".name_on_card").val("");	
	displayFormForCC();
}




function submitForm(){
	if($("#paymentMode").val()=="Netbanking")
	{
		var code=bankCodeCheck();
		if(code){
			submitNBForm();
		}
		else{
			$("#netbankingIssueError").css("display","block");
		}
	}
	else if($("#paymentMode").val()=="COD"){
		var otpNUMField= $('#otpNUMField').val();
		if(otpNUMField=="")
		{
			$("#otpNUM, #sendOTPNumber, #emptyOTPMessage, #otpSentMessage").css("display","block");
		}
		else
		{
			$("#otpNUM, #sendOTPNumber, #paymentFormButton, #sendOTPButton, #otpSentMessage").css("display","block");
			$("#emptyOTPMessage").css("display","none");
		$.ajax({
			url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/validateOTP/"+otpNUMField,
			type: "POST",
			cache: false,		
			success : function(response) {
				if(response=='redirect'){
					$(location).attr('href',ACC.config.encodedContextPath+"/cart"); //TIS 404
				}else{
					$("#emptyOTPMessage").css("display","none");
					if(response!=null)
					{
						if(response=="INVALID")
						{
							$("#otpNUM, #sendOTPNumber, #enterOTP, #wrongOtpValidationMessage").css("display","block");		
							$("#expiredOtpValidationMessage").css("display","none");
							$("#otpSentMessage").css("display","none");
						}
						else if(response=="EXPIRED")
						{
							$("#otpNUM, #sendOTPNumber, #enterOTP, #expiredOtpValidationMessage").css("display","block");
							$("#wrongOtpValidationMessage").css("display","none");	
							$("#otpSentMessage").css("display","none");
						}
						else{
							
							//TISPRO-153
							sendTealiumData();
														
							$("#form-actions, #otpNUM").css("display","block");
							$("#wrongOtpValidationMessage, #expiredOtpValidationMessage").css("display","none");
							$("#otpSentMessage").css("display","none");
							$(".pay .payment-button").prop("disabled",true);
							$(".pay .payment-button").css("opacity","0.5");
							$(".pay").append('<img src="/store/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: absolute; right: 25%;bottom: 10px; height: 30px;">');
							$(".pay .spinner").css("left",(($(".pay#paymentFormButton").width()+$(".pay#paymentFormButton .payment-button").width())/2)+10);
							$("body").append("<div id='no-click' style='opacity:0.65; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
							$("#silentOrderPostForm").submit();
						}
					}
					else
					{
						alert("Error validating OTP. Please select another payment mode and proceed");
						$(".pay button").prop("disabled",false);
						$(".pay button").css("opacity","1");
						$(".pay .spinner").remove();
						$("#no-click").remove();
					}
				}
			},
			error : function(resp) {
				alert("Error validating OTP. Please select another payment mode and proceed");
				$(".pay button").prop("disabled",false);
				$(".pay button").css("opacity","1");
				$(".pay .spinner").remove();
				$("#no-click").remove();
				
			}
		});
		}
	}
	else
		alert("Please make valid selection and proceed");
}



//TISEE-5555
//$("#savedDebitCard .debit-card-group .cvvValdiation").focus(function(){
//$("#savedDebitCard .debit-card-group").focus(function(){
//	//TISBOX-1732
//    $(".card_cvvErrorSavedCard, .card_cvvErrorSavedCard_hide, .card_ebsErrorSavedCard, .card_ebsErrorSavedCard_hide").css("display","none");
//	$("input[name=debitCards]:radio.card_token,input[name=creditCards]:radio.card_token,input[name=emiCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
//	$(this).parent().parent().find(".card").find(".radio").find(".card_token_hide").removeClass("card_token_hide").addClass("card_token");	
//	$(this).parent().parent().find(".card").find(".radio .debitCardsRadio").click();
//	$(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
//	$(".card_token_hide").parent().find('.card_bank').removeClass("card_bank").addClass("card_bank_hide");
//	$(".card_token_hide").parent().find('.card_brand').removeClass("card_brand").addClass("card_brand_hide");
//	$(".card_token_hide").parent().find('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
//	$(".card_token_hide").parent().find('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
//	$(".card_token_hide").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
//	$(".card_token").parent().parent().parent().find(".cvv").find(".security_code_hide").removeClass("security_code_hide").addClass("security_code");
//	$(".card_token").parent().find('.card_bank_hide').removeClass("card_bank_hide").addClass("card_bank");
//	$(".card_token").parent().find('.card_brand_hide').removeClass("card_brand_hide").addClass("card_brand");
//	$(".card_token").parent().find('.card_is_domestic_hide').removeClass("card_is_domestic_hide").addClass("card_is_domestic");
//	$(".card_token").parent().find('.card_ebsErrorSavedCard_hide').removeClass("card_ebsErrorSavedCard_hide").addClass("card_ebsErrorSavedCard");
//	$(".card_token").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard_hide').removeClass("card_cvvErrorSavedCard_hide").addClass("card_cvvErrorSavedCard");
//	$(".security_code_hide").val(null);
//	if($(".card_brand").val()=="MAESTRO"){
//		$("#maestroMessage").css("display","block");
//	}
//	else{
//		$("#maestroMessage").css("display","none");
//	}
//	
//	//TISEE-5555
//	$('.security_code_hide').prop('disabled', true);
//	$('.security_code').prop('disabled', false); 
//});

//TISEE-5555
//$("#savedCreditCard .credit-card-group .cvvValdiation").focus(function(){
//$("#savedCreditCard .credit-card-group").focus(function(){
//	//TISBOX-1732
//    $(".card_cvvErrorSavedCard, .card_cvvErrorSavedCard_hide, .card_ebsErrorSavedCard, .card_ebsErrorSavedCard_hide").css("display","none");
//	$("input[name=debitCards]:radio.card_token,input[name=creditCards]:radio.card_token,input[name=emiCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
//	$(this).parent().parent().find(".card").find(".radio").find(".card_token_hide").removeClass("card_token_hide").addClass("card_token");	
//	$(this).parent().parent().find(".card").find(".radio .creditCardsRadio").click();
//	$(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
//	$(".card_token_hide").parent().find('.card_bank').removeClass("card_bank").addClass("card_bank_hide");
//	$(".card_token_hide").parent().find('.card_brand').removeClass("card_brand").addClass("card_brand_hide");
//	$(".card_token_hide").parent().find('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
//	$(".card_token_hide").parent().find('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
//	$(".card_token_hide").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
//	$(".card_token").parent().parent().parent().find(".cvv").find(".security_code_hide").removeClass("security_code_hide").addClass("security_code");
//	$(".card_token").parent().find('.card_bank_hide').removeClass("card_bank_hide").addClass("card_bank");
//	$(".card_token").parent().find('.card_brand_hide').removeClass("card_brand_hide").addClass("card_brand");
//	$(".card_token").parent().find('.card_is_domestic_hide').removeClass("card_is_domestic_hide").addClass("card_is_domestic");
//	$(".card_token").parent().find('.card_ebsErrorSavedCard_hide').removeClass("card_ebsErrorSavedCard_hide").addClass("card_ebsErrorSavedCard");
//	$(".card_token").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard_hide').removeClass("card_cvvErrorSavedCard_hide").addClass("card_cvvErrorSavedCard");
//	$(".security_code_hide").val(null);
//	//TISEE-5555
//	$('.security_code_hide').prop('disabled', true);
//	$('.security_code').prop('disabled', false); 
//});
//TISEE-5555
//$(document).on("focus", "#savedEMICard .credit-card-group .cvvValdiation", function() {
//$(document).on("focus", "#savedEMICard .credit-card-group", function() {
//	//TISBOX-1732
//    $(".card_cvvErrorSavedCard, .card_cvvErrorSavedCard_hide, .card_ebsErrorSavedCard, .card_ebsErrorSavedCard_hide").css("display","none");
//	$("input[name=debitCards]:radio.card_token,input[name=creditCards]:radio.card_token,input[name=emiCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
//	$(this).parent().parent().find(".card").find(".radio").find(".card_token_hide").removeClass("card_token_hide").addClass("card_token");	
//	$(this).parent().parent().find(".card").find(".radio .emiCardsRadio").click();
//	$(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
//	$(".card_token_hide").parent().find('.card_bank').removeClass("card_bank").addClass("card_bank_hide");
//	$(".card_token_hide").parent().find('.card_brand').removeClass("card_brand").addClass("card_brand_hide");
//	$(".card_token_hide").parent().find('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
//	$(".card_token_hide").parent().find('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
//	$(".card_token_hide").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
//	$(".card_token").parent().parent().parent().find(".cvv").find(".security_code_hide").removeClass("security_code_hide").addClass("security_code");
//	$(".card_token").parent().find('.card_bank_hide').removeClass("card_bank_hide").addClass("card_bank");
//	$(".card_token").parent().find('.card_brand_hide').removeClass("card_brand_hide").addClass("card_brand");
//	$(".card_token").parent().find('.card_is_domestic_hide').removeClass("card_is_domestic_hide").addClass("card_is_domestic");
//	$(".card_token").parent().find('.card_ebsErrorSavedCard_hide').removeClass("card_ebsErrorSavedCard_hide").addClass("card_ebsErrorSavedCard");
//	$(".card_token").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard_hide').removeClass("card_cvvErrorSavedCard_hide").addClass("card_cvvErrorSavedCard");
//	$(".security_code_hide").val(null);
//	//TISEE-5555
//	$('.security_code_hide').prop('disabled', true);
//	$('.security_code').prop('disabled', false); 
//});


function hideErrorMsg(){
	$("#wrongOtpValidationMessage #expiredOtpValidationMessage").css("display","none");	
}



function bankCodeCheck(){
	$("#netbankingIssueError").css("display","none");
	var checkedValue;
	var selection = $(".NBBankCode");
	for (i=0; i<selection.length; i++)
		  if (selection[i].checked == true)
			  checkedValue=selection[i].value;
	var handle = $("#bankCodeSelection");
	var number = handle.value;	
	if(number=="select")
		if(checkedValue!=null){
			return true;
		}
		else{
			return false;
		}
	else{
		if(checkedValue!=null){
			return false;
		}
		else{
			return true;
		}
	}
}



function deselectRadio(){
	$("#netbankingIssueError").css("display","none");
	var handle = $("#bankCodeSelection");
	var number = handle.val();
	var selection = $(".NBBankCode");
	$("#netbankingError").css("display","none");
	if(number!="select"){
	for (i=0; i<selection.length; i++)
		  if (selection[i].checked == true){
			  checkedValue=selection[i].value;
			  selection[i].checked = false;
		  }
		setBankForSavedCard($("#bankCodeSelection option:selected").html());
		
	}
	else
		setBankForSavedCard(null);
		
}



function deselectSelection(){
	$("#netbankingIssueError").css("display","none");
	var handle = $("#bankCodeSelection");
	var number = handle.val();
	var selection = $(".NBBankCode");
	$("#netbankingError").css("display","none");
	for (i=0; i<selection.length; i++)
		  if (selection[i].checked == true){
			  var bankName=$("#NBBankName"+i).val();
			  $("#bankCodeSelection").val("select");
			  setBankForSavedCard(bankName);
	}
	//setBankForSavedCard(bankName);
}



function getSelectedEMIBank(){
	$("#emiPromoError").css("display","none");
	var selectedBank=$("select[id='bankNameForEMI']").find('option:selected').text();		
	$(".card_number, .name_on_card, .security_code, #cardType, #otpNUMField").val("");
	 $('ul.accepted-cards li').removeClass('active-card');
	$(".card_exp_month").val("month");	
	$(".card_exp_year").val("year");	
	$(".juspay_locker_save").attr('checked', false);
	$('.form-group span').text('');
	if(selectedBank!="Select"){
		//Code to reset the values
		setBankForSavedCard(selectedBank);
	}
	else{
		hideTable();
		//$("#COD, #emi, #netbanking, #card, #paymentFormButton, #submitPaymentFormButton, #submitPaymentFormCODButton, #mobileNoError, #OTPGenerationErrorMessage, #codMessage, #customerBlackListMessage, #otpValidationMessage, #wrongOtpValidationMessage, #expiredOtpValidationMessage, #fulfillmentMessage, #codItemEligibilityMessage, #emptyOTPMessage, #resendOTPMessage, #savedEMICard").css("display","none");
		
		$("#card, #emi-notice").css("display","none");
		applyPromotion(null);
	}
}



function hideTable(){
	$("#radioForEMI").css("display","none");
	$('#savedEMICard').find(".credit-card-group").remove();
	var emiTable=document.getElementById("EMITermTable");
	var rowCount = emiTable.rows.length;
	for(var i=rowCount-1; i>0; i--){
		emiTable.deleteRow(i);
		rowCount--;
	}
}



function validateSelection(){
	var elements = document.getElementsByName("termRadio");
	for (var i = 0; i < elements.length; i++)
    {
        if (elements[i].checked)
        {   
            $("#selectedTerm, #emi_tenure").val(elements[i].value);
            var selectedBank=$("#bankNameForEMI").val();
            $("#is_emi").val("true");
            $("#emi_bank").val(selectedBank);
        	$("#card").css("display","block");
        	$("#dcHeader, #savedCreditCard").css("display","none");
        	$("#ccHeader").css("display","block");
        	$('#savedEMICard').find(".credit-card-group").remove();
        	displayEMICards();
        }
    }
}


function displayEMICards(){
	var bankName=$("select[id='bankNameForEMI']").find('option:selected').text();
	$("input[name=debitCards]:radio.card_token,input[name=creditCards]:radio.card_token,input[name=emiCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");	
	$(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
	$(".card_token_hide").parent().find('.card_bank').removeClass("card_bank").addClass("card_bank_hide");
	$(".card_token_hide").parent().find('.card_brand').removeClass("card_brand").addClass("card_brand_hide");
	$(".card_token_hide").parent().find('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
	$(".card_token_hide").parent().find('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
	$(".card_token_hide").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
	$.ajax({
		url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/listEMICards",
		data: { 'bankName' : bankName },
		type: "GET",
		cache: false,
		success : function(myMap) {
			var len = Object.keys(myMap).length;
			$("#card").css("display","block");
			$("#card").find(".product-block").css("display","block");
			$("#card").find(".product-block").find(".header").css("display","table");
			$("#ccHeader").css("display","block");
			$("#dcHeader").css("display","none");
			$(".savedCard, .newCard").css("display","none");
			if(len=="0")
			{
				$("#savedCard, #savedCreditCard, #savedDebitCard, #savedEMICard").css("display","none");
				$("#billingAddress, .make_payment").css("display","block");
				$(".newCard, .savedCard, .saved-card-button").css("display","none");
				$("#newCard, .newCardPayment, .accepted-cards").css("display","block");
				$(".accepted-cards .maestro").parent().css("display","none");
				$(".accepted-cards .visa").parent().css("display","inline-block");
				$(".accepted-cards .master").parent().css("display","inline-block");
				$(".accepted-cards .amex").parent().css("display","none");
				populateBillingAddress();					
			}
			else{
				$("#savedCard, #savedEMICard,.saved-card-button, #make_saved_cc_payment").css("display","block");
				$(".newCard").css("display","table-cell");
				$("#savedCreditCard, #savedDebitCard").css("display","none");
				$("#newCard, .newCardPayment").css("display","none");
				
				var index=-1;
				var index1=0;
				$.each(myMap, function(i, val) {
					    index=index+1;
					    index1=index1+1;
					    $("#savedEMICard").css("display","block");
					    //alert($("#savedEMICard").find('#credit-card-group'+index1).find('.card').find('.radio').find('#ec1').val());
					    if($("#savedEMICard").find('#credit-card-group'+index1).find('.card').find('.radio').find('#ec'+index).val()==undefined){
					    $("#savedEMICard").append(
		                		$('<div />', {
		                			class: 'credit-card-group',
		                			id: 'credit-card-group'+index1
		                		})
		                );
					    $("#savedEMICard").find('#credit-card-group'+index1).append(
		                		$('<div />', {
		                			class: 'card',
		                			id: 'card'+index1
		                		})
		                );
					    $("#savedEMICard").find('#credit-card-group'+index1).append(
		                		$('<div />', {
		                			class: 'cvv right-align-form',
		                			id: 'cvv'+index1
		                		})
		                );
					    $("#savedEMICard").find('#credit-card-group'+index1).find('#card'+index1).append(
		                		$('<div />', {
		                			class: 'radio',
		                			id: 'radio'+index1
		                		})
		                );
					   $("#savedEMICard").find('#credit-card-group'+index1).find('#card'+index1).find('#radio'+index1).append(
				                $('<input />', {
				                    type: 'radio',
				                    name: 'emiCards',
				                    class: 'card_token emiCardsRadio',
				                    id: 'ec'+ index,
				                    value: val.cardToken
				                })
					    );
		                $("#savedEMICard").find('#credit-card-group'+index1).find('#card'+index1).find('#radio'+index1).append(
		                		$('<label />', {
		                			'text': val.cardBrand+' ending in ' + val.cardEndingDigits,
		                			 for: 'ec'+ index,
		                		})
		                );
		                $("#savedEMICard").find('#credit-card-group'+index1).find('#card'+index1).find('#radio'+index1).append(
		                		$('<p />', {
		                			'text': val.nameOnCard
		                		})
		                );
		                $("#savedEMICard").find('#credit-card-group'+index1).find('#card'+index1).find('#radio'+index1).append(
		                		$('<p />', {
		                			'text': 'Expires on: '+val.expiryMonth+'/'+val.expiryYear
		                		})
		                );
		                $("#savedEMICard").find('#credit-card-group'+index1).find('#card'+index1).find('#radio'+index1).append(
		                		$('<hidden />', {
		                			name: 'emiCardsBank',
		                			class: 'card_bank',
		                			value: val.cardIssuer,
		                			id: 'card_bank'+index
		                		})
		                );
		                $("#savedEMICard").find('#credit-card-group'+index1).find('#card'+index1).find('#radio'+index1).append(
		                		$('<hidden />', {
		                			name: 'emiCardsBrand',
		                			class: 'card_brand',
		                			value: val.cardBrand
		                		})
		                );
		                $("#savedEMICard").find('#credit-card-group'+index1).find('#card'+index1).find('#radio'+index1).append(
		                		$('<hidden />', {
		                			name: 'emiIsDomestic',
		                			class: 'card_is_domestic',
		                			value: val.isDomestic
		                		})
		                );
		                $("#savedEMICard").find('#credit-card-group'+index1).find('#card'+index1).find('#radio'+index1).append(
		                		$('<div />', {
		                			class: 'card_ebsErrorSavedCard error-message',
		                			id: "ebsErrorSavedCard"
		                		})
		                );
		                $("#savedEMICard").find('#credit-card-group'+index1).find('#card'+index1).find('#radio'+index1).find('.card_ebsErrorSavedCard').append(
		                		$('<spring:theme code="checkout.multi.paymentMethod.savedCard.ebsError"/>', {
		                		})
		                );		                
		                $("#savedEMICard").find('#credit-card-group'+index1).find('#cvv'+index1).append(
		                		$('<label />', {
		                			'text': 'CVV*'
		                		})
		                );
		                $("#savedEMICard").find('#credit-card-group'+index1).find('#cvv'+index1).append(
		                		$('<input type="password" class="cvvValdiation form-control cvv_show security_code_hide" maxlength="4" onkeypress="return isNumber(event)" onclick="hideError()" />', {
		                			id: 'cvv'+index
		                		})
		                );
		                $("#savedEMICard").find('#credit-card-group'+index1).find('#cvv'+index1).append(
		                		$('<br />', {
		                		})
		                );
		                $("#savedEMICard").find('#credit-card-group'+index1).find('#cvv'+index1).append(
		                		$('<div />', {
		                			class: 'card_cvvErrorSavedCard error-message',
		                			id: "cvvErrorSavedCard"
		                		})
		                );
		                $("#savedEMICard").find('#credit-card-group'+index1).find('#cvv'+index1).find('.card_cvvErrorSavedCard').html("Please enter a valid CVV");}
					});
				
				$("input[name=debitCards]:radio.card_token,input[name=creditCards]:radio.card_token,input[name=emiCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");	
				$(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
				$(".card_token_hide").parent().find('.card_bank').removeClass("card_bank").addClass("card_bank_hide");
				$(".card_token_hide").parent().find('.card_brand').removeClass("card_brand").addClass("card_brand_hide");
				$(".card_token_hide").parent().find('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
				$(".card_token_hide").parent().find('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
				$(".card_token_hide").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
					
				$("input[name=emiCards]:radio").first().prop("checked", true);
				$("input[name=emiCards]:radio").first().removeClass("card_token_hide").addClass("card_token");
				$(".card_token").parent().parent().parent().find(".cvv").find(".security_code_hide").removeClass("security_code_hide").addClass("security_code");
				$(".card_token").parent().find('.card_bank_hide').removeClass("card_bank_hide").addClass("card_bank");
				$(".card_token").parent().find('.card_brand_hide').removeClass("card_brand_hide").addClass("card_brand");
				$(".card_token").parent().find('.card_is_domestic_hide').removeClass("card_is_domestic_hide").addClass("card_is_domestic");
				$(".card_token").parent().find('.card_ebsErrorSavedCard_hide').removeClass("card_ebsErrorSavedCard_hide").addClass("card_ebsErrorSavedCard");
				$(".card_token").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard_hide').removeClass("card_cvvErrorSavedCard_hide").addClass("card_cvvErrorSavedCard");
			}
		},
		error : function(resp) {
			//alert("Some issues are there with Payment at this time. Please try payment later or contact out helpdesk");
		}
	});
	//TISEE-5555
	$('.security_code_hide').prop('disabled', true);
	$('.security_code').prop('disabled', false); 
}


function displayFormForCC(){
	$("#savedCard, #savedCreditCard, #billingAddress, .make_payment").css("display","block");
	$("#savedDebitCard").css("display","none");
	$("input[name=debitCards]:radio.card_token,input[name=creditCards]:radio.card_token,input[name=emiCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
	$("input[name=creditCards]:radio").first().removeClass("card_token_hide").addClass("card_token");
	$(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
	$(".card_token_hide").parent().find('.card_bank').removeClass("card_bank").addClass("card_bank_hide"); 
	$(".card_token_hide").parent().find('.card_brand').removeClass("card_brand").addClass("card_brand_hide");
	$(".card_token_hide").parent().find('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
	$(".card_token_hide").parent().find('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
	$(".card_token_hide").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
	if(document.getElementsByName("creditCards")[0]==undefined){
		$("#savedCard, #savedCreditCard, #savedDebitCard, .newCard, .savedCard, .saved-card-button").css("display","none");
		$("#newCard, .newCardPayment").css("display","block");
		$(".accepted-cards .maestro").parent().css("display","none");
		$(".accepted-cards .visa").parent().css("display","inline-block");
		$(".accepted-cards .master").parent().css("display","inline-block");
		$(".accepted-cards .amex").parent().css("display","inline-block");
		populateBillingAddress();
		if($("#paymentMode").val()!='EMI'){
			applyPromotion(null);
		}
		
	}
	else
	{
		if($("#paymentMode").val()!='EMI'){
			$(".card_token").parent().parent().parent().find(".cvv").find(".security_code_hide").removeClass("security_code_hide").addClass("security_code");
			$(".card_token").parent().find('.card_bank_hide').removeClass("card_bank_hide").addClass("card_bank"); 
			$(".card_token").parent().find('.card_brand_hide').removeClass("card_brand_hide").addClass("card_brand");
			$(".card_token").parent().find('.card_is_domestic_hide').removeClass("card_is_domestic_hide").addClass("card_is_domestic");
			$(".card_token").parent().find('.card_ebsErrorSavedCard_hide').removeClass("card_ebsErrorSavedCard_hide").addClass("card_ebsErrorSavedCard");
			$(".card_token").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard_hide').removeClass("card_cvvErrorSavedCard_hide").addClass("card_cvvErrorSavedCard");
			$("#savedCreditCard, .saved-card-button").css("display","block");
			$("#savedDebitCard, #newCard, .savedCard, .newCardPayment").css("display","none");
			$(".newCard").css("display","table-cell");
			$("input[name=debitCards]:radio").first().prop("checked", false);
			$("input[name=creditCards]:radio").first().prop("checked", true);
			$("#card li.header ul").append($("#emi li.newCard"));
			$("#card li.header ul").append($("#emi li.savedCard"));
			var bankName=$('.card_bank').val();
			if($(".card_brand").val()=="AMEX")
			{
				$(".security_code").attr('maxlength','4');
			}
			else
			{
				$(".security_code").attr('maxlength','3');
			}
			setBankForSavedCard(bankName);
		}
	}
	//TISEE-5555
	$('.security_code_hide').prop('disabled', true);
	$('.security_code').prop('disabled', false); 
}
  

function mobileBlacklist(){
	$("#sendOTPButton").append('<img src="/store/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: absolute; right: 25%;bottom: 0px; height: 30px;">');
	if($("#sendOTPButton #resendOTPMessage").css("display") == 'block') {
		$("#sendOTPButton .spinner").css("bottom","33px")
	}
	var number=$("#otpMobileNUMField").val();
	$.ajax({
		url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/mobileBlacklist",
		data: { 'mobileNumber' : number },
		type: "POST",
		cache: false,	
		success : function(response) {
			if(response==true)
			{
				generateOTP();
				$("#customerBlackListMessage").css("display","none");
				$("#otpSentMessage").css("display","none");
			}
			else{
				$("#enterOTP, #paymentFormButton, #resendOTPMessage").css("display","none");
				$("#customerBlackListMessage").css("display","block");
				$("#sendOTPButton .spinner").remove();
			}
		},
		error : function(resp) {
			$("#sendOTPButton .spinner").remove();
		}
	});
	
}



function generateOTP(){
	$("#submitButtons").css("display","none");
	var number=$("#otpMobileNUMField").val();
	var prefixBefore=$("#mobilePrefix").val();
	var prefix=prefixBefore.replace(/\D/g,'');
	var mobileNumber=number;
	$("#otpNUMField").val("");
	$("#wrongOtpValidationMessage, #expiredOtpValidationMessage").css("display","none");
	if(number.length!=10){
		$("#mobileNoError").css("display","block");
		$("#resendOTPMessage, #enterOTP, #paymentFormButton").css("display","none");
	}
	else if(number.charAt(0)=='0'){
		$("#mobileNoError").css("display","block");
	}
	else{
	$.ajax({
		url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/generateOTP",
		data: { 'mobileNumber' : mobileNumber, 'prefix' : prefix },
		type: "POST",
		cache: false,	
		success : function(response) {
			if(response=='redirect'){
				$(location).attr('href',ACC.config.encodedContextPath+"/cart"); //TIS 404
			}
			else if(response=="fail")
			{
				$("#OTPGenerationErrorMessage").css("display","block");
			}
			else{
				$("#codMessage").css("display","none");
				$("#otpNUM, #otpSentMessage, #sendOTPNumber, #enterOTP, #paymentFormButton, #submitPaymentFormCODButton, .make_payment, #sendOTPButton, #resendOTPMessage").css("display","block");
				
			}
			
			$("#sendOTPButton .spinner").remove();
		},
		error : function(resp) {
			$("#sendOTPButton .spinner").remove();
			alert("OTP cannot be generated at this time due to technical errors. Please select another payment mode and proceed");
		}
	});
	}
}



$("#otpMobileNUMField").focus(function(){
	$("#mobileNoError").css("display","none");
});


//TISPT-29 Commenting entire method and merging functionality with applypromotion
// function resetConvCharge()
// {
//	 var paymentMode=$("#paymentMode").val();
//	 $('input:password').val("");
//	 $(".name_on_card").val("");	
//	 $.ajax({
//		url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/resetConvCharge",
//		data: { 'paymentMode' : paymentMode },
//		type: "GET",
//		cache: false,
//		success : function(response) {
//			
//			if(response=='redirect'){
//				$(location).attr('href',ACC.config.encodedContextPath+"/cart"); //TISEE-510
//			}
//			else
//			{
//				var values=response.split("|");
//				var totalPrice=values[0];
//				var convCharge=values[1];
//				$("#convChargeFieldId, #convChargeField").css("display","none");
//				document.getElementById("convChargeField").innerHTML=convCharge;
//				document.getElementById("totalWithConvField").innerHTML=totalPrice;
//				isCodSet = false;	
//				if($("#paymentMode").val()=="Credit Card")
//				{
//					displayFormForCC();
//				}	
//				else if($("#paymentMode").val()=="Debit Card")
//				{
//					displayDCForm();
//				}
//				else if($("#paymentMode").val()=="EMI")
//				{
//					applyPromotion();
//				}
//				else if($("#paymentMode").val()=="Netbanking")
//				{
//					applyPromotion();
//				}
//			}	
//		},
//		error : function(resp) {
//			$("#no-click").remove();
//			alert("Some issues are there with Payment at this time. Please try payment later or contact out helpdesk");
//		}
//	});	 
// }
 
 

  function resetConvChargeElsewhere()
  {
 	 $.ajax({
 		url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/resetConvChargeElsewhere",
 		type: "GET",
 		cache: false,
 		success : function(response) {
 			var values=response.split("|");
			var totalPrice=values[0];
			var convCharge=values[1];
			$("#convChargeFieldId, #convChargeField").css("display","none");
			document.getElementById("convChargeField").innerHTML=convCharge;
			document.getElementById("totalWithConvField").innerHTML=totalPrice;
 			isCodSet = false;
 			if(paymentMode!=null){
 				applyPromotion(null);
			}
 		},
 		error : function(resp) {
 		}
 	});	 
  }
 
 
 
// function setCellNo()
// {
//	 $.ajax({
//		url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/setCellNo",
//		type: "GET",
//		cache: false,
//		success : function(response) {
//			if(response==""){
//				$("#otpMobileNUMField").val("");
//			}
//			else{
//				$("#otpMobileNUMField").val(response);
//			}
//		},
//		error : function(resp) {
//		}
//	});	 
// }
 

 
 $("input[name=creditCards]:radio").each(function(){
     var frm = $(this.form)
     
 }).on('change', function(e){
     var $this = $(this)
     var cg = $this.parents("div.credit-card-group:first")
	     //TISBOX-1732
	     $(".card_cvvErrorSavedCard, card_cvvErrorSavedCard_hide, .card_ebsErrorSavedCard, .card_ebsErrorSavedCard_hide").css("display","none");
	     $("input[name=creditCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
	     $("input[name=debitCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
	     $("input[name=emiCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
	     $(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
		 $(e.currentTarget).removeClass("card_token_hide").addClass("card_token");
		 $(".card_bank").removeClass("card_bank").addClass("card_bank_hide");
		 $(".card_brand").removeClass("card_brand").addClass("card_brand_hide");
		 $('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
		 $('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
		 $('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
	     $(e.currentTarget).parent().find('.card_bank_hide').removeClass("card_bank_hide").addClass("card_bank"); 
	     $(e.currentTarget).parent().parent().parent().find(".cvv").find(".security_code_hide").removeClass("security_code_hide").addClass("security_code");
	     $(e.currentTarget).parent().find('.card_brand_hide').removeClass("card_brand_hide").addClass("card_brand");
	     $(e.currentTarget).parent().find('.card_is_domestic_hide').removeClass("card_is_domestic_hide").addClass("card_is_domestic");
	     $(e.currentTarget).parent().find('.card_ebsErrorSavedCard_hide').removeClass("card_ebsErrorSavedCard_hide").addClass("card_ebsErrorSavedCard");
	     $(e.currentTarget).parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard_hide').removeClass("card_cvvErrorSavedCard_hide").addClass("card_cvvErrorSavedCard");
	     $(".security_code_hide").val(null);
	   //TISEE-5555
	 	$('.security_code_hide').prop('disabled', true);
	 	$('.security_code').prop('disabled', false); 
	     if($(".card_bank").val()=="AMEX")
			{
				$(".security_code").attr('maxlength','4');
			}
			else
			{
				$(".security_code").attr('maxlength','3');
			}
	     var bankName=$('.card_bank').val();
	     setBankForSavedCard(bankName);
 });
 
 
 
  $("input[name=debitCards]:radio").each(function(){
	     var frm = $(this.form)
	     
	 }).on('change', function(e){
	     var $this = $(this)
	     //TISBOX-1732
	     $(".card_cvvErrorSavedCard, card_cvvErrorSavedCard_hide, .card_ebsErrorSavedCard, .card_ebsErrorSavedCard_hide").css("display","none");
	     var cg = $this.parents("div.debit-card-group:first")
	     $("input[name=creditCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
	     $("input[name=debitCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
	     $("input[name=emiCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
	     $(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
	     $(e.currentTarget).removeClass("card_token_hide").addClass("card_token");
	     $(".card_bank").removeClass("card_bank").addClass("card_bank_hide");
	     $(".card_brand").removeClass("card_brand").addClass("card_brand_hide");
	     $('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
	     $('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
	     $('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
	     $(e.currentTarget).parent().find('.card_bank_hide').removeClass("card_bank_hide").addClass("card_bank"); 
	     $(e.currentTarget).parent().parent().parent().find(".cvv").find(".security_code_hide").removeClass("security_code_hide").addClass("security_code");
	     $(e.currentTarget).parent().find('.card_brand_hide').removeClass("card_brand_hide").addClass("card_brand");
	     $(e.currentTarget).parent().find('.card_is_domestic_hide').removeClass("card_is_domestic_hide").addClass("card_is_domestic");
	     $(e.currentTarget).parent().find('.card_ebsErrorSavedCard_hide').removeClass("card_ebsErrorSavedCard_hide").addClass("card_ebsErrorSavedCard");
	     $(e.currentTarget).parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard_hide').removeClass("card_cvvErrorSavedCard_hide").addClass("card_cvvErrorSavedCard");
	     $(".security_code_hide").val(null);
	   //TISEE-5555
	 	$('.security_code_hide').prop('disabled', true);
	 	$('.security_code').prop('disabled', false); 
	     if($(".card_bank").val()=="AMEX")
			{
				$(".security_code").attr('maxlength','4');
			}
			else
			{
				$(".security_code").attr('maxlength','3');
			}
	     var bankName=$('.card_bank').val();
	     setBankForSavedCard(bankName);
	     if($(".card_brand").val()=="MAESTRO"){
				$("#maestroMessage").css("display","block");
	     }
	     else{
				$("#maestroMessage").css("display","none");
	     }
	 });
  
  
  //$(document).on("focus", "#savedEMICard .credit-card-group .cvvValdiation", function() {
/*  $(document).on("click", *//*$("input[name=emiCards]:radio").each(function(){
	     var frm = $(this.form)*/
	     
	// }).on('click', function(e){
  	//TISEE-395
	$(document).on("change","input[name=emiCards]:radio",function(e){
	     var $this = $(this)
	     var cg = $this.parents("div.credit-card-group:first")
	     //TISBOX-1732
	     $(".card_cvvErrorSavedCard, card_cvvErrorSavedCard_hide, .card_ebsErrorSavedCard, .card_ebsErrorSavedCard_hide").css("display","none");
	     $("input[name=creditCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
	     $("input[name=debitCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
	     $("input[name=emiCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
	     $(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
		 $(e.currentTarget).removeClass("card_token_hide").addClass("card_token");
		 $(".card_bank").removeClass("card_bank").addClass("card_bank_hide");
		 $(".card_brand").removeClass("card_brand").addClass("card_brand_hide");
		 $('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
		 $('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
		 $('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
	     $(e.currentTarget).parent().find('.card_bank_hide').removeClass("card_bank_hide").addClass("card_bank"); 
	     $(e.currentTarget).parent().parent().parent().find(".cvv").find(".security_code_hide").removeClass("security_code_hide").addClass("security_code");
	     $(e.currentTarget).parent().find('.card_brand_hide').removeClass("card_brand_hide").addClass("card_brand");
	     $(e.currentTarget).parent().find('.card_is_domestic_hide').removeClass("card_is_domestic_hide").addClass("card_is_domestic");
	     $(e.currentTarget).parent().find('.card_ebsErrorSavedCard_hide').removeClass("card_ebsErrorSavedCard_hide").addClass("card_ebsErrorSavedCard");
	     $(e.currentTarget).parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard_hide').removeClass("card_cvvErrorSavedCard_hide").addClass("card_cvvErrorSavedCard");
	     $(".security_code_hide").val(null);
	   //TISEE-5555
	 	$('.security_code_hide').prop('disabled', true);
	 	$('.security_code').prop('disabled', false); 
	     if($(".card_bank").val()=="AMEX")
			{
				$(".security_code").attr('maxlength','4');
			}
			else
			{
				$(".security_code").attr('maxlength','3');
			}
	 });
  
  
 
 
  function createJuspayOrderForSavedCard(){
		$(".pay button").prop("disabled",true);
		$(".pay button").css("opacity","0.5");
		$(".pay").append('<img src="/store/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: absolute; right: 25%;bottom: 30px; height: 30px;">');
		$(".pay .spinner").css("left",(($(".pay.saved-card-button").width()+$(".pay.saved-card-button button").width())/2)+10);
		$("body").append("<div id='no-click' style='opacity:0.65; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	  // TISPRO-153		
		sendTealiumData();
		
		var firstName=lastName=addressLine1=addressLine2=addressLine3=country=state=city=pincode=null;
		var cardSaved=sameAsShipping=false;
		
		if($(".redirect").val()=="false"){
			Juspay.startSecondFactor();
		}
		$.ajax({
			url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/createJuspayOrder",
			data: { 'firstName' : firstName , 'lastName' : lastName , 'addressLine1' : addressLine1, 'addressLine2' : addressLine2 , 'addressLine3' : addressLine3, 'country' : country , 'state' : state, 'city' : city , 'pincode' : pincode, 'cardSaved' : cardSaved, 'sameAsShipping' : sameAsShipping},
			type: "GET",
			cache: false,
			async: false,
			success : function(response) {
				
				if(response=='redirect'){
//					if($(".redirect").val()=="false"){
//						Juspay.stopSecondFactor();
//					}
					$(location).attr('href',ACC.config.encodedContextPath+"/cart"); //TIS 404
				}else if(response=="" || response==null || response=="JUSPAY_CONN_ERROR"){
//					if($(".redirect").val()=="false"){
//						Juspay.stopSecondFactor();
//					}
					document.getElementById("juspayErrorMsg").innerHTML="Sorry! The system is down, please try again";
					$("#juspayconnErrorDiv").css("display","block");
					$(".pay button").prop("disabled",false);
					$(".pay button").css("opacity","1");
					$(".pay .spinner").remove();
					$("#no-click").remove();
					//$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/add");
				}else{
//					if($(".redirect").val()=="false"){
//						//Juspay.startSecondFactor();
//					}
					$("#order_id_saved").val(response);
					var baseUrl=window.location.origin;
					var website = ACC.config.encodedContextPath;
					var thank_you_page = /*(website.indexOf("https") > -1 ? "" : "https://") +*/ baseUrl+website + "/checkout/multi/payment-method/cardPayment";
					var error_page = /*(website.indexOf("https") > -1 ? "" : "https://") +*/ baseUrl+website + "/checkout/multi/payment-method/cardPayment";
					Juspay.Setup({
						payment_form: "#card_form",
						success_handler: function(status, statusObj) {
							//redirect to success page
							var p = "order_id=" + statusObj.orderId
							p = p + "&status=" + statusObj.status 
							p = p + "&status_id=" + statusObj.statusId
							window.location.href = thank_you_page
						},
						error_handler: function(error_code, error_message, bank_error_code, bank_error_message, gateway_id) {
							//redirect to failure page
							//alert("Transaction not successful. Error: " + bank_error_message)
							window.location.href = error_page
						},
						second_factor_window_closed_handler: function() {
						    // enable the pay button for the user
							window.location.href = error_page
						}
					})
					$("#card_form").submit() 
				}
			},
			error : function(resp) {
				if($(".redirect").val()=="false"){
					Juspay.stopSecondFactor();
				}
				$(".pay button").prop("disabled",false);
				$(".pay button").css("opacity","1");
				$(".pay .spinner").remove();
				$("#no-click").remove();
			}
		});		
	}
  
  
  function createJuspayOrderForNewCard(){
		$(".pay button").prop("disabled",true);
		$(".pay button").css("opacity","0.5");
		$(".pay").append('<img src="/store/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: absolute; right: 25%;bottom: 30px; height: 30px;">');
		$(".pay .spinner").css("left",(($(".pay.newCardPayment").width()+$(".pay.newCardPayment button").width())/2)+10);
		$("body").append("<div id='no-click' style='opacity:0.65; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
		
	  // TISPRO-153
		sendTealiumData();
		
		var firstName=$("#firstName").val();
		var lastName=$("#lastName").val();
		var addressLine1=$("#address1").val();
		var addressLine2=$("#address2").val();
		var addressLine3=$("#address3").val();
		var country=$("#country").val();
		var state=$("#state").val();
		var city=$("#city").val();
		var pincode=$("#pincode").val();
		if ($('#save-card').is(":checked"))
		{
			var cardSaved = true;
		}
		else {
			var cardSaved = false;
		}
		if ($('#sameAsShipping').is(":checked"))
		{
			var sameAsShipping = true;
		}
		else {
			var sameAsShipping = false;
		}
		
		if($(".redirect").val()=="false"){
			Juspay.startSecondFactor();
		}
		$.ajax({
			url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/createJuspayOrder",
			data: { 'firstName' : firstName , 'lastName' : lastName , 'addressLine1' : addressLine1, 'addressLine2' : addressLine2 , 'addressLine3' : addressLine3, 'country' : country , 'state' : state, 'city' : city , 'pincode' : pincode, 'cardSaved' : cardSaved, 'sameAsShipping' : sameAsShipping},
			type: "GET",
			cache: false,
			async: false,
			success : function(response) {	
				if(response=='redirect'){
//					if($(".redirect").val()=="false"){
//						Juspay.stopSecondFactor();
//					}
					$(location).attr('href',ACC.config.encodedContextPath+"/cart"); //TIS 404
				}else if(response=="" || response==null || response=="JUSPAY_CONN_ERROR"){	
//					if($(".redirect").val()=="false"){
//						Juspay.stopSecondFactor();
//					}
					document.getElementById("juspayErrorMsg").innerHTML="Sorry! The system is down, please try again";
					$("#juspayconnErrorDiv").css("display","block");
					$(".pay button").prop("disabled",false);
					$(".pay button").css("opacity","1");
					$(".pay .spinner").remove();
					$("#no-click").remove();
					//$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/add");
				}else{
//					if($(".redirect").val()=="false"){
//						Juspay.startSecondFactor();
//					}
					$("#order_id_new").val(response);
					submitCardForm();				
				}
			},
			error : function(resp) {
				if($(".redirect").val()=="false"){
					Juspay.stopSecondFactor();
				}
				$(".pay button").prop("disabled",false);
				$(".pay button").css("opacity","1");
				$(".pay .spinner").remove();
				$("#no-click").remove();
			}
		});		
	}
  
 
  

 $("#make_saved_cc_payment").click(function(){
	var password = $(".card_token").parent().parent().parent().find(".cvv").find(".cvvValdiation").val();
	var ebsDownCheck=$("#ebsDownCheck").val();
	var isDomestic=$(".card_token").parent().parent().parent().find('.card').find('.radio').find('.card_is_domestic').val();
	if (password.length < 3 && 	$(".card_brand").val()!="MAESTRO"){
		$(".card_cvvErrorSavedCard").css("display","block");		
		return false;
	}
 	else if($(".card_brand").val()=="MAESTRO" && password==""){
 		createJuspayOrderForSavedCard(); 
 	}
	else if(ebsDownCheck=="Y" && (isDomestic=="false" || isDomestic==""))
	{
		$(".card_ebsErrorSavedCard").css("display","block");		
		return false;
	}
	else{
		createJuspayOrderForSavedCard(); 
	}
 })
 
 
 $(".security_code").focus(function(){
	 $("#savedCVVError").css("display","none");
	 $(".card_ebsErrorSavedCard, .card_cvvErrorSavedCard").css("display","none");	
	 document.getElementById("cvvError").innerHTML="";
	 
 })
 
 

 $("#make_cc_payment").click(function(){
	 var name = validateName();
	 if(binStatus==true){
		 var cardNo=true;
	 }
	 else{
		 if(document.getElementById("cardNoError").innerHTML.length==0){
			 document.getElementById("cardNoError").innerHTML="Please enter a valid card number ";
		 }
		 var cardNo = false;
	 }
	 var firstName=validateNameOnAddress($("#firstName").val(), document.getElementById("firstNameError"), "firstName");
	 var lastName=validateNameOnAddress($("#lastName").val(), document.getElementById("lastNameError"), "lastName");
	 var addressLine1=validateAddressLine1($("#address1").val(), document.getElementById("address1Error"));
	 var addressLine2=validateAddressLine2($("#address2").val(), document.getElementById("address2Error"));
	 var addressLine3=validateLandmark($("#address3").val(), document.getElementById("address3Error"));
	 var pin = validatePin();
	 var city=validateCity();
	 var state=validateState();
	 var cardType=$("#cardType").val();
	 if($("#paymentMode").val()=="Credit Card" || $("#paymentMode").val()=="EMI"){
		 if(cardType=="MAESTRO"){
			 if (name && cardNo && pin && firstName && lastName && addressLine1 && addressLine2 && addressLine3 && city && state){
				 createJuspayOrderForNewCard();
			 }
			 else{
				 return false;
			 }
		 }
		 else{
			 var cvv = validateCVV();
			 var expMM = validateExpMM();
			 var expYY = validateExpYY();
			 if (cvv && expYY && name && expMM && cardNo && pin && firstName && lastName && addressLine1 && addressLine2 && addressLine3 && city && state){
				 createJuspayOrderForNewCard();
			 }
			 else{
				 return false;
			 }
		 }
			 
	 }
	 else if($("#paymentMode").val()=="Debit Card"){
		 if(cardType=='MAESTRO'){
			 if (name && cardNo){
				 createJuspayOrderForNewCard();		 
			 }
			 else{
				 return false;
			 }
		 }
		 else{
			 var cvv = validateCVV();
			 var expMM = validateExpMM();
			 var expYY = validateExpYY();
			 if (cvv && expYY && name && expMM && cardNo){
				 createJuspayOrderForNewCard();		 
			 }
			 else{
				 return false;
			 }
		 }
	 }
 })
 
 

 function submitCardForm(){
	 var baseUrl=window.location.origin;
		var website = ACC.config.encodedContextPath;
		 var thank_you_page = /*(website.indexOf("https") > -1 ? "" : "https://") +*/baseUrl+ website + "/checkout/multi/payment-method/cardPayment";
		 var error_page = /*(website.indexOf("https") > -1 ? "" : "https://") +*/ baseUrl+website + "/checkout/multi/payment-method/cardPayment";
		 Juspay.Setup({
			 payment_form: "#payment_form",
			 success_handler: function(status, statusObj) {
	         // redirect to success page
				 var p = "order_id=" + statusObj.orderId
				 p = p + "&status=" + statusObj.status 
				 p = p + "&status_id=" + statusObj.statusId
				 window.location.href = thank_you_page
			 },
			 error_handler: function(error_code, error_message, bank_error_code, bank_error_message, gateway_id) {
	         //redirect to failure page
				 /*alert("Transaction not successful. Error: " + bank_error_message)*/

				 window.location.href = error_page
			 },
			 second_factor_window_closed_handler: function() {
				    // enable the pay button for the user
					window.location.href = error_page
			 }
		 });
 $("#payment_form").submit() ;   
 return false; 	 
 }
 
 

 function newCardForm(){
	$(".newCard, #savedCard, .saved-card-button").css("display","none");
	$(".savedCard").css("display","table-cell");
	$(".newCardPayment, #newCard").css("display","block");
	document.getElementById("card_form").reset();
	document.getElementById("payment_form").reset();
	$('.form-group span').text('');
	$("input[name=debitCards]:radio.card_token,input[name=creditCards]:radio.card_token,input[name=emiCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
	$(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
	$(".card_token_hide").parent().find('.card_bank').removeClass("card_bank").addClass("card_bank_hide");
	$(".card_token_hide").parent().find('.card_brand').removeClass("card_brand").addClass("card_brand_hide");
	$(".card_token_hide").parent().find('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
	$(".card_token_hide").parent().find('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
	$(".card_token_hide").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
	if($("#paymentMode").val()=="Credit Card"){
		populateBillingAddress();
		$(".accepted-cards .maestro").parent().css("display","none");
		$(".accepted-cards .visa").parent().css("display","inline-block");
		$(".accepted-cards .master").parent().css("display","inline-block");
		$(".accepted-cards .amex").parent().css("display","inline-block");
		$("input[name=creditCards]:radio").first().removeClass("card_token_hide").addClass("card_token");
		$(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
		$(".card_token_hide").parent().find('.card_bank').removeClass("card_bank").addClass("card_bank_hide"); 
		$(".card_token_hide").parent().find('.card_brand').removeClass("card_brand").addClass("card_brand_hide");
		$(".card_token_hide").parent().find('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
		$(".card_token_hide").parent().find('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
		$(".card_token_hide").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
		applyPromotion(null);
	}
	else if($("#paymentMode").val()=="EMI"){
		var selectedBank=$("#bankNameForEMI").val();
		$(".new-card").find("#is_emi").val("true");
		$(".new-card").find("#emi_bank").val(selectedBank);
		$("#billingAddress, .newCardPayment, #make_cc_payment").css("display","block");
		populateBillingAddress();
		$(".accepted-cards .maestro").parent().css("display","none");
		$(".accepted-cards .visa").parent().css("display","inline-block");
		$(".accepted-cards .master").parent().css("display","inline-block");
		$(".accepted-cards .amex").parent().css("display","none");
		$("input[name=emiCards]:radio").first().removeClass("card_token_hide").addClass("card_token");
		$(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
		$(".card_token_hide").parent().find('.card_bank').removeClass("card_bank").addClass("card_bank_hide"); 
		$(".card_token_hide").parent().find('.card_brand').removeClass("card_brand").addClass("card_brand_hide");
		$(".card_token_hide").parent().find('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
		$(".card_token_hide").parent().find('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
		$(".card_token_hide").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
	}
	else if($("#paymentMode").val()=="Debit Card"){
		$(".accepted-cards .maestro").parent().css("display","inline-block");
		$(".accepted-cards .visa").parent().css("display","inline-block");
		$(".accepted-cards .master").parent().css("display","inline-block");
		$(".accepted-cards .amex").parent().css("display","none");
		$("input[name=debitCards]:radio").first().removeClass("card_token_hide").addClass("card_token");
		$(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
		$(".card_token_hide").parent().find('.card_bank').removeClass("card_bank").addClass("card_bank_hide"); 
		$(".card_token_hide").parent().find('.card_brand').removeClass("card_brand").addClass("card_brand_hide");
		$(".card_token_hide").parent().find('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
		$(".card_token_hide").parent().find('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
		$(".card_token_hide").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
		applyPromotion(null);
	}
	//TISEE-5555
	$('.security_code_hide').prop('disabled', true);
	$('.security_code').prop('disabled', false); 
}
 
 

 function populateBillingAddress(){	
	 $("#firstNameError, #lastNameError, #address1Error, #address2Error, #address3Error, #cityError, #stateError, #pinError").text("");
	 $.ajax({
			url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/setShippingAddress",
			type: "GET",
			cache: false,
			success : function(response) {
				var values=response.split("|");
				$("#firstName").val(values[0]);
				$("#lastName").val(values[1]);
				$("#address1").val(values[2]);
				$("#address2").val(values[3]);
				$("#address3").val(values[4]);
				$("#country").val(values[5]);
				$("#state").val(values[6]);
				$("#city").val(values[7]);
				$("#pincode").val(values[8]);
				$("#firstName, #lastName, #address1, #address2, #address3, #state, #city, #pincode").attr("readonly", true);
				$("#country").attr("disabled", true);
			},
			error : function(resp) {
			}
		});
 }
 
	 
 	 
function savedCardForm(){
	$(".savedCard, .newCardPayment, #newCard").css("display","none");
	$(".newCard").css("display","table-cell");
	$("#savedCard, .saved-card-button").css("display","block");
	document.getElementById("payment_form").reset();
	document.getElementById("card_form").reset();
	$('.form-group span').text('');
	$("input[name=debitCards]:radio.card_token,input[name=creditCards]:radio.card_token,input[name=emiCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
	$(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
	$(".card_token_hide").parent().find('.card_bank').removeClass("card_bank").addClass("card_bank_hide");
	$(".card_token_hide").parent().find('.card_brand').removeClass("card_brand").addClass("card_brand_hide");
	$(".card_token_hide").parent().find('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
	$(".card_token_hide").parent().find('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
	$(".card_token_hide").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
	if($("#paymentMode").val()=="Credit Card")
	{
		$("input[name=debitCards]:radio").first().prop("checked", false);
		$("input[name=emiCards]:radio").first().prop("checked", false);
		$("input[name=creditCards]:radio").first().prop("checked", true);
		$("input[name=creditCards]:radio").first().removeClass("card_token_hide").addClass("card_token");
		$(".card_token").parent().parent().parent().find(".cvv").find(".security_code_hide").removeClass("security_code_hide").addClass("security_code");
		$(".card_token").parent().find('.card_bank_hide').removeClass("card_bank_hide").addClass("card_bank");
		$(".card_token").parent().find('.card_brand_hide').removeClass("card_brand_hide").addClass("card_brand");
		$(".card_token").parent().find('.card_is_domestic_hide').removeClass("card_is_domestic_hide").addClass("card_is_domestic");
		$(".card_token").parent().find('.card_ebsErrorSavedCard_hide').removeClass("card_ebsErrorSavedCard_hide").addClass("card_ebsErrorSavedCard");
		$(".card_token").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard_hide').removeClass("card_cvvErrorSavedCard_hide").addClass("card_cvvErrorSavedCard");
		var bankName=$('.card_bank').val();
		setBankForSavedCard(bankName);
	}
	else if($("#paymentMode").val()=="Debit Card")
	{
		$("input[name=debitCards]:radio").first().prop("checked", true);
		$("input[name=creditCards]:radio").first().prop("checked", false);
		$("input[name=emiCards]:radio").first().prop("checked", false);
		$("input[name=debitCards]:radio").first().removeClass("card_token_hide").addClass("card_token");
		$(".card_token").parent().parent().parent().find(".cvv").find(".security_code_hide").removeClass("security_code_hide").addClass("security_code");
		$(".card_token").parent().find('.card_bank_hide').removeClass("card_bank_hide").addClass("card_bank");
		$(".card_token").parent().find('.card_brand_hide').removeClass("card_brand_hide").addClass("card_brand");
		$(".card_token").parent().find('.card_is_domestic_hide').removeClass("card_is_domestic_hide").addClass("card_is_domestic");
		$(".card_token").parent().find('.card_ebsErrorSavedCard_hide').removeClass("card_ebsErrorSavedCard_hide").addClass("card_ebsErrorSavedCard");
		$(".card_token").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard_hide').removeClass("card_cvvErrorSavedCard_hide").addClass("card_cvvErrorSavedCard");
		if($(".card_brand").val()=="MAESTRO"){
			$("#maestroMessage").css("display","block");
		}
		else{
			$("#maestroMessage").css("display","none");
		}
		var bankName=$('.card_bank').val();
		setBankForSavedCard(bankName);
	}
	else if($("#paymentMode").val()=="EMI")
	{
		$("input[name=emiCards]:radio").first().prop("checked", true);
		$("input[name=debitCards]:radio").first().prop("checked", false);
		$("input[name=creditCards]:radio").first().prop("checked", false);
		$("input[name=emiCards]:radio").first().removeClass("card_token_hide").addClass("card_token");
		$(".card_token").parent().parent().parent().find(".cvv").find(".security_code_hide").removeClass("security_code_hide").addClass("security_code");
		$(".card_token").parent().find('.card_bank_hide').removeClass("card_bank_hide").addClass("card_bank");
		$(".card_token").parent().find('.card_brand_hide').removeClass("card_brand_hide").addClass("card_brand");
		$(".card_token").parent().find('.card_is_domestic_hide').removeClass("card_is_domestic_hide").addClass("card_is_domestic");
		$(".card_token").parent().find('.card_ebsErrorSavedCard_hide').removeClass("card_ebsErrorSavedCard_hide").addClass("card_ebsErrorSavedCard");
		$(".card_token").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard_hide').removeClass("card_cvvErrorSavedCard_hide").addClass("card_cvvErrorSavedCard");
	}
	//TISEE-5555
	$('.security_code_hide').prop('disabled', true);
	$('.security_code').prop('disabled', false); 
}


//Card Validations

function validateExpMM() {
	var cardType=$("#cardType").val();
	var year = document.getElementsByName("expyy");
	var month = document.getElementsByName("expmm");
	var errorHandle = document.getElementById("expYYError");
	if(cardType!="MAESTRO")
	{
		var yy = year[0].value;
		var mm = month[0].value;
		var date = new Date();
		var currentYear = date.getFullYear();
		var currentMonth = date.getMonth();
		currentMonth = currentMonth + 1;
		if (mm == 'month') {
			errorHandle.innerHTML = "   Please select a date. ";
			return false;
		} else if (yy==currentYear){
			if (mm < currentMonth) {
				errorHandle.innerHTML = "   Please select a valid date. ";
				return false;
			}
		}
		errorHandle.innerHTML = "";
		return true;
	}
	else{
		errorHandle.innerHTML = "";
		return true;
	}
}


function validateExpYY() {
	var cardType=$("#cardType").val();
	var year = document.getElementsByName("expyy");
	var month = document.getElementsByName("expmm");
	var errorHandle = document.getElementById("expYYError");
	if(cardType!="MAESTRO")
	{
		var yy = year[0].value;
		var mm = month[0].value;
		var date = new Date();
		var currentYear = date.getFullYear();
		var currentMonth = date.getMonth();
		currentMonth = currentMonth + 1;
		if (yy == 'year' || mm == 'month') {
			errorHandle.innerHTML = "   Please select a date. ";
			return false;
		} else if (yy < currentYear) {
			errorHandle.innerHTML = "   Please select a valid date. ";
			return false;
		}
		else if (yy==currentYear){
			if (mm < currentMonth) {
				errorHandle.innerHTML = "   Please select a valid date. ";
				return false;
			}
		}
		errorHandle.innerHTML = "";
		return true;
	}
	else{
		errorHandle.innerHTML = "";
		return true;
	}
}


function validateName() {
	//var handle = document.getElementsByName("memberName");
	var errorHandle = document.getElementById("memberNameError");
	var name =$(".name_on_card").val();
	if (name == "") {
		errorHandle.innerHTML = "  Please enter name. ";
		return false;
	}else {
		var count = 0;
		for ( var index = 0; index < name.length; index++)
			if (name.charAt(index) == ' ')
				count++;
		if (count == name.length) {
			errorHandle.innerHTML = "  Please enter a valid name. ";
			return false;
		}else if (name.length < 3) {
			errorHandle.innerHTML = "   Name must be longer. ";
			return false; 
		} else {
			for ( var index = 0; index < name.length; index++){
				if (name.toUpperCase().charAt(index) < 'A'
						|| name.toUpperCase().charAt(index) > 'Z'){
					if (name.charAt(index) != ' ') {
						errorHandle.innerHTML = "   Only alphabets and spaces allowed. ";
						return false;
					}
				}		
			}				
		} 
	} 
	errorHandle.innerHTML = "";
	return true;
}


$(window).on('load resize',function(){	
	if($(window).width()<651){
$(".cvvHelp").popover({
    html: 'true',
    placement: 'bottom',
    trigger: 'hover',
    title: 'Card Security Code',
    content: $("#cvvHelpContent").val()
});
	}
	else{
		$(".cvvHelp").popover({
		    html: 'true',
		    placement: 'right',
		    trigger: 'hover',
		    title: 'Card Security Code',
		    content: $("#cvvHelpContent").val()
		});
	}
});



function validateCVV() {
	var cardType=$("#cardType").val();
	var handle = document.getElementsByName("cvv");
	var errorHandle = document.getElementById("cvvError");
	if(cardType!="MAESTRO")
	{
		var number = handle[0].value;
		if (number == "") {
			errorHandle.innerHTML = "Please enter CVV.";
			return false;
		} else {
			var count = 0;
			for ( var index = 0; index < number.length; index++)
				if (number.charAt(index) == ' ')
					count++;
			if (count == number.length) {
				errorHandle.innerHTML = "Spaces are not allowed.";
				return false;
			} else {
				for ( var index = 0; index < number.length; index++)
					if (number.charAt(index) < '0'
							|| number.charAt(index) > '9' || number.length < 3) {
						errorHandle.innerHTML = "Please enter CVV.";
						return false;
					}
			}
		}
		errorHandle.innerHTML = "";
		return true;
	}
	else
	{
		errorHandle.innerHTML = "";
		return true;
	}
}



function validateCardNo() {
	var value=$("#cardNo").val();
	var errorHandle = document.getElementById("cardNoError"); 
	var cardType= $("#cardType").val();
	// The Luhn Algorithm.
	var nCheck = 0, nDigit = 0, bEven = false;
	value = value.replace(/\D/g, "");
	for (var n = value.length - 1; n >= 0; n--) {
		var cDigit = value.charAt(n),
		nDigit = parseInt(cDigit, 10);	 
		if (bEven) {
			if ((nDigit *= 2) > 9) nDigit -= 9;
		}	 
		nCheck += nDigit;
		bEven = !bEven;
	}
	var result= ((nCheck % 10)==0);
	// accept only digits, dashes or spaces	
	if (/[^0-9-\s]+/.test(value)) {
		binStatus=false;
		errorHandle.innerHTML = "Please enter a valid card number";
		return false;
	}
	else if(value.length<13 || value.length>19){
		binStatus=false;
		errorHandle.innerHTML = "Please enter a valid card number";
		return false;
	}
	else if(cardType=='MAESTRO' && !(value.length==13 || value.length==14 || value.length==15 || value.length==16 || value.length==17 || value.length==18 || value.length==19)){
		binStatus=false;
		errorHandle.innerHTML = "Please enter a valid card number";
		return false;
	}
	else if(cardType=='VISA' && !(value.length==16 || value.length==13)){
		binStatus=false;
		errorHandle.innerHTML = "Please enter a valid card number";
		return false;
	}
	else if(cardType=='MASTERCARD' && value.length!=16){
		binStatus=false;
		errorHandle.innerHTML = "Please enter a valid card number";
		return false;
	}
	else if(cardType=='AMEX' && value.length!=15){
		binStatus=false;
		errorHandle.innerHTML = "Please enter a valid card number";
		return false;
	}
	else if(cardType=='AMEX' && value.length==15 && ($("#paymentMode").val()=='EMI' || $("#paymentMode").val()=='Debit Card')){
		binStatus=false;
		errorHandle.innerHTML = "Please enter a valid debit card";
		return false;
	}
	else if(cardType=='MAESTRO' && (value.length==16 || value.length==18 || value.length==19) && ($("#paymentMode").val()=='EMI' || $("#paymentMode").val()=='Credit Card')){
		binStatus=false;
		errorHandle.innerHTML = "Please enter a valid credit card";
		return false;
	}
	else if(cardType==""){
		binStatus=false;
		errorHandle.innerHTML = "Sorry, the entered card type is not supported";
		return false;
	}
	//Commented for unsupported card types in Release 1
	
	/*else if(cardType=='DINERS' && !(value.length==16 || value.length==15 || value.length==14)){
		binStatus=false;
		errorHandle.innerHTML = "   Please enter a valid card no. ";
		return false;
	}
	else if(cardType=='DISCOVER' && value.length!=16){
		binStatus=false;
		errorHandle.innerHTML = "   Please enter a valid card no. ";
		return false;
	}
	else if(cardType=='JCB' && !(value.length==15 || value.length==16)){
		binStatus=false;
		errorHandle.innerHTML = "   Please enter a valid card no. ";
		return false;
	}*/
	else if(result==false){
		binStatus=false;
		errorHandle.innerHTML = "Please enter a valid card number";
		return false;
	}
	//BIN Validation
	var bin=value.slice(0,6);
	
	//calling BIN Check AJAX
	$.ajax({
		url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/binCheck/"+bin,
		type: "POST",
		cache: false,
		success : function(response) {	
			if(!response.isValid)
			{
				errorHandle.innerHTML = "Cannot proceed. Please try with diff card";
				return false;
			}
			else
			{
				if(response.cardType==null)
				{
					binStatus=true;
					if($("#paymentMode").val()!='EMI'){
						applyPromotion(null);
					}					
					errorHandle.innerHTML = "";
					return true;
				}
				else
				{
					var selectedBank=$("select[id='bankNameForEMI']").find('option:selected').text();
					if($("#paymentMode").val()=='EMI')
					{
						if(response.cardType=="" || response.cardType==null || response.cardType=="CREDIT" || response.cardType=="CC" || response.cardType=="Credit")
						{
							if(selectedBank!="select" && selectedBank==response.bankName){
								binStatus=true;
								//applyPromotion();
								errorHandle.innerHTML = "";
								return true;			
							}
							else if(selectedBank!="select" && selectedBank!=response){
								binStatus=false;
								errorHandle.innerHTML = "Please enter a card same as the selected bank";
								return false;	
							}
						}
						else
						{
							binStatus=false;
							errorHandle.innerHTML = "Please enter a valid Credit Card number";
						}
					}
					else if(document.getElementById("paymentMode").value=='Credit Card'){
						if(response.cardType=="" || response.cardType==null || response.cardType=="CREDIT" || response.cardType=="CC" || response.cardType=="Credit")
						{
							binStatus=true;
							applyPromotion(null);
							errorHandle.innerHTML = "";
							return true;
						}
						else
						{
							binStatus=false;
							errorHandle.innerHTML = "Please enter a valid Credit Card number";
						}
					}
					else if(document.getElementById("paymentMode").value=='Debit Card'){
						if(response.cardType=="" || response.cardType==null || response.cardType=="DEBIT" || response.cardType=="DC" || response.cardType=="Debit")
						{
							binStatus=true;
							applyPromotion(null);
							errorHandle.innerHTML = "";
							return true;
						}
						else
						{
							binStatus=false;
							errorHandle.innerHTML = "Please enter a valid Debit Card number";
						}
					}
				}
			}
		},
		error : function(resp) {
			binStatus=false;
			errorHandle.innerHTML = "   Please enter a valid card number ";
			$("#promotionApplied").css("display","none");
			resetConvCharge();
			return false;
		}
	});		
	errorHandle.innerHTML = "";
	return true;
}



function creditCardTypeFromNumber(number){
	var re = {
			//maestro: /^(5018|5044|5020|5038|5081|603845|6304|6759|676[1-3]|6220|504834|56|58)/,
			maestro: /^(50|56|57|58|59|60|61|62|63|64|65|66|67)/,
	        visa: /^4/,
	        mastercard: /^5[1-5]/,
	        amex: /^3[47]/,
	        //diners: /^(36|38|30[0-5])\d+$/,
	        //discover: /^(6011|65|64[4-9]|622)\d+$/,
	        //jcb: /^35\d+$/
	    };
	    if (re.maestro.test(number)) {
	        document.getElementById("cardType").value='MAESTRO';
	        $("#cardNo").attr('maxlength','19');
	        $(".security_code").attr('maxlength','3');
	        $('ul.accepted-cards li').removeClass('active-card');
	        $('ul.accepted-cards li span.maestro').parent('li').addClass('active-card');
	        if($("#paymentMode").val()=="Debit Card")
	        {
	        	$("#newMaestroMessage").css("display","block");
	        }
	        else
	        {
	        	$("#newMaestroMessage").css("display","none");
	        }
	    } else if (re.visa.test(number)) {
	    	document.getElementById("cardType").value='VISA';
	    	 $("#cardNo").attr('maxlength','16');
	    	 $(".security_code").attr('maxlength','3');
	    	 $('ul.accepted-cards li').removeClass('active-card');
		        $('ul.accepted-cards li span.visa').parent('li').addClass('active-card');
		        $("#newMaestroMessage").css("display","none");
	    } else if (re.mastercard.test(number)) {
	    	document.getElementById("cardType").value='MASTERCARD';
	    	 $("#cardNo").attr('maxlength','16');
	    	 $(".security_code").attr('maxlength','3');
	    	 $('ul.accepted-cards li').removeClass('active-card');
		        $('ul.accepted-cards li span.master').parent('li').addClass('active-card');
		        $("#newMaestroMessage").css("display","none");
	    } else if (re.amex.test(number)) {
	    	document.getElementById("cardType").value='AMEX';
	    	 $("#cardNo").attr('maxlength','15');
	    	 $(".security_code").attr('maxlength','4');
	    	 $('ul.accepted-cards li').removeClass('active-card');
		        $('ul.accepted-cards li span.amex').parent('li').addClass('active-card');
		        $("#newMaestroMessage").css("display","none");
	    }
	    //Commented for unsupported card types in Release 1
	    
	    /*else if (re.diners.test(number)) {
	    	document.getElementById("cardType").value='DINERS';
	    	 $('ul.accepted-cards li').removeClass('active-card');
		        $('ul.accepted-cards li span.diners').parent('li').addClass('active-card');
	    } else if (re.discover.test(number)) {
	    	document.getElementById("cardType").value='DISCOVER';
	    	 $('ul.accepted-cards li').removeClass('active-card');
		        $('ul.accepted-cards li span.discover').parent('li').addClass('active-card');
	    } else if (re.jcb.test(number)) {
	    	document.getElementById("cardType").value='JCB';
	    	 $('ul.accepted-cards li').removeClass('active-card');
		        $('ul.accepted-cards li span.jcb').parent('li').addClass('active-card');
	    }*/ else {
	    	document.getElementById("cardType").value="";
	    	 $('ul.accepted-cards li').removeClass('active-card');
	    	 $("#newMaestroMessage").css("display","none");
	    }
	    
}



$('#cardNo').keyup(function(){
	   if($(this).val().length >= 1){
	       cardType = creditCardTypeFromNumber($(this).val());
	   }
	   else if($(this).val().length < 2){
		   document.getElementById("cardType").value="";
		   $('ul.accepted-cards li').removeClass('active-card');
	   }
});



function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}


function populateAddress(){
	var checkbox=document.getElementById("sameAsShipping")
	if(checkbox.checked==true){
		populateBillingAddress();
	}
	else{
		$("#firstName").val("");
		$("#lastName").val("");
		$("#address1").val("");
		$("#address2").val("");
		$("#address3").val("");
		$("#country").val("India");
		$("#state").val("");
		$("#city").val("");
		$("#pincode").val("");
		$("#firstName, #lastName, #address1, #address2, #address3, #state, #city, #pincode").attr("readonly", false);
		$("#country").attr("disabled", false);
	}
}


function validatePin() {
	var number = $("#pincode").val();
	var errorHandle = document.getElementById("pinError");
	var regex = new RegExp("^[a-zA-Z0-9]+$");
	if (number == "") {
		errorHandle.innerHTML = "Please enter a Pincode.";
		return false;
	}
	else if(!regex.test(number)) {
		errorHandle.innerHTML = "Please enter valid Pincode.";
		return false;
	}
	errorHandle.innerHTML = "";
	return true;
}  


$("#newAddressButton").click(function() {
	var validate=true;
	var regPostcode = /^([1-9])([0-9]){5}$/;
    var mob = /^[1-9]{1}[0-9]{9}$/;
    var letters = /^[a-zA-Z]+$/; 
    var cityPattern = /^[a-zA-Z]+([\s]?[a-zA-Z]+)*$/;
    var firstName = document.getElementById("address.firstName");
	var lastName = document.getElementById("address.surname");
	var address1 = document.getElementById("address.line1");
	var regAddress = /^[0-9a-zA-Z\-\/\,\s]+$/;
	var address2 = document.getElementById("address.line2");
	var address3 = document.getElementById("address.line3");
	var city= document.getElementById("address.townCity");
	var stateValue = document.getElementById("address.states");
	var zipcode = document.getElementsByName("postcode")[0].value;
	var txtMobile = document.getElementsByName("MobileNo")[0].value;
	var result=firstName.value;
	 
	if(result == undefined || result == "" )
	{	
		$("#firstnameError").show();
		$("#firstnameError").html("<p>First Name cannot be Blank</p>");
		validate= false;
	}
	else if(letters.test(result) == false)  
	{ 
		$("#firstnameError").show();
		$("#firstnameError").html("<p>First Name must be alphabet only</p>");
		validate= false;
	}  
	else
	{
		$("#firstnameError").hide();
	}
			
	 result=lastName.value;
	if(result == undefined || result == "")
	{	
		$("#lastnameError").show();
		$("#lastnameError").html("<p>Last Name cannot be Blank</p>");
		validate= false;
	}
	else if(letters.test(result) == false)  
	{ 
		$("#lastnameError").show();
		$("#lastnameError").html("<p>Last Name must be alphabet only</p>");
		validate= false;
	} 
	else
	{
		$("#lastnameError").hide();
	}
	
	result=address1.value;
	if(result == undefined || result == "")
	{	
		$("#address1Error").show();
		$("#address1Error").html("<p>Address Line 1 cannot be blank</p>");	
		validate= false;
	}
	else if(regAddress.test(result) == false)  
	{ 
		$("#address1Error").show();
		$("#address1Error").html("<p>Address Line 1 must be alphanumeric only</p>");
		validate= false;
	}  
		else
	{
		$("#address1Error").hide();
	}	
	
	    result=address2.value;
		if(result == undefined || result == "")
	{	
		$("#address2Error").show();
		$("#address2Error").html("<p>Address Line 2 cannot be blank</p>");
		validate= false;
	}
	else if(regAddress.test(result) == false)  
	{ 
		$("#address2Error").show();
		$("#address2Error").html("<p>Address Line 2 must be alphanumeric only</p>");
		validate= false;
	}
	else
	{
		$("#address2Error").hide();
	}
	
	result=address3.value;
	if(result == undefined || result == "")
	{	
		$("#address3Error").show();
		$("#address3Error").html("<p>Landmark cannot be blank</p>");
		validate= false;
	}
	else if(regAddress.test(result) == false)  
	{ 
		$("#address3Error").show();
		$("#address3Error").html("<p>LandMark must be alphanumeric only</p>");	
		validate= false;
	}  
	else
	{
		$("#address3Error").hide();	
	}
	
	
	  result=city.value;
	if(result == undefined || result == "")
	{	
		$("#cityError").show();
		$("#cityError").html("<p>City cannot be blank</p>");
		 validate=false;
	}
	else if(cityPattern.test(result) == false)  
	{ 
		$("#cityError").show();
		$("#cityError").html("<p>City must be alphabet only</p>");
		validate= false;
	}
	else
	{
		$("#cityError").hide();
	}

	result=stateValue.options[stateValue.selectedIndex].value;
	if(result == undefined || result == "")
	{	
		$("#stateError").show();
		$("#stateError").html("<p>Please choose a state</p>");
		 validate = false;
	}
	else
	{
		$("#stateError").hide();
	}
	
   if(zipcode == undefined || zipcode == "")
	{	
		$("#pincodeError").show();
		$("#pincodeError").html("<p>Please enter a pincode</p>");
		validate = false;
	}
    else if(regPostcode.test(zipcode) == false){
        $("#pincodeError").show();
        $("#pincodeError").html("<p>Please enter a valid pincode</p>");
		validate= false;  
	}
    else
	{
		$("#pincodeError").hide();
	}
 
   if(txtMobile  == undefined || txtMobile == "")
	{	
		$("#mobileError").show();
		$("#mobileError").html("<p>Please enter mobile no.</p>");
        validate = false;
	}
    else if (mob.test(txtMobile) == false) {
		$("#mobileError").show();
		$("#mobileError").html("<p> Please enter correct mobile no.</p>");
		 validate=false;   
    }
       else
	{
		$("#mobileError").hide();
	}
   
	if(validate==false)
	{
		return false;
	}
	else
	{
		$('#addressForm').submit();	
		
//		$.ajax({
//			url: ACC.config.encodedContextPath + "/cart/checkPincodeServiceability/"+zipcode,
//			type: "GET",
//			cache: false,
//			success : function(response) {
//				console.log("response "+response);
//				var values=response.split("|");
//				var isServicable=values[0];
//				if(isServicable=='N'){
//					$("#addressPincodeServicableDiv").show();
//					$("#addressPincodeServicableDiv").html("<p>Pincode is not serviceable</p>");
//				}else{
//				
//					$('#addressForm').submit();	
//				}
//			},
//			error : function(resp) {
//				alert("Some issues are there with Checkout at this time. Please try  later or contact our helpdesk");
//			}
//		});	 
	}
	return false;
});



function setBankForSavedCard(bankName){
//TISEE-5555
//	$.ajax({
//		url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/setBankForSavedCard",
//		data: { 'bankName' : bankName },
//		type: "GET",
//		cache: false,
//		success : function(response) {
//			applyPromotion();
//		},
//		error : function(resp) {
//			$("#no-click").remove();
//		}
//	});	
	applyPromotion(bankName);	

}


function applyPromotion(bankName)
{
	$(".make_payment").attr('disabled','true');
	var paymentMode=$("#paymentMode").val();
	$("#promotionApplied,#promotionMessage").css("display","none");
	var selectedBank=$("select[id='bankNameForEMI']").find('option:selected').text();
	$.ajax({
		url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/applyPromotions",
		data: { 'paymentMode' : paymentMode , 'bankName' : bankName },
		type: "GET",
		cache: false,
		dataType:'json',
		success : function(response) {
			if(null!=response.errorMsgForEMI && response.errorMsgForEMI=="redirect")
			{
				$(location).attr('href',ACC.config.encodedContextPath+"/cart"); //TISEE-510
			}
			else
			{
				var totalDiscount=0;
				var productDiscount="";
				var orderDiscount="";
				$("#promotionMessage").empty();
				var total=response.totalPrice.formattedValue;
				document.getElementById("totalWithConvField").innerHTML=response.totalPrice.formattedValue;
				$("#cartPromotionApplied").css("display","none");
				$("#codAmount").text(response.totalPrice.formattedValue);
				if(response.mplPromo==null || response.mplPromo==[])
				{
					$("#promotionApplied,#promotionMessage").css("display","none");
				}
				else
				{
					for (var x = 0; x < response.mplPromo.length; x++)
					{
						var promoIdentifier=response.mplPromo[x].promoTypeIdentifier;
						if(promoIdentifier=="PotentialPromotion")
						{
							var spanTag = document.createElement("p");
							spanTag.id = "p"+x;	
							$("#promotionApplied").css("display","none");
							$("#promotionMessage").css("display","block");
							spanTag.innerHTML=response.mplPromo[x].potentialPromotion.promoMessage;
							$("#promotionMessage").append(spanTag);
							$("spanTag.id").append('</br>');
						}
						
					}
					
					//TISEE-352
					if(response.totalDiscount.value != 0){
						$("#promotionApplied").css("display","block");
						document.getElementById("promotion").innerHTML="-"+response.totalDiscount.formattedValue;				
					}
					
					//TISST-7955
					var ussidPriceJson = JSON.parse(response.ussidPriceDetails);
					for ( var key in ussidPriceJson) 
					{
						var ussid=key;
						var ussidPrice= ussidPriceJson[key];
						$("#"+ussid+"_productPriceId").html(ussidPrice);
					}
				}
				//TISPT-29
				if(paymentMode=='EMI' || paymentMode=='Credit Card' || paymentMode=='Debit card' || paymentMode=='Netbanking')
				{
					isCodSet = false;
				}
				var cartTotal=response.totalPrice.value;
				
				if(paymentMode=='EMI')
				{
					if(selectedBank==""){
						//var cartTotal=response.totalPrice.value;
						$.ajax({
							url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/getEMIBanks",
							data: { 'cartTotal' : cartTotal },
							type: "GET",
							cache: false,
							dataType:'json',
							success : function(response) {
								if(response.length>0){
									$("#bankNameForEMI, #listOfEMiBank").css("display","block");
									$("#emiRangeError").css("display","none");
									var bankList=document.getElementById("bankNameForEMI");
									var fragment = document.createDocumentFragment();
									var opt = document.createElement('option');
									opt.innerHTML = "Select";
									opt.value = "select";
									fragment.appendChild(opt);
								    bankList.appendChild(fragment);
									for(var i=0; i<response.length;i++)
									{
										var opt = document.createElement('option');
										opt.innerHTML = response[i].bankName;
										opt.value = response[i].bankCode;
									    fragment.appendChild(opt);
									    bankList.appendChild(fragment);
									}
								}
								else{								
									$("#bankNameForEMI, #listOfEMiBank").css("display","none");
									$("#emiRangeError").css("display","block");
								}
								
							},
							error : function(resp) {
								$("#bankNameForEMI, #listOfEMiBank").css("display","none");
								$("#emiRangeError").css("display","block");
							}
						});
					}
					else if(selectedBank!="Select" && selectedBank!=""){
						$("#is_emi").val("true");
				        $("#emi_bank").val(selectedBank);
				    	$("#card").css("display","none");
				    	$("#dcHeader").css("display","none");
				    	$("#ccHeader").css("display","none");
				    	if(null!=response.errorMsgForEMI){
				    		$("#emiPromoError").css("display","block");
				    		$("#emiPromoError").text(response.errorMsgForEMI);
				    	}
						$.ajax({
							url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/getTerms",
							data: { 'selectedEMIBank' : selectedBank },
							type: "GET",
							cache: false,
							success : function(data) {	
								$("#radioForEMI").css("display","block");
								$("#selectedTerm").val("select");
								var emiTable=document.getElementById("EMITermTable");
								var rowCount = emiTable.rows.length;
								for(var i=rowCount-1; i>0; i--){
									emiTable.deleteRow(i);
									rowCount--;
								}
								if(data[0]!=undefined){
									$("#radioForEMI").css("display","block");
									for	(var index = 0; index < data.length; index++){
										var rowCount = emiTable.rows.length;
										var row = emiTable.insertRow(rowCount);
										row.insertCell(0).innerHTML= '<input type="radio" name="termRadio" id="termRadioId' + index + '" value="" style="display: inherit;" onclick="validateSelection()"><label for="termRadioId' + index + '"></label>';
										row.insertCell(1).innerHTML= data[index].term + " months";
										row.insertCell(2).innerHTML= data[index].interestRate + "%";
										row.insertCell(3).innerHTML= /*"Rs. " + */data[index].monthlyInstallment;
										row.insertCell(4).innerHTML= /*"Rs. " + */data[index].interestPayable;
										var radioId=document.getElementsByName("termRadio")[index].id;
										document.getElementById(radioId).value=data[index].term;
										
									}
									$("#emi-notice").show();
								}
								else{
									$("#radioForEMI").css("display","none");
									$("#emiNoBankError").show();
								}
							},
							error : function(resp) {
								alert("Please select a Bank again");
							}
						});
					}
				}
				$("#no-click").remove();
				$(".make_payment").removeAttr('disabled');
			}
		},
		error : function(resp) {
			$("#no-click").remove();
			$(".make_payment").removeAttr('disabled');
		}
	});
}


function submitNBForm(){
	$("#netbankingIssueError").css("display","none");
	var selectedValue=$('input[class=NBBankCode]:checked').val();
	if(selectedValue==undefined)
	{
		selectedValue=$('select[id="bankCodeSelection"]').val();
	}
	if(selectedValue=="select")
	{
		$("#netbankingError").css("display","block");
	}
	else{
		$(".pay button").prop("disabled",true);
		$(".pay button").css("opacity","0.5");
		$(".pay").append('<img src="/store/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: absolute; right: 25%;bottom: 30px; height: 30px;">');
		$(".pay .spinner").css("left",(($(".pay.top-padding").width()+$(".pay.top-padding button").width())/2)+10);
		$("body").append("<div id='no-click' style='opacity:0.00; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
		
		$("#netbankingError").css("display","none");
		var firstName=selectedValue;
		var lastName=addressLine1=addressLine2=addressLine3=country=state=city=pincode=null;
		var cardSaved=false;
		$.ajax({
			url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/createJuspayOrder",
			data: { 'firstName' : firstName , 'lastName' : lastName , 'addressLine1' : addressLine1, 'addressLine2' : addressLine2 , 'addressLine3' : addressLine3, 'country' : country , 'state' : state, 'city' : city , 'pincode' : pincode, 'cardSaved' : cardSaved},
			type: "GET",
			cache: false,
			success : function(response) {
				if(response=='redirect'){
					$(location).attr('href',ACC.config.encodedContextPath+"/cart"); //TIS 404
				}
				else if(response=="" || response==null){
					$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/add"); 
				}
				else{
					
					//TISPRO-153
					sendTealiumData();
					
					$("#juspayOrderId").val(response);
					var juspayOrderId=$("#juspayOrderId").val();
					$.ajax({
						url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/submitNBForm",
						data: { 'selectedNBBank' : selectedValue , 'juspayOrderId' : juspayOrderId },
						dataType: "json",
						contentType : "application/json; charset=utf-8",
						type: "GET",
						cache: false,
						success : function(response) {
								var juspayResponse = JSON.parse(response);
								//console.log(juspayResponse);
								var url = juspayResponse.payment.authentication.url;
								var method = juspayResponse.payment.authentication.method;
								
								if(method === "POST") {
									var frm = document.createElement("form")
									frm.style.display = "none"; // ensure that the form is hidden from the user
									frm.setAttribute("method", method);
									frm.setAttribute("action", url);	
									
									var params = juspayResponse.payment.authentication.params;
									for(var key in params) {
								    var value = params[key];
								    var field = document.createElement("input");
								    field.setAttribute("type", "hidden");
								    field.setAttribute("name", key);
								    field.setAttribute("value", value);
								    frm.appendChild(field);
								  }
									document.body.appendChild(frm)
									// form is now ready
									frm.submit();
								}
								
								 if(method == "GET") {
								    window.location.href = url;
								    return;
								 }
								
//								var frm = document.createElement("form")
//								frm.style.display = "none"; // ensure that the form is hidden from the user
//								frm.setAttribute("method", method);
//								frm.setAttribute("action", url);
//								if(method === "POST") {
//								  var params = juspayResponse.payment.authentication.params;
//								  for(var key in params) {
//								    var value = params[key];
//								    var field = document.createElement("input");
//								    field.setAttribute("type", "hidden");
//								    field.setAttribute("name", key);
//								    field.setAttribute("value", value);
//								    frm.appendChild(field);
//								  }
//								}
//								document.body.appendChild(frm)
//								// form is now ready
//								frm.submit();
								
								
						},
						error : function(resp) {
							$("#netbankingIssueError").css("display","block");
							$(".pay button").prop("disabled",false);
							$(".pay button").css("opacity","1");
							$(".pay .spinner").remove();
							$("#no-click").remove();
						}
					});		
				}
			},
			error : function(resp) {
			}
		});
	}
}

function calculateDeliveryCost(radioId,deliveryCode)
{
	if(radioId=="" || radioId==undefined || deliveryCode=="" || deliveryCode==undefined )
	{
		var radioSelected=$('#deliveryradioul input:radio');	
		 radioSelected.each(function() {
			 if (this.checked != true){
				 var radioButtonId=$(this).attr('id');
				 $('#'+radioButtonId).prop('checked', true);
				 var checkedDeliveryCode = radioButtonId.split("_")[2];
				 var entryNumber = radioButtonId.split("_")[1];
				 $("#radio_"+entryNumber).val(checkedDeliveryCode);
			 }
		    });
	}
	
	if(radioId!="" && radioId!="undefined" && deliveryCode!="" && deliveryCode!="undefined" )
	{
		$("#"+radioId).val(deliveryCode);
	}
	
	var radioSelected=$('#deliveryradioul input:radio');
	var totalDeliveryCharge=0;
	 
	 radioSelected.each(function() {
	        if (this.checked === true) {
	        	var delCost=$(this).val();
	        	totalDeliveryCharge +=  parseFloat(delCost);
	           }
	    });
	 
	 $.ajax({
	 		url: ACC.config.encodedContextPath + "/checkout/multi/delivery-method/calculateDeliveryCost/"+totalDeliveryCharge,
	 		type: "POST",
	 		cache: false,
	 		success : function(response) {
	 			var values=response.split("|");
	 			var currency=values[0];
	 			var deliveryCost=values[1];
	 			var totalPrice=values[2];
	 			
	 			$("#convChargeFieldId, #convChargeField").css("display","none");
	 			//TISST-13010
	 			$("#cartPromotionApplied").css("display","block");
	 			if(parseFloat(deliveryCost) > 0){
	 				document.getElementById("deliveryCostSpanId").innerHTML=currency+deliveryCost;
		 		}else{
	 				document.getElementById("deliveryCostSpanId").innerHTML="Free";
		 		}
	 			
	 			document.getElementById("totalWithConvField").innerHTML=currency+totalPrice;
	 			isCodSet = false;
	 		},
	 		error : function(resp) {
	 			alert("Some issues are there with Checkout at this time. Please try  later or contact our helpdesk");

	 		}
	 	});	 
}
////TISST-13010
function showPromotionTag()
{
	$("#cartPromotionApplied").css("display","block");
}

$(document).ready(function(){
	$("#defaultPinCodeIds").keyup(function(event){
	    if(event.keyCode == 13){
	        $("#pinCodeButtonIds").click();
	    }
	});

});


function checkPincodeServiceability(buttonType)
{
	var selectedPincode=$('#defaultPinCodeIds').val();
	var regPostcode = /^([1-9])([0-9]){5}$/;
	
	if(selectedPincode === ""){
		
		$( "#error-Id").hide();
		$("#emptyId").css({
			"color":"red",
			"display":"block",
			});

		return false;
	}
	else if(regPostcode.test(selectedPincode) != true){
    	$("#defaultPinCodeIds").css("color","red");
        $("#error-Id").show();
		$("#emptyId").hide();
        //$("#checkoutBtnIdLink").hide();
		//$("#expresscheckoutid").hide();
		//$("#checkoutBtnIdButton").show();
		$("#error-Id").css({
			"color":"red",
			"display":"block",

			});
		
        return false;  
    }
	else
    {
		$("#defaultPinCodeIds").css("color","black");
		$( "#error-Id").hide();
		$("#emptyId").hide();
	$.ajax({
 		url: ACC.config.encodedContextPath + "/cart/checkPincodeServiceability/"+selectedPincode,
 		type: "GET",
 		cache: false,
 		success : function(response) {
 			if(response=="N")
 				{
 				alert("Some issues are there with Checkout at this time. Please try  later or contact our helpdesk");
 	 			$("#isPincodeServicableId").val('N');
 				}
 			else
 				{
 					populatePincodeDeliveryMode(response,buttonType);
 				}
 			
 		},
 		error : function(resp) {
 			alert("Some issues are there with Checkout at this time. Please try  later or contact our helpdesk");
 			$("#isPincodeServicableId").val('N');
 		}
 	});
	if ($('#giftYourselfProducts').html().trim().length > 0 && selectedPincode!=null && selectedPincode != undefined && selectedPincode!="") 
	{
		window.location.reload();
		
	}
   }
}


function populatePincodeDeliveryMode(response,buttonType){
	
	var checkoutLinkURlId = $('#checkoutLinkURlId').val(); 
	//response='Y|123456|[{"fulfilmentType":null,"isPrepaidEligible":"Y","ussid":"123653098765485130011717","pinCode":null,"validDeliveryModes":[{"isCOD":true,"isPrepaidEligible":null,"isPincodeServiceable":null,"isCODLimitFailed":null,"type":"ED","inventory":"2","deliveryDate":null},{"isCOD":true,"isPrepaidEligible":null,"isPincodeServiceable":null,"isCODLimitFailed":null,"type":"HD","inventory":"4","deliveryDate":null}],"cod":"Y","transportMode":null,"isCODLimitFailed":"N","deliveryDate":"2015-08-29T13:30:00Z","isServicable":"Y","stockCount":12},{"fulfilmentType":null,"isPrepaidEligible":"Y","ussid":"123653098765485130011719","pinCode":null,"validDeliveryModes":[{"isCOD":true,"isPrepaidEligible":null,"isPincodeServiceable":null,"isCODLimitFailed":null,"type":"HD","inventory":"12","deliveryDate":null}],"cod":"Y","transportMode":null,"isCODLimitFailed":"N","deliveryDate":"2015-08-29T13:30:00Z","isServicable":"Y","stockCount":12}]';
	//response='N|123456|[{"fulfilmentType":null,"isPrepaidEligible":"Y","ussid":"123653098765485130011717","pinCode":null,"validDeliveryModes":[{"isCOD":true,"isPrepaidEligible":null,"isPincodeServiceable":null,"isCODLimitFailed":null,"type":"ED","inventory":"2","deliveryDate":null},{"isCOD":true,"isPrepaidEligible":null,"isPincodeServiceable":null,"isCODLimitFailed":null,"type":"HD","inventory":"2","deliveryDate":null}],"cod":"Y","transportMode":null,"isCODLimitFailed":"N","deliveryDate":"2015-08-29T13:30:00Z","isServicable":"Y","stockCount":2},{"fulfilmentType":null,"isPrepaidEligible":null,"ussid":"123653098765485130011719","pinCode":null,"validDeliveryModes":null,"cod":null,"transportMode":null,"isCODLimitFailed":null,"deliveryDate":null,"isServicable":"N","stockCount":null}]';
	
	console.log(response);
	
	var values=response.split("|");
	var isServicable=values[0];
	var selectedPincode=values[1];
	var deliveryModeJsonMap=values[2];
	var deliveryModeJsonObj = JSON.parse(deliveryModeJsonMap);
	var length = Object.keys(deliveryModeJsonObj).length;
	var isStockAvailable="Y";
	
	for ( var key in deliveryModeJsonObj) {
	var ussId= deliveryModeJsonObj[key].ussid;
	$("#"+ussId+"_qtyul").remove();
	if(deliveryModeJsonObj[key].isServicable==='N'){
		$("#"+ussId).remove();
		var newUi = document.createElement("ul");
		newUi.setAttribute("id", ussId);
		var newSpan = document.createElement("span");
		var text = document.createTextNode("This item is not serviceable for pincode "+selectedPincode);
		newSpan.appendChild(text);
		newUi.appendChild(newSpan);
		$("#"+ussId+"_li").append(newUi);
	}
	else{
		$("#"+ussId).remove();
		var newUi = document.createElement("ul");
		newUi.setAttribute("id", ussId);
		
		var jsonObj=deliveryModeJsonObj[key].validDeliveryModes;
		
		var inventory=0;
		var quantityValue=$("#quantity_"+ussId).val();
		var stockAvailable =false;
		
		for ( var count in jsonObj) {
			
			
			inventory=jsonObj[count].inventory;
			if(parseFloat(quantityValue) <= parseFloat(inventory)){
				stockAvailable=true;
			}
		}
		
		if(stockAvailable==false){
			var newUl = document.createElement("ul");
			newUl.setAttribute("id", ussId+'_qtyul');
			var newLi = document.createElement("li");
			var text = document.createTextNode("Oops! We only have "+inventory+" in stock! For pincode : "+selectedPincode);
			newLi.appendChild(text);
			newUl.appendChild(newLi);
			$("#"+ussId+"_qty").append(newUl);
			isStockAvailable="N";
		}
		
			
			for ( var count in jsonObj) {
				var inventory=0;
				var deliveryType=jsonObj[count].type;
				inventory=jsonObj[count].inventory;
				if(deliveryType==='HD' /*&& parseFloat(inventory) >= parseFloat(quantityValue)*/ ){
					var newLi = document.createElement("li");
					newLi.setAttribute("class", "methodHome");
					var text = document.createTextNode("Home Delivery");
					newLi.appendChild(text);
					newUi.appendChild(newLi);
				}
				else if(deliveryType==='ED'/* && parseFloat(inventory) >= parseFloat(quantityValue)*/){
					var newLi = document.createElement("li");
					newLi.setAttribute("class", "methodExpress");
					var text = document.createTextNode("Express Delivery");
					newLi.appendChild(text);
					newUi.appendChild(newLi);
				}
				else if(deliveryType==='CNC'/* && parseFloat(inventory) >= parseFloat(quantityValue)*/){
					var newLi = document.createElement("li");
					newLi.setAttribute("class", "click-collect");
					var text = document.createTextNode("Click and Collect");
					newLi.appendChild(text);
					newUi.appendChild(newLi);
				}
			}
			$("#"+ussId+"_li").append(newUi);
			
		}
	}
	
	$("#defaultPinCodeIds").val(selectedPincode);	
	
	
	
	if(isServicable==='Y' && isStockAvailable==='Y')
	{
		//TISBOX-879
		$("#isPincodeServicableId").val('Y');
		$('#checkout-id #checkout-enabled').removeClass('checkout-disabled'); //TISEE-6257
		$('#expresscheckoutid #expressCheckoutButtonId').removeClass('express-checkout-disabled'); //TISEE-6257
		//Code Start TISPRD-437
		var str1 = document.referrer; 
		if(str1.indexOf('checkout') != -1){ //last page checkout
			if($('.global-alerts').length != 0) { //error exist in dom
				var errortext = $(".global-alerts,alert-danger, alert-dismissable").text();
				if( errortext != null && errortext != 'undefined' && errortext != '') {
					  $(".global-alerts").remove();
				} 
			}
		}
		//Code End TISPRD-437
		
		if(buttonType=='typeCheckout')
		{
			redirectToCheckout(checkoutLinkURlId);
		}
		
		if(buttonType=='typeExpressCheckout')
		{
			var expressCheckoutAddresId=$("#expressCheckoutAddressSelector").val(); 
			$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/express?expressCheckoutAddressSelector="+expressCheckoutAddresId);
		}
	}
	else
	{
		$("#isPincodeServicableId").val('N');
		$('#checkout-id #checkout-enabled').addClass('checkout-disabled'); //TISEE-6257
		$('#expresscheckoutid #expressCheckoutButtonId').addClass('express-checkout-disabled'); //TISEE-6257
	}
}


//TISBOX-879
function redirectToCheckout(checkoutLinkURlId)
{
	var checkoutUrl = checkoutLinkURlId;
	var cartEntriesError=false;
	cartEntriesError = ACC.pickupinstore.validatePickupinStoreCartEntires();
	if (!cartEntriesError)
	{
		var expressCheckoutObject = $('.express-checkout-checkbox');
		if(expressCheckoutObject.is(":checked"))
		{
			window.location = expressCheckoutObject.data("expressCheckoutUrl");
		}
		else
		{
			var flow = $('#selectAltCheckoutFlow').attr('value');
			if ( flow == undefined || flow == '')
			{
				// No alternate flow specified, fallback to default behaviour
				window.location = checkoutUrl;
			}
			else
			{
				// Fix multistep-pci flow
				if ('multistep-pci' == flow)
				{
				flow = 'multistep';
				}
				var pci = $('#selectPciOption').attr('value');

				// Build up the redirect URL
				var redirectUrl = checkoutUrl + '/select-flow?flow=' + flow + '&pci=' + pci;
				window.location = redirectUrl;
			}
		}
	}
	return false;
}

function checkIsServicable()
{
	
	var selectedPincode=$("#defaultPinCodeIds").val();
	if(selectedPincode!=null && selectedPincode != undefined && selectedPincode!=""){
	
		$.ajax({
	 		url: ACC.config.encodedContextPath + "/cart/checkPincodeServiceability/"+selectedPincode,
	 		type: "GET",
	 		cache: false,
	 		success : function(response) {
	 			populatePincodeDeliveryMode(response,'pageOnLoad');
	 		},
	 		error : function(resp) {
	 			alert("Some issues are there with Checkout at this time. Please try  later or contact our helpdesk");
	 			$("#isPincodeServicableId").val('N');
	 		}
	 	});
	}
	
}

function activateSignInTab()
{
	var isSignInActive=$("#isSignInActive").val();
	if(isSignInActive==='Y'){
		$("#signIn_link").addClass('active');
		$("#sign_in_content").addClass('active');
		$("#SignUp_link").removeClass('active');
		$("#sign_up_content").removeClass('active');
	}else{
		$("#SignUp_link").addClass('active');
		$("#sign_up_content").addClass('active');
		$("#signIn_link").removeClass('active');
		$("#sign_in_content").removeClass('active');
	}
}
function checkSignInValidation(path){
	
	if(path=="Checkout")
	{
		var emailId = $("#j_username").val();
		var password = $("#j_password").val();
		var encodedPWD= encodeURIComponent(password);
		$("#j_password").val(encodedPWD);
	}
	else
	{
		var emailId = $("#j_username_login").val();
		var password = $("#j_password_login").val();
		var encodedPWD= encodeURIComponent(password);
		$("#j_password_login").val(encodedPWD);
	}
	
	
	var emailPattern=/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
	var validationResult= true;
	//TISUT-1761
	$("#signinPasswordDiv").hide();
	$("#signinEmailIdDiv").hide();
	if(emailId == null || emailId == ""){
		$("#signinEmailIdDiv").show();
		$("#signinEmailIdDiv").html("E-mail cannot be left empty");	
		validationResult=false;
	}
	else if(!emailPattern.test(emailId)){
		$("#signinEmailIdDiv").show();
		$("#signinEmailIdDiv").html("Please enter a valid email ID");
		validationResult=false;
	}
	else if(password==null || password=="" || password.length==0){
		$("#signinPasswordDiv").show();
		$("#signinPasswordDiv").html("Password cannot be left empty");
		validationResult=false;
	}
	
	// Code shoud be commented : No length check is required for Sign In
	
	/*else if(password.length < 8){
		$("#signinPasswordDiv").show();
		$("#signinPasswordDiv").html("Invalid username or password");
		validationResult=false;
	}*/
	
	else{
		$("#signinPasswordDiv").hide();
	}
	// TISPRO-153
	/*if(validationResult){
		utag.link({ "event_type" : "Login", "link_name" : "Login" });
	}*/
	return validationResult;
}

function checkSignUpValidation(path){
	
	var password = "";
	var rePassword = "";
	
	if(path=="Checkout")
	{
		var emailId = document.getElementById("register.email").value;
		password = $("#password").val();
		rePassword = document.getElementById("register.checkPwd").value;
		$("#password_minchar").hide();
	}
	else
	{
		var emailId = document.getElementById("register.email_login").value;
		password = $("#password_login").val();
		rePassword = document.getElementById("register.checkPwd_login").value;
		$("#password_login_minchar").hide();
	}
	
	
	var emailPattern=/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
	var validationResult= true;
	
	if(emailId == null || emailId.length==0){
		$("#signupEmailIdDiv").show();
		$("#signupEmailIdDiv").html("Email is blank");
		validationResult=false;	
	}else if(!emailPattern.test(emailId)){
		$("#signupEmailIdDiv").show();
		$("#signupEmailIdDiv").html("Please enter a valid email ID");
		validationResult=false;	
	}else{
		$("#signupEmailIdDiv").hide();
	}
	
	if(password==null || password.length==0){
		$("#signupPasswordDiv").show();
		$("#signupPasswordDiv").html("Password is blank");
		validationResult=false;
	}else if(password.length < 8){
		$("#signupPasswordDiv").show();
		$("#signupPasswordDiv").html("Minimum length is 8 characters");
		validationResult=false;
	}else{
		$("#signupPasswordDiv").hide();
	}
	
	if(rePassword==null || rePassword.length==0){
		$("#signupConfirmPasswordDiv").show();
		$("#signupConfirmPasswordDiv").html("Confirm Password is blank");
		validationResult=false;
	}else if(rePassword.length < 8){
		$("#signupConfirmPasswordDiv").show();
		$("#signupConfirmPasswordDiv").html("Minimum length is 8 characters");
		validationResult=false;
	}else{
		$("#signupConfirmPasswordDiv").hide();
	}
	
	if(validationResult && password!=rePassword){
		$("#signupPasswordDiv").show();
		$("#signupPasswordDiv").html("The passwords don’t match. Try again?");
		validationResult=false;
	}  else if(validationResult){
		$("#signupPasswordDiv").hide();
		$("#signupConfirmPasswordDiv").hide();
	
	}
		
	//Fix for defect TISEE-3986 : handling special character like #	
	if(validationResult)
	{
		// TISPRO-153
		/*utag.link({ "event_type" : "Login", "link_name" : "Login" });*/
		var encodedPWD= encodeURIComponent(password);
		var encodedRePWD= encodeURIComponent(rePassword);		
		if(path=="Checkout"){
			$("#password").val(encodedPWD);
			document.getElementById("register.checkPwd").value=encodedRePWD;
		}
		else{
			$("#password_login").val(encodedPWD);
			document.getElementById("register.checkPwd_login").value=encodedRePWD;		
		}
	}
	
	return validationResult;	
}

function checkExpressCheckoutPincodeService(buttonType){
	//TISBOX-1631
	var selectedAddressId= $("#addressListSelectId").val();
	selectedAddressId =$.trim(selectedAddressId);
	$("#expressCheckoutAddressSelector").val(selectedAddressId);
	
	if(selectedAddressId.length > 0){
		//TISBOX-882
		$("#expresscheckoutErrorDiv").css("display","none");
		$.ajax({
	 		url: ACC.config.encodedContextPath + "/cart/checkExCheckoutPincodeServiceability/"+selectedAddressId,
	 		type: "GET",
	 		cache: false,
	 		success : function(response) {
	 			populatePincodeDeliveryMode(response,buttonType);
	 		},
	 		error : function(resp) {
	 			alert("Some issues are there with Checkout at this time. Please try  later or contact our helpdesk");
	 			$("#isPincodeServicableId").val('N');
	 		}
	 	});	 
	}
	else{
		//TISBOX-882
		$("#isPincodeServicableId").val('N');
		$("#expresscheckoutErrorDiv").css("display","block");
	}
}


//Error message handling for Cards
$("#cardNo").focus(function(){
	document.getElementById("cardNoError").innerHTML="";
});
$(".name_on_card").focus(function(){
	document.getElementById("memberNameError").innerHTML="";
});
$(".card_exp_month").focus(function(){
	document.getElementById("expYYError").innerHTML="";
});
$(".card_exp_year").focus(function(){
	document.getElementById("expYYError").innerHTML="";
});
$(".security_code").focus(function(){
	document.getElementById("cvvError").innerHTML="";
});
$("#cardNo").blur(function(){
	if($("#cardNo").val()!="")
	{
		validateCardNo();
	}
});
$(".name_on_card").blur(function(){
	if($(".name_on_card").val()!="")
	{
		validateName();
	}
});
$(".card_exp_month").blur(function(){
	if($(".card_exp_month").val()!="month")
	{
		validateExpMM();
	}
	else if($(".card_exp_month").val()=="month" && $(".card_exp_year").val()!="year")
	{
		validateExpMM();
	}
});
$(".card_exp_year").blur(function(){
	if($(".card_exp_year").val()!="year")
	{
		validateExpYY();
	}
	else if($(".card_exp_month").val()!="month" && $(".card_exp_year").val()=="year")
	{
		validateExpYY();
	}
});
$(".security_code").blur(function(){
	if(this.value!="")
	{
		validateCVV();
	}
});
$("#cardNo").keyup(function() {
    if (!this.value) {
    	document.getElementById("cardNoError").innerHTML="";
    }
});
$(".name_on_card").keyup(function() {
    if (!this.value) {
    	document.getElementById("memberNameError").innerHTML="";
    }
});
$(".security_code").keyup(function() {
	if (!this.value) {
    	document.getElementById("cvvError").innerHTML="";
    }
});
$(".card_exp_month").keyup(function() {
    if (this.value=="month" && $(".card_exp_year").val()=="year") {
    	document.getElementById("expYYError").innerHTML="";
    }
});
$(".card_exp_year").keyup(function() {
    if (this.value=="year" && $(".card_exp_month").val()=="month") {
    	document.getElementById("expYYError").innerHTML="";
    }
});
//Error message handling for cards ends


//Calling isNumber on keypress
$( "#cardNo" ).keypress(function(evt){
  return isNumber(evt);
});
$( ".security_code" ).keypress(function(evt){
	  return isNumber(evt);
});
//Calling isNumber ends


//OnChange for Billing address
$( "#sameAsShipping" ).change(function(){
	populateAddress();
});
//Onchange ends


//New Form Validation

function validateNameOnAddress(name, errorHandle, identifier) {
	var regex = new RegExp(/^[a-zA-Z ]+$/);
	if(name=="" && identifier=="firstName"){
		errorHandle.innerHTML = "Please enter a First name.";
        return false;
	}
	if(name=="" && identifier=="lastName"){
		errorHandle.innerHTML = "Please enter a Last name.";
        return false;
	}
	else if (!regex.test(name)) {
		errorHandle.innerHTML = "Only alphabets and spaces allowed.";
        return false;
    }
	else if (name.length < 1 && identifier=="firstName") {
		errorHandle.innerHTML = "Name should be longer."; 
		return false;
	}
	else if (name.length < 1 && identifier=="lastName") {
		errorHandle.innerHTML = "Name should be longer.";
		return false;
	}
	errorHandle.innerHTML = "";
	return true;
}

//NOT Needed
/*function validateLastNameOnAddress(name, errorHandle) {
	var regex = new RegExp(/^[a-zA-Z ]+$/);
	if (!regex.test(name)) {
		errorHandle.innerHTML = "Only alphabets and spaces allowed.";
        return false;
    }
	else if(name==""){
		errorHandle.innerHTML = "Please enter a valid name.";
        return false;
	}
	errorHandle.innerHTML = "";
	return true;
}
*/
$("#firstName").keyup(function() {
    if (!this.value) {
    	document.getElementById("firstNameError").innerHTML="";
    }
});

$("#firstName").focus(function(){
	document.getElementById("firstNameError").innerHTML="";
});

$("#lastName").keyup(function() {
    if (!this.value) {
    	document.getElementById("lastNameError").innerHTML="";
    }
});

$("#lastName").focus(function(){
	document.getElementById("lastNameError").innerHTML="";
});

function validateAddressLine1(addressLine, errorHandle){
	if(addressLine==""){
		errorHandle.innerHTML = "Please enter an Address line 1.";
        return false;
	}
	errorHandle.innerHTML = "";
	return true;
}

function validateAddressLine2(addressLine, errorHandle){
	if(addressLine==""){
		errorHandle.innerHTML = "Please enter an Address line 2.";
        return false;
	}
	errorHandle.innerHTML = "";
	return true;
}


function validateLandmark(addressLine, errorHandle){
	if(addressLine==""){
		errorHandle.innerHTML = "Please enter a Landmark.";
        return false;
	}
	errorHandle.innerHTML = "";
	return true;
}

$("#address1").keyup(function() {
    if (!this.value) {
    	document.getElementById("address1Error").innerHTML="";
    }
});

$("#address1").focus(function(){
	document.getElementById("address1Error").innerHTML="";
});

$("#address2").keyup(function() {
    if (!this.value) {
    	document.getElementById("address2Error").innerHTML="";
    }
});

$("#address2").focus(function(){
	document.getElementById("address2Error").innerHTML="";
});

$("#address3").keyup(function() {
    if (!this.value) {
    	document.getElementById("address3Error").innerHTML="";
    }
});

$("#address3").focus(function(){
	document.getElementById("address3Error").innerHTML="";
});

$("#pincode").keyup(function() {
    if (!this.value) {
    	document.getElementById("pinError").innerHTML="";
    }
});

$("#pincode").focus(function(){
	document.getElementById("pinError").innerHTML="";
});

function validateCity() {
	var name=$("#city").val();
	var errorHandle=document.getElementById("cityError");
	var regex = new RegExp(/^[a-zA-Z ]+$/);
	if(name==""){
		errorHandle.innerHTML = "Please enter a City.";
        return false;
	}
	else if (!regex.test(name)) {
		errorHandle.innerHTML = "Please enter a valid City.";
        return false;
	}
	errorHandle.innerHTML = "";
	return true;
}

function validateState() {
	var name=$("#state").val();
	var errorHandle=document.getElementById("stateError");
	var regex = new RegExp(/^[a-zA-Z\. ]+$/);
	if(name==""){
		errorHandle.innerHTML = "Please enter a State.";
        return false;
	}
	else if (!regex.test(name)) {
		errorHandle.innerHTML = "Please enter a valid State.";
        return false;
	}
	errorHandle.innerHTML = "";
	return true;
}

$("#city").keyup(function() {
    if (!this.value) {
    	document.getElementById("cityError").innerHTML="";
    }
});

$("#city").focus(function(){
	document.getElementById("cityError").innerHTML="";
})

$("#state").keyup(function() {
    if (!this.value) {
    	document.getElementById("stateError").innerHTML="";
    }
});

$("#state").focus(function(){
	document.getElementById("stateError").innerHTML="";
})


function updateCart(formId){
	var entryNumber = formId.split("_");
	var form = $('#updateCartForm' + entryNumber[1]);
	form.submit();
}


function expressbutton()
{
	var addressList= $("#addressListSelectId").val();
	var selectedAddressId =$.trim(addressList);
	$("#expressCheckoutAddressSelector").val(selectedAddressId);
 	
	//TISBOX-882
	if(selectedAddressId.length == 0)
	{
		$("#expresscheckoutErrorDiv").css("display","block");
		return false;
	}
	else
	{
		$("#expresscheckoutErrorDiv").css("display","none");
		
		
		$.ajax({
	 		url: ACC.config.encodedContextPath + "/cart/checkExCheckoutPincodeServiceability/"+selectedAddressId,
	 		type: "GET",
	 		cache: false,
	 		success : function(response) {
	 			populatePincodeDeliveryMode(response,'typeExpressCheckout');
	 		},
	 		error : function(resp) {
	 			alert("Some issues are there with Checkout at this time. Please try  later or contact our helpdesk");
	 			$("#isPincodeServicableId").val('N');
	 		}
	 	});	 
	}
	
	//return false;
}



//COD
$("#otpNUMField").focus(function(){	
	$("#emptyOTPMessage, #wrongOtpValidationMessage, #expiredOtpValidationMessage").css("display","none");;
});

$("#otpMobileNUMField").focus(function(){	
	$("#customerBlackListMessage").css("display","none");;
});


$(".savedEMICard").find(".credit-card-group").find(".cvv").find(".security_code").focus(function(){	
	$("#savedCVVError").css("display","none");
	$(".card_ebsErrorSavedCard, .card_cvvErrorSavedCard").css("display","none");
});

function hideError(){
	$("#savedCVVError").css("display","none");
	$(".card_ebsErrorSavedCard, .card_cvvErrorSavedCard").css("display","none");
}

function clearDisable()
{
	$("#no-click").remove();
	$(".make_payment").removeAttr('disabled');
}

function sendTealiumData(){
	try {		
		var payment_method_map = {
		            "viewPaymentEMI": "EMI",
		            "viewPaymentCredit": "Credit Card",
		            "viewPaymentNetbanking": "NetBanking",
		            "viewPaymentCOD": "COD",
		            "viewPaymentDebit": "Debit Card"
		        };
		        var payment = jQuery("ul.checkout-paymentmethod.nav li.active span").attr("id");
		        var payment_mode = payment_method_map[payment];
		        var payment_type = "",
		            priority_banks = "";
		        if (payment_mode === "EMI") {
		        	
		            payment_type = jQuery("select#bankNameForEMI").val();
	
		        } else if (payment_mode === "Credit Card" || payment_mode === "Debit Card") {
	
		            payment_type = jQuery("li.active-card span").attr("class") || "Saved Card";
	
		        } else if (payment_mode === "NetBanking") {
	
		            priority_banks = jQuery("#netbanking input[name='priority_banks']:checked");
		            if (priority_banks.length > 0) {
		                payment_type = priority_banks.val();
		            } else {
		            	payment_type = jQuery("#netbanking #bankCodeSelection").val();
		            }
	
		        } else if (payment_mode === "COD") {
		            payment_type = "COD";
		        }
	
		        if (!payment_type) {
		            payment_type = "NA";
		        }
		        
		        if(!(utag.data.product_id === "" || utag.data.product_id === undefined))
		        {
		        	
		        	if(payment_mode === "COD"){
		        		utag.link({
				            "link_name": 'Final Checkout',
				            "event_type": 'PayNow',
				            "payment_method": "" + payment_type,
				            "product_id": utag.data.product_id
		
				        });
		        		
		        	}else{
		        		
		        	
			        	utag.link({
				            "link_name": 'Final Checkout',
				            "event_type": 'PayNow',
				            "payment_method": "" + payment_mode + "|" + payment_type,
				            "product_id": utag.data.product_id
		
				        });
		        	}
		        }
	        
	   } catch (e) {
		// TODO: handle exception
	   }     
}