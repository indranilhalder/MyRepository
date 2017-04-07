<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<input type="hidden"  id="page_name" value="${page_name}"/>
<input type="hidden" id="page_category_name" value="${dropDownText}"/>
<input type="hidden" id="categoryId" value="${categoryCode}"/>
<input type="hidden" id="site_section" value="${site_section}"/>
<!-- TPR-430 -->
<input type="hidden" id="product_category" value="${product_category}"/>
<input type="hidden" id="page_subcategory_name" value="${page_subcategory_name}"/>
<input type="hidden" id="page_subcategory_name_l3" value="${page_subcategory_name_l3}"/>
<!-- UF-15-16 -->
<c:choose>
	<c:when test="${lazyInterface}">
	<div id="productGrid">	<!-- Div to be overridden by AJAX response : TPR-198 --> 
	<c:if test="${searchPageData.pagination.totalNumberOfResults ne 0}">
	<div class="left-block">
			<cms:pageSlot position="ProductLeftRefinements" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
	</div>
	</c:if>
	
		<cms:pageSlot position="ProductGridSlot" var="feature">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
	</div>
</c:when>
<c:otherwise>
<template:page pageTitle="${pageTitle}">
<div class="list_title"><h1>${dropDownText}</h1></div>
<div class="listing wrapper">
	<div class="search-result">
			<h2>&nbsp;</h2>
	</div>
	<div id="productGrid">	<!-- Div to be overridden by AJAX response : TPR-198 --> 
	<c:if test="${searchPageData.pagination.totalNumberOfResults ne 0}">
	<div class="left-block">
			<cms:pageSlot position="ProductLeftRefinements" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
	</div>
	</c:if>
	
		<cms:pageSlot position="ProductGridSlot" var="feature">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
	</div>

	
</div>
	<product:productCompare/>
	<storepickup:pickupStorePopup />
	
	<!-- For Infinite Analytics Start -->
			<div class="trending"  id="ia_products_recent"></div>
	<!-- For Infinite Analytics End -->
	
	
</template:page>
</c:otherwise>
</c:choose>


