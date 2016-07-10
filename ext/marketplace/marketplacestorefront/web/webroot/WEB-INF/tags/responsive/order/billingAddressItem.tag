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


<c:if test="${not empty order.paymentInfo.billingAddress}">
	 <p class="title"><spring:theme code="paymentMethod.billingAddress.header"/></p>
    
      <address>
            <c:if test="${not empty order.paymentInfo.billingAddress.title}">
                ${fn:escapeXml(order.paymentInfo.billingAddress.title)}&nbsp;<br>
            </c:if>
            ${fn:escapeXml(order.paymentInfo.billingAddress.firstName)}&nbsp;${fn:escapeXml(order.paymentInfo.billingAddress.lastName)}<br>      
       ${fn:escapeXml(order.paymentInfo.billingAddress.line1)},&nbsp;${fn:escapeXml(order.paymentInfo.billingAddress.line2)},&nbsp;${fn:escapeXml(order.paymentInfo.billingAddress.line3)},<br>
        ${fn:escapeXml(order.paymentInfo.billingAddress.region.name)} ${fn:escapeXml(order.paymentInfo.billingAddress.town)},&nbsp;${fn:escapeXml(order.paymentInfo.billingAddress.state)},&nbsp;${fn:escapeXml(order.paymentInfo.billingAddress.postalCode)}&nbsp; 
        ${fn:escapeXml(order.paymentInfo.billingAddress.country.isocode)}   <br>
</address>
</c:if>
