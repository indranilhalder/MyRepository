<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:url value="/contact/upload" var="uploadUrl" />
<script src="https://www.google.com/recaptcha/api.js"></script>
<style>
.readOnlyInputBox {
	width: 100%;
	border: none !important;
	cursor: default;
	color: #A9143C;
}
</style>
<form:form action="${uploadUrl}" method="POST"
	enctype="multipart/form-data" commandName="mplContactUsForm"
	id="contactUsForm" cssClass="pureForm">

	<%-- <form:errors path="*" class="error-message" element="div" /> --%>
	<div class="issue">
		<label><spring:theme code="text.contactus.your.issue" /></label> <span
			class="issue"></span>
		<form:input idKey="issueDetails" path="issueDetails"
			cssClass="readOnlyInputBox" readonly="true" />
	</div>
	<div class="order-details">
		<div id="contactOrderField">
			<label><spring:theme code="text.contactus.order.number" /></label>
			<form:input labelKey="orderDetails" idKey="orderDetails"
				path="orderDetails" cssClass="inputBox" />
		</div>
		<div id="orderError" class="error"></div>
		<form:errors path="orderDetails" class="error-message" element="div" />

		<div id="contactEmailField">
			<label><spring:theme code="text.contactus.email" /></label>
			<c:choose>
				<c:when test="${mplContactUsForm.emailId eq null}">
					<form:input labelKey="emailId" idKey="emailId" path="emailId"
						cssClass="inputBox" />
				</c:when>
				<c:otherwise>
					<form:input labelKey="emailId" idKey="emailId" path="emailId"
						cssClass="inputBox" readonly="true" />
				</c:otherwise>
			</c:choose>

		</div>
		<div id="emailError" class="error"></div>
		<form:errors path="emailId" class="error-message" element="div" />
		<div class="file-input">
			<label><spring:theme code="text.contactus.upload" /></label>
			<form:input type="file" name="file" path="file" cssClass="inputBox" />
			<input type="text" value="Choose File">
			<button>Browse</button>
			<span class="file-types"><spring:theme
					code="text.contactus.upload.type" /></span>
			<form:errors path="file" cssClass="error-message" element="div" />
		</div>
		<!-- Captcha start -->
		<div class="captcha" id="recaptchaWidget">
			<label><spring:theme code="text.contactus.captcha" /></label>
			<div class="g-recaptcha" data-sitekey="${recaptchaKey}"></div>
			<div id="captchaError"></div>
		</div>
	</div>
	<!-- Captcha end -->
	<div class="message">
		<label><spring:theme code="text.contactus.enter.your.message" /></label>
		<form:textarea idKey="message" placeholder="" path="message" rows="5"
			columns="50" />
		<div id="messageError" class="error"></div>
		<form:errors path="message" cssClass="error" element="div" />
		<button type="submit" class="blue" id="submitRequestButton">
			<spring:theme code="text.contactus.submit.request" />
		</button>
				<input type="hidden" id="selectedTabs" name="selectedTabsByName" value="${selectedTab}"/>
	</div>
</form:form>
