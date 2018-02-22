<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/nav" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%-- <%@ page import="com.tisl.mpl.data.MplDepartmentHierarchyData"%> --%>

<script>
    $(function() {
        var inputArray = ${departmentHierarchyData.hierarchyList};
        if(inputArray!=""||inputArray!=[]){
            constructDepartmentHierarchy(inputArray);
        }
    });
</script>

	<input type="hidden" name="isConceirge" id="isConceirge" value="${isConceirge}" />
	<input type="hidden" name="isCategoryPage" id="isCategoryPage" value="${isCategoryPage}" />
	
	<c:if test="${not empty searchPageData.breadcrumbs}">
		<nav:luxuryFacetNavAppliedFilters pageData="${searchPageData}"/>
	</c:if>

	<div class="facetList">
		<div class="facet open">
		<div class="facetHead">
			<h4>
				<a class="" href="javascript:;">
					 <c:if test="${empty hideDepartments}">
						<spring:theme code="search.nav.facetTitle" arguments="Department"/>
					</c:if>
				</a>
				<span class="sprite sp-minus"></span>
			</h4>
		</div>
		<div class="facetValues">
			<div class="allFacetValues">
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
					<c:choose>
					<c:when test="${not empty searchCategory}">
					<input type="hidden" name="searchCategory" id="searchCategoryTree" value="${searchCategory}"/>
					</c:when>		
					<c:otherwise>
					<input type="hidden" name="searchCategory" id="searchCategoryTree"/>
					</c:otherwise>
					</c:choose>				
					<div id="searchPageDeptHierTree"></div>
					</form>
				</c:otherwise>
				</c:choose>
					</div>
				
			</div>
		</div>
	    <nav:luxuryFacetNavRefinements pageData="${searchPageData}"/>
	</div>

