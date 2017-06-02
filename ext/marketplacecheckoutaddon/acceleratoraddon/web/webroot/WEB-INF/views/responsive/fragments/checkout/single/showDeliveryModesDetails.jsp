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

<div id="selecteDeliveryModeMessage" style=""></div>
	<form:form id="selectDeliveryMethodForm" action="${request.contextPath}/checkout/single/select"
		method="post" commandName="deliveryMethodForm">
		<div class="checkout-shipping left-block">
			<div class="checkout-indent">
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