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
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>


<%-- <c:choose><:when test="${walletForm.qcVerifyFirstName not empty}"><c:set var="qcFnameDisable" value="disabled"></c:set></:when>
									<:otherwise><c:set var="qcFnameDisable" value=""></c:set></:otherwise></c:choose>
<c:choose><:when test="${walletForm.qcVerifyLastName not empty}"><c:set var="qcLnameDisable" value="disabled"></c:set></:when>
									<:otherwise><c:set var="qcLnameDisable" value=""></c:set></:otherwise></c:choose>
<c:choose><:when test="${walletForm.qcVerifyLastName not empty}"><c:set var="qcMobileDisable" value="disabled"></c:set></:when>
									<:otherwise><c:set var="qcMobileDisable" value=""></c:set></:otherwise></c:choose> --%>

		<form:form method="POST" id="walletForm"
			action="${request.contextPath}/wallet/validateWalletOTP"
			commandName="walletForm">
			<c:choose>
				<c:when test="${walletForm.firstNameFlag}"><c:set var="qcFnDisableStatus" value="false"></c:set></c:when>
				<c:otherwise><c:set var="qcFnDisableStatus" value="true"></c:set></c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${walletForm.lastNameFlag}"><c:set var="qcLnDisableStatus" value="false"></c:set></c:when>
				<c:otherwise><c:set var="qcLnDisableStatus" value="true"></c:set></c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${walletForm.mobileNoFlag}"><c:set var="qcMobileDisableStatus" value="false"></c:set></c:when>
				<c:otherwise><c:set var="qcMobileDisableStatus" value="true"></c:set></c:otherwise>
			</c:choose>
			<div class="row text-center egv-otp-container">
				<div>
					<span class="h4"><strong>Please enter below information to verify your mobile number.</strong></span>
				</div>
			<hr />
				<div class="col-md-12">
					<div class="clearfix">
						<div class="row">
								<div class="clearfix col-sm-3 text-left">
									<label for="otpFirstName">Name*</label>
								</div>
								<div class="clearfix col-sm-9">
									<div class="input-group">
										<form:input path="qcVerifyFirstName" class="giftCard_input" id="otpFirstName" readonly="${qcFnDisableStatus}"
									value="${walletForm.qcVerifyFirstName}" placeholder="Enter First Name"
									maxlength="140" />
									<div class="input-group-btn">
										<span class="otp-button otp-edit-button" id="editQcFirstName" onclick="editOtpField('otpFirstName')"><span class="glyphicon glyphicon-edit"></span></span>
									</div></div>
                                   <div class="otp-error-text error_text otpFirstNameError"></div>
									
								</div>
						</div>
						<br />
						<div class="row">
								<div class="clearfix col-sm-3 text-left">
									<label for="otpLastName">Surname*</label>
								</div>
								<div class="clearfix col-sm-9">
									<div class="input-group">
										<form:input path="qcVerifyLastName" id="otpLastName" class="giftCard_input" readonly="${qcLnDisableStatus}"
									value="${walletForm.qcVerifyLastName}" placeholder="Enter Last Name" 
									maxlength="140" />
									<div class="input-group-btn">
										<span class="otp-button otp-edit-button" id="editQcLastName" onclick="editOtpField('otpLastName')"><span class="glyphicon glyphicon-edit"></span></span>
									</div></div>
									<div class="otp-error-text error_text otpLastNameError"></div>
								</div>
								
						</div>
						<br />
						<div class="row">
								<div class="clearfix col-sm-3 text-left">
									<label for="otpPhonenumber">Mobile No*</label>
								</div>
								<div class="clearfix col-sm-9">
									<div class="input-group">
										<form:input path="qcVerifyMobileNo" class="giftCard_input" id="otpPhonenumber" readonly="${qcMobileDisableStatus}"
									value="${walletForm.qcVerifyMobileNo}" placeholder="Mobile Number"
									maxlength="10" />
									<div class="input-group-btn">
										<span class="otp-button otp-edit-button" id="editQcMobileNo" onclick="editOtpField('otpPhonenumber')"><span class="glyphicon glyphicon-edit"></span></span>
									</div></div>
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
		
