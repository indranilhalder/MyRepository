<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>

<%@ attribute name="containerCSS" required="false" type="java.lang.String" %>


<div id="orderTotals" class="${containerCSS}">
	<h2 class="order-summary"><spring:theme code="text.account.order.orderTotals" text="Order Summary"/></h2>
	<ul class="totals">
	
		<li>
			<spring:theme code="text.account.order.subtotal" text="Subtotal"/>
			<span class="amt"><format:price priceData="${order.subTotal}"/></span>
		</li>
		<c:if test="${order.totalDiscounts.value > 0}">
			<li>
				<spring:theme code="text.account.order.savings" text="Discount"/>
				<span class="amt"> -<format:price priceData="${order.totalDiscounts}"/></span>
			</li>
		</c:if>
		
		<li>
			<spring:theme code="text.account.order.delivery" text="Delivery"/>
			<span class="amt"><format:price priceData="${order.deliveryCost}" displayFreeForZero="true"/></span>
		</li>
		<!-- TISBOX-1417 Displaying COD related Information-->
		<li>
			<c:if test="${order.convenienceChargeForCOD.value > 0}">
			<spring:theme code="text.account.order.convenience" text="Convenience Charge"/>
			<span class="amt"><format:price priceData="${order.convenienceChargeForCOD}" displayFreeForZero="true"/></span>
			</c:if>
		</li>
		<!-- TISBOX-1417 Displaying COD related Information-->
		<li id="total"> 
			<spring:theme code="text.account.order.total" text="Total"/>
			<c:choose>
				<c:when test="${order.convenienceChargeForCOD.value > 0}">
					<span class="amt"><format:price priceData="${order.totalPriceWithConvCharge}"/></span>
				</c:when>
				<c:when test="${order.net}">
					<span class="amt"><format:price priceData="${order.totalPriceWithTax}"/></span>
				</c:when>
				<c:otherwise>
					<span class="amt"><format:price priceData="${order.totalPrice}"/></span>
				</c:otherwise>
			</c:choose>
		</li>
	</ul>
</div>
