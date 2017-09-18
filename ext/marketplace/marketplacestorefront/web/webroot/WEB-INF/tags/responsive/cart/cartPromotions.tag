<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!--  needs responsive CSS classes; issue created -->
<c:if test="${not empty cartData.appliedOrderPromotions}">
<div class="cartPromotions">
    <%-- <div class="promotions cartproline "><spring:theme code="basket.received.promotions" /></div> --%>
    <ycommerce:testId code="cart_recievedPromotions_labels">
       <%-- <c:forEach items="${cartData.appliedOrderPromotions}" var="promotion">
            <div class="promotionDesc">${promotion.description}</div>
        </c:forEach> --%>
    </ycommerce:testId>
</div>
</c:if>


<!-- TPR-3893 starts here -->
<c:if test="${not empty cartData.appliedProductPromotions}">
    <div class="cartPromotions">
    <c:set var="alreadyPrinted" value="false"/>
		<c:forEach items="${cartData.appliedProductPromotions}" var="promotion">
			<c:if test="${fn:contains(promotion.promotionData.promotionType, 'Tata Etail - Bundling Promotion With Percentage Slabs') && promotion.description != null && promotion.description != '' && !alreadyPrinted}">
				<div class="promotions cartproline ">
					<spring:theme code="basket.received.promotions" />
				</div>
				<ycommerce:testId code="cart_recievedPromotions_labels">
					<div class="promotionDesc">${promotion.description}</div>
				</ycommerce:testId>
				<c:set var="alreadyPrinted" value="true"/>
			</c:if>
		</c:forEach>

     </div>
</c:if>
<!-- TPR-3893 ends here -->
