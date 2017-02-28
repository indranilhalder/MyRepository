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
<input type="hidden" id="search_type" value="${searchType}">
<input type="hidden" id="mSeller_name" value="${mSellerName}"> <!-- TPR-4471 -->
<input type="hidden" id="mSellerID" value="${mSellerID}"> <!-- TPR-4471 -->

<template:page pageTitle="${pageTitle}">

<!-- TPR-4471 Starts -->
<c:url value="${param}" var="paramUrl" />

<c:if test="${fn:contains(paramUrl,'mSellerID')}">
<div class="productGrid-header-wrapper">
<div class="productGrid-header">
<div class="productGrid-menu">
 <nav>
					<ul>
						<c:if test="${empty showOnlySiteLogo }">
						
									<cms:pageSlot position="ProductGridMenu" var="component">
										<cms:component component="${component}" />
									</cms:pageSlot>
								
							</c:if>
					</ul>
				</nav>
				</div>
<div class="productGrid-logo">
			<cms:pageSlot position="ProductGridLogo" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
	</div>
	
	
	<div class="product-grid-search">
			<cms:pageSlot position="ProductGridSearch" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
	</div>
	

	<div class="bag">
					<a href="/cart" class="mini-cart-link myBag-sticky"
						data-mini-cart-url="/cart/rollover/MiniCart"
						data-mini-cart-refresh-url="/cart/miniCart/SUBTOTAL"
						data-mini-cart-name="Cart" data-mini-cart-empty-name="Empty Cart"
						style="position: static;"></a>
			</div>
			</div>
			</div>
	</c:if>
	<!-- TPR-4471 Ends -->
	
	<div id="facetSearchAjaxData"> <!-- Div to be overridden by AJAX response : TPR-198 -->
		 <nav:searchFacetFilterData/> 
	</div>

	<div class="feedBack-block">
		
		<product:feedBack/>
	</div>
	<product:productCompare/> 
	
	<!-- For Infinite Analytics Start -->
	<div class="trending"  id="ia_products_recent"></div>
	

	
	
	
	<storepickup:pickupStorePopup />

</template:page>
