<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty stringMessage}">
${stringMessage}
</c:if>
<c:if
	test="${empty stringMessage}">
	<div class="modal-dialog">
		<div class="modal-content">
			<form class="form-horizontal otpForm">
				<div class="modal-body">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h2>
						<spring:theme code="text.accountOrderDetails.otp.lable" />
					</h2>
					
					<div class="row messageSpace">
					<spring:theme code="text.accountOrderDetails.otpPopup.message"
						arguments="${phoneNumber}"/>
					</div>
					<div class="row">
						<div class="col-md-6 col-sm-6">
							<label for="otp" class="lableSpace"><spring:theme
									code="text.accountOrderDetails.otp.lable" />*</label> <input
								type="text" class="form-control textOTP" id="OTP"
								placeholder="******">
							<div class="error_text otpError"></div>
							<div class="error_text"></div>
						</div>
						<div class="col-md-6 col-sm-6 otpMessage">
							<span style="font-size: 11px"> <spring:theme
									code="text.accountOrderDetails.otpPopup.newOTPMsgBefore" /> <a
								href="#" onclick="newOTPGenerate('${orderCode}')"
								style="color: #C9C923;font-size: 14px;">here</a> <spring:theme
									code="text.accountOrderDetails.otpPopup.newOTPMsg" />
							</span>
						</div>
					</div>
				</div>
				<p style="clear: both"></p>

				<p style="clear: both;"></p>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary blue"
						onclick="generateOTP('${orderCode}')">SUBMIT</button>
				</div>
			</form>
		</div>
	</div>
	<script>
$(document).ready(function(){

	$(".close,.wrapBG").click(function() {
		$("#changeAddressPopup,#otpPopup").hide();
		$(".wrapBG").hide();
	});
		
});
function generateOTP(orderId){
	 $(".otpError").hide();
    $(".serverError").hide();
    
	 var numberOTP=$("#OTP").val();
	 var isString = isNaN(numberOTP);
    var numberOTP=numberOTP.trim();
   if(isString==true || numberOTP.trim()==''){
  	 $(".otpError").show();
	 $(".otpError").text("Invalid OTP, Please Re-enter.");
   }else if(numberOTP < 5 && numberOTP > 6){
  	 $(".otpError").show();
	     $(".otpError").text("Invalid OTP, Please Re-enter.");
   } else{
		$.ajax({
			type : "GET",
			url : ACC.config.encodedContextPath + "/my-account/validationOTP",
			data : "orderId=" + orderId + "&otpNumber="+$("#OTP").val(),
			success : function(response) {
				if(response=="Pincode not Serviceable"){
					$("#changeAddressPopup").show();
					$("#otpPopup").hide();
					$(".wrapBG").show();
					var height = $(window).height();
					$(".wrapBG").css("height", height);
					$("#changeAddressPopup").css("z-index", "999999");
					$(".pincodeNoError").show();
					$(".pincodeNoError").text("Pincode not Serviceable");
				}
				else if(response=="INVALID"){	
					$(".otpError").show();
					$(".otpError").text("Invalid OTP, Please Re-enter.");		
				}else if(response=="Internal Server Error, Please try again later"){
					$(".otpError").show();
					$(".otpError").text(response);
					
				}else{
					window.location.href=ACC.config.encodedContextPath+"/my-account/order/?orderCode="+orderId+"&isServiceable="+true;
				} 
			},
			error: function(jqXHR, textStatus, errorThrown) {
			  console.log(textStatus, errorThrown);
			  $("#showOTP .error_text").text("Internal Server Error, Please try again later.");
			  alert("Internal Server Error, Please try again later.");
			  location.reload();
			}
		}); 
   
	}	 
}

</script>

<style>
.otpForm .messageSpace{
550065
	padding: 10px;
}
.otpForm .lableSpace{
	padding-bottom: 4px;
}
.otpForm input[type="text"]{
	width: 100%;
}
.otpForm .otpMessage{
	padding: 18px;
}
.otpForm button.blue:hover {
    color: #fff;
    background-color: #a9143c;
}
</style>
</c:if>