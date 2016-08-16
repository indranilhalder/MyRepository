<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="modal-dialog">
	<div class="modal-content">
		<form class="form-horizontal">
			<div class="modal-body">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4><spring:theme code="text.accountOrderDetails.otp.lable"/></h4>

				 <spring:theme code="text.accountOrderDetails.otpPopup.message" arguments="${phoneNumber}" />
				<div class="row">
					<div class="col-md-6 col-sm-6">
						<label for="otp"><spring:theme code="text.accountOrderDetails.otp.lable"/>*</label> 
						  <input
							type="text" class="form-control textOTP" id="OTP"
							placeholder="******">
						<div class="error_text otpError"></div>
						<div class="error_text"></div>
					</div>
					<div class="col-md-6 col-sm-6 otpMessage">
						<span style="font-size: 10px"> 
						   <spring:theme code="text.accountOrderDetails.otpPopup.newOTPMsgBefore" /> <a href="#" onclick="newOTPGenerate('${subOrder.code}')"style="color: #C9C923; font-weight: bold;">here</a>
						   <spring:theme code="text.accountOrderDetails.otpPopup.newOTPMsg" />
						</span>
					</div>
				</div>
			</div>
			<p style="clear: both"></p>

			<p style="clear: both;"></p>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary"
					onclick="generateOTP('${orderCode}')">SUBMIT</button>
			</div>
		</form>
	</div>
</div>