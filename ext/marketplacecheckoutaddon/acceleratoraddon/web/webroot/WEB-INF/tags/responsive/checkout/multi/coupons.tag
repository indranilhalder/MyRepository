<%@ attribute name="showTax" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showTaxEstimate" required="false" type="java.lang.Boolean" %>
<%@ attribute name="subtotalsCssClasses" required="false" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ attribute name="isCart" required="false" type="java.lang.Boolean" %>	<!-- TPR-629 -->
	
<h2 class="coupon">Apply Coupon Code</h2>	
<div class="coupon block">
		<label for="couponFieldId" class="coupon_title_desktop">Have coupon code? Apply here</label>
		<label for="couponFieldId" class="coupon_title_mobile">Coupon Code</label>
		<div><input type="text" id="couponFieldId" />
		<button type="submit" id="couponSubmitButton" class="button btn-block">Apply</button></div>
		<span class="error-message" id="invalidCouponError"><spring:theme code="checkout.multi.coupons.invalid"/></span>
		<span class="error-message" id="expiredCouponError"><spring:theme code="checkout.multi.coupons.expired"/></span>
		<span class="error-message" id="issueCouponError"><spring:theme code="checkout.multi.coupons.issue"/></span>
		<span class="error-message" id="priceCouponError"><spring:theme code="checkout.multi.coupons.priceExceeded"/></span>
		<span class="error-message" id="appliedCouponError"><spring:theme code="checkout.multi.coupons.alreadyApplied"/></span>
		<span class="error-message" id="emptyCouponError"><spring:theme code="checkout.multi.coupons.notApplied"/></span>
		<span class="error-message" id="notApplicableCouponError"><spring:theme code="checkout.multi.coupons.notApplicable"/></span>
		<span class="error-message" id="notReservableCouponError"><spring:theme code="checkout.multi.coupons.notReservable"/></span>
		<span class="error-message" id="freebieCouponError"><spring:theme code="checkout.multi.coupons.freebie"/></span>
		<span class="error-message" id="userInvalidCouponError"><spring:theme code="checkout.multi.coupons.userInvalid"/></span>
		<span class="error-message" id="sellerCouponError"><spring:theme code="checkout.multi.coupons.seller"/></span>
		<span class="error-message" id="orderThresholdError"><spring:theme code="checkout.multi.coupons.orderThreshold"/></span>
<!-- changes for  [ TPR-4461] TISSTRT-1523-->		
		<span class="error-message correctMessageCoupon" id="couponPaymentRestrictionMessage"></span>
<!-- changes for  [ TPR-1075] TISSTRT-1523-->
	   <span class="error-message" id="firstPurchaseOfferError"><spring:theme code="checkout.multi.coupons.firstPurchaseInvalid"/></span>
<!-- changes for  [ TPR-4460]-->
	   <span class="error-message" id="invalidChannelError"><spring:theme code="checkout.multi.coupons.channelInvalid"/></span>
	<!-- Top 5 coupons-----Commented as functionality out of scope of R2.1   Uncomment when in scope -->
	<%-- <div id="voucherDisplay">
		<c:if test="${not empty voucherDataList}">
			<h2>Top 5 Coupons</h2>
			<select name="voucherDisplaySelection" id="voucherDisplaySelection">
				<c:forEach var="voucherList" items="${voucherDataList}">
					<option value="${voucherList.voucherCode}">${voucherList.voucherCode} ${voucherList.voucherDescription}</option>	
				</c:forEach>
			</select>
		</c:if>
	</div> --%>
		
</div>
