<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div>
	<span class="accountPopupClose close">&times;</span> <br />

	<div class="orderStatementContainer">

		<form:form method="POST"
			action="${request.contextPath}/wallet/validateWalletOTP"
			commandName="walletForm">
			<div class="row">
				<div class="col-md-7 NOP">
					<div class="row">
						<div class="col-md-6 form-group">
							<label for="firstName"><spring:theme
									code="text.order.returns.firstname" /></label>
							<!-- PRDI-124 start-->
							<form:input path="qcVerifyFirstName" class="" id="firstName"
								value="${walletForm.qcVerifyFirstName}" placeholder="First Name"
								maxlength="140" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-7 NOP">
					<div class="row">
						<div class="col-md-6 form-group">

							<label for="lastName"><spring:theme
									code="text.order.returns.lastname" /></label>
							<form:input path="qcVerifyLastName" id="lastName"
								value="${walletForm.qcVerifyLastName}" placeholder="Last Name"
								maxlength="140" />
							<div class="error_text lastNameError"></div>
						</div>
					</div>
				</div>

			</div>
			<div class="row">
				<div class="col-md-8 form-group">
					<label for="phonenumber"><spring:theme
							code="text.order.returns.phonenumber" /></label>
					<form:input path="qcVerifyMobileNo"  maxlength="10" onkeypress="return isNumber(event)" id="mobileNo"
						value="${walletForm.qcVerifyMobileNo}" placeholder="Mobile Number" />
					<div class="error_text mobileNumberError"></div>
				</div>
			</div>

			<div class="row">
				<div class="col-md-8 form-group">
					<button type="button" onclick="createWalletOTP()">OTP Request
					</button>
					<div class="error_text wcOTPError"></div>
				</div>
			</div>

			<div class="row">
				<div class="col-md-8 form-group">
					<label for="otp">Enter OTP Number</label>
					<form:input path="otpNumber" maxlength="6" />
				</div>
			</div>
			<div class="modal-footer">
				<div class="error_text main_error"></div>
				<button type="button" onclick="submitWalletData()"
					class="btn btn-primary saveBlockData">
					<spring:theme code="text.order.returns.savebutton" />
				</button>
			</div>

		</form:form>
	</div>

</div>
