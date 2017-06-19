<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>

<%--  <c:set var = "addressFlag" scope="session" value = "${addressFlag}" />  --%>


<!-- <script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css"> -->
<div class="checkout-content cart checkout wrapper">

	<script>
					//TISST-13010
					$(document).ready(function() {
						showPromotionTag();
					});
					/* var timeoutID;
					function setup() {
					    this.addEventListener("mousemove", resetTimer, false);
					    this.addEventListener("mousedown", resetTimer, false);
					    this.addEventListener("keypress", resetTimer, false);
					    this.addEventListener("DOMMouseScroll", resetTimer, false);
					    this.addEventListener("mousewheel", resetTimer, false);
					    this.addEventListener("touchmove", resetTimer, false);
					    this.addEventListener("MSPointerMove", resetTimer, false);
					    startTimer();
					}
					setup();

					function startTimer() {
					    // wait 2 seconds before calling goInactive
					    timeoutID = window.setTimeout(goInactive, '${timeout}');
					}

					function resetTimer(e) {
					    window.clearTimeout(timeoutID);

					    goActive();
					}

					function goInactive() {
					  // window.location = '${request.contextPath}/cart';
					}

					function goActive() {
					      startTimer();
					} */
					</script>
	<script type="text/javascript">

	function openPopForAdddPosToCartEntry(ussId,posName){
		//var productCode = $("#product").val();
		//alert(ussId+"@@@"+posName);
		$(".continue_btn, .continue_btn_a").css("pointer-events", "none");
		$(".continue_btn, .continue_btn_a").css("cursor", "default"); 
		$(".continue_btn, .continue_btn_a").attr("data-id", $(".continue_btn, .continue_btn_a").attr("href"));
		$(".continue_btn, .continue_btn_a").removeAttr("href");
		var requiredUrl = ACC.config.encodedContextPath +"/checkout/multi/delivery-method/addPosToOrderEntry";
		var dataString = 'ussId=' + ussId+ '&posName=' + posName;
			$.ajax({
			url : requiredUrl,
			data : dataString,
			success : function(data) {
				if(ACC.singlePageCheckout.getIsResponsive())
				{
					ACC.singlePageCheckout.getPickUpPersonPopUpMobile();
					//$("#singlePagePickupPersonPopup").modal('show');
				}
				$(".continue_btn, .continue_btn_a").css("pointer-events", "all");
				$(".continue_btn, .continue_btn_a").css("cursor", "pointer"); 
				$(".continue_btn, .continue_btn_a").attr("href", $(".continue_btn, .continue_btn_a").attr("data-id"));
			},
			error : function(xhr, status, error) {
				alert("Oops something went wrong!!!");	
			}
		});
	}
	
</script>
	<div class="checkout-shipping-items  left-block left-block-width">
	<span class="cnc_arrow"></span>
		<div class="cnc_title_search">
		<h1>
			Select nearby store to pick up from (pincode ${defaultPincode}) <span>[?]</span>
		</h1>
		<div class="cnc_search_wrapper">
		<input class="cncStoreSearch" type="text" id="cncStoreSearch${entryNumber}" name="cncStoreSearch" placeholder="Search nearby store">
		<button onclick="ACC.singlePageCheckout.searchCNCStores('cncStoreSearch${entryNumber}','${entryNumber}');" type="button"></button>
		</div>
		</div>
		<div class="change_pincode_block block${entryNumber}">
			<span class="change_txt txt${entryNumber}">Change Pincode?</span>
			<div class="input${entryNumber} row" style="width: 111%">
				<div class="col-md-8 col-sm-4 col-xs-4" style="padding:0px;">
					<input style="width: 100%" type="text" name="changepin${entryNumber}" class="changepin${entryNumber}" maxlength="6" placeholder="Enter Pincode to Change.">
				</div>
				<div class="col-md-4 col-sm-2 col-xs-2">
					<button type="button" class="submitPincode submitPincode${entryNumber}" style="height: 40px !important; background: #A9143C !important; border: none !important; color: #fff !important;" name="submitPincode${entryNumber}">Submit</button>
				</div>
			</div>
			<div class="pincodeValidation error_txt" style="margin-left: 15px;width: 200px;">
			
			</div>
		</div>
		<%-- <div class="productCount">(for ${cnccount} out of ${delModeCount + cnccount}
			items in your bag)</div> --%>

		<div id="cncUlDiv${entryNumber}">
		<%@include
				file="/WEB-INF/views/addons/marketplacecheckoutaddon/responsive/fragments/checkout/single/showPickupLocationFragments.jsp"%>
		</div>
		<!-- End of main UL where every thing happens -->
		<!-- Start:Below span is used to display error if no stores are selected -->
		<span class="${entryNumber}_select_store select_store error_txt"
			style="text-align: left; font-size: 15px;"> <spring:theme
				code="checkout.multi.cnc.select.products.validate.msg" />
		</span>
		<!-- End:Above span is used to display error if no stores are selected -->

	</div>


	<div>
		<input type="hidden" name="CSRFToken" value="${CSRFToken}">
	</div>
