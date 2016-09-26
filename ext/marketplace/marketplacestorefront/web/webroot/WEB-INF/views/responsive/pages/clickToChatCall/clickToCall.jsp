<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<style>
.error {
	color: #ff1c47;
	font-size: 12px;
	text-align: left;
	margin-top:5px;
}
</style>
<div class="modal fade" id="clicktoCallModal">
	<div class="overlay" data-dismiss="modal"></div>
	<div class="content">
		<div class="close-btns">
			<a href="#" class="cminimize"></a> <a href="#" class="cclose"
				data-dismiss="modal"></a>
		</div>
		<div class="click2call-container">
			<h2 class="ccall-ico">
				<spring:theme code="popupc2c.click2call.title" />
			</h2>
			<form method="get" id="validateOTP" style="display: none;">

				<div class="input-box">
					<input type="text" value="" name="otp" id="otp"> <label
						for="otp" class="clabel"><spring:theme
							code="popupc2c.enterOtp" /></label> <label for="errorOTP" class="error"></label>
				</div>
				<div class="button_fwd_wrapper actions call-validateOtp">
					<button id="validateOTPBtn" type="button" class="bsubmit" style="margin-top:20px;">
						<spring:theme code="popupc2c.validateOtp" />
					</button>
				</div>
				<!-- TISPRDT-111 fix -->
				<input type="hidden" name="emailId" id="c2cEmailId"> <input
					type="hidden" name="contactNo" id="contactNo"> <input
					type="hidden" name="customerName" id="customerName"> <input
					type="hidden" name="reason" id="reason">
			</form>

			<div class="validOTP" style="display: none;">
				<p>
					<spring:theme code="popupc2c.validOtp.message" />
				</p>
			</div>

			<div class="invalidOTP" style="display: none;">
				<p style="color: red;">
					<spring:theme code="popupc2c.invalidOtp.message" />
				</p>
			</div>

			<div class="mandetoryFieldMissing" style="display: none;">
				<p style="color: red;">
					<spring:theme code="popupc2c.mandatoryFields.message" />
				</p>
			</div>

			<form id="generateOTP" method="get" action="clickto/generateOTP">


				<div class="input-box">
					<input type="text" value="${name}" name="customerName"
						maxlength="40" id="customerNameId"> <label for="customerNameId" class="clabel"><spring:theme
							code="popupc2c.customer.name" /></label> <label for="errorCustomerName"
						class="error"></label>
				</div>
				<div class="input-box">
					<input type="text" value="${emailId}" name="emailId"
						maxlength="240" id="emailIdCall"> <label for="emailIdCall" class="clabel"><spring:theme
							code="popupc2c.customer.email" /></label> <label
						for="errorCustomerEmail" class="error"></label>
				</div>

				<div class="input-box">
					<input type="text" value="${mobileNo}" name="contactNo"
						maxlength="10" id="contactNoId"> <label for="contactNoId" class="clabel"><spring:theme
							code="popupc2c.customer.mobileNo" /></label> <label
						for="errorCustomerMobileNo" class="error"></label>
				</div>

				<div class="input-box">
					<select name="reason" id="reasonIdCall">
						<c:if test="${reasons ne null }">
							<c:forEach items="${reasons}" var="reason">
								<option value="${reason.key}">${reason.value}</option>
							</c:forEach>
						</c:if>
					</select> <label for="reasonIdCall" class="clabel"><spring:theme
							code="popupc2c.customer.reason" /></label> <span></span>
				</div>
				<div class="button_fwd_wrapper actions">
					<button id="generateOTPBtn" type="button" class="bsubmit">
						<spring:theme code="popupc2c.call.generateOtp" />
					</button>
					<a class="close bcancel" href="#nogo" data-dismiss="modal" id="call"><spring:theme
							code="text.button.cancel" /></a>
				</div>
			</form>
		</div>

	</div>

</div>
<!--  -->
<script>
$(document).ready(function(){
	window.setTimeout(function() {
		if($('#clicktoCallModal').length){
			$(".input-box input").each(function(){
				if( $(this).val() != ""){
					$(this).addClass("used");
				}
				else {
					$(this).removeClass("used");
				}	
			});
			$(".input-box select").each(function(){
				if( $(this).val() != ""){
					$(this).addClass("used");
				}
				else {
					$(this).removeClass("used");
				}	
			});
			//var x = $("#chatForm .input-box input[name='emailId']");
			
		}
	},500);
});
</script>