<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ attribute name="actionNameKey" required="true" type="java.lang.String"%>
<%@ attribute name="action" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<div class="forgottenPwd clearfix"> 
Please enter OTP sent to your mobile phone
<c:url value="/login/pw/OTPValidation" var="otpValidationUrl" />
 <div class="forgottenPwd clearfix"> 

		<div class="headline"><spring:theme code="sendSmsOtp.sms"/></div>
		<div class="required right"><spring:theme code="form.required"/></div>
		<div class="description"><spring:theme code="forgottenPwd.description"/></div> 
		<form:form method="post" commandName="sendSmsOtpForm" action="${otpValidationUrl}">
		<formElement:formInputBox idKey="sendSmsOtp.OTPNumber" labelKey="sendSmsOtp.OTPNumber" path="OTPNumber" inputCSS="text" mandatory="true"/>
			<button class="btn btn-primary btn-block"  type="submit" ><spring:theme code="sendSmsOTP.submit"/></button>		
		</form:form> 
		
</div>
  </div>  
