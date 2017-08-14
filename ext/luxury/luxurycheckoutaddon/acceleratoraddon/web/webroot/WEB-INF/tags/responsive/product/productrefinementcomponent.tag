<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/nav" %>
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
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('department.count.L1')" var="deptL1"/>
    <spring:eval expression="T(de.hybris.platform.util.Config).getParameter('department.count.L2')" var="deptL2"/>
 	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('department.count.L3')" var="deptL3"/>
  	<input type="hidden" id="deptCountL1" value="${deptL1}"/>
    <input type="hidden" id="deptCountL2" value="${deptL2}"/>
    <input type="hidden" id="deptCountL3" value="${deptL3}"/>
	


<!-- Changes for TISPRO-796 -->
	<span class="toggle-filterSerp"  onclick="toggleFilter()">Filter By</span>
	<div class="mob-filter-wrapper">
	<div class="filter-title">Filter <a href="#nogo" class="filter-close"></a> </div>
	<!--<span class="toggle-filterSerp">Filter By </span>-->
<!-- End of changes for TISPRO-796 -->	
<ul id="product-facet " class="product-facet js-product-facet listing-leftmenu">

		<div class="facet-name js-facet-name facet_mobile">
				<c:if test="${empty hideDepartments}">
				<h3 class="true tree-dept"><span class="filter-nav"><spring:theme code="search.nav.facetTitle" arguments="Department"/></span><span class="category-icons"><span></span></span></h3>
				</c:if>
		</div>
		<div class="facet-name js-facet-name facet_desktop">
				<c:if test="${empty hideDepartments}">
				<h3 class="true active tree-dept"><spring:theme code="search.nav.facetTitle" arguments="Department"/></h3>
				</c:if>
		</div>
		<c:choose>
		<c:when test="${isCategoryPage}">
			<form id="categoryPageDeptHierTreeForm" name="categoryPageDeptHierTreeForm" method="get">
			<p class="filter-name facet_mobile"><spring:theme code="search.nav.facetTitle" arguments="Department"/></p>
				<input type="hidden" name="q" id="q" value="${searchPageData.currentQuery.query.value}"/>
				<div id="categoryPageDeptHierTree"></div>
			</form>
		</c:when>		
		<c:otherwise>
			<form id="searchPageDeptHierTreeForm" method="get">
			<p class="filter-name facet_mobile"><spring:theme code="search.nav.facetTitle" arguments="Department"/></p>
				<input type="hidden" name="q" id="q" value="${searchPageData.currentQuery.query.value}"/>
				<input type="hidden" name="text" id="text" value="${searchPageData.freeTextSearch}"/>
				<input type="hidden" name="searchCategory" id="searchCategoryTree"/>				
				<div id="searchPageDeptHierTree"></div>				
				<div id="displayAll"> + Show all</div>
				<div id="clickToMore"> - Hide</div>
			</form>
		</c:otherwise>
		</c:choose>


	    <nav:facetNavAppliedFilters pageData="${searchPageData}"/>
	    <nav:facetNavRefinements pageData="${searchPageData}"/>
</ul>
</div>
