<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/responsive/storepickup"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="pagination" tagdir="/WEB-INF/tags/responsive/nav/pagination" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/nav" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/product" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

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
<input type="hidden" id="search_type" value="${searchType}"><!-- For TPR-666 -->
<input type="hidden" id="product_category" value="${product_category}"> <!-- For TPR-430 -->
<input type="hidden" id="page_subcategory_name" value="${page_subcategory_name}">
<input type="hidden" id="page_subcategory_name_l3" value="${page_subcategory_name_l3}"> 
	<!-- For TPR-666 -->
<!-- End -->
<c:choose>
	<c:when test="${lazyInterface}">
			<nav:luxurySearchFacetFilterData/>
</c:when>
<c:otherwise>
	<template:page pageTitle="${pageTitle}">
		<section>
			<div class="container-fluid plp-banner">
				<div class="plp-banner-wrapper">
					<cms:pageSlot position="LuxPLPBannerPosition" var="feature" element="div" class="span-24 section5 cms_disp-img_slot">
						<cms:component component="${feature}" />
					</cms:pageSlot>
				</div>
			</div>
			<cms:pageSlot position="LuxPLPVisualFilterPosition" var="feature" element="div" class="span-24 section5 cms_disp-img_slot">
				<cms:component component="${feature}" />
			</cms:pageSlot>
			<nav:luxurySearchFacetFilterData/>
		</section>
	</template:page>
</c:otherwise>
</c:choose>

