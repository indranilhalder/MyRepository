
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/formElement" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/checkout/multi"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/common"%>

<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/nav" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/address"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>


<%--  <c:set var = "addressFlag" scope="session" value = "${addressFlag}" />  --%>



<template:page pageTitle="${pageTitle}" hideHeaderLinks="true" showOnlySiteLogo="true">

	<%-- <div class="checkout-headline">
		<spring:theme code="checkout.multi.secure.checkout" text="Secure Checkout"></spring:theme>
	</div> --%>
	<div class="checkout-content cart checkout wrapper">
	<!-- store url fix -->
	<script type="text/javascript" src="/_ui/responsive/common/js/jquery-2.1.1.min.js"></script>
		<c:if test="${showDeliveryMethod eq true}">
		
			<multi-checkout:checkoutSteps checkoutSteps="${checkoutSteps}" progressBarId="${progressBarId}">
			<jsp:body>
				<script>
    				//window.onload =	function(){
    					//resetConvChargeElsewhere(); Commented for TIS 400
    				//}
    				$(document).ready(function() {
    					selectDefaultDeliveryMethod();
    				});
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
    				 //  window.location = '${request.contextPath}/cart';
    				}

    				function goActive() {
    				      startTimer();
    				}
				</script>
				<ycommerce:testId code="checkoutStepTwo">
				
				<form:form id="selectDeliveryMethodForm" action="${request.contextPath}/checkout/multi/delivery-method/next" method="get" >
				<!-- TISCR-305 starts -->
				<button class="button" id="deliveryMethodSubmitUp" type="submit" class="checkout-next"><spring:theme code="checkout.pickup.continue.button" text="Next"/></button>
				<!-- TISCR-305 ends -->
					<div class="checkout-shipping left-block">
					
						<div class="checkout-indent">
									<multi-checkout:shipmentItemsForDeliverySlot cartData="${cartData}" defaultPincode="${defaultPincode}"  showDeliveryAddress="true" />
								<%-- <button class="button" id="deliveryMethodSubmit" type="submit" class="checkout-next"><spring:theme code="checkout.pickup.continue.button" text="Next"/></button> --%>
						</div>
						
						
						
					</div>
					
					</form:form>
					
				</ycommerce:testId>
			</jsp:body>
			</multi-checkout:checkoutSteps>			
		</c:if>
		
	<div class="right-block shipping">
			<div class="checkout-order-summary">
				<%-- <multi-checkout:orderTotals cartData="${cartData}"
					showTaxEstimate="${showTaxEstimate}" showTax="${showTax}"  /> --%>
<ul class="totals outstanding-totalss">
					<li id="totals" class="outstanding-amounts"><spring:theme
							code="basket.page.totals.outstanding.amount" /><span class="amt"
						id="outstanding-amount-mobile"><ycommerce:testId
								code="cart_totalPrice_label">
								<format:price priceData="${cartData.totalPrice}" />
							</ycommerce:testId></span></li>
							</ul>
			</div>
			</div>
	</div>

</template:page>