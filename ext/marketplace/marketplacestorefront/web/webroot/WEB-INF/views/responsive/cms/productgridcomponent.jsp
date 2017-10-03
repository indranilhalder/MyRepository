<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="right-block">
	<nav:pagination top="true" supportShowPaged="${isShowPageAllowed}"
		supportShowAll="${isShowAllAllowed}"
		searchPageData="${searchPageData}"
		searchUrl="${searchPageData.currentQuery.url}"
		numberPagesShown="${numberPagesShown}" hide="true" />
		

<input type="hidden" name="noOfPages" value="${searchPageData.pagination.numberOfPages}"/>
	<!-- Hero product pane -->
	<c:if test="${not empty heroProducts}">
	 <!-- <h2>Shop Our Top Picks</h2> -->
	</c:if>
	<c:if test="${not empty heroProducts}">
	<ul class="product-listing product-grid hero_carousel">
		<c:forEach items="${heroProducts}" var="heroProduct"
			varStatus="status">
			<product:productListerGridHeroItem product="${heroProduct}" />
		</c:forEach>
	</ul>
	</c:if>

	<!-- Subtracted normal product pane -->
	<c:if test="${not empty normalProducts}">
	<ul class="product-listing product-grid lazy-grid-normal">
		<c:forEach items="${normalProducts}" var="product"
			varStatus="status">
			<product:productListerGridItem product="${product}" index="${status.index}"/>
		</c:forEach>
	</ul>
	</c:if>
	
	<c:if test="${not empty otherProducts}">
	<ul class="product-listing product-grid lazy-grid">
		<c:forEach items="${searchPageData.results}" var="product" varStatus="status">
			<product:productListerGridItem product="${product}" index="${status.index}"/>
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
		numberPagesShown="${numberPagesShown}" hide="true"/>
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
<input type="hidden" name="productGrid" value="1"/>
