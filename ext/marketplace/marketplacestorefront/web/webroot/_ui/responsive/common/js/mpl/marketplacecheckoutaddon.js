var isCodSet = false;	//this is a variable to check whether convenience charge is set or not
var binStatus= false;
var isNewCard = false; // this is variable to fix paynow blackout issue

var couponApplied=false;
var bankNameSelected=null;
//var promoAvailable=$("#promoAvailable").val();
//var bankAvailable=$("#bankAvailable").val();

// Display forms based on mode button click
$("#viewPaymentCredit").click(function(){
	
	/*TPR-3446 new starts*/
	var staticHost = $('#staticHost').val();
	//$("body").append("<div id='no-click' style='opacity:0.40; background:transparent; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
	/*TPR-3446 new ends*/
	
	// Display Credit Card form only if the session is active
	if(isSessionActive()){
		displayCreditCardForm();
	}
	else{
		redirectToCheckoutLogin();	
	}
	
	if($("*[data-id=savedCCard]").is(":checked")){
		$(".proceed-button").each(function(){
			$(this).hide();
		});
		$("#make_saved_cc_payment_up").show();
	}else{
		$(".proceed-button").each(function(){
			$(this).hide();
		});
		$("#make_cc_payment_up").show();
	}
	//TPR-665
	if(typeof utag !="undefined"){
	utag.link({
		"link_text": "pay_credit_card_selected" , "event_type" : "payment_mode_selection"
			
	});
	}
});

$("#viewPaymentDebit").click(function(){
	/*TPR-3446 new starts*/
	var staticHost = $('#staticHost').val();
	//$("body").append("<div id='no-click' style='opacity:0.40; background:transparent; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
	/*TPR-3446 new ends*/
	// Display Debit Card form only if the session is active
	if(isSessionActive()){
		displayDebitCardForm();
	}
	else{
		redirectToCheckoutLogin();	
	}
	
	if($("*[data-id=savedDCard]").is(":checked")){
		$(".proceed-button").each(function(){
			$(this).hide();
		});
		$("#make_saved_dc_payment_up").show();
	}else{
		$(".proceed-button").each(function(){
			$(this).hide();
		});
		$("#make_dc_payment_up").show();
	}
	//TPR-665
	utag.link({
		"link_text": "pay_debit_card_selected" , "event_type" : "payment_mode_selection"
	});
});

$("#viewPaymentNetbanking").click(function(){
	/*TPR-3446 new starts*/
	var staticHost = $('#staticHost').val();
	//$("body").append("<div id='no-click' style='opacity:0.40; background:transparent; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
	/*TPR-3446 new ends*/
	// Display NET BANKING form only if the session is active
	if(isSessionActive()){
		displayNetbankingForm();
	}
	else{
		redirectToCheckoutLogin();	
	}
	//TPR-665
	utag.link({
		"link_text": "pay_net_banking_selected" , "event_type" : "payment_mode_selection"
	});
});

$("#viewPaymentCOD").click(function(){
	if (!$("#paymentButtonId_up").is(':visible')){
		$(".totals.outstanding-totalss").css("bottom","0px");
	}
	else{
		$(".totals.outstanding-totalss").css("bottom","40px");
	}
	/*TPR-3446 new starts*/
	var staticHost = $('#staticHost').val();
	//$("body").append("<div id='no-click' style='opacity:0.40; background:transparent; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
	/*TPR-3446 new ends*/
	// Display COD form only if the session is active
	if(isSessionActive()==true){
		displayCODForm();
	}
	else{
		redirectToCheckoutLogin();
	}
	//TPR-665
	utag.link({
		"link_text": "pay_cod_selected" , "event_type" : "payment_mode_selection"
	});
});

$("#viewPaymentEMI").click(function(){
	/*TPR-3446 new starts*/
	var staticHost = $('#staticHost').val();
	//$("body").append("<div id='no-click' style='opacity:0.40; background:transparent; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
	/*TPR-3446 new ends*/
	// Display EMI form only if the session is active
	if(isSessionActive()){
		displayEMIForm();
	}
	else{
		redirectToCheckoutLogin();
	}
	
	//TPR-665
	utag.link({
		"link_text": "pay_emi_selected" , "event_type" : "payment_mode_selection"
	});
}); 
// Mode button click function ends

// Disabling Paste
$('.security_code').on('paste', function () {
	return false;
});

$('#pincode').on('paste', function () {
	return false;
});
// Disabling Paste Ends



function refresh(){
	$(".pay button, #make_cc_payment_up, #make_saved_cc_payment_up, .cod_payment_button_top").prop("disabled",false);
	$(".pay button, #make_cc_payment_up, #make_saved_cc_payment_up, .cod_payment_button_top").css("opacity","1");
	$(".pay .spinner").remove();
	$("#no-click,.spinner").remove();
	// $(".checkout-content.checkout-payment
	// .left-block").css("margin-top","0px");
	
	$("#paymentMode, #bankNameForEMI, #selectedTerm, #bankCodeSelection").val("select");
	if(null!= document.getElementById('silentOrderPostForm')){
		document.getElementById('silentOrderPostForm').reset();
	}
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
	$("#COD, #emi, #netbanking, #card,#cardEmi, #paymentFormButton, #submitPaymentFormButton, #submitPaymentFormCODButton, #mobileNoError, #OTPGenerationErrorMessage, #codMessage, #customerBlackListMessage, #otpValidationMessage, #wrongOtpValidationMessage, #expiredOtpValidationMessage, #fulfillmentMessage, #codItemEligibilityMessage, #emptyOTPMessage, #resendOTPMessage, .nbAjaxError").css("display","none");
	$("#netbankingError,.newCard, #emiRangeError, #juspayconnErrorDiv").css("display","none");
	$("#bankNameForEMI, #listOfEMiBank, #netbankingIssueError, #emiPromoError, #codErrorMessage").css("display","none");
	$("#convChargeFieldId, #convChargeField").css("display","none");
	$(".card_ebsErrorSavedCard, .card_cvvErrorSavedCard, #maestroMessage, #newMaestroMessage").css("display","none");
	$(".make_payment_top_nb, .make_payment_top_savedCard, .make_payment_top_newCard, .cod_payment_button_top").css("display","none");
	$("").css("display","none");
	hideTable();
	if("undefined" != typeof(document.silentOrderPostForm)){
		var selection = document.silentOrderPostForm.EMIBankCode;
		if(selection!=undefined){
			for (i=0; i<selection.length; i++){
				selection[i].checked = false;
			}
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
	// TISEE-5555
	$('.security_code_hide').prop('disabled', true);
	$('.security_code').prop('disabled', false); 
}


// TISPT-235 Commented to make netbanking ajax call

// function displayNetbankingForm(){
// refresh();
// $("#paymentMode").val("Netbanking");
// //resetConvCharge(); TISPT-29
// $('input:password').val("");
// $(".name_on_card").val("");
// applyPromotion(null);
// $("#paymentDetails, #netbanking, #make_nb_payment").css("display","block");
// $(".make_payment_top_nb").css("display","block");
// $("#submitButtons, #paymentFormButton, #submitPaymentFormButton,
// #submitPaymentFormCODButton").css("display","none");
// }



// TISPT-235 Ajax call for netbanking
function displayNetbankingForm(){
	refresh();
	$("#paymentMode").val("Netbanking");
	$("#paymentModeValue").val("Netbanking");
	
	$.ajax({
		url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/setupMplNetbankingForm",
		type: "GET",
		cache: false,
		success : function(response) {
			$(".netbankingPanel").html(response);
			// resetConvCharge(); TISPT-29
			$('input:password').val("");
			$(".name_on_card").val("");	
			applyPromotion(null,"none","none");
			$("#paymentDetails, #make_nb_payment").css("display","block");
			$(".make_payment_top_nb").css("display","block");
			$("#submitButtons, #paymentFormButton, #submitPaymentFormButton, #submitPaymentFormCODButton").css("display","none");
		},
		error : function(resp) {
			$(".nbButton").css("display","none");
			$(".nbAjaxError").css("display","block");
			$("#no-click,.spinner").remove();
			$(".make_payment").removeAttr('disabled');
		}
	});
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
	$("#paymentModeValue").val("EMI");
	$('input:password').val("");
	$(".name_on_card").val("");	
	applyPromotion(null,"none","none");
	//resetConvCharge();	TISPT-29
	$("#paymentDetails, #emi").css("display","block");
	$("#emi-notice").hide();
	$("#emiNoBankError").hide();
}


// Commented for TISPT-235
// function displayCODForm()
// {
// var codEligible=$("#codEligible").val();
// refresh();
// //TISPRD-2138
// applyPromotion(null);
// $("#paymentMode").val("COD");
// var paymentMode=$("#paymentMode").val();
// $("#COD, #paymentDetails, #otpNUM, #sendOTPNumber,
// #sendOTPButton").css("display","block");
// /*$("#enterOTP, #submitPaymentFormButton, #submitPaymentFormCODButton,
// .make_payment, #paymentFormButton,
// #otpSentMessage").css("display","block");*/ //Modified back as erroneously
// pushed by performance team
// $("#enterOTP, #submitPaymentFormButton, #submitPaymentFormCODButton,
// .make_payment, #paymentFormButton,
// #otpSentMessage").css("display","none");/*modified for pprd testing --
// changing back*/
// //setCellNo();
// if(codEligible=="BLACKLISTED")
// {
// $("#customerBlackListMessage").css("display","block");
// $("#otpNUM").css("display","none");
// $("#otpSentMessage").css("display","none");
// $("#no-click").remove();
// }
// else if(codEligible=="NOT_TSHIP")
// {
// $("#fulfillmentMessage").css("display","block");
// $("#otpNUM").css("display","none");
// $("#otpSentMessage").css("display","none");
// $("#no-click").remove();
// }
// else if(codEligible=="ITEMS_NOT_ELIGIBLE")
// {
// $("#codItemEligibilityMessage").css("display","block");
// $("#otpNUM").css("display","none");
// $("#otpSentMessage").css("display","none");
// $("#no-click").remove();
// }
// else if(codEligible=="NOT_PINCODE_SERVICEABLE")
// {
// $("#codMessage").css("display","block");
// $("#otpNUM").css("display","none");
// $("#otpSentMessage").css("display","none");
// $("#no-click").remove();
// }
// else{
// if(isCodSet == false){
// $.ajax({
// url: ACC.config.encodedContextPath +
// "/checkout/multi/payment-method/validateOTP",
// type: "GET",
// data: { 'paymentMode' : paymentMode },
// cache: false,
// success : function(response) {
// if(response==null){
// $(location).attr('href',ACC.config.encodedContextPath+"/cart"); //TISEE-510
// }
// else{
// $("#sendOTPNumber, #convCharge").css("display","block");
// var totalPrice=response.totalPrice.formattedValue;
// var convCharge=response.convCharge.formattedValue;
// $("#convChargeFieldId, #convChargeField").css("display","block");
// if(response.cellNo==""){
// $("#otpMobileNUMField").val("");
// }
// else{
// $("#otpMobileNUMField").val(response.cellNo);
// }
// if(response.convCharge.value!=0){
// document.getElementById("convChargeField").innerHTML=convCharge;
// //alert("Because you have selected COD, convenience charges have been added
// to your order amount")
// $("#convChargeMessage").css("display","inline-block");
// }
// else
// {
// document.getElementById("convChargeField").innerHTML="Free"; //TISPRD-146
// $("#convChargeMessage").css("display","none");
// }
// //document.getElementById("totalWithConvFields").innerHTML=totalPrice;
// TISPT-29
// if(paymentMode!=null){
// applyPromotion(null);
// }
// isCodSet = true;
// }
// },
// error : function(resp) {
// alert("COD is not available at this time. Please select another payment mode
// and proceed");
// $("#no-click").remove();
// }
// });
// }
// else{
// $("#convChargeFieldId, #convChargeField").css("display","block");
// $("#no-click").remove();
// }
// }
// //$("#no-click").remove();
// }




// TISPT-235 : COD Ajax Call
function displayCODForm()
{
	refresh();
	// TISPRD-2138
	// applyPromotion(null);
	$("#paymentMode").val("COD");
	$("#paymentModeValue").val("COD");
	var paymentMode=$("#paymentMode").val();
	//var cartValue=$("#cartValue").val();
	var httpRequest=$("#httpRequest").val();
	var guid=$("#guid").val();

	$.ajax({
		url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/setupMplCODForm",
		type: "GET",
		data: { /*'cartValue' : cartValue , */'request' : httpRequest , 'guid' : guid},		//Commented as not used - TPR-629
		cache: false,
		success : function(response) {
			$("#otpNUM").html(response);
			var codEligible=$("#codEligible").val();
			$("#paymentDetails, #otpNUM, #sendOTPNumber, #sendOTPButton").css("display","block");
			$("#enterOTP, #submitPaymentFormButton, #submitPaymentFormCODButton, #paymentFormButton, #otpSentMessage").css("display","none");/*modified for pprd testing -- changing back*/
			if(codEligible=="BLACKLISTED")
			{
				$("#customerBlackListMessage").css("display","block");
				$("#otpNUM").css("display","none");
				$("#otpSentMessage").css("display","none");
				// $("#no-click").remove();
				$(".terms.cod").remove();
				applyPromotion(null,"none","none");
			}
			else if(codEligible=="NOT_TSHIP")
			{
				$("#fulfillmentMessage").css("display","block");
				$("#otpNUM").css("display","none");
				$("#otpSentMessage").css("display","none");
				// $("#no-click").remove();
				$(".terms.cod").remove();
				applyPromotion(null,"none","none");
			}
			else if(codEligible=="ITEMS_NOT_ELIGIBLE")
			{
				$("#codItemEligibilityMessage").css("display","block");
				$("#otpNUM").css("display","none");
				$("#otpSentMessage").css("display","none");
				// $("#no-click").remove();
				$(".terms.cod").remove();
				applyPromotion(null,"none","none");
			}
			else if(codEligible=="NOT_PINCODE_SERVICEABLE")
			{
				$("#codMessage").css("display","block");
				$("#otpNUM").css("display","none");
				$("#otpSentMessage").css("display","none");
				// $("#no-click").remove();
				$(".terms.cod").remove();
				applyPromotion(null,"none","none");
			}
			else{
				if(isCodSet == false){
				   	$.ajax({
						url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/setConvCharge",
						type: "GET",
						data: { 'paymentMode' : paymentMode , 'guid' : guid },
						cache: false,
						success : function(response) {
							if(response==null){
								$(location).attr('href',ACC.config.encodedContextPath+"/cart"); // TISEE-510
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
									document.getElementById("convChargeField").innerHTML="Free"; // TISPRD-146
									$("#convChargeMessage").css("display","none");
								}
								// document.getElementById("totalWithConvFields").innerHTML=totalPrice;
								// TISPT-29
								if(paymentMode!=null){
									applyPromotion(null,"none","none");
								}
								isCodSet = true;
							}
						},
						error : function(resp) {
							//alert("COD is not available at this time. Please select another payment mode and proceed");
							$("#paymentDetails").css("display","block");
							$("#otpSentMessage").css("display","none");
							$("#codErrorMessage").css("display","block");
							$("#no-click,.spinner").remove();
						}
					});
				}
				else{
					$("#convChargeFieldId, #convChargeField").css("display","block");
					$("#no-click,.spinner").remove();
				}
			}
		},
		error : function(resp) {
			//alert("COD is not available at this time. Please select another payment mode and proceed");	
			$("#paymentDetails").css("display","block");
			$("#otpSentMessage").css("display","none");
			$("#codErrorMessage").css("display","block");
			$("#no-click,.spinner").remove();
			// $(".make_payment").removeAttr('disabled');
		}
	});

	// $("#no-click").remove();
}




function displayDebitCardForm(){
	refresh();
	$("#paymentMode").val("Debit Card");
	$("#paymentModeValue").val("Debit Card");
	//resetConvCharge();	TISPT-29
	$('input:password').val("");
	$(".name_on_card").val("");	
	displayDCForm();
}



function displayDCForm(){
	$("#is_emi").val("false");
	//$("#card, #dcHeader, #savedCard, #savedDebitCard, .make_payment").show();
	//$("#ccHeader, #savedCreditCard, #billingAddress").hide();
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
	//if(document.getElementsByName("debitCards")[0]==undefined){
		//$("#savedCard, #savedCreditCard, #savedDebitCard, .newCard, .savedCard, .saved-card-button").css("display","none");
		$("#newCard, .newCardPayment").css("display","block");
		$(".make_payment_top_savedCard").css("display","none");
		$(".make_payment_top_newCard").css("display","block");
		$(".accepted-cards .maestro").parent().css("display","inline-block");
		$(".accepted-cards .visa").parent().css("display","inline-block");
		$(".accepted-cards .master").parent().css("display","inline-block");
		$(".accepted-cards .amex").parent().css("display","none");
		//applyPromotion(null,"none","none");
	//}	
	//else
	//{
		//$("#savedCreditCard, .savedCard, .newCardPayment, #newCard, .newCardPayment").css("display","none");
		//$("#savedDebitCard, .saved-card-button").css("display","block");
		$(".make_payment_top_savedCard").css("display","block");
		$(".make_payment_top_newCard").css("display","none");
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
		if(typeof bankName==="undefined")
		{
			bankName=null;
		}
		setBankForSavedCard(bankName);
		if($(".card_brand").val()=="MAESTRO"){
			$("#maestroMessage").css("display","block");
		}
		else{
			$("#maestroMessage").css("display","none");
		}
	//}
	
	// TISEE-5555
	$('.security_code_hide').prop('disabled', true);
	$('.security_code').prop('disabled', false); 
}




function displayCreditCardForm(){		
	refresh();
	$("#paymentMode").val("Credit Card");
	$("#paymentModeValue").val("Credit Card");
	$("#is_emi").val("false");
	$("#card, #ccHeader").css("display","block");
	$("#dcHeader").css("display","none");	
	// resetConvCharge(); TISPT-29
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
		//TPR-665
		if(typeof utag !="undefined"){
		utag.link({"link_text": "pay_cod_validate_otp" , "event_type" : "payment_mode_cod"});
		}
		
		if(otpNUMField=="")
		{
			//TPR-665
			if(typeof utag !="undefined"){
			utag.link({"link_text": "pay_cod_otp_error" , "event_type" : "payment_mode_cod"});
			}
			
			$("#otpNUM, #sendOTPNumber, #emptyOTPMessage, #otpSentMessage").css("display","block");
		}
		else
		{
			$("#otpNUM, #sendOTPNumber, #paymentFormButton, #sendOTPButton, #otpSentMessage").css("display","block");
			$("#emptyOTPMessage").css("display","none");
			$('#paymentButtonId').prop('disabled', true); //TISPRD-958
			var guid=$("#guid").val();
		$.ajax({
			url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/validateOTP/"+otpNUMField,
			type: "POST",
			data: {'guid' : guid},
			cache: false,		
			success : function(response) {
				if(response=='redirect'){
					$(location).attr('href',ACC.config.encodedContextPath+"/cart"); //TIS 404

				}
				//TPR-815
				else if(response=='redirect_to_payment'){
					$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/pay?value="+guid); //TPR-629
				}
				else{
					$("#emptyOTPMessage").css("display","none");
					if(response!=null)
					{
						if(response=="INVALID")
						{
							//TPR-665
							utag.link(
							{"link_text": "pay_cod_otp_error" , "event_type" : "payment_mode_cod"}
							);
							
							$("#otpNUM, #sendOTPNumber, #enterOTP, #wrongOtpValidationMessage").css("display","block");		
							$("#expiredOtpValidationMessage").css("display","none");
							$("#otpSentMessage").css("display","none");
							$('#paymentButtonId').prop('disabled', false); // TISPRD-958
						}
						else if(response=="EXPIRED")
						{
							//TPR-665
							if(typeof utag !="undefined"){
							utag.link({"link_text": "pay_cod_otp_timeout" , "event_type" : "payment_mode_cod"});
							}
							
							$("#otpNUM, #sendOTPNumber, #enterOTP, #expiredOtpValidationMessage").css("display","block");
							$("#wrongOtpValidationMessage").css("display","none");	
							$("#otpSentMessage").css("display","none");
							$('#paymentButtonId').prop('disabled', false); // TISPRD-958
						}
						else{
							//TPR-665
							if(typeof utag !="undefined"){
							utag.link({"link_text": "pay_cod_otp_success" , "event_type" : "payment_mode_cod"});
							}
							
							var staticHost=$('#staticHost').val();
							// TISPRO-153
							sendTealiumData();	
							$("#form-actions, #otpNUM").css("display","block");
							$("#wrongOtpValidationMessage, #expiredOtpValidationMessage").css("display","none");
							$("#otpSentMessage").css("display","none");
							$(".pay .payment-button,.cod_payment_button_top").prop("disabled",true);
							$(".pay .payment-button,.cod_payment_button_top").css("opacity","0.5");
							// store url change
							/*TPR-3446 COD After OTP Entered starts*/
							/*$(".pay").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: absolute; right: 23%;bottom: 100px; height: 30px;">');
							$(".pay .spinner").css("left",(($(".pay#paymentFormButton").width()+$(".pay#paymentFormButton .payment-button").width())/2)+10);
							$("body").append("<div id='no-click' style='opacity:0.65; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");*/
							$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
							$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
							/*TPR-3446 COD After OTP Entered ends*/
							$("#silentOrderPostForm").submit();
						}
					}
					else
					{
						//TPR-665
						if(typeof utag !="undefined"){
						utag.link({"link_text": "pay_cod_otp_error" , "event_type" : "payment_mode_cod"});
						}
						
						alert("Error validating OTP. Please select another payment mode and proceed");
						$(".pay button,.cod_payment_button_top").prop("disabled",false);
						$(".pay button,.cod_payment_button_top").css("opacity","1");
						$(".pay .spinner").remove();
						$("#no-click,.spinner").remove();
						$('#paymentButtonId').prop('disabled', false); // TISPRD-958
					}
				}
			},
			error : function(resp) {
				//TPR-665
				if(typeof utag !="undefined"){
				utag.link({link_text: 'pay_cod_otp_error' , event_type : 'payment_mode_cod'});
				}
				
				alert("Error validating OTP. Please select another payment mode and proceed");
				$(".pay button,.cod_payment_button_top").prop("disabled",false);
				$(".pay button,.cod_payment_button_top").css("opacity","1");
				$(".pay .spinner").remove();
				$("#no-click,.spinner").remove();
				
			}
		});
		}
	}
	else
		alert("Please make valid selection and proceed");
}



