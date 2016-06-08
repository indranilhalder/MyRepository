<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:if test="${not empty component.layout}">
	<c:set var="cssClass"
		value="brandClass" />
	<c:if test="${component.layout eq 'AtoZ'}">
		<c:set var="cssClass" value="A-ZBrands" />
		<input type="hidden" id="componentUid" value="${component.uid}"/>
	</c:if>
	<div class="toggle ${cssClass}">
		<c:url var="masterBrandUrl" value="${component.masterBrandURL}" />
		<a class="${cssClass}" href="${masterBrandUrl}" id="${component.uid}">${component.masterBrandName}</a>
	</div>

	<!-- Rendering the sub-brand images if the layout is FiveBrandImages  -->
	<c:if test="${component.layout eq 'FiveBrandImages'}">
    <ul class="images"></ul>
    </c:if>
    
     <c:if test="${component.layout eq 'OneCloumnOneBrandImage'}">
     <ul id="kids" class="images">
     </ul>
     </c:if>
	<!-- Rendering the brands alphabetically if the layout is AtoZ -->
	<c:if test="${component.layout eq 'AtoZ'}">
		<ul class="a-z">
			<li class="short images" id="atozbrandsdiplay">
			<div class="view_brands">
				<a href="${brandlistUrl}"><h4>
						<b> <spring:theme code="navigation.brand.viewAllBrands" />

						</b>
					</h4></a>
			</div>
			</li>
		</ul>
	</c:if>
</c:if>
