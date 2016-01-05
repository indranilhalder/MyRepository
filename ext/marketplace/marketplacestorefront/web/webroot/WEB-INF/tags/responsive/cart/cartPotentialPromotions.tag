<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>


<!-- TODO needs responsive classes -->
<!-- Issue created -->
<%-- <c:set var="promotions" value="cartData.potentialOrderPromotions">${promotions.description}</c:set> --%>
 <c:forEach items="${cartData.potentialOrderPromotions}" var="promotioncheck">
         <c:set var="showmessege" value="${promotioncheck.description}"/>
  </c:forEach>
<c:if test="${not empty showmessege}">
<div class="cartPromotions">
    <ycommerce:testId code="potentialPromotions_promotions_labels">
    <div class="promotions cartpotproline"><spring:theme code="basket.potential.promotions" /></div>
        <c:forEach items="${cartData.potentialOrderPromotions}" var="promotion">
            <div class="cartpotproline_desc">${promotion.description}</div>
        </c:forEach>
    </ycommerce:testId>
    </div>
    
</c:if>