// TISEE-5555
// $("#savedDebitCard .debit-card-group .cvvValdiation").focus(function(){
// $("#savedDebitCard .debit-card-group").focus(function(){
// //TISBOX-1732
// $(".card_cvvErrorSavedCard, .card_cvvErrorSavedCard_hide,
// .card_ebsErrorSavedCard,
// .card_ebsErrorSavedCard_hide").css("display","none");
// $("input[name=debitCards]:radio.card_token,input[name=creditCards]:radio.card_token,input[name=emiCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
// $(this).parent().parent().find(".card").find(".radio").find(".card_token_hide").removeClass("card_token_hide").addClass("card_token");
// $(this).parent().parent().find(".card").find(".radio
// .debitCardsRadio").click();
// $(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
// $(".card_token_hide").parent().find('.card_bank').removeClass("card_bank").addClass("card_bank_hide");
// $(".card_token_hide").parent().find('.card_brand').removeClass("card_brand").addClass("card_brand_hide");
// $(".card_token_hide").parent().find('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
// $(".card_token_hide").parent().find('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
// $(".card_token_hide").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
// $(".card_token").parent().parent().parent().find(".cvv").find(".security_code_hide").removeClass("security_code_hide").addClass("security_code");
// $(".card_token").parent().find('.card_bank_hide').removeClass("card_bank_hide").addClass("card_bank");
// $(".card_token").parent().find('.card_brand_hide').removeClass("card_brand_hide").addClass("card_brand");
// $(".card_token").parent().find('.card_is_domestic_hide').removeClass("card_is_domestic_hide").addClass("card_is_domestic");
// $(".card_token").parent().find('.card_ebsErrorSavedCard_hide').removeClass("card_ebsErrorSavedCard_hide").addClass("card_ebsErrorSavedCard");
// $(".card_token").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard_hide').removeClass("card_cvvErrorSavedCard_hide").addClass("card_cvvErrorSavedCard");
// $(".security_code_hide").val(null);
// if($(".card_brand").val()=="MAESTRO"){
// $("#maestroMessage").css("display","block");
// }
// else{
// $("#maestroMessage").css("display","none");
// }
//	
// //TISEE-5555
// $('.security_code_hide').prop('disabled', true);
// $('.security_code').prop('disabled', false);
// });


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
	var bankName = $("#bankCodeSelection option:selected").html();
	$("#netbankingError").css("display","none");
	
	if(number!="select"){
	for (i=0; i<selection.length; i++)
		  if (selection[i].checked == true){
			  checkedValue=selection[i].value;
			  selection[i].checked = false;
		  }
	//TPR-665
	if(typeof utag !="undefined"){
	utag.link({"link_text": "net_banking_dropdown_"+bankName.replace(/ /g,'_').toLowerCase() , "event_type" : "payment_mode_dropdown"});
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
			  
		//TPR-665
			  if(typeof utag !="undefined"){
			utag.link({"link_text": "net_banking_popular_"+bankName.replace(/ /g,'_').toLowerCase() , "event_type" : "payment_mode_popular"});
			  }
	}
	// setBankForSavedCard(bankName);
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
		// Code to reset the values
		setBankForSavedCard(selectedBank);
	}
	else{
		hideTable();
		//$("#COD, #emi, #netbanking, #card, #paymentFormButton, #submitPaymentFormButton, #submitPaymentFormCODButton, #mobileNoError, #OTPGenerationErrorMessage, #codMessage, #customerBlackListMessage, #otpValidationMessage, #wrongOtpValidationMessage, #expiredOtpValidationMessage, #fulfillmentMessage, #codItemEligibilityMessage, #emptyOTPMessage, #resendOTPMessage, #savedEMICard").css("display","none");
		
		$("#card, #emi-notice").css("display","none");
		applyPromotion(null,"none","none");
	}
}



function hideTable(){
	$("#radioForEMI").css("display","none");
	$('#savedEMICard').find(".credit-card-group").remove();
	$('#EMITermTable tbody').empty();
	/*var emiTable=document.getElementById("EMITermTable");
	var rowCount = emiTable.rows.length;
	for(var i=rowCount-1; i>0; i--){
		emiTable.deleteRow(i);
		rowCount--;
	}*/
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
        	//$("#card").css("display","block");
        	$("#dcHeader").css("display","none");
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
			$("#cardEmi").css("display","block");
			$("#cardEmi").find(".product-block").css("display","block");
			$("#cardEmi").find(".product-block").find(".header").css("display","table");
			$("#ccHeader").css("display","block");
			$("#dcHeader").css("display","none");
			//$(".savedCard, .newCard").css("display","none");
			if(len=="0")
			{
				$("#savedEMICard").css("display","none");
				$("#billingAddressEmi, .make_payment").css("display","block");
				$(".newCard, .savedCard, .saved-card-button").css("display","none");
				$("#newCard, .newCardPayment, .accepted-cards").css("display","block");
				//$(".make_payment_top_savedCard").css("display","none");
				//$(".make_payment_top_newCard").css("display","block");
				$(".proceed-button").each(function(){
	    			$(this).hide();
	    		});
	    		//$("#make_emi_payment_up").show();
				$(".accepted-cards .maestro").parent().css("display","none");
				$(".accepted-cards .visa").parent().css("display","inline-block");
				$(".accepted-cards .master").parent().css("display","inline-block");
				$(".accepted-cards .amex").parent().css("display","inline-block");
				populateBillingAddressEmi();				
			}
			else{
				$("#savedCard, #savedEMICard,.saved-card-button, #make_saved_emi_payment").css("display","block");
				$(".newCard").css("display","table-cell");
				//$("#savedCreditCard, #savedDebitCard").css("display","none");
				$("#newCard, .newCardPayment").css("display","none");
				//$(".make_payment_top_savedCard").css("display","block");
				//$(".make_payment_top_newCard").css("display","none");
				$(".proceed-button").each(function(){
	    			$(this).hide();
	    		});
	    		//$("#make_emi_payment_up").show();
				var index=-1;
				var index1=0;
				$.each(myMap, function(i, val) {
					    index=index+1;
					    index1=index1+1;
					    $("#savedEMICard").css("display","block");
					    // alert($("#savedEMICard").find('#credit-card-group'+index1).find('.card').find('.radio').find('#ec1').val());
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
	// TISEE-5555
	$('.security_code_hide').prop('disabled', true);
	$('.security_code').prop('disabled', false); 
}


function displayFormForCC(){
	//$("#savedCard, #savedCreditCard, #billingAddress, .make_payment").show();
	//$("#savedDebitCard").hide();
	$("input[name=debitCards]:radio.card_token,input[name=creditCards]:radio.card_token,input[name=emiCards]:radio.card_token").removeClass("card_token").addClass("card_token_hide");
	$("input[name=creditCards]:radio").first().removeClass("card_token_hide").addClass("card_token");
	$(".card_token_hide").parent().parent().parent().find(".cvv").find(".security_code").removeClass("security_code").addClass("security_code_hide");
	$(".card_token_hide").parent().find('.card_bank').removeClass("card_bank").addClass("card_bank_hide"); 
	$(".card_token_hide").parent().find('.card_brand').removeClass("card_brand").addClass("card_brand_hide");
	$(".card_token_hide").parent().find('.card_is_domestic').removeClass("card_is_domestic").addClass("card_is_domestic_hide");
	$(".card_token_hide").parent().find('.card_ebsErrorSavedCard').removeClass("card_ebsErrorSavedCard").addClass("card_ebsErrorSavedCard_hide");
	$(".card_token_hide").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard').removeClass("card_cvvErrorSavedCard").addClass("card_cvvErrorSavedCard_hide");
	
	//if(document.getElementsByName("creditCards")[0]==undefined){
		//$("#savedCard, #savedCreditCard, #savedDebitCard, .newCard, .savedCard, .saved-card-button").css("display","none");
		//$(".make_payment_top_savedCard").css("display","none");
		$("#newCard, .newCardPayment").css("display","block");
		//$(".make_payment_top_newCard").css("display","block");
		$('#make_cc_payment_up').show();
		$(".accepted-cards .maestro").parent().css("display","none");
		$(".accepted-cards .visa").parent().css("display","inline-block");
		$(".accepted-cards .master").parent().css("display","inline-block");
		$(".accepted-cards .amex").parent().css("display","inline-block");
		populateBillingAddress();
//		if($("#paymentMode").val()!='EMI'){
//			applyPromotion(null,"none","none");
//		}
		
	//}
	//else
	//{
		if($("#paymentMode").val()!='EMI'){
			$(".card_token").parent().parent().parent().find(".cvv").find(".security_code_hide").removeClass("security_code_hide").addClass("security_code");
			$(".card_token").parent().find('.card_bank_hide').removeClass("card_bank_hide").addClass("card_bank"); 
			$(".card_token").parent().find('.card_brand_hide').removeClass("card_brand_hide").addClass("card_brand");
			$(".card_token").parent().find('.card_is_domestic_hide').removeClass("card_is_domestic_hide").addClass("card_is_domestic");
			$(".card_token").parent().find('.card_ebsErrorSavedCard_hide').removeClass("card_ebsErrorSavedCard_hide").addClass("card_ebsErrorSavedCard");
			$(".card_token").parent().parent().parent().find(".cvv").find('.card_cvvErrorSavedCard_hide').removeClass("card_cvvErrorSavedCard_hide").addClass("card_cvvErrorSavedCard");
			//$("#savedCreditCard, .saved-card-button").css("display","block");
			$(".make_payment_top_savedCard").css("display","block");
			//$("#savedDebitCard, #newCard, .savedCard, .newCardPayment").css("display","none");
			$(".make_payment_top_newCard").css("display","none");
			$(".newCard").css("display","table-cell");
			$("input[name=debitCards]:radio").first().prop("checked", false);
			$("input[name=creditCards]:radio").first().prop("checked", true);
			$("#card li.header ul").append($("#emi li.newCard"));
			$("#card li.header ul").append($("#emi li.savedCard"));
			var bankName=$('.card_bank').val();
			if(typeof bankName==="undefined")
			{
				bankName=null;
			}
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
	//}
	//TISEE-5555
	$('.security_code_hide').prop('disabled', true);
	$('.security_code').prop('disabled', false); 
}
  

function mobileBlacklist(){
	var number=$("#otpMobileNUMField").val();
	var mobileNumber=number;
	$("#otpNUMField").val("");
	$("#wrongOtpValidationMessage, #expiredOtpValidationMessage").css("display","none");
	if(number.length!=10){
		$("#mobileNoError").css("display","block");
		$("#resendOTPMessage, #enterOTP, #otpSentMessage, #paymentFormButton").css("display","none");
	}
	else if(number.charAt(0)=='0'){
		$("#mobileNoError").css("display","block");
		//	$("#sendOTPButton .spinner").remove();
	}
// Check if the session is active before generating OTP
	else if(isSessionActive()){
	$("#mobileNoError").css("display","none");	
	// store url change
	var staticHost=$('#staticHost').val();
	
	/*TPR-3446 COD starts*/
	//$("#sendOTPButton").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: absolute; right: 10%;bottom: 0px; height: 30px;">');
	$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
	/*TPR-3446 COD ends*/
	
	if($("#sendOTPButton #resendOTPMessage").css("display") == 'block') {
		$("#sendOTPButton .spinner").css("bottom","33px")
	}
	var number=$("#otpMobileNUMField").val();
	//alert(number);
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
				$("#no-click,.spinner").remove();
			}
		},
		error : function(resp) {
			$("#sendOTPButton .spinner").remove();
			$("#no-click,.spinner").remove();
		}
	});
	}
	else{
		redirectToCheckoutLogin();
	}
}



