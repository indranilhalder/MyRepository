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

<style>
.otp-button {
    background-color: #a9143c;
    display: inline;
    color: white;
    font-size: 12px;
    font-weight: 400;
    height: 36px;
    padding: 10px 20px;}
    
.otp-error-text {
	text-align: left;
}
    
#otp-submit-section {display: none;}
</style>
		<form:form method="POST" id="walletForm"
			action="${request.contextPath}/wallet/validateWalletOTP"
			commandName="walletForm">
			<div class="row text-center">
				<div class="col-md-12">
					<div class="clearfix">
						<div class="row">
								<div class="clearfix col-sm-3 text-right">
									<label for="otpFirstName"><spring:theme
										code="text.order.returns.firstname" /></label>
								</div>
								<div class="clearfix col-sm-9">
									<form:input path="qcVerifyFirstName" class="giftCard_input" id="otpFirstName"
									value="${walletForm.qcVerifyFirstName}" placeholder="Enter First Name"
									maxlength="140" />
                                   <div class="otp-error-text error_text otpFirstNameError"></div>
									
								</div>
						</div>
						<br />
						<div class="row">
								<div class="clearfix col-sm-3 text-right">
									<label for="otpLastName"><spring:theme
										code="text.order.returns.lastname" /></label>
								</div>
								<div class="clearfix col-sm-9">
									<form:input path="qcVerifyLastName" id="otpLastName" class="giftCard_input"
									value="${walletForm.qcVerifyLastName}" placeholder="Enter Last Name"
									maxlength="140" />
									<div class="otp-error-text error_text otpLastNameError"></div>
								</div>
								
						</div>
						<br />
						<div class="row">
								<div class="clearfix col-sm-3 text-right">
									<label for="otpPhonenumber"><spring:theme
								code="text.order.returns.phonenumber" /></label>
								</div>
								<div class="clearfix col-sm-9">
									<form:input path="qcVerifyMobileNo"  maxlength="10" class="giftCard_input" onkeypress="return isNumber(event)" id="otpPhonenumber"
									value="${walletForm.qcVerifyMobileNo}" placeholder="Enter Mobile Number" />
								<div class="error_text otp-error-text mobileNumberError"></div>
								</div>
						</div>
					</div>
				</div>
				
			</div>
			<br />
			<div class="row text-center">
					<button type="button" class="otp-button" onclick="createWalletOTP()">GENERATE OTP
					</button>
					<div class="error_text wcOTPError"></div>
			</div>
			<div id="otp-submit-section">
				<hr />
				<div class="row text-center">
					<div class="col-md-12">
						<div class="clearfix">
							<div class="row">
									<div class="clearfix col-sm-3 text-right">
										<label for="otpValue">Enter OTP</label>
									</div>
									<div class="clearfix col-sm-6">
										<form:input path="otpNumber" class="giftCard_input" id="otpValue" maxlength="6" />									
									</div>
							</div>
						</div>
					</div>
					<br />&nbsp;
					<div class="col-md-12 text-center">
						<div class="error_text main_error"></div>
						<button type="button" onclick="submitWalletData()"
						class="otp-button">
						VERIFY
						</button>
					</div>
				</div>
			</div>
			<%-- <div class="row">
				<div class="col-md-8 form-group">
					<label for="phonenumber"><spring:theme
							code="text.order.returns.phonenumber" /></label>
					<form:input path="qcVerifyMobileNo"  maxlength="10" onkeypress="return isNumber(event)" id="mobileNo"
						value="${walletForm.qcVerifyMobileNo}" placeholder="Mobile Number" />
					<div class="error_text mobileNumberError"></div>
				</div>
			</div> --%>

			<!-- <div class="row">
				<div class="col-md-8">
					<button type="button" onclick="createWalletOTP()">OTP Request
					</button>
					<div class="error_text wcOTPError"></div>
				</div>
			</div> -->

		<%-- 	 <div class="row">
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
			</div>  --%>

		</form:form>
