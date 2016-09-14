<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
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

//TPR-158 and 413 starts here
//alert( "document loaded" );
//load limited department hierarchy on page load
$(document).ready(function(){
	$("#displayAll").show();
	$("#clickToMore").hide();
	donotShowAll();		
});
//TPR-158 and 413 ends here
</script>

	<input type="hidden" name="isConceirge" id="isConceirge" value="${isConceirge}" />
	<input type="hidden" name="isCategoryPage" id="isCategoryPage" value="${isCategoryPage}" />
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('department.count.L1')" var="deptL1"/>
    <spring:eval expression="T(de.hybris.platform.util.Config).getParameter('department.count.L2')" var="deptL2"/>
 	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('department.count.L3')" var="deptL3"/>
  	<input type="hidden" id="deptCountL1" value="${deptL1}"/>
    <input type="hidden" id="deptCountL2" value="${deptL2}"/>
    <input type="hidden" id="deptCountL3" value="${deptL3}"/>
	
	<span class="toggle-filterSerp">Filter By</span>
<ul id="product-facet " class="product-facet js-product-facet listing-leftmenu">

		<div class="facet-name js-facet-name">
				<c:if test="${empty hideDepartments}">
				<h4 class="true active tree-dept"><spring:theme code="search.nav.facetTitle" arguments="Department"/></h4>
				</c:if>
		</div>
		<c:choose>
		<c:when test="${isCategoryPage}">
			<form id="categoryPageDeptHierTreeForm" name="categoryPageDeptHierTreeForm" method="get">
				<input type="hidden" name="q" id="q" value="${searchPageData.currentQuery.query.value}"/>
				<div id="categoryPageDeptHierTree"></div>
			</form>
		</c:when>
		<c:otherwise>
			<form id="searchPageDeptHierTreeForm" method="get">
				<input type="hidden" name="q" id="q" />
				<input type="hidden" name="text" id="text" value="${searchPageData.freeTextSearch}"/>
				<input type="hidden" name="searchCategory" id="searchCategoryTree"/>
				<div id="searchPageDeptHierTree"></div>
				<div id="displayAll"> Show all</div>
				<div id="clickToMore"> Hide</div>
			</form>
		</c:otherwise>
		</c:choose>


	    <nav:facetNavAppliedFilters pageData="${searchPageData}"/>
	    <nav:facetNavRefinements pageData="${searchPageData}"/>
</ul>
