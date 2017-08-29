<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="format" required="true" type="java.lang.String" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<%-- <c:set value="${ycommerce:productImage(product, format)}" var="primaryImage"/> --%>
<c:set value="${ycommerce:productImage(product, format)}" var="primaryImage"/>
<c:set var="imgClass" value="plp-default-img"/>
<c:if test="${format eq 'luxuryModel'}">
	<c:set var="imgClass" value="plp-model-img"/>
</c:if>
<c:if test="${format eq 'luxurySecondary'}">
	<c:set var="imgClass" value="plp-hover-img"/>
</c:if>
<c:choose>
	<c:when test="${not empty primaryImage && not empty primaryImage.url}">
		<c:choose>
			<c:when test="${not empty primaryImage.altText}">
				<img class="${imgClass}" src="${primaryImage.url}" data-zoom-image="" alt="${fn:escapeXml(primaryImage.altText)}" title="${fn:escapeXml(primaryImage.altText)}"/>
			</c:when>
			<c:otherwise>
				<img class="${imgClass}" src="${primaryImage.url}" data-zoom-image="" alt="${fn:escapeXml(product.name)}" title="${fn:escapeXml(product.name)}"/>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<theme:luxuryImage imgClass="${imgClass}" code="img.missingProductImage.product" alt="${fn:escapeXml(product.name)}" title="${fn:escapeXml(product.name)}"/>
	</c:otherwise>
</c:choose>

