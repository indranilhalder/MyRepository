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
			Select nearby store to pick up from (pincode <span id="cncChangedPincode${entryNumber}">${defaultPincode}</span>) <span>[?]</span>
		</h1>
		<div class="cnc_search_wrapper">
		<input class="cncStoreSearch" type="text" id="cncStoreSearch${entryNumber}" name="cncStoreSearch" placeholder="Search nearby store" onkeypress="ACC.singlePageCheckout.searchOnEnterPress(event,'cncStoreSearch${entryNumber}','${entryNumber}')">
		<button onclick="ACC.singlePageCheckout.searchCNCStores('cncStoreSearch${entryNumber}','${entryNumber}');" type="button"></button>
		</div>
		</div>
		<div class="cnc_pincode_search_wrapper change_pincode_block block${entryNumber}">
			<span class="change_txt txt${entryNumber}">Change Pincode</span>
			<div class="input${entryNumber} row enter-pincode-block" style="display:none;">
				<span class="">
						Want to pick from other area? 
						Enter pincode below
				</span>
				<div class="" style="padding:0px;">
					<input style="width: 100%" type="text" name="changepin${entryNumber}" class="changepin${entryNumber}" maxlength="6" placeholder="New Pincode">
					<button type="button" class="submitPincode submitPincode${entryNumber}" style="" name="submitPincode${entryNumber}">Submit</button>
				</div>
				<div class="pincodeValidation error_txt" style="margin-left: 15px;width: 200px;"></div>
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
/* $(".txt${entryNumber}").click(function(){
		//$(".txt${entryNumber}").hide();
		$(".input${entryNumber}").show();
	});  */
	$(".enter-pincode-block").hide();
	$(".txt${entryNumber}").click(function(e){
		e.stopPropagation();
		$(".enter-pincode-block").slideToggle();
	});
	$(".txt${entryNumber}").click(function(e){
		e.stopPropagation();
	});
	$(document).click(function(e){
		$(".enter-pincode-block").slideUp();
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
			          dataType : "html",
			    	  cache: false,
			          data : dataString${entryNumber},   
			          success : function(data) {
			        	 $("#cncUlDiv${entryNumber}").html(data);
			        	 $("#cncChangedPincode${entryNumber}").html($(".changepin${entryNumber}").val());
			        	 var cnc_arrow_left, cnc_top;
			            	if($('#cncStoreContainer${entryNumber}').parent().prev().find("li.click-and-collect").length > 0){
			            	cnc_arrow_left = parseInt($('#cncStoreContainer${entryNumber}').parent().prev().find("li.click-and-collect").offset().left) + 40;
			            	//cnc_top = parseInt($('#cncStoreContainer'+entryNumber).offset().top) - parseInt($('#cncStoreContainer'+entryNumber).parent().prev().find("li.click-and-collect").offset().top) - 8;
			            	}
			            	$('#cncStoreContainer${entryNumber}').find(".cnc_arrow").css("left",cnc_arrow_left+"px");
			            	//$('#cncStoreContainer'+entryNumber).parent().css("margin-top","-"+cnc_top+"px");
			            	//CNC Carousel
			            	if($(".cnc_item .removeColor1").length == 2){
			        			$("#cnc_carousel").addClass("two_address");
			        		}
			        		if($(".cnc_item .removeColor1").length == 1){
			        			$("#cnc_carousel").addClass("one_address");
			        		}
			            	$(".cnc_carousel").on('initialize.owl.carousel initialized.owl.carousel ' +
			        				'initialize.owl.carousel initialize.owl.carousel ' +
			        				'to.owl.carousel changed.owl.carousel',
			        				function(event) {
			        			var items     = event.item.count;     // Number of items
			        			var item      = event.item.index;     // Position of the current item
			        			
			        			if($(window).width() > 1263){
									var page_no = parseInt(items/3);
									if(items%3 > 0){
										page_no = parseInt(items/3) + 1;
									}
									var current_page = parseInt(item/3) + 1;
									if(item%3 > 0){
										current_page = parseInt(item/3) + 2;
									}
									}
									else{
										var page_no = parseInt(items/2);
										if(items%2 > 0){
											page_no = parseInt(items/2) + 1;
										}
										var current_page = parseInt(item/2) + 1;
										if(item%2 > 0){
											current_page = parseInt(item/2) + 2;
										}
									}
			        			$(".page_count_cnc").html("<span>"+current_page + " / " + page_no+"</span>");
			        		});
			        		$(".cnc_carousel").owlCarousel({
			        			items:3,
			        			loop: false,
			        			dots:false,
			        			margin: 60,
			        			navText:[],
			        			slideBy: 3,
			        			responsive : {
			            			// breakpoint from 0 up
			            			0 : {
			            				items:1,
			            				stagePadding: 36,
			            				slideBy: 1,
			            				margin: 0,
			            				nav: ($(".cnc_item .removeColor1").length <= 1)?false:true,
			            			},
			            			// breakpoint from 768 up
			            			768 : {
			            				items:2,
			            				slideBy: 2,
			            				nav: ($(".cnc_item .removeColor1").length <= 2)?false:true,
			            			},
			            			// breakpoint from 1280 up
			            			1280 : {
			            				items:3,
			            				nav: ($(".cnc_item .removeColor1").length <= 3)?false:true,
			            			}			
			            		},
			            		onRefresh: function () {
			            			$(".cnc_carousel").find('div.owl-item').height('');
			                    },
			                    onRefreshed: function () {
			                    	$(".cnc_carousel").find('div.owl-item').height($(".cnc_carousel").height());
			                    }
			        		});
			            	
			        		$( '.cnc_carousel input.radio_btn' ).on( 'click change', function(event) {
			        			event.stopPropagation();
			        		});
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