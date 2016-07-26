<%@ tag language="java" pageEncoding="UTF-8"%>
<div class="modal-dialog">
	<div class="modal-content">
		<form class="form-horizontal">
			<div class="modal-body">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4>One-Time-Password (OTP)</h4>

				To save the changes, please enter the One-Time-Password(OTP)
				received via SMS on your registered mobile number: ${phoneNumber}and
				click on 'submit'.
				<div class="row">
					<div class="col-md-6 col-sm-6">
						<label for="otp">One-Time-Password (OTP)*</label> <input
							type="text" class="form-control textOTP" id="OTP"
							placeholder="******">
						<div class="error_text otpError"></div>
						<div class="error_text"></div>
					</div>
					<div class="col-md-6 col-sm-6 otpMessage">
						<span style="font-size: 10px"> If you did not receive OTP
							via SMS or your SMS-OTP has expired. Please click <a href="#" onclick="newOTPGenerate('${subOrder.code}')"style="color: #C9C923; font-weight: bold;">here</a>
							to get new OTP to your mobile phone via SMS
						</span>
					</div>

				</div>
			</div>
			<p style="clear: both"></p>

			<p style="clear: both;"></p>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary"
					onclick="generateOTP('${subOrder.code}')">SUBMIT</button>
			</div>
		</form>
	</div>
</div>