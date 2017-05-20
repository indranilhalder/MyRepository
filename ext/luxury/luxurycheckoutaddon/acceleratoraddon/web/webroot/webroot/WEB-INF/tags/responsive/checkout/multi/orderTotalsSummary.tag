<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="showTax" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showTaxEstimate" required="false" type="java.lang.Boolean" %>
<%@ attribute name="subtotalsCssClasses" required="false" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template/compressible/js.tag"%>
	
<div class="subtotals ${subtotalsCssClasses}">
	<div class="subtotal-headline"><spring:theme code="order.order.totals"/></div>

	<div class="subtotal">
		<spring:theme code="basket.page.totals.subtotal"/> 
		<span>
			<ycommerce:testId code="Order_Totals_Subtotal">
				<format:price priceData="${cartData.subTotal}"/>
			</ycommerce:testId>
		</span>
	</div>

	<c:if test="${not empty cartData.deliveryCost}">
		<div class="shipping">
			<spring:theme code="basket.page.totals.delivery"/>
			<span>
				<ycommerce:testId code="Order_Totals_Delivery">
					<format:price priceData="${cartData.deliveryCost}" displayFreeForZero="TRUE"/>
				</ycommerce:testId>
			</span>
		</div>
	</c:if>
	<c:if test="${paymentMode eq 'COD'}">
	<c:if test="${cartData.convenienceChargeForCOD ne 0}">
		<div id="convChargeFieldId" style="display:block">
			<spring:theme code="basket.page.totals.convenience"/>
			<span>
				${cartData.convenienceChargeForCOD}
			</span>
		</div>
	</c:if>
	<c:if test="${cartData.convenienceChargeForCOD eq 0}">
		<div id="convChargeFieldId" style="display:block">
			<spring:theme code="basket.page.totals.convenience"/>
			<span>
				FREE
			</span>
		</div>
	</c:if>
	</c:if>
	
	<c:if test="${cartData.totalDiscounts.value > 0}">
            <div class="discount"><spring:theme code="basket.page.totals.savings"/></div>
            <div class=" discount">
                -<ycommerce:testId code="Order_Totals_Savings"><format:price priceData="${cartData.totalDiscounts}"/></ycommerce:testId>
            </div>         
    </c:if>
	
	<div class="totals">
		<div id="totalPriceConvChargeId">
			<spring:theme code="basket.page.totals.total"/> 
			<span>
				${cartData.totalPriceWithConvCharge}
			</span>
		</div>
	</div>
	
	<c:if test="${cartData.net && cartData.totalTax.value > 0 && showTax}">
		<div class="tax">
			<spring:theme code="basket.page.totals.netTax"/>
			<span>
				<format:price priceData="${cartData.totalTax}"/>
			</span>
		</div>
	</c:if>
	
	<c:if test="${not cartData.net}">
		<div class="realTotals">
			<ycommerce:testId code="cart_taxes_label">
				<p>
					<spring:theme code="basket.page.totals.grossTax" arguments="${cartData.totalTax.formattedValue}" argumentSeparator="!!!!"/>
				</p>
			</ycommerce:testId>
		</div>
	</c:if>
	
	<c:if test="${cartData.net && not showTax }"> 
		<div class="realTotals">
			<ycommerce:testId code="cart_taxes_label">
				<p>
					<spring:theme code="basket.page.totals.noNetTax"/>
				</p>
			</ycommerce:testId>
		</div>
	</c:if>

</div>
	
