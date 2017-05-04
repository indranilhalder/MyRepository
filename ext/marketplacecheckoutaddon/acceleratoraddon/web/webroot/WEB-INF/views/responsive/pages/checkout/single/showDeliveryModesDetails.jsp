<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="single-checkout"
	tagdir="/WEB-INF/tags/addons/marketplacecheckoutaddon/responsive/checkout/single"%>
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
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%-- <%@ attribute name="cartData" required="true"
	type="java.util.List"%> --%>

<c:if test="${showDeliveryMethod eq true}">
	<script>
		//window.onload =	function(){
		//resetConvChargeElsewhere(); Commented for TIS 400
		//}
		/* $(document).ready(function() {
			selectDefaultDeliveryMethod();

		}); */
/* 		var timeoutID;
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
 */	</script>
<div id="selecteDeliveryModeMessage" style=""></div>
	<form:form id="selectDeliveryMethodForm" action="${request.contextPath}/checkout/single/select"
		method="post" commandName="deliveryMethodForm">
		<!-- TISCR-305 starts -->
		<!-- TISPRO-625 starts -->
		<input type="hidden" id="isExpressCheckoutSelected"
			value="${isExpressCheckoutSelected}" />
		<c:choose>
			<c:when test="${isExpressCheckoutSelected}">
				<button class="button proceed-button" id="deliveryMethodSubmitUp"
					type="button" class="checkout-next">
					<spring:theme
						code="checkout.multi.deliveryMethod.expresscheckout.continue"
						text="Next" />
				</button>
			</c:when>
			<c:otherwise>
				<button class="button proceed-button" id="deliveryMethodSubmitUp"
					type="button" class="checkout-next">
					<spring:theme code="checkout.multi.deliveryMethod.continue"
						text="Next" />
				</button>
			</c:otherwise>
		</c:choose>
		<!-- TISPRO-625 ends -->
		<!-- TISCR-305 ends -->
		<div class="checkout-shipping left-block">

			<div class="checkout-indent">
				<single-checkout:showShipmentItems cartData="${cartData}"
					defaultPincode="${defaultPincode}" showDeliveryAddress="true" />

			</div>



		</div>

	</form:form>

</c:if>
<div class="right-block shipping">
		<div class="checkout-order-summary">
			<%-- <multi-checkout:orderTotals cartData="${cartData}"
				showTaxEstimate="${showTaxEstimate}" showTax="${showTax}" /> --%>
	<c:choose>
		<c:when test="${isExpressCheckoutSelected}">
				<button class="button"
				type="button" id="del_continue_btn" onclick="ACC.singlePageCheckout.proceedOnDeliveryModeSelection(this)";>
				<spring:theme
					code="checkout.single.deliveryMethod.continue"
					text="Next" />
			</button>
		</c:when>
		<c:otherwise>
					<button class="button"
				type="button" id="del_continue_btn" onclick="ACC.singlePageCheckout.proceedOnDeliveryModeSelection(this)";>
				<spring:theme code="checkout.single.deliveryMethod.continue"
					text="Next" />
			</button>
		</c:otherwise>
	</c:choose>

	</div>
	
</div>