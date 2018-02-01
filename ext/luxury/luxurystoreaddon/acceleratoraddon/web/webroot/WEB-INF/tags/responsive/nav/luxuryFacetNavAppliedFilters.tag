<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>	
<%@ attribute name="pageData" required="true" type="de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:url value="/page-0" var="queryUrl"/>
<c:url value="/search/?text=${searchPageData.freeTextSearch}&searchCategory=${searchCategory}" var="resetQueryUrlSearch"/>
<div class="filter-box">
	<div class="filter-choosed">
		<h5 class="mb-20">Filtered by <span>${searchPageData.pagination.totalNumberOfResults} items found</span></h5>
	</div>
	<ul class="">
		<c:forEach items="${searchPageData.breadcrumbs}" var="breadcrumb" varStatus="linkIndex">
			<c:set var="patternToReplace" value="/page-{pageNo}"/>
			<li class="remove-filter" data-facetCode="${breadcrumb.facetValueCode}" data-removeUrl="${fn:replace(breadcrumb.removeQuery.url,patternToReplace,queryUrl)}<c:if test="${not empty searchPageData.freeTextSearch}">&searchCategory=${searchCategory}</c:if>">
				<span class="filter-heading">${breadcrumb.facetName} : </span>
				<span class="filter-name">${breadcrumb.facetValueName}</span>
			</li>
			
			<c:if test="${linkIndex.index == 0 }">
				<c:url value="${fn:replace(breadcrumb.removeQuery.url,patternToReplace,'/')}" var="resetQueryUrl"/>
				<c:choose>
					<c:when test="${fn:contains(resetQueryUrl,'/search')}">
						<c:set var="resetQueryUrlUpdated" value="${resetQueryUrlSearch}"/>
					</c:when>
					<c:when test="${fn:contains(resetQueryUrl,'?')}">
						<c:set var="resetQueryUrlUpdated" value="${fn:substringBefore(resetQueryUrl, '?')}?"/>
					</c:when>
					<c:otherwise>
						<c:set var="resetQueryUrlUpdated" value="${resetQueryUrl}?"/>
					</c:otherwise>
				</c:choose>
			</c:if>
		</c:forEach>
	</ul>
	<input value="REMOVE FILTERS" title="REMOVE FILTERS" class="btn btn-default leftbar-btn reset-filters btn-block" data-resetQueryUrl="${resetQueryUrlUpdated}" />
</div>