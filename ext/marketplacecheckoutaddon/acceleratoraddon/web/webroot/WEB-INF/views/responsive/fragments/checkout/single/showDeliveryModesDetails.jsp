<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="single-checkout"
	tagdir="/WEB-INF/tags/addons/marketplacecheckoutaddon/responsive/checkout/single"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div id="selecteDeliveryModeMessage"></div>
	<!-- Delivery mode selection form -->
	<form:form id="selectDeliveryMethodForm" action="/checkout/single/select"
		method="post" commandName="deliveryMethodForm">
		<div class="checkout-shipping left-block">
			<div class="checkout-indent">
				<!-- Tag contains radio elements for radio button -->
				<single-checkout:showShipmentItems cartData="${cartData}"
					defaultPincode="${defaultPincode}" showDeliveryAddress="true" />

			</div>
		</div>

	</form:form>
	
<div class="right-block shipping">
	<div class="checkout-order-summary">
		<button class="button" type="button" id="del_continue_btn" onclick="ACC.singlePageCheckout.proceedOnDeliveryModeSelection(this)";>
			<spring:theme code="checkout.single.deliveryMethod.continue"
				text="Next" />
		</button>
	</div>
</div>