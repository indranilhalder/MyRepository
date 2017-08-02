<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<%@ attribute name="showDeliveryAddress" required="true" type="java.lang.Boolean" %>
<%@ attribute name="showPaymentInfo" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showTax" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showTaxEstimate" required="false" type="java.lang.Boolean" %>
<%@ attribute name="isCart" required="false" type="java.lang.Boolean" %>		<!-- TPR-629 -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="multi-checkout" tagdir="/WEB-INF/tags/addons/marketplacecheckoutaddon/responsive/checkout/multi" %>
<%@ taglib prefix="single-checkout" tagdir="/WEB-INF/tags/addons/marketplacecheckoutaddon/responsive/checkout/single" %>
<%@ attribute name="cartData" required="false" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="orderData" required="false" type="de.hybris.platform.commercefacades.order.data.OrderData" %>	<!-- TPR-629 -->
<!-- TPR-629 orderData added to tag parameters -->

<div class="right-block billing  checkout-list-right">
	<div class="checkout-order-summary">
	<h3><spring:theme code="checkout.single.payment.payableAmount"/></h3>
		<span id="orderTotalSpanId">
		<%-- <single-checkout:showOrderTotals cartData="${cartData}" showTaxEstimate="${showTaxEstimate}" showTax="${showTax}" isCart="${isCart}" orderData="${orderData}"/> --%>
			<multi-checkout:orderTotals cartData="${cartData}" showTaxEstimate="${showTaxEstimate}" showTax="${showTax}" isCart="${isCart}" orderData="${orderData}"/>
		</span>
		<multi-checkout:coupons isCart="${isCart}"/>
		
		<span id="totalWithConvField" style="display:none"></span>
	</div>
</div>
