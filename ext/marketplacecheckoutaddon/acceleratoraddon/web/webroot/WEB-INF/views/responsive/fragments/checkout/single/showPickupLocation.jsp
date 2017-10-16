<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>


<div class="checkout-content cart checkout wrapper">
<c:if test="${storesAvailable eq true}">
	<script>
					//TISST-13010
					$(document).ready(function() {
						showPromotionTag();
					});
					</script>

	<div class="checkout-shipping-items  left-block left-block-width">
	<span class="cnc_arrow"></span>
		<div class="cnc_title_search">
		<h1>
			<spring:theme code="checkout.single.cnc.nearBy"/> (pincode <span id="cncChangedPincode${entryNumber}">${defaultPincode}</span>) 
			<span id="cncpickuppincode_info">[?]
			<p id="cncpickuppincode_tooltip">Place the order and collect your items from a nearby store. We will keep it ready for you.</p>
			</span>
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
						<spring:theme code="checkout.single.cnc.otherArea"/>
						<spring:theme code="checkout.single.cnc.enterPincode"/>
				</span>
				<div class="" style="padding:0px;">
					<input style="width: 100%" type="text" name="changepin${entryNumber}" class="changepin${entryNumber}" maxlength="6" placeholder="New Pincode">
					<button type="button" class="submitPincode submitPincode${entryNumber}" style="" name="submitPincode${entryNumber}"><spring:theme code="checkout.single.cnc.submitPincode"/></button>
				</div>
				<div class="pincodeValidation error_txt" style="margin-left: 15px;width: 200px;"></div>
			</div>
		</div>
		<%-- <div class="productCount">(for ${cnccount} out of ${delModeCount + cnccount}
			items in your bag)</div> --%>

		<!-- UF-281/281:starts -->
		<div id="cncUlDiv${entryNumber}">
		<%@include
				file="/WEB-INF/views/addons/marketplacecheckoutaddon/responsive/fragments/checkout/single/showPickupLocationFragments.jsp"%>
		</div>
		<!-- UF-281/281:ends -->
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
</c:if>
<c:if test="${storesAvailable eq false}">
	<!-- UF-281/281 -->
	<span><spring:theme code="checkout.single.nostores.select.another"/></span>
</c:if>
</div>
<c:if test="${storesAvailable eq true}">
<script>
$(document).ready(function() {
	
	$(".enter-pincode-block").hide();
	$(".txt${entryNumber}").click(function(e){
		e.stopPropagation();
		$(".enter-pincode-block").slideToggle();
	});
	$(".enter-pincode-block").click(function(e){
		e.stopPropagation();
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
				var productcode${entryNumber} = $("#productcode"+${entryNumber}).text();
				var sellerId${entryNumber} = $("#sellerId"+${entryNumber}).text();
				var dataString${entryNumber} = "pin=" + pinvalue${entryNumber} + "&productCode="+ productcode${entryNumber} + "&sellerId="+sellerId${entryNumber} + "&entryNumber="+${entryNumber};
				$.ajax({
			          url :  ACC.config.encodedContextPath +"/checkout/single/updatePincodeCheck",
			          type: "GET",
			          dataType : "html",
			    	  cache: false,
			          data : dataString${entryNumber},   
			          success : function(data) {
			        	  //UF-281/281:starts
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
			        				'changed.owl.carousel',
			        				function(event) {
			        			var items     = event.item.count;     // Number of items
			        			var item      = event.item.index;     // Position of the current item
			        			//Below method will display correct page number.
			        			ACC.singlePageCheckout.carouselPageNumberDisplay(items,item,"page_count_cnc");
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
			            				mouseDrag:($(".cnc_item .removeColor${entryNumber}").length <= 1)?false:true,
			            				nav: ($(".cnc_item .removeColor${entryNumber}").length <= 1)?false:true,
			            			},
			            			// breakpoint from 768 up
			            			768 : {
			            				items:2,
			            				slideBy: 2,
			            				mouseDrag:($(".cnc_item .removeColor${entryNumber}").length <= 2)?false:true,
			            				nav: ($(".cnc_item .removeColor${entryNumber}").length <= 2)?false:true,
			            			},
			            			// breakpoint from 1280 up
			            			1280 : {
			            				items:3,
			            				mouseDrag:($(".cnc_item .removeColor${entryNumber}").length <= 3)?false:true,
			            				nav: ($(".cnc_item .removeColor${entryNumber}").length <= 3)?false:true,
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
			        		//UF-281/281:ends
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
</c:if>