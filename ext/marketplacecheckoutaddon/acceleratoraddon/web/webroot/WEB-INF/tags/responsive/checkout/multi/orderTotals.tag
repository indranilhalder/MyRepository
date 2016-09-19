<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="showTax" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showTaxEstimate" required="false" type="java.lang.Boolean" %>
<%@ attribute name="subtotalsCssClasses" required="false" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
	
<div class="subtotals top block ${subtotalsCssClasses} summary-info">
	<%-- <h2><spring:theme code="order.order.totals"/></h2> --%>
<ul class="totals">
<%-- Commented for defect TISBOX-1636 --%>
<%-- <li id="promotionMessage" >
	<!--  needs responsive CSS classes; issue created -->
<c:if test="${not empty cartData.appliedOrderPromotions}">
<div class="cartPromotions">
    <div class="promotions cartproline ">
    <spring:theme code="basket.received.promotions" />
    </div>
    <ycommerce:testId code="cart_recievedPromotions_labels">
        <c:forEach items="${cartData.appliedOrderPromotions}" var="promotion">
            <div class="promotionDesc">${promotion.description}</div>
        </c:forEach>
        <div class="promotionDesc">${promotionMssgDeliveryMode}</div>
    </ycommerce:testId>
</div>  
</c:if>
</li> --%>
	<li class="subtotal">
		<span class="subTotalSpan"><spring:theme code="basket.page.totals.subtotal"/> </span>
		<span class="amt">
			<ycommerce:testId code="Order_Totals_Subtotal">
				<format:price priceData="${cartData.subTotal}"/>
			</ycommerce:testId>
		</span>
	</li>
	
	<c:if test="${cartData.totalDiscounts.value > 0}">
	<li id="cartPromotionApplied">
		<span class="cartpromotionSpan"><spring:theme code="basket.page.totals.savings"/></span>
		<span id="cartPromotion" style="float: right">  <format:price priceData="${cartData.totalDiscounts}"/> 	</span>

	</li> 
    </c:if>
	
	<c:if test="${not empty cartData.deliveryCost}">
		<li class="shipping">
			<span class="shippingSpan"><spring:theme code="basket.page.totals.delivery"/></span>
			<span id="deliveryCostSpanId">
				<ycommerce:testId code="Order_Totals_Delivery">
					<format:price priceData="${cartData.deliveryCost}" displayFreeForZero="TRUE"/>
				</ycommerce:testId>
			</span>
		</li>
	</c:if>
	
	<li id="convChargeFieldId">
		<span><spring:theme code="basket.page.totals.convenience"/></span>
		<span id="convChargeField" style="float: right">
		</span>
	</li>
    <%-- Commented due to making confusion in the Payment page calculation --%>
	<!-- Tag used for Delivery Mode and Delivery Address Page promotion display TISBOX-1618-->
	<%-- <c:if test="${cartData.totalDiscounts.value > 0}">
	<li id="cartPromotionApplied">
		<span class="cartpromotionSpan"><spring:theme code="basket.page.totals.savings"/></span>
		<span id="cartPromotion" style="float: right"> - <format:price priceData="${cartData.totalDiscounts}"/> 	</span>

	</li> 
    </c:if> --%> 
	<!-- Tag used for Payment Page promotion display-->
	<li id="promotionApplied" >
		<span><spring:theme code="basket.page.totals.savings"/></span>
		<span id="promotion" style="float: right"><format:price priceData="${cartData.totalDiscounts}"/> 	</span>

	</li> 
     
    <li id="couponApplied" >
	<button class="remove-coupon-button"></button>
		<spring:theme code="basket.page.totals.coupons"/>
		<span id="couponValue" style="float: right"> </span>
<input type="hidden" id="couponRelContent" value="<spring:theme code="coupon.release.content"/>">
	</li>
    
    
	<li class="total" id="total">
		<div id="totalPriceConvChargeId">
			<span class="totalsSpan"><spring:theme code="basket.page.totals.total"/> </span>
			<span id="totalWithConvField" style="float: right"><format:price priceData="${cartData.totalPrice}"/></span>
		</div>
	</li>
	
	<c:if test="${cartData.net && cartData.totalTax.value > 0 && showTax}">
		<li class="tax">
			<span><spring:theme code="basket.page.totals.netTax"/></span>
			<span>
				<format:price priceData="${cartData.totalTax}"/>
			</span>
		</li>
	</c:if>
	
	<li id="promotionMessage" />
	<li id="couponMessage" />
	<!-- Commented to hide your order includes %tax  
	<c:if test="${not cartData.net}">
		<li class="realTotals">
			<ycommerce:testId code="cart_taxes_label">
				<p>
					<spring:theme code="basket.page.totals.grossTax" arguments="${cartData.totalTax.formattedValue}" argumentSeparator="!!!!"/>
				</p>
			</ycommerce:testId>
		</li>
	</c:if>
	<c:if test="${cartData.net && not showTax }"> 
		<li class="realTotals">
			<ycommerce:testId code="cart_taxes_label">
				<p>
					<spring:theme code="basket.page.totals.noNetTax"/>
				</p>
			</ycommerce:testId>
		</li>
	</c:if>
	-->
	</ul>
</div>

<ul class="totals outstanding-totalss">
          <li id="totals" class="outstanding-amounts"><spring:theme code="basket.page.totals.outstanding.amount"/><span class="amt" id="outstanding-amount-mobile"><ycommerce:testId code="cart_totalPrice_label">
               <format:price priceData="${cartData.totalPrice}"/>
            </ycommerce:testId></span></li>
          </ul>	
