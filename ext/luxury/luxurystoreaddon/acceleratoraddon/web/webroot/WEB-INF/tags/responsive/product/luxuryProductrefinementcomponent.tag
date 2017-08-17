<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/nav" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%-- <%@ page import="com.tisl.mpl.data.MplDepartmentHierarchyData"%> --%>


	<input type="hidden" name="isConceirge" id="isConceirge" value="${isConceirge}" />
	<input type="hidden" name="isCategoryPage" id="isCategoryPage" value="${isCategoryPage}" />
	
	<c:if test="${not empty searchPageData.breadcrumbs}">
		<nav:luxuryFacetNavAppliedFilters pageData="${searchPageData}"/>
	</c:if>
	
	<div class="facetList">
	    <nav:luxuryFacetNavRefinements pageData="${searchPageData}"/>
	</div>

	<c:choose>
		<c:when test="${isCategoryPage}">
			<form id="categoryPageDeptHierTreeForm" name="categoryPageDeptHierTreeForm" method="get">
			<%-- <p class="filter-name facet_mobile"><spring:theme code="search.nav.facetTitle" arguments="Department"/></p> --%>
				<input type="hidden" name="q" id="q" value="${searchPageData.currentQuery.query.value}"/>
				<div id="categoryPageDeptHierTree"></div>
			</form>
		</c:when>		
		<c:otherwise>
			<form id="searchPageDeptHierTreeForm" method="get">
				<input type="hidden" name="q" id="q" value="${searchPageData.currentQuery.query.value}"/>
				<input type="hidden" name="text" id="text" value="${searchPageData.freeTextSearch}"/>
				<input type="hidden" name="site" id="siteId" value="lux"/>
				<input type="hidden" name="searchCategory" id="searchCategoryTree"/>				
				<div id="searchPageDeptHierTree"></div>
			</form>
		</c:otherwise>
	</c:choose>
