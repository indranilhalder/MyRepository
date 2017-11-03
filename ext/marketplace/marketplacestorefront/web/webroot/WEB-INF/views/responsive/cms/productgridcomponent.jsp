<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

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

<div class="bottom-pagination pagination-search">
<div class="">
<c:if test="${searchPageData.pagination.numberOfPages > 1}">
<span class="">
<span class="total-pagecount">Pages <span id="pageOf">1</span> of ${searchPageData.pagination.numberOfPages}</span>
</span>
</c:if>
<c:choose>
<c:when test="${searchPageData.pagination.numberOfPages > 1}">
<div class="prev-block"><a href=""><span class="prev-page">Previous</span></a></div>
    <ul class="pagination-block">
    	<c:forEach begin="1" end="${searchPageData.pagination.numberOfPages}" var="page" varStatus="loop">
    	<c:choose>
    		<c:when test="${loop.index eq 1}">
    		<c:choose>
    			<c:when test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'page')}">
    			<c:set var="splittedURL" value="${fn:split(requestScope['javax.servlet.forward.request_uri'] , '/' )}"></c:set>
    			<li class="pageNoLi"><a class="pageNo active" href="/${splittedURL[0]}/${splittedURL[1]}/page-${page}">${page}</a></li>
    			</c:when>
    			<c:otherwise>
    			<li class="pageNoLi"><a class="pageNo active" href="/${requestScope['javax.servlet.forward.request_uri']}/page-${page}">${page}</a></li>
    			</c:otherwise>
    		</c:choose>
    		
    		</c:when>
    		<c:otherwise>
    		<c:choose>
    			<c:when test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'page')}">
    			<c:set var="splittedURL" value="${fn:split(requestScope['javax.servlet.forward.request_uri'] , '/' )}"></c:set>
    			<li class="pageNoLi"><a class="pageNo" href="/${splittedURL[0]}/${splittedURL[1]}">${page}</a></li>
    			</c:when>
    			<c:otherwise>
    			<li class="pageNoLi"><a class="pageNo" href="/${requestScope['javax.servlet.forward.request_uri']}/page-${page}">${page}</a></li>
    			</c:otherwise>
    		</c:choose>
    		</c:otherwise>
    	</c:choose>
        </c:forEach>
    </ul>
    <div class="next-block"><a href="#next-page"><span>Next</span></a></div>
</c:when>
</c:choose>
</div>
</div>

<script>
	$(document).ready(function(){
		$.each($(".facet-name js-facet-name").find("h3"),function(){
			if($(this).text() == "Department"){
				$(this).remove();
			}
		});
		
	});
var currentPageNoFrom = "${pageNo}"; 	
</script>
<input type="hidden" name="productGrid" value="1"/>
