<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageData" required="true" type="de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
	
<c:url value="/search?searchCategory=${searchCategory}&text=${searchPageData.freeTextSearch}" var="resetQueryUrl"/>
<div class="filter-box">
	<div class="filter-choosed">
		<h5 class="mb-20">Filtered by <span>${searchPageData.pagination.totalNumberOfResults} items found</span></h5>
	</div>
	<ul class="">
		<li><span class="filter-heading">Category :</span><span class="filter-name">Handbag</span></li>
		<li><span class="filter-heading">Category :</span><span class="filter-name">Shoes</span></li>
		<li><span class="filter-heading">Brand :</span><span class="filter-name">Brand1</span></li>
		<li><span class="filter-heading">Brand:</span><span class="filter-name">Armani</span></li>
		<li><span class="filter-heading">colors:</span><span class="filter-name">Red</span></li>
	</ul>
	<input value="REMOVE FILTERS" title="REMOVE FILTERS" class="btn btn-default leftbar-btn remove-filter btn-block" readonly>
</div>