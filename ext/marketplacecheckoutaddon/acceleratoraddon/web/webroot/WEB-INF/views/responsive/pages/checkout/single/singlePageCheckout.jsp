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
<cart:tealiumCartParameters/>
	<!-- Accordions -->
<div id="singlePageAccordion" class="checkout-accordion-wrapper">
    <div class="checkout-accordion <c:if test="${openTab eq 'deliveryAddresses'}">accordion-open</c:if>">
        <div class="checkout-accordion-heading">
        <div class="checkout-mobile-heading">Delivery Address</div>
            <div><h4><span>1</span> Delivery Address</h4></div>
            <div style="display:none" id="selectedAddressDivId"><span id="selectedAddressHighlight"></span><span>CHANGE ADDRESS</span></div>
        </div>
        <div class="checkout-accordion-body">
         <div class="change-cancel-wrapper"><p class="change-mobile">Change</p>
            <p class="cancel-mobile">Cancel</p></div>
            <div id="chooseDeliveryAddress">
                <single-checkout:showDeliveryAddressDetails deliveryAddresses="${deliveryAddresses}"/>
            </div>
        </div>
    </div>
    <div class="checkout-accordion <c:if test="${openTab eq 'deliveryMethod'}">accordion-open</c:if>">
        <div class="checkout-accordion-heading">
        <div class="checkout-mobile-heading">Delivery Method</div>
            <div><h4><span>2</span> Delivery Method</h4></div>
            <div style="display:none" id="selectedDeliveryOptionsDivId"><span id="selectedDeliveryOptionsHighlight"></span><span>CHANGE DELIVERY OPTION</span></div>
        </div>
        <div class="checkout-accordion-body">
        <div class="change-cancel-wrapper"><p class="change-mobile">Change</p>
            <p class="cancel-mobile">Cancel</p></div>
            <div id="choosedeliveryMode" class="cart wrapper">
                <p>Bootstrap is a powerful front-end framework for faster and easier web development. It is a collection of CSS and HTML conventions. <a href="http://www.tutorialrepublic.com/twitter-bootstrap-tutorial/" target="_blank">Learn more.</a></p>
            </div>
        </div>
    </div>
    <div class="checkout-accordion <c:if test="${openTab eq 'reviewOrder'}">accordion-open</c:if>">
        <div class="checkout-accordion-heading">
        <div class="checkout-mobile-heading">Review Order</div>
            <div><h4><span>3</span> Review Order</h4></div>
            <div style="display:none" id="selectedReviewOrderDivId"><span id="selectedReviewOrderHighlight"></span><span>REVIEW ORDER</span></div>
        </div>
        <div class="checkout-accordion-body">
        <div class="change-cancel-wrapper"><p class="change-mobile">Change</p>
            <p class="cancel-mobile">Cancel</p></div>
            <div id="reviewOrder" class="cart wrapper">
                <p>CSS stands for Cascading Style Sheet. CSS allows you to specify various style properties for a given HTML element such as colors, backgrounds, fonts etc. <a href="http://www.tutorialrepublic.com/css-tutorial/" target="_blank">Learn more.</a></p>
            </div>
        </div>
    </div>
    <div class="checkout-accordion <c:if test="${openTab eq 'payment'}">accordion-open</c:if>">
        <div class="checkout-accordion-heading">
          <div class="checkout-mobile-heading">Payment</div>
            <div><h4><span>4</span> Payment</h4></div>
            <div style="display:none"><span></span><span></span></div>
        </div>
        <div class="checkout-accordion-body">
         <div class="change-cancel-wrapper"><p class="change-mobile">Change</p>
            <p class="cancel-mobile">Cancel</p></div>
            <div id="makePayment">
                <c:if test="${prePopulateTab eq 'payment'}" >
                	<%@include file="/WEB-INF/views/addons/marketplacecheckoutaddon/responsive/pages/checkout/single/showAddPaymentMethodPage.jsp"%> 
                </c:if>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="singlePageAddressPopup">
	<div class="content" style="padding: 40px;max-width: 650px;">
		<span id="modalBody"></span>
		<!-- <button class="close" data-dismiss="modal"></button> -->
	</div>
	<div class="overlay" data-dismiss="modal">
	</div>
</div>
<div class="modal fade" id="singlePagePickupPersonPopup">
	<div class="content" style="padding: 40px;max-width: 650px;">
		<span id="modalBody" data-htmlPopulated="NO"></span>
		<!-- <button class="close" data-dismiss="modal"></button> -->
	</div>
	<div class="overlay" data-dismiss="modal">
	</div>
</div>
<script>
$(".checkout-accordion-wrapper .checkout-accordion .checkout-accordion-heading").on("click", function(){
	//$(this).parent().find(".checkout-accordion-body").slideToggle();
	$(this).parent().toggleClass("accordion-open");
	//$(this).parent().siblings().find(".checkout-accordion-body").slideUp();
	$(this).parent().siblings().removeClass("accordion-open");
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
	</script>
</c:if>
</template:page>
