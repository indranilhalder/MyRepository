<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/responsive/storepickup"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="pagination" tagdir="/WEB-INF/tags/responsive/nav/pagination" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>

<c:set value="${(searchPageData.pagination.currentPage * searchPageData.pagination.pageSize) + 1}" var="currentPageStart"/>
<c:set value="${(searchPageData.pagination.currentPage + 1) * searchPageData.pagination.pageSize}" var="currentPageEnd"/>
<c:if test="${currentPageEnd > searchPageData.pagination.totalNumberOfResults}">
    <c:set value="${searchPageData.pagination.totalNumberOfResults}" var="currentPageEnd"/>
</c:if>
<!-- Tealium hidden fields -->
<input type="hidden" id="search_keyword" value="${searchPageData.freeTextSearch}">
<input type="hidden" id="searchCategory" value="${searchCategory}">
<input type="hidden" id="search_results" value="${currentPageEnd}">
<input type="hidden" id="page_name" value="${page_name}">
<!-- End -->
<template:page pageTitle="${pageTitle}">
	<div id="facetSearchAjaxData" class="listing wrapper"> <!-- Div to be overridden by AJAX response : TPR-198 -->
		<nav:searchFacetFilterData/>
	</div>

	<div class="feedBack-block">
		<%-- <cms:pageSlot position="FeedBackSlot" var="feature">
			<cms:component component="${feature}" />
		</cms:pageSlot> --%>
		<product:feedBack/>
	</div>
	<product:productCompare/> 
	
	<!-- For Infinite Analytics Start -->
	<div class="trending"  id="ia_products_recent"></div>
	<!-- For Infinite Analytics End -->
	
	<%-- <div class="recentlyViewed-Wrapper">
			<div class="recentlyViewed img-responsive"><img src='${commonResourcePath}/images/recently-viewed-items.png'/>
	</div>
</div> --%>

	
	
	
	<storepickup:pickupStorePopup />

</template:page>
