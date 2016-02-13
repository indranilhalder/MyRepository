<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="com.tisl.mpl.data.MplDepartmentHierarchyData"%>

<script>
$(function() {
	var inputArray = ${departmentHierarchyData.hierarchyList};
	constructDepartmentHierarchy(inputArray);
	});
	
	
$(document).ready(function(){
	 $(".facet-name.js-facet-name h4").each(function(){
		if($(this).hasClass("false")){
	    	$(this).parent().siblings('#searchPageDeptHierTreeForm').find("#searchPageDeptHierTree").hide(100);
	    	$(this).parent().siblings('#categoryPageDeptHierTreeForm').find("#categoryPageDeptHierTree").hide(100);
		}
	}); 
});
	
</script>

	<input type="hidden" name="isConceirge" id="isConceirge" value="${isConceirge}" />
	<input type="hidden" name="isCategoryPage" id="isCategoryPage" value="${isCategoryPage}" />

	<span class="toggle-filterSerp">Filter By</span>
<ul id="product-facet " class="product-facet js-product-facet listing-leftmenu">

		<div class="facet-name js-facet-name">
				<h4 class="false tree-dept"><spring:theme code="search.nav.facetTitle" arguments="department"/></h4>
		</div>
		<c:choose>
		<c:when test="${isCategoryPage}">
			<form id="categoryPageDeptHierTreeForm" name="categoryPageDeptHierTreeForm" method="get">
				<div id="categoryPageDeptHierTree"></div>
			</form>
		</c:when>
		<c:otherwise>
			<form id="searchPageDeptHierTreeForm" method="get">
				<input type="hidden" name="q" id="q" />
				<input type="hidden" name="text" id="text" value="${searchPageData.freeTextSearch}"/>
				<input type="hidden" name="searchCategory" id="searchCategoryTree"/>
				<div id="searchPageDeptHierTree"></div>
			</form>
		</c:otherwise>
		</c:choose>


	    <nav:facetNavAppliedFilters pageData="${searchPageData}"/>
	    <nav:facetNavRefinements pageData="${searchPageData}"/>
</ul>