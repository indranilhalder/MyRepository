<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/desktop/product" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<style>
.orderBoxalignment{
	margin-left: 15px ;
	
}
</style>

<c:set var="hasShippedItems" value="${order.deliveryItemsQuantity > 0}" />
<c:set var="myline2" value="${fn:trim(order.deliveryAddress.line2)}"/>
<c:set var="myline3" value="${fn:trim(order.deliveryAddress.line3)}"/>
<h1 style="margin-left: 15px"><spring:theme code="text.deliveryAddress" text="Delivery Details"/></h1>
<div class="orderBox address orderBoxalignment">
	<h4>Shipping Address</h4>
	<c:if test="${not hasShippedItems}">
		<spring:theme code="checkout.pickup.no.delivery.required"/>
	</c:if>
	<c:if test="${hasShippedItems}">
	
	<c:if test="${empty myline2  && empty myline3}">
	<address>
	
			${fn:escapeXml(order.deliveryAddress.title)}${fn:escapeXml(order.deliveryAddress.firstName)}&nbsp;${fn:escapeXml(order.deliveryAddress.lastName)}<br>
			${fn:escapeXml(order.deliveryAddress.line1)},<br>
		${fn:escapeXml(order.deliveryAddress.town)},&nbsp;${fn:escapeXml(order.deliveryAddress.region.name)}${fn:escapeXml(order.deliveryAddress.state)},&nbsp;
			${fn:escapeXml(order.deliveryAddress.postalCode)}&nbsp;
			${fn:escapeXml(order.deliveryAddress.country.isocode)}<br>
			<spring:theme code="checkout.phone.no" text="91"/>	&nbsp;${fn:escapeXml(order.deliveryAddress.phone)}
			
			</address>
	
	</c:if>
	
	<c:if test="${not empty myline2  && empty myline3}">
	
	<address>
	
			${fn:escapeXml(order.deliveryAddress.title)}${fn:escapeXml(order.deliveryAddress.firstName)}&nbsp;${fn:escapeXml(order.deliveryAddress.lastName)}<br>
			${fn:escapeXml(order.deliveryAddress.line1)},&nbsp;${fn:escapeXml(order.deliveryAddress.line2)},<br>
		${fn:escapeXml(order.deliveryAddress.town)},&nbsp;${fn:escapeXml(order.deliveryAddress.region.name)}${fn:escapeXml(order.deliveryAddress.state)},&nbsp;
			${fn:escapeXml(order.deliveryAddress.postalCode)}&nbsp;
			${fn:escapeXml(order.deliveryAddress.country.isocode)}<br>
			<spring:theme code="checkout.phone.no" text="91"/>	&nbsp;${fn:escapeXml(order.deliveryAddress.phone)}
			
			</address>
	
	</c:if>
	
	<c:if test="${ empty myline2  && not empty myline3}">
	
	<address>
	
			${fn:escapeXml(order.deliveryAddress.title)}${fn:escapeXml(order.deliveryAddress.firstName)}&nbsp;${fn:escapeXml(order.deliveryAddress.lastName)}<br>
			${fn:escapeXml(order.deliveryAddress.line1)},&nbsp;${fn:escapeXml(order.deliveryAddress.line3)},<br>
		${fn:escapeXml(order.deliveryAddress.town)},&nbsp;${fn:escapeXml(order.deliveryAddress.region.name)}${fn:escapeXml(order.deliveryAddress.state)},&nbsp;
			${fn:escapeXml(order.deliveryAddress.postalCode)}&nbsp;
			${fn:escapeXml(order.deliveryAddress.country.isocode)}<br>
			<spring:theme code="checkout.phone.no" text="91"/>	&nbsp;${fn:escapeXml(order.deliveryAddress.phone)}
			
			</address>
	
	</c:if>
	
	 <c:if test="${not empty myline2  && not empty myline3}">
	<address>
	
			${fn:escapeXml(order.deliveryAddress.title)}${fn:escapeXml(order.deliveryAddress.firstName)}&nbsp;${fn:escapeXml(order.deliveryAddress.lastName)}<br>
			${fn:escapeXml(order.deliveryAddress.line1)},&nbsp;${fn:escapeXml(order.deliveryAddress.line2)},&nbsp;${fn:escapeXml(order.deliveryAddress.line3)},<br>
		${fn:escapeXml(order.deliveryAddress.town)},&nbsp;${fn:escapeXml(order.deliveryAddress.region.name)}${fn:escapeXml(order.deliveryAddress.state)},&nbsp;
			${fn:escapeXml(order.deliveryAddress.postalCode)}&nbsp;
			${fn:escapeXml(order.deliveryAddress.country.isocode)}<br>
			<spring:theme code="checkout.phone.no" text="91"/>	&nbsp;${fn:escapeXml(order.deliveryAddress.phone)}
			
			</address>
			</c:if>
	</c:if>
	<c:if test="${totalCount > 1}"><p>${totalCount}&nbsp;<spring:theme code="confirmation.message.items" /></p></c:if>
    <c:if test="${totalCount == 1}"><p>${totalCount}&nbsp;<spring:theme code="confirmation.message.item" /></p></c:if> 
<%-- 	<p>${totalCount} ITEMS SHIPPING TO THE ADDRESS</p> --%>

</div>