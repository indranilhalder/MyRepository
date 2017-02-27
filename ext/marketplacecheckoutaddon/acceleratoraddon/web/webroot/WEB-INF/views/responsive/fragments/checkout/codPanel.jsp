<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<p class="title"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cod.desc" /></p>
<p class="desc"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cod.desc1" /></p>
<p class="desc-ext"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.cod.desc2" /></p>
<!-- commented as part of UF-203 -->
<!-- <div class="amtPayable"> -->
	<%-- <h4>
		<spring:theme
			code="checkout.multi.paymentMethod.addPaymentDetails.cod.amtPayable" />
		&nbsp;<span id="codAmount"></span>
	</h4> --%>
	<%-- &nbsp;<span id="convChargeMessage"><spring:theme
			code="checkout.multi.paymentMethod.addPaymentDetails.cod.convChargeMsg" /></span> --%>
<!-- </div> -->
<!-- commented as part of UF-203 -->
<div id="sendOTPNumber" class="cont-del">
	<input type="hidden" id="codEligible" value="${codEligible}" />
	<div class="description">
		<spring:theme code="" />
	</div>
	<label name="Enter OTP" class="cod-mob-label"><spring:theme
			code="checkout.multi.paymentMethod.addPaymentDetails.mobileNo" /></label> <input
		type="text" id="mobilePrefix" name="mobilePrefix" value="+91"
		disabled="disabled" /><input type="text" id="otpMobileNUMField"
		name="otpNUM" value="${cellNo}" maxlength="10" />
	<div id="mobileNoError" class="error-message">
		<spring:theme
			code="checkout.multi.paymentMethod.addPaymentDetails.mobileNoErrorMessage" />
	</div>
	<div id="otpSentMessage" class="error-message payment-notification"><spring:theme code="checkout.multi.paymentMethod.addPaymentDetails.otpSentMessage"/>
									</div>
	<%-- <p class="process">
		<spring:theme
			code="checkout.multi.paymentMethod.addPaymentDetails.mobileNoMessage" />
		&nbsp;<a
			href="${request.contextPath}/checkout/multi/payment-method/add?Id=updateItHereLink"
			class="cod-link"><spring:theme
				code="checkout.multi.paymentMethod.cod.updateItHereLink" /></a>
	</p> --%>
	<!-- INC_11794 -->
	<!-- commented as part of UF-203 -->
<%-- 	<p class="process"> 
		<spring:theme
			code="checkout.multi.paymentMethod.addPaymentDetails.mobileNoMessage" />
		 <a class="cod-link" onclick="updateMobileNo()"><spring:theme
				code="checkout.multi.paymentMethod.cod.updateItHereLink" /></a>
	</p>  --%>
	<!-- commented as part of UF-203 -->

	<div id="sendOTPButton">

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
	</div>
	<div id="OTPGenerationErrorMessage" class="error-message">
		<spring:theme
			code="checkout.multi.paymentMethod.addPaymentDetails.codMessage" />
	</div>
</div>

<div id="enterOTP" class="cont-del">
	<%-- <label name="Enter OTP"><spring:theme
			code="checkout.multi.paymentMethod.CODPayment.enterOTP"
			text="Enter OTP:&nbsp;" /> --%> 
			<label for="otpNUMField"><spring:theme code="checkout.multi.paymentMethod.cod.otp" /></label>
			<input type="text" id="otpNUMField"
		name="otpNUM" onfocus="hideErrorMsg()" autocomplete="off" /> <!-- </label> -->
</div>
