<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="single-checkout" tagdir="/WEB-INF/tags/addons/marketplacecheckoutaddon/responsive/checkout/single"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>


<div class="delivery-slot-popup-wrapper">
	<ycommerce:testId code="checkoutStepTwo">				
		<form:form id="selectDeliverySlotForm" action="/checkout/single/deliverySlotCostForEd" method="post" commandName="deliverySlotForm">
			<div class="delivery-slot-popup-container">
				<!-- <div class="checkout-indent"> -->
					<single-checkout:showShipmentItemsForDeliverySlot cartData="${cartData}"/>
				<!-- </div> -->
			</div>					
		</form:form>
	</ycommerce:testId>
	<div>
		<button type="button" class="done_delslot" onclick="ACC.singlePageCheckout.onSlotDeliveryDoneClick();">Done</button>
	</div>
	<div>
		<a href="javascript:void(0);" class="cancel_delslot" onclick="ACC.singlePageCheckout.onSlotDeliveryCancelClick();">Cancel</a>
	</div>
</div>
