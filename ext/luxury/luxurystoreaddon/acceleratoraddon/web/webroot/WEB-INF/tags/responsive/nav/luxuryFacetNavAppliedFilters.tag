<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageData" required="true" type="de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:url value="/search/getFacetData?searchCategory=${searchCategory}&text=${searchPageData.freeTextSearch}&q=${searchPageData.freeTextSearch}%3A" var="resetQueryUrl"/>
<div class="filter-box">
	<div class="filter-choosed">
		<h5 class="mb-20">Filtered by <span>${searchPageData.pagination.totalNumberOfResults} items found</span></h5>
	</div>
	<ul class="">
		<c:forEach items="${searchPageData.breadcrumbs}" var="breadcrumb">
			<li class="remove-filter" data-facetCode="${breadcrumb.facetValueCode}">
				<span class="filter-heading">${breadcrumb.facetName}</span>
				<span class="filter-name">${breadcrumb.facetValueName}</span>
			</li>
		</c:forEach>
	</ul>
	<input value="REMOVE FILTERS" title="REMOVE FILTERS" class="btn btn-default leftbar-btn reset-filters btn-block" data-resetQueryUrl="${resetQueryUrl}" />
</div>