<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ attribute name="showTax" required="false" type="java.lang.Boolean" %>
<%@ attribute name="showTaxEstimate" required="false" type="java.lang.Boolean" %>
<%@ attribute name="subtotalsCssClasses" required="false" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
	
<div class="coupon block">
<h2>Apply Coupon Code</h2>
		<input type="text" id="couponFieldId" placeholder="Enter coupon code"/>
		<button type="submit" id="couponSubmitButton" class="button btn-block">Submit</button>
		<span class="error-message" id="couponError"></span>

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