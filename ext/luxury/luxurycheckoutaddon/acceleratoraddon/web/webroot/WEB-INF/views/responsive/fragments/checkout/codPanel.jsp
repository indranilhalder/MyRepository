<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script src="https://www.google.com/recaptcha/api.js"></script>

<p class="title">Pay using Cash-on-Delivery</p>
<p>Verify Order</p>
<%-- <p>
	<spring:theme
		code="checkout.multi.paymentMethod.addPaymentDetails.cod.desc" />
</p>
<div class="amtPayable">
	<h4>
		<spring:theme
			code="checkout.multi.paymentMethod.addPaymentDetails.cod.amtPayable" />
		&nbsp;<span id="codAmount"></span>
	</h4>--%>
	<%-- &nbsp;<span id="convChargeMessage"><spring:theme
			code="checkout.multi.paymentMethod.addPaymentDetails.cod.convChargeMsg" /></span> --%>
<!-- </div>  -->
<div id="sendOTPNumber" class="cont-del">
	<input type="hidden" id="codEligible" value="${codEligible}" />
	<div class="description">
		<spring:theme code="" />
	</div>
<%-- 	<label name="Enter OTP" class="cod-mob-label"><spring:theme
			code="checkout.multi.paymentMethod.addPaymentDetails.mobileNo" /></label> <input
		type="text" id="mobilePrefix" name="mobilePrefix" value="+91"
		disabled="disabled" /><input type="text" id="otpMobileNUMField"
		name="otpNUM" value="${cellNo}" maxlength="10" /> --%>
	<%-- <div id="mobileNoError" class="error-message">
		<spring:theme
			code="checkout.multi.paymentMethod.addPaymentDetails.mobileNoErrorMessage" />
	</div> --%>
	<%-- <p class="process">
		<spring:theme
			code="checkout.multi.paymentMethod.addPaymentDetails.mobileNoMessage" />
		&nbsp;<a
			href="${request.contextPath}/checkout/multi/payment-method/add?Id=updateItHereLink"
			class="cod-link"><spring:theme
				code="checkout.multi.paymentMethod.cod.updateItHereLink" /></a>
	</p> --%>
	<!-- INC_11794 -->
<%-- 	<p class="process">
		<spring:theme
			code="checkout.multi.paymentMethod.addPaymentDetails.mobileNoMessage" />
		 <a class="cod-link" onclick="updateMobileNo()"><spring:theme
				code="checkout.multi.paymentMethod.cod.updateItHereLink" /></a>
	</p>  --%>

<%-- 	<div id="sendOTPButton">

		<button type="button" class="positive right cod-otp-button"
			onclick="mobileBlacklist()">
			<spring:theme
				code="checkout.multi.paymentMethod.addPaymentDetails.sendOTP"
				text="Verify Number" />
		</button>
		<div id="resendOTPMessage" class="description">
			<spring:theme
				code="checkout.multi.paymentMethod.addPaymentDetails.codResendMessage" />
		</div>
	</div> --%>
	<div id="OTPGenerationErrorMessage" class="error-message" style="display:none">
		<spring:theme
			code="checkout.multi.paymentMethod.addPaymentDetails.codMessage" />
	</div>
</div>
<div id="enterOTP" class="cont-del">
	<%-- <label name="Enter OTP"><spring:theme
			code="checkout.multi.paymentMethod.CODPayment.enterOTP"
			text="Enter OTP:&nbsp;" /> --%> <input type="text" id="otpNUMField" placeholder="OTP"
		name="otpNUM" onfocus="hideErrorMsg()" autocomplete="off" /> <!-- </label> -->
</div>
<div class="g-recaptcha" data-sitekey="${grecaptchaKey}"></div>
<div id="captchaError"></div>

<button type="button" class="make_payment payment-button btn-lg btn-primary" id="paymentButtonId">		
									<div id="submitPaymentFormCODButton">	
										<spring:theme code="checkout.multi.paymentMethod.codContinue" />
									</div>
								</button> 
<script type="text/javascript" >
(function($) {
	$.ajax({
		url: ACC.config.encodedContextPath + "/login/captcha/widget/recaptcha",
		type: 'GET',
		cache: false,
		success: function (html)
		{			
			if ($(html) != [])
			{
				//if($('#captchaCount').val() >= 5)
				{
//					alert("Please validate with captcha !")
					$("#recaptchaWidgetForLogin").show();
					$("#captchaError").empty();

					$("#paymentButtonId").click(function(){	
						if(!$("#g-recaptcha-response").val()){
						//	alert("TWO=>"+$("#g-recaptcha-response").val());
							$('#captchaError').html("<font color='red'>Please verify that you are not a robot! </font>")
							return false;
						}
						else{
							submitCODForm();
							return true;
						}
					});
				}
			}
		}
	});
})(jQuery);
</script>