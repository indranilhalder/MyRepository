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
    	<li id="promotionApplied" >
		<span><spring:theme code="basket.page.totals.savings"/></span>
		<span id="promotion" style="float: right"><format:price priceData="${cartData.totalDiscounts}"/> 	</span>

	</li> 
	
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
		<spring:theme code="basket.page.totals.savings"/>
		<span id="cartPromotion" style="float: right"> - <format:price priceData="${cartData.totalDiscounts}"/> 	</span>

	</li> 
    </c:if>  
	<!-- Tag used for Payment Page promotion display-->
	<li id="promotionApplied" >
		<spring:theme code="basket.page.totals.savings"/>
		<span id="promotion" style="float: right"> - <format:price priceData="${cartData.totalDiscounts}"/> 	</span>

	</li> --%>
	
     
    <li id="couponApplied" >
	<button class="remove-coupon-button"></button>
		<span class="couponSpan"><spring:theme code="basket.page.totals.coupons"/></span>
		<span id="couponValue" style="float: right"> </span>
<input type="hidden" id="couponRelContent" value="<spring:theme code="coupon.release.content"/>">
	</li>
    
    
	<li class="total" id="total">
		<div id="totalPriceConvChargeId">
			<spring:theme code="basket.page.totals.total"/> 
			
			<span id="totalWithConvFields" style="float: right"><format:price priceData="${cartData.totalPrice}"/></span>
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
	
	</ul>
</div>

<ul class="totals outstanding-totalss">
          <li id="totals" class="outstanding-amounts"><spring:theme code="basket.page.totals.outstanding.amount"/><span class="amt" id="outstanding-amount-mobile"><ycommerce:testId code="cart_totalPrice_label">
               <format:price priceData="${cartData.totalPrice}"/>
            </ycommerce:testId></span></li>
          </ul>	
