<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${not empty stringMessage}">
${stringMessage}
</c:if>
<c:if test="${empty stringMessage}">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="form-horizontal otpForm">
					<div class="modal-body">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h2 style="font-size: 23px;">
							<spring:theme code="text.accountOrderDetails.otp.lable" />
						</h2>

						<div class="row messageSpace">
						
								<div class="error_text"></div>
							<spring:theme code="text.accountOrderDetails.otpPopup.message"
								arguments="${phoneNumber}" />
						</div>
						<div class="row clearfix">
							<div class="col-md-6 col-sm-6">
								<label for="otp" class="lableSpace"><spring:theme
										code="text.accountOrderDetails.otp.lable" />*</label> <input
									type="text" class="form-control textOTP" id="OTP"
									placeholder="******">
								<div class="error_text otpError" style="padding: 7px 0px;"></div>
								<div class="error_text"></div>
							</div>
							<div class="col-md-6 col-sm-6 otpMessage">
								<span style="font-size: 9px"> <spring:theme
										code="text.accountOrderDetails.otpPopup.newOTPMsgBefore" /> <a
									href="#" onclick="newOTPGenerate('${orderCode}')"
									style="color: #ff9900; font-size: 11px;text-decoration: underline;">here</a> <spring:theme
										code="text.accountOrderDetails.otpPopup.newOTPMsg" />
								</span>
							</div>
						</div>
						<div class="col-md-12 clearfix">
							<button type="button" class="btn btn-primary submitOrange"
								onclick="submitOTP('${orderCode}')"><spring:theme
										code="text.otpPopup.submit" /></button>
						</div>
					</div>
					<p style="clear: both;"></p>

				</div>
			</div>
		</div>
	<script>
$(document).ready(function(){
	$(".close,.wrapBG").click(function() {
		$("#changeAddressPopup, #otpPopup").hide();
		$(".wrapBG").hide();
	});
		
});
function submitOTP(orderCode){
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
			data : "orderId=" + orderCode + "&otpNumber="+$("#OTP").val(),
			success : function(response) {
					$(".otpError").show();
				if(response=="INVALID"){	
					$(".otpError").text("Invalid OTP, Please Re-enter.");		
				}
				else if(response =='isNotChangable'){
					 $(".messageSpace .error_text").show().text("Order is no more eligible for Address change. Please contact Customer Care for any assistance");
					   $(".submitOrange").prop('disabled',true);
	                   $(".otpMessage").hide();
	                }
				else if(response=="Internal Server Error, Please try again later"){
					$(".otpError").show();
					$(".otpError").text(response);
					
				}else if(response == null){
					$(".otpError").text("Invalid OTP, Please Re-enter.");
				}
				else{
					window.location.href=ACC.config.encodedContextPath+"/my-account/order/?orderCode="+orderCode+"&isServiceable="+true;
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
.otpForm .messageSpace {
	padding: 20px 5px;
	font-size: 10px;
	font-weight: 100;
	line-height: 20px;
}

.otpForm .lableSpace {
	padding-bottom: 4px;
}

.otpForm input[type="text"] {
	width: 100%;
}

.otpForm .otpMessage {
	font-size: 10px;
	line-height: 13px;
	padding: 14px 0px;
}

.otpForm button.blue:hover {
	color: #fff;
	background-color: #a9143c;
}

.otpForm .submitOrange {
	background: #A9143C;
	color: #fff;
	height: 35px;
}

#otpPopup .modal-dialog {
	padding: 20px 20px 30px 20px; 
}
</style>
</c:if>