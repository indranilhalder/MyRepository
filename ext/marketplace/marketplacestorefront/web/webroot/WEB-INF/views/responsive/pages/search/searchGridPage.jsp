<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/responsive/storepickup"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="pagination" tagdir="/WEB-INF/tags/responsive/nav/pagination" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set value="${(searchPageData.pagination.currentPage * searchPageData.pagination.pageSize) + 1}" var="currentPageStart"/>
<c:set value="${(searchPageData.pagination.currentPage + 1) * searchPageData.pagination.pageSize}" var="currentPageEnd"/>
<c:if test="${currentPageEnd > searchPageData.pagination.totalNumberOfResults}">
    <c:set value="${searchPageData.pagination.totalNumberOfResults}" var="currentPageEnd"/>
</c:if>
<!-- Tealium hidden fields -->
<input type="hidden" id="search_keyword" value="${searchPageData.freeTextSearch}">
<input type="hidden" id="searchCategory" value="${searchCategory}">
<input type="hidden" id="search_results" value="${searchPageData.pagination.totalNumberOfResults}">
<input type="hidden" id="page_name" value="${page_name}">
<!-- End -->
<template:page pageTitle="${pageTitle}">
	<div class="listing wrapper">
		<div class="search-result">
			<h2>
				<c:choose>
					<c:when test="${not empty searchPageData.spellingSuggestion.suggestion}">
						0 <spring:theme code="search.page.searchText"/> '<span class="searchString"><spring:theme code="search.page.searchTextValue"
							arguments="${searchPageData.freeTextSearch}" /></span>', <spring:theme code="search.page.searchResultsCount"
							arguments="${currentPageStart},${currentPageEnd},${searchPageData.pagination.totalNumberOfResults}" />
						results in 
						<span class="searchString"><i>
						
						<c:set value="${fn:split(searchPageData.freeTextSearch, ' ')}" var="searchTextAry" />
						<c:set value="${fn:split(searchPageData.spellingSuggestion.suggestion, ' ')}" var="suggestionAry" />
						<c:choose>
							<c:when test="${fn:length(suggestionAry) > 1}">
								<c:forEach begin="0" end="${fn:length(suggestionAry)}" var="current">
									<c:choose>
										<c:when test="${suggestionAry[current] ne searchTextAry[current]}">
											<strike>${searchTextAry[current]}</strike>&nbsp; 
										</c:when>
										<c:otherwise>
											<c:out value="${searchTextAry[current]}"/>&nbsp;
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<spring:theme code="search.page.searchTextValue"
							arguments="${searchPageData.spellingSuggestion.suggestion}" />
							</c:otherwise>
						</c:choose>
						
							
							
							</i></span>
					</c:when>
					<!--<c:otherwise>
						<spring:theme code="search.page.searchResultsCount"
							arguments="${currentPageStart},${currentPageEnd},${searchPageData.pagination.totalNumberOfResults}" />
						<spring:theme code="search.page.searchText"/>
						"<span class="searchString"><spring:theme code="search.page.searchTextValue"
							arguments="${searchPageData.freeTextSearch}" /></span>"
					</c:otherwise> -->
					<!-- Changes for Search result text changes -->
					<c:otherwise>
					 <spring:theme code="search.page.searchText"/>	
					 <span>"<spring:theme code="search.page.searchResultsCount" arguments="${searchPageData.pagination.totalNumberOfResults}"/>"</span> 
					  <spring:theme code="search.page.searchTextItem"/>
					  <span class="searchString">					
					 "<spring:theme code="search.page.searchTextValue" arguments="${searchPageData.freeTextSearch}"/>"</span>
					</c:otherwise>
				</c:choose>
			</h2>
		</div>
	
		<div class="left-block">
		<%-- <cms:pageSlot position="ProductLeftRefinements" var="feature">
			<cms:component component="${feature}" />
		</cms:pageSlot> --%>
		<product:productrefinementcomponent/>
		</div>

		<%-- <cms:pageSlot position="SearchResultsGridSlot" var="feature">
			<cms:component component="${feature}" />
		</cms:pageSlot> --%>
		<product:searchresultsgridcomponent/>
		
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