function generateOTP(){
	$("#submitButtons").css("display","none");
	var number=$("#otpMobileNUMField").val();
	// var prefixBefore=$("#mobilePrefix").val();
	// var prefix=prefixBefore.replace(/\D/g,'');
	var mobileNumber=number;
	$("#otpNUMField").val("");
	$("#wrongOtpValidationMessage, #expiredOtpValidationMessage").css("display","none");
	if(number.length!=10){
		$("#mobileNoError").css("display","block");
		$("#resendOTPMessage, #enterOTP, #otpSentMessage, #paymentFormButton").css("display","none");
	}
	else if(number.charAt(0)=='0'){
		$("#mobileNoError").css("display","block");
		//	$("#sendOTPButton .spinner").remove();
	}
	else{
		var guid=$("#guid").val();
		//TPR-665
		if(typeof utag !="undefined"){
		utag.link({'link_text': 'pay_cod_verify_number', 'event_type': 'payment_mode_cod'});
		}
	$.ajax({
		url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/generateOTP",
		// data: { 'mobileNumber' : mobileNumber, 'prefix' : prefix },

		data: { 'mobileNumber' : mobileNumber , 'guid' : guid},
		type: "POST",
		cache: false,	
		success : function(response) {
			if(response=='redirect'){
				$(location).attr('href',ACC.config.encodedContextPath+"/cart"); //TIS 404
			}
			else if(response=='redirect_to_payment'){
				$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/pay?value="+guid); //TPR-629
			}
			else if(response=="fail")
			{
				$("#OTPGenerationErrorMessage").css("display","block");
			}
			else{
				$("#codMessage").css("display","none");
				$("#otpNUM, #otpSentMessage, #sendOTPNumber, #enterOTP, #paymentFormButton, #submitPaymentFormCODButton, .make_payment, #sendOTPButton, #resendOTPMessage, .cod_payment_button_top").css("display","block");
				$(".totals.outstanding-totalss").css("bottom","40px");
				//$(".checkout-content.checkout-payment .left-block").css("margin-top","-48px");
				
			}
			
			$("#sendOTPButton .spinner").remove();
			$("#no-click,.spinner").remove();
		},
		error : function(resp) {
			$("#sendOTPButton .spinner").remove();
			$("#no-click,.spinner").remove();
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
 				applyPromotion(null,"none","none");
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
	     // TISBOX-1732
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
	   // TISEE-5555
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
	     // TISBOX-1732
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
	   // TISEE-5555
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
  	// TISEE-395
	$(document).on("change","input[name=emiCards]:radio",function(e){
	     var $this = $(this)
	     var cg = $this.parents("div.credit-card-group:first")
	     // TISBOX-1732
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
	   // TISEE-5555
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
	  	var staticHost=$('#staticHost').val();
		$(".pay button, #make_saved_cc_payment_up").prop("disabled",true);
		$(".pay button, #make_saved_cc_payment_up").css("opacity","0.5");
		// store url change
		
		/*TPR-3446 new starts*/
		/*$(".pay").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: absolute; right: 23%;bottom: 92px; height: 30px;">');
		$(".pay .spinner").css("left",(($(".pay.saved-card-button").width()+$(".pay.saved-card-button button").width())/2)+10);
		$("body").append("<div id='no-click' style='opacity:0.65; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");*/
		var staticHost = $('#staticHost').val();
		$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
		$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
		
		/*TPR-3446 new ends*/
	  // TISPRO-153
		sendTealiumData();
		var firstName=lastName=addressLine1=addressLine2=addressLine3=country=state=city=pincode=null;
		var cardSaved=sameAsShipping=false;
		var guid=$("#guid").val();
        //TISPRO-313	
		//if($(".redirect").val()=="false"){
			//Juspay.startSecondFactor();
		//}
		$.ajax({
			url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/createJuspayOrder",
			data: { 'firstName' : firstName , 'lastName' : lastName , 'addressLine1' : addressLine1, 'addressLine2' : addressLine2 , 'addressLine3' : addressLine3, 'country' : country , 'state' : state, 'city' : city , 'pincode' : pincode, 'cardSaved' : cardSaved, 'sameAsShipping' : sameAsShipping , 'guid' : guid},
			type: "GET",
			cache: false,
			async: false,
			success : function(response) {
				if(response=='redirect'){
//					if($(".redirect").val()=="false"){
//						Juspay.stopSecondFactor();
//					}
					$(location).attr('href',ACC.config.encodedContextPath+"/cart"); //TIS 404
				}
				else if(response=='redirect_to_payment'){
					$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/pay?value="+guid); //TPR-629
				}
				else if(response=="" || response==null || response=="JUSPAY_CONN_ERROR"){
//					if($(".redirect").val()=="false"){
//						Juspay.stopSecondFactor();
//					}
					document.getElementById("juspayErrorMsg").innerHTML="Sorry! The system is down, please try again";
					$("#juspayconnErrorDiv").css("display","block");
					$(".pay button, #make_saved_cc_payment_up").prop("disabled",false);
					$(".pay button, #make_saved_cc_payment_up").css("opacity","1");
					$(".pay .spinner").remove();
					$("#no-click,.spinner").remove();
					//$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/add");
				}
				else if(response=='redirect_with_details'){
					$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/cardPayment/"+guid); //TPR-629
				}
				else{
					//TISSTRT-1391
					window.sessionStorage.removeItem("header");
					 //TISPRO-313
					if($(".redirect").val()=="false"){
						Juspay.startSecondFactor();

				    } 
					setTimeout(function(){ 	
						var values=response.split("|"); 
						$("#order_id_saved").val(values[0]);
						
						//$("#order_id_saved").val(response);
						var baseUrl=window.location.origin;
						var website = ACC.config.encodedContextPath;
						var thank_you_page = /*(website.indexOf("https") > -1 ? "" : "https://") +*/ baseUrl+website + "/checkout/multi/payment-method/cardPayment";

						var error_page = /*(website.indexOf("https") > -1 ? "" : "https://") +*/ baseUrl+website + "/checkout/multi/payment-method/cardPayment";
						Juspay.Setup({
							payment_form: "#card_form",
							success_handler: function(status, statusObj) {
								// redirect to success page
								var p = "order_id=" + statusObj.orderId
								p = p + "&status=" + statusObj.status 
								p = p + "&status_id=" + statusObj.statusId
								window.location.href = thank_you_page+"/"+values[1]

							},
							error_handler: function(error_code, error_message, bank_error_code, bank_error_message, gateway_id) {
								//redirect to failure page
								//alert("Transaction not successful. Error: " + bank_error_message)
								window.location.href = error_page+"/"+values[1]
							},
							second_factor_window_closed_handler: function() {
							    // enable the pay button for the user
								window.location.href = error_page+"/"+values[1]
							}
						})
						$("#card_form").submit(); 		
						
					 }, 1000);
				}
			},
			error : function(resp) {
				if($(".redirect").val()=="false"){
					Juspay.stopSecondFactor();
				}
				$(".pay button, #make_saved_cc_payment_up").prop("disabled",false);
				$(".pay button, #make_saved_cc_payment_up").css("opacity","1");
				$(".pay .spinner").remove();
				$("#no-click,.spinner").remove();
			}
		});		
	}
  
  function createJuspayOrderForSavedDebitCard(){
	  
	  	var staticHost=$('#staticHost').val();
		$(".pay button, #make_saved_dc_payment_up").prop("disabled",true);
		$(".pay button, #make_saved_dc_payment_up").css("opacity","0.5");
		//store url change
		/*TPR-3446 new starts*/
		var staticHost = $('#staticHost').val();
		/*$(".pay").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: absolute; right: 23%;bottom: 92px; height: 30px;">');
		$(".pay .spinner").css("left",(($(".pay.saved-card-button").width()+$(".pay.saved-card-button button").width())/2)+10);
		$("body").append("<div id='no-click' style='opacity:0.65; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");*/
		$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
		$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
		/*TPR-3446 new ends*/
		// TISPRO-153		
		sendTealiumData();

		var firstName=lastName=addressLine1=addressLine2=addressLine3=country=state=city=pincode=null;
		var cardSaved=sameAsShipping=false;
      //TISPRO-313	
		//if($(".redirect").val()=="false"){
			//Juspay.startSecondFactor();

		//}
		var guid=$("#guid").val();
		$.ajax({
			url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/createJuspayOrder",
			data: { 'firstName' : firstName , 'lastName' : lastName , 'addressLine1' : addressLine1, 'addressLine2' : addressLine2 , 'addressLine3' : addressLine3, 'country' : country , 'state' : state, 'city' : city , 'pincode' : pincode, 'cardSaved' : cardSaved, 'sameAsShipping' : sameAsShipping, 'guid' : guid},
			type: "GET",
			cache: false,
			async: false,
			success : function(response) {
				if(response=='redirect'){
//					if($(".redirect").val()=="false"){
//						Juspay.stopSecondFactor();
//					}
					$(location).attr('href',ACC.config.encodedContextPath+"/cart"); //TIS 404
				}else if(response=='redirect_to_payment'){
					$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/pay?value="+guid); //TPR-629
				}else if(response=='redirect_with_details'){
					$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/cardPayment/"+guid); //TPR-629
				}
				else if(response=="" || response==null || response=="JUSPAY_CONN_ERROR"){
//					if($(".redirect").val()=="false"){
//						Juspay.stopSecondFactor();
//					}
					document.getElementById("juspayErrorMsg").innerHTML="Sorry! The system is down, please try again";
					$("#juspayconnErrorDiv").css("display","block");
					$(".pay button, #make_saved_dc_payment_up").prop("disabled",false);
					$(".pay button, #make_saved_dc_payment_up").css("opacity","1");
					$(".pay .spinner").remove();
					$("#no-click,.spinner").remove();
					//$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/add");
				}else{

					 //TISPRO-313
					if($(".redirect").val()=="false"){
						Juspay.startSecondFactor();

				    } 
					setTimeout(function(){ 	
						var values=response.split("|"); 
						$("#order_id_saved_dc").val(values[0]);
						var baseUrl=window.location.origin;
						var website = ACC.config.encodedContextPath;
						var thank_you_page = /*(website.indexOf("https") > -1 ? "" : "https://") +*/ baseUrl+website + "/checkout/multi/payment-method/cardPayment";
						var error_page = /*(website.indexOf("https") > -1 ? "" : "https://") +*/ baseUrl+website + "/checkout/multi/payment-method/cardPayment";
						Juspay.Setup({
							payment_form: "#card_form_saved_debit",
							success_handler: function(status, statusObj) {
								//redirect to success page
								var p = "order_id=" + statusObj.orderId
								p = p + "&status=" + statusObj.status 
								p = p + "&status_id=" + statusObj.statusId
								window.location.href = thank_you_page+"/"+values[1]

							},
							error_handler: function(error_code, error_message, bank_error_code, bank_error_message, gateway_id) {
								//redirect to failure page
								//alert("Transaction not successful. Error: " + bank_error_message)
								window.location.href = error_page+"/"+values[1]

							},
							second_factor_window_closed_handler: function() {
							    // enable the pay button for the user
								window.location.href = error_page+"/"+values[1]
							}
						})
						$("#card_form_saved_debit").submit(); 		
						
					 }, 1000);
				}
			},
			error : function(resp) {
				if($(".redirect").val()=="false"){
					Juspay.stopSecondFactor();
				}
				$(".pay button, #make_saved_cc_payment_up").prop("disabled",false);
				$(".pay button, #make_saved_cc_payment_up").css("opacity","1");
				$(".pay .spinner").remove();
				$("#no-click,.spinner").remove();
			}
		});		
	}
  
  
  function createJuspayOrderForNewCard(isDebit){
	    var staticHost=$('#staticHost').val();  
	   isNewCard = true;// //this is variable to fix paynow blackout issue
		$(".pay button, #make_cc_payment_up").prop("disabled",true);
		$(".pay button, #make_cc_payment_up").css("opacity","0.5");
		// store url change
		var staticHost = $('#staticHost').val();
		/*TPR-3446 credit card starts*/
		/*$(".pay").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: absolute; right: 23%;bottom: 92px; height: 30px;">');
		$(".pay .spinner").css("left",(($(".pay.newCardPayment").width()+$(".pay.newCardPayment button").width())/2)+10);
		$("body").append("<div id='no-click' style='opacity:0.65; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");*/
		
		$("body").append("<div id='no-click1' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
		$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner1" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
		console.log(1);
		/*TPR-3446 credit card ends*/
		
		// TISPRO-153
		sendTealiumData();
		var firstName=$("#firstName").val();
		var lastName=$("#lastName").val();
		var addressLine1=encodeURIComponent($("#address1").val());
		var addressLine2=encodeURIComponent($("#address2").val());
		var addressLine3=encodeURIComponent($("#address3").val());
		var country=$("#country").val();
		var state=$("#state").val();
		var city=$("#city").val();
		var pincode=$("#pincode").val();
		
		if(isDebit && $('#save-card-dc').is(":checked")){
			var cardSaved = true;
		}else if ($('#save-card').is(":checked"))
		{
			var cardSaved = true;
		}else {
			var cardSaved = false;
		}
		if ($('#sameAsShipping').is(":checked"))
		{
			var sameAsShipping = true;
		}
		else {
			var sameAsShipping = false;
		}
		var guid=$("#guid").val();
	    //TISPRO-313
		//if($(".redirect").val()=="false"){
			//Juspay.startSecondFactor();
		//}
		// For Payement Page CR Changes starts
		
		// For Payement Page CR Changes ends
		
		$.ajax({
			url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/createJuspayOrder",
			data: { 'firstName' : firstName , 'lastName' : lastName , 'addressLine1' : addressLine1, 'addressLine2' : addressLine2 , 'addressLine3' : addressLine3, 'country' : country , 'state' : state, 'city' : city , 'pincode' : pincode, 'cardSaved' : cardSaved, 'sameAsShipping' : sameAsShipping, 'guid' : guid},
			type: "GET",
			cache: false,
			async: false,
			success : function(response) {	
				if(response=='redirect'){
//					if($(".redirect").val()=="false"){
//						Juspay.stopSecondFactor();
//					}
					$(location).attr('href',ACC.config.encodedContextPath+"/cart"); //TIS 404
				}else if(response=='redirect_to_payment'){
					$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/pay?value="+guid); //TPR-629
				}
				else if(response=='redirect_with_details'){
					$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/cardPayment/"+guid); //TPR-629
				}
				else if(response=="" || response==null || response=="JUSPAY_CONN_ERROR"){	
//					if($(".redirect").val()=="false"){
//						Juspay.stopSecondFactor();
//					}
					document.getElementById("juspayErrorMsg").innerHTML="Sorry! The system is down, please try again";
					$("#juspayconnErrorDiv").css("display","block");
					$(".pay button, #make_cc_payment_up").prop("disabled",false);
					$(".pay button, #make_cc_payment_up").css("opacity","1");
					$(".pay .spinner").remove();
					$("#no-click,.spinner").remove();
					//$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/add");
				}else{	
					//TISSTRT-1391
					window.sessionStorage.removeItem("header");
					 //TISPRO-313
					 if($(".redirect").val()=="false"){
						Juspay.startSecondFactor();

				     } 		 
					 setTimeout(function(){ 			 
						 var values=response.split("|"); 
						  if(typeof(isDebit)!= 'undefined' && isDebit ){
							  $("#order_id_new_dc").val(values[0]);
							  submitDebitCardForm(values[1]);	  
						  }else{
							  $("#order_id_new").val(values[0]);
							  submitCardForm(values[1]);	
						  }
						  	 
					 }, 1000);
			
				}
				$("#no-click,.spinner").remove();
			},
			error : function(resp) {
				if($(".redirect").val()=="false"){
					Juspay.stopSecondFactor();
				}
				$(".pay button, #make_cc_payment_up").prop("disabled",false);
				$(".pay button, #make_cc_payment_up").css("opacity","1");
				$(".pay .spinner").remove();
				$("#no-click,.spinner").remove();
			}
		});		
	}
  
  function createJuspayOrderForNewCardEmi(){
	    var staticHost=$('#staticHost').val();  
	   isNewCard = true;////this is variable to fix paynow blackout issue
		$(".pay button, #make_emi_payment_up").prop("disabled",true);
		$(".pay button, #make_emi_payment_up").css("opacity","0.5");
		//store url change
		/*TPR-3446 new starts*/
		var staticHost = $('#staticHost').val();
		/*$(".pay").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: absolute; right: 23%;bottom: 92px; height: 30px;">');
		$(".pay .spinner").css("left",(($(".pay.newCardPayment").width()+$(".pay.newCardPayment button").width())/2)+10);
		$("body").append("<div id='no-click' style='opacity:0.65; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");*/
		$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
		$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
		/*TPR-3446 new ends*/
		// TISPRO-153
		sendTealiumData();
		var firstName = $("#firstNameEmi").val();
		var lastName = $("#lastNameEmi").val();
		var addressLine1=encodeURIComponent($("#address1Emi").val());
		var addressLine2=encodeURIComponent($("#address2Emi").val());
		var addressLine3=encodeURIComponent($("#address3Emi").val());
		var country = $("#countryEmi").val();
		var state = $("#stateEmi").val();
		var city = $("#cityEmi").val();
		var pincode = $("#pincodeEmi").val();
		
		if($('#save-card-emi').is(":checked")){
			var cardSaved = true;
		}
		if ($('#sameAsShippingEmi').is(":checked"))
		{
			var sameAsShipping = true;
		}
		else {
			var sameAsShipping = false;
		}
		var guid=$("#guid").val();	//TPR-629
	    //TISPRO-313
		//if($(".redirect").val()=="false"){
			//Juspay.startSecondFactor();
		//}
		// For Payement Page CR Changes starts
		
		// For Payement Page CR Changes ends
		
		$.ajax({
			url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/createJuspayOrder",
			data: { 'firstName' : firstName , 'lastName' : lastName , 'addressLine1' : addressLine1, 'addressLine2' : addressLine2 , 'addressLine3' : addressLine3, 'country' : country , 'state' : state, 'city' : city , 'pincode' : pincode, 'cardSaved' : cardSaved, 'sameAsShipping' : sameAsShipping, 'guid' : guid},
			type: "GET",
			cache: false,
			async: false,
			success : function(response) {	
				if(response=='redirect'){
					$(location).attr('href',ACC.config.encodedContextPath+"/cart"); //TIS 404
				}else if(response=='redirect_to_payment'){
					$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/pay?value="+guid); //TPR-629
				}
				else if(response=='redirect_with_details'){
					$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/cardPayment/"+guid); //TPR-629
				}
				else if(response=="" || response==null || response=="JUSPAY_CONN_ERROR"){	
					document.getElementById("juspayErrorMsg").innerHTML="Sorry! The system is down, please try again";
					$("#juspayconnErrorDiv").css("display","block");
					$(".pay button, #make_emi_payment_up").prop("disabled",false);
					$(".pay button, #make_emi_payment_up").css("opacity","1");
					$(".pay .spinner").remove();
					$("#no-click,.spinner").remove();
					//$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/add");
				}else{		
					 //TISPRO-313
					 if($(".redirect").val()=="false"){
						Juspay.startSecondFactor();

				     } 		 
					 setTimeout(function(){ 	
						 	var values=response.split("|"); 
							  $("#order_id_new_emi").val(values[0]);
							  submitEmiCardForm(values[1]);	  
					 }, 1000);
			
				}
				//$("#no-click").remove();
			},
			error : function(resp) {
				if($(".redirect").val()=="false"){
					Juspay.stopSecondFactor();
				}
				$(".pay button, #make_cc_payment_up").prop("disabled",false);
				$(".pay button, #make_cc_payment_up").css("opacity","1");
				$(".pay .spinner").remove();
				$("#no-click,.spinner").remove();
			}
		});		
	}
  
 
  

 $("#make_saved_cc_payment,#make_saved_cc_payment_up").click(function(){
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
 });
 
 $("#make_saved_dc_payment,#make_saved_dc_payment_up").click(function(){
	 
		var password = $(".card_token").parent().parent().parent().find(".cvv").find(".cvvValdiation").val();
		var ebsDownCheck=$("#ebsDownCheck").val();
		var isDomestic=$(".card_token").parent().parent().parent().find('.card').find('.radio').find('.card_is_domestic').val();
		
		if (password.length < 3 && 	$(".card_brand").val()!="MAESTRO"){
			$(".card_cvvErrorSavedCard").css("display","block");		
			return false;
		}
	 	else if($(".card_brand").val()=="MAESTRO" && password==""){
	 		createJuspayOrderForSavedDebitCard(); 
	 	}
		else if(ebsDownCheck=="Y" && (isDomestic=="false" || isDomestic==""))
		{
			$(".card_ebsErrorSavedCard").css("display","block");		
			return false;
		}
		else{
			createJuspayOrderForSavedDebitCard(); 
		}
	 })
 
 
 $(".security_code").focus(function(){
	 $("#savedCVVError").css("display","none");
	 $(".card_ebsErrorSavedCard, .card_cvvErrorSavedCard").css("display","none");	
	 document.getElementById("cvvError").innerHTML="";
	 
 })
  $("#make_cc_payment, #make_cc_payment_up").click(function(){
	  if(isSessionActive()==false){
			 redirectToCheckoutLogin();
			}
			else{
		var bin_current_status = getCardBinstatus();
		//dopayment(bin_current_status);
			}
  });
 
 $("#make_dc_payment,#make_dc_payment_up").click(function(){
	  if(isSessionActive()==false){
			 redirectToCheckoutLogin();
			}
			else{
		var bin_current_status = getCardBinstatusDc();
		//dopaymentDc(bin_current_status);
			}
 }); 
 
 $(document).on('click','#make_emi_payment,#make_emi_payment_up',function(){
	  if(isSessionActive()==false){
			 redirectToCheckoutLogin();
			}
			else{
		var bin_current_status = getCardBinstatusEmiCc();
		//dopaymentEmi(bin_current_status);
			}
});
  
  function getCardBinstatus(){
	 var status = validateCardNo("formSubmit");
	 return status;
  }
 
  function getCardBinstatusDc(){
		 var status = validateDebitCardNo("formSubmit");
		 return status;
	  }
  
  function getCardBinstatusEmiCc(){
		 var status = validateEmiCardNo("formSubmit");
		 return status;
	  }


 function dopayment(bin_current_status){
	 if($("#paymentMode").val()=="Credit Card"){
		 var name = validateName();
		 if(bin_current_status==true){
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
		 var addressLine2=$("#address2").val();
		 var addressLine3=$("#address3").val();
		 var pin = validatePin();
		 var city=validateCity();
		 var state=validateState();
		 var cardType=$("#cardType").val();
		 if(cardType=="MAESTRO"){
			 //if (name && cardNo && pin && firstName && lastName && addressLine1 && addressLine2 && addressLine3 && city && state){
			if (name && cardNo && pin && firstName && lastName && addressLine1 && city && state){
				 createJuspayOrderForNewCard(false);
			 }
			 else{
				 return false;
			 }
		 }
		 else{
			 var cvv = validateCVV();
			 var expMM = validateExpMM();
			 var expYY = validateExpYY();
			// if (cvv && expYY && name && expMM && cardNo && pin && firstName && lastName && addressLine1 && addressLine2 && addressLine3 && city && state){
			 if (cvv && expYY && name && expMM && cardNo && pin && firstName && lastName && addressLine1 && city && state){
				 createJuspayOrderForNewCard(false);
			 }
			 else{
				 return false;
			 }
		 }
				 
	 }
	 else if($("#paymentMode").val()=="Debit Card"){
		 var name = validateNameDc();
		 if(bin_current_status==true){
			 var cardNo=true;
		 }
		 else{
			 if(document.getElementById("cardNoErrorDc").innerHTML.length==0){
				 document.getElementById("cardNoErrorDc").innerHTML="Please enter a valid card number ";
			 }
			 var cardNo = false;
		 }
		 var cardType = $("#cardTypeDc").val();
		 
		 if(cardType=='MAESTRO'){
			 if (name && cardNo){
				 createJuspayOrderForNewCard(true);		 
			 }
			 
			 else{
				 return false;
			 }
		 }
		 else{
			 var cvv = validateCVVDc();
			 var expMM = validateExpMMDc();
			 var expYY = validateExpYYDc();
			 if (cvv && expYY && name && expMM && cardNo){
				 createJuspayOrderForNewCard(true);		 
			 }
			 else{
				 return false;
			 }
		 }
	 }
	 else if($("#paymentMode").val()=="EMI"){
		 var name = validateNameEmi();
		 if(bin_current_status==true){
			 var cardNo=true;
		 }
		 else{
			 if(document.getElementById("cardNoErrorEmi").innerHTML.length==0){
				 document.getElementById("cardNoErrorEmi").innerHTML="Please enter a valid card number ";
			 }
			 var cardNo = false;
		 }
		 var firstName = validateNameOnAddress($("#firstNameEmi").val(), document.getElementById("firstNameErrorEmi"), "firstNameEmi");
		 var lastName = validateNameOnAddress($("#lastNameEmi").val(), document.getElementById("lastNameErrorEmi"), "lastNameEmi");
		 var addressLine1 = validateAddressLine1($("#address1Emi").val(), document.getElementById("address1ErrorEmi"));
		 var addressLine2 = $("#address2Emi").val();
		 var addressLine3 = $("#address3Emi").val();
		 var pin = validatePinEmi();
		 var city = validateCityEmi();
		 var state = validateStateEmi();
		 
		 var cardType = $("#cardTypeEmi").val();
		 
		 if(cardType=="MAESTRO"){
			 //if (name && cardNo && pin && firstName && lastName && addressLine1 && addressLine2 && addressLine3 && city && state){
			if (name && cardNo && pin && firstName && lastName && addressLine1 && city && state){
				createJuspayOrderForNewCardEmi();
			 }
			 else{
				 return false;
			 }
		 }
		 else{
			 var cvv = validateCVVEmi();
			 var expMM = validateExpMMEmi();
			 var expYY = validateExpYYEmi();
			// if (cvv && expYY && name && expMM && cardNo && pin && firstName && lastName && addressLine1 && addressLine2 && addressLine3 && city && state){
			 if (cvv && expYY && name && expMM && cardNo && pin && firstName && lastName && addressLine1 && city && state){
				 createJuspayOrderForNewCardEmi();
			 }
			 else{
				 return false;
			 }
		 }
	 }
 }
 

// function dopaymentDc(bin_current_status){
//	 var name = validateNameDc();
//	 if(bin_current_status==true){
//		 var cardNo=true;
//	 }
//	 else{
//		 if(document.getElementById("cardNoErrorDc").innerHTML.length==0){
//			 document.getElementById("cardNoErrorDc").innerHTML="Please enter a valid card number ";
//		 }
//		 var cardNo = false;
//	 }
//	 var firstName = validateNameOnAddress($("#firstName").val(), document.getElementById("firstNameError"), "firstName");
//	 var lastName = validateNameOnAddress($("#lastName").val(), document.getElementById("lastNameError"), "lastName");
//	 var addressLine1 = validateAddressLine1($("#address1").val(), document.getElementById("address1Error"));
//	 //var addressLine2=validateAddressLine2($("#address2").val(), document.getElementById("address2Error"));
//	 //var addressLine3=validateLandmark($("#address3").val(), document.getElementById("address3Error"));
//	 var addressLine2 = $("#address2").val();
//	 var addressLine3 = $("#address3").val();
//	 var pin = validatePin();
//	 var city = validateCity();
//	 var state = validateState();
//	 var cardType = $("#cardTypeDc").val();
//	 
//	 if($("#paymentMode").val()=="Debit Card"){
//		 if(cardType=='MAESTRO'){
//			 if (name && cardNo){
//				 createJuspayOrderForNewCard();		 
//			 }
//			 
//			 else{
//				 return false;
//			 }
//		 }
//		 else{
//			 var cvv = validateCVVDc();
//			 var expMM = validateExpMMDc();
//			 var expYY = validateExpYYDc();
//			 var isDebit = true;
//			 if (cvv && expYY && name && expMM && cardNo){
//				 createJuspayOrderForNewCard(isDebit);		 
//			 }
//			 else{
//				 return false;
//			 }
//		 }
//	 }
// }
// 
// function dopaymentEmi(bin_current_status){
//	 var name = validateNameEmi();
//	 if(bin_current_status==true){
//		 var cardNo=true;
//	 }
//	 else{
//		 if(document.getElementById("cardNoErrorEmi").innerHTML.length==0){
//			 document.getElementById("cardNoErrorEmi").innerHTML="Please enter a valid card number ";
//		 }
//		 var cardNo = false;
//	 }
//	 var firstName = validateNameOnAddress($("#firstNameEmi").val(), document.getElementById("firstNameErrorEmi"), "firstNameEmi");
//	 var lastName = validateNameOnAddress($("#lastNameEmi").val(), document.getElementById("lastNameErrorEmi"), "lastNameEmi");
//	 var addressLine1 = validateAddressLine1($("#address1Emi").val(), document.getElementById("address1ErrorEmi"));
//	 var addressLine2 = $("#address2Emi").val();
//	 var addressLine3 = $("#address3Emi").val();
//	 var pin = validatePinEmi();
//	 var city = validateCityEmi();
//	 var state = validateStateEmi();
//	 
//	 var cardType = $("#cardTypeEmi").val();
//	 
//	 if($("#paymentMode").val()=="Credit Card" || $("#paymentMode").val()=="EMI"){
//		 if(cardType=="MAESTRO"){
//			 //if (name && cardNo && pin && firstName && lastName && addressLine1 && addressLine2 && addressLine3 && city && state){
//			if (name && cardNo && pin && firstName && lastName && addressLine1 && city && state){
//				createJuspayOrderForNewCardEmi();
//			 }
//			 else{
//				 return false;
//			 }
//		 }
//		 else{
//			 var cvv = validateCVVEmi();
//			 var expMM = validateExpMMEmi();
//			 var expYY = validateExpYYEmi();
//			// if (cvv && expYY && name && expMM && cardNo && pin && firstName && lastName && addressLine1 && addressLine2 && addressLine3 && city && state){
//			 if (cvv && expYY && name && expMM && cardNo && pin && firstName && lastName && addressLine1 && city && state){
//				 createJuspayOrderForNewCardEmi();
//			 }
//			 else{
//				 return false;
//			 }
//		 }
//			 
//	 }
// }

 function submitCardForm(value){
	 var baseUrl=window.location.origin;
		var website = ACC.config.encodedContextPath;
		 var thank_you_page = /*
								 * (website.indexOf("https") > -1 ? "" :
								 * "https://") +
								 */baseUrl+ website + "/checkout/multi/payment-method/cardPayment";
		 var error_page = /* (website.indexOf("https") > -1 ? "" : "https://") + */ baseUrl+website + "/checkout/multi/payment-method/cardPayment";
		 Juspay.Setup({
			 payment_form: "#payment_form",
			 success_handler: function(status, statusObj) {
	         // redirect to success page
				 var p = "order_id=" + statusObj.orderId
				 p = p + "&status=" + statusObj.status 
				 p = p + "&status_id=" + statusObj.statusId
				 window.location.href = thank_you_page+"/"+value
			 },
			 error_handler: function(error_code, error_message, bank_error_code, bank_error_message, gateway_id) {
	         // redirect to failure page
				 /*
					 * alert("Transaction not successful. Error: " +
					 * bank_error_message)
					 */

				 window.location.href = error_page+"/"+value
			 },
			 second_factor_window_closed_handler: function() {
				    // enable the pay button for the user
					window.location.href = error_page+"/"+value
			 }
		 });
 $("#payment_form").submit() ;   
 return false; 	 
 }

 function submitDebitCardForm(value){
	 var baseUrl=window.location.origin;
		var website = ACC.config.encodedContextPath;
		 var thank_you_page = /*(website.indexOf("https") > -1 ? "" : "https://") +*/baseUrl+ website + "/checkout/multi/payment-method/cardPayment";
		 var error_page = /*(website.indexOf("https") > -1 ? "" : "https://") +*/ baseUrl+website + "/checkout/multi/payment-method/cardPayment";
		 Juspay.Setup({
			 payment_form: "#debit_payment_form",
			 success_handler: function(status, statusObj) {
	         // redirect to success page
				 var p = "order_id=" + statusObj.orderId
				 p = p + "&status=" + statusObj.status 
				 p = p + "&status_id=" + statusObj.statusId
				 window.location.href = thank_you_page+"/"+value
			 },
			 error_handler: function(error_code, error_message, bank_error_code, bank_error_message, gateway_id) {
	         //redirect to failure page
				 /*alert("Transaction not successful. Error: " + bank_error_message)*/

				 window.location.href = error_page+"/"+value
			 },
			 second_factor_window_closed_handler: function() {
				    // enable the pay button for the user
					window.location.href = error_page+"/"+value
			 }
		 });
		 	$("#debit_payment_form").submit() ;   
		 	return false; 	 
 	}

 function submitEmiCardForm(value){
	 var baseUrl=window.location.origin;
		var website = ACC.config.encodedContextPath;
		 var thank_you_page = /*(website.indexOf("https") > -1 ? "" : "https://") +*/baseUrl+ website + "/checkout/multi/payment-method/cardPayment";
		 var error_page = /*(website.indexOf("https") > -1 ? "" : "https://") +*/ baseUrl+website + "/checkout/multi/payment-method/cardPayment";
		 Juspay.Setup({
			 payment_form: "#emi_payment_form",
			 success_handler: function(status, statusObj) {
	         // redirect to success page
				 var p = "order_id=" + statusObj.orderId
				 p = p + "&status=" + statusObj.status 
				 p = p + "&status_id=" + statusObj.statusId
				 window.location.href = thank_you_page+"/"+value
			 },
			 error_handler: function(error_code, error_message, bank_error_code, bank_error_message, gateway_id) {
	         //redirect to failure page
				 /*alert("Transaction not successful. Error: " + bank_error_message)*/

				 window.location.href = error_page+"/"+value
			 },
			 second_factor_window_closed_handler: function() {
				    // enable the pay button for the user
					window.location.href = error_page+"/"+value
			 }
		 });
		 $("#emi_payment_form").submit() ;   
		 return false; 	 
 	}
 

 function newCardForm(){
	$(".newCard, #savedCard, .saved-card-button").css("display","none");
	$(".savedCard").css("display","table-cell");
	$(".newCardPayment, #newCard").css("display","block");
	$(".make_payment_top_savedCard").css("display","none");
	$(".make_payment_top_newCard").css("display","block");
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
		applyPromotion(null,"none","none");
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
		$(".accepted-cards .amex").parent().css("display","inline-block");
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
		applyPromotion(null,"none","none");
	}
	// TISEE-5555
	$('.security_code_hide').prop('disabled', true);
	$('.security_code').prop('disabled', false); 
}
 

 

 function populateBillingAddress(){ 
	 $("#firstNameError, #lastNameError, #address1Error, #address2Error, #address3Error, #cityError, #stateError, #pinError").text(""); 
	 var guid=$("#guid").val();
	 var dataString = 'guid=' + guid;
	 $.ajax({ 
		 contentType : "application/json; charset=utf-8",
		 url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/setShippingAddress", 
		 data : dataString,
		 dataType : "json",
		 type: "GET", 
		 cache: false, 
		 success : function(response) { 
			 if(response!="") 
			 { 
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
			 } 
			 else 
			 { 
				 $("#firstName, #lastName, #address1, #address2, #address3, #state, #city, #pincode").attr("readonly", false); 
				 $("#country").attr("disabled", false); 
				 $("#country").val("India"); 
			 } 
		 }, 
		 error : function(resp) { 
	 } 

	 }); 
	 }
 
 function populateBillingAddressEmi(){ 
	 var guid=$("#guid").val();
	 var dataString = 'guid=' + guid;
	 $("#firstNameErrorEmi, #lastNameErrorEmi, #address1ErrorEmi, #address2ErrorEmi, #address3ErrorEmi, #cityErrorEmi, #stateErrorEmi, #pinErrorEmi").text(""); 
	 $.ajax({ 
		 contentType : "application/json; charset=utf-8",
		 url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/setShippingAddress", 
		 data : dataString,
		 dataType : "json",
		 type: "GET", 
		 cache: false, 
		 success : function(response) { 
			 if(response!="") 
			 { 
			 var values=response.split("|"); 
			 $("#firstNameEmi").val(values[0]); 
			 $("#lastNameEmi").val(values[1]); 
			 $("#address1Emi").val(values[2]); 
			 $("#address2Emi").val(values[3]); 
			 $("#address3Emi").val(values[4]); 
			 $("#countryEmi").val(values[5]); 
			 $("#stateEmi").val(values[6]); 
			 $("#cityEmi").val(values[7]); 
			 $("#pincodeEmi").val(values[8]); 
			 $("#firstNameEmi, #lastNameEmi, #address1Emi, #address2Emi, #address3Emi, #stateEmi, #cityEmi, #pincodeEmi").attr("readonly", true); 
			 $("#countryEmi").attr("disabled", true); 
			 } 
			 else 
			 { 
			 $("#firstNameEmi, #lastNameEmi, #address1Emi, #address2Emi, #address3Emi, #stateEmi, #cityEmi, #pincodeEmi").attr("readonly", true);  
			 $("#countryEmi").attr("disabled", false); 
			 $("#countryEmi").val("India"); 
			 } 
		 }, 
		 error : function(resp) { 
		 } 

	 }); 
}

 
function savedCardForm(){
	$(".savedCard, .newCardPayment, #newCard").css("display","none");
	$(".newCard").css("display","table-cell");
	$("#savedCard, .saved-card-button").css("display","block");
	$(".make_payment_top_savedCard").css("display","block");
	$(".make_payment_top_newCard").css("display","none");
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
	// TISEE-5555
	$('.security_code_hide').prop('disabled', true);
	$('.security_code').prop('disabled', false); 
}


// Card Validations

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

function validateExpMMDc() {
	var cardType=$("#cardTypeDc").val();
	var year = document.getElementsByName("expyy");
	var month = document.getElementsByName("expmm");
	var errorHandle = document.getElementById("expYYErrorDc");
	
	if(cardType!="MAESTRO")
	{
		var yy = year[1].value;
		var mm = month[1].value;
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

function validateExpMMEmi() {
	var cardType=$("#cardTypeEmi").val();
	var year = document.getElementsByName("expyy");
	var month = document.getElementsByName("expmm");
	var errorHandle = document.getElementById("expYYErrorEmi");
	
	if(cardType!="MAESTRO")
	{
		var yy = year[2].value;
		var mm = month[2].value;
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

















function validateExpYYDc() {
	var cardType=$("#cardTypeDc").val();
	var year = document.getElementsByName("expyy");
	var month = document.getElementsByName("expmm");
	var errorHandle = document.getElementById("expYYErrorDc");
	
	if(cardType!="MAESTRO")
	{
		var yy = year[1].value;
		var mm = month[1].value;
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

function validateExpYYEmi() {
	var cardType=$("#cardTypeEmi").val();
	var year = document.getElementsByName("expyy");
	var month = document.getElementsByName("expmm");
	var errorHandle = document.getElementById("expYYErrorDc");
	
	if(cardType!="MAESTRO")
	{
		var yy = year[2].value;
		var mm = month[2].value;
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
	// var handle = document.getElementsByName("memberName");
	var errorHandle = document.getElementById("memberNameError");
	//var name =$(".name_on_card").val();
	var memberName = document.getElementsByName("memberName");
	var name = memberName[0].value;
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

function validateNameDc() {
	//var handle = document.getElementsByName("memberName");
	var errorHandle = document.getElementById("memberNameErrorDc");
	var memberName = document.getElementsByName("memberName");
	var name = memberName[1].value;
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

function validateNameEmi() {
	//var handle = document.getElementsByName("memberName");
	var errorHandle = document.getElementById("memberNameErrorEmi");
	var memberName = document.getElementsByName("memberName");
	var name = memberName[2].value;
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
	$(".remove-coupon-button").popover({
		html: 'true',
	    placement: 'left',
	    trigger: 'hover',
	  // title: 'Card Security Code',
	    content: $("#couponRelContent").val()
	});
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

function validateCVVDc() {
	var cardType=$("#cardTypeDc").val();
	var handle = document.getElementsByName("cvv");
	var errorHandle = document.getElementById("cvvErrorDc");
	
	if(cardType!="MAESTRO")
	{
		var number = handle[1].value;
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

function validateCVVEmi() {
	var cardType=$("#cardTypeEmi").val();
	var handle = document.getElementsByName("cvv");
	var errorHandle = document.getElementById("cvvErrorEmi");
	
	if(cardType!="MAESTRO")
	{
		var number = handle[2].value;
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

function validateCardNo(formSubmit) {
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
		//TPR-629
		if(formSubmit=="formSubmit")
		{
			dopayment(binStatus);
		}
		errorHandle.innerHTML = "Please enter a valid card number";
		return false;
	}
	else if(value.length<13 || value.length>19){
		binStatus=false;
		//TPR-629
		if(formSubmit=="formSubmit")
		{
			dopayment(binStatus);
		}
		errorHandle.innerHTML = "Please enter a valid card number";
		return false;
	}
	else if(cardType=='MAESTRO' && !(value.length==13 || value.length==14 || value.length==15 || value.length==16 || value.length==17 || value.length==18 || value.length==19)){
		binStatus=false;
		//TPR-629
		if(formSubmit=="formSubmit")
		{
			dopayment(binStatus);
		}
		errorHandle.innerHTML = "Please enter a valid card number";
		return false;
	}
	else if(cardType=='VISA' && !(value.length==16 || value.length==13)){
		binStatus=false;
		//TPR-629
		if(formSubmit=="formSubmit")
		{
			dopayment(binStatus);
		}
		errorHandle.innerHTML = "Please enter a valid card number";
		return false;
	}
	else if(cardType=='MASTERCARD' && value.length!=16){
		binStatus=false;
		//TPR-629
		if(formSubmit=="formSubmit")
		{
			dopayment(binStatus);
		}
		errorHandle.innerHTML = "Please enter a valid card number";
		return false;
	}
	else if(cardType=='AMEX' && value.length!=15){
		binStatus=false;
		//TPR-629
		if(formSubmit=="formSubmit")
		{
			dopayment(binStatus);
		}
		errorHandle.innerHTML = "Please enter a valid card number";
		return false;
	}
	// TISPRO-572 - Commenting check for EMI in case of AMEX
	else if(cardType=='AMEX' && value.length==15 && (/* $("#paymentMode").val()=='EMI' || */ $("#paymentMode").val()=='Debit Card')){
		binStatus=false;
		//TPR-629
		if(formSubmit=="formSubmit")
		{
			dopayment(binStatus);
		}
		errorHandle.innerHTML = "Please enter a valid debit card";
		return false;
	}
	else if(cardType=='MAESTRO' && (value.length==16 || value.length==18 || value.length==19) && ($("#paymentMode").val()=='EMI' || $("#paymentMode").val()=='Credit Card')){
		binStatus=false;
		//TPR-629
		if(formSubmit=="formSubmit")
		{
			dopayment(binStatus);
		}
		errorHandle.innerHTML = "Please enter a valid credit card";
		return false;
	}
	else if(cardType==""){
		binStatus=false;
		//TPR-629
		if(formSubmit=="formSubmit")
		{
			dopayment(binStatus);
		}
		errorHandle.innerHTML = "Sorry, the entered card type is not supported";
		return false;
	}
	// Commented for unsupported card types in Release 1
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
		//TPR-629
		if(formSubmit=="formSubmit")
		{
			dopayment(binStatus);
		}
		errorHandle.innerHTML = "Please enter a valid card number";
		return false;
	}
	// BIN Validation
	var bin=value.slice(0,6);
	// calling BIN Check AJAX
	// Added for TPR-1035 
	var dataString= $("#paymentMode").val();
	if(dataString == "Credit Card")		
	{
		cardType = "CREDIT";
	}
	//calling BIN Check AJAX

	$.ajax({
		url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/binCheck/"+bin,
		data: "cardType="+cardType,
		type: "POST",
		cache: false,
		success : function(response) {	
			if(!response.isValid)
			{
				//TPR-629
				if(formSubmit=="formSubmit")
				{
					dopayment(binStatus);
				}
				errorHandle.innerHTML = "Cannot proceed. Please try with diff card";
				return false;
			}
			else
			{
				if(response.cardType==null)
				{
					binStatus=true;
					if($("#paymentMode").val()!='EMI'){
						applyPromotion(null,binStatus,formSubmit);
					}
					else
					{
						//TPR-629
						if(formSubmit=="formSubmit")
						{
							dopayment(binStatus);
						}
					}
					errorHandle.innerHTML = "";
					return true;
				}
				else
				{
					var selectedBank=$("select[id='bankNameForEMI']").find('option:selected').text();
					// TISPRO-572 bank selection drop down
					//var selectedBankVal=selectedBank.split(" ", 1);	//comment for INC_11876
					var selectedBankVal = selectedBank.toLowerCase(); //$("#bankNameForEMI").val();  //add for INC_11876
					var responseBankVal = response.bankName.toLowerCase();  //response.bankName;
					if($("#paymentMode").val()=='EMI')
					{
						if(response.cardType=="" || response.cardType==null || response.cardType=="CREDIT" || response.cardType=="CC" || response.cardType=="Credit")
						{

							//if(selectedBank!="select" && responseBankVal.indexOf(selectedBankVal)){	//comment for INC_11876
							if(selectedBank!="select" && responseBankVal.indexOf(selectedBankVal)!=-1){    //add for INC_11876

								binStatus=true;
								//applyPromotion(selectedBankVal,binStatus,formSubmit);
								errorHandle.innerHTML = "";
								return true;			
							}

							//else if(selectedBank!="select" && !responseBankVal.indexOf(selectedBankVal)){		//comment for INC_11876
							else if(selectedBank!="select" && responseBankVal.indexOf(selectedBankVal)==-1){	//add for INC_11876

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
							applyPromotion(null,binStatus,formSubmit);
							errorHandle.innerHTML = "";
							return true;
						}
						else
						{
							binStatus=false;
							//TPR-629
							if(formSubmit=="formSubmit")
							{
								dopayment(binStatus);
							}
							errorHandle.innerHTML = "Please enter a valid Credit Card number";
						}
					}
				}
			}
		},
		error : function(resp) {
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "   Please enter a valid card number ";
			$("#promotionApplied").css("display","none");
			//resetConvCharge(binStatus);		//Commenting as method commented for perf fixes
			return false;
		}
	});		
	errorHandle.innerHTML = "";
	return true;
}

function validateDebitCardNo(formSubmit) {
	
	  var value=$("#cardNoDc").val();
	  var errorHandle = document.getElementById("cardNoErrorDc"); 
		var cardType= $("#cardTypeDc").val();
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
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid card number";
			return false;
		}
		else if(value.length<13 || value.length>19){
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid card number";
			return false;
		}
		else if(cardType=='MAESTRO' && !(value.length==13 || value.length==14 || value.length==15 || value.length==16 || value.length==17 || value.length==18 || value.length==19)){
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid card number";
			return false;
		}
		else if(cardType=='VISA' && !(value.length==16 || value.length==13)){
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid card number";
			return false;
		}
		else if(cardType=='MASTERCARD' && value.length!=16){
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid card number";
			return false;
		}
		else if(cardType=='AMEX' && value.length!=15){
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid card number";
			return false;
		}
		//TISPRO-572 - Commenting check for EMI in case of AMEX
		else if(cardType=='AMEX' && value.length==15 && (/*$("#paymentMode").val()=='EMI' ||*/ $("#paymentMode").val()=='Debit Card')){
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid debit card";
			return false;
		}
		else if(cardType=='MAESTRO' && (value.length==16 || value.length==18 || value.length==19) && ($("#paymentMode").val()=='EMI' || $("#paymentMode").val()=='Credit Card')){
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid credit card";
			return false;
		}
		else if(cardType==""){
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
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
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid card number";
			return false;
		}
		//BIN Validation
		var bin=value.slice(0,6);
		//alert(bin);
		// Added for TPR-1035 
		var dataString= $("#paymentMode").val();
		if(dataString == "Debit Card")		
		{
			cardType = "DEBIT";
		}
		
		//calling BIN Check AJAX
		$.ajax({
			url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/binCheck/"+bin,
			data: "cardType="+cardType,
			type: "POST",
			cache: false,
			success : function(response) {	
				if(!response.isValid)
				{
					//TPR-629
					if(formSubmit=="formSubmit")
					{
						dopayment(binStatus);
					}
					errorHandle.innerHTML = "Cannot proceed. Please try with diff card";
					return false;
				}
				else
				{
					if(response.cardType==null)
					{
						binStatus=true;
//						if($("#paymentMode").val()!='EMI'){
//							applyPromotion(null,binStatus,formSubmit);
//						}
//						//TPR-629
//						else
//						{
							if(formSubmit=="formSubmit")
							{
								dopayment(binStatus);
							}
//						}
						errorHandle.innerHTML = "";
						return true;
					}
					else
					{
//						var selectedBank=$("select[id='bankNameForEMI']").find('option:selected').text();
//						//TISPRO-572 bank selection drop down
//						var selectedBankVal=selectedBank.split(" ", 1);
//						var responseBankVal=response.bankName;
						if(document.getElementById("paymentMode").value=='Debit Card'){
							if(response.cardType=="" || response.cardType==null || response.cardType=="DEBIT" || response.cardType=="DC" || response.cardType=="Debit")
							{
								binStatus=true;
								applyPromotion(null,binStatus,formSubmit);
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
				//TPR-629
				if(formSubmit=="formSubmit")
				{
					dopayment(binStatus);
				}
				errorHandle.innerHTML = "   Please enter a valid card number ";
				$("#promotionApplied").css("display","none");
				resetConvCharge();
				return false;
			}
		});		
		errorHandle.innerHTML = "";
		return true;
	}

function validateEmiCardNo(formSubmit) {
	
	  var value=$("#cardNoEmi").val();
	  var errorHandle = document.getElementById("cardNoErrorEmi"); 
		var cardType= $("#cardTypeEmi").val();
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
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid card number";
			return false;
		}
		else if(value.length<13 || value.length>19){
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid card number";
			return false;
		}
		else if(cardType=='MAESTRO' && !(value.length==13 || value.length==14 || value.length==15 || value.length==16 || value.length==17 || value.length==18 || value.length==19)){
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid card number";
			return false;
		}
		else if(cardType=='VISA' && !(value.length==16 || value.length==13)){
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid card number";
			return false;
		}
		else if(cardType=='MASTERCARD' && value.length!=16){
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid card number";
			return false;
		}
		else if(cardType=='AMEX' && value.length!=15){
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid card number";
			return false;
		}
		//TISPRO-572 - Commenting check for EMI in case of AMEX
		else if(cardType=='AMEX' && value.length==15 && (/*$("#paymentMode").val()=='EMI' ||*/ $("#paymentMode").val()=='Debit Card')){
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid debit card";
			return false;
		}
		else if(cardType=='MAESTRO' && (value.length==16 || value.length==18 || value.length==19) && ($("#paymentMode").val()=='EMI' || $("#paymentMode").val()=='Credit Card')){
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid credit card";
			return false;
		}
		else if(cardType==""){
			binStatus=false;
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
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
			//TPR-629
			if(formSubmit=="formSubmit")
			{
				dopayment(binStatus);
			}
			errorHandle.innerHTML = "Please enter a valid card number";
			return false;
		}
		//BIN Validation
		var bin=value.slice(0,6);
		//alert(bin);
		// Added for TPR-1035 
		var dataString= $("#paymentMode").val();
		if(dataString == "Credit Card")		
		{
			cardType = "CREDIT";
		}
		
		//calling BIN Check AJAX
		$.ajax({
			url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/binCheck/"+bin,
			data: "cardType="+cardType,
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
						errorHandle.innerHTML = "";
						return true;
					}
					else
					{
						var selectedBank=$("select[id='bankNameForEMI']").find('option:selected').text();
						//TISPRO-572 bank selection drop down
						//var selectedBankVal=selectedBank.split(" ", 1);	//comment for INC_11876
						var selectedBankVal = selectedBank.toLowerCase(); //$("#bankNameForEMI").val();  //add for INC_11876
						var responseBankVal = response.bankName.toLowerCase();  //response.bankName;
						if($("#paymentMode").val()=='EMI')
						{
							if(response.cardType=="" || response.cardType==null || response.cardType=="CREDIT" || response.cardType=="CC" || response.cardType=="Credit")
							{

								//if(selectedBank!="select" && responseBankVal.indexOf(selectedBankVal)){		//comment for INC_11876
								if(selectedBank!="select" && responseBankVal.indexOf(selectedBankVal)!=-1){    //add for INC_11876

									binStatus=true;
									//TPR-629
									if(formSubmit=="formSubmit")
									{
										dopayment(binStatus);
									}
									//applyPromotion();
									errorHandle.innerHTML = "";
									return true;			
								}

								//else if(selectedBank!="select" && !responseBankVal.indexOf(selectedBankVal)){		//comment for INC_11876
								else if(selectedBank!="select" && responseBankVal.indexOf(selectedBankVal)==-1){	//add for INC_11876

									binStatus=false;
									//TPR-629
									if(formSubmit=="formSubmit")
									{
										dopayment(binStatus);
									}
									errorHandle.innerHTML = "Please enter a card same as the selected bank";
									return false;	
								}
							}
							else
							{
								binStatus=false;
								//TPR-629
								if(formSubmit=="formSubmit")
								{
									dopayment(binStatus);
								}
								errorHandle.innerHTML = "Please enter a valid Credit Card number";
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
			// maestro:
			// /^(5018|5044|5020|5038|5081|603845|6304|6759|676[1-3]|6220|504834|56|58)/,
			maestro: /^(50|56|57|58|59|60|61|62|63|64|65|66|67)/,
	        visa: /^4/,
	        mastercard: /^5[1-5]/,
	        amex: /^3[47]/,
	        // diners: /^(36|38|30[0-5])\d+$/,
	        // discover: /^(6011|65|64[4-9]|622)\d+$/,
	        // jcb: /^35\d+$/
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
	    // Commented for unsupported card types in Release 1
	    
	    /*
		 * else if (re.diners.test(number)) {
		 * document.getElementById("cardType").value='DINERS';
		 * $('ul.accepted-cards li').removeClass('active-card');
		 * $('ul.accepted-cards li
		 * span.diners').parent('li').addClass('active-card'); } else if
		 * (re.discover.test(number)) {
		 * document.getElementById("cardType").value='DISCOVER';
		 * $('ul.accepted-cards li').removeClass('active-card');
		 * $('ul.accepted-cards li
		 * span.discover').parent('li').addClass('active-card'); } else if
		 * (re.jcb.test(number)) {
		 * document.getElementById("cardType").value='JCB'; $('ul.accepted-cards
		 * li').removeClass('active-card'); $('ul.accepted-cards li
		 * span.jcb').parent('li').addClass('active-card'); }
		 */ else {
	    	document.getElementById("cardType").value="";
	    	 $('ul.accepted-cards li').removeClass('active-card');
	    	 $("#newMaestroMessage").css("display","none");
	    }
	    
}

function debitCardTypeFromNumber(number){
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
	        $("#cardTypeDc").val('MAESTRO');
	        $("#cardNoDc").attr('maxlength','19');
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
	    	$("#cardTypeDc").val('VISA');
	    	 $("#cardNoDc").attr('maxlength','16');
	    	 $(".security_code").attr('maxlength','3');
	    	 $('ul.accepted-cards li').removeClass('active-card');
		        $('ul.accepted-cards li span.visa').parent('li').addClass('active-card');
		        $("#newMaestroMessage").css("display","none");
	    } else if (re.mastercard.test(number)) {
	    	$("#cardTypeDc").val('MASTERCARD');
	    	 $("#cardNoDc").attr('maxlength','16');
	    	 $(".security_code").attr('maxlength','3');
	    	 $('ul.accepted-cards li').removeClass('active-card');
		        $('ul.accepted-cards li span.master').parent('li').addClass('active-card');
		        $("#newMaestroMessage").css("display","none");
	    } else if (re.amex.test(number)) {
	    	 $("#cardTypeDc").val('AMEX');
	    	 $("#cardNoDc").attr('maxlength','15');
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
	    	$("#cardTypeDc").val("");
	    	 $('ul.accepted-cards li').removeClass('active-card');
	    	 $("#newMaestroMessage").css("display","none");
	    }
	    
}

function emiCardTypeFromNumber(number){
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
	        $("#cardTypeEmi").val('MAESTRO');
	        $("#cardNoEmi").attr('maxlength','19');
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
	    	$("#cardTypeEmi").val('VISA');
	    	 $("#cardNoEmi").attr('maxlength','16');
	    	 $(".security_code").attr('maxlength','3');
	    	 $('ul.accepted-cards li').removeClass('active-card');
		        $('ul.accepted-cards li span.visa').parent('li').addClass('active-card');
		        $("#newMaestroMessage").css("display","none");
	    } else if (re.mastercard.test(number)) {
	    	$("#cardTypeEmi").val('MASTERCARD');
	    	 $("#cardNoEmi").attr('maxlength','16');
	    	 $(".security_code").attr('maxlength','3');
	    	 $('ul.accepted-cards li').removeClass('active-card');
		        $('ul.accepted-cards li span.master').parent('li').addClass('active-card');
		        $("#newMaestroMessage").css("display","none");
	    } else if (re.amex.test(number)) {
	    	 $("#cardTypeEmi").val('AMEX');
	    	 $("#cardNoEmi").attr('maxlength','15');
	    	 $(".security_code").attr('maxlength','4');
	    	 $('ul.accepted-cards li').removeClass('active-card');
		        $('ul.accepted-cards li span.amex').parent('li').addClass('active-card');
		        $("#newMaestroMessage").css("display","none");
	    }else{
	    	 $("#cardTypeEmi").val("");
	    	 $('ul.accepted-cards li').removeClass('active-card');
	    	 $("#newMaestroMessage").css("display","none");
	    }
	    }
	    
$('#cardNo').keyup(function(){
	   if($(this).val().length >= 1){
	       cardType = creditCardTypeFromNumber($(this).val());
	   }
	   else if($(this).val().length < 2){
		   console.log('bbb');
		   document.getElementById("cardType").value="";
		   $('ul.accepted-cards li').removeClass('active-card');
	   }
});

$('#cardNoDc').keyup(function(){
	   if($(this).val().length >= 1){
	       cardType = debitCardTypeFromNumber($(this).val());
	   }
	   else if($(this).val().length < 2){
		   $('#cardTypeDc').val('');
		   $('ul.accepted-cards li').removeClass('active-card');
	   }
});

$('#cardNoEmi').keyup(function(){
	   if($(this).val().length >= 1){
	       cardType = emiCardTypeFromNumber($(this).val());
	   }
	   else if($(this).val().length < 2){
		   $('#cardTypeEmi').val('');
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

function populateAddressEmi(){
	var checkbox=document.getElementById("sameAsShippingEmi")
	if(checkbox.checked==true){
		populateBillingAddressEmi();
	}
	else{
		$("#firstNameEmi").val("");
		$("#lastNameEmi").val("");
		$("#address1Emi").val("");
		$("#address2Emi").val("");
		$("#address3Emi").val("");
		$("#countryEmi").val("India");
		$("#stateEmi").val("");
		$("#cityEmi").val("");
		$("#pincodeEmi").val("");
		$("#firstNameEmi, #lastNameEmi, #address1Emi, #address2Emi, #address3Emi, #stateEmi, #cityEmi, #pincodeEmi").attr("readonly", false);
		$("#countryEmi").attr("disabled", false);
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

function validatePinEmi() {
	var number = $("#pincodeEmi").val();
	var errorHandle = document.getElementById("pinErrorEmi");
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




$("#newAddressButton,#newAddressButtonUp").click(function() {
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
		/* Error message changed TISPRD-427 */
		$("#firstnameError").html("<p>First name should not contain any special characters or space</p>");
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
		/* Error message changed TISPRD-427 */
		$("#lastnameError").html("<p>Last name should not contain any special characters or space</p>");
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

	/*else if(regAddress.test(result) == false)  
	{ 
		$("#address1Error").show();
		$("#address1Error").html("<p>Address Line 1 must be alphanumeric only</p>");
		validate= false;
	}  */

		else
	{
		$("#address1Error").hide();
	}	
	
	   /* result=address2.value;
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
	}*/
	
	/*result=address3.value;
	if(result == undefined || result == "")
	{	
		$("#address3Error").show();
		$("#address3Error").html("<p>Address line 3 cannot be blank</p>");
		validate= false;
	}
	else if(regAddress.test(result) == false)  
	{ 
		$("#address3Error").show();
		$("#address3Error").html("<p>Address line 3 must be alphanumeric only</p>");	
		validate= false;
	}  
	else
	{
		$("#address3Error").hide();	
	}*/
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
		if(address1.value.indexOf('#')!=-1)
    	{
		address1.value=encodeURIComponent(address1.value);
    	}
		
		if(address2.value.indexOf('#')!=-1)
    	{
		address2.value=encodeURIComponent(address2.value);
    	}
		if(address3.value.indexOf('#')!=-1)
    	{
		address3.value=encodeURIComponent(address3.value);
    	}
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
	bankNameSelected=bankName;
	//alert(bankName);
	applyPromotion(bankName,"none","none");	

}


//TPR-629---changes in parameter
function applyPromotion(bankName,binValue,formSubmit)
{
	var staticHost=$('#staticHost').val();
	$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 50%;top:50%; height: 30px; z-index: 99999;">'); 

	$(".make_payment, #make_saved_cc_payment_up, #make_cc_payment_up, #make_nb_payment_up, #paymentButtonId_up").attr('disabled','true');
	var paymentMode=$("#paymentMode").val();
	$("#promotionApplied,#promotionMessage").css("display","none");
	var selectedBank=$("select[id='bankNameForEMI']").find('option:selected').text();
	var guid=$("#guid").val();
	$.ajax({
		url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/applyPromotions",
		data: { 'paymentMode' : paymentMode , 'bankName' : bankName , 'guid' : guid},
		type: "GET",
		cache: false,
		dataType:'json',
		success : function(response) {
			if(null!=response.promoExpiryMsg && response.promoExpiryMsg=="redirect")
			{
				$(location).attr('href',ACC.config.encodedContextPath+"/cart"); // TISEE-510
			}
//			else if(null!=response.promoExpiryMsg && response.promoExpiryMsg=="redirect_to_payment")
//			{
//				$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/pay?value="+guid);
//			}
			else
			{
				var totalDiscount=0;
				var productDiscount="";
				var orderDiscount="";
				$("#promotionMessage").empty();
				var total=response.totalPrice.formattedValue;
				document.getElementById("totalWithConvField").innerHTML=response.totalPrice.formattedValue;
				document.getElementById("outstanding-amount").innerHTML=response.totalPrice.formattedValue;
				document.getElementById("outstanding-amount-mobile").innerHTML=response.totalPrice.formattedValue;
				$("#cartPromotionApplied").css("display","none");
				$("#codAmount").text(response.totalPrice.formattedValue);

				// Coupon
				if(null!=response.voucherDiscount && null!=response.voucherDiscount.couponDiscount)
				{
					if(response.voucherDiscount.couponDiscount.doubleValue<=0)
					{
		 				$("#couponApplied, #priceCouponError, #emptyCouponError, #appliedCouponError, #invalidCouponError," +
		 						" #expiredCouponError, #issueCouponError, #freebieCouponError").css("display","none");
		 				document.getElementById("couponValue").innerHTML=response.voucherDiscount.couponDiscount.formattedValue;
		 				$('#couponFieldId').attr('readonly', false);
		 				var selection = $("#voucherDisplaySelection").val();
		 				$("#couponFieldId").val(selection);
		 				$("#couponMessage").html("Oh no! This coupon code can't be used anymore. Please try another.");	//TPR-815
		 				$('#couponMessage').show();
		 				$('#couponMessage').delay(5000).fadeOut('slow');
		 				setTimeout(function(){ $("#couponMessage").html(""); }, 10000); 	
					}
					else
					{
						$("#couponApplied").css("display","block");
		 				document.getElementById("couponValue").innerHTML=response.voucherDiscount.couponDiscount.formattedValue;
		 				// $("#couponFieldId").attr('disabled','disabled');
		 				if($("#couponFieldId").val()=="")
		 				{
		 					$("#couponFieldId").val(response.voucherDiscount.voucherCode);
		 				}
		 				$('#couponFieldId').attr('readonly', true);
		 				$("#couponMessage").html("Coupon application may be changed based on promotion application");
		 				$('#couponMessage').show();
		 				$('#couponMessage').delay(5000).fadeOut('slow');
		 				setTimeout(function(){ $("#couponMessage").html(""); }, 10000);
					}
				}
				if(response.mplPromo==null || response.mplPromo==[])
				{
					$("#promotionApplied,#promotionMessage").css("display","none");
				}
				else
				{
					for (var x = 0; x < response.mplPromo.length; x++)
					{
						if(!(response.mplPromo[x]==null || response.mplPromo[x]=='null' || response.mplPromo[x]=='undefined')) //TISSIT-2046 TISBM-4449
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
					}
					
					// TISEE-352
					if(response.totalDiscount.value != 0){
						$("#promotionApplied").css("display","block");
						document.getElementById("promotion").innerHTML=response.totalDiscount.formattedValue;				
					}
					
					// TISST-7955
					var ussidPriceJson = JSON.parse(response.ussidPriceDetails);
					for ( var key in ussidPriceJson) 
					{
						var ussid=key;
						var ussidPrice= ussidPriceJson[key];
						$("#"+ussid+"_productPriceId").html(ussidPrice);
					}
				}
				$("#no-click,.spinner").remove();
				$(".make_payment, #make_saved_cc_payment_up, #make_cc_payment_up, #make_nb_payment_up, #paymentButtonId_up").removeAttr('disabled');
				//TISPT-29
				if(paymentMode=='EMI' || paymentMode=='Credit Card' || paymentMode=='Debit card' || paymentMode=='Netbanking')
				{
					isCodSet = false;
				}
				var cartTotal=response.totalPrice.value;
				
				if(null!=response.promoExpiryMsg && response.promoExpiryMsg=="redirect_to_payment")
				{
					document.getElementById("juspayErrorMsg").innerHTML="Existing Promotion has expired";
					$("#juspayconnErrorDiv").css("display","block");
					$("#no-click,.spinner").remove();
				}
				else if(paymentMode=='EMI')
				{
					if(formSubmit=="formSubmit")
					{
						dopayment(binValue);
					}
					else if(selectedBank==""){
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
									
									/*TPR-641*/
									utag.link({
										link_obj: this,
										link_text: 'emi_more_information' ,
										event_type : 'emi_more_information'
									});
								}
								else{								
									$("#bankNameForEMI, #listOfEMiBank , .bank-label").css("display","none");
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
							data: { 'selectedEMIBank' : selectedBank , 'cartTotal' : cartTotal},
							type: "GET",
							cache: false,
							success : function(data) {	
								$("#radioForEMI").css("display","block");
								$("#selectedTerm").val("select");
								var emiTable=document.getElementById("EMITermTable");
								var rowCount = emiTable.rows.length;
								//for(var i=rowCount-1; i>0; i--){		//comment for INC_11876
								for(var i=rowCount-1; i>=0; i--){		//add for INC_11876
									emiTable.deleteRow(i);
									rowCount--;
								}
								if(data[0]!=undefined){
									$("#radioForEMI").css("display","block");
									for	(var index = 0; index < data.length; index++){
										var rowCount = emiTable.rows.length;
										var row = emiTable.insertRow(rowCount);
										row.insertCell(0).innerHTML= '<input type="radio" name="termRadio" id="termRadioId' + index + '" value="" style="display: inherit;" onclick="validateSelection()"><label for="termRadioId' + index + '">' + data[index].term + ' Months </label>';
										//row.insertCell(1).innerHTML= data[index].term + " months";
										row.insertCell(1).innerHTML= /*"Rs. " + */data[index].monthlyInstallment;
										row.insertCell(2).innerHTML= data[index].interestRate + "%";
										row.insertCell(3).innerHTML= /*"Rs. " + */data[index].interestPayable;
										var radioId=document.getElementsByName("termRadio")[index].id;
										document.getElementById(radioId).value=data[index].term;
										
									}
									$("#emi-notice").show();
									
									/*TPR-641 starts*/
									emiBankSelectedTealium = "emi_option_" + selectedBank.replace(/ /g, "").replace(/[^a-z0-9\s]/gi, '').toLowerCase();
									utag.link({
										link_obj: this, 
										link_text: emiBankSelectedTealium , 
										event_type : 'emi_option_selected'
									});
									/*TPR-641 ends*/
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
				else if(paymentMode=='Credit Card' || paymentMode=='Debit Card')
				{
					if(formSubmit=="formSubmit")
					{
						dopayment(binValue);
					}
				}
				$("#no-click").remove();
				//$(".make_payment").removeAttr('disabled');
			}
			//if(isNewCard){//if this variable is true resetting the opacity
			//$("body").append("<div id='no-click' style='opacity:0.65; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
			//isNewCard = false;
		//}
		},
		error : function(resp) {
			$("#no-click,.spinner").remove();
			$(".make_payment").removeAttr('disabled');
			$("#no-click1,.spinner1").remove();
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
		$(".pay button, .make_payment_top_nb").prop("disabled",true);
		$(".pay button, .make_payment_top_nb").css("opacity","0.5");
		// store url change
		
		/*TPR-3446 net banking starts*/
		
		/*$(".pay").append('<img src="/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: absolute; right: 23%;bottom: 92px; height: 30px;">');
		$(".pay .spinner").css("left",(($(".pay.top-padding").width()+$(".pay.top-padding button").width())/2)+10);
		$("body").append("<div id='no-click' style='opacity:0.00; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");*/
		
		$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>").append('<img src="/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;">');
		$("body").append('<img src="/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
		
		/*TPR-3446 net banking ends*/
		
		$("#netbankingError").css("display","none");
		var firstName=selectedValue;
		var lastName=addressLine1=addressLine2=addressLine3=country=state=city=pincode=null;
		var cardSaved=false;
		var guid=$("#guid").val();
		$.ajax({
			url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/createJuspayOrder",
			data: { 'firstName' : firstName , 'lastName' : lastName , 'addressLine1' : addressLine1, 'addressLine2' : addressLine2 , 'addressLine3' : addressLine3, 'country' : country , 'state' : state, 'city' : city , 'pincode' : pincode, 'cardSaved' : cardSaved, 'guid' : guid},
			type: "GET",
			cache: false,
			success : function(response) {
				if(response=='redirect'){
					$(location).attr('href',ACC.config.encodedContextPath+"/cart"); //TIS 404
				}
				//TPR-629
				else if(response=='redirect_to_payment'){
					$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/pay?value="+guid); //TIS 404
				}
				else if(response=='redirect_with_details'){
					$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/cardPayment/"+guid); //TPR-629
				}

				else if(response=="" || response==null || response=="JUSPAY_CONN_ERROR"){
					document.getElementById("juspayErrorMsg").innerHTML="Sorry! The system is down, please try again";
					$("#juspayconnErrorDiv").css("display","block");
					$(".pay button, .make_payment_top_nb").prop("disabled",false);
					$(".pay button, .make_payment_top_nb").css("opacity","1");
					$(".pay .spinner").remove();
					$("#no-click,.spinner").remove();
				}
				else if(response=='redirect_with_details'){
					$(location).attr('href',ACC.config.encodedContextPath+"/checkout/multi/payment-method/cardPayment/"+guid); //TIS 404
				}
				else{
					//TISSTRT-1391 
					window.sessionStorage.removeItem("header");
					//TISPRO-153
					sendTealiumData();
					var values=response.split("|"); 
					$("#juspayOrderId").val(values[0]);
					var juspayOrderId=$("#juspayOrderId").val();
					$.ajax({
						url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/submitNBForm",
						data: { 'selectedNBBank' : selectedValue , 'juspayOrderId' : juspayOrderId },
						dataType: "json",
						contentType : "application/json; charset=utf-8",
						type: "GET",
						cache: false,
						success : function(response) {
								if(response=="" || response==null || response=="JUSPAY_CONN_ERROR"){
									document.getElementById("juspayErrorMsg").innerHTML="Sorry! The system is down, please try again";
									$("#juspayconnErrorDiv").css("display","block");
									$(".pay button, .make_payment_top_nb").prop("disabled",false);
									$(".pay button, .make_payment_top_nb").css("opacity","1");
									$(".pay .spinner").remove();
									$("#no-click,.spinner").remove();
								}

								else{
									var juspayResponse = JSON.parse(response);
									// console.log(juspayResponse);
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
									
	// var frm = document.createElement("form")
	// frm.style.display = "none"; // ensure that the form is hidden from the
	// user
	// frm.setAttribute("method", method);
	// frm.setAttribute("action", url);
	// if(method === "POST") {
	// var params = juspayResponse.payment.authentication.params;
	// for(var key in params) {
	// var value = params[key];
	// var field = document.createElement("input");
	// field.setAttribute("type", "hidden");
	// field.setAttribute("name", key);
	// field.setAttribute("value", value);
	// frm.appendChild(field);
	// }
	// }
	// document.body.appendChild(frm)
	// // form is now ready
	// frm.submit();
									
								}
							},
						error : function(resp) {
							$("#netbankingIssueError").css("display","block");
							$(".pay button, .make_payment_top_nb").prop("disabled",false);
							$(".pay button, .make_payment_top_nb").css("opacity","1");
							$(".pay .spinner").remove();
							$("#no-click,.spinner").remove();
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
	 			// TISST-13010
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
	 			//TISTI-255
//	 			TISPRD-1666 - console replaced with alert and resp print
	 			//alert("Some issues are there with Checkout at this time. Please try  later or contact our helpdesk");

	 			console.log(resp);
	 			var errorDetails=JSON.stringify(resp);
	 			console.log("errorDetails 1>> "+errorDetails);
	 			
	 			handleExceptionOnServerSide(errorDetails);

	 		}
	 	});	 
}

//TPR-1214
$("#address-form").click(function(){
	
	$.ajax({
 		url: ACC.config.encodedContextPath + "/checkout/multi/delivery-method/new-address",
 		type: "GET",
 		cache: false,
 		dataType: "html",
 		success : function(response) {
 			//alert('here');
 			$(".addnewAddresPage").html(response);
 		},
 		error : function(resp) {
 		}
 		
 		});
});
//TPR-1213
$(document).ready(function(){
	

$(".edit_address").click(function(){
	//var address_id = $(this).parents().find(".edit").next(".editnewAddresPage").attr("id");
	var address_id = $(this).attr('id');
	console.log(address_id);
	var address_id_new = address_id.split('_');
	console.log(address_id_new[1]);
	//$(this).parents().find(".address, label").toggle();
	$.ajax({
 		url: ACC.config.encodedContextPath + $(this).attr("href"),
 		type: "GET",
 		cache: false,
 		dataType: "html",
 		success : function(response) {
 		//	$(this).parents().find(".edit").next(".editnewAddresPage#"+address_id).html(response);
 			$('.editnewAddresPage, .formaddress').slideUp();
 			$(".add-address").slideDown();
 			$("#"+address_id_new[1]).html(response);
 		//	$(this).parents().find(".edit").next(".editnewAddresPage").show();
 			//$(".editnewAddresPage .checkout-shipping.formaddress").prepend("<input type='button' value='cancel' class='cancelBtnEdit'>");
 			//alert('hi');
 			
 			$("#"+address_id_new[1] + " .checkout-shipping.formaddress").prepend("<div class='heading-form'><h3>Edit Address</h3><input type='button' value='cancel' class='cancelBtnEdit' id='cancel-"+address_id_new[1]+"'></div>");
 			$("#"+address_id_new[1]).slideDown();
 		},
 		error : function(resp) {
 		}
 		
 		});
	return false;
});
});
//TPR-1215
$(".regular-radio").click(function(){
	var radio = $(this);
	var radio_label = $(this).parent().find('label');
	$.ajax({
 		url: ACC.config.encodedContextPath + "/checkout/multi/delivery-method/set-default-address/"+ $(this).attr("data-address-id"),
 		type: "GET",
 		cache: false,
 		dataType: "text",
 		success : function(response) {
 			if(response == 'true'){
 				//console.log(radio);
 				$(".address-list input[type='radio']+label").removeClass("radio-checked");
 				
 				radio.attr('checked', 'checked');
 				console.log(radio_label);
 				radio_label.addClass('radio-checked');
 				//radio_label.css('background-color',' #999999');
 			}else{
 				alert("Unable to set default address");
 			}
 		},
 		error : function(response) {
 			alert('Inside eror'+response);
 		}
 		
 		});
	return false;
});

//viewMore Address tpr-1212 starts
$(".viewMore,.viewMoreSign").click(function(e){
	
e.preventDefault();	
$(".add-address").removeClass("add-addressShifts");
$(".addsign.viewMoreSign").removeClass("addsignContent");
$('.checkTab .address-list.hideItem').slideToggle(function(){
	 $(".addNew_wrapper").removeClass("moreEvens_address");
	if ($(this).is(':visible') && $(".address-list").length % 2 != 0){
		 $(".addNew_wrapper").addClass("moreEvens_address");
	 }
	else{
		$(".add-address").removeClass("add-addressShifts");
		$(".add-address").removeClass("addressClear");
	}
	 
	
	 
	
	 
	if($( ".checkTab .address-list.hideItem" ).is( ":visible" ))
	{
		$(".addsign.viewMoreSign").addClass("addsignContent");
		$('.viewMore').text('View Less Address');
	}else{
		$(".addsign.viewMoreSign").removeClass("addsignContent");
		$('.viewMore').text('View More Address');
		$(".viewMoreContainer").removeClass("addressClear");
	}
	
	var len=0;
	$(".address-list").each(function(){
		if($(this).css("display") != "none"){
			len++;
		}
	});
	if(len%2 != 0){
		$(".add-address").addClass("add-addressShifts");
		//$(".addsign.viewMoreSign").addClass("addsignContent");
		$(".viewMoreContainer").removeClass("addressClear");
	}
	else{
		$(".add-address").removeClass("add-addressShifts");
		/*$(".addsign.viewMoreSign").removeClass("addsignContent");*/
	}
	
});

});


/*$(document).on("click",".edit>a.edit_address",function(){
	$(".viewMoreContainer").removeClass("addressClear");
});*/

$(".edit").eq(2).children("a").click(function(e){
	e.preventDefault();
	if(($(".address-list").length % 2 !=0)){
		$(".add-address").addClass("add-addressShifts");
		$(".add-address").addClass("addressClear");
	}
});


$(".edit").children("a").click(function(e){
	e.preventDefault();
	
	if(!$(".addsign").hasClass("addsignContent")){
		$(".add-address").removeClass("add-addressShifts");
		$(".add-address").removeClass("addressClear");
		/*if($(".address-list").length % 2 !=0){
			$(".viewMoreContainer").addClass("addressClear");
		}*/
	}
	/*else{
		$(".add-address").removeClass("add-addressShifts");
		$(".add-address").removeClass("addressClear");
		$(".viewMoreContainer").addClass("addressClear");
	}*/
	

});


$(document).on("click",".cancelBtnEdit",function(){
	if($(".address-list").length % 2 !=0){
	$(".add-address").removeClass("add-addressShifts");
	$(".add-address").removeClass("addressClear");
	}
});

$(document).on("click",".cancelBtnEdit:last",function(){
	if($(".viewMoreSign").hasClass("addsignContent") && ($(".address-list").length % 2 !=0)){
		$(".add-address").addClass("add-addressShifts");
		$(".add-address").addClass("addressClear");
	}
});

$(".edit:last").children("a.edit_address").on("click",function(){
	if($(".viewMoreSign").hasClass("addsignContent") && ($(".address-list").length % 2 !=0)){
		$(".add-address").addClass("add-addressShifts");
		$(".add-address").addClass("addressClear");
	}
});


//viewMore Address tpr-1212 ends


function selectDefaultDeliveryMethod() {
	 $('#deliveryradioul .delivery ul').each(function(){
		 var length = $(this).find("li").length; 
		 // console.log(length);
		 if(length >= "1") {
			  // console.log($(this).find("li:first").children("input:radio").attr("id"));
			  var radioSplit = $(this).find("li:first").children("input:radio").attr("id").split("_");
			  var radioId = radioSplit[0]+"_"+radioSplit[1];
			  calculateDeliveryCost(radioId,radioSplit[2]); // TISPT-104 REMOVED
			  $("#"+$(this).find("li:first").children("input:radio").attr("id")).prop('checked', true);
			  if($(this).find("input[type='radio']:checked").attr("id").split("_")[2] == "click-and-collect") {
			  		changeCTAButtonName($(this).find("input[type='radio']:checked").attr("id").split("_")[2]);
			  	}
		 }
	 });
}


$('#selectDeliveryMethodForm #deliveryradioul .delivery_options .delivery ul li input:radio').click(function(){
	/*TPR-685 starts*/
		 var length = $(this).find("li").length; 
		
			var radioSplit = $(this).attr("id").split("_");
			
			 var radioId = radioSplit[0]+"_"+radioSplit[1];
		
			  var mode=radioSplit[2]
			  
			 
				if(mode=="home-delivery"){
					utag.link(
							{link_text: 'deliver_mode_home' , event_type : 'delivery_mode_select'}
							);
				}
					
				else if(mode=="express-delivery"){
					utag.link(
							{link_text: 'deliver_mode_express' , event_type : 'delivery_mode_select'}
							);
				}
					
				else{
					utag.link(
							{link_text: 'deliver_mode_clickcollect' , event_type : 'delivery_mode_select'}
							);
				}
					
	/*TPR-685 ends*/		  
    changeCTAButtonName("DefaultName");
	$('#deliveryradioul .delivery ul').each(function(){
		var length = $(this).find("li").length; 
		if(length >= "1") {
			if($(this).find("input[type='radio']:checked").attr("id").split("_")[2]== "click-and-collect") {
				changeCTAButtonName($(this).find("input[type='radio']:checked").attr("id").split("_")[2]);
			}
		}
	});	
});




function changeCTAButtonName(deliveryCode) {
	// console.log(deliveryCode);
	// TISPRO-625
	var isExpressCheckoutSelected=$('#isExpressCheckoutSelected').val();
	if(deliveryCode == "click-and-collect") {
		$("#deliveryMethodSubmit").text("Proceed To Store");
		$("#deliveryMethodSubmitUp").text("Proceed To Store");
	} else if(deliveryCode== "DefaultName") {
		// TISPRO-625
		// $("#deliveryMethodSubmit").text("Choose Address");
		// $("#deliveryMethodSubmitUp").text("Choose Address");
		if(isExpressCheckoutSelected=='true')
		{
			$("#deliveryMethodSubmit").text("Proceed to Payment");
			$("#deliveryMethodSubmitUp").text("Proceed to Payment");
		}
		else
		{
			$("#deliveryMethodSubmit").text("Proceed To Address");
			$("#deliveryMethodSubmitUp").text("Proceed To Address");
		}
	}
}

// //TISST-13010
function showPromotionTag()
{
	$("#cartPromotionApplied").css("display","block");
}

$(document).ready(function(){
	$("#ussid").addClass("ussid");
	var elementId = $(".desktop li:nth-child(4) ul");
	elementId.after("<span class='pincodeServiceError'></span>");	
	$("#defaultPinCodeIds").keyup(function(event){
	    if(event.keyCode == 13){
	        $("#pinCodeButtonIds").click();
	    }
	});
	

	$("#popUpExpAddress input.address_radio[data-index='0']").attr("checked","checked");	

	/*TPR-1212 starts*/
	
	/*if(!$(".addsign.viewMoreSign").hasClass("addsignContent") && $(".address-list").length % 2 == 0){
		$(".addNew_wrapper").removeClass("moreEvens_address");
	}
	else{
		$(".addNew_wrapper").addClass("moreEvens_address");
	}
	if(!$(".addsign.viewMoreSign").hasClass("addsignContent")){
		$(".addNew_wrapper").removeClass("moreEvens_address");
	}*/
	
	/*TPR-1212 ends*/
	
});

//TPR-1786
function checkServiceabilityRequired(buttonType,el){
	var sessionPin = $("#pinId").val();
	var selectedPin=$('#defaultPinCodeIds').val();
	var checkoutLinkURlId = $('#checkoutLinkURlId').val();
	if(sessionPin != selectedPin){
		checkPincodeServiceability(buttonType,el);
	}
	else{
		
		redirectToCheckout(checkoutLinkURlId);
	}
}


function checkPincodeServiceability(buttonType,el)
{
// alert($(el).attr("id")+" :::button id")
	if(buttonType == "typeCheckout")
	{
		
		if(typeof utag == "undefined"){
			console.log("Utag is undefined")
		}
		else{
			//TPR-683
			utag.link(
			{"link_text": "mybag_checkout" , "event_type" : "mybag_checkout"}
			);
		}
	}
	/*spinner commented starts*/
	//$("#pinCodeDispalyDiv").append('<img src="/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: absolute; right:0;bottom:0; left:0; top:0; margin:auto; height: 30px;">');
	/*spinner commented ends*/
	// $("#pinCodeDispalyDiv
	// .spinner").css("left",(($("#pinCodeDispalyDiv").width()+$("#pinCodeDispalyDiv").width())/2)+10);
	/*TPR-3446 new starts*/
	//$("body").append("<div id='no-click' style='opacity:0.6; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	var staticHost = $('#staticHost').val();
	$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
	/*TPR-3446 new ends*/
	var selectedPincode=$('#defaultPinCodeIds').val();
	var regPostcode = /^([1-9])([0-9]){5}$/;
	$(".deliveryUlClass").remove();//TPR-1341
	
	if(selectedPincode === ""){	
		$( "#error-Id").hide();
		$( "#error-Id_tooltip").hide();
		$("#emptyId").css({
			"color":"#ff1c47",
			"display":"block",
			});
		$( "#emptyId_tooltip").show();
		$('#checkout-id #checkout-enabled').addClass('checkout-disabled');
		 $("#cartPinCodeAvailable").hide();// TPR-1055
		 $("#pinCodeButtonIds").text("Change Pincode");
		// setTimeout(function(){
		 $("#unserviceablepincode").hide();// tpr-1341
		 $("#unserviceablepincode_tooltip").hide();
		 $(".cartItemBlankPincode").show();
		$("#pinCodeButtonIds").text("Check");
		 $("#AvailableMessage").hide();
		 $(".pincodeServiceError").hide();
		 $(".delivery ul.success_msg").hide();
		$("#pinCodeDispalyDiv .spinner").remove();
		$("#no-click,.spinner").remove();
		// },500);

		return false;
	}
	else if(regPostcode.test(selectedPincode) != true){
		
    	$("#defaultPinCodeIds").css("color","red");
        $("#error-Id").show();
        $("#error-Id_tooltip").show();
        $("#cartPinCodeAvailable").hide();// TPR-1055
        $("#unserviceablepincode").hide();
        $("#unserviceablepincode_tooltip").hide();
        $("#AvailableMessage").hide();
        //TPR-1341
        $(".pincodeServiceError").hide();
        $(".delivery ul.success_msg").hide();
        $("#pinCodeButtonIds").text("Change Pincode");
        $(".cartItemBlankPincode").show();
		$("#emptyId").hide();
		$("#emptyId_tooltip").hide();
		$("#error-Id").css({
			"color":"red",
			"display":"block",

			});
		$("#error-Id_tooltip").show();
		$('#checkout-id #checkout-enabled').addClass('checkout-disabled');
		// setTimeout(function(){
		$("#pinCodeDispalyDiv .spinner").remove();
		$("#no-click,.spinner").remove();
		// },500);
        return false;  
    }
	// TPR-1055 starts
	else if($("#pinCodeButtonIds").text() == 'Change Pincode'&& $(el).attr("id") =="pinCodeButtonIds"){
		
		
		$("#unserviceablepincode").hide();
		$("#unserviceablepincode_tooltip").hide();
		$("#cartPinCodeAvailable").show();
		 $(".pincodeServiceError").hide();
		 $("#AvailableMessage").hide();
		 $(".cartItemBlankPincode").show();
		$("#pinCodeButtonIds").text("Check");
		 $('#defaultPinCodeIds').focus();
		$("#pinCodeDispalyDiv .spinner").remove();
		$("#emptyId").hide();
		$("#emptyId_tooltip").hide();
		$("#error-Id").hide();
		$("#error-Id_tooltip").hide();
		$("#no-click,.spinner").remove();
		$(".delivery ul.success_msg").hide();//TPR-1341
		return false; 
		// TPR-1055 ends
	}
	else
    {
		// $("#defaultPinCodeIds").prop('disabled', true);
		$("#pinCodeButtonIds").text("Check Pincode");
		$("#defaultPinCodeIds").css("color","black");
		$( "#error-Id").hide();
		$( "#error-Id_tooltip").hide();
		// $("#cartPinCodeAvailable").show();//TPR-1055
		$("#emptyId").hide();
		$("#emptyId_tooltip").hide();
	$.ajax({
 		url: ACC.config.encodedContextPath + "/cart/checkPincodeServiceability/"+selectedPincode,
 		type: "GET",
 		cache: false,
 		success : function(response) {
 			//"sprint merger issue
 			var responeStr=response['pincodeData'].split("|");
 			//TPR-970 changes
 			populateCartDetailsafterPincodeCheck(responeStr[1]);
 			//TPR-970 changes
 			if(responeStr[0]=="N")
 			{
 				if(typeof utag !="undefined")
 				{
	 				utag.link(
	 				{"link_obj": this,"link_text": "mybag_pincode:"+selectedPincode+":not serviceable", "event_type" : "mybag_pincode"}
	 		 	 	);
 				}
 				// TISTI-255
				// Please try later or contact our helpdesk");
 				// TISPRD-1666 - console replaced with alert and resp print
 				$("#AvailableMessage").hide();
 				$("#cartPinCodeAvailable").hide();
 				$("#unserviceablepincode").show();
 				$("#unserviceablepincode_tooltip").show();
 				 $(".pincodeServiceError").show();
 				populatePincodeDeliveryMode(response,buttonType);
					reloadpage(selectedPincode,buttonType);
 	 			$("#isPincodeServicableId").val('N');
 	 			// reloadpage(selectedPincode,buttonType);
 				} 
 			else
 				{
 				if(typeof utag !="undefined")
 				{
 				utag.link(
 		 	 	{"link_obj": this,"link_text": "mybag_pincode:"+selectedPincode+":success", "event_type" : "mybag_pincode"}
 		 	 	);
 				}
 				$(".pincodeServiceError").hide();
 				$("#unserviceablepincode").hide();
 				$("#unserviceablepincode_tooltip").hide();
 				$("#cartPinCodeAvailable").hide();
 				$("#AvailableMessage").html("Available delivery options for the pincode " +selectedPincode+ " are");
	 			$("#AvailableMessage").show();
 					populatePincodeDeliveryMode(response,buttonType);
 					reloadpage(selectedPincode,buttonType);
 				}
 			
 			// TISPRM-33
	 			$("#defaultPinDiv").show();
	 			
 	 			// $("#changePinDiv").hide();
 	 			$('#defaultPinCodeIdsq').val(selectedPincode);
 	 			// setTimeout(function(){
 	 		
 	 				$("#pinCodeDispalyDiv .spinner").remove();
 	 				$("#no-click,.spinner").remove();
 	 			// },500);
 	 				// TPR-1055
 	 				$('#defaultPinCodeIds').blur();
 	 				if ( $('#defaultPinCodeIds').val() == "") {
 	 				
 	 				// $("#cartPinCodeAvailable").html("Enter your pincode to
					// see your available delivery options");
 	 					$("#cartPinCodeAvailable").show();
 	 					$("#pinCodeButtonIds").text("Check")
 	 					
 	 				} else {
 	 					$("#cartPinCodeAvailable").hide();
 	 					// $("#unserviceablepincode").hide();
 	 					$("#pinCodeButtonIds").text("Change Pincode")
 	 				}
 		},
 		error : function(resp) {
 			if(typeof utag !="undefined"){
 			utag.link(
 			{"link_obj": this,"link_text": "mybag_pincode:"+selectedPincode+":not serviceable", "event_type" : "mybag_pincode"}
 			);
 			}
 			//TISTI-255
 			//alert("Some issues are there with Checkout at this time. Please try  later or contact our helpdesk");
 			console.log(resp);
 			$("#isPincodeServicableId").val('N');
 			reloadpage(selectedPincode,buttonType);
 			
// TISPRD-1666 - console replaced with alert and resp print
 			var errorDetails=JSON.stringify(resp);
 			console.log("errorDetails 1>> "+errorDetails);
 			
 			handleExceptionOnServerSide(errorDetails);
 			console.log('Some issue occured in checkPincodeServiceability');
 			// setTimeout(function(){
 	 			$("#pinCodeDispalyDiv .spinner").remove();
 	 			$("#no-click,.spinner").remove();
 	 		// },500);
 		}
 	});
	

   }
}
//TPR-970 changes starts
function populateCartDetailsafterPincodeCheck(responseData){
	if(null!=responseData['cartData']||""!=responseData['cartData']){
		var cartValue=responseData['cartData'];
		var cartData=responseData['cartEntries'];
		for(var cart in cartData){
			var entryNumber=parseInt(cartData[cart]['entryNumber']);
			$("#off-cartLevelDiscAmt_"+entryNumber).html("");
			$("#off-bag-cartLevelDisc_"+entryNumber).html("");
			$("#off-bag-ItemLevelDisc_"+entryNumber).html("");
			$("#off-bag-ItemLevelDiscAmt_"+entryNumber).html(""); 
			$("#off-bag-itemDisc_"+entryNumber).html("");
			$("#off-itemDiscAmt_"+entryNumber).html("");
			$("#cartCentOfferDisplay_"+entryNumber).hide();
			$("#cartAmtOfferDisplay_"+entryNumber).hide();
			$("#itemCartCentDisplay_"+entryNumber).hide();
			$("#itemCartAmtDisplay_"+entryNumber).hide();
			var isOfferPresent=false;
			var basePrice=$("#basePrice_"+entryNumber).val();
			if(basePrice!=""){
				$("#totalPrice_"+entryNumber).html(basePrice).addClass("delAction");
			}
			else{
				$("#totalPrice_"+entryNumber).html(cartData[cart]['totalPrice'].formattedValue).addClass("delAction");
			}
			if(cartData[cart]['cartLevelDisc']!=null){
				isOfferPresent=true;
				$("#CartofferDisplay_"+entryNumber).show();
				$("#off-bag-cartLevelDisc_"+entryNumber).html(cartData[cart]['cartLevelDisc'].formattedValue).addClass("priceFormat").append("<span>Off Bag</span>");
			    $("#off-cartLevelDiscAmt_"+entryNumber).html(cartData[cart]['amountAfterAllDisc'].formattedValue).addClass("priceFormat");
			}
			
			if(cartData[cart]['cartLevelPercentage']!=null){
				isOfferPresent=true;
				$("#CartofferDisplay_"+entryNumber).show();
				$("#off-bag-cartLevelDisc_"+entryNumber).html(cartData[cart]['cartLevelPercentage']+"%").addClass("priceFormat").append("<span>Off Bag</span>");
			    $("#off-cartLevelDiscAmt_"+entryNumber).html(cartData[cart]['amountAfterAllDisc'].formattedValue).addClass("priceFormat");
			}
			if(cartData[cart]['cartLevelDisc']!=null&&cartData[cart]['productPerDiscDisplay']!=null && cartData[cart]['productLevelDisc']){
				isOfferPresent=true;
				$("#ItemAmtofferDisplay_"+entryNumber).show();
				$("#off-bag-itemDisc_"+entryNumber).html(cartData[cart]['productPerDiscDisplay'].value+"%").addClass("priceFormat").append("<span>Off Item</span>");
				 $("#off-bag-ItemLevelDiscAmt_"+entryNumber).html(cartData[cart]['netSellingPrice'].formattedValue).addClass("priceFormat");
			}
			else{
				isOfferPresent=true;
				$("#ItemAmtofferDisplay_"+entryNumber).show();
				$("#off-bag-ItemLevelDisc_"+entryNumber).html(cartData[cart]['productPerDiscDisplay'].value+"%").addClass("priceFormat").append("<span>Off Item</span>");
				 $("#off-bag-ItemLevelDiscAmt_"+entryNumber).html(cartValue['totalPrice'].formattedValue).addClass("priceFormat");
			}
			if(cartData[cart]['productPerDiscDisplay']!=null && cartData[cart]['productLevelDisc']){
				isOfferPresent=true;
				$("#ItemAmtofferDisplay_"+entryNumber).show();
				$("#off-bag-itemDisc_"+entryNumber).html(cartData[cart]['productPerDiscDisplay'].value+"%").addClass("priceFormat").append("<span>Off Item</span>");
				 $("#off-bag-ItemLevelDiscAmt_"+entryNumber).html(cartData[cart]['netSellingPrice'].formattedValue).addClass("priceFormat");
			}
			else{
				isOfferPresent=true;
				$("#ItemAmtofferDisplay_"+entryNumber).show();
				$("#off-bag-ItemLevelDisc_"+entryNumber).html(cartData[cart]['productPerDiscDisplay'].value+"%").addClass("priceFormat").append("<span>Off Item</span>");
				 $("#off-bag-ItemLevelDiscAmt_"+entryNumber).html(cartData[cart]['totalPrice'].formattedValue).addClass("priceFormat");
			}
			
			if(isOfferPresent==false){
				$("#totalPrice_"+entryNumber).removeClass("delAction");
			}
		}
		if(cartValue!=null){
		$("#subtotal_Value").show();
		$("#subtotal").hide();
		$("#subtotalValue").html(cartValue['subTotal'].formattedValue).addClass("priceFormat");
		$("#discount").show();
		$("discount .amt").html("");
		if(null!=cartValue['totalDiscounts']&&cartValue['totalDiscounts'].value>0){
		$("#discount").show();
		$("#discount .amt").html(cartValue['totalDiscounts'].formattedValue);
		}
		else{
			$("#discount").hide();
		}
		$("#total .amt").html("");
		if(null!=cartValue['totalPriceWithTax']){
		$("#total .amt").html(cartValue['totalPriceWithTax'].formattedValue);
		}
		else{
			$("#total .amt").html(cartValue['totalPrice'].formattedValue);
		}
		}
		}
	
}

//TPR-970 changes ends

//TPR-1055
$("#defaultPinCodeIds").click(function(){
	$("#unserviceablepincode").hide();
	$("#unserviceablepincode_tooltip").hide();
	$(".deliveryUlClass").remove();//TPR-1341
	$("#cartPinCodeAvailable").show();
	 $( "#error-Id").hide();
	 $( "#error-Id_tooltip").hide();
	 $("#emptyId").hide();
	 $("#emptyId_tooltip").hide();
	 $(".pincodeServiceError").hide();
	 //TPR-1341
	 $(".cartItemBlankPincode").show();
	 $(".delivery ul.success_msg").hide();
	if($("#pinCodeButtonIds").text() == 'Change Pincode'){
		$("#pinCodeButtonIds").text("Check");
		$("#AvailableMessage").hide();
	}
	
});


function reloadpage(selectedPincode,buttonType) {
	if (/*$('#giftYourselfProducts').html().trim().length > 0 && */selectedPincode!=null && selectedPincode != undefined && selectedPincode!="") 

	{		
		if(buttonType != 'typeCheckout') {

		  window.location.reload(); 
		}
		
	}

}


function populatePincodeDeliveryMode(response,buttonType){
	
	var checkoutLinkURlId = $('#checkoutLinkURlId').val(); 
	// response='Y|123456|[{"fulfilmentType":null,"isPrepaidEligible":"Y","ussid":"123653098765485130011717","pinCode":null,"validDeliveryModes":[{"isCOD":true,"isPrepaidEligible":null,"isPincodeServiceable":null,"isCODLimitFailed":null,"type":"ED","inventory":"2","deliveryDate":null},{"isCOD":true,"isPrepaidEligible":null,"isPincodeServiceable":null,"isCODLimitFailed":null,"type":"HD","inventory":"4","deliveryDate":null}],"cod":"Y","transportMode":null,"isCODLimitFailed":"N","deliveryDate":"2015-08-29T13:30:00Z","isServicable":"Y","stockCount":12},{"fulfilmentType":null,"isPrepaidEligible":"Y","ussid":"123653098765485130011719","pinCode":null,"validDeliveryModes":[{"isCOD":true,"isPrepaidEligible":null,"isPincodeServiceable":null,"isCODLimitFailed":null,"type":"HD","inventory":"12","deliveryDate":null}],"cod":"Y","transportMode":null,"isCODLimitFailed":"N","deliveryDate":"2015-08-29T13:30:00Z","isServicable":"Y","stockCount":12}]';
	// response='N|123456|[{"fulfilmentType":null,"isPrepaidEligible":"Y","ussid":"123653098765485130011717","pinCode":null,"validDeliveryModes":[{"isCOD":true,"isPrepaidEligible":null,"isPincodeServiceable":null,"isCODLimitFailed":null,"type":"ED","inventory":"2","deliveryDate":null},{"isCOD":true,"isPrepaidEligible":null,"isPincodeServiceable":null,"isCODLimitFailed":null,"type":"HD","inventory":"2","deliveryDate":null}],"cod":"Y","transportMode":null,"isCODLimitFailed":"N","deliveryDate":"2015-08-29T13:30:00Z","isServicable":"Y","stockCount":2},{"fulfilmentType":null,"isPrepaidEligible":null,"ussid":"123653098765485130011719","pinCode":null,"validDeliveryModes":null,"cod":null,"transportMode":null,"isCODLimitFailed":null,"deliveryDate":null,"isServicable":"N","stockCount":null}]';

// console.log(response);
	//"sprint merger issue
	var values=response['pincodeData'].split("|");
	var isServicable=values[0];
	var selectedPincode=values[1];
	var deliveryModeJsonMap=values[2];
	
	$(".pincodeServiceError").hide();
	if(deliveryModeJsonMap=="null"){
		$('#unsevisablePin').show();
		
		$("#checkout-enabled").css("pointer-events","none");
		$("#checkout-enabled").css("cursor","default");
		$("#checkout-enabled").css("opacity","0.5");
		$("#expressCheckoutButtonId").css("pointer-events","none");
		$("#expressCheckoutButtonId").css("cursor","default");
		$("#expressCheckoutButtonId").css("opacity","0.5");
		var pincodeEntered = $('#defaultPinCodeIds').val();
		var pincodeServiceError = "This item is not serviceable for pincode "+pincodeEntered+"!";
		// console.log(pincodeServiceError);
		var elementId = $(".desktop li:nth-child(3) ul");
		elementId.hide();
		$(".pincodeServiceError").text(pincodeServiceError);
		// TPR-933
		$('.success_msg').hide();
		//TPR-1341
		$(".cartItemBlankPincode").hide();
		$(".pincodeServiceError").show();
		$(".deliveryUlClass").remove();
	}else{
		//TPR-1341
		
		$('#unsevisablePin').hide();
		$(".pincodeServiceError").hide();
		$("#checkout-enabled").css("pointer-events","all");
		$("#checkout-enabled").css("cursor","cursor");
		$("#checkout-enabled").css("opacity","1");
		$("#expressCheckoutButtonId").css("pointer-events","all");
		$("#expressCheckoutButtonId").css("cursor","cursor");
		$("#expressCheckoutButtonId").css("opacity","1");
		var deliveryModeJsonObj = JSON.parse(deliveryModeJsonMap);
		var length = Object.keys(deliveryModeJsonObj).length;
		var isStockAvailable="Y";

		if(deliveryModeJsonMap == 'N') {
			console.log("This is NO");
		}	
	}





	for ( var key in deliveryModeJsonObj) {
	var ussId= deliveryModeJsonObj[key].ussid;
	$("#"+ussId+"_qtyul").remove();
	if(deliveryModeJsonObj[key].isServicable==='N'){
		$("#"+ussId).remove();
		var newUi = document.createElement("ul");
		newUi.setAttribute("id", ussId);
		newUi.setAttribute("class", "deliveryUlClass");//TPR-1341
		var newSpan = document.createElement("span");
		var text = document.createTextNode("This item is not serviceable for pincode "+selectedPincode+"!");
		newSpan.appendChild(text);
		newUi.appendChild(newSpan);
		$("#"+ussId+"_li").append(newUi);
		$(".cartItemBlankPincode").hide();//TPR-1341
		 $(".pincodeServiceError").hide();
		console.log("Not servicable");
	}
	else{
		console.log("No Stock");
		$("#"+ussId).remove();
		var newUi = document.createElement("ul");
		newUi.setAttribute("id", ussId);
		// TPR-933 class added
		newUi.setAttribute("class", "success_msg");
		var jsonObj=deliveryModeJsonObj[key].validDeliveryModes;
		
		var inventory=deliveryModeJsonObj[key].stockCount;
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
			newUl.setAttribute("class", 'less-stock');
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
				if(deliveryType==='HD' /*
										 * && parseFloat(inventory) >=
										 * parseFloat(quantityValue)
										 */ ){
					var newLi = document.createElement("li");
					newLi.setAttribute("class", "methodHome");
					var text = document.createTextNode("Home Delivery");
					newLi.appendChild(text);
					newUi.appendChild(newLi);
				}
				else if(deliveryType==='ED'/*
											 * && parseFloat(inventory) >=
											 * parseFloat(quantityValue)
											 */){
					var newLi = document.createElement("li");
					newLi.setAttribute("class", "methodExpress");
					var text = document.createTextNode("Express Delivery");
					newLi.appendChild(text);
					newUi.appendChild(newLi);
				}
				else if(deliveryType==='CNC'/*
											 * && parseFloat(inventory) >=
											 * parseFloat(quantityValue)
											 */){
					var newLi = document.createElement("li");
					newLi.setAttribute("class", "methodClick");
					var text = document.createTextNode("Click and Collect");
					newLi.appendChild(text);
					newUi.appendChild(newLi);
				}
			}
			/** **TISPRM-65 - Cart Page show pincode serviceability msg** */
			/** **Tpr-634 - commented for scope of improvement** */
			/*
			 * var cartMessage = document.createElement("span"); cartMessage.id =
			 * "successPin" cartMessage.style.color = "green"; var message =
			 * document.createTextNode("Yes, it's available. Go ahead.");
			 * cartMessage.appendChild(message); newUi.appendChild(cartMessage);
			 */
			$("#"+ussId+"_li").append(newUi);
			//TPR-1341
			$(".cartItemBlankPincode").hide();
			 $(".pincodeServiceError").hide();
			
		}
	}
	
	$("#defaultPinCodeIds").val(selectedPincode);	
	
	
	
	if(isServicable==='Y' && isStockAvailable==='Y')
	{
		
		// TISBOX-879
		$("#isPincodeServicableId").val('Y');
		$('#checkout-id #checkout-enabled').removeClass('checkout-disabled'); // TISEE-6257
		$('#expresscheckoutid #expressCheckoutButtonId').removeClass('express-checkout-disabled'); // TISEE-6257
		// Code Start TISPRD-437
		var str1 = document.referrer; 
		if(str1.indexOf('checkout') != -1){ // last page checkout
			if($('.global-alerts').length != 0) { // error exist in dom
				var errortext = $(".global-alerts,alert-danger, alert-dismissable").text();
				if( errortext != null && errortext != 'undefined' && errortext != '') {
					  $(".global-alerts").remove();
				} 
			}
		}
		// Code End TISPRD-437
		
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
		$('#checkout-id #checkout-enabled').addClass('checkout-disabled'); // TISEE-6257
		$('#expresscheckoutid #expressCheckoutButtonId').addClass('express-checkout-disabled'); // TISEE-6257
	}
}


//TPR-1786
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
	// TPR-1055
	var selectedPincode=$("#defaultPinCodeIds").val();
	// $("#defaultPinCodeIds").prop('disabled', true);
	$("#pinCodeButtonIds").text("Check");// tpr-1334
	$("#unserviceablepincode").hide();
	$("#unserviceablepincode_tooltip").hide();
	// TPR-1055 ends
	if(selectedPincode!=null && selectedPincode != undefined && selectedPincode!=""){
	
		$.ajax({
	 		url: ACC.config.encodedContextPath + "/cart/checkPincodeServiceability/"+selectedPincode,
	 		type: "GET",
	 		cache: false,
	 		success : function(response) {
	 			// TPR-1055
	 			//Sprint merge issue
	 			var responeStr=response['pincodeData'].split("|");
	 			if(responeStr[0]=="N"){
	 			$("#cartPinCodeAvailable").hide();
	 			$("#AvailableMessage").hide();
 				// $("#unserviceablepincode").html("One or more item(s) are not
				// available at this location. Please remove the item(s) to
				// proceed or try an other pincode?");
 				$("#unserviceablepincode").show();// TPR-1329
 				$("#unserviceablepincode_tooltip").show();
 				 $(".pincodeServiceError").show();
 				$("#pinCodeButtonIds").text("Change Pincode");
	 			}
	 			else{
	 				$(".deliveryUlClass").remove();//TPR-1341
	 				 $(".pincodeServiceError").hide();
	 				$("#unserviceablepincode").hide();
	 				$("#unserviceablepincode_tooltip").hide();
	 				$("#cartPinCodeAvailable").hide();
	 				$("#AvailableMessage").html("Available delivery options for the pincode " +selectedPincode+ " are");
	 				$("#AvailableMessage").show();
	 				$("#pinCodeButtonIds").text("Change Pincode");
	 			}
	 			// TPR-1055 ends
	 			populatePincodeDeliveryMode(response,'pageOnLoad');
	 			$('#defaultPinCodeIdsq').val(selectedPincode);
	 			$("#defaultPinDiv").show();
	 			// $("#changePinDiv").hide();
	 		},
	 		error : function(resp) {
	 			// TISTI-255
	 			// TISPRD-1666 - console replaced with alert and resp print
	 			// alert("Some issues are there with Checkout at this time.
				// Please try later or contact our helpdesk");
	 			console.log(resp);
	 			var errorDetails=JSON.stringify(resp);
	 			console.log("errorDetails 1>> "+errorDetails);
	 			
	 			handleExceptionOnServerSide(errorDetails);
	 			console.log('Some issue occured in checkPincodeServiceability');
	 			$("#isPincodeServicableId").val('N');
	 			// TISPRM-65
	 			$('#defaultPinCodeIdsq').val(selectedPincode);
 	 			$("#defaultPinDiv").show();
 	 			// $("#changePinDiv").hide();
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
	// TISUT-1761
	$("#signinPasswordDiv").hide();
	$("#signinEmailIdDiv").hide();
	if(emailId == null || emailId == ""){
		$("#signinEmailIdDiv").show();
		$("#signinEmailIdDiv").html("Please enter all mandatory fields");	

		validationResult=false;
	}
	else if(!emailPattern.test(emailId)){
		$("#signinEmailIdDiv").show();
		// TISPRO-479 Change the text message
		$("#signinEmailIdDiv").html("Please enter a valid email id");
		validationResult=false;
	}
	else if(password==null || password=="" || password.length==0){
		$("#signinPasswordDiv").show();
		$("#signinPasswordDiv").html("Please enter all mandatory fields");
		validationResult=false;
	}
	
	// Code shoud be commented : No length check is required for Sign In
	
	/*
	 * else if(password.length < 8){ $("#signinPasswordDiv").show();
	 * $("#signinPasswordDiv").html("Invalid username or password");
	 * validationResult=false; }
	 */
	
	else{
		$("#signinPasswordDiv").hide();
	}
	// TISPRO-153
	/*
	 * if(validationResult){ utag.link({ "event_type" : "Login", "link_name" :
	 * "Login" }); }
	 */
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
		$("#signupEmailIdDiv").html("Please enter a valid email id");
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
		$("#signupPasswordDiv").html("Your password should be minimum 8 characters");
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
		$("#signupConfirmPasswordDiv").html("Your password should be minimum 8 characters");
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
		
	// Fix for defect TISEE-3986 : handling special character like #
	if(validationResult)
	{
		// TISPRO-153
		/* utag.link({ "event_type" : "Login", "link_name" : "Login" }); */
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
// TODO

function checkExpressCheckoutPincodeService(buttonType){
	// TISPRM-33
	
	// TISBOX-1631
	// var selectedAddressId= $("#addressListSelectId").val();
	var selectedAddressId= $("#popUpExpAddress input[type='radio']:checked").val();
	selectedAddressId =$.trim(selectedAddressId);
	$("#expressCheckoutAddressSelector").val(selectedAddressId);
	// $("#defaultPinCodeIds").val($("#defaultPinCodeIds").val());
	// alert($("#expressCheckoutAddressSelector").val(selectedAddressId));
	
	if(selectedAddressId.length > 0){
		// TISBOX-882
		$("#expresscheckoutErrorDiv").css("display","none");
		$.ajax({
	 		url: ACC.config.encodedContextPath + "/cart/checkExCheckoutPincodeServiceability/"+selectedAddressId,
	 		type: "GET",
	 		cache: false,
	 		success : function(response) {
	 			populatePincodeDeliveryMode(response,buttonType);
	 			
	 			/*TPR-3446 new starts*/
	 			/*$("#pinCodeDispalyDiv").append('<img src="/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: absolute; right:0;bottom:0; left:0; top:0; margin:auto; height: 30px;">');
	 			$("body").append("<div id='no-click' style='opacity:0.6; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");*/
	 			$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	 			$("body").append('<img src="/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
	 			/*TPR-3446 new ends*/
	 			$("#defaultPinCodeIdsq").val($("#defaultPinCodeIds").val());
	 			// setTimeout(function(){
	 			$("#pinCodeDispalyDiv .spinner").remove();
	 			$("#no-click,.spinner").remove();
	 			// },500);
		 		// $("#changePinDiv").hide();
		 		// $("#defaultPinDiv").show();
	 		},
	 		error : function(resp) {
	 			// TISTI-255
	 			// TISPRD-1666 - console replaced with alert and resp print
	 			// alert("Some issues are there with Checkout at this time.
				// Please try later or contact our helpdesk");
	 			$("#isPincodeServicableId").val('N');
	 			console.log(resp);
	 			var errorDetails=JSON.stringify(resp);
	 			console.log("errorDetails 1>> "+errorDetails);
	 			
	 			handleExceptionOnServerSide(errorDetails);
	 			
	 			/*TPR-3446 new starts*/
	 			/*$("#pinCodeDispalyDiv").append('<img src="/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: absolute; right:0;bottom:0; left:0; top:0; margin:auto; height: 30px;">');
	 			$("body").append("<div id='no-click' style='opacity:0.6; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");*/
	 			$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	 			$("body").append('<img src="/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
	 			/*TPR-3446 new ends*/
	 			
	 			$("#defaultPinCodeIdsq").val($("#defaultPinCodeIds").val());
	 			// setTimeout(function(){
	 			$("#pinCodeDispalyDiv .spinner").remove();
	 			$("#no-click,.spinner").remove();
	 			// },500);
		 		// $("#changePinDiv").hide();
		 		// $("#defaultPinDiv").show();
	 		}

	 	});	 
	}
	else{
		// TISBOX-882
		$("#isPincodeServicableId").val('N');
		$("#expresscheckoutErrorDiv").css("display","block");
	}
}


// Error message handling for Cards
$("#cardNo").focus(function(){
	document.getElementById("cardNoError").innerHTML="";
});
$("#cardNoDc").focus(function(){
	document.getElementById("cardNoErrorDc").innerHTML="";
});
$("#cardNoEmi").focus(function(){
	document.getElementById("cardNoErrorEmi").innerHTML="";
});
$(".name_on_card").focus(function(){
	$("#memberNameError").html('');
	$("#memberNameErrorDc").html('');
	$("#memberNameErrorEmi").html('');
});
$(".card_exp_month").focus(function(){
	$("#expYYError").html('');
	$("#expYYErrorDc").html('');
	$("#expYYErrorEmi").html('');
});
$(".card_exp_year").focus(function(){
	$("#expYYError").html('');
	$("#expYYErrorDc").html('');
	$("#expYYErrorEmi").html('');
});
$(".security_code").focus(function(){
	$("#cvvError").html('');
	$("#cvvErrorDc").html('');
	$("#cvvErrorEmi").html('');
});
$("#make_cc_payment").on("mousedown", function(event){
    $("#make_cc_payment").data("mouseDown", "clicked");

  });

$("#make_dc_payment").on("mousedown", function(event){
    $("#make_dc_payment").data("mouseDown", "clicked");

  });
$( "#cardNo" ).keydown(function() {
	$("#make_cc_payment").data("mouseDown", "notclicked");
});

$( "#cardNoDc" ).keydown(function() {
	$("#make_dc_payment").data("mouseDown", "notclicked");
});


$("#cardNo").blur(function(){
	
	if($("#cardNo").val()!="")
	{
		if($("#make_cc_payment").data("mouseDown") != "clicked"){
			// Check if session is timed out before validating card
			if(isSessionActive()){
				validateCardNo("none");
			}
			else{
				redirectToCheckoutLogin();
			}
		}
	} 

});


$("#cardNoDc").blur(function(){
	
	if($("#cardNoDc").val()!="")
	{
		if($("#make_dc_payment").data("mouseDown") != "clicked"){
			//Check if session is timed out before validating card
			if(isSessionActive()){
				validateDebitCardNo("none");
			}
			else{
				redirectToCheckoutLogin();
			}
		}
	} else {
		//TODO
		 document.getElementById("cardNoError").innerHTML="Please enter a valid card number ";

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


$("#cardNoDc").keyup(function() {
    if (!this.value) {
    	//TODO
    	document.getElementById("cardNoError").innerHTML="";
    }
});

$(".name_on_card").keyup(function() {
    if (!this.value) {
    	document.getElementById("memberNameErrorDc").innerHTML="";
    	document.getElementById("memberNameError").innerHTML="";
    }
});
$(".security_code").keyup(function() {
	if (!this.value) {
		document.getElementById("cvvErrorDc").innerHTML="";
    	document.getElementById("cvvError").innerHTML="";
    }
});
$(".card_exp_month").keyup(function() {
    if (this.value=="month" && $(".card_exp_year").val()=="year") {
    	document.getElementById("expYYErrorDc").innerHTML="";
    	document.getElementById("expYYError").innerHTML="";
    }
});
$(".card_exp_year").keyup(function() {
    if (this.value=="year" && $(".card_exp_month").val()=="month") {
    	document.getElementById("expYYErrorDc").innerHTML="";
    	document.getElementById("expYYError").innerHTML="";
    }
});
// Error message handling for cards ends


// Calling isNumber on keypress
$( "#cardNo" ).keypress(function(evt){
  return isNumber(evt);
});


$( "#cardNoDc" ).keypress(function(evt){
	  return isNumber(evt);
	});

$( ".security_code" ).keypress(function(evt){
	  return isNumber(evt);
});
// Calling isNumber ends


// OnChange for Billing address
$( "#sameAsShipping" ).change(function(){
	populateAddress();
});


//OnChange for Billing Emi address
$( "#sameAsShippingEmi" ).change(function(){
	populateAddressEmi();
});
// Onchange ends


// New Form Validation

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

// NOT Needed
/*
 * function validateLastNameOnAddress(name, errorHandle) { var regex = new
 * RegExp(/^[a-zA-Z ]+$/); if (!regex.test(name)) { errorHandle.innerHTML =
 * "Only alphabets and spaces allowed."; return false; } else if(name==""){
 * errorHandle.innerHTML = "Please enter a valid name."; return false; }
 * errorHandle.innerHTML = ""; return true; }
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
		errorHandle.innerHTML = "Please enter a address line 3.";
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
	//var regex = new RegExp(/^[a-zA-Z ]+$/);
	var regex = new RegExp(/^[a-zA-Z]+([\s]?[a-zA-Z]+)*$/);
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

function validateCityEmi() {
	var name=$("#cityEmi").val();
	var errorHandle=document.getElementById("cityErrorEmi");
	//var regex = new RegExp(/^[a-zA-Z ]+$/);
	var regex = new RegExp(/^[a-zA-Z]+([\s]?[a-zA-Z]+)*$/);
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

function validateStateEmi() {
	var name=$("#stateEmi").val();
	var errorHandle=document.getElementById("stateErrorEmi");
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

$("#cityEmi").keyup(function() {
    if (!this.value) {
    	document.getElementById("cityErrorEmi").innerHTML="";
    }
});

$("#city").focus(function(){
	document.getElementById("cityError").innerHTML="";
});

$("#cityEmi").focus(function(){
	document.getElementById("cityErrorEmi").innerHTML="";
});

$("#state").keyup(function() {
    if (!this.value) {
    	document.getElementById("stateError").innerHTML="";
    }
});

$("#state").focus(function(){
	document.getElementById("stateError").innerHTML="";
});

$("#stateEmi").keyup(function() {
    if (!this.value) {
    	document.getElementById("stateErrorEmi").innerHTML="";
    }
});

$("#stateEmi").focus(function(){
	document.getElementById("stateErrorEmi").innerHTML="";
});


function updateCart(formId){
	window.sessionStorage.setItem("qtyUpdate","true");
	var entryNumber = formId.split("_");
	var form = $('#updateCartForm' + entryNumber[1]);
	form.submit();
}


function expressbutton()
{
	//TPR-683
	if(typeof utag !="undefined")
	{
		utag.link(
		{"link_text": "mybag_express_checkout" , "event_type" : "mybag_express_checkout"}
		);
	}
	
	//alert(selectedAddress);
	//TISPRM-33
	// var addressList= $("#addressListSelectId").val();
	var addressList= $("#popUpExpAddress input[type='radio']:checked").val();
	var selectedAddressId =$.trim(addressList);
	$("#expressCheckoutAddressSelector").val(selectedAddressId);
 	
	// TISBOX-882
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
	 			// TISPRM-33

	 			$("#defaultPinCodeIdsq").val($("#defaultPinCodeIds").val());
	 			
		 		// $("#changePinDiv").hide();
		 		// $("#defaultPinDiv").show();
	 		},
	 		error : function(resp) {
	 			// TISTI-255
	 			// TISPRD-1666 - console replaced with alert and resp print
	 			// alert("Some issues are there with Checkout at this time.
				// Please try later or contact our helpdesk");
	 			$("#isPincodeServicableId").val('N');
	 			console.log(resp);
	 			var errorDetails=JSON.stringify(resp);
	 			console.log("errorDetails 1>> "+errorDetails);
	 			handleExceptionOnServerSide(errorDetails);
	 			// TISPRM-33

	 			$("#defaultPinCodeIdsq").val($("#defaultPinCodeIds").val());
	 			
		 		// $("#changePinDiv").hide();
		 		// $("#defaultPinDiv").show();
	 		}
	 	});	 
	}
	
	// return false;
}



// COD
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
	$("#no-click,.spinner").remove();
	$(".make_payment").removeAttr('disabled');

}

// Coupon
$("#couponSubmitButton").click(function(){
	if(!isSessionActive()){
		redirectToCheckoutLogin();
	}else{
	$(this).prop('disabled', true);
	$(this).css("opacity","0.5");
	$("#priceCouponError, #emptyCouponError, #appliedCouponError, " +
			"#invalidCouponError, #expiredCouponError, #issueCouponError, " +
			"#notApplicableCouponError, #notReservableCouponError, #freebieCouponError, #userInvalidCouponError, #firstPurchaseOfferError").css("display","none");
	if($("#couponFieldId").val()==""){
		$("#emptyCouponError").css("display","block");	
		// document.getElementById("couponError").innerHTML="Please enter a
		// Coupon Code";
		$(this).prop('disabled', false);
		$(this).css("opacity","1");
	}
	else if($("#couponFieldId").val()!="" && $('#couponFieldId').prop('readonly') == true)
	{   
		$("#appliedCouponError").css("display","block");	
		// document.getElementById("couponError").innerHTML="Coupon is already
		// applied";
		$(this).prop('disabled', false);
		$(this).css("opacity","1");
	}
	else{
		var couponCode=$("#couponFieldId").val();
		var paymentMode=$("#paymentMode").val();
		var guid=$("#guid").val();
		/*start changes for INC_11738*/
		$("body").append("<div id='no-click' style='opacity:0.5; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
		$("body").append('<img src="'+staticHost+'/_ui/responsive/common/images/spinner.gif" class="spinner" style="position: fixed; left: 45%;top:45%; height: 30px;z-index: 10000">');
		/*end changes for INC_11738*/
		$.ajax({
	 		url: ACC.config.encodedContextPath + "/checkout/multi/coupon/redeem",
	 		type: "GET",
	 		cache: false,
	 		data: { 'couponCode' : couponCode , 'paymentMode' : paymentMode , 'bankNameSelected' : bankNameSelected , 'guid' : guid},
	 		success : function(response) {
	 			$("#no-click,.spinner").remove(); //add for INC_11738
	 			document.getElementById("totalWithConvField").innerHTML=response.totalPrice.formattedValue;
	 			$("#codAmount").text(response.totalPrice.formattedValue);
	 			if(response.redeemErrorMsg!=null){
	 				if(response.redeemErrorMsg=="Price_exceeded")
	 				{
	 					$("#priceCouponError").css("display","block");
	 				}
	 				else if(response.redeemErrorMsg=="Invalid")
	 				{
	 					$("#invalidCouponError").css("display","block");
	 				}
	 				else if(response.redeemErrorMsg=="Expired")
	 				{
	 					$("#expiredCouponError").css("display","block");
	 				}
	 				else if(response.redeemErrorMsg=="Issue")
	 				{
	 					$("#issueCouponError").css("display","block");
	 				}
	 				else if(response.redeemErrorMsg=="Not_Applicable")
	 				{
	 					$("#notApplicableCouponError").css("display","block");
	 				}
	 				else if(response.redeemErrorMsg=="Not_Reservable")
	 				{
	 					$("#notReservableCouponError").css("display","block");
	 				}
	 				else if(response.redeemErrorMsg=="Freebie")
	 				{
	 					$("#freebieCouponError").css("display","block");
	 				}
	 				else if(response.redeemErrorMsg=="User_Invalid")
	 				{
	 					$("#userInvalidCouponError").css("display","block");
	 				}
	 				// TPR-1075
	 				else if(response.redeemErrorMsg=="First_Purchase_User_Invalid")
	 				{
	 					$("#firstPurchaseOfferError").css("display","block");
	 				}
	 				//TPR-658
	 				onSubmitAnalytics("invalid_coupon");
	 				// $("#couponError").css("display","block");
	 				// document.getElementById("couponError").innerHTML=response.redeemErrorMsg;
	 			}
	 			else{
		 			if(response.couponRedeemed==true){
		 				couponApplied=true;
		 			}
		 			if(couponApplied==true){
		 				if(response.couponDiscount.doubleValue>0)
			 			{
			 				$("#couponApplied").css("display","block");
			 				document.getElementById("couponValue").innerHTML=response.couponDiscount.formattedValue;
			 				//$("#couponFieldId").attr('disabled','disabled');
			 				$('#couponFieldId').attr('readonly', true);
			 				$("#couponMessage").html("Coupon <b>"+couponCode+"</b> is applied successfully");
			 				$('#couponMessage').show();
			 				$('#couponMessage').delay(2000).fadeOut('slow');
			 				setTimeout(function(){ $("#couponMessage").html(""); }, 2500);
			 				//TPR-658
			 				onSubmitAnalytics("success");
			 			}
		 				else
		 				{
		 					$("#issueCouponError").css("display","block");
		 				}
		 			}
	 			}
	 			$("#couponSubmitButton").prop('disabled', false);
	 			$("#couponSubmitButton").css("opacity","1");
	 		},
	 		error : function(resp) {
	 			$("#couponSubmitButton").prop('disabled', false);
	 			$("#couponSubmitButton").css("opacity","1");
	 			$("#no-click,.spinner").remove(); //changes for INC_11738
	 		}
	 	});	 
	}
	}// End of session checking
});

//TPR-658 START
function onSubmitAnalytics(msg){
	var couponCode = $('#couponFieldId').val().toLowerCase().replace(/  +/g, ' ').replace(/ /g,"_").replace(/['"]/g,"");
	utag.link({
		link_obj: this,
		link_text: 'apply_coupon_'+msg ,
		event_type : 'apply_coupon',
		coupon_code : couponCode
	});
}
// TPR-658 END


$("#couponFieldId").focus(function(){
	// $("#couponError").css("display","none");
	$("#priceCouponError, #emptyCouponError, #appliedCouponError, #invalidCouponError," +
			" #expiredCouponError, #issueCouponError, #notApplicableCouponError," +
			" #notReservableCouponError, #freebieCouponError, #userInvalidCouponError, #firstPurchaseOfferError").css("display","none");
});


$(".remove-coupon-button").click(function(){
	var couponCode=$("#couponFieldId").val();
	var guid=$("#guid").val();
	$.ajax({
 		url: ACC.config.encodedContextPath + "/checkout/multi/coupon/release",
 		type: "GET",
 		cache: false,
 		data: { 'couponCode' : couponCode , 'guid' : guid},
 		success : function(response) {
 			document.getElementById("totalWithConvField").innerHTML=response.totalPrice.formattedValue;
 			$("#codAmount").text(response.totalPrice.formattedValue);
 			// alert(response.totalPrice.formattedValue);
 			if(response.couponReleased==true){
 				couponApplied=true;
 			}
 			if(couponApplied==true){
 				$("#couponApplied, #priceCouponError, #emptyCouponError, #appliedCouponError, #invalidCouponError," +
 						" #expiredCouponError, #issueCouponError, #freebieCouponError, #userInvalidCouponError, #firstPurchaseOfferError").css("display","none");
 				document.getElementById("couponValue").innerHTML=response.couponDiscount.formattedValue;
 				// $("#couponFieldId").attr('disabled','enabled');
 				$('#couponFieldId').attr('readonly', false);
 				var selection = $("#voucherDisplaySelection").val();
 				$("#couponFieldId").val(selection);
 				// $("#couponFieldId").val("");
 				$("#couponMessage").html("Coupon <b>"+couponCode+"</b> has been removed");
 				$('#couponMessage').show();
 				$('#couponMessage').delay(2000).fadeOut('slow');
 				setTimeout(function(){ $("#couponMessage").html(""); }, 2500); 			}
 			
 				$("#couponSubmitButton").prop('disabled', false);
 				$("#couponSubmitButton").css("opacity","1");
 		},
 		error : function(resp) {
 		}
 	});	 
});


$(document).ready(function(){
	$("#off-bag").show();
	console.log($("*[data-id=savedCCard]").is(":checked"));
	if($('#couponFieldId').prop('readonly') == false)
	{
		var selection = $("#voucherDisplaySelection").val();
		$("#couponFieldId").val(selection);
	}

	if ($("#checkoutPageName").val() == "Select Address"){
		$(" body.page-multiStepCheckoutSummaryPage .right-block.shipping").css("visibility","hidden");
		$(" body.page-multiStepCheckoutSummaryPage .right-block.shipping .subtotals.top.block.summary-info").css("display","none");
		
	}
	if ($("#checkoutPageName").val() == "Payment Options"){
		$(" body.page-multiStepCheckoutSummaryPage .progress-barcheck .step-done span").addClass("paymentStepDone");
		var alert_top= $("header").outerHeight() + $(".container-address.nav").outerHeight(); 
		$(".alert-danger").css({
			  position : "fixed",
			  width: "100%",
			  margin:"0px",
			  top: alert_top
		});
		$(".alert-danger").css("z-index","101");
	}
	
	$("li.price").each(function(){
		if($(this).find(".off-bag").css("display") === "block"){
			$(this).find("span.delSeat").addClass("delAction");
		}
		else{
			$(this).find("span.delSeat").removeClass("delAction");
		}
	});
	
});
$("#voucherDisplaySelection").change(function(){
	if($('#couponFieldId').prop('readonly') == false)
	{
		var selection = $(this).val();
		$("#couponFieldId").val(selection);
	}
});


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
				            "link_text": 'Final Checkout',
				            "event_type": 'PayNow',
				            "payment_method": "" + payment_type,
				            "product_id": utag.data.product_id
		
				        });
		        		
		        	}else{
			        	utag.link({
				            "link_text": 'Final Checkout',
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


// ADD Server side error track // TISPRD-1666

function handleExceptionOnServerSide(errorDetails){
	 setTimeout( function(){
			$.ajax({
		 		url: ACC.config.encodedContextPath + "/cart/networkError",
		 		type: "GET",
		 		cache: false,
		 		data:  { 'errorDetails' : errorDetails},
		 		success : function(errorResponse) {
		 			
		 			console.log(errorResponse);
		 		},
		 		error : function(errorResp) {
		 			console.log(errorResp);
		 		}
		 	});
			 },
			1000);
		
}
// Start script moved from cartItems.tag

function openPopFromCart(entry,productCode,ussid) {
	
	// var productCode = $("#product").val();
	var requiredUrl = ACC.config.encodedContextPath + "/p"+"-viewWishlistsInPDP";
	var dataString = 'productCode=' + productCode+ '&ussid=' + ussid;//modified for ussid

	var entryNo = $("#entryNo").val(entry);
		$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(data) {
			if(data==null)
			{
				$("#wishListNonLoggedInId").show();
				$("#wishListDetailsId").hide();
			}
			else if (data == "" || data == []) {
				loadDefaultWishLstForCart(productCode,ussid);
			}
			else
			{
				LoadWishListsFromCart(data, productCode,ussid);	
			}	
			
		},
		error : function(xhr, status, error) {
			$("#wishListNonLoggedInId").show();
			$("#wishListDetailsId").hide();
		}
	});
}

function loadDefaultWishLstForCart(productCode,ussid) {
		
	var wishListContent = "";
	var wishName = $("#defaultWishId").text();
	$("#wishListNonLoggedInId").hide();
	$("#wishListDetailsId").show();

	wishListContent = wishListContent
			+ "<tr><td><input type='text' id='defaultWishName' value='"
			+ wishName + "'/></td></td></tr>";
	$("#wishlistTbodyId").html(wishListContent); 
	$('#selectedProductCode').attr('value',productCode);
	$('#proUssid').attr('value',ussid);
}


// Added
function addToWishlistForCart(ussid,productCode,alreadyAddedWlName)
{
	var wishName = "";
	var sizeSelected=true;
	
	if (wishListList == "") {
		wishName = $("#defaultWishName").val();
	} else {
		wishName = wishListList[$("#hidWishlist").val()];
	}
	
	
	if(wishName==""){
		var msg=$('#wishlistnotblank').text();
		$('#addedMessage').show();
		$('#addedMessage').html(msg);
		return false;
	}
    if(wishName==undefined||wishName==null){
    	if(alreadyAddedWlName!=undefined || alreadyAddedWlName!=""){
    		if(alreadyAddedWlName=="[]"){
    			$("#wishlistErrorId").html("Please select a wishlist");
    		}
    		else{
    			alreadyAddedWlName=alreadyAddedWlName.replace("[","");
    			alreadyAddedWlName=alreadyAddedWlName.replace("]","");
    			$("#wishlistErrorId").html("Product already added in your wishlist "+alreadyAddedWlName);
    		}
    		$("#wishlistErrorId").css("display","block");
    	}
    	return false;
    }
   	
	$("#wishlistErrorId").css("display","none");
    
    
	var requiredUrl = ACC.config.encodedContextPath + "/p"+ "-addToWishListInPDP";
	var dataString = 'wish='+wishName 
				    +'&product='+ productCode
				    +'&ussid='+ ussid 
				    +'&sizeSelected='+ sizeSelected;

	var entryNo = $("#entryNo").val();
	
	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(data) {
			if (data == true) {
				
				$("#radio_" + $("#hidWishlist").val()).prop("disabled", true);
				
				/*TPR-656*/
					utag.link({
						link_obj: this, 
						link_text: 'add_to_wishlist' , 
						event_type : 'add_to_wishlist', 
						product_sku_wishlist : productCode
					});
				/*TPR-656 Ends*/
				
				localStorage.setItem("movedToWishlist_msgFromCart", "Y");
				
				
/*
 * var msg=$('#movedToWishlistFromCart').text();
 * $('#movedToWishlist_Cart').show(); $('#movedToWishlist_Cart').html(msg);
 * setTimeout(function() { $("#movedToWishlist_Cart").fadeOut().empty(); },
 * 1500);
 */
				
				
		/*
		 * var msg=$('#wishlistSuccess').text() + wishName;
		 * $('#addedMessage').show(); $('#addedMessage').html(msg);
		 * setTimeout(function() { $("#addedMessage").fadeOut().empty(); },
		 * 5000);
		 */
				removefromCart(entryNo,wishName);
			}
		},
	})
	
	$('a.wishlist#wishlist').popover('hide');
} 
// End

function removefromCart(entryNo,wishName)
{
	$.ajax({
		contentType : "application/json; charset=utf-8",
		url :  ACC.config.encodedContextPath+"/cart/removeFromMinicart?entryNumber="+entryNo,
		dataType : "json",
		success : function(data) {
			
			var productName = $("#moveEntry_"+entryNo).parents(".item").find(".desktop .product-name > a").text();
			$("#moveEntry_"+entryNo).parents(".item").hide().empty();
			/*
			 * $(".product-block > li.header").append('<span>'+productName+'
			 * Moved to '+wishName+'</span>');
			 */
			
			// $('.moveToWishlistMsg').html("Item successfully moved to
			// "+wishName);
			// $('.moveToWishlistMsg').show();
			setTimeout(function() {
				$(".product-block > li.header > span").fadeOut(6000).remove();
				// $(".moveToWishlistMsg").fadeOut().empty();
				}, 6000);
			location.reload();
			
			
		},
		complete:function(){
			forceUpdateHeader();
		},
		error:function(data){
			alert("error");
		}
		
	});
	
}

function gotoLogin() {
	window.open(ACC.config.encodedContextPath + "/login", "_self");
}

var wishListList = [];

function LoadWishListsFromCart(data, productCode,ussid) {
    
	// modified for ussid
	
	// var ussid = $("#ussid").val()
	var addedWlList_cart = [];
	var wishListContent = "";
	var wishName = "";
	$this = this;
	$("#wishListNonLoggedInId").hide();
	$("#wishListDetailsId").show();

	for ( var i in data) {
		var index = -1;
		var checkExistingUssidInWishList = false;
		var wishList = data[i];
		wishName = wishList['particularWishlistName'];
		wishListList[i] = wishName;
		var entries = wishList['ussidEntries'];
		for ( var j in entries) {
			var entry = entries[j];
			if (entry == ussid) {
				
				checkExistingUssidInWishList = true;
				break;

			}
		}
		if (checkExistingUssidInWishList) {
			index++;
            
			wishListContent = wishListContent
					+ "<tr class='d0'><td ><input type='radio' name='wishlistradio' id='radio_"
					+ i
					+ "' style='display: none' onclick='selectWishlist("
					+ i + ")' disabled><label for='radio_"
					+ i + "'>"+wishName+"</label></td></tr>";
			addedWlList_cart.push(wishName);
		} else {
			index++;
		  
			wishListContent = wishListContent
					+ "<tr><td><input type='radio' name='wishlistradio' id='radio_"
					+ i
					+ "' style='display: none' onclick='selectWishlist("
					+ i + ")'><label for='radio_"
					+ i + "'>"+wishName+"</label></td></tr>";
		}
		$("#alreadyAddedWlName_cart").val(JSON.stringify(addedWlList_cart));
	}

	$("#wishlistTbodyId").html(wishListContent);
	$('#selectedProductCode').attr('value',productCode);
	$('#proUssid').attr('value',ussid);

}

function selectWishlist(i,productCode, ussid)
{
	$("#hidWishlist").val(i);	
}

// adding product to a wishlist
function addToWishlistFromCart() {
	var productCode = $("#product").val();
	var ussid = $("#ussid").val();
	alert("Into addToWishlistFromCart>>>"+ussid);
	var wishName =wishListList[$("#hidWishlist").val()] ;
	var requiredUrl = ACC.config.encodedContextPath + "/cart"+"/addToWishListFromCart";
	var dataString = 'wish=' + wishName + '&product=' + productCode+ '&ussid=' + ussid;
	$.ajax({
		contentType : "application/json; charset=utf-8",
		url : requiredUrl,
		data : dataString,
		dataType : "json",
		success : function(data) {
			
			if (data == true) {				
				alert("Product Added into wishlist "+wishName);
				$("#radio_" + $("#hidWishlist").val()).prop("disabled", true);
				window.location.reload();
			}
		},
	})
}
// End
// Check Session Active
function isSessionActive(){
	var active=false;
	$.ajax({
		url: ACC.config.encodedContextPath + "/checkout/multi/payment-method/checkSessionActive",
		type: "GET",
		cache: false,
		async:false,
		success : function(response) {
			if(response){
				active=true;
			}
			
			
		},
		error:function(response){
			console.log("Error occured");
			}
		});
	return active;
	
}
// redirect to checkout login page
function redirectToCheckoutLogin(){
	window.location=ACC.config.encodedContextPath + "/checkout/multi/checkoutlogin/login";
}
 // TISPRM-33
function pinCodeDiv(){
	// TISPRM-65
		$("#changePinDiv").show();
		$("#defaultPinDiv").hide();
		$("#defaultPinCodeIds").val("");
		// $(".pincodeServiceError").text("");
		// $(".less-stock").text("");
		// $("#successPin").text("");
	}

// MY BAG Changes TPR-634
/*
 * $(document).mouseup(function (e) { var container =
 * $(".modal-content.content");
 * 
 * if (!container.is(e.target) && container.has(e.target).length === 0 &&
 * container.css("opacity") === "1") {
 * checkExpressCheckoutPincodeService('typeExpressCheckoutDD');
 * //$("#defaultPinDiv").show(); //$("#changePinDiv").hide(); } });
 */
$('#popUpExpAddress').on('hidden.bs.modal', function () {
	 checkExpressCheckoutPincodeService('typeExpressCheckoutDD');
});


//TPR-629
$(".juspayCloseButton").on("click", function(){
	$("#juspayconnErrorDiv").hide();
});

$(document).on("click",".radio input[type='radio']",function(){
	$(".radio input[type='radio']").removeAttr("checked");
	$(this).prop("checked","true");
	
});

$("*[data-id=newCCard]").click(function(){
	$(".proceed-button").each(function(){
		$(this).hide();
	});
	$("#make_cc_payment_up").show();
	$(".card_cvvErrorSavedCard").hide();
	$("#card_form").find("input[type=password]").val("");
	//$("#savedCreditCard").find(".error-message").hide();
});

$("*[data-id=savedCCard]").change(function(){
	$(".proceed-button").each(function(){
		$(this).hide();
	});
	$("#make_saved_cc_payment_up").show();
	$("#cardNoError").empty();
	$("#memberNameError").empty();
	$("#expYYError").empty();
	$("#cvvError").empty();
	//$("#payment_form").find(".error-message").hide();
});

$("*[data-id=newDCard]").click(function(){
	$(".proceed-button").each(function(){
		$(this).hide();
	});
	$("#make_dc_payment_up").show();
	$(".card_cvvErrorSavedCard").hide();
	$("#card_form_saved_debit").find("input[type=password]").val("");
	//$("#savedDebitCard").find(".error-message").hide();
});

$("*[data-id=savedDCard]").change(function(){
	$(".proceed-button").each(function(){
		$(this).hide();
	});
	$("#make_saved_dc_payment_up").show();
	$("#cardNoErrorDc").empty();
	$("#memberNameErrorDc").empty();
	$("#expYYErrorDc").empty();
	$("#cvvErrorDc").empty();
	//$("#debit_payment_form").find(".error-message").hide();
});

$("#payment_form").find("input[type=text]").click(function(){
	$("*[data-id=newCCard]").prop("checked","true");
	$("*[data-id=newCCard]").trigger("click");
	$("#cvvErrorSavedCard").hide();
	$("#card_form").find("input[type=password]").val("");
	//$("#savedCreditCard").find(".error-message").hide();
});

$("#payment_form").find("select").click(function(){
	$("*[data-id=newCCard]").prop("checked","true");
	$("*[data-id=newCCard]").trigger("click");
	$("#card_form").find("input[type=password]").val("");
});

$("#debit_payment_form").find("input[type=text]").click(function(){
	$("*[data-id=newDCard]").prop("checked","true");
	$("*[data-id=newDCard]").trigger("click");
	$(".card_cvvErrorSavedCard").hide();
	$("#card_form_saved_debit").find("input[type=password]").val("");
});

$("#debit_payment_form").find("select").click(function(){
	$("*[data-id=newDCard]").prop("checked","true");
	$("*[data-id=newDCard]").trigger("click");
	$(".card_cvvErrorSavedCard").hide();
	$("#card_form_saved_debit").find("input[type=password]").val("");
});

$("#savedCreditCard").find("input[type=password]").click(function(){
	$("#make_cc_payment_up").show();
	$("#cardNoError").empty();
	$("#memberNameError").empty();
	$("#expYYError").empty();
	$("#cvvError").empty();
	$(".proceed-button").each(function(){
		$(this).hide();
	});
	$("#make_saved_cc_payment_up").show();
	//$("#payment_form").find(".error-message").hide();
});

$("#savedDebitCard").find("input[type=password]").click(function(){
	$("#make_cc_payment_up").show();
	$("#cardNoErrorDc").empty();
	$("#memberNameErrorDc").empty();
	$("#expYYErrorDc").empty();
	$("#cvvErrorDc").empty();
	$(".proceed-button").each(function(){
		$(this).hide();
	});
	$("#make_saved_dc_payment_up").show();
	//$("#debit_payment_form").find(".error-message").hide();
});
	//TPR-1055
	$("#defaultPinCodeIds").click(function(){
		$(this).css("color","black"); //TPR-1470
		$("#unserviceablepincode").hide();
		$("#unserviceablepincode_tooltip").hide();
		$(".deliveryUlClass").remove();//TPR-1341
		$("#cartPinCodeAvailable").show();
		 $( "#error-Id").hide();
		 $( "#error-Id_tooltip").hide();
		 $("#emptyId").hide();
		 $("#emptyId_tooltip").hide();
		 $(".pincodeServiceError").hide();
		 //TPR-1341
		 $(".cartItemBlankPincode").show();
		 $(".delivery ul.success_msg").hide();
		if($("#pinCodeButtonIds").text() == 'Change Pincode'){
			$("#pinCodeButtonIds").text("Check");
			$("#AvailableMessage").hide();
		}
		
	});
	// TPR-1055 ends
//TPR-665

function teliumTrack(){
	utag.link(
	{"link_text": "pay_terms_conditions_click" , "event_type" : "terms_conditions_click"}
	);
}
function updateMobileNo(){
	$("#otpMobileNUMField").val('');
	$("#otpMobileNUMField").focus();    
}
