<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>

<input type="hidden" name="noOfPages" value="${searchPageData.pagination.numberOfPages}"/>

<%-- <c:set value="${(searchPageData.pagination.currentPage * searchPageData.pagination.pageSize) + 1}" var="currentPageStart"/>
<c:set value="${(searchPageData.pagination.currentPage + 1) * searchPageData.pagination.pageSize}" var="currentPageEnd"/>

<c:if test="${currentPageEnd > searchPageData.pagination.totalNumberOfResults}">
    <c:set value="${searchPageData.pagination.totalNumberOfResults}" var="currentPageEnd"/>
</c:if> --%>
	<%-- <div class="results">
		<h1><center><spring:theme code="search.page.searchResultsCount" arguments="${currentPageStart},${currentPageEnd},${searchPageData.pagination.totalNumberOfResults}"/><spring:theme code="search.page.searchText" arguments="${searchPageData.freeTextSearch}"/></center></h1>
		<div class="bestSellingItems-Wrapper">
			<div class="bestSellingItems img-responsive"><img src='${commonResourcePath}/images/BestSellingItems.png'/>
		</div>
	</div>
</div> --%>
<div class="right-block">
	<nav:searchSpellingSuggestion spellingSuggestion="${searchPageData.spellingSuggestion}" />

	<nav:pagination top="true"  supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}"  searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"  numberPagesShown="${numberPagesShown}"/>
<!-- commented as a part of defect fix -->
<%-- <div class="best-selling">
  <h1><span><spring:theme code="text.best.sellers"/></span></h1>
  </div> --%>
  
  <!-- For Infinite Analytics Start -->
  		<input type="hidden" id="ia_search_text" value="${searchPageData.freeTextSearch}"> 
		<input type="hidden" id="selectedSearchCategoryId" value="${param.searchCategory}">
		<input type="hidden" id="selectedSearchCategoryIdMicrosite" value="${param.micrositeSearchCategory}">
		<input type="hidden" id="helpMeShopSearchCatId" value="${searchCategory}">
				<!--  <div class="best-selling"  id="ia_products_search"></div> -->		
				 	<!-- For Infinite Analytics End -->
	
  <!-- Competing Products Section -->
	<!--   INC144315908 starts -->
	<c:choose>
		<c:when test="${not empty competingProductsSearchPageData && not empty competingProductsSearchPageData.results}">
		<div class="similar-products">
		  <h2><span><spring:theme code="text.competing.products"/></span></h2>
		  	<ul class="product-list">
				<c:forEach items="${competingProductsSearchPageData.results}" var="product" varStatus="status">
					<product:productListerGridItem product="${product}" index="${status.index}"/>
				</c:forEach>
				<c:forEach items="${searchPageData.results}" var="product" varStatus="status">
					<product:productListerGridItem product="${product}" index="${status.index}"/>
				</c:forEach>
			</ul>
			</div>
		</c:when>
		<c:otherwise>
		  
			<ul class="product-list">
				<c:forEach items="${searchPageData.results}" var="product" varStatus="status">
					<product:productListerGridItem product="${product}" index="${status.index}"/>
				</c:forEach>
			</ul>
		</c:otherwise>
	</c:choose>
	<!-- INC144315908 ends -->
	<div id="addToCartTitle" style="display:none">
		<div class="add-to-cart-header">
			<div class="headline">
				<span class="headline-text"><spring:theme code="basket.added.to.basket"/></span>
			</div>
		</div>
	</div>
	</div>
<%-- UF-409 unnecessary code commented  <div class="bottom-pagination" style="display: none;">
	<nav:pagination top="false"  supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}"  searchPageData="${searchPageData}" searchUrl="${searchPageData.currentQuery.url}"  numberPagesShown="${numberPagesShown}"/>

</div> --%>

<div class="bottom-pagination">
<div class=""><span class=""><span>Total Pages :: ${searchPageData.pagination.numberOfPages}</span></span>
<c:choose>
<c:when test="${searchPageData.pagination.numberOfPages > 1}">
<div class=""><a href=""><span>Previous</span></a></div>
    <ul class="">
    	<c:forEach begin="1" end="${searchPageData.pagination.numberOfPages}" var="page">
        <li class="pageNoLi"><a class="pageNo" href="#nogo">${page}</a></li>
        </c:forEach>
    </ul>
    <div class=""><a href=""><span>Next</span></a></div>
</c:when>
<c:otherwise>
    <ul class="">
        <li class="pageNo"><a class="" href="#nogo">1</a></li>
    </ul>
</c:otherwise>
</c:choose>
</div>
</div>

<input type="hidden" name="searchPanel" value="1"/>
