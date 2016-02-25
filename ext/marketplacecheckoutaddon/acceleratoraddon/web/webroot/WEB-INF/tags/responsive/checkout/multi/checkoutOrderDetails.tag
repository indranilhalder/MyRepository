<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="showDeliveryAddress" required="true" type="java.lang.Boolean" %>
<%@ attribute name="showPaymentInfo" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showTax" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showTaxEstimate" required="false" type="java.lang.Boolean" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/addons/marketplacecheckoutaddon/responsive/checkout/multi" %>

<div class="right-block billing">
	<div class="checkout-order-summary">
		<%-- <div class="headline"><spring:theme code="checkout.multi.order.summary" text="Order Summary" /></div> --%>
		<multi-checkout:orderTotals cartData="${cartData}" showTaxEstimate="${showTaxEstimate}" showTax="${showTax}" />
		<multi-checkout:coupons cartData="${cartData}" />
		<div class="bottom order-details block">
			<!-- <ul class="checkout-order-summary-list"> -->
				<multi-checkout:deliveryCartItems cartData="${cartData}" showDeliveryAddress="${showDeliveryAddress}" />
				<c:forEach items="${cartData.pickupOrderGroups}" var="groupData" varStatus="status">
						<multi-checkout:pickupCartItems cartData="${cartData}" groupData="${groupData}" index="${status.index}" showHead="true" />
				</c:forEach>

				<multi-checkout:paymentInfo cartData="${cartData}" paymentInfo="${cartData.paymentInfo}" showPaymentInfo="${showPaymentInfo}" />
			<!-- </ul> -->
		</div>
	</div>
</div>
