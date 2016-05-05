<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<style>
.error{
color: red;
font-size: 12px;
}

</style>
<div class="modal fade" id="clicktoCallModal">
<div class="overlay" data-dismiss="modal"></div>	
<div class="content" style="width:35%">
	<button class="close" data-dismiss="modal"></button>
	<div class="click2call-container">
	 <h1>
			<spring:theme code="popupc2c.click2call.title" />
    </h1>
	<form method="get" id="validateOTP" style="display: none;">

	
			<label for="otp"><spring:theme code="popupc2c.enterOtp" /></label> <input
				type="text" value=""  name="otp"
				id="otp">
			<label for="errorOTP" class="error"></label>

		<div class="button_fwd_wrapper actions call-validateOtp">
			<button id="validateOTPBtn" type="button"><spring:theme code="popupc2c.validateOtp" /></button>
		</div>
		<!-- TISPRDT-111 fix -->
		<input type="hidden" name="emailId" id="c2cEmailId"> 
		<input type="hidden" name="contactNo" id="contactNo">
		<input type="hidden" name="customerName" id="customerName">
		<input type="hidden" name="reason" id="reason">
	</form>

	<div class="validOTP" style="display: none;">
		<p><spring:theme code="popupc2c.validOtp.message" /></p>
	</div>

	<div class="invalidOTP" style="display: none;">
		<p style="color: red;"><spring:theme code="popupc2c.invalidOtp.message" /></p>
	</div>

	<div class="mandetoryFieldMissing" style="display: none;">
		<p style="color: red;"><spring:theme code="popupc2c.mandatoryFields.message" /></p>
	</div>

	<form id="generateOTP" method="get" action="clickto/generateOTP">


	
			<label for="customerName" ><spring:theme code="popupc2c.customer.name" /></label> <input
				type="text" 
				value="${name}"
				name="customerName" maxlength="40">
			<label for="errorCustomerName" class="error"></label>

	
			<label for="email" ><spring:theme code="popupc2c.customer.email" /></label> <input
				type="text" 
				value="${emailId}"
				name="emailId" maxlength="240">
			<label for="errorCustomerEmail" class="error"></label>

	
			<label for="mobileNo"><spring:theme code="popupc2c.customer.mobileNo" /></label> <input
				type="text"
				value="${mobileNo}"
			 name="contactNo" maxlength="10">
			<label for="errorCustomerMobileNo" class="error"></label>

	
			<label for="reason"><spring:theme code="popupc2c.customer.reason" /></label> <select
				name="reason" >
				<c:if test="${reasons ne null }">
					<c:forEach items="${reasons}" var="reason">
						<option value="${reason.key}">${reason.value}</option>
					</c:forEach>
				</c:if>
			</select>
		
		<span></span>

		<div class="button_fwd_wrapper actions">
			<button id="generateOTPBtn" type="button" ><spring:theme code="popupc2c.call.generateOtp" /></button>
			 <a class="close" href="#nogo" data-dismiss="modal"><spring:theme code="text.button.cancel" /></a>
		</div>
	</form>
		</div>
		
	</div>
		
	</div>
	<!--  -->
