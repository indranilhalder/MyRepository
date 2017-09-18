<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="multi-checkout"
	tagdir="/WEB-INF/tags/addons/marketplacecheckoutaddon/responsive/checkout/multi"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>

<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="single-checkout" tagdir="/WEB-INF/tags/addons/marketplacecheckoutaddon/responsive/checkout/single"%>

<template:page pageTitle="${pageTitle}" hideHeaderLinks="true" showOnlySiteLogo="true">
<style>
/* below style is written here as class 'page-multiStepCheckoutSummaryPage' appears twice once in one page again in payment failure page*/
body.page-multiStepCheckoutSummaryPage .mainContent-wrapper{
	background: #eee;
}
body.page-multiStepCheckoutSummaryPage #up{
	display: none;
}
</style>
<cart:tealiumCartParameters/>
<c:if test="${deviceType ne 'mobile'}">
<div id="singlePageDesktop">
	<!-- Accordions -->
	<div id="singlePageAccordion" class="checkout-accordion-wrapper">
	    <div class="checkout-accordion accordion-open">
	        <div class="checkout-accordion-heading">
	            <div><h4><span>1</span> Delivery Address</h4></div>
	            <div style="display:none" id="selectedAddressDivId"><span id="selectedAddressHighlight"></span><span>CHANGE ADDRESS</span></div>
	        </div>
	        <div class="checkout-accordion-body">
	            <div id="chooseDeliveryAddress">
	                <single-checkout:showDeliveryAddressDetails deliveryAddresses="${deliveryAddresses}"/>
	            </div>
	        </div>
	    </div>
	    <div class="checkout-accordion">
	        <div class="checkout-accordion-heading">
	            <div><h4><span>2</span> Delivery Method</h4></div>
	            <div style="display:none" id="selectedDeliveryOptionsDivId"><span id="selectedDeliveryOptionsHighlight"></span><span>CHANGE DELIVERY OPTION</span></div>
	        </div>
	        <div class="checkout-accordion-body">
	            <div id="choosedeliveryMode" class="cart wrapper">
	                <p></p>
	            </div>
	        </div>
	    </div>
	    <div class="checkout-accordion">
	        <div class="checkout-accordion-heading">
	        <!-- <div class="checkout-mobile-heading">Review Order</div> -->
	            <div><h4><span>3</span> Review Order</h4></div>
	            <div style="display:none" id="selectedReviewOrderDivId"><span id="selectedReviewOrderHighlight"></span><span>REVIEW ORDER</span></div>
	        </div>
	        <div class="checkout-accordion-body">
	            <div id="reviewOrder" class="cart wrapper">
	                <p></p>
	            </div>
	        </div>
	    </div>
	    <div class="checkout-accordion">
	        <div class="checkout-accordion-heading">
	            <div><h4><span>4</span> Payment</h4></div>
	            <div style="display:none"><span></span><span></span></div>
	        </div>
	        <div class="checkout-accordion-body">
	            <div id="makePaymentDiv">
	                <c:if test="${fn:contains(prePopulateTab, 'payment')}" >
	                	<c:set var="is_responsive" value="false"></c:set>
	                	<%@include file="/WEB-INF/views/addons/marketplacecheckoutaddon/responsive/pages/checkout/single/showAddPaymentMethodPage.jsp"%> 
	                </c:if>
	            </div>
	        </div>
	    </div>
	</div>
