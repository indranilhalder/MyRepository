<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<div class="headline">
	<spring:theme code="validate.otp.title" var="title"/>
</div>

<div class="span-24">
		<div class="accountContentPane clearfix">
			<div class="headline"><spring:theme code="text.account.otpvalidation" text="Validate OTP"/></div>
			<div class="required right"><spring:theme code="form.required" text="Fields marked * are required"/></div>
			<div class="description"><spring:theme code="text.account.profile.validateotpform" text="Please use this form to validate OTP"/></div>
			<form:form action="./OTPValidation" method="post" commandName="sendSmsOtpForm">
			<div class="form_field-elements">
			
				<formElement:formInputBox idKey="profile.OTPNumber" labelKey="profile.OTPNumber" path="OTPNumber" inputCSS="text" mandatory="true"/>

				<div class="form-actions">
					<ycommerce:testId code="profilePage_SaveUpdatesButton">
						<button class="btn btn-primary" type="submit"><spring:theme code="text.account.profile.ValidateOTP" text="Validate"/></button>
					</ycommerce:testId>
				</div>
			</div>
			</form:form>			
		</div>
</div>