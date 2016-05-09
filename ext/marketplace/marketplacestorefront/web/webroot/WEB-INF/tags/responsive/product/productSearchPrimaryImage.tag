<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="product" required="true" type="de.hybris.platform.commercefacades.product.data.ProductData" %>
<%@ attribute name="format" required="true" type="java.lang.String" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:set value="${ycommerce:productImage(product, format)}" var="primaryImage"/>

<c:choose>
	<c:when test="${not empty primaryImage && not empty primaryImage.url}">
		<c:choose>
			<c:when test="${not empty primaryImage.altText}">
<img class="picZoomer-pic" src="${contextPath}/_ui/desktop/theme-blue/images/missing-product-300x300.jpg" data-searchimgsrc="${primaryImage.url}" data-zoom-image="" alt="${fn:escapeXml(primaryImage.altText)}" title="${fn:escapeXml(primaryImage.altText)}"/>
			</c:when>
			<c:otherwise>
<img class="picZoomer-pic" src="${contextPath}/_ui/desktop/theme-blue/images/missing-product-300x300.jpg" data-searchimgsrc="${primaryImage.url}" data-zoom-image="" alt="${fn:escapeXml(product.name)}" title="${fn:escapeXml(product.name)}"/>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<theme:image code="img.missingProductImage.product" alt="${fn:escapeXml(product.name)}" title="${fn:escapeXml(product.name)}"/>
	</c:otherwise>
</c:choose>