</div>
</c:if>
<span id="deviceType" style="display:none;">${deviceType}</span>
<span id="isPincodeRestrictedPromoPresent" style="display:none;">${isPincodeRestrictedPromoPresent}</span>
<c:if test="${deviceType ne 'normal'}">
<div id="singlePageMobile">
	<div class="checkout_mobile_section" id="chooseDeliveryAddressMobile">
		<div class="checkout-mobile-heading">Delivery Address</div>
		<div class="change-cancel-wrapper">
		<p class="change-mobile" id="address-change-link" onclick="ACC.singlePageCheckout.changeAddress();">Change</p>
		<p class="cancel-mobile">Cancel</p>
		</div>
		<div id="chooseDeliveryAddressMobileDiv">
			<single-checkout:showDeliveryAddressDetailsMobile deliveryAddresses="${deliveryAddresses}"/>
		</div>
	</div>
	<div  class="checkout_mobile_section cart wrapper"  id="selectedDeliveryOptionsDivIdMobile">
		<div class="checkout-mobile-heading">Delivery Method</div>
		<div class="change-cancel-wrapper">
		<p class="change-mobile" id="delivery-mode-change-link" onclick="ACC.singlePageCheckout.changeDeliveryMode(this);">Change</p>
		<p class="cancel-mobile">Cancel</p>
		</div>
		<div id="choosedeliveryModeMobile">
			<%@include
				file="/WEB-INF/views/addons/marketplacecheckoutaddon/responsive/fragments/checkout/single/showDeliveryModesDetailsMobile.jsp"%>
		</div>
	</div>
	<div  class="checkout_mobile_section" id="makePaymentMobile">
	<div class="checkout-mobile-heading">Payment</div>
	<div class="change-cancel-wrapper">
		<!-- <p class="change-mobile">Change</p>
		<p class="cancel-mobile">Cancel</p> -->
		</div>
		<div id="makePaymentDivMobile">
			<c:set var="is_responsive" value="true"></c:set>
			<%@include file="/WEB-INF/views/addons/marketplacecheckoutaddon/responsive/pages/checkout/single/showAddPaymentMethodPage.jsp"%>
		</div>
	</div>
	<!-- The actual snackbar -->
<div id="mobileSnackbar">
<h1><spring:theme code="checkout.single.deliverySlot.mobile.chooseMsg"/></h1>
<span id="selectSnackbar">SELECT</span>
</div>
</div>
</c:if>
<div class="modal fade" id="singlePageAddressPopup">
	<div class="content" style="padding: 40px;max-width: 650px;">
		<span id="modalBody"></span>
		<!-- <button class="close" data-dismiss="modal"></button> -->
	</div>
	<div class="overlay" data-dismiss="modal">
	</div>
</div>
<div class="modal fade" id="singlePagePickupPersonPopup">
	<div class="content" style="padding:36px 26px 36px 36px;/*  overflow-y: hidden; */ max-width: 30%;border-radius: 8px;">
		<span id="modalBody" data-htmlPopulated="NO"></span>
		<button class="close" data-dismiss="modal"></button>
	</div>
	<div class="overlay" data-dismiss="modal">
	</div>
</div>
<div class="modal fade" id="singlePageChooseSlotDeliveryPopup">
	<div class="content" style="padding: 40px;">
		<span id="modalBody" data-htmlPopulated="NO"></span>
		<button class="close" data-dismiss="modal"></button>
	</div>
	<div class="overlay" data-dismiss="modal">
	</div>
</div>
<div id="confirmOverlay" style="display:none">
    <div id="confirmBox" style="display:none">

         <h1><spring:theme code="checkout.single.exchange.msg.one"/></h1>
        <p><spring:theme code="checkout.single.exchange.msg.two"/></p>

        <div id="confirmButtons">
            <a class="button blue" id="exConfirmYes" href="#">Yes<span></span></a>
            <a class="button gray"  id="exConfirmNo" href="#">No<span></span></a>
            </div>
    </div>
	</div>	

<style>

/* The snackbar - position it at the bottom and in the middle of the screen */
#mobileSnackbar {
    visibility: hidden; /* Hidden by default. Visible on click */
    width: 100%; /* Set a default minimum width */
    margin-left: -125px; /* Divide value of min-width by 2 */
    background-color: #333; /* Black background color */
    color: #fff; /* White text color */
    text-align: left; /* Centered text */
    border-radius: 2px; /* Rounded borders */
    padding: 10px 14px 14px 8px; /* Padding */
    position: fixed; /* Sit on top of the screen */
    z-index: 1; /* Add a z-index if needed */
    right: 0; /* Center the snackbar */
    bottom: 46px; /* 30px from the bottom */
}


/* Show the snackbar when clicking on a button (class added with JavaScript) */
#mobileSnackbar.show {
    visibility: visible; /* Show the snackbar */

/* Add animation: Take 0.5 seconds to fade in and out the snackbar. 
However, delay the fade out process for 2.5 seconds */
    -webkit-animation: fadein 0.5s, fadeout 0.5s 2.5s;
    animation: fadein 0.5s, fadeout 0.5s 2.5s;
}

