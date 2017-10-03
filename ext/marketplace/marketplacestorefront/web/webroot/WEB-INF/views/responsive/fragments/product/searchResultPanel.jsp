<!-- AJAX VIEW FOR CATEGORY PAGE CONTROLLER (PLP) : TPR-198 -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<input type="hidden" name="noOfPages" value="${searchPageData.pagination.numberOfPages}"/>
<c:if test="${searchPageData.pagination.totalNumberOfResults ne 0}">

	<div class="left-block">

		<!-- <script>
		
		$(function() {
			var inputArray = ${departmentHierarchyData.hierarchyList};
			
			if(inputArray!=""||inputArray!=[]){
			constructDepartmentHierarchy(inputArray);
			}
		});
			
			
		</script> -->
		
			<input type="hidden" name="isConceirge" id="isConceirge" value="${isConceirge}" />
			<input type="hidden" name="isCategoryPage" id="isCategoryPage" value="${isCategoryPage}" />
		
			<span class="toggle-filterSerp"  onclick="toggleFilter()">Filter By</span>
			<div class="mob-filter-wrapper">
	<div class="filter-title">Filter <a href="#nogo" class="filter-close"></a> </div>
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
						<input type="hidden" name="searchCategory" value="${searchCategory}" />
						<div id="categoryPageDeptHierTree"></div>
					</form>
				</c:when>
				<c:otherwise>
					<form id="searchPageDeptHierTreeForm" method="get">
					<p class="filter-name facet_mobile"><spring:theme code="search.nav.facetTitle" arguments="Department"/></p>
						<input type="hidden" name="q" id="q" />
						<input type="hidden" name="text" id="text" value="${searchPageData.freeTextSearch}"/>
						<input type="hidden" name="searchCategory" id="searchCategoryTree" value="${searchCategory}"/>
						<div id="searchPageDeptHierTree"></div>
					</form>
				</c:otherwise>
				</c:choose>
		
		
			    <nav:facetNavAppliedFilters pageData="${searchPageData}"/>
			    <nav:facetNavRefinements pageData="${searchPageData}"/>
		</ul>
</div>

	</div>
</c:if>



<div class="right-block">
	<nav:pagination top="true" supportShowPaged="${isShowPageAllowed}"
		supportShowAll="${isShowAllAllowed}"
		searchPageData="${searchPageData}"
		searchUrl="${searchPageData.currentQuery.url}"
		numberPagesShown="${numberPagesShown}" hide="true"/>

	<!-- Hero product pane -->
	<c:if test="${not empty heroProducts}">
	 <!-- <h2>Shop Our Top Picks</h2> -->
	</c:if>
	<ul class="product-listing product-grid hero_carousel">
		<c:forEach items="${heroProducts}" var="heroProduct"
			varStatus="status">
			<product:productListerGridHeroItem product="${heroProduct}" />
		</c:forEach>
	</ul>


	<!-- Subtracted normal product pane -->

	<ul class="product-listing product-grid lazy-grid-facet">
		<c:forEach items="${normalProducts}" var="product"
			varStatus="status">
			<product:productListerGridItem product="${product}" />
		</c:forEach>
	</ul>
	
	<c:if test="${not empty otherProducts}">
	<ul class="product-listing product-grid lazy-grid">
		<c:forEach items="${searchPageData.results}" var="product" varStatus="status">
			<product:productListerGridItem product="${product}"/>
		</c:forEach>
	</ul> 
	</c:if>

	<div id="addToCartTitle" style="display: none">
		<div class="add-to-cart-header">
			<div class="headline">
				<span class="headline-text"><spring:theme
						code="basket.added.to.basket" /></span>
			</div>
		</div>
	</div>
</div>
<div class="bottom-pagination" style="display: none;">
	<nav:pagination top="false" supportShowPaged="${isShowPageAllowed}"
		supportShowAll="${isShowAllAllowed}"
		searchPageData="${searchPageData}"
		searchUrl="${searchPageData.currentQuery.url}"
		numberPagesShown="${numberPagesShown}"/>
</div>
<script>
	$(document).ready(function(){
		$.each($(".facet-name js-facet-name").find("h3"),function(){
			if($(this).text() == "Department"){
				$(this).remove();
			}
		});
		
	});
</script>
