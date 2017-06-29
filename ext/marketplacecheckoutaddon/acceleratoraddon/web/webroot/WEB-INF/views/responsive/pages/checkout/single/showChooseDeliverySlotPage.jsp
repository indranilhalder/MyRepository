<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="single-checkout" tagdir="/WEB-INF/tags/addons/marketplacecheckoutaddon/responsive/checkout/single"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/address"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>



<div class="delivery-slot-popup-wrapper">
	<ycommerce:testId code="checkoutStepTwo">				
		<form:form id="selectDeliveryMethodForm" action="" method="get" >
			<div class="delivery-slot-popup-container">
				<!-- <div class="checkout-indent"> -->
					<single-checkout:showShipmentItemsForDeliverySlot cartData="${cartData}"/>
				<!-- </div> -->
			</div>					
		</form:form>
	</ycommerce:testId>
	<div>
		<button type="button" class="done_delslot" onclick="$('#singlePageChooseSlotDeliveryPopup').modal('hide');ACC.singlePageCheckout.getReviewOrder();">Done</button>
	</div>
	<div>
		<a href="javascript:void(0);" class="cancel_delslot" onclick="$('#singlePageChooseSlotDeliveryPopup').modal('hide');ACC.singlePageCheckout.getReviewOrder();">Skip</a>
	</div>
	<!-- Below is for responsive -->
	<div>
		<button type="button" class="done_delslot" onclick="$('#singlePageChooseSlotDeliveryPopup').modal('hide');">Done</button>
	</div>
	<div>
		<a href="javascript:void(0);" class="cancel_delslot" onclick="$('#singlePageChooseSlotDeliveryPopup').modal('hide');">Skip</a>
	</div>
</div>