/* Animations to fade the snackbar in and out */
@-webkit-keyframes fadein {
    from {bottom: 0; opacity: 0;} 
    to {bottom: 30px; opacity: 1;}
}

@keyframes fadein {
    from {bottom: 0; opacity: 0;}
    to {bottom: 30px; opacity: 1;}
}

@-webkit-keyframes fadeout {
    from {bottom: 30px; opacity: 1;} 
    to {bottom: 0; opacity: 0;}
}

@keyframes fadeout {
    from {bottom: 30px; opacity: 1;}
    to {bottom: 0; opacity: 0;}
}
span#selectSnackbar {
    float: right;
    line-height: 2.7;
    font-weight: bold;
    letter-spacing: 0.5px;
    cursor: pointer;
}
#mobileSnackbar h1 {
    float: left;
    display: inline-block;
    width: 73%;
    line-height: 1.4;
}

</style>
<script>
$(".checkout-accordion-wrapper .checkout-accordion .checkout-accordion-heading").on("click", function(){
	//$(this).parent().find(".checkout-accordion-body").slideToggle();
	$(this).parent().toggleClass("accordion-open");
	//$(this).parent().siblings().find(".checkout-accordion-body").slideUp();
	$(this).parent().siblings().removeClass("accordion-open");
	//UF-437 fix
	var selector = $(this).find('h4').text();
	var checkoutDeliveryPage = "Multi Checkout Summary Page:Select Address";
	if(selector.indexOf("Delivery Address") > -1){
    	checkoutDeliveryPage = "Multi Checkout Summary Page:Select Address";
    	    if(typeof(utag_data) != "undefined"){
    	       utag_data.page_name = checkoutDeliveryPage;	
    	     }
    	$("#pageName").val(checkoutDeliveryPage);
 	    //TISCSXII-2176 | page name update for dtm
 	       if(typeof (digitalData.page.pageInfo)!= 'undefined'){
    		   digitalData.page.pageInfo.pageName = $('#pageName').val().toLowerCase() ;
    	   }
    }
    else if(selector.indexOf("Delivery Method") > -1){
    	 checkoutDeliveryPage = "Multi Checkout Summary Page:Choose Your Delivery Options";
    	     if(typeof(utag_data) != "undefined"){
     	        utag_data.page_name = checkoutDeliveryPage;
    	       }
    	 $("#pageName").val(checkoutDeliveryPage);
	         if(typeof (digitalData.page.pageInfo)!= 'undefined'){
     	     	digitalData.page.pageInfo.pageName = $('#pageName').val().toLowerCase() ;
     	      }
    }
    else if(selector.indexOf("Review Order") > -1){
    	checkoutDeliveryPage = "Multi Checkout Summary Page:Review Order";
    	    if(typeof(utag_data) != "undefined"){
    	      utag_data.page_name = checkoutDeliveryPage;
    	    }
    	 $("#pageName").val(checkoutDeliveryPage);
	        if(typeof (digitalData.page.pageInfo)!= 'undefined'){
     		   digitalData.page.pageInfo.pageName = $('#pageName').val().toLowerCase() ;
     	     }
    }
    else if(selector.indexOf("Payment") > -1){
    	checkoutDeliveryPage = "Multi Checkout Summary Page:Payment Options";
    	      if(typeof(utag_data) != "undefined"){
    	         utag_data.page_name = checkoutDeliveryPage;
    	       }
    	 $("#pageName").val(checkoutDeliveryPage);
 	          if(typeof (digitalData.page.pageInfo)!= 'undefined'){
      		      digitalData.page.pageInfo.pageName = $('#pageName').val().toLowerCase() ;
      	      }
    } 
});
</script>
<c:if test="${isCart eq true}">
	<script>
		
		var timeoutID;
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
					   window.location = '${request.contextPath}/cart';
					}
	
					function goActive() {
					      startTimer();
					}
					
					$(document).ready(function(){
						//Preventing form submission on key press.
						$(document).on('keypress','input,select,textarea', function(e) {
							var code = e.keyCode || e.which;
							if(code == 13)
						        return false;

						});
					})
					
	</script>
</c:if>
</template:page>
