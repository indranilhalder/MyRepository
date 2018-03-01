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
	
		<li class="subtotalthanks">
			<spring:theme code="text.account.order.subtotal" text="Subtotal"/>
			<!-- UF-260 -->
			<%-- <format:price priceData="${order.subTotal}"/> --%><format:price priceData="${cartTotalMrp}"/></span></span>
		</li>
		<c:if test="${order.totalDiscounts.value > 0 || totalDiscount.value > 0}">
			<li id="cartPromotionAppliedthanks">
				<spring:theme code="text.account.order.savings" text="Discount"/>
				<!-- UF-260 -->
				<%-- <format:price priceData="${order.totalDiscounts}"/> --%>
				<format:price priceData="${totalDiscount}"/></span>
			</li>
		</c:if>
		
		<c:if test="${order.couponDiscount.value > 0}">
			<li id="couponAppliedthanks">
				<spring:theme code="text.account.order.couponSavings" text="Coupon"/>
				<span class="amt"> <format:price priceData="${order.couponDiscount}"/></span>
			</li>
		</c:if>
		<c:if test="${orderData.isEGVOrder ne true}">
		<li class="shippingthanks">
			<%-- <spring:theme code="text.account.order.delivery" text="Delivery"/> --%>
			<span><spring:theme code="text.account.order.delivery1" text="Scheduled Delivery and Shipping Charges"/></span> <!--  TISSUATS-919 -->
			<span class="amt"><format:price priceData="${order.deliveryCost}" displayFreeForZero="true"/></span>
		</li>
		<!-- TISBOX-1417 Displaying COD related Information-->
		<li id="convChargeFieldIdthanks">
			<c:if test="${order.convenienceChargeForCOD.value > 0}">
			<spring:theme code="text.account.order.convenience" text="Convenience Charge"/>
			<span class="amt"><format:price priceData="${order.convenienceChargeForCOD}" displayFreeForZero="true"/></span>
			</c:if>
		</li>
		</c:if>
		<c:if test="${isCliqCashApplied eq true}">
			<li>
				<spring:theme code="basket.page.totals.cliqcash" /> Applied
				<span class="amt">&#8377;${totalQcTotalAmount}</span>
			</li>
		</c:if>
		<!-- TISBOX-1417 Displaying COD related Information-->
		<li id="total"> 
			<c:choose>
				<c:when test="${isCliqCashApplied eq true}">
					<spring:theme code="text.account.order.paid" text="Paid"/>
					<span class="amt">&#8377;${totalPayableAmount}</span>
				</c:when>
				<c:otherwise>
					<spring:theme code="text.account.order.total" text="Total"/>
					<c:choose>
						<c:when test="${order.convenienceChargeForCOD.value > 0}">
							<span class="amt"><format:price priceData="${order.totalPriceWithConvCharge}"/></span>
						</c:when>
						<c:when test="${order.deliveryCost.value > 0}">
							<span class="amt"><format:price priceData="${order.totalPriceWithConvCharge}"/></span>
						</c:when>
						<c:when test="${order.net}">
							<span class="amt"><format:price priceData="${order.totalPriceWithTax}"/></span>
						</c:when>
						<c:otherwise>
							<span class="amt"><format:price priceData="${order.totalPrice}"/></span>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</li>
	</ul>
</div>
