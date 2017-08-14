<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageData" required="true" type="de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<script>

</script>

<input type="hidden" id="sizeCountForAppliedFilter" value="${sizeCount}">
<input type="hidden" id="searchQueryForAppliedFilter" value="${searchQueryValue}">
 
<c:if test="${not empty pageData.breadcrumbs}">
	<div class="facet js-facet">
	<%-- <c:url value="/search?searchCategory=${searchCategory}&text=${searchPageData.freeTextSearch}&resetAll=${true}" var="resetQueryUrl"/> --%>
    <c:choose>
    <c:when test="${not empty  newProduct}">
     <c:url value="/search/viewOnlineProducts" var="resetQueryUrl"/>
    </c:when>
    <c:otherwise>
     <c:url value="/search?searchCategory=${searchCategory}&text=${searchPageData.freeTextSearch}" var="resetQueryUrl"/>
    </c:otherwise>
    </c:choose>
   
   <!-- Commented out for 30th June CR implementation ends -->
   
	<%-- <c:set var="breadCrumbList" value="${pageData.breadcrumbs}" />
	
	<c:set var="breadCrumbSize" value="${fn:length(breadCrumbList)}" />
			<h3><span class="facet-name js-facet-name appliedFacets">FILTER BY</span>
			<a class="reset" href="${resetQueryUrl}" >RESET ALL</a></h3>
		<div class="facet-values js-facet-values">
			<ul class="facet-list">
				<c:forEach items="${pageData.breadcrumbs}" var="breadcrumb">
					<c:if test="${breadcrumb.facetName == 'inStockFlag'}">
						<li>
							<c:url value="${breadcrumb.removeQuery.url}&searchCategory=${searchCategory}" var="removeQueryUrl"/>
							Exclude OutofStock&nbsp;<a href="${removeQueryUrl}" ><span class="remove_filter"></span></a>
						</li>
					</c:if>
					<c:if test="${breadcrumb.facetName ne 'inStockFlag' && breadcrumb.facetName ne 'sellerId' &&  breadcrumb.facetName ne 'isOffersExisting' && breadcrumb.facetName ne 'promotedProduct'}">
						<li>
						   <c:choose>
						   <c:when test="${breadcrumb.removeQuery.url!='' && not empty offers}">
						   <c:set var="removeQueryUrl" value="${fn:replace(breadcrumb.removeQuery.url, 
                                'search', 'view-all-offers')}" />
                            <c:url value="${removeQueryUrl}&searchCategory=${searchCategory}" var="removeQueryUrl"/>
						   </c:when>
						   <c:when test="${breadcrumb.removeQuery.url!='' && not empty newProduct}">
						   <c:set var="removeQueryUrl" value="${fn:replace(breadcrumb.removeQuery.url, 
                                'search', 'search/viewOnlineProducts')}" />
                            <c:url value="${removeQueryUrl}&searchCategory=${searchCategory}" var="removeQueryUrl"/>
						   </c:when>
						   <c:otherwise>
						   <c:url value="${breadcrumb.removeQuery.url}&searchCategory=${searchCategory}" var="removeQueryUrl"/>
						   </c:otherwise>
						   </c:choose>
							<input type="hidden" class="applied-color" value="${breadcrumb.facetValueName}">
							${breadcrumb.facetValueName}&nbsp;<a href="${removeQueryUrl}" ><span class="remove_filter"></span></a>
						</li>
					</c:if>
				</c:forEach>
			</ul>
		</div> --%>		
		<!-- Commented out for 30th June CR implementation ends -->
		
	</div>

</c:if>
