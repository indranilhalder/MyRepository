<!-- AJAX VIEW FOR CATEGORY PAGE CONTROLLER (PLP) : TPR-198 -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:if test="${searchPageData.pagination.totalNumberOfResults ne 0}">
	<div class="left-block">
		<input type="hidden" name="isConceirge" id="isConceirge"
			value="${isConceirge}" /> <input type="hidden" name="isCategoryPage"
			id="isCategoryPage" value="${isCategoryPage}" /> <span
			class="toggle-filterSerp">Filter</span>
			<div class="mob-filter-wrapper">
	<div class="filter-title">Filter <a href="#nogo" class="filter-close"></a> </div>
		<ul id="product-facet" class="product-facet js-product-facet listing-leftmenu">
			<nav:facetNavAppliedFilters pageData="${searchPageData}" />
			<nav:facetNavRefinements pageData="${searchPageData}" />
		</ul>

</div>
	</div>
</c:if>

<div class="right-block">
	<nav:pagination top="true" supportShowPaged="${isShowPageAllowed}"
		supportShowAll="${isShowAllAllowed}"
		searchPageData="${searchPageData}"
		searchUrl="${searchPageData.currentQuery.url}"
		numberPagesShown="${numberPagesShown}" />

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

	<ul class="product-listing product-grid">
		<c:forEach items="${normalProducts}" var="product" varStatus="status">
			<product:productListerGridItem product="${product}" />
		</c:forEach>
	</ul>

	<c:if test="${not empty otherProducts}">
		<ul class="product-listing product-grid">
			<c:forEach items="${searchPageData.results}" var="product"
				varStatus="status">
				<product:productListerGridItem product="${product}" />
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
<div class="bottom-pagination">
	<nav:pagination top="false" supportShowPaged="${isShowPageAllowed}"
		supportShowAll="${isShowAllAllowed}"
		searchPageData="${searchPageData}"
		searchUrl="${searchPageData.currentQuery.url}"
		numberPagesShown="${numberPagesShown}" />
</div>
<script>
	$(document).ready(function() {
		$.each($(".facet-name js-facet-name").find("h3"), function() {
			if ($(this).text() == "Department") {
				$(this).remove();
			}
		});

	});
</script>