</div>
<script>
$(document).ready(function() {
	$(".txt${entryNumber}").click(function(){
		$(".txt${entryNumber}").hide();
		$(".input${entryNumber}").show();
	});
	
	 $(".changepin${entryNumber}").keyup(function(){
	    	$(".pincodeValidation").hide();
		    var pinvalue${entryNumber} = $(".changepin${entryNumber}").val();
			var pinlength = pinvalue${entryNumber}.length;
			var isString = isNaN($(".changepin${entryNumber}").val());
			var mobileSpaceCheck = checkMobileNumberSpace($(".changepin${entryNumber}").val());
			if($(".changepin${entryNumber}").val().length <= "6") {
				if(isString==true || mobileSpaceCheck==true) {
					$(".pincodeValidation").show();
					$(".pincodeValidation").text("Enter Only Digits");
				}
				else if($(".changepin${entryNumber}").val().indexOf("-") > -1 || $(".changepin${entryNumber}").val().indexOf("+") > -1 ) {
					$(".pincodeValidation").show();
					$(".pincodeValidation").text("Enter Only Digits");
				}
			}
	    });
	 
	 $(".submitPincode${entryNumber}").click(function(){
			$(".removeColor${entryNumber} .radio_color").removeClass("colorChange");
			$(".pincodeServicable${entryNumber}").hide();
			$(".pincodeValidation").hide();
			var pinvalue${entryNumber} = $(".changepin${entryNumber}").val();
			var pinlength = pinvalue${entryNumber}.length;
			var isString = isNaN($(".changepin${entryNumber}").val());
			var mobileSpaceCheck = checkMobileNumberSpace($(".changepin${entryNumber}").val());
			if($(".changepin${entryNumber}").val().length <= "6") {
				if(isString==true || mobileSpaceCheck==true) {
					$(".pincodeValidation").show();
					$(".pincodeValidation").text("Enter Only Digits");
				}
				else if ($(".changepin${entryNumber}").val().length <= "5") {
					$(".pincodeValidation").show();
					$(".pincodeValidation").text("Please Enter 6 Digits");
				}
				else if($(".changepin${entryNumber}").val().indexOf("-") > -1 || $(".changepin${entryNumber}").val().indexOf("+") > -1 ) {
					$(".pincodeValidation").show();
					$(".pincodeValidation").text("Enter Only Digits");
				}
				else {
				var productcode${entryNumber} = $("#productcode"+${entryNumber}).html();
				var sellerId${entryNumber} = $("#sellerId"+${entryNumber}).html();
				var dataString${entryNumber} = "pin=" + pinvalue${entryNumber} + "&productCode="+ productcode${entryNumber} + "&sellerId="+sellerId${entryNumber} + "&entryNumber="+${entryNumber};
				$.ajax({
			          url :  ACC.config.encodedContextPath +"/checkout/single/updatePincodeCheck",
			          type: "GET",
			          dataType : "json",
			    	  cache: false,
			    	  contentType : "application/json; charset=utf-8",
			          data : dataString${entryNumber},   
			          success : function(data) {
			        	 $("cncUlDiv${entryNumber}").html(data);
			          },
			          error : function(xhr, data, error) {
			        	  console.log("Error in processing Ajax. Error Message : " +error+" Data : " +data)
			        	  	$(".pincodeServicable${entryNumber}").show();
			        		$(".pincodeServicable${entryNumber}").text("This pincode is not servicable");
			        		$("#changeValue${entryNumber}").text(pinvalue${entryNumber});
						}
			         });
				
				$(".txt${entryNumber}").show();
				$(".input${entryNumber}").hide();
			} 
			}
		});
});
function checkMobileNumberSpace(number) {			
	return /\s/g.test(number);
}
</script>