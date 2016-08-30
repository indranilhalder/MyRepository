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
<%@ attribute name="cartData" required="false" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="orderData" required="false" type="de.hybris.platform.commercefacades.order.data.OrderData" %>	<!-- TPR-629 -->


<!-- TPR-629 orderData added to tag parameters -->
<div class="right-block billing">
	<div class="checkout-order-summary">
		<%-- <div class="headline"><spring:theme code="checkout.multi.order.summary" text="Order Summary" /></div> --%>
		<multi-checkout:orderTotals cartData="${cartData}" showTaxEstimate="${showTaxEstimate}" showTax="${showTax}" isCart="${isCart}" orderData="${orderData}"/>
		<c:if test="${isCart eq true}">
		<multi-checkout:coupons cartData="${cartData}" isCart="${isCart}"/>
		</c:if>
		<div class="bottom order-details block">
			<!-- <ul class="checkout-order-summary-list"> -->
				<multi-checkout:deliveryCartItems cartData="${cartData}" showDeliveryAddress="${showDeliveryAddress}" isCart="${isCart}" orderData="${orderData}"/>
				
				 <c:choose>
				<c:when test="${isCart eq true}">
				<c:forEach items="${cartData.pickupOrderGroups}" var="groupData" varStatus="status">
						<multi-checkout:pickupCartItems cartData="${cartData}" groupData="${groupData}" index="${status.index}" isCart="${isCart}" showHead="true" />
				</c:forEach>
				</c:when>
				<c:otherwise>
				<c:forEach items="${orderData.pickupOrderGroups}" var="groupData" varStatus="status">
						<multi-checkout:pickupCartItems orderData="${orderData}" groupData="${groupData}" index="${status.index}" isCart="${isCart}" showHead="true" />
				</c:forEach>
				</c:otherwise>
				</c:choose>

				<multi-checkout:paymentInfo paymentInfo="${cartData.paymentInfo}" showPaymentInfo="${showPaymentInfo}"/>
			<!-- </ul> -->
		</div>
	</div>
</div>
