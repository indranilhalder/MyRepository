<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<li class="track_order_header"><a href="#" onclick="openTrackOrder()"><spring:theme code="trackorder.header.text" text="Track Order"/></a></li>

<div class="removeModalAfterLoad" id="showTrackOrder">
           <div class="modal-dialog">
               <div class="modal-content">
                <form class="form-horizontal">
                    <div class="modal-body">
                    	<button type="button" onclick="closeTrackOrder()" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    	<h4><spring:theme code="trackorder.track.text" text="Track Your Order"/></h4>                    	
                    	<div class="row">
                    		<div class="col-md-6 col-sm-6">
	                    		<label for="email"><spring:theme code="trackorder.track.email.text" text="Enter Your Email ID*"/></label>
   								<input type="text" class="form-control trackEmail" id="TrackemailId" placeholder="Enter your email Id">
   								<label for="email"><spring:theme code="trackorder.track.order.text" text="Enter Your Order ID*"/></label>
   								<input type="text" class="form-control orderEmail" id="TrackOrderdId" placeholder="Enter your order Id">
                    		</div>
                    		<div class="col-md-6 col-sm-6">
                    			<label for="email"><spring:theme code="trackorder.track.provenotrobo.text" text="Prove you're not a robot*"/></label>
   								<div class="captcha">
   									<img src="http://oi64.tinypic.com/23w1kdz.jpg">
   								</div>
   								<input type="text" class="form-control captcha" id="TrackCaptcha" placeholder="Please enter above text">
                    		</div>
                    	</div><!-- http://oi64.tinypic.com/23w1kdz.jpg -->
                    	<div class="row">
                    		<div class="col-md-12 col-sm-12">
                    			<div class="trackPopupText"><spring:theme code="trackorder.track.check.order.text" text="To know your Order ID, Please check your order confirmation mail or sms."/></div>
                    		</div>
                    	</div>
                    	<div class="row">
                    		<div class="col-md-7 col-sm-12">
                    			
                    		</div>
                    		<div class="col-md-5">
                    			<button class="viewOrderButton" onclick="return viewOrderStatus(event)"><spring:theme code="trackorder.vieworder.button.text" text="View Order Status"/></button>
                    		</div>
                    	</div>
                    </div>
                     <p style="clear: both;"></p>
                     <div class="row">
                    		<div class="col-md-12 col-sm-12" style="padding-left: 15px;">
			                     <span class="error_text emailError"></span>
			                     <span class="error_text orderError"></span>
			                     <span class="error_text captchaError"></span>
			                     <span class="error_text main_error"></span>
			                </div>
			         </div>
                </form>
           </div>
       </div>
   </div>
	
<div class="wrapBG" style="background-color: rgba(0, 0, 0, 0.5); width: 100%; height: 600px; position: fixed; top: 0px; left: 0px; z-index: 99999; display: none;"></div>
	
	
<style>
#showTrackOrder {	
	display: none;
	margin: 0px auto;
	width: 100%;
	position: fixed;
	top: 100px;
	left: 0px;
}

.error_text {
	font-color: #a9143c;
	display: none;
	width: 100%;
	text-align: center;
}

#showTrackOrder .modal-dialog {	
	background: #fff;
	border-radius: 5px;
	padding: 10px 10px 50px 10px;
}

#showTrackOrder .modal-dialog .trackPopupText {
	color: #333;
	font-size: 10px;
	margin-top: 10px;
}

#showTrackOrder .modal-dialog .viewOrderButton {
	font-size: 12px;
	background: #a9143c;
	color: #fff;
	margin-top: 10px;
	float: right;
}

#showTrackOrder .modal-dialog .col-md-6, #showTrackOrder .modal-dialog .col-md-12, #showTrackOrder .modal-dialog .col-md-5 {
	margin: 0px 0px 0px 0px;
	padding: 0px 10px 0px 0px;
}

#showTrackOrder .modal-dialog label {
	font-weight: bold;
	padding-bottom: 5px;
	margin-top: 14px;
}

#showTrackOrder .modal-dialog .captcha img {
	border: 1px solid #ccc;
	margin: 5px 0px 5px 0px;
}

#showTrackOrder .modal-dialog input[type="text"] {
	width: 100%;
}

#showTrackOrder .modal-dialog h4 {
	width: 95%;
	font-size: 24px;
	padding-bottom: 30px;
}

#showTrackOrder .modal-dialog .close {
	float: right;
	border-radius: 50%;
	width: 30px;
	height: 30px;
	border: 1px solid #ccc;
}


@media (max-width: 1024px) {
	.removeModalAfterLoad .changeAdddd {
		height: 550px;
		overflow-y: scroll;
	}
}

@media (max-width: 720px) {
	.removeModalAfterLoad .changeAdddd {
		height: 400px;
		overflow-y: scroll;
	}
}
</style>

<script>
function openTrackOrder() {
	//console.log("Open Track Order");
	var height = $(window).height();
 	$(".wrapBG").css("height",height);
	$(".wrapBG").fadeIn(100);
	$("#showTrackOrder").fadeIn(300);
 	$("#showTrackOrder").css("z-index","999999");
}

function closeTrackOrder() {
	$("#showTrackOrder").fadeOut(200);
	$(".wrapBG").fadeOut(100);
}

function viewOrderStatus(event) {
	event.preventDefault();
	$(".error_text").hide();
	if($(".trackEmail").val().length <= "2") {
		$(".emailError").fadeIn(100).text("Please enter correct email address");
		return false;
	}
	else if($(".orderEmail").val().length <= "2") {
		$(".orderError").fadeIn(100).text("Please enter correct order ID.");
		return false;
	}
	else if($("#TrackCaptcha").val().length <= "2") {
		$(".captchaError").fadeIn(100).text("Please re-verify captcha.");
	 	return false;
	} else {
		//alert("Move to Order Tracking Page");
		
		var orderCode = $('.orderEmail').val();
		var emailId = $('.trackEmail').val();
		var captchaCode=$("#TrackCaptcha").val();
		//alert(emailId)
		//alert(orderCode)
		//alert(captchaCode)
		$.ajax({
			url : ACC.config.encodedContextPath	+"/trackOrder/nonlogintrack",
			type : 'GET',
			data : {
				orderCode : orderCode,
				emailId : emailId,
				captchaCode : captchaCode
				},
		     contentType: "application/json",
			 dataType: 'json',
			 success : function(result) {
				if(result=="true"){
					$("#showTrackOrder").hide();
					$("wrapBG1").hide();
					$("#showOTP").show();
					$(".wrapBG").show();
					var height = $(window).height();
					$(".wrapBG1").css("height", height);
					$("#showOTP").css("z-index", "999999");
					window.location.href =  ACC.config.encodedContextPath+"/trackOrder/shortDetails/?orderCode=" + orderCode;
				}else{
					//alert("else");
					$(".main_error").show();
					$("#showTrackOrder .main_error").text(result);
				}

			},
			error : function(result) {
				alert("Error while tracking the order. Kindly try after some time")
			}

		});
		
	}
}


</script>
